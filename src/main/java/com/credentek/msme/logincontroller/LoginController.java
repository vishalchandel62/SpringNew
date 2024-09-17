package com.credentek.msme.logincontroller;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.credentek.msme.ApplicationGenParm.ApplicationGenParameter;
import com.credentek.msme.api.dao.AccountManagementDAO;
import com.credentek.msme.api.dao.CustomerManagementDAO;
import com.credentek.msme.logindao.LoginDaoImpl;
import com.credentek.msme.loginmodel.UserLogin;
import com.credentek.msme.loginservice.CustomerManagementDAOImpl;
import com.credentek.msme.loginservice.InboxManagementDAO;
import com.credentek.msme.loginservice.MakerCreationDaoImpl;
import com.credentek.msme.loginservice.NotificationManagementDAO;
import com.credentek.msme.loginservice.PasswordChecker;
import com.credentek.msme.maintenance.dao.UserLoginDAO;
import com.credentek.msme.maintenance.dao.UserLoginDaoImpl;
import com.credentek.msme.utils.AesUtil;
import com.credentek.msme.utils.Crypt;
import com.credentek.msme.utils.GeneralService;
import com.credentek.msme.utils.PathRead;
import com.credentek.msme.utils.VerifyRecaptcha;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@RestController
public class LoginController {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private UserLoginDaoImpl loginDaoimpl;
	
	@Autowired
	private LoginDaoImpl loginDaoImpl;
	
	@Autowired
	private GeneralService generalService;
	
	
	
	private static final Logger log = LoggerFactory.getLogger(LoginController.class);
	PathRead path = PathRead.getInstance();
	public String CONTENT_NOTE = ApplicationGenParameter.CONTENT_NOTE;// add for DPSC CR Content NOte
	public String CONTACT_US_DTL = ApplicationGenParameter.CONTACT_US_DTL;// add for TAT CR Contact_us NOte
	String shareKey = "";
	boolean shaTest = false;
	private String timeOutToken = "d@dd@IPOHONE143WALI@qwertasd";
	private boolean passReqToken = false;
	private boolean validSesFlag = false;
	private static String PKFP = "";// Public key file path
	UserLoginDAO userlogObj = new UserLoginDaoImpl(entityManager);
	private static final String SECURE_DATA = "d@dd@IPOHONE143WALI@qwertasd";

	@PostMapping("/SMEMCS005")
	public String processUserLogin(@RequestBody String request) {

		log.info("SMEMCS005 ==== userLogin call====="+request);
		net.sf.json.JSONObject ajaxJSONData = new net.sf.json.JSONObject();
		org.json.JSONObject reqData = new org.json.JSONObject(request);
		net.sf.json.JSONObject loginInfo = new net.sf.json.JSONObject();
		String encPass = "";
		UserLogin credential = new UserLogin();

		boolean eflag = false;
		boolean loginReq = false;
		boolean shaTest = false;
		boolean mFlag = false;
		String loginType = "";
		String data1 = "true";
		String requestMode = "", sessionID = "";

		StringBuffer tempToken = new StringBuffer(Integer.toHexString(this.hashCode()) + new Date().getTime());
		PKFP = !path.getPath("serverType").equals("UAT") ? path.getPath("LOCAL_PUBKEYPATH")
				: path.getPath("YES_PUBKEYPATH");

		if (reqData.has("requestMode")) {
			String requestMode1 = reqData.getString("requestMode").replaceAll(" ", "+");
			log.info("request mode value<<<<<<<<<<<<<<****");
			requestMode = new Crypt().decryptText(Crypt.getKey(), requestMode1, tempToken.toString(), PKFP);
		}

		if (reqData.has("LoginType")) {
			String loginType1 = reqData.getString("LoginType").replaceAll(" ", "+");
			log.info("request mode value<<<<<<<<<<<<<<****");
			loginType = new Crypt().decryptText(Crypt.getKey(), loginType1, tempToken.toString(), PKFP);
		}

		if (reqData.has("sessionID")) {
			String sessionID1 = reqData.getString("sessionID").replaceAll(" ", "+");
			sessionID = new Crypt().decryptText(Crypt.getKey(), sessionID1, tempToken.toString(), PKFP);
		}

		if (requestMode.trim().equals(ApplicationGenParameter.USER_MAKER)) {
			mFlag = true;
		}

		/*********************************************************
		 * 
		 * change encryption/decryption logic for Maker
		 * 
		 **********************************************************/
		if (reqData.has("data") && reqData.has("hash")) {
			log.info("call login if condition ******** ");
			data1 = validateLoginShaImpl(request, "");
			String sshval = sha1EncryptPass(data1);

			if (sshval.equals(reqData.getString("hash"))) {
				shaTest = true;
				loginInfo = parseEncDcrForMaker(request);
			}
		}
		shareKey = loginInfo.getString("shareKey");
		if (shaTest == false && data1 == "false") {
			log.info("Error " + path.getMsg("E116"));
			ajaxJSONData.clear();
			ajaxJSONData.put("ResFlag", "222");
			ajaxJSONData.put("ErrorCode", "E116");
			ajaxJSONData.put("Message", path.getMsg("E116"));
			ajaxJSONData.put("ResService", "SMEMCS005");
			String responseStr = encryptResponse(ajaxJSONData.toString(), shareKey);
			return responseStr;
		}

		if (ApplicationGenParameter.CAPTCHA_FLAG.equals("Y")) {

			if (loginType.equals(ApplicationGenParameter.LOGIN_TYPE_P)) {
				log.info("login Portal maker:::");
				boolean captchaFlag = false;
				try {
					if (reqData.has("captcha")) {
						String captchStr = reqData.get("captcha").toString();
						log.info(":::Captcha value::<<<<<<<<<<<<<::");
						VerifyRecaptcha vr = new VerifyRecaptcha();
						captchaFlag = vr.verify(captchStr);
					}
				} catch (Exception e1) {
					log.info("SQLException ===", e1);
				}

				if (captchaFlag == false) {
					log.info("Error ::captchaFlag::" + path.getMsg("E115"));
					ajaxJSONData.clear();
					ajaxJSONData.put("ResFlag", "201");
					ajaxJSONData.put("ErrorCode", "E120");
					ajaxJSONData.put("Message", path.getMsg("E120"));
					ajaxJSONData.put("ResService", "SMEMCS005");
					String responseStr = encryptResponse(ajaxJSONData.toString(), shareKey);
					log.info(">>>>>>>>>>>>>>::::2740");
					return responseStr;
				}
			} else if (loginType.equals(ApplicationGenParameter.LOGIN_TYPE_M)) {
				log.info("login mobile maker:::");
				if (reqData.has("captcha")) {
					log.info("Error " + path.getMsg("E115"));
					ajaxJSONData.clear();
					ajaxJSONData.put("ResFlag", "222");
					ajaxJSONData.put("ErrorCode", "E115");
					shareKey = shareKey + 1;
					ajaxJSONData.put("Message", path.getMsg("E115"));
					ajaxJSONData.put("ResService", "SMEMCS005");

					String responseStr = encryptResponse(ajaxJSONData.toString(), shareKey);
					return responseStr;
				}
			} else {
				if (loginType.equals(ApplicationGenParameter.LOGIN_TYPE_P)) {
					log.info("Error " + path.getMsg("E115"));
					ajaxJSONData.clear();
					ajaxJSONData.put("ResFlag", "201");
					ajaxJSONData.put("ErrorCode", "E118");
					ajaxJSONData.put("Message", path.getMsg("E118"));
					ajaxJSONData.put("ResService", "SMEMCS005");
					String responseStr = encryptResponse(ajaxJSONData.toString(), shareKey);
					log.info(">>>>>>>>>>>>>>::::2767");
					return responseStr;
				}

				log.info("Error " + path.getMsg("E115"));
				ajaxJSONData.clear();
				ajaxJSONData.put("ResFlag", "222");
				ajaxJSONData.put("ErrorCode", "E115");
				shareKey = shareKey + 1;
				ajaxJSONData.put("Message", path.getMsg("E115"));
				ajaxJSONData.put("ResService", "SMEMCS005");
				String responseStr = encryptResponse(ajaxJSONData.toString(), shareKey);
				return responseStr;
			}

		}
		if (!mFlag && loginType.equals(ApplicationGenParameter.LOGIN_TYPE_P)) {
			String cusID = loginDaoImpl.getCustIDForAlias(loginInfo.getString("loginID"));
			loginInfo.put("loginID", cusID);
		}

		String ipToken = reqData.getString("ipAddress").replaceAll(" ", "+");
		log.info("request mode value<<<<<<<<<<<<<<****");

		StringBuffer tempToken2 = new StringBuffer(Integer.toHexString(this.hashCode()) + new Date().getTime());
		PKFP = !path.getPath("serverType").equals("UAT") ? path.getPath("LOCAL_PUBKEYPATH")
				: path.getPath("YES_PUBKEYPATH");
		String ipReqToken = new Crypt().decryptText(Crypt.getKey(), ipToken, tempToken2.toString(), PKFP);

		log.info("ipAdress=");
		loginInfo.put("ipAddress", ipReqToken);
		loginInfo.put("requestFromFlag", "P");

		if (mFlag == true && reqData.has("firstToken")) {

			String requestToken = reqData.getString("firstToken").replaceAll(" ", "+");
			StringBuffer tempToken1 = new StringBuffer(Integer.toHexString(this.hashCode()) + new Date().getTime());
			PKFP = !path.getPath("serverType").equals("UAT") ? path.getPath("LOCAL_PUBKEYPATH")
					: path.getPath("YES_PUBKEYPATH");
			requestToken = new Crypt().decryptText(Crypt.getKey(), requestToken, tempToken1.toString(), PKFP);
			loginReq = loginDaoImpl.validateLoginRequest(requestToken);

			if (loginReq == true) {//false
				log.info("Error " + path.getMsg("E115"));
				ajaxJSONData.clear();
				ajaxJSONData.put("ResFlag", "222");
				ajaxJSONData.put("ErrorCode", "E115");
				shareKey = shareKey + 1;
				ajaxJSONData.put("Message", path.getMsg("E115"));
				ajaxJSONData.put("ResService", "SMEMCS005");
				String responseStr = encryptResponse(ajaxJSONData.toString(), shareKey);
				return responseStr;
			}
		}

		if (loginInfo.isEmpty()) {
			log.info("Error " + path.getMsg("E100"));
			ajaxJSONData.put("ResFlag", "000");
			ajaxJSONData.put("ErrorCode", "E100");
			ajaxJSONData.put("Message", path.getMsg("E100"));
		} else {

			if (loginInfo.size() > 0) {
				try {
					if (loginInfo.containsKey("loginID")) {
						String login_id = loginInfo.getString("loginID");
						if (!login_id.equals("null") && !login_id.equals("")) {
							credential.setLoginID(login_id);
						} else {
							eflag = true;
						}
					}
					if (loginInfo.containsKey("password")) {
						String pass = loginInfo.getString("password");
						if (!pass.equals("null") && !pass.equals("")) {
							encPass = sha1EncryptPass(pass);
							credential.setPassword(encPass);
						} else {
							eflag = true;
						}
					}

					if (eflag == true) {
						log.info("Error " + path.getMsg("E105"));
						ajaxJSONData.put("ResFlag", "000");
						ajaxJSONData.put("ErrorCode", "E105");
						ajaxJSONData.put("Message", path.getMsg("E105"));
					} else {
						log.info("Inside valid user and set Mapin Data");
						log.info("before <<<<");
						if (mFlag == true) {
							log.info("inside maker flag :::::$$$$$$$$$$$$$$$$$$$::");
							loginInfo.remove("password");
							loginInfo.put("password", encPass);
							String callType = "Mobile";
							String expiryMsg = path.getMsg("E124");
							net.sf.json.JSONObject resJson = loginDaoImpl.validUserForMaker(loginInfo);
							log.info("::maker response:<<<<<<<<<<<:::");



							if (resJson.get("STATUS").equals("SUCCESS")) {
								log.info("SUCCESS " + path.getMsg("S103"));
								if (loginType.equals(ApplicationGenParameter.LOGIN_TYPE_P)) {
									loginDaoImpl.insertSessionId(sessionID, loginInfo.getString("loginID").toUpperCase());
								}

								ajaxJSONData.put("ResFlag", "111");
								ajaxJSONData.put("CONTENT_NOTE", CONTENT_NOTE);//DPSC CR
								ajaxJSONData.put("CONTACT_US_DTL", CONTACT_US_DTL);	//TAT CR for Maker
								String tok = getToken();

								// change 141117
								boolean resTokenFlag = loginDaoImpl.updateTokenDB(getServiceName(new Object() {
								}.getClass().getEnclosingMethod().getName()), tok,
										loginInfo.getString("loginID").toUpperCase());
								if (!resTokenFlag) {
									ajaxJSONData.put("ResFlag", "222");
									ajaxJSONData.put("ErrorCode", "E107");
									ajaxJSONData.put("Message", path.getMsg("E107"));
									String responseStr = encryptResponse(ajaxJSONData.toString(), shareKey);
									return responseStr;
								}

								ajaxJSONData.put("resToken", tok);


								String[] GSTData = ApplicationGenParameter.GST_TAXDATA.split("\\|");
								net.sf.json.JSONObject GstJsonDtls = new 	net.sf.json.JSONObject();
								GstJsonDtls.put("beneficiaryAccountName", GSTData[0]);
								GstJsonDtls.put("beneficiaryBankName", GSTData[1]);
								GstJsonDtls.put("beneficiaryIfscCode", GSTData[2]);
								GstJsonDtls.put("beneficiaryGstRemark", GSTData[3]);

								net.sf.json.JSONObject custoffJson = new 	net.sf.json.JSONObject();
								custoffJson.put("rtgsCutoff", ApplicationGenParameter.RTGS_CUTOFF);
								custoffJson.put("neftCutOff", ApplicationGenParameter.NEFT_CUTOFF);
								GeneralService l_geneSerObj = new GeneralService(entityManager);
								custoffJson.put("holidayExist", l_geneSerObj.checkHoliday());
								custoffJson.put("GSTinfo", GstJsonDtls);
								boolean checkExp = new GeneralService(entityManager)
										.checkPassExpiry(loginInfo.getString("loginID").toUpperCase(), callType);
								if (checkExp) {
									net.sf.json.JSONObject obj =  resJson.getJSONObject("Message");
									obj.put("MakerStatus", "2");
									obj.put("MakerMsg", path.getMsg("E124"));
								}
								custoffJson.put("passExpiryFlag", checkExp);
								custoffJson.put("expiryMsg", expiryMsg);
								custoffJson.put("mappedCustomerID", loginInfo.getString("loginID"));
								custoffJson.put("mappedAliasID", l_geneSerObj.getAliasId(loginInfo.getString("loginID").toUpperCase()));

								ajaxJSONData.put("ruleParam", custoffJson);
								ajaxJSONData.put("Message", resJson.getString("Message"));

								//Added by Prabhat for audit activity details of customer on 23 Oct 2018
								net.sf.json.JSONObject auditJson = new net.sf.json.JSONObject();
								auditJson.put("serviceId", "SMEMCS005");
								auditJson.put("customerId", loginInfo.getString("loginID"));
								generalService.addCustomerAuditDtl(auditJson);

							} else {
								ajaxJSONData.put("ResFlag", "101");
								ajaxJSONData.put("Message", resJson.get("Message"));
								ajaxJSONData.put("token", resJson.get("token"));
							}
						} else if (requestMode.equals(ApplicationGenParameter.USER_CHECKER)) {

							log.info("INSIDE CHECKER @@@@@@@@@@@@@@@:::");
							String loginId = loginInfo.getString("loginID");
							if (!loginId.equals("null") && !loginId.equals("")) {
								credential.setRetailBankUserID(loginId);
							}
							String pass = loginInfo.getString("password");
							if (!pass.equals("null") && !pass.equals("")) {
								encPass = sha1EncryptPass(pass);
								credential.setPassword(encPass);
							}

							if (loginId.trim().matches("[0-9]+")) {
								boolean userExist = loginDaoImpl.checkCustomerExistInXREF(loginId.trim());
								if (!userExist) {
									log.info("Error " + path.getMsg("E122"));
									ajaxJSONData.put("ResFlag", "101");
									ajaxJSONData.put("Message", path.getMsg("E122"));
									ajaxJSONData.put("localReg", "X");
									String responseStr = encryptResponse(ajaxJSONData.toString(), shareKey);
									return responseStr;
								}
							}

							if (loginId.trim().matches("[0-9]+")) {
								boolean userDecExist = loginDaoImpl.checkCustomerDeactivationFlag(loginId.trim());
								if (userDecExist) {
									log.info("Error " + path.getMsg("E128"));
									ajaxJSONData.put("ResFlag", "101");
									ajaxJSONData.put("Message", path.getMsg("E128"));
									ajaxJSONData.put("localReg", "D");
									String responseStr = encryptResponse(ajaxJSONData.toString(), shareKey);
									return responseStr;
								}
							}

							net.sf.json.JSONObject resJson = null;
							// change 171117
							if (loginDaoImpl.userValidate(loginInfo.getString("loginID").toUpperCase(), "PORTAL")) {
								log.info("Custumer id valid for User.....");
								resJson = loginDaoImpl.validCheckerForPortal(loginInfo, credential);
							} else {

								ajaxJSONData.put("ResFlag", "101");
								ajaxJSONData.put("Message", path.getMsg("E142"));
								ajaxJSONData.put("localReg", "N");
								String responseStr = encryptResponse(ajaxJSONData.toString(), shareKey);
								return responseStr;
							}

							log.info("<:::::::::::validCheckerForPortal res::::::::::::::::");
							if (resJson.has("Portal_Pass") && resJson.getString("Portal_Pass").equals("Valid")) {
								log.info("local Portal password ******");
								log.info("SUCCESS " + path.getMsg("S103"));
								if (loginType.equals(ApplicationGenParameter.LOGIN_TYPE_P)) {
									loginDaoImpl.insertSessionId(sessionID, loginInfo.getString("loginID").toUpperCase());
								}

								String[] GSTData = ApplicationGenParameter.GST_TAXDATA.split("\\|");
								net.sf.json.JSONObject GstJsonDtls = new 	net.sf.json.JSONObject();
								GstJsonDtls.put("beneficiaryAccountName", GSTData[0]);
								GstJsonDtls.put("beneficiaryBankName", GSTData[1]);
								GstJsonDtls.put("beneficiaryIfscCode", GSTData[2]);
								GstJsonDtls.put("beneficiaryGstRemark", GSTData[3]);

								net.sf.json.JSONObject custoffJson = new 	net.sf.json.JSONObject();
								GeneralService l_geneSerObj = new GeneralService(entityManager);
								custoffJson.put("rtgsCutoff", ApplicationGenParameter.RTGS_CUTOFF);
								custoffJson.put("neftCutOff", ApplicationGenParameter.NEFT_CUTOFF);
								custoffJson.put("holidayExist", l_geneSerObj.checkHoliday());
								custoffJson.put("passExpiryFlag", l_geneSerObj.checkPassExpiry(loginInfo.getString("loginID").toUpperCase(), "Portal"));
								custoffJson.put("expiryMsg", path.getMsg("E124"));
								custoffJson.put("FRM_SECOND_AUTH_FLAG",resJson.get("FRM_SECOND_AUTH_FLAG"));//FRM Integeration by 2459 at 2023
								custoffJson.put("GSTinfo", GstJsonDtls);
								custoffJson.put("mappedCustomerID", loginInfo.getString("loginID"));
								custoffJson.put("mappedAliasID", l_geneSerObj.getAliasId(loginInfo.getString("loginID").toUpperCase()));
								ajaxJSONData.put("ResFlag", "111");
								ajaxJSONData.put("Message", resJson);
								ajaxJSONData.put("ruleParam", custoffJson);
								ajaxJSONData.put("checkerSerCode", loginDaoImpl
										.getServiceCodeForCustomer(loginInfo.getString("loginID").toUpperCase()));
								String tok = getToken();
								// change 141117
								boolean resTokenFlag = loginDaoImpl.updateTokenDB(getServiceName(new Object() {
								}.getClass().getEnclosingMethod().getName()), tok,
										loginInfo.getString("loginID").toUpperCase());
								if (!resTokenFlag) {//!resTokenFlag
									ajaxJSONData.put("ResFlag", "222");
									ajaxJSONData.put("ErrorCode", "E107");
									ajaxJSONData.put("Message", path.getMsg("E107"));
									String responseStr = encryptResponse(ajaxJSONData.toString(), shareKey);
									return ajaxJSONData.toString();
								}

								//Added by Prabhat for audit activity details of customer on 23 Oct 2018
								net.sf.json.JSONObject auditJson = new net.sf.json.JSONObject();
								auditJson.put("serviceId", "SMEMCS005");
								auditJson.put("customerId", loginInfo.getString("loginID"));
								generalService.addCustomerAuditDtl(auditJson);
								ajaxJSONData.put("resToken", tok);
								ajaxJSONData.put("ResFlag", "111");
								ajaxJSONData.put("token", resJson.get("token"));
								ajaxJSONData.put("CloginType", "local");
								ajaxJSONData.put("lastLogIn", resJson.get("lastLogIn"));
								log.info("final string:::");
								ajaxJSONData.put("ResService", "SMEMCS005");
								ajaxJSONData.put("CONTENT_NOTE", ApplicationGenParameter.CONTENT_NOTE);//DPSC CR
								ajaxJSONData.put("CONTACT_US_DTL", ApplicationGenParameter.CONTACT_US_DTL);//TAT CR for checker

								String responseStr = encryptResponse(ajaxJSONData.toString(), shareKey);
								return ajaxJSONData.toString();

							}
							else if(resJson.has("Portal_Pass") && resJson.getString("Portal_Pass").equals("WRONGOTP")) {//FRM Integeration by 2459 at 2023
								ajaxJSONData.put("ResFlag", "102");
								ajaxJSONData.put("Message", resJson.getString("Message"));
								ajaxJSONData.put("ResService", "SMEMCS005");
								log.info("WRONGOTP Portal login frm");
								String responseStr = encryptResponse(ajaxJSONData.toString(), shareKey);
								return responseStr;
							}
							else if (resJson.has("Portal_Pass")
									&& resJson.getString("Portal_Pass").equals("NotValid")) {

								ajaxJSONData.put("ResFlag", "101");
								ajaxJSONData.put("Message", resJson.getString("Message"));
								if (resJson.has("localReg")) {
									ajaxJSONData.put("localReg", resJson.getString("localReg"));
								}
								ajaxJSONData.put("ResService", "SMEMCS005");
								log.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$12");
								String responseStr = encryptResponse(ajaxJSONData.toString(), shareKey);
								return responseStr;
							}

							if (resJson.has("ResBody")&& resJson.getJSONObject("ResBody").getString("Status").equals("SUCCESS")) {
								log.info("SUCCESS " + path.getMsg("S103"));
								net.sf.json.JSONObject custoffJson = new 	net.sf.json.JSONObject();
								if (loginType.equals(ApplicationGenParameter.LOGIN_TYPE_P)) {
									loginDaoImpl.insertSessionId(sessionID, loginInfo.getString("loginID").toUpperCase());
								}

								String[] GSTData = ApplicationGenParameter.GST_TAXDATA.split("\\|");
								net.sf.json.JSONObject GstJsonDtls = new 	net.sf.json.JSONObject();
								GstJsonDtls.put("beneficiaryAccountName", GSTData[0]);
								GstJsonDtls.put("beneficiaryBankName", GSTData[1]);
								GstJsonDtls.put("beneficiaryIfscCode", GSTData[2]);
								GstJsonDtls.put("beneficiaryGstRemark", GSTData[3]);
								GeneralService l_geneSerObj = new GeneralService(entityManager);
								custoffJson.put("GSTinfo", GstJsonDtls);
								custoffJson.put("rtgsCutoff", ApplicationGenParameter.RTGS_CUTOFF);
								custoffJson.put("neftCutOff", ApplicationGenParameter.NEFT_CUTOFF);
								custoffJson.put("holidayExist", l_geneSerObj.checkHoliday());
								custoffJson.put("passExpiryFlag", l_geneSerObj
										.checkPassExpiry(loginInfo.getString("loginID").toUpperCase(), "Portal"));
								custoffJson.put("expiryMsg", path.getMsg("E124"));
								custoffJson.put("mappedCustomerID", loginInfo.getString("loginID"));
								custoffJson.put("mappedAliasID", l_geneSerObj.getAliasId(loginInfo.getString("loginID").toUpperCase()));
								ajaxJSONData.put("ResFlag", "111");
								ajaxJSONData.put("Message", path.getMsg("S103"));
								ajaxJSONData.put("ruleParam", custoffJson);
								ajaxJSONData.put("checkerSerCode", loginDaoImpl.getServiceCodeForCustomer(loginInfo.getString("loginID").toUpperCase()));
								String tok = getToken();
								boolean resTokenFlag = loginDaoImpl.updateTokenDB(getServiceName(new Object() {
								}.getClass().getEnclosingMethod().getName()), tok,
										loginInfo.getString("loginID").toUpperCase());
								if (!resTokenFlag) {
									ajaxJSONData.put("ResFlag", "222");
									ajaxJSONData.put("ErrorCode", "E107");
									ajaxJSONData.put("Message", path.getMsg("E107"));
									String responseStr = encryptResponse(ajaxJSONData.toString(), shareKey);
									return responseStr;
								}
								ajaxJSONData.put("resToken", tok);
								ajaxJSONData.put("ResFlag", "111");
								ajaxJSONData.put("token", resJson.get("token"));
								ajaxJSONData.put("lastLogIn", resJson.get("lastLogIn"));
								ajaxJSONData.put("localReg", "N");
								log.info("final string:::3131");

								//Added by Prabhat for audit activity details of customer on 23 Oct 2018
								net.sf.json.JSONObject auditJson = new net.sf.json.JSONObject();
								auditJson.put("serviceId", "SMEMCS005");
								auditJson.put("customerId", loginInfo.getString("loginID"));
								generalService.addCustomerAuditDtl(auditJson);

							} else if (resJson.has("ErrorDetails")) {
								ajaxJSONData.put("ResFlag", "101");
								ajaxJSONData.put("Message", resJson.get("ErrorDetails"));
							}
						} else {
							ajaxJSONData.put("ResFlag", "000");
							ajaxJSONData.put("ErrorCode", "S104");
							ajaxJSONData.put("Message", path.getMsg("S104"));
						}
					}

				} catch (Exception e) {
					ajaxJSONData.put("ResFlag", "000");
					ajaxJSONData.put("ErrorCode", "E102");
					ajaxJSONData.put("Message", path.getMsg("E102"));
					log.info("Error log >>>>", e);
				}
			}
		}
		log.info("End #:Final Response::::::SMEMCS005 "+ajaxJSONData);
		ajaxJSONData.put("ResService", "SMEMCS005");
		String responseStr = encryptResponse(ajaxJSONData.toString(), shareKey);


		log.info("Final Response::::::SMEMCS005 "+	ajaxJSONData.toString());

		return ajaxJSONData.toString();
	}

	public String encryptResponse(String data, String KEY) {
		log.info(" in encryptResponse function ");
		String resJsonString = "";
		AesUtil aesUtil = new AesUtil();
		if (KEY != null && !"".equals(KEY)) {

		} else {
			KEY = "NA";
		}
		resJsonString = aesUtil.encrypt(KEY, data);
		return resJsonString;
	}

	public static String sha1EncryptPass(String input) {
		log.info("Inside sha1EncryptPass of crypt JAVA");
		StringBuffer sb = new StringBuffer();
		MessageDigest mDigest;
		try {
			mDigest = MessageDigest.getInstance("SHA1");
			byte[] result = mDigest.digest(input.getBytes());
			for (int i = 0; i < result.length; i++) {
				sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
			}
		} catch (NoSuchAlgorithmException e) {
			// e.printStackTrace();
			log.info("NoSuchAlgorithmException :::", e);
		}
		return sb.toString();
	}

	public String getServiceName(String methodName) {
		log.info(" in getServiceName function::");

		Method m;
		String serviceName = "SMEMCS005";
		try {
			m = this.getClass().getMethod(methodName, String.class);
			// serviceName = m.getAnnotation(Path.class).value();

			serviceName = serviceName.substring(1);
		} catch (NoSuchMethodException | SecurityException e) {
			log.info("Error " + e);
		}

		return serviceName;
	}

	public String getToken() {

		log.info(" in getToken function");
		StringBuffer tempToken = new StringBuffer(Integer.toHexString(this.hashCode()) + System.nanoTime());

		return tempToken.toString();
	}

	public String validateLoginShaImpl(String request, String type) {
		log.info("in validateLoginShaImpl function " + type + " ........request .......");
		net.sf.json.JSONObject resJson = null;
		String SECURE_DATA = "d@dd@IPOHONE143WALI@qwertasd";

		AesUtil aesUtil = new AesUtil();
		String finalKey = "";
		String loginVal = "";
		String passval = "";
		String firstFChar = "";
		try {
			if (request != null && !request.equals("")) {
				log.info("in If condition");
				org.json.JSONObject reqData1 = new org.json.JSONObject(request);

				String data = reqData1.getString("data").replaceAll(" ", "+");
				String token1 = reqData1.getString("token").replaceAll(" ", "+");
				org.json.JSONObject reqData = new org.json.JSONObject();

				reqData.put("data", data);
				reqData.put("token", token1);

				String token = reqData.getString("token");
				log.info("token from UI............");
				StringBuffer tempToken = new StringBuffer(Integer.toHexString(this.hashCode()) + new Date().getTime());
				String PKFP = !path.getPath("serverType").equals("UAT") ? path.getPath("LOCAL_PUBKEYPATH")
						: path.getPath("YES_PUBKEYPATH");

				log.info("in If condition tempToken");
				String key = "";
				String l_shareKey = "";
				if ("checker".equals(type)) {
					String decToken = new Crypt().decryptText(Crypt.getKey(), token.toString(), tempToken.toString(),
							PKFP);
					log.info("token from UI dec............");
					l_shareKey = new GeneralService(entityManager).getShareKey(decToken);
					if (l_shareKey != null && !l_shareKey.equals("")) {
						key = new Crypt().decryptText(Crypt.getKey(), l_shareKey.toString(), tempToken.toString(),
								PKFP);
					} else {
						log.info("in If condition l_shareKey NA");
						key = "NA";
					}
				} else {
					key = SECURE_DATA
							+ new Crypt().decryptText(Crypt.getKey(), token.toString(), tempToken.toString(), PKFP);
				}

				log.info("original key.....");

				String lgnType = reqData1.getString("LoginType").replaceAll(" ", "+");
				String loginType = new Crypt().decryptText(Crypt.getKey(), lgnType, tempToken.toString(), PKFP).trim();
				if (reqData1.has("checkKey") && reqData1.has("LoginType")) {
					String IMEI = reqData1.getString("IMEI").replaceAll(" ", "+");

					StringBuffer IMEITempToken1 = new StringBuffer(
							Integer.toHexString(this.hashCode()) + new Date().getTime());
					String IMEINO = new Crypt().decryptText(Crypt.getKey(), IMEI, IMEITempToken1.toString(), PKFP);

					if (loginType.equals(ApplicationGenParameter.LOGIN_TYPE_M)) {
						String uiShareKey = reqData1.getString("checkKey").replaceAll(" ", "+");
						log.info("uiShareKey key.....");
						StringBuffer uiShareKeyTempToken1 = new StringBuffer(
								Integer.toHexString(this.hashCode()) + new Date().getTime());
						String uiShareKeyToken = new Crypt().decryptText(Crypt.getKey(), uiShareKey,
								uiShareKeyTempToken1.toString(), PKFP);
						log.info("uishare key..............");
						if (key.equals(uiShareKeyToken)) {
							// No operation needed here
						} else {
							if (key.contains(IMEINO) && uiShareKeyToken.contains(IMEINO)) {
								key = loginDaoimpl.validateShareKeyForVersoionIssue(IMEINO, uiShareKeyToken,
										uiShareKey);
								log.info("after updation key............");
							}
						}
					}
				}

				String resJsonString = aesUtil.decrypt(key, reqData.getString("data"));
				log.info("in If condition l_shareKey resJsonString");
				net.sf.json.JSONObject temp = net.sf.json.JSONObject.fromObject(resJsonString);
				log.info("in If condition l_shareKey temp");
				resJson = temp.getJSONObject("reqData");

				loginVal = resJson.getString("loginID");
				passval = resJson.getString("password");

				log.info("in If condition l_shareKey type else");

				firstFChar = data.substring(0, 4);
				finalKey = loginVal + passval + firstFChar;
				log.info("final String value @@@@@@@@@@::<<<<:::");
				return finalKey;
			}
		} catch (Exception e) {
			log.error("Exception in validateLoginShaImpl", e);
			finalKey = "false";
		}

		return finalKey;
	}

	public net.sf.json.JSONObject parseEncDcrForMaker(String request) throws JSONException {
		log.info("parseEncDcrForMaker=====");

		net.sf.json.JSONObject resJson = new net.sf.json.JSONObject();
		String SECURE_DATA = "d@dd@IPOHONE143WALI@qwertasd";
		AesUtil aesUtil = new AesUtil();
		if (request != null && !request.equals("")) {
			org.json.JSONObject reqData1 = new org.json.JSONObject(request);

			String data = reqData1.getString("data").replaceAll(" ", "+");
			String requestMode = reqData1.getString("requestMode").replaceAll(" ", "+");
			String token1 = reqData1.getString("token").replaceAll(" ", "+");

			org.json.JSONObject reqData = new org.json.JSONObject();

			reqData.put("data", data);
			reqData.put("requestMode", requestMode);
			reqData.put("token", token1);

			String token = reqData.getString("token");

			StringBuffer tempToken = new StringBuffer(Integer.toHexString(this.hashCode()) + new Date().getTime());
			PKFP = !path.getPath("serverType").equals("UAT") ? path.getPath("LOCAL_PUBKEYPATH")
					: path.getPath("YES_PUBKEYPATH");

			String key = SECURE_DATA
					+ new Crypt().decryptText(Crypt.getKey(), token.toString(), tempToken.toString(), PKFP);
			String resJsonString = aesUtil.decrypt(key, reqData.getString("data"));
			net.sf.json.JSONObject temp = net.sf.json.JSONObject.fromObject(resJsonString);
			resJson = temp.getJSONObject("reqData");
			resJson.put("shareKey", key);
			return resJson;
		} else {
			resJson.put("shareKey", shareKey);
			return resJson;
		}

	}
	@PostMapping("/MCSC0000")
	public String processCheckLoginExist(@RequestBody String request)
	{

		log.info("MCSC0000 ==== checkLoginExitUpdated call=====");
		org.json.JSONObject reqData = null;

		UserLoginDAO userinfo = null;
		CustomerManagementDAO custInfo = null;
		boolean exitUser = false;
		boolean exitImei = false;
		String token = "";
		String deviceImei = "";
		String userId = "";
		//String shareKey = "";
		boolean reqDataFlag = false;
		net.sf.json.JSONObject ajaxJSONData = new net.sf.json.JSONObject();
		net.sf.json.JSONObject responseJson = new net.sf.json.JSONObject();
		UserLoginDaoImpl loginObj = new UserLoginDaoImpl(entityManager);
		GeneralService generalService = new GeneralService(entityManager);
		try {// try Start

			reqData = new org.json.JSONObject(request);
			if (reqData.length() == 0) {
				log.info("Error " + path.getMsg("E100"));
				ajaxJSONData.put("ResFlag", "000");
				ajaxJSONData.put("ResService", "MCSC0000");
				ajaxJSONData.put("ErrorCode", "E100");
				ajaxJSONData.put("Message", path.getMsg("E100"));
			} else {// Start else 1

				// need to initialise here
				custInfo = new CustomerManagementDAO();
				String objRefernce = this.toString();
				StringBuffer bufferToken = new StringBuffer(
						objRefernce.substring(objRefernce.indexOf("@") + 1, objRefernce.length()));
				// If reqData has json value
				userinfo = new UserLoginDaoImpl(entityManager);
				StringBuffer tempToken = new StringBuffer(Integer.toHexString(this.hashCode()) + new Date().getTime());
				PKFP = !path.getPath("serverType").equals("UAT") ? path.getPath("LOCAL_PUBKEYPATH")
						: path.getPath("YES_PUBKEYPATH");

				if (reqData.has("IMEI")) {

					deviceImei = new Crypt().decryptText(Crypt.getKey(), reqData.getString("IMEI"),
							tempToken.toString(), PKFP);
					reqDataFlag = true;
				}
				if (reqData.has("userName")) {
					userId = new Crypt().decryptText(Crypt.getKey(), reqData.getString("userName"),
							tempToken.toString(), PKFP);
					reqDataFlag = true;
				}
				if (reqData.has("KEY")) {
					reqDataFlag = true;
				}

				if (!reqDataFlag) {
					log.info("Error " + path.getMsg("E101"));
					ajaxJSONData.put("ResFlag", "000");
					ajaxJSONData.put("ResService", "MCSC0000");
					ajaxJSONData.put("ErrorCode", "E100");
					ajaxJSONData.put("Message", path.getMsg("E101"));
				}

				if (userId.trim().matches("[0-9]+")) {
					boolean userExist = userinfo.checkCustomerExistInXREF(userId.trim());
					if (!userExist) {
						log.info("Error " + path.getMsg("E122"));
						ajaxJSONData.put("ResFlag", "000");
						ajaxJSONData.put("ResService", "MCSC0000");
						ajaxJSONData.put("ErrorCode", "E122");
						ajaxJSONData.put("Message", path.getMsg("E122"));
						return ajaxJSONData.toString();
					}
				}

				if (userId.trim().matches("[0-9]+")) {
					boolean userDecExist =loginDaoImpl.checkCustomerDeactivationFlag(userId.trim());
					if (userDecExist) {
						log.info("Error " + path.getMsg("E128"));
						ajaxJSONData.put("ResFlag", "000");
						ajaxJSONData.put("ResService", "MCSC0000");
						ajaxJSONData.put("ErrorCode", "E128");
						ajaxJSONData.put("Message", path.getMsg("E128"));
						return ajaxJSONData.toString();
					}
				}
				
				// Check Customer Block by swapnil on sep-26
				boolean custValidFlag = loginObj.blockOrLock(userId.trim());
				if (custValidFlag) {
					log.info("Error " + path.getMsg("BL009"));
					ajaxJSONData.put("ResFlag", "000");
					ajaxJSONData.put("ResService", "MCSC0000");
					ajaxJSONData.put("ErrorCode", "BL009");
					ajaxJSONData.put("Message", path.getMsg("BL009"));
					return ajaxJSONData.toString();
				}
				//end

				// Check device IMEI Exits in PUBKEY TABle mapping

				exitImei = userinfo.imeiExitCheckInPubkeyTable(deviceImei.trim());
				if (exitImei) {

					if (!userId.trim().matches("[0-9]+")) {
						userId = custInfo.setCustomerAliasBaneMappingExitInTable(userId);
						log.info("setCustomerAliasBaneMappingExitInTable :");
					}
					loginDaoimpl.deleteTempTableOfUser(userId.trim(), deviceImei.trim());
					// If IMEI exist then go for Login Table for (IMEI+USERID)
					// check
					exitUser = userinfo.userBindWithMobCheck(userId.trim(), deviceImei.trim());
					if (exitUser) {
						log.info("exitUser True");
						JSONObject r_tempJson = new JSONObject();
					
						token = bufferToken.toString();
						loginDaoimpl.updateToken(userId, token);
						log.info("updateToken");

						ajaxJSONData.put("ResFlag", "111");
						ajaxJSONData.put("ResService", "MCSC0000");
						r_tempJson.put("LoginStatus", exitUser);
						r_tempJson.put("token", token);
						r_tempJson.put("deviceStatus", "self");
						responseJson.put("ResBody", r_tempJson);
						ajaxJSONData.put("Message", responseJson);

					} else {
						log.info("exituser false");

						if (!userId.trim().matches("[0-9]+")) {
							userId = custInfo.setCustomerAliasBaneMappingExitInTable(userId);
						}

						exitUser = userinfo.userExitCheck(userId);
						if (exitUser) {

							log.info("else exituser true");
							token = bufferToken.toString();
							String KEY = reqData.getString("KEY").toString();
							log.info("KEY found");
							generalService.insertKeyForIMEI(deviceImei, KEY, token);
							log.info("After calling insertKeyForIMEI");
							// log.info(" KEY Generated in temp table of
							// SME_IMEI_PUBKEY_ENTRY_TMP");

							ajaxJSONData.put("ResFlag", "000");
							ajaxJSONData.put("ResService", "MCSC0000");
							ajaxJSONData.put("ErrorCode", "E109");
							ajaxJSONData.put("Message", path.getMsg("E109"));
							ajaxJSONData.put("token", token);
							ajaxJSONData.put("deviceStatus", "NA");
						} else {
							// This device is registered with another userid if
							// yes go for Deregistration process

							log.info("else exituser false");
							token = bufferToken.toString();
							String KEY = reqData.getString("KEY").toString();
							log.info("KEY ===generated");
							generalService.insertKeyForIMEI(deviceImei, KEY, token);
							log.info("token is ===insertKeyForIMEI ");

							ajaxJSONData.put("ResFlag", "000");
							ajaxJSONData.put("ResService", "MCSC0000");
							ajaxJSONData.put("ErrorCode", "E110");
							ajaxJSONData.put("Message", path.getMsg("E110"));
							ajaxJSONData.put("token", token);
							ajaxJSONData.put("deviceStatus", "NA");
						}
					}
				} else {
					log.info("in else ");
					// Else check userid exist or not

					if (!userId.trim().matches("[0-9]+")) {
						userId = custInfo.setCustomerAliasBaneMappingExitInTable(userId);
					}
					exitUser = userinfo.userExitCheck(userId);
					if (exitUser) {
						userinfo.deleteTempTableOfUser(userId, deviceImei);
						log.info("After deleteTempTableOfUser");
					
						token = bufferToken.toString();
						String KEY = reqData.getString("KEY").toString();

						generalService.insertKeyForIMEI(deviceImei, KEY, token);
						log.info("after insertKeyForIMEI");

						ajaxJSONData.put("ResFlag", "000");
						ajaxJSONData.put("ResService", "MCSC0000");
						ajaxJSONData.put("ErrorCode", "E109");
						ajaxJSONData.put("Message", path.getMsg("E109"));
						ajaxJSONData.put("token", token);
						ajaxJSONData.put("deviceStatus", "NA");
					} else {
						JSONObject r_tempJson = new JSONObject();

						log.info("in exituser true");
						token = bufferToken.toString();
						String KEY = reqData.getString("KEY").toString();
						generalService.insertKeyForIMEI(deviceImei, KEY, token);
						log.info("after insertKeyForIMEI");
					
						ajaxJSONData.put("ResFlag", "111");
						ajaxJSONData.put("ResService", "MCSC0000");
						r_tempJson.put("LoginStatus", exitUser);
						r_tempJson.put("token", token);
						r_tempJson.put("deviceStatus", "NA");
						responseJson.put("ResBody", r_tempJson);
						ajaxJSONData.put("Message", responseJson);
					}
				}
				
				ajaxJSONData.put("SMBND", ApplicationGenParameter.SMBND);
				ajaxJSONData.put("SMBVMN", ApplicationGenParameter.SMBVMN);
				ajaxJSONData.put("SMBFRQTM", ApplicationGenParameter.SMBFRQTM);

			} // End else 1

		} // End Try
		catch (JSONException je) {
			ajaxJSONData.put("ResFlag", "000");
			ajaxJSONData.put("ResService", "MCSC0000");
			ajaxJSONData.put("ErrorCode", "E102");
			ajaxJSONData.put("Message", path.getMsg("E102"));
			log.info("JSONException log >>>>", je);
		} catch (Exception ee) {
			ajaxJSONData.put("ResFlag", "000");
			ajaxJSONData.put("ResService", "MCSC0000");
			ajaxJSONData.put("ErrorCode", "E102");
			ajaxJSONData.put("Message", path.getMsg("E102"));
			log.info("Exception log >>>>", ee);
		}
		if (ajaxJSONData.has("MPIN")) {
			ajaxJSONData.remove("MPIN");
		}
		if (ajaxJSONData.has("password")) {
			ajaxJSONData.remove("password");
		}

		log.info("End of MCSC0000");
		return ajaxJSONData.toString();
	
	}
	
	@PostMapping("/SMEMCS008")
	public String CheckCustBlockUnblockFlow(@RequestBody String request)
	{
		log.info("SMEMCS008 ==== CheckCustomerBlockUnblockFlow call=====");
		net.sf.json.JSONObject ajaxJSONData = new net.sf.json.JSONObject();
		net.sf.json.JSONObject flowJson = new net.sf.json.JSONObject();
		org.json.JSONObject reqData = new org.json.JSONObject(request);
		net.sf.json.JSONObject custInfo = new 	net.sf.json.JSONObject();
		net.sf.json.JSONObject resJson = new 	net.sf.json.JSONObject();
		UserLoginDAO mpinCntObj = new UserLoginDaoImpl(entityManager);
		
		//GeneralService generalService = new GeneralService(entityManager);
		
		CustomerManagementDAOImpl custFlowInfo = new CustomerManagementDAOImpl(entityManager);
		String loginType = "";
		String requestMode = "";
		String custId = "";
		String userType = "";
		try {
			StringBuffer tempToken = new StringBuffer(Integer.toHexString(this.hashCode()) + new Date().getTime());
			PKFP = !path.getPath("serverType").equals("UAT") ? path.getPath("LOCAL_PUBKEYPATH")
					: path.getPath("YES_PUBKEYPATH");

			log.info("Call method::::::::<<<<<<<<<<<<<<<<<<<<<<<<<<<<::::::::");
			if (reqData.length() == 0) {
				log.info("Error " + path.getMsg("E100"));
				ajaxJSONData.put("ResFlag", "000");
				ajaxJSONData.put("ResBody", "ERROR");
				ajaxJSONData.put("ResService", "SMEMCS008");
				ajaxJSONData.put("ErrorCode", "E100");
				ajaxJSONData.put("Message", path.getMsg("E100"));
			} else {
				if (reqData.has("LoginType")) {
					String lgnType = reqData.getString("LoginType").replaceAll(" ", "+");
					loginType = new Crypt().decryptText(Crypt.getKey(), lgnType, tempToken.toString(), PKFP).trim();
				}

				if (reqData.has("requestMode")) {
					String requestMode1 = reqData.getString("requestMode").replaceAll(" ", "+");
					log.info("request mode value<<<<<<<<<<<<<<****");
					requestMode = new Crypt().decryptText(Crypt.getKey(), requestMode1, tempToken.toString(), PKFP);
				}

				if (reqData.has("reqData")) {
					custInfo = net.sf.json.JSONObject.fromObject(reqData.get("reqData").toString());
				} else {
					if (loginType.equals(ApplicationGenParameter.LOGIN_TYPE_P)) {
						custInfo = userAuthentication(reqData, tempToken);
					} else if (loginType.equals(ApplicationGenParameter.LOGIN_TYPE_M)) {
						shaTest = checkSHA(reqData.toString());
						if (shaTest) {
							custInfo = parseEncDcr(reqData.toString());
							if (custInfo.containsKey("ResFlag")) {
								ajaxJSONData = custInfo;
								ajaxJSONData.put("ResService", "SMEMCS008");
								String responseStr = encryptResponse(ajaxJSONData.toString(), shareKey);
								log.info("responseStr ");
								return responseStr;
							}
						}
					} else {
						log.info("Error ******* " + path.getMsg("E100"));
						ajaxJSONData.put("ResFlag", "000");
						ajaxJSONData.put("ResBody", "ERROR");
						ajaxJSONData.put("ResService", "SMEMCS008");
						ajaxJSONData.put("ErrorCode", "E100");
						ajaxJSONData.put("Message", path.getMsg("E100"));
					}
				}

				shareKey = custInfo.getString("shareKey");

				ajaxJSONData = new net.sf.json.JSONObject();
				String[] checkManKey = { "CustomerId" };
				custId = custInfo.getString("CustomerId").trim();
				userType = custInfo.getString("UserType").trim();

				// For User Validation
				// get customer ID from alias id if user insert alias ID at time of forgot password/Unblock password
				if (!custId.trim().matches("[0-9]+")) {
					custId = new CustomerManagementDAO().setCustomerAliasBaneMappingExitInTable(custId);
					log.info("setCustomerAliasBaneMappingExitInTable :"+custId);
				}
				boolean validFlag = mpinCntObj.userValidOrNot(custId, requestMode);
				if (requestMode.equals("M") && validFlag == true) {
					if (userType.equals("F")) {
						ajaxJSONData.put("ResFlag", "000");
						ajaxJSONData.put("ResService", "SMEMCS008");
						ajaxJSONData.put("ResBody", "SUCCESS");
						String responseStr = encryptResponse(ajaxJSONData.toString(), shareKey);
						return responseStr;
					} else if (userType.equals("UB")) {
						log.info("Error ********* " + path.getMsg("UNBL001"));
						ajaxJSONData.clear();
						ajaxJSONData.put("ResFlag", "000");
						ajaxJSONData.put("ResBody", "ERROR");
						ajaxJSONData.put("ResService", "SMEMCS008");
						ajaxJSONData.put("ErrorCode", "UNBL001");
						ajaxJSONData.put("Message", path.getMsg("UNBL001"));
						String responseStr = encryptResponse(ajaxJSONData.toString(), shareKey);
						return responseStr;
					}
				}
				if (!validFlag) {
					log.info("Error ******* " + path.getMsg("UNBL001"));
					ajaxJSONData.clear();
					ajaxJSONData.put("ResFlag", "000");
					ajaxJSONData.put("ResBody", "ERROR");
					ajaxJSONData.put("ResService", "SMEMCS008");
					ajaxJSONData.put("ErrorCode", "UNBL001");
					ajaxJSONData.put("Message", path.getMsg("UNBL001"));
					String responseStr = encryptResponse(ajaxJSONData.toString(), shareKey);
					return responseStr;
				}

				int i = 0;
				while (i < checkManKey.length) {

					if (custInfo.containsKey(checkManKey[i]) && !custInfo.get(checkManKey[i]).equals("")) {
						i++;
					} else {
						log.info("In else ===" + i);
						i = -i;
						break;
					}
				}

				if (i == -1) {
					log.info("Error ******* " + path.getMsg("E134"));
					ajaxJSONData.put("ResFlag", "000");
					ajaxJSONData.put("ResBody", "ERROR");
					ajaxJSONData.put("ErrorCode", "E134");
					ajaxJSONData.put("Message", path.getMsg("E134"));

				} else {
					log.info("::inside else condition:::");

					flowJson = custFlowInfo.handleBlockUnblockWorkFlow(custId, userType, requestMode);
					if (!flowJson.getString("FlowFlag").equals("true")) {
						log.info("Inside User Avoid On Wrong Click ...");
						log.info("Error ******* " + flowJson.getString("Message"));
						ajaxJSONData.put("ResFlag", "000");
						ajaxJSONData.put("ResBody", "ERROR");
						ajaxJSONData.put("ResService", "SMEMCS008");
						ajaxJSONData.put("ErrorCode", flowJson.getString("ErrorCode"));
						ajaxJSONData.put("Message", flowJson.getString("Message"));
						String responseStr = encryptResponse(ajaxJSONData.toString(), shareKey);
						return responseStr;
					}
					log.info("AvoidFlag is ****************  " + flowJson.getString("FlowFlag"));
					log.info("SUCCESS *********************  " + "Validation Successfully Done For Next Flow");

					resJson.put("ResCode", "0");
					resJson.put("ResBody", "SUCCESS");
					ajaxJSONData.put("ResFlag", "111");
					ajaxJSONData.put("Message", resJson);

					net.sf.json.JSONObject auditJson = new net.sf.json.JSONObject();
					auditJson.put("serviceId", "SMEMCS008");
					auditJson.put("customerId", custId);
					generalService.addCustomerAuditDtl(auditJson);
				}
			}
		} catch (JSONException e1) {
			ajaxJSONData.put("ResFlag", "000");
			ajaxJSONData.put("ResService", "SMEMCS008");
			ajaxJSONData.put("ErrorCode", "E102");
			ajaxJSONData.put("Message", path.getMsg("E102"));
			log.info("JSONException log >>>>", e1);
		} catch (net.sf.json.JSONException e2) {
			log.info("net.sf.json.JSONException :::", e2);
		} catch (Exception e) {
			ajaxJSONData.put("ResFlag", "000");
			ajaxJSONData.put("ResService", "SMEMCS008");
			ajaxJSONData.put("ErrorCode", "E102");
			ajaxJSONData.put("Message", path.getMsg("E102"));
			log.info("Exception :::", e);
		} 
		String responseStr = encryptResponse(ajaxJSONData.toString(), shareKey);
		log.info("End SMEMCS008 ==== CheckCustomerBlockUnblockFlow call=====");
		return ajaxJSONData.toString();
		
	}
	
	@PostMapping("SMEMCS006")
     public String changePasswordForMaker(@RequestBody String request)
     { 
		log.info("SMEMCS006 ==== changePasswordForMaker call=====");
		net.sf.json.JSONObject ajaxJSONData = new net.sf.json.JSONObject();
		net.sf.json.JSONObject loginInfo = parseEncDcrForMaker(request);
		UserLoginDaoImpl userinfo = null;
		PasswordChecker passObj = null;
		String encPass = "";
		int i = 0;

		shareKey = loginInfo.getString("shareKey");
		String[] checkManKey = new String[] { "loginID", "password" };
		validSesFlag = true;
		
		if (validSesFlag == false) {
			log.info("Error " + path.getMsg("E117").replace("<>", ApplicationGenParameter.SESSION_TIME));
			ajaxJSONData.clear();
			ajaxJSONData.put("ResFlag", "222");
			ajaxJSONData.put("ErrorCode", "E117");
			ajaxJSONData.put("Message", path.getMsg("E117").replace("<>", ApplicationGenParameter.SESSION_TIME));
		} else {
			userinfo = new UserLoginDaoImpl(entityManager);
			while (i < checkManKey.length) {
				if (loginInfo.containsKey(checkManKey[i]) && !loginInfo.get(checkManKey[i]).equals("")) {
					i++;
				} else {
					log.info("IN else===" + i);
					i = -1;
					break;
				}
			}
			if (i == -1) {
				log.info("Error " + path.getMsg("E134"));
				ajaxJSONData.put("ResFlag", "000");
				ajaxJSONData.put("ErrorCode", "E134");
				ajaxJSONData.put("Message", path.getMsg("E134"));
			} else {
				if (!MakerCreationDaoImpl.checkValidPassWord(loginInfo.getString("password").trim())) {
					ajaxJSONData.put("ResFlag", "111");
					ajaxJSONData.put("ErrorCode", "E312");
					ajaxJSONData.put("Message", path.getMsg("E312"));
					ajaxJSONData.put("ResService", "SMEMCS006");
					String responseStr = encryptResponse(ajaxJSONData.toString(), shareKey);
					return responseStr;
				}

				encPass = LoginController.sha1EncryptPass(loginInfo.getString("password"));
				loginInfo.put("password", encPass);
				passObj = new PasswordChecker();
				String passFlag = passObj.checkPass(loginInfo.getString("loginID"), encPass);

				log.info("passFlag:::<<::");
				if ("3".equals(passFlag.split("~")[0])) {
					ajaxJSONData.put("ResFlag", "111");
					ajaxJSONData.put("ErrorCode", "E303");
					ajaxJSONData.put("Message", path.getMsg("E303"));
					ajaxJSONData.put("ResService", "SMEMCS006");
					String responseStr = encryptResponse(ajaxJSONData.toString(), shareKey);
					return responseStr;
				}

				boolean response = userinfo.changePassword(loginInfo);
				log.info("Response from API/Static JSOn ===");
				if (response == true) {
					ajaxJSONData.put("ResFlag", "111");
					ajaxJSONData.put("Message", "SUCCESS");
				} else {
					ajaxJSONData.put("ResFlag", "000");
					ajaxJSONData.put("Message", "ERROR");
				}
			}
		}
		if (ajaxJSONData.has("MPIN")) {
			ajaxJSONData.remove("MPIN");
		}
		if (ajaxJSONData.has("password")) {
			ajaxJSONData.remove("password");
		}
		log.info("Cahange password final response:::<<::SMEMCS006");
		ajaxJSONData.put("ResService", "SMEMCS006");
		String responseStr = encryptResponse(ajaxJSONData.toString(), shareKey);
		return responseStr;
	
     }
	
	
	
	public  net.sf.json.JSONObject userAuthentication(org.json.JSONObject reqData, StringBuffer tempToken) throws JSONException {

	    log.info("inside userAuthentication function :::::<<<::::");
	    boolean mFlag = false;
	    net.sf.json.JSONObject custInfo = new    net.sf.json.JSONObject();
	    String requestMode = "";
	    String loginType = "";
	    String regType = "";

	    boolean loginVaild = false;
	    boolean shaTest = false;
	    if (reqData.has("LoginType")) {

	        String lgnType = reqData.getString("LoginType").replaceAll(" ", "+");
	        loginType = new Crypt().decryptText(Crypt.getKey(), lgnType, tempToken.toString(), PKFP).trim();

	        if (loginType.equals(ApplicationGenParameter.LOGIN_TYPE_P)) {
	            custInfo.put("reqMode", ApplicationGenParameter.LOGIN_TYPE_P);
	            regType = ApplicationGenParameter.LOGIN_TYPE_P;
	        }
	    }
	    if (reqData.has("requestMode")) {
	        String requestMode1 = reqData.getString("requestMode").replaceAll(" ", "+");
	        requestMode = new Crypt().decryptText(Crypt.getKey(), requestMode1, tempToken.toString(), PKFP).trim();
	    }

	    if (loginType.equals(ApplicationGenParameter.LOGIN_TYPE_P)
	            || loginType.equals(ApplicationGenParameter.LOGIN_TYPE_M)) {

	        loginVaild = true;

	    } else {
	        log.info("Error " + path.getMsg("E115"));
	        custInfo.clear();
	        custInfo.put("ResFlag", "222");
	        custInfo.put("ErrorCode", "E115");
	        custInfo.put("Message", path.getMsg("E115"));
	        custInfo.put("shareKey", "NA");
	    }

	    if (loginVaild == true && requestMode.equals(ApplicationGenParameter.USER_MAKER)) {
	        /*********************************************************
	         * 
	         * change encryption/decryption logic for Maker
	         * 
	         **********************************************************/
	        mFlag = true;

	        // change 161117
	        GeneralService gnrlService = new GeneralService(entityManager);
	        gnrlService.checkHoliday();
	    } else if (loginVaild == true && requestMode.equals(ApplicationGenParameter.USER_CHECKER)) {

	        if (loginType.equals(ApplicationGenParameter.LOGIN_TYPE_M)) {
	            log.info("this is valid mobile checker @@@@@@:");
	            shaTest = checkSHA(reqData.toString());
	            if (shaTest) {
	                custInfo = parseEncDcr(reqData.toString());
	                custInfo.put("UserFlag", ApplicationGenParameter.USER_CHECKER);
	                custInfo.put("shareKey", custInfo.getString("shareKey"));
	                custInfo.put("loginMode", loginType);
	            }
	        }
	    }
	    // Valid For Mobile Maker and Portal Maker or Checker
	    log.info("login type value2:::::");
	    if ((loginVaild == true && mFlag == true)
	            || (loginVaild == true && loginType.equals(ApplicationGenParameter.LOGIN_TYPE_P))) {

	        /* Start MCSC13 */
	        if ((loginType.equals(ApplicationGenParameter.LOGIN_TYPE_P))
	                && (reqData.has("ServiceType") && reqData.getString("ServiceType").equals("CREATE_FD"))) {

	        	   net.sf.json.JSONObject custInfo1 = new    net.sf.json.JSONObject();
	            custInfo1 = parseEncDcrForMaker(reqData.toString());

	            String shaMaturity = "";
	            String shaintrRate = "";

	            shaMaturity += custInfo1.getString("FromAccNumber").toString()
	                    + custInfo1.getString("ProductCode").toString()
	                    + custInfo1.getString("DepositAmount").toString() + custInfo1.getString("Months").toString()
	                    + custInfo1.getString("Days").toString();

	            if (custInfo1.has("PAN")) {
	                shaMaturity += custInfo1.getString("PAN");
	            } else {
	                shaMaturity += "";
	            }
	            log.info("CustInfo Value::::<<<<<<<<<<<<<<<<<<::: 4018");

	            String sshMatur = sha1EncryptPass(shaMaturity);

	            if (sshMatur.equals(custInfo1.getString("formHashValue"))) {
	                log.info(":::form ssha true:::");
	            } else {
	                custInfo.put("ResFlag", "222");
	            }

	            shaintrRate = sha1EncryptPass(custInfo1.getString("InterestRate").toString());

	            if (shaintrRate.equals(custInfo1.getString("interestHashValue"))) {
	                log.info(":::interest rate true:::");
	            } else {
	                custInfo.put("ResFlag", "222");
	            }

	            String shaMtr = sha1EncryptPass(custInfo1.getString("MaturityAmount").toString());
	            if (shaMtr.equals(custInfo1.getString("maturityHashValue"))) {
	                log.info(":::Maturity  rate true:::");
	            } else {
	                custInfo.put("ResFlag", "222");
	            }

	            log.info("CustInfo Value::::<<<<<<<<<<<<<<<<<<:::4043");

	        }
	        // MCSC43
	        else if ((loginType.equals(ApplicationGenParameter.LOGIN_TYPE_P))
	                && (reqData.has("ServiceType") && reqData.getString("ServiceType").equals("Maturity"))) {

	        	   net.sf.json.JSONObject custInfo1 = new    net.sf.json.JSONObject();
	        	   net.sf.json.JSONObject m_res_header = null;
	            custInfo1 = parseEncDcrForMaker(reqData.toString());

	            String shaMaturity = "";
	            String shaintrRate = "";

	            log.info("maturity json value:::<<<::");

	            shaMaturity += custInfo1.getString("DepositAccountNo").toString()
	                    + custInfo1.getString("ProductCode").toString()
	                    + custInfo1.getString("PrincipalAmount").toString()
	                    + custInfo1.getString("DepositNoMonth").toString()
	                    + custInfo1.getString("DepositNoDay").toString();

	            if (custInfo1.has("DepositPanNo")) {
	                shaMaturity += custInfo1.getString("DepositPanNo");
	            } else {
	                shaMaturity += "";
	            }

	            log.info("CustInfo Value::::<<<<<<<<<<<<<<<<<<:::");

	            String sshMatur = sha1EncryptPass(shaMaturity);

	            shaintrRate = sha1EncryptPass(custInfo1.getString("InterestRate").toString());
	            if (shaintrRate.equals(custInfo1.getString("interestHashValue"))) {
	                log.info(":::interest rate true:::");
	            } else {
	                custInfo.put("ResFlag", "222");
	            }

	            if (sshMatur.equals(custInfo1.getString("formHashValue"))) {

	                // Set FD-RD Rates Start
	                custInfo1.put("ProductType", "FD");
	                custInfo1.put("Months", custInfo1.getString("DepositNoMonth"));
	                custInfo1.put("Days", custInfo1.getString("DepositNoDay"));
	                custInfo1.put("CustomerId", custInfo1.get("CustomerId"));
	                AccountManagementDAO amd = new AccountManagementDAO();
	                m_res_header = amd.getFDRDProductRates(custInfo1, "MCSC13");
	                log.info("resFd::<<<<<<<::4092");

	                try {
	                    if (m_res_header.has("ResCode") && m_res_header.getString("ResCode").equals("0")) {

	                        for (int i = 0; i < m_res_header.getJSONObject("ResBody").getJSONArray("Product")
	                                .toArray().length; i++) {

	                            if (m_res_header.getJSONObject("ResBody").getJSONArray("Product").getJSONObject(i)
	                                    .get("ProductCode").equals(custInfo1.getString("ProductCode").toString())) {
	                                log.info(" if:::::::::");

	                                for (int j = 0; j < m_res_header.getJSONObject("ResBody").getJSONArray("Product")
	                                        .getJSONObject(i).getJSONArray("FDProductSlabDetail")
	                                        .toArray().length; j++) {

	                                    String interstRate = m_res_header.getJSONObject("ResBody")
	                                            .getJSONArray("Product").getJSONObject(i)
	                                            .getJSONArray("FDProductSlabDetail").optJSONObject(j)
	                                            .getString("InterestRate");

	                                    String maxTermMonth = m_res_header.getJSONObject("ResBody")
	                                            .getJSONArray("Product").getJSONObject(i)
	                                            .getJSONArray("FDProductSlabDetail").optJSONObject(j)
	                                            .getJSONObject("MaxTermPeriod").getString("Months");
	                                    String minTermMonth = m_res_header.getJSONObject("ResBody")
	                                            .getJSONArray("Product").getJSONObject(i)
	                                            .getJSONArray("FDProductSlabDetail").optJSONObject(j)
	                                            .getJSONObject("MinTermPeriod").getString("Months");

	                                    String maxTermDays = m_res_header.getJSONObject("ResBody")
	                                            .getJSONArray("Product").getJSONObject(i)
	                                            .getJSONArray("FDProductSlabDetail").optJSONObject(j)
	                                            .getJSONObject("MaxTermPeriod").getString("Days");
	                                    String minTermDays = m_res_header.getJSONObject("ResBody")
	                                            .getJSONArray("Product").getJSONObject(i)
	                                            .getJSONArray("FDProductSlabDetail").optJSONObject(j)
	                                            .getJSONObject("MinTermPeriod").getString("Days");

	                                    if (custInfo1.getString("DepositNoMonth").toString().equals(maxTermMonth)
	                                            && custInfo1.getString("DepositNoDay").toString().equals(maxTermDays)) {
	                                        // log.info(" Max interst rate val:::"
	                                        m_res_header.getJSONObject("ResBody").getJSONArray("Product")
	                                                .getJSONObject(i).getJSONArray("FDProductSlabDetail")
	                                                .optJSONObject(j).getString("InterestRate");

	                                    } else if (custInfo1.getString("DepositNoMonth").toString().equals(minTermMonth)
	                                            && custInfo1.getString("DepositNoDay").toString().equals(minTermDays)) {
	                                        // log.info(" Min interst rate val:::"
	                                        m_res_header.getJSONObject("ResBody").getJSONArray("Product")
	                                                .getJSONObject(i).getJSONArray("FDProductSlabDetail")
	                                                .optJSONObject(j).getString("InterestRate");
	                                    }

	                                    if (interstRate.equals(custInfo1.getString("InterestRate").toString())
	                                            || custInfo1.getString("InterestRate").toString().equals("7.75")) {
	                                        log.info("Match Interst rate :::");

	                                    } else {

	                                    }
	                                }
	                            }
	                            log.info(":test:::4178");
	                        }
	                    }
	                } catch (Exception e) {
	                    log.info("Exception", e);
	                }
	                // Set FD-RD Rates End

	                log.info(":::form ssha true:::");
	            } else {
	                custInfo.put("ResFlag", "222");
	            }

	        } // ADOC
	        /* Start MCSC39 */
	        else if ((loginType.equals(ApplicationGenParameter.LOGIN_TYPE_P))
	                && (reqData.has("ServiceType") && reqData.getString("ServiceType").equals("ADOC"))) {

	        	   net.sf.json.JSONObject custInfo1 = new    net.sf.json.JSONObject();
	            custInfo1 = parseEncDcrForMaker(reqData.toString());

	            String shaMaturity = "";

	            shaMaturity += custInfo1.getString("FromAccountNo").toString()
	                    + custInfo1.getString("ToAccountNo").toString();

	            log.info("CustInfo Value::::<<<<<<<<<<<<<<<<<<:::");

	            String sshMatur = sha1EncryptPass(shaMaturity);
	            log.info("Adoc hash value::<<<<<<<<<<<<:");

	            if (sshMatur.equals(custInfo1.getString("FormHashValueFD"))) {
	                log.info(":::form ADOC true:::");
	            } else {
	                log.info(":::form ADOC false:::");
	                custInfo.put("ResFlag", "222");
	            }
	            log.info("CustInfo Value::::<<<<<<<<<<<<<<<<<<:::");
	        }

	        // MCSC03 //Last Ten TXN
	        else if ((loginType.equals(ApplicationGenParameter.LOGIN_TYPE_P))
	                && (reqData.has("ServiceType") && reqData.getString("ServiceType").equals("LastTenTxn"))) {
	        	   net.sf.json.JSONObject custInfo1 = new    net.sf.json.JSONObject();
	            custInfo1 = parseEncDcrForMaker(reqData.toString());

	            log.info("Last Ten Transaction details<<<<<<<<<<<<<<<<<<<<::::::");

	            String shaLastTenTxn = "";

	            shaLastTenTxn += custInfo1.getString("CustomerID").toString() + "|"
	                    + custInfo1.getString("AccountNumber").toString();

	            log.info("CustInfo Value::::<<<<<<<<<<<<<<<<<<:::");

	            String sshMatur = sha1EncryptPass(shaLastTenTxn);

	            if (sshMatur.equals(custInfo1.getString("savingAccHashValue"))) {
	                log.info(":::form Last Ten true:::");
	            } else {
	                log.info(":::form Last Ten false:::");
	                custInfo.put("ResFlag", "222");
	            }

	        }

	        // MCSC27 TradeSummary
	        else if ((loginType.equals(ApplicationGenParameter.LOGIN_TYPE_P))
	                && (reqData.has("ServiceType") && reqData.getString("ServiceType").equals("tradeSummary"))) {
	        	   net.sf.json.JSONObject custInfo1 = new    net.sf.json.JSONObject();
	            custInfo1 = parseEncDcrForMaker(reqData.toString());

	            log.info("Trade Summary <<<<<<<<<<<<<<<<<<<::::::");
	            String shaTradeSummary = "";
	            shaTradeSummary += custInfo1.getString("CustomerId").toString() + "|" + "CustomerId";
	            log.info("CustInfo Value::::<<<<<<<<<<<<<<<<<<:::");
	            String sshTrade = sha1EncryptPass(shaTradeSummary);
	            if (sshTrade.equals(custInfo1.getString("tradeHashValue"))) {
	                log.info(":::ssh Trade true:::");
	            } else {
	                log.info("::ssh Trade  false:::");
	                custInfo.put("ResFlag", "222");
	            }
	        } else if ((loginType.equals(ApplicationGenParameter.LOGIN_TYPE_P)) && (reqData.has("ServiceType")
	                && reqData.getString("ServiceType").equals("FUND_TRNS_ALL_ACCOUNT"))) {

	        	   net.sf.json.JSONObject custInfo1 = new    net.sf.json.JSONObject();
	            custInfo1 = parseEncDcrForMaker(reqData.toString());

	            log.info("FUND_TRNS_ALL_ACCOUNT<<<<<<<<<<<<<<<<<<<::::::");

	            String fundTransForAllAcc = "";

	            boolean gstFlag = false;
	            String sshAllACC = "";
	            if (custInfo1.containsKey("BeneIfscCd") && ApplicationGenParameter.GST_TAXDATA.split("\\|")[2]
	                    .equals(custInfo1.getString("BeneIfscCd"))) {
	                gstFlag = true;
	            } else {
	                fundTransForAllAcc += custInfo1.getString("BeneAccNo") + custInfo1.getString("beneficiaryID")
	                        + custInfo1.getString("BeneName") + custInfo1.getString("accountNo")
	                        + custInfo1.getString("transferAmount");

	                log.info("CustInfo Value::::<<<<<<<<<<<<<<<<<<:::");

	                sshAllACC = sha1EncryptPass(fundTransForAllAcc);
	            }

	            if (sshAllACC.equals(custInfo1.getString("formHashValue"))) {
	                log.info(":::ssh All Account true:::");
	            } else if (gstFlag) {
	                log.info(":::ssh for GST will not check:::");
	            }

	            else {
	                log.info("::ssh All Account true:::::");
	                custInfo.put("ResFlag", "222");
	                custInfo.put("shareKey", custInfo1.getString("shareKey"));
	            }
	        }

	        if (!custInfo.has("ResFlag")) {

	            if (reqData.has("data") && reqData.has("reqToken")) {

	                String data1 = reqData.getString("data").replaceAll(" ", "+");
	                String reqToken12 = reqData.getString("reqToken").replaceAll(" ", "+");
	                String dataTokenVal = data1 + reqToken12;
	                String sshval = sha1EncryptPass(dataTokenVal);

	                if (sshval.equals(reqData.getString("hash"))) {
	                    shaTest = true;
	                    custInfo = parseEncDcrForMaker(reqData.toString());

	                    custInfo.put("reqMode", regType);
	                    log.info("sshval Value::::<<<<<<<<<<<<<<<<<<:::");
	                    if (requestMode.equals(ApplicationGenParameter.USER_MAKER)) {
	                        custInfo.put("mFlag", "true");
	                        custInfo.put("loginMode", loginType);
	                        custInfo.put("shareKey", custInfo.getString("shareKey"));
	                    } else {
	                        custInfo.put("shareKey", custInfo.getString("shareKey"));
	                        custInfo.put("loginMode", loginType);
	                        custInfo.put("UserFlag", ApplicationGenParameter.USER_CHECKER);

	                    }
	                } else {
	                    custInfo.clear();
	                    custInfo.put("ResFlag", "222");
	                    custInfo.put("ErrorCode", "E115");
	                    custInfo.put("Message", path.getMsg("E115"));
	                    custInfo.put("shareKey", "NA");
	                }
	            } else {
	                custInfo.clear();
	                custInfo.put("ResFlag", "222");
	                custInfo.put("ErrorCode", "E115");
	                custInfo.put("Message", path.getMsg("E115"));
	                custInfo.put("shareKey", "NA");
	            }
	        }

	    }

	    if (custInfo.toString().contains("CardNumber") || custInfo.toString().contains("OldPIN")
	            || custInfo.toString().contains("OldPIN") || custInfo.toString().contains("debitCardNo")
	            || custInfo.toString().contains("custPass") || custInfo.toString().contains("MPIN")) {
	        log.info(":End of userAuthentication");
	    } else {

	        // PAN CARD MASKING FOR APPSEC POINT
	        // Implementation of PAN Number Masking in the Application Logs
	        try {
	            net.sf.json.JSONObject duplicatecustInfo = new    net.sf.json.JSONObject();

	            duplicatecustInfo.put("custInfo", custInfo);
	            log.info("userAuthentication 1" + duplicatecustInfo);

	            if (duplicatecustInfo.getJSONObject("custInfo").has("custPanCardNo")) {
	                log.info("userAuthentication 1" + duplicatecustInfo);
	                // to mask the last digit of pan card.
	                duplicatecustInfo.getJSONObject("custInfo").replace("custPanCardNo", duplicatecustInfo .getJSONObject("custInfo").getString("custPanCardNo").replaceAll("\\w(?=\\w{4})", "*"));

	                // to remove pan card from the log
	                log.info("userAuthentication " + duplicatecustInfo);
	                // duplicatecustInfo.getJSONObject("custInfo").remove("custPanCardNo");

	            }
	        } catch (Exception e) {
	            log.error("Exception in PAN card masking" + e);
	        }

	        log.info(":End of userAuthentication");
	    }
	    return custInfo;
	}

public boolean checkSHA(String request) throws JSONException {
	log.info(" in checkSHA function");
	boolean shaFlag = false;
	org.json.JSONObject p_reqJson = new org.json.JSONObject(request);

	String data1 = p_reqJson.getString("data").replaceAll(" ", "+");
	String reqToken12 = p_reqJson.getString("reqToken").replaceAll(" ", "+");
	String dataTokenVal = data1 + reqToken12;
	String sshval = sha1EncryptPass(dataTokenVal);

	if (sshval.equals(p_reqJson.getString("hash"))) {
		shaFlag = true;
	}

	return shaFlag;
}
	

public net.sf.json.JSONObject parseEncDcr(String request) throws JSONException {

	log.info("parseEncDcr ==== parseEncDcr call=====");
	org.json.JSONObject resObj = new org.json.JSONObject(request);
     String token="";
	String shareKey = "";
	String resData = "";
	String device_imei="";
	net.sf.json.JSONObject resJson = new net.sf.json.JSONObject();
	net.sf.json.JSONObject reqInJson = new net.sf.json.JSONObject();
	AesUtil aesUtil = new AesUtil();
	net.sf.json.JSONObject ajaxJSONData = new net.sf.json.JSONObject();

	try {

		if (resObj.has("CallTmp") && resObj.get("CallTmp").equals("Y")) {
			log.info("In If CallTmp");
			if (resObj.has("token") && !resObj.get("token").equals("")) {
				log.info("In If token");
				StringBuffer tempToken = new StringBuffer(
						Integer.toHexString(this.hashCode()) + new Date().getTime());
				PKFP = !path.getPath("serverType").equals("UAT") ? path.getPath("LOCAL_PUBKEYPATH")
						: path.getPath("YES_PUBKEYPATH");
				token = new Crypt().decryptText(Crypt.getKey(), resObj.getString("token").toString(),
						tempToken.toString(), PKFP);
				String l_shareKey = new GeneralService(entityManager).getShareKeyTmp(token);
				if (l_shareKey != null && !l_shareKey.equals("")) {
					shareKey = new Crypt().decryptText(Crypt.getKey(), l_shareKey.toString(), tempToken.toString(),
							PKFP);
				} else {
					shareKey = "NA";
				}
			}

		} else {
			if (resObj.has("token") && !resObj.get("token").equals("")) {
				log.info("In else If token");
				StringBuffer tempToken = new StringBuffer(
						Integer.toHexString(this.hashCode()) + new Date().getTime());
				PKFP = !path.getPath("serverType").equals("UAT") ? path.getPath("LOCAL_PUBKEYPATH")
						: path.getPath("YES_PUBKEYPATH");
				token = new Crypt().decryptText(Crypt.getKey(), resObj.getString("token").toString(),
						tempToken.toString(), PKFP);
				String l_shareKey = new GeneralService(entityManager).getShareKey(token);
				if (l_shareKey != null && !l_shareKey.equals("")) {
					shareKey = new Crypt().decryptText(Crypt.getKey(), l_shareKey.toString(), tempToken.toString(),PKFP);

				} else {
					shareKey = "NA";
				}

			} else {
				log.info("In If IMEI 1");
				if (resObj.has("IMEI") && !resObj.get("IMEI").equals("")) {
					log.info("In If IMEI");
					StringBuffer tempToken = new StringBuffer(
							Integer.toHexString(this.hashCode()) + new Date().getTime());
					PKFP = !path.getPath("serverType").equals("UAT") ? path.getPath("LOCAL_PUBKEYPATH")
							: path.getPath("YES_PUBKEYPATH");
					device_imei = new Crypt().decryptText(Crypt.getKey(), resObj.getString("IMEI").toString(),
							tempToken.toString(), PKFP);
					String l_shareKey = new GeneralService(entityManager).getShareKeyAtLogIn(device_imei);

					if (l_shareKey != null && !l_shareKey.equals("")) {
						shareKey = new Crypt().decryptText(Crypt.getKey(), l_shareKey.toString(),
								tempToken.toString(), PKFP);
					} else {
						shareKey = "NA";
					}

				}
			}
		}
		if (!shareKey.equals("NA") && !shareKey.equals("")) {
			if (resObj.has("data") && !resObj.get("data").equals("")) {
				log.info("In If data");
				String lgnType = resObj.getString("LoginType").replaceAll(" ", "+");
				StringBuffer tempToken = new StringBuffer( Integer.toHexString(this.hashCode()) + new Date().getTime());
				String loginType = new Crypt().decryptText(Crypt.getKey(), lgnType, tempToken.toString(), PKFP).trim();
				if (resObj.has("checkKey") && resObj.has("LoginType")) {

					if (loginType.equals(ApplicationGenParameter.LOGIN_TYPE_M)) {

						PKFP = !path.getPath("serverType").equals("UAT") ? path.getPath("LOCAL_PUBKEYPATH"): path.getPath("YES_PUBKEYPATH");

						String uiShareKey = resObj.getString("checkKey").replaceAll(" ", "+");
						StringBuffer uiShareKeyTempToken1 = new StringBuffer(Integer.toHexString(this.hashCode()) + new Date().getTime());
						String uiShareKeyToken = new Crypt().decryptText(Crypt.getKey(), uiShareKey, uiShareKeyTempToken1.toString(), PKFP);

						if (shareKey.equals(uiShareKeyToken)) {

						} else {

							if (resObj.has("deviceIMEI")) {
								StringBuffer tempTokenIMEI = new StringBuffer(Integer.toHexString(this.hashCode()) + new Date().getTime());
								device_imei = new Crypt().decryptText(Crypt.getKey(), resObj.getString("deviceIMEI").toString(), tempTokenIMEI.toString(), PKFP);

								if (shareKey.contains(device_imei) && uiShareKeyToken.contains(device_imei)) {
									shareKey = userlogObj.validateShareKeyForVersoionIssue(device_imei ,uiShareKeyToken , uiShareKey);
								}
							}

						}
					}
				}

				resData = aesUtil.decrypt(shareKey, resObj.getString("data"));
				resJson = net.sf.json.JSONObject.fromObject(resData);
				if (resJson.containsKey("reqData") && !resJson.get("reqData").equals("")) {
					reqInJson = net.sf.json.JSONObject.fromObject(resJson.get("reqData").toString());
					ajaxJSONData = reqInJson;
				} else {
				}

			}

			ajaxJSONData.put("shareKey", shareKey);
			return ajaxJSONData;
		} else {
			ajaxJSONData.clear();
			ajaxJSONData.put("ResFlag", "222");
			ajaxJSONData.put("ErrorCode", "E114");
			ajaxJSONData.put("Message", path.getMsg("E114"));
			ajaxJSONData.put("shareKey", shareKey);
			return ajaxJSONData;
		}

	} catch (NullPointerException e) {
		log.info("NullPointerException Occured ====", e);
		ajaxJSONData.clear();
		ajaxJSONData.put("ResFlag", "222");
		ajaxJSONData.put("ErrorCode", "E114");
		ajaxJSONData.put("Message", path.getMsg("E114"));
		ajaxJSONData.put("shareKey", shareKey);
		return ajaxJSONData;
	} catch (Exception e1) {
		log.info("Exception Occured ====", e1);
		return ajaxJSONData;
	}
}


@PostMapping("MCSC32")
public String requestNotification(@RequestBody String request) {

	log.info("MCSC32 ==== requestNotify call=====");
	net.sf.json.JSONObject ajaxJSONData = new net.sf.json.JSONObject();
	String l_custId = "";


	try {
		org.json.JSONObject reqData = new org.json.JSONObject(request);

		net.sf.json.JSONObject reqNotify = null;
		String[] checkManKey;
		boolean mFlag = false;
		String reqToken1 = "";
		String sessionID = "";

		StringBuffer tempToken = new StringBuffer(Integer.toHexString(this.hashCode()) + new Date().getTime());
		PKFP = !path.getPath("serverType").equals("UAT") ? path.getPath("LOCAL_PUBKEYPATH")
				: path.getPath("YES_PUBKEYPATH");

		if (reqData.has("sessionID")) {
			String sessionID1 = reqData.getString("sessionID").replaceAll(" ", "+");
			sessionID = new Crypt().decryptText(Crypt.getKey(), sessionID1, tempToken.toString(), PKFP);
		}

		reqNotify = userAuthentication(reqData, tempToken);
		log.info("@@@@:::custinfo:::::#####::::");

		if (reqNotify.containsKey("ResFlag")) {
			log.info("Error " + path.getMsg("E115") + "::error data:::");
			ajaxJSONData = reqNotify;
			ajaxJSONData.put("ResService", "MCSC32");
			String responseStr = encryptResponse(ajaxJSONData.toString(), shareKey);
			return responseStr;
		} else if ((reqNotify.has("reqMode")
				&& reqNotify.get("reqMode").equals(ApplicationGenParameter.LOGIN_TYPE_P))
				&& (!loginDaoimpl.checkSessionValidate(sessionID))) {
			// change 161017
			log.info("Error " + path.getMsg("E115"));
			ajaxJSONData.clear();
			ajaxJSONData.put("ResFlag", "222");
			ajaxJSONData.put("ErrorCode", "E115");
			shareKey = shareKey + 1;
			ajaxJSONData.put("Message", path.getMsg("E115"));
			ajaxJSONData.put("ResService", "MCSC32");
			String responseStr = encryptResponse(ajaxJSONData.toString(), shareKey);
			return responseStr;
		}

		if (reqNotify.containsKey("mFlag")) {
			mFlag = true;
			checkManKey = new String[] { "AuthId", "CustomerId", "ServiceFlag" };
		} else {
			checkManKey = new String[] { "CustomerId", "ServiceFlag" };
		}

		shareKey = reqNotify.getString("shareKey");

		NotificationManagementDAO nmd;
		InboxManagementDAO imd;
		int i = 0;
		net.sf.json.JSONObject resJson = new net.sf.json.JSONObject();

		ajaxJSONData.put("ResFlag", "111");
		ajaxJSONData.put("Message", resJson);

		l_custId = reqNotify.getString("CustomerId").trim();

		if (reqData.has("timeOutToken") && !reqData.getString("timeOutToken").equals("")) {
			reqToken1 = new Crypt().decryptText(Crypt.getKey(), reqData.getString("timeOutToken").trim(),
					tempToken.toString(), PKFP);
			String l_passToken = timeOutToken + l_custId;
			if (l_passToken.equals(reqToken1)) {
				passReqToken = true;
			} else {
				passReqToken = false;
			}
		}

		if (reqData.has("reqToken")) {
			String reqToken = reqData.getString("reqToken").replaceAll(" ", "+");
			log.info("reqToken value<<<<<<<<<<<<<<****");
			reqToken1 = new Crypt().decryptText(Crypt.getKey(), reqToken, tempToken.toString(), PKFP);
			boolean tokenValidFlag = loginDaoimpl.validateTokenFromDB(getServiceName(new Object() {
			}.getClass().getEnclosingMethod().getName()), reqToken1, l_custId);// reqToken
			log.info("::tokenValidflag:::>>>" + tokenValidFlag + "::custId::>>" + l_custId);
			// tokenValidFlag=true;//tokenValidFlag
			if (tokenValidFlag) {
				ApplicationGenParameter.tokenList.put(l_custId, reqToken1);
			}
		}
		if (mFlag == false
				&& !ApplicationGenParameter.companyCustomerMapList.containsKey(l_custId.trim().toUpperCase())) {
			loginDaoimpl.getCompanyId(l_custId);
		} else if (mFlag == true && !ApplicationGenParameter.companyCustomerMapList
				.containsKey(reqNotify.getString("AuthId").trim().toUpperCase())) {
			loginDaoimpl.getCompanyId(reqNotify.getString("AuthId"));
		}

		if (passReqToken || reqToken1 != null && !reqToken1.equals(" ") && ApplicationGenParameter.tokenList.get(l_custId).equals(reqToken1)) {
			validSesFlag = true;
			if (validSesFlag == false) {
				log.info("Error " + path.getMsg("E117").replace("<>", ApplicationGenParameter.SESSION_TIME));
				ajaxJSONData.clear();
				ajaxJSONData.put("ResFlag", "222");
				ajaxJSONData.put("ErrorCode", "E117");
				ajaxJSONData.put("Message", path.getMsg("E117").replace("<>", ApplicationGenParameter.SESSION_TIME));
			} else {
				while (i < checkManKey.length) {

					if (reqNotify.containsKey(checkManKey[i]) && !reqNotify.get(checkManKey[i]).equals("")) {
						i++;
					} else {
						log.info("IN else===" + i);
						i = -1;
						break;
					}
				}
				if (i == -1) {
					log.info("Error " + path.getMsg("E134"));
					ajaxJSONData.put("ResFlag", "000");
					ajaxJSONData.put("ErrorCode", "E134");
					ajaxJSONData.put("Message", path.getMsg("E134"));
				} else {
					reqNotify.remove("token");
					if (reqNotify.getString("ServiceFlag").equals("N")) {
						nmd = new NotificationManagementDAO(entityManager);

						if (mFlag == true) {
							reqNotify.put("CustomerId", reqNotify.getString("CustomerId").trim());
						}

						resJson = nmd.getNotificationDetails(reqNotify);
						ajaxJSONData.put("ResFlag", "111");
						ajaxJSONData.put("Message", resJson);
					}

					if (reqNotify.getString("ServiceFlag").equals("I")) {
						imd = new InboxManagementDAO();
						if (mFlag == true) {
							String authID = reqNotify.getString("AuthId");
							reqNotify.put("CustomerId", authID);
						}

						resJson = imd.getInboxDetails(reqNotify);
						ajaxJSONData.put("ResFlag", "111");
						ajaxJSONData.put("Message", resJson);

						//Added by Prabhat for audit activity details of customer on 23 Oct 2018
						net.sf.json.JSONObject auditJson = new net.sf.json.JSONObject();
						auditJson.put("serviceId", "MCSC32");
						auditJson.put("customerId", l_custId);
						new GeneralService(entityManager).addCustomerAuditDtl(auditJson);
					}

				}
			}
		} else {
			log.info("Error " + path.getMsg("E114"));
			ajaxJSONData.clear();
			ajaxJSONData.put("ResFlag", "222");
			ajaxJSONData.put("ErrorCode", "E114");
			ajaxJSONData.put("Message", path.getMsg("E114"));
		}

	} catch (JSONException e1) {
		log.info("JSONException :::", e1);
	} catch (net.sf.json.JSONException e2) {
		log.info("net.sf.json.JSONException :::", e2);
	} catch (Exception e) {
		log.info("Exception :::", e);
	} finally {
		String tok = getToken();

		boolean resTokenFlag = loginDaoImpl.updateTokenDB(getServiceName(new Object() {
		}.getClass().getEnclosingMethod().getName()), tok, l_custId);
		log.info(":::resTokenFlag::::" + resTokenFlag);
		if (!resTokenFlag) {
			ajaxJSONData.put("ResFlag", "222");
			ajaxJSONData.put("ErrorCode", "E107");
			ajaxJSONData.put("Message", path.getMsg("E107"));
			String responseStr = encryptResponse(ajaxJSONData.toString(), shareKey);
			return responseStr;
		}
		ajaxJSONData.put("resToken", tok);
		if (ajaxJSONData.has("MPIN")) {
			ajaxJSONData.remove("MPIN"); 
		}
		if (ajaxJSONData.has("password")) {
			ajaxJSONData.remove("password");
		}
		ajaxJSONData.put("ResService", "MCSC32");
	}
	log.info("End MCSC32 ==== requestNotify call=====");
	return ajaxJSONData.toString();
}// End of Notification Request




}
