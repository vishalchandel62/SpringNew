package com.credentek.msme.logindao;

import org.springframework.stereotype.Repository;

import com.credentek.msme.loginmodel.AccountDetails;
import com.credentek.msme.loginmodel.UserLogin;

import org.json.JSONObject;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface LoginDao  {
	

	//public JSONObject getAllAccDetails(AccountDetails acc_dtl); 
	
		public JSONObject handleBlockUnblockWorkFlow(String customerId,String userType, String usrReq);
		
		public String getCustIDForAlias(String aliasName);
		
		  public boolean checkCustomerDeactivationFlag(String customerID);
	 
	 
}
	
	
