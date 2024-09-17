package com.credentek.msme.ApplicationGenParm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;


import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class PurgingActivityDaoImpl  implements PurgingActivityDao {

	private static final Log log = LogFactory.getLog("com.credentek.sme.sceduler.util.PurgingActivityDaoImpl");
	@Autowired
	private EntityManager entityManager;
	
//	public synchronized boolean purgeActivityOnTable(String purgeTable)
//	{
//		log.info("purgeActivityOnTable call");
//		String reGenFlag = "";
//		String nextExecutionDate="";
//		Date sysDate = new Date();
//		Connection connection=null;
//		PreparedStatement pStatement=null;
//		ResultSet resultSet=null;
//		String sql="";
//		String executionTime="";
//		int processFlag=0;
//		int frequency=0;
//		Date lastExecutionDate=null;
//		Date nextScheduleDate=null;
//		boolean status = false;
//		try 
//		{
//			connection=getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
//			SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
//			sql="SELECT NEXT_EXECUTION_DATE,EXECUTION_TIME,REGEN_FLG,LAST_EXECUTION_DATE, NEXT_SCHEDULE_DATE,FREQUENCY FROM SME_THREAD_REQUEST_DTL "
//					+ "WHERE (NEXT_SCHEDULE_DATE IS NULL OR NEXT_SCHEDULE_DATE < ?) AND REPORT_CODE=?";
//			pStatement=connection.prepareStatement(sql);
//			pStatement.setTimestamp(1,  new Timestamp(sysDate.getTime()) );
//			pStatement.setString(2, purgeTable.split("-")[0]);
//			resultSet=pStatement.executeQuery();
//
//			if(resultSet.next())
//			{
//				nextExecutionDate=resultSet.getString(1);
//				executionTime=resultSet.getString(2);
//				reGenFlag=resultSet.getString(3);
//				lastExecutionDate=resultSet.getDate(4);
//				nextScheduleDate=resultSet.getDate(5);
//				frequency = resultSet.getInt(6);
//				if(nextScheduleDate == null)
//				{
//					nextScheduleDate = sysDate;
//				}
//				processFlag=1;
//
//			}
//
//			resultSet.close();
//			pStatement.close();
//
//			try 
//			{
//				//String str[]={dsbTxn, dsbPick, dsbCust, proFund}
//				int count=0;
//
//				if(processFlag==1 && purgeTable.contains("PURGE_PROCESS_THREAD"))
//				{
//					log.info("Go For Purging");
//					
//					// Activity log Purging 
//					//INSERT_ACTIVITY_LOG_RECORD_IN_HISTORY=insert into SME_TXN_AUDIT_ACTIVITY_LOG_HIS (select * from SME_TXN_AUDIT_ACTIVITY_LOG where CHANGED_DT < sysdate-?)
//							//DELETE_ACTIVITY_LOG_FROM_MAIN=DELETE SME_TXN_AUDIT_ACTIVITY_LOG where CHANGED_DT < sysdate-?
//					log.info("Activity log Purging");
//					pStatement=connection.prepareStatement(getQuery("INSERT_ACTIVITY_LOG_RECORD_IN_HISTORY"));
//					pStatement.setInt(1,Integer.parseInt(ApplicationGenParameter.PURGE_ACTIVITY_DAYS.trim()));
//					count = pStatement.executeUpdate();
//					log.info("Total "+count+"records is moved.");
//					pStatement.close();
//					
//					pStatement=connection.prepareStatement(getQuery("DELETE_ACTIVITY_LOG_FROM_MAIN"));
//					pStatement.setInt(1,Integer.parseInt(ApplicationGenParameter.PURGE_ACTIVITY_DAYS.trim()));
//					count = pStatement.executeUpdate();
//					log.info("Total "+count+"records is deleted.");
//					pStatement.close();
//					
//					// Activity Approval log Purging 
//					log.info("Activity Approval log Purging");
//					//INSERT_SME_TXN_APPROVAL_ACTIVITY_LOG_IN_HISTORY=insert into SME_TXN_APPRL_ACT_LOG_HIS (select * from SME_TXN_APPROVAL_ACTIVITY_LOG where AUTH_DT < sysdate-?)
//					//DELETE_SME_TXN_APPROVAL_ACTIVITY_FROM_MAIN=DELETE SME_TXN_APPROVAL_ACTIVITY_LOG where CHANGED_DT < sysdate-?
//					pStatement=connection.prepareStatement(getQuery("INSERT_SME_TXN_APPROVAL_ACTIVITY_LOG_IN_HISTORY"));
//					pStatement.setInt(1,Integer.parseInt(ApplicationGenParameter.PURGE_ACTIVITY_DAYS.trim()));
//					count = pStatement.executeUpdate();
//					log.info("Total "+count+"records is moved.");
//					pStatement.close();
//					
//					pStatement=connection.prepareStatement(getQuery("DELETE_SME_TXN_APPROVAL_ACTIVITY_FROM_MAIN"));
//					pStatement.setInt(1,Integer.parseInt(ApplicationGenParameter.PURGE_ACTIVITY_DAYS.trim()));
//					count = pStatement.executeUpdate();
//					log.info("Total "+count+"records is deleted.");
//					pStatement.close();
//					
//					// Service Activity log Purging
//					log.info("Service Activity log Purging");
//					
//					//INSERT_SME_SERV_APP_ACTIVITY_LOG_HIS_IN_HISTORY=insert into SME_SERV_APP_ACTIVITY_LOG_HIS (select * from SME_SERV_APP_ACTIVITY_LOG where REQUESTED_DT < sysdate-15)
//					//DELETE_SME_SERV_APP_ACTIVITY_LOG_FROM_MAIN=DELETE SME_SERV_APP_ACTIVITY_LOG where REQUESTED_DT < sysdate-? 
//					
//					pStatement=connection.prepareStatement(getQuery("INSERT_SME_SERV_APP_ACTIVITY_LOG_HIS_IN_HISTORY"));
//					pStatement.setInt(1,Integer.parseInt(ApplicationGenParameter.PURGE_ACTIVITY_DAYS.trim()));
//					count = pStatement.executeUpdate();
//					log.info("Total "+count+"records is moved.");
//					pStatement.close();
//					
//					pStatement=connection.prepareStatement(getQuery("DELETE_SME_SERV_APP_ACTIVITY_LOG_FROM_MAIN"));
//					pStatement.setInt(1,Integer.parseInt(ApplicationGenParameter.PURGE_ACTIVITY_DAYS.trim()));
//					count = pStatement.executeUpdate();
//					log.info("Total "+count+"records is deleted.");
//					pStatement.close();
//					
//					
//					Calendar c = Calendar.getInstance();
//					c.setTime(nextScheduleDate);
//					while (nextScheduleDate.getTime() < sysDate.getTime())	// keep increasing till next schedule date is greater than current run time
//					{
//						c.add(Calendar.MINUTE, frequency);  // number of min to add
//						nextScheduleDate=c.getTime();
//					}
//					SimpleDateFormat sdf1=new SimpleDateFormat("HH:mm");
//
//					nextExecutionDate = sdf.format(nextScheduleDate);
//
//					executionTime=sdf1.format(nextScheduleDate);
//
//
//					sql="UPDATE SME_THREAD_REQUEST_DTL SET  NEXT_EXECUTION_DATE = ?, EXECUTION_TIME = ?, " +
//							"LAST_EXECUTION_DATE=?, NEXT_SCHEDULE_DATE = ?  WHERE REPORT_CODE = ?";
//					
//					pStatement=connection.prepareStatement(sql);
//					
//					pStatement.setString(1,nextExecutionDate );
//					pStatement.setString(2,executionTime );
//					pStatement.setTimestamp(3, new Timestamp(sysDate.getTime()) );
//					pStatement.setTimestamp(4, new Timestamp(nextScheduleDate.getTime()) );
//					pStatement.setString(5,purgeTable.split("-")[0]);
//					log.info("Updating for Purge Type : "+purgeTable.split("-")[0]);
//
//					int x=pStatement.executeUpdate();
//
//					if(x>0){
//
//						log.info(x +" Records Updation Successfully for SME_THREAD_REQUEST_DTL for next execution");
//					}
//					else
//					{
//						log.info("Records Updation Failed");
//					}
//					pStatement.close();
//					status = false;
//				}
//				else if(processFlag==1 && purgeTable.contains("GEFU_CREATION_THREAD")) 
//				{
//					log.info("Go For Limit Gefu Creation");
//					
//					boolean isInIntevalTime = false;
//					
//					Calendar systemCal = Calendar.getInstance();
//				    String strTimeFormat = "HH:mm:ss";
//				    DateFormat timeFormat = new SimpleDateFormat(strTimeFormat);
//				    DateFormat simDatFor = new SimpleDateFormat("YYYYMMDD");
//					String systemDate = simDatFor.format(systemCal.getTime());
//					String baBankMastDate = "";
//					//SELECT_BA_BANK_MAST_DATE=select to_char(DAT_PROCESS,'YYYYMMDD') from BA_BANK_MAST;
//					pStatement=connection.prepareStatement(getQuery("SELECT_BA_BANK_MAST_DATE"));
//					resultSet = pStatement.executeQuery();
//					if(resultSet.next()) 
//					{
//						baBankMastDate = resultSet.getString(1);
//					}
//					resultSet.close();
//					pStatement.close();
//					if(systemDate.equals(baBankMastDate)) 
//					{
//						log.info("Both date are same");
//						try {
//
//						    String startTime = ApplicationGenParameter.parameterMasterList.get("GEFU_CREATION_BLOCK_TIME").split("~")[0].trim()+":00";
//						    log.info("startTime ="+startTime);
//						    Date startTimeObj = timeFormat.parse(startTime);
//						    Calendar calendar1 = Calendar.getInstance();
//						    log.info("calendar1= " +calendar1.getTime());
//						    calendar1.setTime(startTimeObj);
//
//						    String endTime = ApplicationGenParameter.parameterMasterList.get("GEFU_CREATION_BLOCK_TIME").split("~")[1].trim()+":00"; 
//						    log.info("endTime ="+endTime);
//						    Date endtTimeObj = timeFormat.parse(endTime);
//						    Calendar calendar2 = Calendar.getInstance();
//						    calendar2.setTime(endtTimeObj);
//						    calendar2.add(Calendar.DATE, 1);
//
//						    
//					        Calendar cal = Calendar.getInstance();
//					        Date date=cal.getTime();
//					       // DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//					        String someRandomTime=timeFormat.format(date);
//					        log.info("someRandomTime ="+someRandomTime);
//						    //String someRandomTime = "00:00:01";
//						    Date d = timeFormat.parse(someRandomTime);
//						    Calendar calendar3 = Calendar.getInstance();
//						    calendar3.setTime(d);
//						    
//						    Date x1 = calendar3.getTime();
//						    
//						    String midTime = "23:59:59";
//						    log.info("midTime ="+midTime);
//						    Date midTimeObj = timeFormat.parse(midTime);
//						    Calendar calendar4 = Calendar.getInstance();
//						    //log.info("calendar4= " +calendar4.getTime());
//						    calendar4.setTime(midTimeObj);
//						    
//						    if(x1.after(calendar4.getTime()) && x1.before(calendar2.getTime())) 
//						    {
//						    	calendar3.add(Calendar.DATE, 1);
//						    }
//						    
//						    //log.info("isInIntevalTime= " +isInIntevalTime);
//						    
//
//						    log.info("calendar1= " +calendar1.getTime());
//						    log.info("calendar2= " +calendar2.getTime());
//						    log.info("calendar3= " +calendar3.getTime());
//						    log.info("calendar4= " +calendar4.getTime());
//						    Date x = calendar3.getTime();
//						    log.info("x "+x);
//						    if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
//						        //checkes whether the current time is between 14:49:00 and 20:11:13.
//						    	isInIntevalTime = true;
//						    }
//						} catch (ParseException e) {
//							log.info("ParseException = "+e);
//						}
//						
//						log.info("isInIntevalTime = "+isInIntevalTime);
//						
//						if(!isInIntevalTime) 
//						{
//							
//							log.info("If it is in time interval = "+ new Date());
//							
//							JSONArray gefuDtlArray = new JSONArray();
//							//SELECT_TRACKER_ID_FOR_GEFU_CREATION=select OD_TRACK_ID,GEFU_JSON_DATA,GEFU_DTL_ENTRY_INITIATED_DT from SME_ONLINE_OD_GEFU_DTL where IS_LIMIT_GEFU_EXIT = ?
//							pStatement=connection.prepareStatement(getQuery("SELECT_TRACKER_ID_FOR_GEFU_CREATION"));
//							pStatement.setString(1,"N");
//							resultSet = pStatement.executeQuery();
//							while(resultSet.next()) 
//							{
//								JSONObject l_gefuDtl = new JSONObject();
//								l_gefuDtl.put("trackerId", resultSet.getString(1));
//								l_gefuDtl.put("gefuJsonDtl", resultSet.getString(2));
//								l_gefuDtl.put("gefuInitiationDT", resultSet.getString(3));
//								gefuDtlArray.add(l_gefuDtl);
//							}
//							resultSet.close();
//							pStatement.close();
//							
//							for(int i=0;i<gefuDtlArray.size();i++) 
//							{
//								 boolean isGefuProcess = false;
//								 JSONObject l_gefuDtl = gefuDtlArray.getJSONObject(i);
//								 log.info("Go For Limit Gefu Creation trackerId = "+l_gefuDtl.get("trackerId").toString());
//								 OnlineOdCustomerDao l_odObj = new OnlineOdCustomerDaoImpl();
//								 isGefuProcess = l_odObj.limitSetUpGefuCreation(l_gefuDtl.get("gefuJsonDtl").toString());
//								 log.info("Process "+isGefuProcess+" for Tracker Id = "+ l_gefuDtl.get("trackerId").toString());
//							}
//							
//							log.info("End of gefu creation time = "+ new Date());
//						}
//						
//					}
//					
//					
//					
//					
//					Calendar c = Calendar.getInstance();
//					c.setTime(nextScheduleDate);
//					while (nextScheduleDate.getTime() < sysDate.getTime())	// keep increasing till next schedule date is greater than current run time
//					{
//						c.add(Calendar.MINUTE, frequency);  // number of min to add
//						nextScheduleDate=c.getTime();
//					}
//					SimpleDateFormat sdf1=new SimpleDateFormat("HH:mm");
//
//					nextExecutionDate = sdf.format(nextScheduleDate);
//
//					executionTime=sdf1.format(nextScheduleDate);
//
//
//					sql="UPDATE SME_THREAD_REQUEST_DTL SET  NEXT_EXECUTION_DATE = ?, EXECUTION_TIME = ?, " +
//							"LAST_EXECUTION_DATE=?, NEXT_SCHEDULE_DATE = ?  WHERE REPORT_CODE = ?";
//					
//					pStatement=connection.prepareStatement(sql);
//					
//					pStatement.setString(1,nextExecutionDate );
//					pStatement.setString(2,executionTime );
//					pStatement.setTimestamp(3, new Timestamp(sysDate.getTime()) );
//					pStatement.setTimestamp(4, new Timestamp(nextScheduleDate.getTime()) );
//					pStatement.setString(5,purgeTable.split("-")[0]);
//
//					int x=pStatement.executeUpdate();
//
//					if(x>0){
//
//						log.info(x +" Records Updation Successfully for SME_THREAD_REQUEST_DTL for next execution");
//					}
//					else
//					{
//						log.info("Records Updation Failed");
//					}
//					pStatement.close();
//					status = false;
//				
//				}//PSGEFU_EMAIL_THREAD
//				else if(processFlag==1 && purgeTable.contains("PSGEFU_EMAIL_THREAD")) 
//				{
//					log.info("Go For PS Gefu Creation/Email Triggered");
//					
//					boolean isInIntevalTime = false;
//					
//					Calendar systemCal = Calendar.getInstance();
//				    String strTimeFormat = "HH:mm:ss";
//				    DateFormat timeFormat = new SimpleDateFormat(strTimeFormat);
//				    DateFormat simDatFor = new SimpleDateFormat("YYYYMMDD");
//					String systemDate = simDatFor.format(systemCal.getTime());
//					String baBankMastDate = "";
//					//SELECT_BA_BANK_MAST_DATE=select to_char(DAT_PROCESS,'YYYYMMDD') from BA_BANK_MAST;
//					pStatement=connection.prepareStatement(getQuery("SELECT_BA_BANK_MAST_DATE"));
//					resultSet = pStatement.executeQuery();
//					if(resultSet.next()) 
//					{
//						baBankMastDate = resultSet.getString(1);
//					}
//					resultSet.close();
//					pStatement.close();
//					if(systemDate.equals(baBankMastDate)) 
//					{
//						log.info("Both date are same");
//						try {
//
//						    String startTime = "19:05:00";
//						    log.info("startTime ="+startTime);
//						    Date startTimeObj = timeFormat.parse(startTime);
//						    Calendar calendar1 = Calendar.getInstance();
//						    log.info("calendar1= " +calendar1.getTime());
//						    calendar1.setTime(startTimeObj);
//
//						    String endTime = "19:30:00"; 
//						    log.info("endTime ="+endTime);
//						    Date endtTimeObj = timeFormat.parse(endTime);
//						    Calendar calendar2 = Calendar.getInstance();
//						    calendar2.setTime(endtTimeObj);
//						    calendar2.add(Calendar.DATE, 1);
//
//						    
//					        Calendar cal = Calendar.getInstance();
//					        Date date=cal.getTime();
//					       // DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//					        String someRandomTime=timeFormat.format(date);
//					        log.info("someRandomTime ="+someRandomTime);
//						    //String someRandomTime = "00:00:01";
//						    Date d = timeFormat.parse(someRandomTime);
//						    Calendar calendar3 = Calendar.getInstance();
//						    calendar3.setTime(d);
//						    
//						   
//
//						    log.info("calendar1= " +calendar1.getTime());
//						    log.info("calendar2= " +calendar2.getTime());
//						    log.info("calendar3= " +calendar3.getTime());
//						    Date x = calendar3.getTime();
//						    log.info("x "+x);
//						    if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
//						        //checkes whether the current time is between 14:49:00 and 20:11:13.
//						    	isInIntevalTime = true;
//						    }
//						} catch (ParseException e) {
//							log.info("ParseException = "+e);
//						}
//						
//						log.info("isInIntevalTime = "+isInIntevalTime);
//						
//						if(isInIntevalTime) 
//						{
//							boolean isPsGefuMake = false;
//							log.info("If it is in time interval = "+ new Date());
//							
//							Map<String,String> custMapList = new HashMap<String,String>();
//							//SELECT_CUSTOMER_ID_FOR_PS_GEFU=select CUSTOMER_ID from SME_ONLINE_OD_CUST_NAV_DTL where CUST_NAV_ID in (select OD_TRACK_ID from SME_ONLINE_OD_GEFU_DTL where IS_LIMIT_GEFU_EXIT = 'Y' and IS_PF_GEFU_EXIT = 'N' and IS_PS_GEFU_EXIT = 'N')
//							pStatement=connection.prepareStatement(getQuery("SELECT_CUSTOMER_ID_FOR_PS_GEFU"));
//							resultSet = pStatement.executeQuery();
//							while(resultSet.next()) 
//							{
//								custMapList.put(resultSet.getString(1),resultSet.getString(2));
//							}
//							resultSet.close();
//							pStatement.close();
//							
//							OnlineOdCustomerDao l_odObj = new OnlineOdCustomerDaoImpl();
//							isPsGefuMake = l_odObj.psSetUpGefuCreationAndMailTriggered(custMapList);
//							
//							log.info("End of PS gefu creation time = "+ new Date() + "  And isPsGefuMake=  "+isPsGefuMake);
//						}
//						
//					}
//					
//					Calendar c = Calendar.getInstance();
//					c.setTime(nextScheduleDate);
//					while (nextScheduleDate.getTime() < sysDate.getTime())	// keep increasing till next schedule date is greater than current run time
//					{
//						c.add(Calendar.MINUTE, frequency);  // number of min to add
//						nextScheduleDate=c.getTime();
//					}
//					log.info("nextScheduleDate : ==> "+nextScheduleDate);
//					SimpleDateFormat sdf1=new SimpleDateFormat("HH:mm");
//
//					nextExecutionDate = sdf.format(nextScheduleDate);
//
//					executionTime=sdf1.format(nextScheduleDate);
//
//					log.info("executionTime before updation : "+executionTime);
//
//					sql="UPDATE SME_THREAD_REQUEST_DTL SET  NEXT_EXECUTION_DATE = ?, EXECUTION_TIME = ?, " +
//							"LAST_EXECUTION_DATE=?, NEXT_SCHEDULE_DATE = ?  WHERE REPORT_CODE = ?";
//					
//					pStatement=connection.prepareStatement(sql);
//					
//					pStatement.setString(1,nextExecutionDate );
//					pStatement.setString(2,executionTime );
//					pStatement.setTimestamp(3, new Timestamp(sysDate.getTime()) );
//					pStatement.setTimestamp(4, new Timestamp(nextScheduleDate.getTime()) );
//					pStatement.setString(5,purgeTable.split("-")[0]);
//					log.info("Updating for Gefu Type : "+purgeTable.split("-")[0]);
//
//					int x=pStatement.executeUpdate();
//
//					if(x>0){
//
//						log.info(x +" Records Updation Successfully for SME_THREAD_REQUEST_DTL for next execution");
//					}
//					else
//					{
//						log.info("Records Updation Failed");
//					}
//					pStatement.close();
//					log.info("Next processing on "+nextExecutionDate+"/"+executionTime);
//					status = false;
//				
//				}
//				else
//				{
//					status = false;
//					//log.info(" Not Appropriate Time for download !!!!! ");
//				}
//			} catch(SQLException e) 
//			{
//				log.info("SQLException : ",e);
//			}
//			catch(Exception e) 
//			{
//				log.info("Exception : ",e);
//			}finally
//			{
//				//bw.close();
//			}
//		} 
//		catch(SQLException e)
//		{
//			log.info("SQLException : ",e);
//		}
//		catch(Exception e)
//		{
//			log.info("Exception : ",e);
//		}
//		finally
//		{
//
//			try {
//				if(resultSet!=null)
//				{
//					resultSet.close();
//				}
//				if(pStatement!=null)
//				{
//					pStatement.close();
//				}
//				if(connection!=null)
//				{
//					closeConnection(connection,new Object(){}.getClass().getEnclosingMethod().getName().toString());
//				}
//			} catch (SQLException e) {
//				log.info("SQLException : ",e);
//			}
//
//		}
//		return status;
//	}
	
	
	public synchronized void updateThreadStatus(String p_threadID, String p_Status, String p_Process, String p_WorkingOn, String p_trackerID)
	{
		log.info("Inside updateThreadStatus method TrackerID :" + p_trackerID + " p_Status >>> " + p_Status + " >>>> " + p_Process);
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try 
		{
		//	connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
			
			if(p_Process.equals("PURGING"))
			{
				preparedStatement = connection.prepareStatement("UPDATE SME_THREAD_DTL SET STATUS = ?, PROCESS = ?, WORKING_ON = ?, last_tracker_id = ?, last_update_time = sysdate, THREAD_STOP_TIME = sysdate WHERE THREAD_ID = ? ");
			}
			else if(p_trackerID == null || p_trackerID.trim().equalsIgnoreCase(""))
			{
				preparedStatement = connection.prepareStatement("UPDATE SME_THREAD_DTL SET STATUS = ?, PROCESS = ?, WORKING_ON = ?, last_tracker_id = ?, last_update_time = sysdate WHERE THREAD_ID = ? ");
			}
			else if(p_Process.equals(""))
			{
				preparedStatement = connection.prepareStatement("UPDATE SME_THREAD_DTL SET STATUS = ?, PROCESS = ?, WORKING_ON = ?, last_tracker_id = ?, last_update_time = sysdate	 WHERE THREAD_ID = ? ");
			}
			else if(p_Process.equals("DATA UPLOADING"))
			{
				preparedStatement = connection.prepareStatement("UPDATE SME_THREAD_DTL SET STATUS = ?, PROCESS = ?, WORKING_ON = ?, last_tracker_id = ?, last_update_time = sysdate	 WHERE THREAD_ID = ? ");
			}
			else
			{
				preparedStatement = connection.prepareStatement("UPDATE SME_THREAD_DTL SET STATUS = ?, PROCESS = ?, WORKING_ON = ?, last_tracker_id = ?, last_update_time = sysdate WHERE THREAD_ID = ? ");
			}
			
			preparedStatement.setString(1, p_Status);
			preparedStatement.setString(2, p_Process);
			preparedStatement.setString(3, p_WorkingOn);
			preparedStatement.setString(4, p_trackerID);
			preparedStatement.setString(5, p_threadID);
			
			
			int cnt = preparedStatement.executeUpdate();
			
			if(cnt == 0)
			{
				log.info("Error to Update the table ");
			}
		} 
		catch (Exception e) 
		{
			log.info("Exception ",e);
		}finally
		{
			try 
			{
				if(preparedStatement != null)
				{
					preparedStatement.close();
					preparedStatement = null;
				}
				if(connection != null)
				{
				}
			} 
			catch (Exception e2) 
			{
				log.info(e2);
			}
		}
	}
	public synchronized boolean insertThreadStatus(String p_ThreadName, String p_ThreadVersion, String p_Status, String p_threadID)
	{
		log.info("Inside updateImageUploadStatus method");
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		boolean status = false;
		try 
		{
			
			preparedStatement = connection.prepareStatement("UPDATE SME_THREAD_DTL SET THREAD_NAME = ?, VERSION = ?, STATUS = ?, PROCESS = '', WORKING_ON = '', THREAD_START_TIME = sysdate, thread_stop_time = null WHERE THREAD_ID = ?");
				
			preparedStatement.setString(1, p_ThreadName);
			preparedStatement.setString(2, p_ThreadVersion);
			preparedStatement.setString(3, p_Status);
			
			preparedStatement.setString(4, p_threadID);
			
			preparedStatement.executeUpdate();
			status = true;
		} 
		catch (Exception e) 
		{
			log.info("Exception ",e);
		}
		finally
		{
			try 
			{
				if(resultSet != null)
				{
					resultSet.close();
					resultSet = null;
				}
				if(preparedStatement != null)
				{
					preparedStatement.close();
					preparedStatement = null;
				}
				if(connection != null)
				{
					connection = null;
				}
			} 
			catch (Exception e2) 
			{
				log.info("Exception ",e2);
			}
		}
		
		return status;
	}
	
	
	@Override
	@Transactional
	public synchronized void updateThreadRequestTable(String threadName) {
	    log.info("In updateThreadRequestTable for " + threadName);
	    
	    try {
	        String sql = "UPDATE SME_THREAD_REQUEST_DTL SET NEXT_EXECUTION_DATE = CURRENT_DATE + INTERVAL '1' DAY, " +
	                     "LAST_EXECUTION_DATE = CURRENT_DATE, NEXT_SCHEDULE_DATE = CURRENT_DATE + INTERVAL '1' DAY " +
	                     "WHERE REPORT_CODE = :threadName";

	        Query query = entityManager.createNativeQuery(sql);
	        query.setParameter("threadName", threadName.trim());
	        
	        int rowsUpdated = query.executeUpdate();

	        if (rowsUpdated > 0) {
	            log.info(rowsUpdated + " Records Updated Successfully in SME_THREAD_REQUEST_DTL for next execution");
	        } else {
	            log.info("No Records Updated");
	        }

	    } catch (Exception e) {
	        log.error("Exception occurred during updateThreadRequestTable for threadName: " + threadName, e);
	    }
	}

	
	
}
