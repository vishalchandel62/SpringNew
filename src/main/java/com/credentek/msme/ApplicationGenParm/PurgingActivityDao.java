package com.credentek.msme.ApplicationGenParm;

public interface PurgingActivityDao {

	
	 void updateThreadRequestTable(String threadName);
	 boolean insertThreadStatus(String p_ThreadName, String p_ThreadVersion, String p_Status, String p_threadID);
	
}
