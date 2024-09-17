package com.credentek.msme.api.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.credentek.msme.database.DaoBase;
import com.credentek.msme.utils.PathRead;

public class ServiceCallAPI  extends DaoBase{
	
	
	public static final Logger log = LogManager.getLogger(ServiceCallAPI.class);
	public static String ServiceURLbene = "";
	public static String ServiceURL = "";
	public static PathRead path = PathRead.getInstance();
	public static String serverType = "";
	public static String serverTypeApi = "";
	public static String serverRequesterID = "";
	public static String orgName="";
	public static HashMap<String, String> apiURLMasterList = null;
	public static String channel1="";
	/*public static String p2Achannel1="";*/
	/*public ServiceCallAPI()
	{
		callServiceCallAPI();
	}*/
	public void callServiceCallAPI()
	{
		//p2Achannel1=path.getPath("channel1");
		channel1=path.getPath("channel");
		serverType = path.getPath("serverType");
		serverTypeApi = path.getPath("serverTypeApi");
		serverRequesterID = path.getPath("serverRequesterID");
		orgName=path.getPath("organisationName");
		if(serverType.equals("UAT"))//For UAT API Call
		{
			ServiceURLbene = "https://10.0.40.239:7081/Worklight";
			ServiceURL = "https://10.0.45.87:1443";
			
			        /*if("OBDX".equals(serverTypeApi))
					{
				        new ServiceCallAPI().updateApiList("OBDX");
					}
			        else if("YESUAT".equals(serverTypeApi))
			        {
			        	new ServiceCallAPI().updateApiList("YESUAT");
			        }
			        else
			        {
			        	new ServiceCallAPI().updateApiList("UAT");
			        }*/
			
			new ServiceCallAPI().updateApiList("UAT");
			
		}
		else if(serverType.equals("LIVE"))//For LIVE API Call
		{
			ServiceURLbene = "http://LIVE";
			new ServiceCallAPI().updateApiList("LIVE");
		}
		else if(serverType.trim().equals("LOCAL"))//For LIVE API Call
		{
			new ServiceCallAPI().updateApiList("UAT");
		}
		else if(serverType.trim().equals("LOCAL_WEBLOGIC"))//For LIVE API Call
		{
			new ServiceCallAPI().updateApiList("UAT");
		}
		
	}
	
	
	public void updateApiList(String envName){
		log.info("####### updateApiList - "+orgName+"......"+envName.trim());
		apiURLMasterList = new HashMap<String,String>();
		PreparedStatement pStatement = null;
		ResultSet rSet = null;
		Connection conn = null;
		
		String sql = "SELECT C.REST_SERVICE_NAME,NVL(C.URL,'NA') AS URL FROM COM_SERVICE_API_MASTER C WHERE C.BANK_CODE = ? and C.ENVIRONMENT=? and C.REST_SERVICE_NAME<>'NA'";
		
		try
		{
			conn = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
			pStatement = conn.prepareStatement(sql);
			pStatement.setString(1, orgName.trim());
			pStatement.setString(2, envName.trim());
			rSet = pStatement.executeQuery();
			while(rSet.next())
			{
				apiURLMasterList.put(rSet.getString(1), rSet.getString(2));
			}
			log.info(apiURLMasterList.size());
			rSet.close();
			pStatement.close();
		}
		catch (SQLException sqlEx)
		{
			log.info(sqlEx);
			log.info("Database is Not Bound : So an empty prepareUserMenuList list is Made.");
		}
		catch(Exception e)
		{
			log.info(e);
		}
		finally
		{
			try
            {
                if (rSet != null){
                    rSet.close();
                }
                if (pStatement != null){
                	pStatement.close();
                }

                if(conn!=null ){
                	closeConnection(conn,new Object(){}.getClass().getEnclosingMethod().getName().toString());
                }
            }
			catch(Exception ex){
				log.info(ex);
            }
		}
	}
	

}
