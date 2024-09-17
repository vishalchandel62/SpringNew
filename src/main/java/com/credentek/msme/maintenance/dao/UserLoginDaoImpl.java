/**********************************************************************
*
* Module Name         :SMALL AND MEDIUM ENTERPRISES(SME)
*
* File Name           :UserLoginDaoImpl.Java
*                             
*            Version Control Block
*            ---------------------
* Date              Version      Author               Description
* Description         :
* ---------         --------   ---------------  ---------------------------
* FEB 11, 2022         3.1.0      Credentek Team       Response Model
**********************************************************************/

package com.credentek.msme.maintenance.dao;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.credentek.msme.ApplicationGenParm.ApplicationGenParameter;
import com.credentek.msme.api.dao.CustomerManagementDAO;
import com.credentek.msme.database.DaoBase;
import com.credentek.msme.logincontroller.LoginController;
import com.credentek.msme.loginmodel.MsmeSecurityLogDetail;
import com.credentek.msme.loginmodel.UserLogin;
import com.credentek.msme.utils.CommonUtility;
import com.credentek.msme.utils.HibernateUtil;
import com.credentek.msme.utils.PathRead;
import com.credentek.msme.utils.Utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import org.apache.logging.log4j.LogManager;


@Service
public class UserLoginDaoImpl extends DaoBase implements UserLoginDAO {

	private static final Logger log = LogManager.getLogger(UserLoginDaoImpl.class);
	PathRead path = PathRead.getInstance();
	public String DOC_FILE_PATH = ApplicationGenParameter.UPLOAD_PATH;
	public String default_limit_setup = ApplicationGenParameter.DEFAULT_LIMIT_SETUP; //Default limit setup
	static ResourceBundle resourceBundleEnvi = ResourceBundle.getBundle("EnvironmentFlag");
	static String environmentStr = resourceBundleEnvi.getString("ENVIRONMENT");
	
	
	

	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	private final EntityManager entityManager;
	
	@Autowired
	public UserLoginDaoImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	


	public net.sf.json.JSONObject checkDebitRegistration(UserLogin userlogin) {
		log.info("In checkDebitRegistration function "+userlogin.getRetailBankUserID());
		String debitNo = "";
		String debitPass = "";
		String rndMapId = "";
		boolean flag = false;
		net.sf.json.JSONObject m_res_header = new net.sf.json.JSONObject();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
			JSONObject s_res_header = new JSONObject();
			JSONObject m1_res_header = new JSONObject();

			if (userlogin.getRetailBankUserID().trim().matches("[0-9]+")) {
				// VALID_DEBIT_REG=select RNB_DEBIT_CARD_NO,RNB_DEBIT_CARD_PASS
				// from
				// COM_RNB_USER_MST where RNBID = ? and RNB_USER_STATUS = 'A'
				pstmt = connection.prepareStatement(getQuery("VALID_DEBIT_REG"));
				pstmt.setString(1, userlogin.getRetailBankUserID());
				rset = pstmt.executeQuery();
				if (rset.next()) {
					flag = true;
					debitNo = rset.getString(1);
					debitPass = rset.getString(2);
				}
				rset.close();
				pstmt.close(); //close for further use-2299
			} else {
				// VALID_DEBIT_REG_FOR_ALIAS=select
				// RNB_DEBIT_CARD_NO,RNB_DEBIT_CARD_PASS,EXPIRY_DATE,RNBID from
				// COM_RNB_USER_MST where ALIAS_NAME = ? and RNB_USER_STATUS =
				// 'A'
				pstmt = connection.prepareStatement(getQuery("VALID_DEBIT_REG_FOR_ALIAS"));
				pstmt.setString(1, userlogin.getRetailBankUserID());
				rset = pstmt.executeQuery();
				if (rset.next()) {
					flag = true;
					debitNo = rset.getString(1);
					debitPass = rset.getString(2);
					rndMapId = rset.getString(4);
				}
				rset.close();
				pstmt.close(); //close for further use-2299
			}

			//rset.close();
			//pstmt.close(); //no need to close stmt here

			if (!flag) {
				m_res_header.put("ResCode", "1");
				m_res_header.put("ErrorDetails", "Wrong RNB Credential.");
			} else {
				if (debitNo.equals(userlogin.getDebitCardNo()) && debitPass.equals(userlogin.getDebitCardPin())) {
					// Go for Success
					m1_res_header.put("Status", "SUCCESS");
					m1_res_header.put("CustomerID", userlogin.getRetailBankUserID());
					m1_res_header.put("RnbCustomerID", rndMapId);
					s_res_header.put("ValidateDCLoginRes", m1_res_header);
					m_res_header.put("ResCode", "0");
					m_res_header.put("ResBody", m1_res_header);
				} else {
					// Not a valid credential
					m_res_header.put("ResCode", "1");
					m_res_header.put("ErrorDetails", "Wrong RNB Credential.");
				}
			}

		} catch (Exception e) {
			log.info("Exception ===", e);
		} finally {

			try {
				if (rset != null) {
					rset.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null ) {
					closeConnection(connection,new Object(){}.getClass().getEnclosingMethod().getName().toString());
				}
			} catch (SQLException e) {
				log.info("SQLException ===", e);
			}
		}

		return m_res_header;

	}

	public boolean userLogout(UserLogin userlogin) {
		return true;
	}

	public net.sf.json.JSONObject validateOTP(net.sf.json.JSONObject otpInfo) {
		
		String otpKey = "";
		String otpVal = "";
		boolean flag = false;
		net.sf.json.JSONObject m_res_header = new net.sf.json.JSONObject();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try
		{
		// PAN CARD MASKING FOR APPSEC POINT
		// Implementation of PAN Number Masking in the Application Logs
		
		net.sf.json.JSONObject duplicatecustInfo1 = new net.sf.json.JSONObject();
		duplicatecustInfo1.put("otpInfo",otpInfo);
		
		if(duplicatecustInfo1.getJSONObject("otpInfo").has("PAN"))
		{
			duplicatecustInfo1.getJSONObject("otpInfo").replace("PAN",duplicatecustInfo1.getJSONObject("otpInfo").getString("PAN").replaceAll("\\w(?=\\w{4})", "*"));
			if(duplicatecustInfo1.getJSONObject("otpInfo").has("reqObject"))
			{
				duplicatecustInfo1.getJSONObject("otpInfo").getJSONObject("reqObject").replace("PAN",duplicatecustInfo1.getJSONObject("otpInfo").getJSONObject("reqObject").getString("PAN").replaceAll("\\w(?=\\w{4})", "*"));
			}
		}
		
		
		log.info("In validateOTP function ->"+duplicatecustInfo1);
		
		}
		catch(Exception e)
		{
			log.error("Exception in PAN card masking"+e);
		}
		
		
		try {
			connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
			JSONObject s_res_header = new JSONObject();
			JSONObject m1_res_header = new JSONObject();
			// VERIFY_OTP=select OTP_KEY,OTP_VALUE from COM_RNB_USER_MST where
			// RNBID = ?
			pstmt = connection.prepareStatement(getQuery("VERIFY_OTP"));
			pstmt.setString(1, otpInfo.getString("CustomerId"));
			rset = pstmt.executeQuery();
			if (rset.next()) {
				flag = true;
				otpKey = rset.getString(1);
				otpVal = rset.getString(2);
			}
			rset.close();
			pstmt.close();
			if (!flag) {
				// Not a valid RNB credential
				
				m_res_header.put("ResCode", "1");
				m_res_header.put("ErrorDetails", "Invalid OTP Key");
			} else {
				if (otpKey.equals(otpInfo.getString("otpKey")) && otpVal.equals(otpInfo.getString("otpValue"))) {
					// Go for Success
					m1_res_header.put("version", "1");
					

					m1_res_header.put("isValid", "true");
					m1_res_header.put("verificationFaultReason", "");
					s_res_header.put("verifyOTPResponse", m1_res_header);

					m_res_header.put("ResCode", "0");
					m_res_header.put("ResBody", s_res_header);

					
				} else {
					// Not a valid credential
					m1_res_header.put("version", "1");
					m1_res_header.put("isValid", "false");
					m1_res_header.put("verificationFaultReason", "Invalid OTP Key");
					s_res_header.put("verifyOTPResponse", m1_res_header);

					m_res_header.put("ResCode", "0"); // changed by Rahul on
														// 06092018
					m_res_header.put("ResBody", s_res_header);
				}
			}
			log.info("m_res_header ===");

		} catch (Exception e) {
			log.info("Exception ===", e);
		} finally {

			try {
				if (rset != null) {
					rset.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null ) {
					closeConnection(connection,new Object(){}.getClass().getEnclosingMethod().getName().toString());
				}
			} catch (SQLException e) {
				log.info("SQLException ===", e);
			}
		}

		return m_res_header;

	}
	

	public boolean changePassword(net.sf.json.JSONObject makerInfo) {
		log.info("Inside changePassword :::");
		boolean flag = false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
			// {"loginID", "password"}
			// CAHNGE_PASSWORD_MAKER=update SME_APP_LOGIN_INFO set PASSWORD = ?
			// FIRST_LOGIN = 'N' where LOGIN_ID = ?
			pstmt = connection.prepareStatement("update SME_APP_LOGIN_INFO set PASSWORD = ?, FIRST_LOGIN = 'N',LAST_REQUEST_TIME = sysdate where LOGIN_ID = ?");
			pstmt.setString(1, makerInfo.getString("password"));
			pstmt.setString(2, makerInfo.getString("loginID").trim().toUpperCase());
			pstmt.executeUpdate();
			pstmt.close();

			// Added by ptrabhat on 22-01-2018
			Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, Integer.parseInt(ApplicationGenParameter.PASSWORD_EXPIRY_DAYS));
			String[] dateArray = new String[5];
			// Added by ptrabhat on 22-01-2018
			int regCount = 0;
			
			// CHECK Registration date table
			pstmt = connection.prepareStatement("SELECT COUNT(*),TO_CHAR(PORTAL_REG_DATE, 'DD-MON-YYYY HH24:MI:SS'),TO_CHAR(MOBILE_REG_DATE, 'DD-MON-YYYY HH24:MI:SS'),TO_CHAR(REMAIN_DATE_FOR_EXPIRE_M, 'DD-MON-YYYY HH24:MI:SS'),TO_CHAR(REMAIN_DATE_FOR_EXPIRE_P, 'DD-MON-YYYY HH24:MI:SS') FROM SME_REGISTRATION_DATE_DTL WHERE REG_ID = ? group by PORTAL_REG_DATE,MOBILE_REG_DATE,REMAIN_DATE_FOR_EXPIRE_M,REMAIN_DATE_FOR_EXPIRE_P");
			pstmt.setString(1, makerInfo.getString("loginID").trim().toUpperCase());
			rset = pstmt.executeQuery();
			if (rset.next()) {
				regCount = rset.getInt(1);
				dateArray[1] = rset.getString(2);// PORTAL_REG_DATE
				dateArray[2] = rset.getString(3);// MOBILE_REG_DATE
				dateArray[3] = rset.getString(4);// REMAIN_DATE_FOR_EXPIRE_M
				dateArray[4] = rset.getString(5);// REMAIN_DATE_FOR_EXPIRE_P

			}
			rset.close();
			pstmt.close();

			if (regCount > 0) {
				pstmt = connection.prepareStatement(getQuery("UPDATE_SME_REGISTRATION_DATE_DTL"));
				pstmt.setString(1, dateArray[1]);
				pstmt.setString(2, "" + Integer.parseInt(ApplicationGenParameter.PASSWORD_EXPIRY_DAYS));
				pstmt.setString(3, dateArray[4]);
				pstmt.setString(4, makerInfo.getString("loginID").trim().toUpperCase());
				pstmt.executeUpdate();
				pstmt.close();
			} else {
				pstmt = connection.prepareStatement("UPDATE SME_REGISTRATION_DATE_DTL SET PORTAL_REG_DATE = TO_DATE(?, 'DD-MON-YYYY HH24:MI:SS'),MOBILE_REG_DATE =sysdate ,REMAIN_DATE_FOR_EXPIRE_M =sysdate+?, REMAIN_DATE_FOR_EXPIRE_P=TO_DATE(?, 'DD-MON-YYYY HH24:MI:SS') where REG_ID = ?");
				pstmt.setString(1, makerInfo.getString("loginID").trim().toUpperCase());
				pstmt.setString(2, null);
				pstmt.setString(3, "" + Integer.parseInt(ApplicationGenParameter.PASSWORD_EXPIRY_DAYS));
				pstmt.setString(4, null);
				pstmt.executeUpdate();
				pstmt.close();
			}

			flag = true;
		} catch (Exception e) {
			log.info("Exception ===", e);
		} finally {

			try {
				if (rset != null) {
					rset.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null ) {
					closeConnection(connection,new Object(){}.getClass().getEnclosingMethod().getName().toString());
				}
			} catch (SQLException e) {
				log.info("SQLException ===", e);
			}
		}
		return flag;
	}

	public boolean setMpinApin(JSONObject pinData) {
		log.info("In setMpin Function");
		boolean flag = false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		// String regID="";
		try {
			connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
			connection.setAutoCommit(false);

			UserLogin credentialData = new UserLogin();

			// SELECT_PORTAL_CREDENTIAL || REG_ID,PORTAL_PASS,USER_TYPE
			// 1 2 3 4 5 6 7 8
			// SELECT_PORTAL_CREDENTIAL_AT_APP_BINDING=select
			// REG_ID,PASSWORD,USER_TYPE,LOGIN_TYPE_PORTAL,LOGIN_DATE,LOGOUT_DATE,LOGOUT_MODE,EXCEPTION_CD,
			// 9 10 11 12 13 14 15 16 17 18
			// DEVICE_NAME,DEVICE_PLATFORM,DEVICE_UUID,DEVICE_VERSION,DEVICE_DATE,DEVICE_IMEI,TOKEN,LOGIN_NAME,LAST_REQUEST_TIME,LAST_FAILURE_ATTEMPT_TIME,
			// 19
			// FIRST_LOGIN from SME_APP_LOGIN_INFO where LOGIN_ID = ?
			pstmt = connection.prepareStatement(getQuery("SELECT_PORTAL_CREDENTIAL_AT_APP_BINDING"));
			pstmt.setString(1, pinData.getString("custID"));
			rset = pstmt.executeQuery();
			if (rset.next()) {
				credentialData.setRegID(rset.getString(1));
				credentialData.setPassword(rset.getString(2));
				credentialData.setUserType(rset.getString(3));
				credentialData.setLoginTypeFlag(rset.getString(4));
				credentialData.setLoginDate(rset.getString(5));
				credentialData.setLogOutDate(rset.getString(6));
				credentialData.setLogOutMode(rset.getString(7));
				credentialData.setExceptionCd(rset.getString(8));
				credentialData.setDeviceName(rset.getString(9));
				credentialData.setDevicePlat(rset.getString(10));
				credentialData.setDeviceID(rset.getString(11));
				credentialData.setDeviceVer(rset.getString(12));
				credentialData.setDeviceDate(rset.getString(13));
				credentialData.setIMEI_NO(rset.getString(14));
				credentialData.setToken(rset.getString(15));
				credentialData.setLoginID(rset.getString(16));
				credentialData.setLastReqTime(rset.getString(17));
				credentialData.setLastFailureAttemptTime(rset.getString(18));
				credentialData.setFirstLogin(rset.getString(19));
			}
			rset.close();
			pstmt.close();

			// Insert IMEI-PUBKEY data In main Table
			pstmt = connection.prepareStatement(getQuery("INSERT_KEY_FOR_IMEI_MAIN"));
			pstmt.setString(1, pinData.getString("token"));
			pstmt.executeUpdate();
			pstmt.close();

			// Insert Login Data data In main Table
			pstmt = connection.prepareStatement(getQuery("INSERT_LOGIN_DETAIL_MAIN"));
			pstmt.setString(1, pinData.getString("custID"));
			pstmt.executeUpdate();
			pstmt.close();

			// Insert Registration Data data In main Table
			pstmt = connection.prepareStatement(getQuery("INSERT_REG_CRED_MAIN"));
			pstmt.setString(1, pinData.getString("custID"));
			pstmt.executeUpdate();
			pstmt.close();

			// SET APIN and MPIN in Registration Main Table
			pstmt = connection.prepareStatement(getQuery("SET_REG_APIN_MPIN"));
			pstmt.setString(1, pinData.getString("encMpin"));
			pstmt.setString(2, pinData.getString("encApin"));
			pstmt.setString(3, pinData.getString("custID"));
			pstmt.executeUpdate();
			pstmt.close();

			if ("Y".equals(credentialData.getLoginTypeFlag())) {
				log.info("in credential ===" + credentialData.getLoginDate());
				// UPDATE_LOGIN_TABLE_FOR_PORTAL_AT_REG_IN_APP=UPDATE
				// SME_APP_LOGIN_INFO SET PORTAL_PASS = ?,PASSWORD =
				// ?,USER_TYPE=?,LOGIN_TYPE_PORTAL=?,
				// LOGIN_DATE=TO_DATE(?, 'DD-MON-YYYY
				// HH24:MI:SS'),LOGOUT_DATE=TO_DATE(?, 'DD-MON-YYYY
				// HH24:MI:SS'),LOGOUT_MODE=?,EXCEPTION_CD=?,
				// DEVICE_NAME=?,DEVICE_PLATFORM=?,DEVICE_UUID=?,DEVICE_VERSION=?,DEVICE_DATE=?,DEVICE_IMEI=?,TOKEN=?,LOGIN_NAME=?,
				// LAST_REQUEST_TIME=TO_DATE(?, 'DD-MON-YYYY
				// HH24:MI:SS'),LAST_FAILURE_ATTEMPT_TIME=TO_DATE(?,
				// 'DD-MON-YYYY HH24:MI:SS'),FIRST_LOGIN=? WHERE LOGIN_ID = ?
				pstmt = connection.prepareStatement(getQuery("UPDATE_LOGIN_TABLE_FOR_PORTAL_AT_REG_IN_APP"));
				pstmt.setString(1, credentialData.getPassword());// This is
																	// actually
																	// password
																	// for
																	// portal
				pstmt.setString(2, pinData.getString("encApin"));
				pstmt.setString(3, credentialData.getUserType());
				pstmt.setString(4, credentialData.getLoginTypeFlag());
				pstmt.setString(5, credentialData.getLoginDate());
				pstmt.setString(6, credentialData.getLogOutDate());
				pstmt.setString(7, credentialData.getLogOutMode());
				pstmt.setString(8, credentialData.getExceptionCd());
				pstmt.setString(9, credentialData.getDeviceName());
				pstmt.setString(10, credentialData.getDevicePlat());
				pstmt.setString(11, credentialData.getDeviceID());
				pstmt.setString(12, credentialData.getDeviceVer());
				pstmt.setString(13, credentialData.getDeviceDate());
				pstmt.setString(14, credentialData.getIMEI_NO());
				pstmt.setString(15, credentialData.getToken());
				pstmt.setString(16, credentialData.getLoginID());
				pstmt.setString(17, credentialData.getLastReqTime());
				pstmt.setString(18, credentialData.getLastFailureAttemptTime());
				pstmt.setString(19, credentialData.getFirstLogin());
				pstmt.setString(20, credentialData.getSessionID());
				pstmt.setString(21, pinData.getString("custID"));
				pstmt.executeUpdate();
				pstmt.close();

			} else {
				// To Update password in User_app_login_Table
				pstmt = connection.prepareStatement(getQuery("SET_PASS_IN_APP_LOGIN"));
				pstmt.setString(1, pinData.getString("encApin"));
				pstmt.setString(2, pinData.getString("custID"));
				pstmt.executeUpdate();
				pstmt.close();
			}

			// To Delete temp table After insertion in main Table
			pstmt = connection.prepareStatement(getQuery("DELETE_KEY_TMP_IF_EXIST1"));
			pstmt.setString(1, pinData.getString("token"));
			pstmt.executeUpdate();
			pstmt.close();

			pstmt = connection.prepareStatement(getQuery("DELETE_USER_TMP_IF_EXIST"));
			pstmt.setString(1, pinData.getString("custID"));
			pstmt.executeUpdate();
			pstmt.close();
			
			pstmt = connection.prepareStatement(getQuery("DELETE_DROP_OFF_CUSTOMER_DTL"));
			pstmt.setString(1, pinData.getString("custID"));
			pstmt.executeUpdate();
			pstmt.close();


			pstmt = connection.prepareStatement(getQuery("DELETE_APP_LOGIN_TMP_IF_EXIST"));
			pstmt.setString(1, pinData.getString("custID"));
			pstmt.executeUpdate();
			pstmt.close();

			connection.commit();
			
			flag = true;
		} catch (Exception e) {
			log.info("Exception ===", e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				log.info("########################:::Exception::::::", e1);
			}
		} finally {

			try {
				if (rset != null) {
					rset.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null ) {
					closeConnection(connection,new Object(){}.getClass().getEnclosingMethod().getName().toString());
				}
			} catch (SQLException e) {
				log.info("SQLException ===", e);
				try {
					connection.rollback();
				} catch (SQLException e1) {
					log.info("########################:::Exception::::::", e1);
				}

			}
		}
		return flag;
	}

	public JSONObject insertRegistrationDetailUpdated(UserLogin userlogin) {

		log.info("Inside insertRegistrationDetailUpdated userType Value:::" + userlogin.getUserType());
		UserLogin fetchDetails = new UserLogin();

		String regID = "";
		JSONObject resData = new JSONObject();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		log.info("For New Registration >>>");
		try {

			// insertIpAddress(connection,userlogin.getRetailBankUserID(),userlogin.getIpAddress());
			connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());

			connection.setAutoCommit(false);
			pstmt = connection.prepareStatement(getQuery("SELECT_REGI_ID"));
			rset = pstmt.executeQuery();
			if (rset.next()) {
				regID = rset.getString(1);
				log.info("Registration ID ===" + regID);
			}
			rset.close();
			pstmt.close();

			pstmt = connection.prepareStatement(getQuery("SELECT_USER_DAT_FROM_VERTUAL"));
			pstmt.setString(1, userlogin.getRetailBankUserID().trim());
			rset = pstmt.executeQuery();
			if (rset.next()) {

				fetchDetails.setRetailBankUserID(rset.getString("COD_CUST_ID"));
				fetchDetails.setUserName(rset.getString("NAM_CUST_FULL"));
				fetchDetails.setUserContactNo(rset.getString("REF_CUST_TELEX"));
				fetchDetails.setUserEmail(rset.getString("REF_CUST_EMAIL"));
				fetchDetails.setServiceRM(rset.getString("SERVICE_RM"));
				fetchDetails.setSSC(rset.getString("SSC"));
				fetchDetails.setAssetRM(rset.getString("ASSET_RM"));
				fetchDetails.setAssetRMemail(rset.getString("ASSET_RM_EMAILID"));
				fetchDetails.setAssetRMmob(rset.getString("ASSET_RM_MOB"));
				fetchDetails.setServRMemail(rset.getString("SERVICE_RM_EMAILID"));
				fetchDetails.setServRMmob(rset.getString("SERVICE_RM_MOB"));// ,ASSET_RM_NAME,SERVICE_RM_NAME
				fetchDetails.setAssetRMName(rset.getString("ASSET_RM_NAME"));
				fetchDetails.setServiceRMName(rset.getString("SERVICE_RM_NAME"));

			}

			rset.close();
			pstmt.close();

			pstmt = connection.prepareStatement(getQuery("CUSTOMER_COMPANY_ID"));
			pstmt.setString(1, userlogin.getRetailBankUserID().trim());
			rset = pstmt.executeQuery();
			if (rset.next()) {
				fetchDetails.setCompanyID(rset.getString(1));
			}
			rset.close();
			pstmt.close();

			// Delete Reg temp table if userid exists
			// DELETE_USER_TMP_IF_EXIST=DELETE SME_REG_USER_INFO_TMP where
			// CUST_ID =?
			pstmt = connection.prepareStatement(getQuery("DELETE_USER_TMP_IF_EXIST"));
			pstmt.setString(1, userlogin.getRetailBankUserID().trim());
			pstmt.executeUpdate();
			pstmt.close();
			
			pstmt = connection.prepareStatement(getQuery("DELETE_DROP_OFF_CUSTOMER_DTL"));
			pstmt.setString(1, userlogin.getRetailBankUserID().trim());
			pstmt.executeUpdate();
			pstmt.close();


			// RESISTER_USER_TMP=Insert into SME_REG_USER_INFO_TMP
			// (CUST_ID,CUST_NAME,COMPANY_ID,COMPANY_NAME,RM_ID,HAS_MAKER,REGISTERED_DT,REG_ID,REG_MODE)
			// values (?,?,?,?,?,?,sysdate,?,'MOBILE')
			pstmt = connection.prepareStatement(getQuery("RESISTER_USER_TMP"));
			// CUST_ID,CUST_NAME,COMPANY_ID,COMPANY_NAME,RM_ID,HAS_MAKER,REGISTERED_DT,REG_ID,REG_MODE
			pstmt.setString(1, userlogin.getRetailBankUserID().trim());
			pstmt.setString(2, fetchDetails.getUserName());
			pstmt.setString(3, fetchDetails.getCompanyID());
			pstmt.setString(4, "NA");
			pstmt.setString(5, fetchDetails.getServiceRM());
			pstmt.setString(6, "N");
			pstmt.setString(7, regID);
			pstmt.setString(8, userlogin.getUserType());
			// pstmt.setString(8, userlogin.getCompanyIDForAlisId().trim());
			pstmt.executeUpdate();
			pstmt.close();

			log.info("Registration Successful in tmp table");
			boolean forgotPasswordFlag = false;
			try{
				pstmt = connection.prepareStatement(getQuery("SELECT_APP_LOGIN_INFO_COUNT"));
				pstmt.setString(1, userlogin.getLoginID());
				rset =pstmt.executeQuery();
				while(rset.next()){
					if(rset.getInt(1) > 0)
						forgotPasswordFlag = true;
				}
			}
			catch(Exception e){
				log.info("Exception occuredwhile checking customer is registered or not "+e.getLocalizedMessage());
			}
			log.info("forgotpassword scenario "+forgotPasswordFlag);
			if(forgotPasswordFlag== false){
				try{
					log.info("2330 "+userlogin.getLoginID()+" "+userlogin.getCompanyID());
					pstmt = connection.prepareStatement(getQuery("INSERT_INTO_DROP_OFF_CUSTOMER_DTL"));
					pstmt.setString(1, userlogin.getLoginID());
					pstmt.setString(2, userlogin.getCompanyID());
					pstmt.setString(3, "NA");
					pstmt.setString(4, userlogin.getUserType());
					pstmt.setString(5, userlogin.getUserRegisterType());
					pstmt.executeUpdate();
					pstmt.close();
				}
				catch(Exception e){
					log.info("Exception occured while creating entry in drop off table "+e);
				}
			}
			// Delete Login temp table if userid exists
			// DELETE_APP_LOGIN_TMP_IF_EXIST=DELETE SME_APP_LOGIN_INFO_TMP where
			// LOGIN_ID =?
			pstmt = connection.prepareStatement(getQuery("DELETE_APP_LOGIN_TMP_IF_EXIST"));
			pstmt.setString(1, userlogin.getRetailBankUserID().trim());
			pstmt.executeUpdate();
			pstmt.close();

			log.info(":::get userType:::::<<<<::::" + userlogin.getUserType());
			// INSERT_APP_LOGIN_DETAILS_REG_TIME_TMP_MOBILE=Insert into
			// SME_APP_LOGIN_INFO_TMP
			// (LOGIN_ID,USERNAME,PASSWORD,DEVICE_NAME,DEVICE_PLATFORM,DEVICE_UUID,DEVICE_VERSION,DEVICE_DATE,DEVICE_IMEI,TOKEN,LOGIN_NAME,REG_ID,USER_TYPE,LOGIN_TYPE_MOBILE)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)
			// INSERT_APP_LOGIN_DETAILS_REG_TIME_TMP_PORTAL=Insert into
			// SME_APP_LOGIN_INFO_TMP
			// (LOGIN_ID,USERNAME,PASSWORD,DEVICE_NAME,DEVICE_PLATFORM,DEVICE_UUID,DEVICE_VERSION,DEVICE_DATE,DEVICE_IMEI,TOKEN,LOGIN_NAME,REG_ID,USER_TYPE,LOGIN_TYPE_PORTAL)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)
			if ("MOBILE".equals(userlogin.getUserType())) {
				pstmt = connection.prepareStatement(getQuery("INSERT_APP_LOGIN_DETAILS_REG_TIME_TMP_MOBILE"));
			} else {
				// Insert into SME_APP_LOGIN_INFO_TMP
				// (LOGIN_ID,USERNAME,PASSWORD,DEVICE_NAME,DEVICE_PLATFORM,DEVICE_UUID,DEVICE_VERSION,DEVICE_DATE,DEVICE_IMEI,TOKEN,LOGIN_NAME,REG_ID,USER_TYPE,LOGIN_TYPE_PORTAL)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)
				pstmt = connection.prepareStatement(getQuery("INSERT_APP_LOGIN_DETAILS_REG_TIME_TMP_PORTAL"));
			}

			// INSERT_APP_LOGIN_DETAILS_REG_TIME_TMP= Insert into
			// SME_APP_LOGIN_INFO_TMP
			// (LOGIN_ID,USERNAME,PASSWORD,DEVICE_NAME,DEVICE_PLATFORM,DEVICE_UUID,DEVICE_VERSION,DEVICE_DATE,DEVICE_IMEI,TOKEN,LOGIN_NAME,REG_ID)values
			// (?,?,?,?,?,?,?,?,?,?,?,?)

			pstmt.setString(1, userlogin.getRetailBankUserID().trim());
			pstmt.setString(2, fetchDetails.getUserName());
			pstmt.setString(3, "");
			pstmt.setString(4, userlogin.getDeviceName());
			pstmt.setString(5, userlogin.getDevicePlat());
			pstmt.setString(6, userlogin.getDeviceID());
			pstmt.setString(7, userlogin.getDeviceVer());
			pstmt.setString(8, "");
			pstmt.setString(9, userlogin.getIMEI_NO());
			pstmt.setString(10, userlogin.getToken());
			pstmt.setString(11, fetchDetails.getUserName());
			pstmt.setString(12, regID);
			pstmt.setString(13, "A");
			pstmt.setString(14, "Y");

			pstmt.executeUpdate();
			pstmt.close();

			log.info("App Detail Inserted In APP TMP Login Table Successful");

			resData.put("code", "E108");
			resData.put("CustName", fetchDetails.getUserName());
			resData.put("CustContact", fetchDetails.getUserContactNo());
			resData.put("CustEmail", fetchDetails.getUserEmail());
			resData.put("ServRMID", fetchDetails.getServiceRM());
			resData.put("ServMobRM", fetchDetails.getServRMmob());
			resData.put("ServEmailRM", fetchDetails.getServRMemail());
			resData.put("AssetRMID", fetchDetails.getAssetRM());
			resData.put("AssetMobRM", fetchDetails.getAssetRMmob());
			resData.put("AssetEmailRM", fetchDetails.getAssetRMemail());
			resData.put("CompanyID", fetchDetails.getCompanyID());
			resData.put("AssetRmName", fetchDetails.getAssetRMName());
			resData.put("ServiceRmName", fetchDetails.getServiceRMName());
			if (userlogin.getCompanyIDForAlisId() != null) {
				resData.put("mappedCustomerID", userlogin.getCompanyIDForAlisId().trim());
			} else {
				resData.put("mappedCustomerID", userlogin.getRetailBankUserID().trim());
			}

			connection.commit();

		} catch (Exception e) {
			log.info("Exception ===", e);
		} finally {

			try {
				if (rset != null) {
					rset.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null ) {
					closeConnection(connection,new Object(){}.getClass().getEnclosingMethod().getName().toString());
				}
			} catch (SQLException e) {
				log.info("SQLException ===", e);
				try {
					connection.rollback();
				} catch (SQLException e1) {
					log.info("########################:::Exception::::::", e1);
				}
			}
		}

		return resData;
	}

	public JSONObject getCustomerDetail(UserLogin userlogin) {
		log.info("Inside getCustomerDetail" + userlogin.getRetailBankUserID());
		UserLogin fetchDetails = new UserLogin();
		JSONObject resData = new JSONObject();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {

			connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
			pstmt = connection.prepareStatement(getQuery("SELECT_USER_DAT_FROM_VERTUAL"));
			pstmt.setString(1, userlogin.getRetailBankUserID().trim());
			rset = pstmt.executeQuery();
			if (rset.next()) {
				fetchDetails.setRetailBankUserID(rset.getString("COD_CUST_ID"));
				fetchDetails.setUserName(rset.getString("NAM_CUST_FULL"));
				fetchDetails.setUserContactNo(rset.getString("REF_CUST_TELEX"));
				fetchDetails.setUserEmail(rset.getString("REF_CUST_EMAIL"));
				fetchDetails.setServiceRM(rset.getString("SERVICE_RM"));
				fetchDetails.setSSC(rset.getString("SSC"));
				fetchDetails.setAssetRM(rset.getString("ASSET_RM"));
				fetchDetails.setAssetRMemail(rset.getString("ASSET_RM_EMAILID"));
				fetchDetails.setAssetRMmob(rset.getString("ASSET_RM_MOB"));
				fetchDetails.setServRMemail(rset.getString("SERVICE_RM_EMAILID"));
				fetchDetails.setServRMmob(rset.getString("SERVICE_RM_MOB"));
				fetchDetails.setAssetRMName(rset.getString("ASSET_RM_NAME"));
				fetchDetails.setServiceRMName(rset.getString("SERVICE_RM_NAME"));
			}

			rset.close();
			pstmt.close();

			// CUSTOMER_COMPANY_ID=select distinct(COD_CUST) from
			// VW_CH_ACCT_CUST_XREF where COD_ACCT_CUST_REL = 'SOW'
			// and COD_ACCT_NO in (select COD_ACCT_NO from
			// VW_CH_ACCT_CUST_XREF where COD_CUST = ?)
			pstmt = connection.prepareStatement(getQuery("CUSTOMER_COMPANY_ID"));
			pstmt.setString(1, userlogin.getRetailBankUserID().trim());
			rset = pstmt.executeQuery();
			if (rset.next()) {
				fetchDetails.setCompanyID(rset.getString(1));
			}
			rset.close();
			pstmt.close();

			resData.put("code", "E108");
			resData.put("CustName", fetchDetails.getUserName());
			resData.put("CustContact", fetchDetails.getUserContactNo());
			resData.put("CustEmail", fetchDetails.getUserEmail());
			resData.put("ServRMID", fetchDetails.getServiceRM());
			resData.put("ServMobRM", fetchDetails.getServRMmob());
			resData.put("ServEmailRM", fetchDetails.getServRMemail());
			resData.put("AssetRMID", fetchDetails.getAssetRM());
			resData.put("AssetMobRM", fetchDetails.getAssetRMmob());
			resData.put("AssetEmailRM", fetchDetails.getAssetRMemail());
			resData.put("CompanyID", fetchDetails.getCompanyID());
			resData.put("AssetRmName", fetchDetails.getAssetRMName());
			resData.put("ServiceRmName", fetchDetails.getServiceRMName());
			if (userlogin.getCompanyIDForAlisId() != null) {
				resData.put("mappedCustomerID", userlogin.getCompanyIDForAlisId().trim());
			} else {
				resData.put("mappedCustomerID", userlogin.getRetailBankUserID().trim());
			}

		} catch (Exception e) {
			log.info("Exception ===", e);
			resData.put("code", "E102");
		} finally {

			try {
				if (rset != null) {
					rset.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null ) {
					closeConnection(connection,new Object(){}.getClass().getEnclosingMethod().getName().toString());
				}
			} catch (SQLException e) {
				log.info("SQLException ===", e);
			}
		}
		return resData;
	}

	public JSONObject insertRegistrationDetail(UserLogin userlogin) {
		log.info("Inside insertRegistrationDetail");
		UserLogin fetchDetails = new UserLogin();

		int status = 0;
		String regID = "";
		JSONObject resData = new JSONObject();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		if (!checkRegExist(userlogin.getRetailBankUserID()) || "true".equals(userlogin.getDeregFlag())) {
			log.info("For New Registration >>>");
			
			try {
				connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());

				connection.setAutoCommit(false);
				pstmt = connection.prepareStatement(getQuery("SELECT_REGI_ID"));
				rset = pstmt.executeQuery();
				if (rset.next()) {
					regID = rset.getString(1);
					log.info("Registration ID ===" + regID);
				}
				rset.close();
				pstmt.close();

				pstmt = connection.prepareStatement(getQuery("DELETE_USER_TMP_IF_EXIST"));
				pstmt.setString(1, userlogin.getRetailBankUserID().trim());
				pstmt.executeUpdate();
				pstmt.close();
				
				pstmt = connection.prepareStatement(getQuery("DELETE_DROP_OFF_CUSTOMER_DTL"));
				pstmt.setString(1, userlogin.getRetailBankUserID().trim());
				pstmt.executeUpdate();
				pstmt.close();

				// Insert into SME_REG_USER_INFO
				// (CUST_ID,CUST_NAME,COMPANY_ID,COMPANY_NAME,RM_ID,HAS_MAKER,REGISTERED_DT,REG_ID,REG_MODE)
				// values (?,?,?,?,?,?,sysdate,?,"MOBILE")

				// fetchDetails =
				// getUserDataFromVertual(userlogin.getRetailBankUserID());

				pstmt = connection.prepareStatement(getQuery("SELECT_USER_DAT_FROM_VERTUAL"));
				pstmt.setString(1, userlogin.getRetailBankUserID().trim());
				rset = pstmt.executeQuery();
				if (rset.next()) {
					fetchDetails.setRetailBankUserID(rset.getString("COD_CUST_ID"));
					fetchDetails.setUserName(rset.getString("NAM_CUST_FULL"));
					fetchDetails.setUserContactNo(rset.getString("REF_CUST_TELEX"));
					fetchDetails.setUserEmail(rset.getString("REF_CUST_EMAIL"));
					fetchDetails.setServiceRM(rset.getString("SERVICE_RM"));
					fetchDetails.setSSC(rset.getString("SSC"));
					fetchDetails.setAssetRM(rset.getString("ASSET_RM"));
					fetchDetails.setAssetRMemail(rset.getString("ASSET_RM_EMAILID"));
					fetchDetails.setAssetRMmob(rset.getString("ASSET_RM_MOB"));
					fetchDetails.setServRMemail(rset.getString("SERVICE_RM_EMAILID"));
					fetchDetails.setServRMmob(rset.getString("SERVICE_RM_MOB"));
					fetchDetails.setAssetRMName(rset.getString("ASSET_RM_NAME"));
					fetchDetails.setServiceRMName(rset.getString("SERVICE_RM_NAME"));

				}

				rset.close();
				pstmt.close();

				// CUSTOMER_COMPANY_ID=select distinct(COD_CUST) from
				// VW_CH_ACCT_CUST_XREF where COD_ACCT_CUST_REL = 'SOW'
				// and COD_ACCT_NO in (select COD_ACCT_NO from
				// VW_CH_ACCT_CUST_XREF where COD_CUST = ?)
				pstmt = connection.prepareStatement(getQuery("CUSTOMER_COMPANY_ID"));
				pstmt.setString(1, userlogin.getRetailBankUserID().trim());
				rset = pstmt.executeQuery();
				if (rset.next()) {
					fetchDetails.setCompanyID(rset.getString(1));
				}
				rset.close();
				pstmt.close();
				boolean forgotPasswordFlag = false;
				try{
					pstmt = connection.prepareStatement(getQuery("SELECT_APP_LOGIN_INFO_COUNT"));
					pstmt.setString(1, userlogin.getLoginID());
					rset =pstmt.executeQuery();
					while(rset.next()){
						if(rset.getInt(1) > 0)
							forgotPasswordFlag = true;
						
					}
				}
				catch(Exception e){
					log.info("Exception occuredwhile checking customer is registered or not "+e.getLocalizedMessage());
				}
				log.info("forgotpassword scenario "+forgotPasswordFlag);
				if(forgotPasswordFlag== false){
						try{

							log.info("2330 "+userlogin.getLoginID()+" "+userlogin.getCompanyID());
						pstmt = connection.prepareStatement(getQuery("INSERT_INTO_DROP_OFF_CUSTOMER_DTL"));
						pstmt.setString(1, userlogin.getLoginID());
						pstmt.setString(2, fetchDetails.getCompanyID());
						pstmt.setString(3, "NA");
						pstmt.setString(4, userlogin.getUserType());
						pstmt.setString(5, userlogin.getUserRegisterType());
						pstmt.executeUpdate();
						pstmt.close();
					}
					catch(Exception e){
						log.info("Exception occured while creating entry in drop off table "+e);
					}
				}
				pstmt = connection.prepareStatement(getQuery("RESISTER_USER_TMP"));
				// CUST_ID,CUST_NAME,COMPANY_ID,COMPANY_NAME,RM_ID,HAS_MAKER,REGISTERED_DT,REG_ID,REG_MODE
				pstmt.setString(1, userlogin.getRetailBankUserID().trim());
				pstmt.setString(2, fetchDetails.getUserName());
				pstmt.setString(3, fetchDetails.getCompanyID());
				pstmt.setString(4, "NA");
				pstmt.setString(5, fetchDetails.getServiceRM());
				pstmt.setString(6, "N");
				pstmt.setString(7, regID);
				status = pstmt.executeUpdate();
				pstmt.close();
				if (status > 0) {
					log.info("Registration Successful in tmp table");
					// String objRefernce = this.toString();
					// StringBuffer token = new
					// StringBuffer(objRefernce.substring(objRefernce.indexOf("@")
					// + 1,objRefernce.length()));
					// token.append(new Date().getTime());
					// log.info("Generated Token ==="+token.toString());
					// userlogin.setToken(token.toString());
					// pstmt.close();
					// Insert into SME_APP_LOGIN_INFO
					// (LOGIN_ID,USERNAME,PASSWORD,DEVICE_NAME,DEVICE_PLATFORM,
					// DEVICE_UUID,DEVICE_VERSION,DEVICE_DATE,DEVICE_IMEI,TOKEN,LOGIN_NAME,REG_ID)
					// values (?,?,?,?,?,?,?,?,?,?,?,?)
					pstmt = connection.prepareStatement(getQuery("DELETE_APP_LOGIN_TMP_IF_EXIST"));
					pstmt.setString(1, userlogin.getRetailBankUserID().trim());
					pstmt.executeUpdate();
					pstmt.close();

					pstmt = connection.prepareStatement(getQuery("INSERT_APP_LOGIN_DETAILS_REG_TIME_TMP"));
					pstmt.setString(1, userlogin.getRetailBankUserID().trim());
					pstmt.setString(2, fetchDetails.getUserName());
					pstmt.setString(3, "");
					pstmt.setString(4, userlogin.getDeviceName());
					pstmt.setString(5, userlogin.getDevicePlat());
					pstmt.setString(6, userlogin.getDeviceID());
					pstmt.setString(7, userlogin.getDeviceVer());
					pstmt.setString(8, "");
					pstmt.setString(9, userlogin.getIMEI_NO());
					pstmt.setString(10, userlogin.getToken());
					pstmt.setString(11, fetchDetails.getUserName());
					pstmt.setString(12, regID);
					status = pstmt.executeUpdate();
					pstmt.close();
					if (status > 0) {
						log.info("App Detail Inserted In APP TMP Login Table Successful");
						// Add Share Key in DB
						// pstmt =
						// connection.prepareStatement(getQuery("INSERT_KEY_FOR_IMEI"));
						// pstmt.setString(1, userlogin.getIMEI_NO());
						// pstmt.setString(2, userlogin.getShareKey());
						// pstmt.executeUpdate();
						// pstmt.close();
						resData.put("code", "E108");
						resData.put("CustName", fetchDetails.getUserName());
						resData.put("CustContact", fetchDetails.getUserContactNo());
						resData.put("CustEmail", fetchDetails.getUserEmail());
						resData.put("ServRMID", fetchDetails.getServiceRM());
						resData.put("ServMobRM", fetchDetails.getServRMmob());
						resData.put("ServEmailRM", fetchDetails.getServRMemail());
						resData.put("AssetRMID", fetchDetails.getAssetRM());
						resData.put("AssetMobRM", fetchDetails.getAssetRMmob());
						resData.put("AssetEmailRM", fetchDetails.getAssetRMemail());
						resData.put("CompanyID", fetchDetails.getCompanyID());
						resData.put("AssetRmName", fetchDetails.getAssetRMName());
						resData.put("ServiceRmName", fetchDetails.getServiceRMName());
						connection.commit();
						return resData;
					} else {
						log.info("App Detail Inserted In APP Login Table Failed");
						resData.put("code", "E104");
						connection.commit();
						return resData;
					}

				} else {
					log.info("Resigtration Failed");
					resData.put("code", "E103");
					return resData;
				}

			} catch (Exception e) {
				log.info("Exception ===", e);
				resData.put("code", "E102");
				return resData;
			} finally {

				try {
					if (rset != null) {
						rset.close();
					}
					if (pstmt != null) {
						pstmt.close();
					}
					if (connection != null ) {
						closeConnection(connection,new Object(){}.getClass().getEnclosingMethod().getName().toString());
					}
				} catch (SQLException e) {
					log.info("SQLException ===", e);
					try {
						connection.rollback();
					} catch (SQLException e1) {
						log.info("SQLException ===", e1);
					}
				}
			}

		} else {
			try {
				connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
				pstmt = connection.prepareStatement(getQuery("SELECT_USER_DAT_FROM_VERTUAL"));
				pstmt.setString(1, userlogin.getRetailBankUserID().trim());
				rset = pstmt.executeQuery();
				if (rset.next()) {
					fetchDetails.setRetailBankUserID(rset.getString("COD_CUST_ID"));
					fetchDetails.setUserName(rset.getString("NAM_CUST_FULL"));
					fetchDetails.setUserContactNo(rset.getString("REF_CUST_TELEX"));
					fetchDetails.setUserEmail(rset.getString("REF_CUST_EMAIL"));
					fetchDetails.setServiceRM(rset.getString("SERVICE_RM"));
					fetchDetails.setSSC(rset.getString("SSC"));
					fetchDetails.setAssetRM(rset.getString("ASSET_RM"));
					fetchDetails.setAssetRMemail(rset.getString("ASSET_RM_EMAILID"));
					fetchDetails.setAssetRMmob(rset.getString("ASSET_RM_MOB"));
					fetchDetails.setServRMemail(rset.getString("SERVICE_RM_EMAILID"));
					fetchDetails.setServRMmob(rset.getString("SERVICE_RM_MOB"));
					fetchDetails.setAssetRMName(rset.getString("ASSET_RM_NAME"));
					fetchDetails.setServiceRMName(rset.getString("SERVICE_RM_NAME"));
				}

				rset.close();
				pstmt.close();

				// CUSTOMER_COMPANY_ID=select distinct(COD_CUST) from
				// VW_CH_ACCT_CUST_XREF where COD_ACCT_CUST_REL = 'SOW'
				// and COD_ACCT_NO in (select COD_ACCT_NO from
				// VW_CH_ACCT_CUST_XREF where COD_CUST = ?)
				pstmt = connection.prepareStatement(getQuery("CUSTOMER_COMPANY_ID"));
				pstmt.setString(1, userlogin.getRetailBankUserID().trim());
				rset = pstmt.executeQuery();
				if (rset.next()) {
					fetchDetails.setCompanyID(rset.getString(1));
				}
				rset.close();
				pstmt.close();
				
				resData.put("code", "E108");
				resData.put("CustName", fetchDetails.getUserName());
				resData.put("CustContact", fetchDetails.getUserContactNo());
				resData.put("CustEmail", fetchDetails.getUserEmail());
				resData.put("ServRMID", fetchDetails.getServiceRM());
				resData.put("ServMobRM", fetchDetails.getServRMmob());
				resData.put("ServEmailRM", fetchDetails.getServRMemail());
				resData.put("AssetRMID", fetchDetails.getAssetRM());
				resData.put("AssetMobRM", fetchDetails.getAssetRMmob());
				resData.put("AssetEmailRM", fetchDetails.getAssetRMemail());
				resData.put("CompanyID", fetchDetails.getCompanyID());
				resData.put("AssetRmName", fetchDetails.getAssetRMName());
				resData.put("ServiceRmName", fetchDetails.getServiceRMName());

			} catch (Exception e) {
				log.info("Exception ===", e);
				resData.put("code", "E102");
				return resData;
			} finally {

				try {
					if (rset != null) {
						rset.close();
					}
					if (pstmt != null) {
						pstmt.close();
					}
					if (connection != null ) {
						closeConnection(connection,new Object(){}.getClass().getEnclosingMethod().getName().toString());
					}
				} catch (SQLException e) {
					log.info("SQLException ===", e);
				}
			}

			log.info("No need To Registration");
			return resData;
		}

	}

	public boolean checkRegExist(String custId) {
		log.info("Inside checkRegExist");
		int count = 0;
		boolean flag = false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
			pstmt = connection.prepareStatement(getQuery("CHECK_REG_EXIST"));
			pstmt.setString(1, custId);
			rset = pstmt.executeQuery();
			if (rset.next()) {
				count = rset.getInt(1);
			}
			rset.close();
			pstmt.close();
			if (count > 0) {
				log.info("Registration Already Exist");
				flag = true;
			} else {
				log.info("New Registration");
				flag = false;
			}
		} catch (Exception e) {
			log.info("Exception ===", e);
		} finally {

			try {
				if (rset != null) {
					rset.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null ) {
					closeConnection(connection,new Object(){}.getClass().getEnclosingMethod().getName().toString());
				}
			} catch (SQLException e) {
				log.info("SQLException ===", e);
			}
		}

		return flag;

	}

	public boolean validateLoginRequest(String requestToken) {

		log.info("validateLoginRequest =" + requestToken);
		int logOutCnt = 0;
		boolean logRequest = false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
			pstmt = connection.prepareStatement(getQuery("GET_LOGIN_REQUEST"));
			pstmt.setString(1, requestToken);
			rset = pstmt.executeQuery();
			if (rset.next()) {
				logOutCnt = rset.getInt(1);
			}
			rset.close();
			pstmt.close();
			if (logOutCnt > 0) {
				return logRequest;
			} else {
				pstmt = connection.prepareStatement(getQuery("INSERT_LOGIN_REQUEST"));
				pstmt.setString(1, requestToken);
				int insertCnt = pstmt.executeUpdate();
				if (insertCnt > 0) {
					logRequest = true;
				}
			}

		} catch (Exception e) {
			log.info("Exception-", e);
			return logRequest;
		} finally {
			try {
				if (rset != null) {
					rset.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null ) {
					closeConnection(connection,new Object(){}.getClass().getEnclosingMethod().getName().toString());
				}
			} catch (SQLException e) {
				log.info("SQLException ===", e);
			}
		}
		return logRequest;
	}

	

	/*************************************************************
	 * This method returns the path name for the particular method
	 * 
	 * @author rasmiranjan
	 * @param methodName
	 * 
	 *************************************************************/

	public void updateFailureCount(String loginID, int failCnt) {

		log.info("In getLoginFailureCount" + loginID + "," + failCnt);
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
			pstmt = connection.prepareStatement(getQuery("UPDATE_MPIN_FAILURE_UPDATE"));
			pstmt.setInt(1, failCnt + 1);
			pstmt.setString(2, loginID);

			pstmt.executeUpdate();

			pstmt.close();
			
		} catch (Exception e) {
			log.info("Exception ===", e);
		} finally {

			try {

				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null ) {
					closeConnection(connection,new Object(){}.getClass().getEnclosingMethod().getName().toString());
				}
			} catch (SQLException e) {
				log.info("SQLException ===", e);
			}
		}

	}

	/*************************************************************
	 * This method returns the path name for the particular method
	 * 
	 * @author rasmiranjan
	 * @param methodName
	 * 
	 *************************************************************/

	public void updateFailureCount(String loginID) {

		log.info("In getLoginFailureCount");
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
			pstmt = connection.prepareStatement(getQuery("UPDATE_MPIN_SUCCESS_UPDATE"));
			// pstmt.setInt(1, failCnt+1);
			pstmt.setString(1, loginID);

			pstmt.executeUpdate();

			pstmt.close();
			
		} catch (Exception e) {
			log.info("Exception ===", e);
		} finally {

			try {

				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null ) {
					closeConnection(connection,new Object(){}.getClass().getEnclosingMethod().getName().toString());
				}
			} catch (SQLException e) {
				log.info("SQLException ===", e);
			}
		}

	}

	

	public void lockedUser(String loginID) {

		log.info("In lockedUser");
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
			pstmt = connection.prepareStatement(getQuery("USER_LOCKED"));
			pstmt.setString(1, loginID);
			pstmt.executeUpdate();
			pstmt.close();

		} catch (Exception e) {
			log.info("Exception ===", e);
		} finally {

			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null ) {
					closeConnection(connection,new Object(){}.getClass().getEnclosingMethod().getName().toString());
				}
			} catch (SQLException e) {
				log.info("SQLException ===", e);
			}
		}

	}

	public void lockedUserWithConn(Connection connection, String loginID) {

		log.info("In lockedUser");
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(getQuery("USER_LOCKED"));
			pstmt.setString(1, loginID);

			pstmt.executeUpdate();

			pstmt.close();

		} catch (Exception e) {
			log.info("Exception ===", e);
		} finally {

			try {
				
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				log.info("SQLException ===", e);
			}
		}

	}

	public void getLoginFailureCount(String loginID, int logOutCnt) {

		log.info("In getLoginFailureCount");
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
			// UPDATE_LOGIN_FAILURE_UPDATE=UPDATE SME_APP_LOGIN_INFO SET
			// LAST_FAILURE_ATTEMPT_TIME = sysdate,LOGIN_COUNT=? WHERE LOGIN_ID
			// = ?
			pstmt = connection.prepareStatement(getQuery("UPDATE_LOGIN_FAILURE_UPDATE"));
			pstmt.setInt(1, logOutCnt + 1);
			pstmt.setString(2, loginID.trim());
			pstmt.executeUpdate();
			log.info("hiii i am here");
			pstmt.close();
			log.info("hiii i am there");
		} catch (Exception e) {
			log.info("Exception ===", e);
		} finally {

			try {

				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null ) {
					closeConnection(connection,new Object(){}.getClass().getEnclosingMethod().getName().toString());
				}
			} catch (SQLException e) {
				log.info("SQLException ===", e);
			}
		}

	}

	public void getLoginFailureCountWithConn(Connection connection, String loginID, int logOutCnt) {

		log.info("In getLoginFailureCount");
		PreparedStatement pstmt = null;
		try {
			// UPDATE_LOGIN_FAILURE_UPDATE=UPDATE SME_APP_LOGIN_INFO SET
			// LAST_FAILURE_ATTEMPT_TIME = sysdate,LOGIN_COUNT=? WHERE LOGIN_ID
			// = ?
			pstmt = connection.prepareStatement(getQuery("UPDATE_LOGIN_FAILURE_UPDATE"));
			pstmt.setInt(1, logOutCnt + 1);
			pstmt.setString(2, loginID.trim());
			pstmt.executeUpdate();
			log.info("hiii i am here");
			pstmt.close();
			log.info("hiii i am there");
		} catch (Exception e) {
			log.info("Exception ===", e);
		} finally {

			try {

				if (pstmt != null) {
					pstmt.close();
				}

			} catch (SQLException e) {
				log.info("SQLException ===", e);
			}
		}

	}

	// pstmt.setInt(1, logcount+1);
	public JSONObject checkUserLoggedIn(String userID, String regID) {
		log.info("In checkUserLoggedIn");
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		JSONObject resmsg = new JSONObject();
		int mpinCnt = 0;
		try {
			int count = 0;
			connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
			pstmt = connection.prepareStatement(getQuery("CHECK_LOGOUT_MODE"));

			pstmt.setString(1, userID);

			rset = pstmt.executeQuery();
			if (rset.next()) {
				count = rset.getInt(1);
				mpinCnt = rset.getInt(2);
				log.info("count==" + count);

			} else {
				log.info("else User Already Logged In");
				resmsg.put("STATUS", "Error");
				resmsg.put("Message", "User Already Logged In");
			}

			rset.close();
			pstmt.close();

			if (count > 0) {
				log.info("If UPDATE_LOGIN_TIME");

				pstmt = connection.prepareStatement(getQuery("INSERT_SME_APP_LOGIN_INFO_LOG_INFO"));
				pstmt.setString(1, userID);
				pstmt.executeUpdate();
				pstmt.close();

				// Update LogIn date and Time
				pstmt = connection.prepareStatement(getQuery("INSERT_LOGIN_DATE"));
				pstmt.setInt(1, 0);
				pstmt.setString(2, "T");
				pstmt.setString(3, regID);
				pstmt.executeUpdate();
				pstmt.close();

				if (mpinCnt < 5) {
					pstmt = connection.prepareStatement(getQuery("UPDATE_MPIN_COUNT"));
					pstmt.setString(1, regID);
					pstmt.executeUpdate();
					pstmt.close();
				}

				// Update Logout Mode 'F' for status user now login
				/*
				 * pstmt1 =
				 * connection.prepareStatement(getQuery("UPDATE_LOGOUT_MODE"));
				 * pstmt1.setString(1, regID); pstmt1.executeUpdate();
				 * pstmt1.close();
				 */
				resmsg.put("STATUS", "SUCCESS");
				resmsg.put("Message", "User Log In Successfully");

			} else {
				log.info("else User Already Logged In");
				resmsg.put("STATUS", "Error");
				resmsg.put("Message", "User Already Logged In");
			}


			return resmsg;
		} catch (Exception e) {
			log.info("Exception ===", e);
			resmsg.put("STATUS", "Error");
			resmsg.put("Message", "Exception Occured");
			return resmsg;
		} finally {

			try {
				if (rset != null) {
					rset.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null ) {
					closeConnection(connection,new Object(){}.getClass().getEnclosingMethod().getName().toString());
				}
			} catch (SQLException e) {
				log.info("SQLException ===", e);
			}
		}
	}

	// Added By Prabhat on 30-03-2018

	public JSONObject getServiceCodeForCustomer(String userID) {
		log.info("In getServiceCodeForCustomer =" + userID);
		JSONObject resmsg = new JSONObject();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			StringBuilder serviceCodeBuild = new StringBuilder();
			connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
			// SELECT_SERVICE_CODE_FOR_ACCESS_CUSTOMER=SELECT SERVICE_CD FROM
			// COM_SERVICE_MST WHERE SERVICE_TYPE IN (WITH T AS (SELECT
			// GROUP_SERVICE_CODE STR FROM SME_SERVICE_ACCESS_MST WHERE
			// CUSTOMER_ID = '98643' AND ACTIVE_FLAG = 'Y') SELECT REGEXP_SUBSTR
			// (STR, '[^|]+', 1, LEVEL) SPLIT_VALUES FROM T CONNECT BY LEVEL <=
			// (SELECT LENGTH (REPLACE (STR, '|', NULL)) FROM T)) AND
			// ACTIVE_FLAG = 'Y'
			pstmt = connection.prepareStatement(getQuery("SELECT_SERVICE_CODE_FOR_ACCESS_CUSTOMER"));

			pstmt.setString(1, userID);

			rset = pstmt.executeQuery();
			while (rset.next()) {
				serviceCodeBuild.append(rset.getString(1) + "|");

			}

			rset.close();
			pstmt.close();

			if (serviceCodeBuild.length() == 0) {
				log.info("If serviceCodeBuild length is zer0");
				/*
				 * resmsg.put("status", "0"); resmsg.put("message",
				 * path.getMsg("E127"));
				 */
				resmsg.put("status", "1");
				resmsg.put("message",
						"BT02|BT00|BT01|DU01|SL00|SL01|SL03|FT00|FT01|FT02|FT03|SI00|FD00|SB03|GB00|SB00|SB01|SB02|SL02|SLE01|SLE02|GB01|GB02|GT01|SB03|");

			} else {
				log.info("else serviceCodeBuild length exist ");
				/*
				 * resmsg.put("status", "1"); resmsg.put("message",
				 * serviceCodeBuild.toString());
				 */
				resmsg.put("status", "1");
				resmsg.put("message",
						"BT02|BT00|BT01|DU01|SL00|SL01|SL03|FT00|FT01|FT02|FT03|SI00|FD00|SB03|GB00|SB00|SB01|SB02|SL02|SLE01|SLE02|GB01|GB02|GT01|SB03|");

			}

		} catch (Exception e) {
			log.info("Exception ===", e);
			resmsg.put("status", "0");
			resmsg.put("message", "Exception occured.");

		} finally {

			try {
				if (rset != null) {
					rset.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null ) {
					closeConnection(connection,new Object(){}.getClass().getEnclosingMethod().getName().toString());
				}
			} catch (SQLException e) {
				log.info("SQLException ===", e);
			}
		}

		log.info("resmsg = ");
		return resmsg;
	}

	public void updateToken(String logInID, String token) {
        log.info("In updateToken logInID..........." + logInID);
        EntityManager localEntityManager = entityManagerFactory.createEntityManager().unwrap(Session.class);
        
        try {
            // Create a native query
            Query query = localEntityManager.createNativeQuery( "UPDATE SME_APP_LOGIN_INFO SET TOKEN = :token WHERE LOGIN_ID = :loginId");
            
            // Set parameters
            query.setParameter("token", token);
            query.setParameter("loginId", logInID);
            
            // Execute the update
            localEntityManager.getTransaction().begin();
            int rowsUpdated = query.executeUpdate();
            localEntityManager.getTransaction().commit();
            log.info("Rows updated: " + rowsUpdated);
            
        } catch (Exception e) {
            log.info("Exception ===", e);
        }
    }
	public JSONObject logoutUser(String logInID) {
		log.info("In logoutUser " + logInID + ApplicationGenParameter.companyCustomerMapList.get(logInID));
		JSONObject resmsg = new JSONObject();
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());

			// LOGOUT_USER=UPDATE SME_APP_LOGIN_INFO SET LOGOUT_DATE = sysdate
			// WHERE LOGIN_ID = ?
			pstmt = connection.prepareStatement(getQuery("LOGOUT_USER"));
			pstmt.setString(1, "T");
			pstmt.setString(2, "");
			pstmt.setString(3, logInID.trim());

			pstmt.executeUpdate();
			resmsg.put("ResCode", "0");
			resmsg.put("ResBody", "SUCCESS");
			pstmt.close();

			// DELETE_ENTRY_OF_CUSTOMER=DELETE SME_ONLINE_SESSION_DTL WHERE
			// COMPANY_ID = ? AND CUSTOMER_ID = ?
			pstmt = connection.prepareStatement(getQuery("DELETE_ENTRY_OF_CUSTOMER"));
			pstmt.setString(1, ApplicationGenParameter.companyCustomerMapList.get(logInID.trim()));
			pstmt.setString(2, logInID.trim());
			int sessCntD = pstmt.executeUpdate();
			pstmt.close();

			if (sessCntD > 0) {
				log.info("delete entry for sessionCnt logoutUser" + logInID);
			}
			
			
			try {
				pstmt = connection.prepareStatement(getQuery("UPDATE_OLD_TOKEN_FOR_SESSION_OUT"));
				pstmt.setString(1, logInID.trim());
				int tokenUpdated = pstmt.executeUpdate();
				pstmt.close();
				
				log.info(" UPDATE_OLD_TOKEN_FOR_SESSION_OUT===", tokenUpdated);
				
			} catch (Exception e) {
				log.info("Exception UPDATE_OLD_TOKEN_FOR_SESSION_OUT===", e);
			}
			

		} catch (Exception e) {
			log.info("Exception ===", e);
			resmsg.put("STATUS", "Error");
			resmsg.put("Message", "Exception Occured");
		} finally {

			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null ) {
					closeConnection(connection,new Object(){}.getClass().getEnclosingMethod().getName().toString());
				}
			} catch (SQLException e) {
				log.info("SQLException ===", e);
			}
		}
		return resmsg;
	}

	

	public boolean imeiExitCheck(String indata) {
		boolean flag = false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
			// CHECK_IMEI_EXIT=select count(*) from SME_APP_LOGIN_INFO where
			// DEVICE_IMEI = ?
			pstmt = connection.prepareStatement(getQuery("CHECK_IMEI_EXIT"));
			pstmt.setString(1, indata.trim());
			rset = pstmt.executeQuery();
			if (rset.next()) {
				if (rset.getInt(1) > 0) {
					log.info("User " + indata + " Exist");
					flag = true;
				}
			}

			rset.close();
			pstmt.close();
		} catch (Exception e) {
			log.info("Exception ===", e);

		} finally {

			try {
				if (rset != null) {
					rset.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null ) {
					closeConnection(connection,new Object(){}.getClass().getEnclosingMethod().getName().toString());
				}

			} catch (SQLException e) {
				log.info("SQLException ===", e);

			}

		}
		return flag;
	}

	 @Transactional
	    public boolean imeiExitCheckInPubkeyTable(String indata) {
	        boolean flag = false;

	        try {
	            // Define the native SQL query
	            String sql = "SELECT COUNT(*) FROM SmeImeiPubkeyEntry WHERE imeiNo = :imeiNo";
	            
	            // Create the query and set the parameters
	            Query query = entityManager.createQuery(sql);
	            query.setParameter("imeiNo", indata.trim());
	            
	            // Execute the query and get the result
	            Number count = (Number) query.getSingleResult();

	            if (count != null && count.intValue() > 0) {
	                log.info("User Exit in PUBKEY TABLE ===" + indata);
	                flag = true;
	            }
	        } catch (Exception e) {
	            log.error("Exception ===", e);
	        }

	        return flag;
	    }
	 
	 public boolean userExitCheck(String indata) {
	        boolean flag = false;
	        try {
	            // Query to check if user exists
	            String queryStr = "SELECT COUNT(*) FROM SmeAppLoginInfo WHERE loginId = :loginId AND loginTypeMobile = 'Y'";
	            Query query = entityManager.createQuery(queryStr);
	            query.setParameter("loginId", indata);

	            // Execute the query
	            Number count = (Number) query.getSingleResult();
	            if (count.intValue() > 0) {
	                log.info("User " + indata + " exists");
	                flag = true;
	            }

	        } catch (Exception e) {
	            log.error("Exception occurred:", e);
	        }
	        return flag;
	    }
	 
	 @Transactional
	    public boolean userBindWithMobCheck(String userID, String imei) {
	        boolean flag = false;

	        try {
	            // Define the native SQL query
	            String sql = "SELECT COUNT(*) FROM SmeAppLoginInfo WHERE loginId = :loginId AND deviceImei = :deviceImei";
	            
	            // Create the query and set the parameters
	            Query query = entityManager.createQuery(sql);
	            query.setParameter("loginId", userID);
	            query.setParameter("deviceImei", imei);
	            
	            // Execute the query and get the result
	            Number count = (Number) query.getSingleResult();

	            if (count != null && count.intValue() > 0) {
	                log.info("User " + userID + " Exist With IMEI");
	                flag = true;
	            }
	        } catch (Exception e) {
	            log.error("Exception ===", e);
	        }

	        return flag;
	    }

	
	

	/******************************************
	 * This method is used to delete all the user data for the given parameters
	 * 
	 * @param imei
	 * @param newUser
	 ******************************************/
	public boolean deleteUserData(String imei, String newUser) {

		log.info("In deleteUserData function imei===" + "---------" + newUser);
		String userID = "", imeiNo = "";
		boolean flag = false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
			connection.setAutoCommit(false);

			// Delete Login Data From login table using IMEI and fetch UsedID
			pstmt = connection.prepareStatement(getQuery("SELECT_LOGINID"));
			pstmt.setString(1, imei.trim());
			rset = pstmt.executeQuery();
			if (rset.next()) {
				userID = rset.getString(1);
			}
			rset.close();
			pstmt.close();
			pstmt = connection.prepareStatement(getQuery("DELETE_LOGIN_TABLE"));
			pstmt.setString(1, imei.trim());
			pstmt.executeUpdate();
			pstmt.close();

			// Delete IMEI-PUBKEY data In main Table using IMEI
			pstmt = connection.prepareStatement(getQuery("DELETE_PUBKEY_TABLE"));
			pstmt.setString(1, imei.trim());
			pstmt.executeUpdate();
			pstmt.close();

			// Delete Registration data from Registration table using UserID and
			// put in BackUp Table
			// iNSERT rEGISTRATION dETAIL iNTO bACKUP
			pstmt = connection.prepareStatement(getQuery("INSERT_REG_DATA_BACKUP"));
			pstmt.setString(1, userID);
			pstmt.executeUpdate();
			pstmt.close();

			pstmt = connection.prepareStatement(getQuery("DELETE_REG_DATA_MAIN"));
			pstmt.setString(1, userID);
			pstmt.executeUpdate();
			pstmt.close();

			// Fetch IMEI using userID and call method of Backup
			pstmt = connection.prepareStatement(getQuery("SELECT_LOGIN_IMEI"));
			pstmt.setString(1, newUser);
			rset = pstmt.executeQuery();
			if (rset.next()) {
				imeiNo = rset.getString(1);
			}
			rset.close();
			pstmt.close();

			// iNSERT rEGISTRATION dETAIL iNTO bACKUP
			pstmt = connection.prepareStatement(getQuery("INSERT_REG_DATA_BACKUP"));
			pstmt.setString(1, newUser);
			pstmt.executeUpdate();

			pstmt.close();

			// dELETE MAIN REGISTRATION BACKUP
			pstmt = connection.prepareStatement(getQuery("DELETE_REG_DATA_MAIN"));
			pstmt.setString(1, newUser);
			pstmt.executeUpdate();

			pstmt.close();

			// dELETE MAIN lOGIN BACKUP
			pstmt = connection.prepareStatement(getQuery("DELETE_LOGIN_DATA_MAIN"));
			pstmt.setString(1, newUser);
			pstmt.executeUpdate();

			pstmt.close();

			// dELETE MAIN iMEI BACKUP
			pstmt = connection.prepareStatement(getQuery("DELETE_IMEI_MAIN"));
			pstmt.setString(1, imeiNo);
			pstmt.executeUpdate();

			pstmt.close();

			connection.commit();

			flag = true;
		} catch (Exception e) {
			log.info("Exception ===", e);
		} finally {

			try {
				if (rset != null) {
					rset.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null ) {
					closeConnection(connection,new Object(){}.getClass().getEnclosingMethod().getName().toString());
				}
			} catch (SQLException e) {
				log.info("SQLException ===", e);
				try {
					connection.rollback();
				} catch (SQLException e1) {
					log.info("SQLException ===", e1);
				}
			}
		}

		return flag;
	}

	
	 @Transactional
	 public void deleteTempTableOfUser(String userId, String imei) {
	        log.info("In deleteTempTableOfUser Function");
	        EntityManager localEntityManager = entityManagerFactory.createEntityManager().unwrap(Session.class);
	        
	        try {
	            // Begin transaction (managed automatically by @Transactional)

	            // DELETE_REG_TABLE_TMP
	            Query query = localEntityManager.createQuery("DELETE FROM SmeRegUserInfoTmp WHERE custId = :custId");
	            query.setParameter("custId", userId);
	            localEntityManager.getTransaction().begin();
	            query.executeUpdate();

	            // DELETE_USER_DROP_OFF_DTL
	            query = localEntityManager.createQuery("DELETE FROM SmeUserDropOffDtl WHERE customerId=:custId");
	            query.setParameter("custId", userId);
	            query.executeUpdate();

	            // DELETE_LOGIN_TABLE_TMP
	            query = localEntityManager.createQuery("DELETE FROM SmeAppLoginInfoTmp WHERE loginId=:loginId");
	            query.setParameter("loginId", userId);
	            query.executeUpdate();

	            // DELETE_PUBKEY_TMP
	            query = localEntityManager.createQuery("DELETE FROM SmeImeiPubkeyEntryTmp WHERE imeiNo=:imei");
	            query.setParameter("imei", imei);
	          
	            query.executeUpdate(); 
	            localEntityManager.getTransaction().commit();


	        } catch (Exception e) {
	            log.info("Exception ===", e);
	            // Handle transaction rollback in case of an exception (managed automatically by @Transactional)
	        }
	    }
	 

	public String getRNBCustomerId(String aliasId) {
		log.info("In getRNBCustomerId function >>>>");
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
			// SELECT_RNBID_FROM_MAPPING=SELECT CUSTOMER_ID FROM
			// RNBID_ALIASID_MAPPING WHERE ALIAS_ID = ?
			pstmt = connection.prepareStatement(getQuery("SELECT_RNBID_FROM_MAPPING"));
			pstmt.setString(1, aliasId);
			rset = pstmt.executeQuery();
			if (rset.next()) {
				return rset.getString(1);
			}

		} catch (Exception e) {
			log.info("Exception ===", e);
		} finally {

			try {
				if (rset != null) {
					rset.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null ) {
					closeConnection(connection,new Object(){}.getClass().getEnclosingMethod().getName().toString());
				}
			} catch (SQLException e) {
				log.info("SQLException ===", e);
			}
		}
		return "";
	}

	@Transactional
	public String getCompanyId(String custId) {
	    String companyId = null;

	    try {
	        // Execute the JPQL query using EntityManager
	        companyId = entityManager.createQuery(
	                "SELECT s.companyId FROM SmeRegUserInfo s WHERE s.custId = :custId", String.class)
	                .setParameter("custId", custId)
	                .getSingleResult();

	        if (companyId == null || companyId.trim().isEmpty()) {
	            // If companyId is null or empty, execute the CUSTOMER_COMPANY_ID query using JPQL
	            String modifiedQuery = "SELECT DISTINCT v.codCust FROM VwChAcctCustXref v " +
	                    "WHERE v.codAcctCustRel = 'SOW' AND v.codProd NOT IN (404) " +
	                    "AND v.codAcctNo IN (SELECT v2.codAcctNo FROM VwChAcctCustXref v2 WHERE v2.codCust = :custId)";

	            companyId = entityManager.createQuery(modifiedQuery, String.class)
	                    .setParameter("custId", custId)
	                    .getSingleResult();
	        }

	        // Store companyId in ApplicationGenParameter.companyCustomerMapList
	        ApplicationGenParameter.companyCustomerMapList.put(custId, companyId);

	    } catch (Exception e) {
	        // Handle exception (log or rethrow)
	        e.printStackTrace(); // Replace with proper logging
	    }

	    return companyId;
	}


	

	

	public JSONObject authUserFlag(JSONObject loginInfo) {
		log.info("Calling authUserFlag method ...");
		JSONObject temp = new JSONObject();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {

			connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
			pstmt = connection.prepareStatement("select USER_TYPE from SME_APP_LOGIN_INFO where LOGIN_ID= ? ");
			pstmt.setString(1, loginInfo.getString("loginID"));
			rset = pstmt.executeQuery();
			if (rset.next()) {
				if (rset.getString(1) != null) {
					temp.put("USER_TYPE", rset.getString(1).trim());
				}
			} else {
				temp.put("USER_TYPE", "dummy");
			}
			rset.close();
			pstmt.close();

		} catch (Exception e) {
			log.info("Exception ===", e);
		} finally {
			try {
				if (rset != null) {
					rset.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					closeConnection(connection,new Object(){}.getClass().getEnclosingMethod().getName().toString());
				}
			} catch (SQLException e) {
				log.info("SQLException ===", e);
			}
		}
		log.info("########################:::Response::::::");
		return temp;
	}

	/*******************************************************
	 * 
	 * This is SME Portal Checker Validate //yeshpal 120917
	 * 
	 *****************************************************/

	
	// #####:::::::::::::::::::@@@@@@@@@@@@@@@@@@@:::

	// Session id save in DB


	

	 @Transactional
	    public boolean validateTokenFromDB(String methodName, String token, String custId) {
 	        log.info("Inside validateTokenFromDB function::ID: Customer Id:" + custId.trim().toUpperCase()
	                + " Access Time" + new Date() + " Call from service " + methodName);

	        boolean result = false;
	        int l_timeDiff = 0;
	        final int SETTIMEDIFF = 50;
	        double l_UpdatedSeq = 0;
	        String dbToken = "";
	        EntityManager localEntityManager = entityManagerFactory.createEntityManager().unwrap(Session.class);
	        try {
	            // Query to get the most recent active session's token and sequence number
	            String queryStr = "SELECT hostSessionId, seq FROM MsmeSecurityLogDetail "
	                    + "WHERE seq = (SELECT MAX(seq) FROM MsmeSecurityLogDetail WHERE userId = :custId AND sessionActive = 'Y')";

	            Query query = entityManager.createQuery(queryStr);
	            query.setParameter("custId", custId.trim().toUpperCase());
	            Object[] resultArray = (Object[]) query.getSingleResult();
	            if (resultArray != null) {
	                dbToken = (String) resultArray[0];
	                l_UpdatedSeq = ((Number) resultArray[1]).doubleValue();
	            }

				if (token.equals(dbToken)) {
					result = true;
					// Update session to inactive
					Query updateQuery = localEntityManager
							.createQuery("UPDATE MsmeSecurityLogDetail SET sessionActive = 'N' WHERE seq = :seq");
					updateQuery.setParameter("seq", l_UpdatedSeq);
					localEntityManager.getTransaction().begin();
					updateQuery.executeUpdate();
					localEntityManager.getTransaction().commit();
	                
	            } else {
	                dbToken = "";

	                // Check previous session
	                String previousSessionQueryStr = "SELECT s.hostSessionId, COALESCE(s.timeDifference, 0) FROM MsmeSecurityLogDetail s "
	                        + "WHERE s.seq = (SELECT MAX(s2.seq) FROM MsmeSecurityLogDetail s2 "
	                        + "WHERE s2.userId = :custId AND s2.seq < (SELECT MAX(s3.seq) FROM MsmeSecurityLogDetail s3 "
	                        + "WHERE s3.userId = :custId))";

	                Query previousSessionQuery = entityManager.createQuery(previousSessionQueryStr);
	                previousSessionQuery.setParameter("custId", custId.trim().toUpperCase());
	                List<Object[]> resultList = previousSessionQuery.getResultList();

	                if (!resultList.isEmpty()) {
	                    Object[] resultArray1 = resultList.get(0);
	                    dbToken = (String) resultArray1[0]; // Ensure this is a String
	                    l_timeDiff = ((Number) resultArray1[1]).intValue(); // Ensure this is converted to int
	                }

	                if (token.equals(dbToken) && l_timeDiff >= SETTIMEDIFF) {
	                    result = true;
	                } else {
	                    dbToken = "";
	                    // Check second last session
	                    String secondLastSessionQueryStr = "SELECT s.hostSessionId, COALESCE(s.timeDifference, 0) FROM MsmeSecurityLogDetail s "
	                            + "WHERE s.seq = (SELECT MAX(s2.seq) FROM MsmeSecurityLogDetail s2 "
	                            + "WHERE s2.userId = :custId AND s2.seq < (SELECT MAX(s3.seq) FROM MsmeSecurityLogDetail s3 "
	                            + "WHERE s3.userId = :custId AND s3.seq < (SELECT MAX(s4.seq) FROM MsmeSecurityLogDetail s4 "
	                            + "WHERE s4.userId = :custId)))";

	                    Query secondLastSessionQuery = entityManager.createQuery(secondLastSessionQueryStr);
	                    secondLastSessionQuery.setParameter("custId", custId.trim().toUpperCase());
	                    resultList = secondLastSessionQuery.getResultList();

	                    if (!resultList.isEmpty()) {
	                        Object[] resultArray2 = resultList.get(0);
	                        dbToken = (String) resultArray2[0];
	                        l_timeDiff = ((Number) resultArray2[1]).intValue(); // Ensure this is converted to int
	                    if (token.equals(dbToken) && l_timeDiff >= SETTIMEDIFF) {
	                        result = true;
	                    }
	                }

	                return result;
	                }
	            }

	            if (result) {
	            	  localEntityManager.getTransaction().begin();

	                  // Create a new instance of the entity
	                  MsmeSecurityLogDetail logDetail = new MsmeSecurityLogDetail();
	                  logDetail.setUserId(custId.trim().toUpperCase());
	                  logDetail.setLoginDt(new Timestamp(System.currentTimeMillis())); // Set current time
	                  logDetail.setRequesttime(new Timestamp(System.currentTimeMillis())); // Set current time
	                  logDetail.setServiceId(methodName);
	                  logDetail.setSessionActive("Y");

	                  // Persist the new entity
	                  localEntityManager.persist(logDetail);

	                  // Commit the transaction
	                  localEntityManager.getTransaction().commit();
	            }

	        } catch (Exception e) {
	        	e.printStackTrace();
	            log.info("Exception -", e);
	        }

	        log.info("validateTokenFromDB Result: " + result);
	        return result;
	    }
	 
	 
	 @Transactional
	    public boolean checkSessionValidate(String sessionID) {
	        String queryStr = "SELECT COUNT(*) FROM SmeAppLoginInfo WHERE sessionId = :sessionId";
	        boolean result = false;

	        try {
	            Query query = entityManager.createQuery(queryStr);
	            query.setParameter("sessionId", sessionID.trim());
	            int count = ((Number) query.getSingleResult()).intValue();

	            if (count > 0) {
	                result = true;
	            }
	        } catch (Exception e) {
	            // Log the exception (consider using a logger)
	            e.printStackTrace();
	        }

	        return result;
	    }
	// @@@@:::::VALIDATE USER LOGIN APP OR PORTAL START::::@@@@@@@@@@@@@@@@@@

	

	// Added by Prabhat for Third Registration on 31-08-2018


	

	// Added by 2157 for NON-RNB customer entry on 04-08-2020 method insert data Id in table
	public void setNonRnbData(String nonRnbCustomerId) {
		log.info("In setNonRnbData" + nonRnbCustomerId);

		PreparedStatement pstmt = null;
		Connection connection = null;
		ResultSet rset = null;
		boolean l_isExit = false;

		try {
			connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());

			// INSERT_INTO_SME_NON_RNB_CUSTOMER_DTL=Insert into
			// SME_NON_RNB_CUSTOMER_DTL (CUSTOMER_ID) values (?)
			// SELECT_COUNT_SME_NON_RNB_CUSTOMER_DTL=Select count(*) from
			// SME_NON_RNB_CUSTOMER_DTL where CUSTOMER_ID = ? and
			// IS_CUSTOMER_EXIT = 'Y'
			// UPDATE_INTO_SME_NON_RNB_CUSTOMER_DTL=Update
			// SME_NON_RNB_CUSTOMER_DTL set IS_CUSTOMER_EXIT = ? where
			// CUSTOMER_ID = ?

			pstmt = connection.prepareStatement(getQuery("SELECT_COUNT_SME_NON_RNB_CUSTOMER_DTL"));
			pstmt.setString(1, nonRnbCustomerId);
			rset = pstmt.executeQuery();
			if (rset.next() && rset.getInt(1) == 0) {
				l_isExit = true;
			}
			rset.close();
			pstmt.close();

			if (l_isExit) {
				pstmt = connection.prepareStatement(getQuery("INSERT_INTO_SME_NON_RNB_CUSTOMER_DTL"));
				pstmt.setString(1, nonRnbCustomerId);
				int result = pstmt.executeUpdate();
				log.info("result......." + result);
				pstmt.close();
			}

		} catch (SQLException e) {
			log.info("SQLException", e);
		} catch (Exception e) {
			log.info("Exception", e);
		} finally {
			try {
				if (rset != null) {
					rset.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					closeConnection(connection,new Object(){}.getClass().getEnclosingMethod().getName().toString());
				}
			} catch (SQLException e) {
				log.info("SQLException -", e);
			}
		}
	}

	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@::::: END::::@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	
	public void maintainCustomerRegistrationProcess(UserLogin userlogin) {
		log.info("Inside maintainCustomerRegistrationProcess " + userlogin.getLoginID().trim());
		PreparedStatement pstmt = null,pstmt1 = null;
		Connection connection = null;
		ResultSet rset = null,rset1=null;

		try {

			connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
			
			pstmt1 = connection.prepareStatement(getQuery("COUNT_REG_USER"));
			pstmt1.setString(1, userlogin.getLoginID().trim());
			rset1 = pstmt1.executeQuery();
			if(rset1.next() && rset1.getInt(1)==1){
				
			}else{	
			pstmt = connection.prepareStatement(getQuery("CUSTOMER_COMPANY_ID"));
			pstmt.setString(1, userlogin.getLoginID().trim());
			rset = pstmt.executeQuery();
			if (rset.next()) {
				userlogin.setCompanyID(rset.getString(1));
			}
			rset.close();
			pstmt.close();
			boolean forgotPasswordFlag = false;
			try{
				pstmt = connection.prepareStatement(getQuery("SELECT_APP_LOGIN_INFO_COUNT"));
				pstmt.setString(1, userlogin.getLoginID());
				rset =pstmt.executeQuery();
				while(rset.next()){
					if(rset.getInt(1) > 0)
						forgotPasswordFlag = true;
				}
				rset.close();
				pstmt.close();
			}
			catch(Exception e){
				log.info("Exception occuredwhile checking customer is registered or not "+e.getLocalizedMessage());
			}
			log.info("forgotpassword scenario "+forgotPasswordFlag);
				if(forgotPasswordFlag== false){
				try{
					
					pstmt = connection.prepareStatement(getQuery("SELECT_DROP_OFF_ENTRY"));
					pstmt.setString(1, userlogin.getLoginID());
					pstmt.setString(2, userlogin.getCompanyID());
					pstmt.setString(3, userlogin.getUserType());
					pstmt.setString(4, userlogin.getUserRegisterType());
					ResultSet rs =pstmt.executeQuery();
					if(rs.next() && rs.getString(1).equals("NA")){
						log.info("data not present in table");
					}else{
						PreparedStatement pstmt2= null; 
						pstmt2 = connection.prepareStatement(getQuery("DELETE_DROP_OFF_CUSTOMER_DTL_BEFORE"));
						pstmt2.setString(1, userlogin.getLoginID());
						pstmt2.setString(2, userlogin.getCompanyID());
						pstmt2.setString(3, userlogin.getUserType());
						pstmt2.setString(4, userlogin.getUserRegisterType());
						int deletedCount=pstmt2.executeUpdate();
						pstmt2.close();
						log.info("deletedCount.."+deletedCount);
					}
					rs.close();
					pstmt.close();
					
					log.info("2330 "+userlogin.getLoginID()+" "+userlogin.getCompanyID());
					pstmt = connection.prepareStatement(getQuery("INSERT_INTO_DROP_OFF_CUSTOMER_DTL"));
					pstmt.setString(1, userlogin.getLoginID());
					pstmt.setString(2, userlogin.getCompanyID());
					pstmt.setString(3, "NA");
					pstmt.setString(4, userlogin.getUserType());
					pstmt.setString(5, userlogin.getUserRegisterType());
					pstmt.executeUpdate();
					pstmt.close();
				
				}
				catch(Exception e){
					log.info("Exception occured while creating entry in drop off table "+e);
				}
			}
			}
		} catch (SQLException e) {
			log.info("SQLException", e);
		} catch (Exception e) {
			log.info("Exception", e);
		} finally {
			try {
				if (rset != null) {
					rset.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}if (rset1 != null) {
					rset1.close();
				}
				if (pstmt1 != null) {
					pstmt1.close();
				}
				if (connection != null) {
					//close connection with closeConnection 2299 - 22032021
					closeConnection(connection,new Object(){}.getClass().getEnclosingMethod().getName().toString());
				}
			} catch (SQLException e) {
				log.error("SQLException -", e);
			}
		}

	}
	
	
	//Added By Deepak For CHECKER BlockUnBlockFlow  on 02-08-2022 
		@Override
		public boolean userIdBlock(String userId, String usrReq) {
			log.info("Inside userIdBlock");
			PreparedStatement pstmt = null;
			Connection connection = null;
			boolean flag = false;
			try {
				connection = getConnection(new Object() {}.getClass().getEnclosingMethod().getName().toString());

				// For Customer Id Block
				pstmt = connection.prepareStatement(getQuery("UPDATE_CUSTOMER_FOR_BLOCK"));
				pstmt.setString(1, userId);
				pstmt.setString(2, usrReq);
				int cnt = pstmt.executeUpdate();
				if (cnt > 0) {
					flag = true;
				}
				pstmt.close();

			} catch (SQLException e) {
				log.info("SQLException", e);
			} catch (Exception e) {
				log.info("Exception", e);
			} finally {
				try {
					if (pstmt != null) {
						pstmt.close();
					}
					if (connection != null) {
						closeConnection(connection, new Object() {}.getClass().getEnclosingMethod().getName().toString());
					}
				} catch (SQLException e) {
					log.error("SQLException -", e);
				}
			}
			return flag;
		}
		
		//Added By Deepak For CHECKER BlockUnBlockFlow  on 17-08-2022 
		 public boolean userValidOrNot(String custId, String usrReq) {
		        log.info("Inside userValidOrNot");
		        boolean flag = false;
		        try {
		            // Create a native query
		            String sql = "SELECT count(*) FROM SmeAppLoginInfo WHERE loginId = :loginId AND userType = :userReq";
		            Query query = entityManager.createQuery(sql);

		            // Set parameters
		            query.setParameter("loginId", custId);
		            query.setParameter("userReq", usrReq);

		            // Execute the query and get the result
		            Number count = (Number) query.getSingleResult();
		            if (count.intValue() > 0) {
		                log.info("User " + custId + " Exists");
		                flag = true;
		            }
		        } catch (Exception e) {
		            log.info("Exception ===", e);
		        }
		        return flag;
		    }
		 
			//Added By Deepak For CHECKER BlockUnBlockFlow  on 17-08-2022 
		@Override
		public boolean checkCustomerBlockOrNot(String custId) {
			log.info("Inside userValidOrNot");
			boolean flag = false;
			Connection connection = null;
			PreparedStatement pstmt = null;
			ResultSet rset = null;
			String userBlock = "";
			try {
				connection = getConnection(new Object() {
				}.getClass().getEnclosingMethod().getName().toString());
				pstmt = connection.prepareStatement(getQuery("GET_SINGLE_CUSTOMER_DATA_FROM_LOGIN_TABLE"));
				pstmt.setString(1, custId);
				pstmt.setString(2, "A");
				rset = pstmt.executeQuery();
				while (rset.next()) {
					userBlock = rset.getString(2);
				}

				if (userBlock.equals("Y")) {
					flag = true;
				}

				rset.close();
				pstmt.close();
			} catch (Exception e) {
				log.info("Exception ===", e);
			} finally {

				try {
					if (rset != null) {

						rset.close();
					}
					if (pstmt != null) {
						pstmt.close();
					}
					if (connection != null ) {
						closeConnection(connection, new Object() {
						}.getClass().getEnclosingMethod().getName().toString());
					}
				} catch (SQLException e) {
					log.info("SQLException ===", e);

				}
			}
			return flag;
		}

		
		//Added By swapnil For CHECKER blockorNot  on sep-26 
		public boolean blockOrLock(String custId) {
	        log.info("Inside blockOrLock");
	        boolean flag = false;
	        try {
	            // Query to check USER_LOCKED and USER_BLOCKED status
	            String queryStr = "SELECT userLocked, userBlocked FROM SmeAppLoginInfo WHERE loginId = :loginId AND userType = :userType";
	            Query query = entityManager.createQuery(queryStr);
	            query.setParameter("loginId", custId);
	            query.setParameter("userType", "A");

	            // Execute the query and get the result
	            Object[] result = (Object[]) query.getSingleResult();
	            String userLock = (String.valueOf(result[1].toString()));
	            String userBlock = (String.valueOf(result[1].toString()));

	            if ("Y".equals(userBlock) || "Y".equals(userLock)) {
	                flag = true;
	            }

	        } catch (Exception e) {
	            log.error("Exception occurred:", e);
	        }
	        return flag;
	    }
	
		
		
		public boolean checkCustomerExistInXREF(String customerID) {
	        boolean existFlag = false;

	        try {
	            // Execute the query using EntityManager
	            Query query = entityManager.createQuery(
	                    "select count(*) from VwChAcctCustXref where codCust = :custId AND codProd NOT IN (404) AND codAcctCustRel not in ('SOW','GUR','JOF','JOO','JAO','JAF','DEV','GUA','NOM','SOL','THR','VAL','COB','OTH','PAR','PRO')");
	            query.setParameter("custId", customerID);

	            Object result = query.getSingleResult();

	            // Get the count result
	            int count = ((Number) result).intValue();

	            // Check if count is greater than zero
	            if (count > 0) {
	                existFlag = true;
	            }

	        } catch (Exception e) {
	            // Handle exception (log or rethrow)
	            e.printStackTrace(); // Replace with proper logging
	        }

	        return existFlag;
	    }
		
//-----------------------------FRM-------------------------------------------------------------------------		
		
		
		public String getDeviceIdDate(String REG_ID) {
			
			log.info("Inside getDevice_Id_Date");
			Connection connection = null;
			PreparedStatement pstmt = null;
			ResultSet rset = null;
			String mobile_reg_date = "";
			
			try {
				connection = getConnection(new Object() {
				}.getClass().getEnclosingMethod().getName().toString());
				pstmt = connection.prepareStatement(getQuery("GET_DEVICE_BND_DATE"));
				pstmt.setString(1, REG_ID);
				
				rset = pstmt.executeQuery();
				if (rset.next()) {
					mobile_reg_date = rset.getString(1);
					log.info("mobile_reg_date ==="+mobile_reg_date);
				}
				rset.close();
				pstmt.close();
			} catch (Exception e) {
				log.info("Exception ===", e);
			} finally {

				try {
					if (rset != null) {
						rset.close();
					}
					if (pstmt != null) {
						pstmt.close();
					}
					if (connection != null ) {
						closeConnection(connection, new Object() {}.getClass().getEnclosingMethod().getName().toString());
					}
				} catch (SQLException e) {
					log.info("SQLException ===", e);
				}
			}
			return mobile_reg_date;
		}
		
		public String getFirstLogin(String serName,String custId) {
			log.info(serName+" =getFirstLogin= "+custId);
			Connection connection = null;
			PreparedStatement pstmt = null;
			ResultSet rset = null;
			int cnt = 0;
			String firstLogin="Y";
			
			try {
				connection = getConnection(new Object() {}.getClass().getEnclosingMethod().getName().toString());
				pstmt = connection.prepareStatement(getQuery("GET_LOGIN_CNT"));
				pstmt.setString(1, serName);
				pstmt.setString(2, custId);
				
				rset = pstmt.executeQuery();
				if (rset.next()) {
					cnt = rset.getInt(1);
				}
				
				if(cnt>0) {
					firstLogin="N";
				}
				
				rset.close();
				pstmt.close();
			} catch (Exception e) {
				log.info("Exception ===", e);
			} finally {

				try {
					if (rset != null) {
						rset.close();
					}
					if (pstmt != null) {
						pstmt.close();
					}
					if (connection != null ) {
						closeConnection(connection, new Object() {}.getClass().getEnclosingMethod().getName().toString());
					}
				} catch (SQLException e) {
					log.info("SQLException ===", e);

				}
			}
			return firstLogin;
		}
		
		public boolean getSecondFactorByRisk(String riskBand,String deviceType) {
			Connection connection=null;
			PreparedStatement pstmt=null;
			ResultSet rset = null;
			boolean secondFactRequired=false;
			try {
				
				connection = getConnection(new Object() {}.getClass().getEnclosingMethod().getName().toString());
				pstmt = connection.prepareStatement(getQuery("GET_AUTH_FACTOR"));
				pstmt.setString(1, riskBand);
				pstmt.setString(2, deviceType);
				log.info("riskBand ==="+riskBand+"--");
				log.info("deviceType ==="+deviceType+"--");
				rset = pstmt.executeQuery();
				if (rset.next()) {
					String secFact= rset.getString(1);
					log.info("secFact ==="+secFact);
					if(secFact!=null && secFact.equals("Y")) {
						secondFactRequired=true;
					}

				}
				rset.close();
				pstmt.close();
			} catch (SQLException e) {
				log.info("SQLException ===", e);
				e.printStackTrace();
			}	
			finally {

				try {
					if (pstmt != null) {
						pstmt.close();
					}
					if (connection != null ) {closeConnection(connection, new Object() {}.getClass().getEnclosingMethod().getName().toString());
					}
				} catch (SQLException e) {
					log.info("SQLException ===", e);
				}
			}
			
			log.info(""+riskBand+"==="+deviceType+"secondFactRequired--->"+secondFactRequired);
			return secondFactRequired;
			
		}
//---------------SIM_BINDING  ----------------------------------------------------------------------
				
		public boolean senderValidOrNot(String CUSTOMER_ID ,String msg) {
			log.info("Inside senderValidOrNot");
			Connection connection = null;
			PreparedStatement pstmt = null;
			ResultSet rset = null;
			int cnt=0;
			boolean isValid=false;
			try {
				connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
				pstmt = connection.prepareStatement(getQuery("GET_SME_SIMBND_VERIFICATION_FLAG"));
				pstmt.setString(1, CUSTOMER_ID);
				pstmt.setString(2, msg);
				rset = pstmt.executeQuery();
				if (rset.next()) {
					cnt= rset.getInt(1);
				}
				
				if(cnt>0) {
					isValid=true;
				}
				
				rset.close();
				pstmt.close();
			} catch (Exception e) {
				log.info("Exception ===", e);
			} finally {

				try {
					if (rset != null) {

						rset.close();
					}
					if (pstmt != null) {
						pstmt.close();
					}
					if (connection != null ) {
						closeConnection(connection, new Object() {
						}.getClass().getEnclosingMethod().getName().toString());
					}
				} catch (SQLException e) {
					log.info("SQLException ===", e);

				}
			}
			return isValid;
		}
		public String getPhoneNoFromView(String  msg,String mobNo) {
			log.info("Inside userValidOrNot");
			Connection connection = null;
			PreparedStatement pstmt = null;
			ResultSet rset = null;
			String vw_mobno="";
			String verfiedFlag="N";

			try {
				
				String COD_CUST_ID =getCustIdFromMSG(msg);
				log.info(" COD_CUST_ID - ---- " + COD_CUST_ID);
				connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
				pstmt = connection.prepareStatement(getQuery("GET_MOBILE_NO_FROM_VIEW"));
				pstmt.setString(1, COD_CUST_ID);
				rset = pstmt.executeQuery();
				if (rset.next()) {

					if(rset.getString(1).length()==10) {
						vw_mobno = "91"+rset.getString(1);// append 91 if length is 10
					}
					else {
						vw_mobno=rset.getString(1);
					}
					
				}

				if(mobNo.length()==10) {
					mobNo = "91"+mobNo;// append 91 if length is 10
				}
				log.info(mobNo+"vw_mobno="+vw_mobno);
				
				if(vw_mobno.equals(mobNo)) {
					verfiedFlag ="Y";
				}

				rset.close();
				pstmt.close();
			} catch (Exception e) {
				log.info("Exception ===", e);
			} finally {

				try {
					if (rset != null) {

						rset.close();
					}
					if (pstmt != null) {
						pstmt.close();
					}
					if (connection != null ) {
						closeConnection(connection, new Object() {
						}.getClass().getEnclosingMethod().getName().toString());
					}
				} catch (SQLException e) {
					log.info("SQLException ===", e);

				}
			}
			return verfiedFlag;
		}


		public int updateSimBindingStatus(JSONObject customerInfo) {

			PreparedStatement pstmt = null;
			ResultSet rset = null;
			int i=0;
			Connection conn = null;

			try {
				log.info("Inside updateSimBindingStatus ----- " + customerInfo);
				conn = getConnection(new Object() {}.getClass().getEnclosingMethod().getName().toString());
				pstmt = conn.prepareStatement(getQuery("UPDATE_SME_SIMBND_DTL"));
				pstmt.setString(1, customerInfo.getString("VERIFIED"));
				pstmt.setTimestamp(2, new java.sql.Timestamp(Utils.toDate(customerInfo.getString("SENT_DATE_TIME"),"yyyy-MM-dd HH:mm:ss").getTime()));
				pstmt.setString(3, CommonUtility.maskString(customerInfo.getString("SENDER")));
				pstmt.setString(4, customerInfo.getString("MSG"));

				i = pstmt.executeUpdate();
				pstmt.close();
				log.info(" UPDATE_SME_SIMBND_DTL inserted....." + i);

			} catch (Exception e) {
				log.info(e);
			} finally {
				try {

					if (rset != null) {
						rset.close();
					}
					if (pstmt != null) {
						pstmt.close();
					}
					if (conn != null) {
						closeConnection(conn, new Object() {
						}.getClass().getEnclosingMethod().getName().toString());
					}
				} catch (Exception e) {
					log.info(e);
				}
			}
			return i;
		}

		public String getCustIdFromMSG(String msg) {

			PreparedStatement pstmt = null;
			ResultSet rset = null;
			String custId ="";
			Connection conn = null;

			try {
				log.info(" msg ----- " + msg);
				conn = getConnection(new Object() {}.getClass().getEnclosingMethod().getName().toString());
				pstmt = conn.prepareStatement(getQuery("SELECT_SME_SIMBND_DTL"));
				pstmt.setString(1, msg);

				rset = pstmt.executeQuery();
				if (rset.next()) {
					custId= rset.getString(1);
				}
				pstmt.close();
				log.info(" custId ....." +custId);

			} catch (Exception e) {
				log.info(e);
			} finally {
				try {

					if (rset != null) {
						rset.close();
					}
					if (pstmt != null) {
						pstmt.close();
					}
					if (conn != null) {
						closeConnection(conn, new Object() {
						}.getClass().getEnclosingMethod().getName().toString());
					}
				} catch (Exception e) {
					log.info(e);
				}
			}
			return custId;
		}

		
		public int insertSimBindingStatus1(JSONObject customerInfo) {

			PreparedStatement pstmt = null;
			ResultSet rset = null;
			Integer id = 0;
			int i=0;
			Connection conn = null;
			System.out.println("customerInfo.getString(\"MSG\")"+customerInfo.getString("MSG"));
			/*String msg = null;
			try {
				msg = java.net.URLDecoder.decode(customerInfo.getString("MSG"), StandardCharsets.UTF_8.name());
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("msg"+msg);*/
			try {
				log.info("Inside insertSimBindingStatus ----- " + customerInfo);
				conn = getConnection(new Object() {}.getClass().getEnclosingMethod().getName().toString());
				pstmt = conn.prepareStatement(getQuery("SME_UPI_TXN_DTL_SEQ"));
				rset = pstmt.executeQuery();
				if (rset.next()) {
					id = rset.getInt(1);
				}
				rset.close();
				pstmt.close();
				pstmt = conn.prepareStatement(getQuery("INSERT_SME_SIMBND_DTL"));
				pstmt.setInt(1, id);
				pstmt.setString(2,customerInfo.getString("CUSTOMER_ID"));
				pstmt.setString(3, customerInfo.getString("MSG"));

				i = pstmt.executeUpdate();
				pstmt.close();
				log.info(" INSERT_SME_SIMBND_DTL inserted....." + i);

			} catch (Exception e) {
				log.info(e);
			} finally {
				try {

					if (rset != null) {
						rset.close();
					}
					if (pstmt != null) {
						pstmt.close();
					}
					if (conn != null) {
						closeConnection(conn, new Object() {
						}.getClass().getEnclosingMethod().getName().toString());
					}
				} catch (Exception e) {
					log.info(e);
				}
			}
			return i;
		}




//------------------------------------------Default limit setup CR-------------------------------------------------
						
//------------------------------------------1>IS_CUSTOMER_SOLE_PROPRIETOR------------------------------------------
		
	public boolean isCustomerSoleProp(String loginId) {

		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Connection conn = null;
		String result = null;
		int countOfCompanyId = 0;
		boolean existFlag = false;
		String sql = "";
		
		try {
		
			conn = getConnection(new Object() {}.getClass().getEnclosingMethod().getName().toString());

			if (environmentStr.equals("UAT")) {
				
				sql = getQuery("CHECK_CUSTOMER_SOLE_PROPRIETOR_UAT");
				
			} else if (environmentStr.equals("LIVE")) {
				
				sql = getQuery("CHECK_CUSTOMER_SOLE_PROPRIETOR_LIVE");
				
			} else {
				sql = getQuery("CHECK_CUSTOMER_SOLE_PROPRIETOR_LOCAL");
			}
			
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, loginId);

			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				
				countOfCompanyId = resultSet.getInt(1);
				System.out.println(result);

			}
			if (countOfCompanyId > 0) {
				existFlag = true;
				log.info(" sole propriter CustID ----- " + loginId);
			}
			log.info(" custId ....." + result);

		} catch (Exception e) {
			log.info(e);
		} finally {
			try {

				if (rset != null) {
					rset.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					closeConnection(conn, new Object() {}.getClass().getEnclosingMethod().getName().toString());
				}
			} catch (Exception e) {
				log.info(e);
			}
		}

		return existFlag;
	}

//------------------------------------------2>GET_COMPANYID_OF_SOLE_PROP_CUSTOMER------------------------------------------
		
	public String getCompanyIdOfSoleProprietorCustomer(String loginId) {

		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Connection conn = null;
		String companyId = null;
			
		try {

			conn = getConnection(new Object() {}.getClass().getEnclosingMethod().getName().toString());
			PreparedStatement statement = conn.prepareStatement(getQuery("SELECT_COMPANY_ID"));
			statement.setString(1, loginId);

			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				companyId = resultSet.getString(1);

			}

		} catch (Exception e) {
			log.info(e);
		} finally {
			try {

				if (rset != null) {
					rset.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					closeConnection(conn, new Object() {
					}.getClass().getEnclosingMethod().getName().toString());
				}
			} catch (Exception e) {
				log.info(e);
			}
		}

		return companyId;
	}

//------------------------------------------3>GET_ACCOUNT_OF_SOLE_PROP_CUSTOMER------------------------------------------
		
	public String getAccountNumberOfSoleProprietorCustomer(String loginId) {

		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Connection conn = null;
		String accountNo = null;

		try {

			conn = getConnection(new Object() {}.getClass().getEnclosingMethod().getName().toString());
			PreparedStatement statement = conn.prepareStatement(getQuery("SELECT_ACCOUNT_OF_SOLE_PROP_CUSTOMER"));
			statement.setString(1, loginId);

			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				accountNo = resultSet.getString(1);

			}
				
		} catch (Exception e) {
			log.info(e);
		} finally {
			try {

				if (rset != null) {
					rset.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					closeConnection(conn, new Object() {}.getClass().getEnclosingMethod().getName().toString());
				}
			} catch (Exception e) {
				log.info(e);
			}
		}

		return accountNo;
	}

//------------------------------------------4>GET_GROUPID_FROM_GROUPNAME------------------------------------------
		
	public String getGroupIDFromGroupName() {

		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Connection conn = null;
		String groupID = null;
			
		try {
			
			conn = getConnection(new Object() {}.getClass().getEnclosingMethod().getName().toString());
			PreparedStatement statement = conn.prepareStatement(getQuery("SELECT_GROUPID_FROM_GROUPNAME"));
			statement.setString(1, "Financial Transactions - Onscreen");
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				groupID = resultSet.getString(1);

			}

		} catch (Exception e) {
			log.info(e);
		} finally {
			try {

				if (rset != null) {
					rset.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					closeConnection(conn, new Object() {}.getClass().getEnclosingMethod().getName().toString());
				}
			} catch (Exception e) {
				log.info(e);
			}
		}

		return groupID;
	}
		
//------------------------------------5>GET_SERVICECD_FROM_COM_SERVICE_MST--------------------------------------------
		
	public String[] getServiceCdFromComServiceMst() {

		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Connection conn = null;
		List<String> serviceCdList = new ArrayList<>();

		try {

			conn = getConnection(new Object() {}.getClass().getEnclosingMethod().getName().toString());
			PreparedStatement statement = conn.prepareStatement(getQuery("SELECT_SERVICECD_FROM_COM_SERVICE_MST"));
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				String serviceCd = resultSet.getString("SERVICE_CD");
				serviceCdList.add(serviceCd);
			}

		} catch (Exception e) {
			log.info(e);
		} finally {
			try {
				if (rset != null) {
					rset.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					closeConnection(conn, new Object() {
					}.getClass().getEnclosingMethod().getName().toString());
				}
			} catch (Exception e) {
				log.info(e);
			}
		}
		
		// Convert List<String> to String[]
		String[] serviceCdArray = serviceCdList.toArray(new String[0]);
		return serviceCdArray;
	}

//---------------------------------6>CHECK_CUSTOMER_LIMIT_SETUP_OR_NOT-------------------------------------------
		
	public boolean isCustomerLimitSetupOrNot(String loginId) {

		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Connection conn = null;
		String result = null;
		int countOfCompanyId = 0;
		boolean existFlag = false;
		
		try {
			
			conn = getConnection(new Object() {}.getClass().getEnclosingMethod().getName().toString());
			
			PreparedStatement statement = conn.prepareStatement(getQuery("CHECK_CUSTOMER_LIMIT_SETUP_OR_NOT"));
			statement.setString(1, loginId);
			ResultSet resultSet = statement.executeQuery();
		
			while (resultSet.next()) {
				
				countOfCompanyId = resultSet.getInt(1);
				System.out.println(result);

			}
			if (countOfCompanyId > 0) {
				existFlag = true;

			}

		} catch (Exception e) {
			log.info(e);
		} finally {
			try {

				if (rset != null) {
					rset.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					closeConnection(conn, new Object() {}.getClass().getEnclosingMethod().getName().toString());
				}
			} catch (Exception e) {
				log.info(e);
			}
		}

		return existFlag;
	}


public net.sf.json.JSONObject checkValidRegistration(UserLogin userlogin, String regFlag) {
	log.info("In checkValidRegistration function" + userlogin.getRetailBankUserID() + "   ");
	String rnbPass = "";
	String rndMapId = "";
	boolean flag = false;
	net.sf.json.JSONObject m_res_header = new net.sf.json.JSONObject();
	Connection connection = null;
	PreparedStatement pstmt = null;
	ResultSet rset = null;
	try {
		connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());

		JSONObject m1_res_header = new JSONObject();

		//// Added by Prabhat for Third Registration on 31-08-2018
		if ("THIRD".equals(regFlag)) {
			if (userlogin.getRetailBankUserID().trim().matches("[0-9]+")) {

				pstmt = connection.prepareStatement(getQuery("CHECK_PAN_DOB_VALIDATE"));
				pstmt.setString(1, userlogin.getRetailBankUserID());
				pstmt.setString(2, userlogin.getCustPanNo().trim());
				pstmt.setString(3, userlogin.getCustDOB().trim().replace("/", ""));
				rset = pstmt.executeQuery();
	
			//	log.info(rset);
				if (rset.next() && rset.getInt(1) > 0) {
					flag = true;

				}
				rset.close();
				pstmt.close();

				if(flag == false){

						pstmt = connection.prepareStatement(getQuery("UPDATE_REGISTRATION_DTL"));
						pstmt.setString(1, "Invalid Pan Card or Date of Birth ");
						pstmt.setString(2, userlogin.getLoginID().trim());
						pstmt.setString(3, userlogin.getUserType());
						pstmt.setString(4, userlogin.getCompanyID());
						pstmt.setString(5, userlogin.getUserType());
						int j = pstmt.executeUpdate();
						log.info("records updated "+j);
						pstmt.close();
					
				}
			}
		} else {
			if (userlogin.getRetailBankUserID().trim().matches("[0-9]+")) {
				// VALID_RNB_REG=select RNB_PASS from COM_RNB_USER_MST where
				// RNBID =
				// ? and RNB_USER_STATUS = 'A'
				pstmt = connection.prepareStatement(getQuery("VALID_RNB_REG"));
				pstmt.setString(1, userlogin.getRetailBankUserID());
				rset = pstmt.executeQuery();
				if (rset.next()) {
					flag = true;
					rnbPass = rset.getString(1);
				}
				rset.close();
				pstmt.close(); //close for further use-2299
			} else {
				// VALID_RNB_REG_FOR_ALIAS=select RNB_PASS from
				// COM_RNB_USER_MST where ALIAS_NAME = ? and RNB_USER_STATUS
				// = 'A'
				pstmt = connection.prepareStatement(getQuery("VALID_RNB_REG_FOR_ALIAS"));
				pstmt.setString(1, userlogin.getRetailBankUserID());
				rset = pstmt.executeQuery();
				if (rset.next()) {
					flag = true;
					rnbPass = rset.getString(1);
					rndMapId = rset.getString(2);
				}
				rset.close();
				pstmt.close(); //close for further use-2299
			}
		}

		//rset.close();
		//pstmt.close();

		log.info("getRetailBankUserID:::" + userlogin.getRetailBankUserID() + ":::pass::");
		if (!flag) {
			// log.info("If cond :" + rnbPass + " 1" +
			// userlogin.getPassword());
			// Not a valid RNB credential
			/*
			 * m1_res_header.put("Status", "SUCCESS");
			 * m1_res_header.put("CustomerID",
			 * userlogin.getRetailBankUserID());
			 * s_res_header.put("ValidateRNBLoginRes", m1_res_header);
			 */
			m_res_header.put("ResCode", "1");
			m_res_header.put("ErrorDetails", "Wrong RNB Credential.");
		} else {
			// log.info("If cond :" + rnbPass + " 2" +
			// userlogin.getPassword());
			if ("THIRD".equals(regFlag)) {
				m1_res_header.put("Status", "SUCCESS");
				m1_res_header.put("CustomerID", userlogin.getRetailBankUserID());
				m1_res_header.put("RnbCustomerID", userlogin.getRetailBankUserID());
				// s_res_header.put("ValidateRNBLoginRes", m1_res_header);
				m_res_header.put("ResCode", "0");
				m_res_header.put("ResBody", m1_res_header);
			} else {
				if (rnbPass.equals(userlogin.getPassword())) {
					// Go for Success
					// log.info("If cond :" + rnbPass + " 1" +
					// userlogin.getPassword());
					m1_res_header.put("Status", "SUCCESS");
					m1_res_header.put("CustomerID", userlogin.getRetailBankUserID());
					m1_res_header.put("RnbCustomerID", rndMapId);
					// s_res_header.put("ValidateRNBLoginRes",
					// m1_res_header);
					m_res_header.put("ResCode", "0");
					m_res_header.put("ResBody", m1_res_header);
				} else {
					// log.info("If cond :" + rnbPass + " 3" +
					// userlogin.getPassword());
					// Not a valid credential
					m_res_header.put("ResCode", "1");
					m_res_header.put("ErrorDetails", "Wrong RNB Credential.");
				}
			}
		}
	} catch (Exception e) {
		//log.info("Exception ===", e);
	} finally {

		try {
			if (rset != null) {
				rset.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (connection != null ) {
				closeConnection(connection,new Object(){}.getClass().getEnclosingMethod().getName().toString());
			}
		} catch (SQLException e) {
		//	log.info("SQLException ===", e);
		}
	}

	return m_res_header;

}
	

	
	
//-------------------------------------------------END------------------------------------------------------------------
	
	@Override
    public String validateShareKeyForVersoionIssue(String IMEI, String KeyFromUI, String KeyFromUIEncrypted) {

        EntityManager localEntityManager = entityManagerFactory.createEntityManager().unwrap(Session.class);

        try {

            // Create a NativeQuery for the update statement
            jakarta.persistence.Query query = localEntityManager.createNativeQuery("UPDATE SME_IMEI_PUBKEY_ENTRY SET PUB_KEY = :pub_key WHERE IMEI_NO = :imei_no");
            query.setParameter("pub_key", KeyFromUIEncrypted);
            query.setParameter("imei_no", IMEI);

            localEntityManager.getTransaction().begin();
            int result = query.executeUpdate();
            localEntityManager.getTransaction().commit();

            // log.info("Rows updated: " + result);

        } catch (Exception e) {
            // log.error("Exception occurred:", e);
        } finally {

        }

        return KeyFromUI;
    }






}
