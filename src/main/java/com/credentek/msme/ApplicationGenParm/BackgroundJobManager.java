package com.credentek.msme.ApplicationGenParm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;



public class BackgroundJobManager implements Runnable {

	public static final Logger log = LogManager.getLogger(BackgroundJobManager.class);
	
	Thread thread;
	private String threadId;
	private String threadName;
	private long newtimeSleep;
	//PurgingActivityDaoImpl scheduler = null;
	static final String g_threadVersion = "1.0.0";
	String g_threadID = "";
	static int i = 0;
	ApplicationGenParameter applicationObj = null;
	
	
	BackgroundJobManager(String threadId, String threadName, long newtimeSleep)
	{
		log.info("BackgroundJobManager call ="+threadId + threadName);
		
		
		try
		{
			this.threadId = threadId;
			this.threadName = threadName;
			this.newtimeSleep = newtimeSleep;	
		}
		catch(Exception e)
		{
			log.info("Error in starting thread : "+e);
		}
		catch(Throwable t) 
		{
			log.info("Throwable : "+t);
		}
	}
	
	@Override
	public void run() {

		
		boolean threadStartCheck = true;
		try 
		{	
			log.info("BackgroundJobManager Thread run method :: ");
				try
				{
					applicationObj = new ApplicationGenParameter();
				//	log.info("BackgroundJobManager Thread ID :: " + threadId);
					PurgingActivityDaoImpl	scheduler = new PurgingActivityDaoImpl();
					threadStartCheck = scheduler.insertThreadStatus(threadName, g_threadVersion, "RUNNABLE" , g_threadID);
					log.info("threadStartCheck ::" + threadStartCheck);
					applicationObj.prepareParameterList();
					applicationObj.ServiceGenerater();
						// Go for Update thread detail table
						log.info("Go for Update thread detail table ");
						scheduler.updateThreadRequestTable(threadName);
					Thread.sleep(newtimeSleep);
					
				
				}catch (Exception e) {
					log.info(" BackgroundJobManager Exception : "+e);
				}
		} 
		catch (Exception e) {
			log.info(" BackgroundJobManager Exception : "+e);
		}
		catch(Throwable t) 
		{
			log.info("Throwable : "+t);
		}
	}
	@Override
	protected void finalize()  
	{
		log.info("BackgroundJobManager Thread finalize call :: ");
	}

} 
