package com.credentek.msme.loginservice;

import org.json.JSONObject;

import com.credentek.msme.loginmodel.AccountDetails;

public interface CustomerManagementDAO {
	

	public JSONObject getAllAccDetails(AccountDetails acc_dtl); 
	
	//Added By Deepak For CHECKER BlockUnBlockFlow on 11-08-2022
		public net.sf.json.JSONObject handleBlockUnblockWorkFlow(String customerId,String userType, String usrReq);
		
		//public boolean checkCustomerExistInXREF(String customerID);
	//	public boolean checkCustomerDeactivationFlag(String customerID);
		
		
		
		
		

}
