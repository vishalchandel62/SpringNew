package com.credentek.msme.api.dao;

import java.util.HashSet;

import net.sf.json.JSONObject;

public interface accountDetailsForOnlineOdDao {
	
	JSONObject fetchAccountDetailsForOd(String account,String CustId,String LineID,String ParentLineID,String balFromAcctMast);
	String makeListOfAccount(HashSet<String> listAsccount);
	String formatAllSrn(String singleBeneJson,String GroupBeneJson);

}
