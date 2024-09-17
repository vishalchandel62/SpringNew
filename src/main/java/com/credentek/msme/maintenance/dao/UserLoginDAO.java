package com.credentek.msme.maintenance.dao;

import org.json.JSONObject;

import com.credentek.msme.loginmodel.UserLogin;

public interface UserLoginDAO {

	// public net.sf.json.JSONObject checkValidRegistration(UserLogin userlogin,String regFlag);
	 
	public net.sf.json.JSONObject checkDebitRegistration(UserLogin userlogin);
	
	public boolean checkCustomerExistInXREF(String customerID);



	public boolean userLogout(UserLogin userlogin);

	public boolean setMpinApin(JSONObject pinData);

	public JSONObject insertRegistrationDetail(UserLogin userlogin);




	public boolean userExitCheck(String indata);

	public boolean userBindWithMobCheck(String userID, String imei);

	public boolean imeiExitCheck(String indata);


	public void updateToken(String logInID, String token);

	public JSONObject logoutUser(String logInID);




	//public JSONObject getListOfAccount(JSONObject custJSON, boolean mFlag);

	// Added by prabhat 18-05-17
	public boolean imeiExitCheckInPubkeyTable(String indata);

	public void deleteTempTableOfUser(String userId, String imei);

	public JSONObject insertRegistrationDetailUpdated(UserLogin userlogin);

	public JSONObject getCustomerDetail(UserLogin userlogin);

	public boolean validateLoginRequest(String requestToken);

	public boolean checkSessionValidate(String sessionID);

	//public String getRNBCustomerId(String aliasId);

	//public boolean checkAccNumForCustomer(String customerID, String accountNo);

	public JSONObject getServiceCodeForCustomer(String userID);

	// Added by Prabhat for Third Registration on 31-08-2018
	//public boolean isUserCredentialExist(UserLogin userlogin);

	public String validateShareKeyForVersoionIssue(String iMEINO, String uiShareKeyToken, String uiShareKey);

	// Added by 2157 for NON-RNB customer entry on 04-08-2020
	public void setNonRnbData(String nonRnbCustomerId);

	// Added By Deepak For CHECKER BlockUnBlockFlow on 02-08-2022
	public boolean userIdBlock(String userId, String usrReq);

	// Added By Deepak For CHECKER BlockUnBlockFlow on 17-08-2022
	public boolean userValidOrNot(String userId, String usrReq);

	// Added By Deepak For CHECKER BlockUnBlockFlow on 26-08-2022
	public boolean checkCustomerBlockOrNot(String userId);

	public String getCompanyId(String custId);
	

}
