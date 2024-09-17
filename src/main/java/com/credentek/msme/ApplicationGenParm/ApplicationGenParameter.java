/**********************************************************************
*
* Module Name         :SMALL AND MEDIUM ENTERPRISES(SME)
*
* File Name           :ApplicationGenParameter.Java
*                             
*            Version Control Block
*            ---------------------
* Date              Version      Author               Description
* Description         :
* ---------         --------   ---------------  ---------------------------
* FEB 11, 2022         3.1.0      Credentek Team       Response Model
**********************************************************************/

package com.credentek.msme.ApplicationGenParm;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.credentek.msme.database.DaoBase;

//import com.credentek.msme.utils.DAOBase;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

import org.apache.logging.log4j.LogManager;

//extends DAOBase
public class ApplicationGenParameter extends DaoBase{

	private static final Logger log = LogManager.getLogger(ApplicationGenParameter.class);

	public static Map<String, String> parameterMasterList = null;

	public static HashMap<String, HashMap<String, List<String>>> categoryCrmMap = new HashMap<String, HashMap<String, List<String>>>();
//	public static JSONObject issueIdJsonArray = new JSONObject();
//	public static JSONObject issueNameJsonArray = new JSONObject();
	public static Map<String, String> exceptionMasterList = null;
	public static Map<String, String> apiMasterList = null;
	public static Map<String, LinkedList<String>> userMenuList = null;
	public static Map<String, String> tokenList = new HashMap<String, String>();
	public static Map<String, String> companyCustomerMapList = new HashMap<String, String>();
	public static Map<String, String> makerAliasMapList = new HashMap<String, String>();
	public static Map<String, String> messageMasterList = null;
	public static Map<String, String> docNameIdMap = null;
	public static Map<String, String> soapParamMapList = null;
	public static Map<String, String> custIpAddMapList = null;
	public static ArrayList<String> masterPageMenuList = null;
	public static String Authorization = "";
	public static String SERVICE_TYPE = "false";
	public static String PARTNERKEY = "";
	public static String TURNOVERTYPE = "";
	public static String ACCEPTANCETYPE = "";
	public static String UPITXNLIMIT = "";
	public static String VALIDUPTOJSON = "", VALID_INT_UPTOJSON = "";
	public static String UPIURL = "";
	public static String UPIPRXIP = "";
	public static String UPIPRXPRT = "";
	public static String UPI_PAYMENT_LINK = "";
	public static String INTENT_SME_SEND_FLAG = "";
	public static String INTENT_SME_CONTENT = "";
	public static String INTENT_EMAIL_SEND_FLAG = "";
	public static String INTENT_EMAIL_CONTENT = "";
	public static String INTENT_EMAIL_SUBJECT = "";
	public static Map<String, String> stateMapUpi = new HashMap<String, String>();
	public static Map<String, String> ownershipMapUpi = new HashMap<String, String>();
	public static HashMap<String, String> mccMapInner = new HashMap<String, String>();
	public static String BENE_COOLING_MINUTES = "";// validate cooling period CR----29-03-2023---2459
	public static String UPLOAD_PATH = "";
	public static String AD_AUTHENTICATION = "";
	public static String PROMO_IMG_UP_PATH = "";
	public static Date date;
	public static SimpleDateFormat sdf;
	public static SimpleDateFormat sdff;
	public static String LINE_ID;
	public static String MAIN_LINE;
	public static String BULK_FILE_UPLOAD_PATH;
	public static String BULK_BENE_FILE_UPLOAD_PATH;
	public static String UPIKEYPATH;
	public static String GST_TAXDATA;
	public static String SESSION_TIME;
	public static String OTP_REGENERATION_TIME;
	public static String MAKER_DAILY_LIMIT;
	public static String SMS_CONTENT_CHECKER;
	public static String SMS_SEND_FLAG_CHECKER;
	public static String EMAIL_SEND_FLAG;
	public static String NEFT_CUTOFF;
	public static String TRANSACTION_TIMEOUT;
	public static String RTGS_CUTOFF;
	public static String IMPS_LIMIT;
	public static String IMPS_LIMIT_VALIDATION;
	public static String ADHOC_LIMIT;
	public static String USER_MAKER;
	public static String USER_CHECKER;
	public static String LOGIN_TYPE_M;
	public static String LOGIN_TYPE_P;
	public static String CAPTCHA_URL;
	public static String CAPTCHA_SECREAT_KEY;
	public static String CAPTCHA_FLAG;
	public static String YES_AMAZON_OTP_FLAG;
	public static String YES_AMAZON_UPLOAD_PATH;
	public static String YES_AMAZON_SSL;
	public static String YES_AMAZON_HOST;
	public static String YES_AMAZON_PORT;
	public static String YES_AMAZON_SSLPORT;
	public static String YES_AMAZON_TO;
	public static String YES_AMAZON_FROM;
	public static String YESAMAZON_BANK_URL;
	public static String YESAMAZON_PASSWORD;
	public static String PASSWORD_EXPIRY_DAYS;
	public static String REGGULAR_EXPRESION_ALPHA;
	public static String REGGULAR_EXPRESION_NUM;
	public static String CASE_INITITIATION_URL;
	public static String UPLOAD_DOCUMENT_URL;
	public static String SOAP_PARAMETER_LIST;
	public static Map<String, String> activeServerList = new HashMap<String, String>();
	public static Map<String, String> serverNameTimeFreqList = new HashMap<String, String>();
	public static String PURGE_ACTIVITY_DAYS;
	public static String FORGOT_PASS_LINK;
	public static String AGENT_ID_FOR_PORTAL;
	public static String AGENT_ID_FOR_MOBILE;
	public static String BILLDESK_CHANNEL_ID_PORTAL;
	public static String BILLDESK_CHANNEL_ID_MOBILE;
	public static String MAKER_TRANSACTION_LIMIT;
	public static String FRMENQUEURL; // SHA changes control flag App sec May 2023
	public static String FRMRDURL; // FRM changes control flag App sec May 2023
	public static String SYSTEM_USER_ID;
	public static String FRMSTATUS;
	public static String SMBND; // SIM Binding May 2023 by 2459
	public static String SMBVMN; // SIM Binding May 2023 by 2459
	public static String SMBFRQTM; // SIM Binding May 2023 by 2459
	public static String CONTENT_NOTE;// Userd for TAT ContentNote menu
	public static String CONTACT_US_DTL;// Userd for TAT ContactUs menu
	public static String THIRD_PARTY_FD_PRODUCT_CODE;// Third Party FD Change
	public static String DEFAULT_LIMIT_SETUP;// Default limit setup
	public static String DEFAULT_LIMIT_SETUP_FLAG;// Default limit setup
	
	private EntityManager entityManager;

	@Autowired
	public ApplicationGenParameter(EntityManager entityManager) {
		this.entityManager = entityManager;
		prepareParameterList();
		messageMasterList();
		docNameIdMapList();
		ServiceGenerater();
		getServerListFromDB();
		customerIpAddressMapList();
		stateNameMapList();
		mccUPIMapList();
		ownershipMapList();
	}

	public ApplicationGenParameter() {

		// new SmeHelpSectionDaoImpl().sendDataForModuleServiceCode("f3");

		// getQueryAndLabel
		// new
		// LogReportBluePrintGenerationUtil().getQueryAndLabel("BT00","0","5","SME_TXN_AUDIT_ACTIVITY_LOG~SME_TXN_ID|SME_TRANSACTION_DTL~TRAN_REF_NO");
		// new LogReportBluePrintGenerationUtil().saveLogBlueprintInTable();
		// new CallSchedulerForPurging();
		// apiMasterListGenerator();
		// prepareExceptionMasterList();
		// prepareUserMenuList();
		// new SchedulerMain().callScedule();

	}
	public synchronized void prepareParameterList() {
		log.info("############ In prepareParameterMasterList() of ApplicationGenParameter ############");

		if (parameterMasterList == null) {
			parameterMasterList = new ConcurrentHashMap<>();
		}

		String sql = "SELECT PARAMETER_NAME, NVL(PARAMETER_VALUE,'NA') PARAMETER_VALUE, NVL(PARAMETER_VALUE1,'NA') PARAMETER_VALUE1,"
				+ " NVL(PARAMETER_VALUE2,'NA') PARAMETER_VALUE2 FROM GENERAL_PARAM_MST ORDER BY PARAMETER_SRNO";

		try {
			log.info("In try");

			if(!entityManager.isOpen()) {
			System.out.println("Entity Manaer is closed");
			
			
			}
			
			Query query = entityManager.createNativeQuery(sql);
			List<Object[]> resultList = query.getResultList();

			int count = 0;
			for (Object[] result : resultList) {
				String paramCode = (String) result[0];
				String paramValue = ((String) result[1]).trim();

				if (!((String) result[2]).trim().equals("NA")) {
					paramValue += ((String) result[2]).trim();
				}
				if (!((String) result[3]).trim().equals("NA")) {
					paramValue += ((String) result[3]).trim();
				}

				String paramDesc = paramValue;
				count++;
				parameterMasterList.put(paramCode, paramDesc.trim());
			}
			log.info("SERVICE_TYPE ..." + SERVICE_TYPE);
			log.info("############ Total " + count + " Parameter Info Loaded ############");

		} catch (Exception e) {
			log.error("Exception occurred during prepareParameterList", e);
			throw new RuntimeException("Failed to initialize ApplicationGenParameter", e);
		}
	}

	@Transactional // Ensure this method runs within a transaction
	public void prepareExceptionMasterList() {
		if (exceptionMasterList == null) {
			exceptionMasterList = new HashMap<>();
		} else {
			exceptionMasterList.clear(); // Clear existing data if needed
		}

		String sql = "SELECT EXCEPTION_CD, NVL(EXCEPTION_DESC, ' ') AS EXCEPTION_DESC FROM COM_EXCEPTION_MST WHERE AUTH_STATUS IN ('M','A') ORDER BY EXCEPTION_CD";

		try {
			Query query = entityManager.createNativeQuery(sql);
			List<Object[]> results = query.getResultList();

			int count = 0;
			for (Object[] row : results) {
				String excepCode = (String) row[0];
				String excepDesc = ((String) row[1]).trim();
				exceptionMasterList.put(excepCode, excepDesc);
				count++;
			}

			log.info("############ Total " + count + " Exception Parameter Info Loaded ############");
		} catch (Exception e) {
			log.error("Exception occurred while preparing exceptionMasterList", e);
		}
	}

	// Optional: Getter for exceptionMasterList
	public Map<String, String> getExceptionMasterList() {
		return exceptionMasterList;
	}

	@Transactional
	public void prepareUserMenuList() {
		log.info("############ In prepareUserMenuList() of ApplicationGenParameter ############");

		userMenuList = new HashMap<>();
		masterPageMenuList = new ArrayList<>();

		String groupQuery = "SELECT DISTINCT(nvl(USR_GROUP,'NA')) USR_GROUP from SME_GROUP_M ORDER BY USR_GROUP";

		try {
			Query groupQueryObj = entityManager.createNativeQuery(groupQuery);
			List<String> userGroups = groupQueryObj.getResultList();

			int totalGroupCount = 0;
			int totalMenuCount = 0;

			for (String currUserGroup : userGroups) {
				if (!currUserGroup.equals("NA")) {
					currUserGroup = currUserGroup.toUpperCase();
					LinkedList<String> myCurrList = new LinkedList<>();

					String menuQuery = "SELECT NVL(REGEXP_SUBSTR (DFM.SCREEN_ACTION, '[^,]+', 1,2 ), 'N') FROM SME_FUNCTION_M DFM,GROUP_MENU_M GMM WHERE DFM.ID = GMM.ID AND UPPER(GMM.USR_GROUP) = UPPER(:userGroup)";
					Query menuQueryObj = entityManager.createNativeQuery(menuQuery);
					menuQueryObj.setParameter("userGroup", currUserGroup);
					List<String> menuActions = menuQueryObj.getResultList();

					int menuCount = 0;
					for (String menuActionName : menuActions) {
						if (!menuActionName.equals("N")) {
							menuActionName = menuActionName.replace("/", "").trim();
							myCurrList.add(menuActionName);
							menuCount++;
						}
					}

					totalMenuCount += menuCount;
					userMenuList.put(currUserGroup, myCurrList);
				}

				totalGroupCount++;
			}

			log.info("############ Total Group Count - " + totalGroupCount + " Loaded And Total Menu Count Loaded - "
					+ totalMenuCount + " ############");

			int masterPageMenuCount = 0;
			String masterPageMenuQuery = "SELECT NVL(REGEXP_SUBSTR (DFM.SCREEN_ACTION, '[^,]+', 1,2 ), 'N') SCREEN_ACTION from SME_FUNCTION_M DFM ORDER BY DFM.ID";
			Query masterPageMenuQueryObj = entityManager.createNativeQuery(masterPageMenuQuery);
			List<String> pageMenus = masterPageMenuQueryObj.getResultList();

			for (String pageMenu : pageMenus) {
				if (!pageMenu.equals("N")) {
					pageMenu = pageMenu.replace("/", "").trim();
					masterPageMenuList.add(pageMenu);
					masterPageMenuCount++;
				}
			}

			log.info("############ Total " + masterPageMenuCount + " Master Page Menu Loaded ############");

		} catch (Exception e) {
			log.error("Exception occurred while preparing user menu list", e);
		}
	}

	public synchronized void ServiceGenerater() {

		log.info("ServiceGenerater function ");

		date = new Date();
		sdf = new SimpleDateFormat("yyyy-dd-MM hh:mm:ss");// 2001-10-26T21:32:52
		sdff = new SimpleDateFormat("dd-MMM-yyyy");// 05-MAY-2016
		try {
			UPLOAD_PATH = ApplicationGenParameter.parameterMasterList.get("UPLOAD_PATH").trim();
			AD_AUTHENTICATION = ApplicationGenParameter.parameterMasterList.get("AD_AUTHENTICATION").trim();
			BULK_FILE_UPLOAD_PATH = ApplicationGenParameter.parameterMasterList.get("BULK_FILE_UPLOAD_PATH").trim();
			PROMO_IMG_UP_PATH = ApplicationGenParameter.parameterMasterList.get("PROMO_IMG_UP_PATH").trim();
			LINE_ID = ApplicationGenParameter.parameterMasterList.get("LINE_ID").trim();
			MAIN_LINE = ApplicationGenParameter.parameterMasterList.get("MAIN_LINE").trim();
			SESSION_TIME = ApplicationGenParameter.parameterMasterList.get("SESSION_TIME").trim();
			OTP_REGENERATION_TIME = ApplicationGenParameter.parameterMasterList.get("OTP_REGENERATION_TIME").trim();
			MAKER_DAILY_LIMIT = ApplicationGenParameter.parameterMasterList.get("MAKER_DAILY_LIMIT").trim();

			/* For Authrorization Purpose */
			Authorization = ApplicationGenParameter.parameterMasterList.get("AUTHORIZATION_KEY").trim();

			EMAIL_SEND_FLAG = ApplicationGenParameter.parameterMasterList.get("EMAIL_SEND_FLAG").trim();

			NEFT_CUTOFF = ApplicationGenParameter.parameterMasterList.get("NEFT_CUTOFF").trim();

			TRANSACTION_TIMEOUT = ApplicationGenParameter.parameterMasterList.get("TRANSACTION_TIMEOUT").trim();

			RTGS_CUTOFF = ApplicationGenParameter.parameterMasterList.get("RTGS_CUTOFF").trim();

			IMPS_LIMIT = ApplicationGenParameter.parameterMasterList.get("IMPS_LIMIT").trim();

			IMPS_LIMIT_VALIDATION = ApplicationGenParameter.parameterMasterList.get("IMPS_LIMIT_VALIDATION").trim();

			ADHOC_LIMIT = ApplicationGenParameter.parameterMasterList.get("ADHOC_LIMIT").trim();

			USER_MAKER = ApplicationGenParameter.parameterMasterList.get("MAKER").trim();

			USER_CHECKER = ApplicationGenParameter.parameterMasterList.get("CHECKER").trim();

			LOGIN_TYPE_P = ApplicationGenParameter.parameterMasterList.get("LOGIN_TYPE_P").trim();

			LOGIN_TYPE_M = ApplicationGenParameter.parameterMasterList.get("LOGIN_TYPE_M").trim();

			CAPTCHA_URL = ApplicationGenParameter.parameterMasterList.get("CAPTCHA_URL").trim();

			CAPTCHA_SECREAT_KEY = ApplicationGenParameter.parameterMasterList.get("CAPTCHA_SECREAT_KEY").trim();

			CAPTCHA_FLAG = ApplicationGenParameter.parameterMasterList.get("CAPTCHA_FLAG").trim();

			AGENT_ID_FOR_PORTAL = ApplicationGenParameter.parameterMasterList.get("BILLDESK_AGENT_ID_PORTAL").trim();

			AGENT_ID_FOR_MOBILE = ApplicationGenParameter.parameterMasterList.get("BILLDESK_AGENT_ID_MOBILE").trim();

			BILLDESK_CHANNEL_ID_PORTAL = ApplicationGenParameter.parameterMasterList.get("BILLDESK_CHANNEL_ID_PORTAL")
					.trim();

			BILLDESK_CHANNEL_ID_MOBILE = ApplicationGenParameter.parameterMasterList.get("BILLDESK_CHANNEL_ID_MOBILE")
					.trim();

			// Added By Swapni for Applied Limit On Transaction Creation By Maker

			MAKER_TRANSACTION_LIMIT = ApplicationGenParameter.parameterMasterList.get("MAKER_TRANSACTION_LIMIT").trim();

			BENE_COOLING_MINUTES = parameterMasterList.get("BENE_COOLING_MINUTES") != null
					? parameterMasterList.get("BENE_COOLING_MINUTES").trim()
					: "";// validate cooling period CR----29-03-2023---2459
			SMS_CONTENT_CHECKER = parameterMasterList.get("SMS_CONTENT_CHECKER") != null
					? parameterMasterList.get("SMS_CONTENT_CHECKER").trim()
					: "";// SMS communication to All Checkers CR----29-03-2023---2459
			SMS_SEND_FLAG_CHECKER = parameterMasterList.get("SMS_SEND_FLAG_CHECKER") != null
					? parameterMasterList.get("SMS_SEND_FLAG_CHECKER").trim()
					: "";// SMS communication to All Checkers CR----29-03-2023---2459
			FRMSTATUS = parameterMasterList.get("FRMSTATUS") != null ? parameterMasterList.get("FRMSTATUS").trim() : "";// FRM
																														// integration----2459
			FRMENQUEURL = parameterMasterList.get("FRMENQUEURL") != null ? parameterMasterList.get("FRMENQUEURL").trim()
					: "";// FRM integration----2459
			FRMRDURL = parameterMasterList.get("FRMRDURL") != null ? parameterMasterList.get("FRMRDURL").trim() : "";// FRM
																														// integration----2459
			SYSTEM_USER_ID = parameterMasterList.get("SYSTEM_USER_ID") != null
					? parameterMasterList.get("SYSTEM_USER_ID").trim()
					: "";// FRM integration May 2023----2459
			SMBND = parameterMasterList.get("SMBND") != null ? parameterMasterList.get("SMBND").trim() : "";// Sim
																											// Binding
																											// 2023----2459
			SMBVMN = parameterMasterList.get("SMBVMN") != null ? parameterMasterList.get("SMBVMN").trim() : "";// Sim
																												// Binding
																												// 2023----2459
			SMBFRQTM = parameterMasterList.get("SMBFRQTM") != null ? parameterMasterList.get("SMBFRQTM").trim() : "";// Sim
																														// Binding
																														// 2023----2459
			CONTENT_NOTE = parameterMasterList.get("CONTENT_NOTE") != null
					? parameterMasterList.get("CONTENT_NOTE").trim()
					: "";// Used for TAT CR Conta Note
			CONTACT_US_DTL = parameterMasterList.get("CONTACT_US_DTL") != null
					? parameterMasterList.get("CONTACT_US_DTL").trim()
					: "";// Used for TAT CR Conta Note
			THIRD_PARTY_FD_PRODUCT_CODE = parameterMasterList.get("THIRD_PARTY_FD_PRODUCT_CODE") != null
					? parameterMasterList.get("THIRD_PARTY_FD_PRODUCT_CODE").trim()
					: "";// Third Party FD Change

			DEFAULT_LIMIT_SETUP = parameterMasterList.get("DEFAULT_LIMIT_SETUP") != null
					? parameterMasterList.get("DEFAULT_LIMIT_SETUP").trim()
					: "";// Default limit setup
			DEFAULT_LIMIT_SETUP_FLAG = parameterMasterList.get("DEFAULT_LIMIT_SETUP_FLAG") != null
					? parameterMasterList.get("DEFAULT_LIMIT_SETUP_FLAG").trim()
					: "";// Default limit setup

			PARTNERKEY = ApplicationGenParameter.parameterMasterList.get("PARTNERKEY").trim();
			TURNOVERTYPE = ApplicationGenParameter.parameterMasterList.get("TURNOVERTYPE").trim();
			ACCEPTANCETYPE = ApplicationGenParameter.parameterMasterList.get("ACCEPTANCETYPE").trim();
			UPITXNLIMIT = ApplicationGenParameter.parameterMasterList.get("UPITXNLIMIT").trim();
			VALIDUPTOJSON = ApplicationGenParameter.parameterMasterList.get("VALIDUPTOJSON").trim();
			UPIKEYPATH = ApplicationGenParameter.parameterMasterList.get("UPIKEYPATH").trim();
			UPIURL = ApplicationGenParameter.parameterMasterList.get("UPIURL").trim();
			UPIPRXIP = parameterMasterList.get("UPIPRXIP") != null ? parameterMasterList.get("UPIPRXIP").trim() : "";// UPI
																														// Intent
			UPIPRXPRT = parameterMasterList.get("UPIPRXPRT") != null ? parameterMasterList.get("UPIPRXPRT").trim() : "";// UPI
																														// Intent
			// UPI_PAYMENT_LINK =parameterMasterList.get("UPI_PAYMENT_LINK")!=null?
			// parameterMasterList.get("UPI_PAYMENT_LINK").trim():"";// UPI Intent
			// INTENT_SME_SEND_FLAG = parameterMasterList.get("INTENT_SME_SEND_FLAG")!=null?
			// parameterMasterList.get("INTENT_SME_SEND_FLAG").trim():"";// UPI Intent
			// INTENT_SME_CONTENT = parameterMasterList.get("INTENT_SME_CONTENT")!=null?
			// parameterMasterList.get("INTENT_SME_CONTENT").trim():"";// UPI Intent
			// INTENT_EMAIL_SEND_FLAG =
			// parameterMasterList.get("INTENT_EMAIL_SEND_FLAG")!=null?
			// parameterMasterList.get("INTENT_EMAIL_SEND_FLAG").trim():"";// UPI Intent
			// INTENT_EMAIL_CONTENT = parameterMasterList.get("INTENT_EMAIL_CONTENT")!=null?
			// parameterMasterList.get("INTENT_EMAIL_CONTENT").trim():"";// UPI Intent
			// INTENT_EMAIL_SUBJECT = parameterMasterList.get("INTENT_EMAIL_SUBJECT")!=null?
			// parameterMasterList.get("INTENT_EMAIL_SUBJECT").trim():"";// UPI Intent
			// VALID_INT_UPTOJSON =
			// ApplicationGenParameter.parameterMasterList.get("VALID_INT_UPTOJSON").trim();

			YES_AMAZON_OTP_FLAG = ApplicationGenParameter.parameterMasterList.get("YES_AMAZON_OTP_FLAG").trim();
			YES_AMAZON_UPLOAD_PATH = ApplicationGenParameter.parameterMasterList.get("YES_AMAZON_UPLOAD_PATH").trim();
			YES_AMAZON_SSL = ApplicationGenParameter.parameterMasterList.get("YES_AMAZON_SSL").trim();
			YES_AMAZON_HOST = ApplicationGenParameter.parameterMasterList.get("YES_AMAZON_HOST").trim();
			YES_AMAZON_PORT = ApplicationGenParameter.parameterMasterList.get("YES_AMAZON_PORT").trim();
			YES_AMAZON_SSLPORT = ApplicationGenParameter.parameterMasterList.get("YES_AMAZON_SSLPORT").trim();
			YES_AMAZON_TO = ApplicationGenParameter.parameterMasterList.get("YES_AMAZON_TO").trim();
			YES_AMAZON_FROM = ApplicationGenParameter.parameterMasterList.get("YES_AMAZON_FROM").trim();
			YESAMAZON_BANK_URL = ApplicationGenParameter.parameterMasterList.get("YESAMAZON_BANK_URL").trim();
			YESAMAZON_PASSWORD = ApplicationGenParameter.parameterMasterList.get("YESAMAZON_PASSWORD").trim();
			log.info("PASSWORD_EXPIRY_DURATION===");
			PASSWORD_EXPIRY_DAYS = ApplicationGenParameter.parameterMasterList.get("PASSWORD_EXPIRY_DURATION").trim();
			if (ApplicationGenParameter.parameterMasterList.get("SERVICE_TYPE").trim() != null) {
				log.info("IF SERVICE_TYPE Before===" + SERVICE_TYPE);
				SERVICE_TYPE = ApplicationGenParameter.parameterMasterList.get("SERVICE_TYPE").trim();
				log.info("SERVICE_TYPE After===" + SERVICE_TYPE);
			} else {
				log.info("Else SERVICE_TYPE Before===" + SERVICE_TYPE);
				SERVICE_TYPE = "false";
				log.info("SERVICE_TYPE After===" + SERVICE_TYPE);
			}
			/* COMMENT FOR LIVE */
			REGGULAR_EXPRESION_ALPHA = ApplicationGenParameter.parameterMasterList.get("REG_EX_PATTERN_ALPHA").trim();
			REGGULAR_EXPRESION_NUM = ApplicationGenParameter.parameterMasterList.get("REG_EX_PATTERN_NUM").trim();

			// ADDED BY PRABHAT FOR NEWGEN ACTIVITY ON 06-03-2018 CASE_INITITIATION_URL
			// DOC_UPLOAD_URL SOAP_PARAMETER_LIST
			CASE_INITITIATION_URL = ApplicationGenParameter.parameterMasterList.get("CASE_INITIATION_URL").trim();

			UPLOAD_DOCUMENT_URL = ApplicationGenParameter.parameterMasterList.get("UPLOAD_DOCUMENT_URL").trim();
			log.info("UPLOAD_DOCUMENT_URL :");
			SOAP_PARAMETER_LIST = ApplicationGenParameter.parameterMasterList.get("SOAP_PARAMETER_LIST").trim();

			byte[] valueDecoded = Base64.decodeBase64(SOAP_PARAMETER_LIST);

			SOAP_PARAMETER_LIST = new String(valueDecoded);

			soapParamMapList = new HashMap<String, String>();
			String[] soapArrayList = SOAP_PARAMETER_LIST.split("\\|");
			for (String value : soapArrayList) {
				soapParamMapList.put(value.split("\\~")[0], value.split("\\~")[1]);
			}

			// FOr purging date added by prabhat om 12-03-2018
			PURGE_ACTIVITY_DAYS = ApplicationGenParameter.parameterMasterList.get("PURGE_ACTIVITY_DAYS").trim();

			// FOr bulk bene file path date added by neha 18-04-2018
			BULK_BENE_FILE_UPLOAD_PATH = ApplicationGenParameter.parameterMasterList.get("BULK_BENE_FILE_UPLOAD_PATH")
					.trim();

			// FOr bene GST data added by neha 24-04-2018
			GST_TAXDATA = ApplicationGenParameter.parameterMasterList.get("GST_TAXDATA").trim();

			//// FOr FORGOT_PASS_LINK data added by neha 06-06-2018
			FORGOT_PASS_LINK = ApplicationGenParameter.parameterMasterList.get("FORGOT_PASS_LINK").trim();
			/*
			 * SERVER_REQUSTER_ID = ApplicationGenParameter.parameterMasterList
			 * .get("SERVER_REQUSTER_ID").trim();
			 */
			// For APK Version

		} catch (Exception e) {
			log.info("Exception===", e);
		}
	}

	public void docNameIdMapList() {
		log.info("############ In docNameIdMapList() of ApplicationGenParameter ############");

		if (docNameIdMap == null) {
			docNameIdMap = new HashMap<>();
		}

		String sql = "select DOC_CODE_NAME, DOC_NAME from RC_COM_DOC_NAME where ACTIVE_FLAG = 'Y'";

		try {
			List<Object[]> resultList = entityManager.createNativeQuery(sql).getResultList();

			for (Object[] result : resultList) {
				String docCode = (String) result[0];
				String docName = ((String) result[1]).trim();

				docNameIdMap.put(docCode, docName);
			}

			log.info("############ Total " + resultList.size() + " Document Name-Id Mapping Loaded ############");

		} catch (Exception e) {
			log.error("Exception occurred during docNameIdMapList", e);
			throw new RuntimeException("Failed to initialize docNameIdMap", e);
		}
	}

	public void stateNameMapList() {
		log.info("############ In stateNameMapList() of ApplicationGenParameter ############");

		if (stateMapUpi == null) {
			stateMapUpi = new HashMap<>();
		}

		String sql = "SELECT STATE_CODE, STATE_NAME FROM SME_UPI_STATE_MST WHERE ACTIVE_FLAG = 'Y'";

		try {
			List<Object[]> resultList = entityManager.createNativeQuery(sql).getResultList();

			for (Object[] result : resultList) {
				Integer stateCode = ((Number) result[0]).intValue(); // Cast to Integer
				String stateName = (String) result[1]; // Cast to String
				stateMapUpi.put(stateName, stateCode.toString()); // Store in map
			}

			log.info("############ Total " + resultList.size() + " State Name Mapping Info Loaded ############");

		} catch (Exception e) {
			log.error("Exception occurred during stateNameMapList", e);
			throw new RuntimeException("Failed to initialize stateMapUpi", e);
		}
	}

	public void ownershipMapList() {
		log.info("############ In ownershipMapList() of ApplicationGenParameter ############");

		if (ownershipMapUpi == null) {
			ownershipMapUpi = new HashMap<>();
		}

		String sql = "SELECT FLG_CUST_TYP, OWNERSHIPTYPE FROM SME_UPI_OWNER_MST WHERE ACTIVE_FLAG = 'Y'";

		try {
			List<Object[]> resultList = entityManager.createNativeQuery(sql).getResultList();

			for (Object[] result : resultList) {
				String flagCustType = (String) result[0];
				String ownerShip = (String) result[1];
				ownershipMapUpi.put(flagCustType, ownerShip);
			}

			log.info("############ Total " + resultList.size() + " Ownership Mapping Info Loaded ############");

		} catch (Exception e) {
			log.error("Exception occurred during ownershipMapList", e);
			throw new RuntimeException("Failed to initialize ownershipMapUpi", e);
		}
	}

	public void mccUPIMapList() {
		log.info("############ In mccUPIMapList() of ApplicationGenParameter ############");

		if (mccMapInner == null) {
			mccMapInner = new HashMap<>();
		}

		String sql = "SELECT MCC_CODE, MCC_DESC FROM SME_UPI_MCC_MST WHERE ACTIVE_FLAG = 'Y' AND AUTH_STATUS = 'A' ORDER BY MCC_CODE";

		try {
			List<Object[]> resultList = entityManager.createNativeQuery(sql).getResultList();

			for (Object[] result : resultList) {
				Integer mccCode = ((Number) result[0]).intValue(); // Cast MCC_CODE to Integer
				String mccName = (String) result[1]; // Cast MCC_DESC to String

				// Left pad MCC_CODE with zeros to ensure consistency
				String paddedMccCode = StringUtils.leftPad(String.valueOf(mccCode), 4, "0");

				mccMapInner.put(paddedMccCode, mccName);
			}

			log.info("############ Total " + resultList.size() + " MCC UPI Mapping Info Loaded ############");

		} catch (Exception e) {
			log.error("Exception occurred during mccUPIMapList", e);
			throw new RuntimeException("Failed to initialize mccMapInner", e);
		}
	}

	// fOR tHRAED MAPPING ADDED BY PRABHAT ON 09-03-2018

	public void getServerListFromDB() {
		log.info("############ In getServerListFromDB() of ApplicationGenParameter ############");

		if (activeServerList == null) {
			activeServerList = new HashMap<>();
		}

		if (serverNameTimeFreqList == null) {
			serverNameTimeFreqList = new HashMap<>();
		}

		String sql = "SELECT DISTINCT THREAD_ID, SERVER_IP, THREAD_NAME FROM SME_THREAD_DTL WHERE THREAD_ACTIVE_STATUS = 'A'";
		try {
			List<Object[]> resultList = entityManager.createNativeQuery(sql).getResultList();

			for (Object[] result : resultList) {
				String threadId = (String) result[0] + "~" + (String) result[2];
				String threadIp = ((String) result[1]).trim();
				log.info("ThreadId=" + threadId + ", ThreadIp=" + threadIp);
				activeServerList.put(threadId, threadIp);
			}

			log.info("############ Total " + resultList.size() + " Active Servers Loaded ############");

			// Fetching server execution details
			sql = "SELECT EXECUTION_TIME, FREQUENCY, REPORT_CODE FROM SME_THREAD_REQUEST_DTL";
			resultList = entityManager.createNativeQuery(sql).getResultList();

			for (Object[] result : resultList) {
				String executionTime = (String) result[0];
				BigDecimal frequency = (BigDecimal) result[1];
				String reportCode = (String) result[2]; // This line causes the ClassCastException

				String execTimeFreq = executionTime.toString() + "~" + frequency.toString();
				serverNameTimeFreqList.put(reportCode, execTimeFreq);
			}

			log.info("############ Total " + resultList.size() + " Server Execution Details Loaded ############");

		} catch (Exception e) {
			log.error("Exception occurred during getServerListFromDB", e);
			throw new RuntimeException("Failed to initialize server lists", e);
		}
	}

	public void messageMasterList() {
		log.info("############ In messageMasterList() of ApplicationGenParameter ############");

		if (messageMasterList == null) {
			messageMasterList = new HashMap<>();
		}

		String sql = "select M.MESSAGE_CODE, M.MESSAGE_TAG_NAME, M.MESSAGE_BODY from SME_MESSAGE_MST M where M.ACTIVE_FLAG = 'A'";

		try {
			List<Object[]> resultList = entityManager.createNativeQuery(sql).getResultList();

			int count = 0;
			for (Object[] result : resultList) {
				String messageCode = (String) result[0];
				String messageTag = ((String) result[1]).trim();
				String messageName = ((String) result[2]).trim();

				messageMasterList.put(messageCode, messageTag + "|" + messageName);
				count++;
			}

			log.info("############ Total " + count + " Exception Message Info Loaded ############");

		} catch (Exception e) {
			log.error("Exception occurred during messageMasterList", e);
			throw new RuntimeException("Failed to initialize messageMasterList", e);
		}
	}

	/* Added by Prabhat on 04-04-2018 */
	public void customerIpAddressMapList() {
		log.info("############ In customerIpAddressMapList() of ApplicationGenParameter ############");

		if (custIpAddMapList == null) {
			custIpAddMapList = new HashMap<>();
		}

		String sql = "SELECT M.CUSTOMER_IP, M.IP_ADDRESS FROM SME_CUSTOMER_IP_ADDRESS_DTL M";

		try {
			List<Object[]> resultList = entityManager.createNativeQuery(sql).getResultList();

			int count = 0;
			for (Object[] result : resultList) {
				String customerIp = (String) result[0];
				String ipAddress = (String) result[1];
				custIpAddMapList.put(customerIp, ipAddress);
				count++;
			}

			log.info("############ Total " + count + " Customer IP Address Info Loaded ############");

		} catch (Exception e) {
			log.error("Exception occurred during customerIpAddressMapList", e);
			throw new RuntimeException("Failed to initialize custIpAddMapList", e);
		}
	}

	public void apiMasterListGenerator() {
		log.info("############ In apiMasterListGenerator() of ApplicationGenParameter ############");

		if (apiMasterList == null) {
			apiMasterList = new HashMap<>();
		}

		String sql = "SELECT C.SERVICE_NAME, NVL(C.URL, 'NA') AS URL FROM COM_SERVICE_API_MASTER C WHERE C.SERVICE_TYPE = 'API' AND C.SERVICE_NAME <> 'NA'";

		try {
			List<Object[]> resultList = entityManager.createNativeQuery(sql).getResultList();

			int count = 0;
			for (Object[] result : resultList) {
				String serviceName = (String) result[0];
				String url = ((String) result[1]).trim();
				apiMasterList.put(serviceName, url);
				count++;
			}

			log.info("############ Total " + count + " API Master Info Loaded ############");

		} catch (Exception e) {
			log.error("Exception occurred during apiMasterListGenerator", e);
			throw new RuntimeException("Failed to initialize apiMasterList", e);
		}
	}
}
