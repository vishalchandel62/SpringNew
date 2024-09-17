package com.credentek.msme.ApplicationGenParm;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Logger;

//import com.credentek.msme.utils.DAOBase;

import org.apache.logging.log4j.LogManager;



//extends DAOBase
public class JavaScheduledExecutorServiceExample {

	
	public static final Logger log = LogManager.getLogger("scheduledexecutorservice.JavaScheduledExecutorServiceExample");
	
	/*private static Runnable GEFU_PROCESS_ACTIVITY;
    private static Runnable MAIL_PROCESS_ACTIVITY;
    private static Runnable PURGE_PROCESS_ACTIVITY;*/
	static int i = 0;
	public static ScheduledExecutorService execService;
	private List<String> allThreadNameIpList;
	
	/*public static void main(String[] args) 
	{
		main123();
	}*/

	public JavaScheduledExecutorServiceExample(List<String> allThreadNameIpList) 
	{
		log.info("In JavaScheduledExecutorServiceExample constructor");
		this.allThreadNameIpList = allThreadNameIpList;
	}
	
	
	
	public void setScheduledExecutorThread() {
		
		
		log.info("In setScheduledExecutorThread function");
		
		 try {
                           execService = Executors.newSingleThreadScheduledExecutor();
		 
		 Iterator<String> itr = allThreadNameIpList.iterator();//[T4~GEFU_CREATION_THREAD-T4]
		 while(itr.hasNext()) 
		 {
			 
			 String[] serviceIpThread = itr.next().split("~");
			 
			 log.info("serverNameTimeFreqList ="+ApplicationGenParameter.serverNameTimeFreqList);
			 log.info("serviceIpThread ="+serviceIpThread[0]+serviceIpThread[1]);
			 //T4~PARAMETER_REFRESH_THREAD-T4
			 
			 if("PARAMETER_REFRESH_THREAD".equals(serviceIpThread[1].split("-")[0])) 
			 {
				 log.info("In PARAMETER_REFRESH_THREAD");
				 execService.scheduleAtFixedRate(new BackgroundJobManager(serviceIpThread[0], serviceIpThread[1].split("-")[0], 10000), calculateMinutesForStart(serviceIpThread[1].split("-")[0].trim()), Long.parseLong(ApplicationGenParameter.serverNameTimeFreqList.get((serviceIpThread[1].split("-")[0].trim())).split("~")[1]), TimeUnit.MINUTES);
			 }
//			 else if("NON_RNB_CUST_PUSH_THREAD".equals(serviceIpThread[1].split("-")[0]))
//			 {
//				 log.info("In NON_RNB_CUST_PUSH_THREAD");
//				 execService.scheduleAtFixedRate(new NonRnbCustPushThread(serviceIpThread[0], serviceIpThread[1].split("-")[0], 10000), calculateMinutesForStart(serviceIpThread[1].split("-")[0].trim()), Long.parseLong(ApplicationGenParameter.serverNameTimeFreqList.get((serviceIpThread[1].split("-")[0].trim())).split("~")[1]), TimeUnit.MINUTES);
//			 }
//			 else if("BENE_PUSH_THREAD".equals(serviceIpThread[1].split("-")[0]))
//			 {
//				 log.info("In BENE_PUSH_THREAD");
//				 execService.scheduleAtFixedRate(new BenePushThread(serviceIpThread[0], serviceIpThread[1].split("-")[0], 10000), calculateMinutesForStart(serviceIpThread[1].split("-")[0].trim()), Long.parseLong(ApplicationGenParameter.serverNameTimeFreqList.get((serviceIpThread[1].split("-")[0].trim())).split("~")[1]), TimeUnit.MINUTES);
//			 }
		 }
		
        log.info("-----------------------");
        // wait until all tasks are finished
			//execService.awaitTermination(1, TimeUnit.SECONDS);
        log.info("All tasks are finished!");
		
		 } catch (RejectedExecutionException r)
				{
			 log.info("-----------------------",r);
		}
				 catch (NullPointerException r)
				 {
			 log.info("-----------------------",r);
		}
				 catch (IllegalArgumentException r)
				 {
			 log.info("-----------------------",r);
		}
	}
	
	public long calculateMinutesForStart(String threadName) 
	{
		log.info("In calculateMinutesForStart function "+threadName);
		long calculateMinutes = 0;
		Calendar caldate = Calendar.getInstance();
		Calendar reCaldat = Calendar.getInstance();
		reCaldat.set(caldate.get(Calendar.YEAR), caldate.get(Calendar.MONTH), caldate.get(Calendar.DATE), Integer.parseInt(ApplicationGenParameter.serverNameTimeFreqList.get(threadName).split("~")[0].split(":")[0]), Integer.parseInt(ApplicationGenParameter.serverNameTimeFreqList.get(threadName).split("~")[0].split(":")[1]));
		log.info("caldate "+caldate.getTime());
		log.info("reCaldat "+reCaldat.getTime());
		
				long diffMs =  reCaldat.getTimeInMillis() - caldate.getTimeInMillis() ;
				long diffSec = diffMs / 1000;
				long min = diffSec / 60;
				//long sec = diffSec % 60;
				//double hour = diffSec / 3600.00;
				//long hour1 = diffSec % 3600;
				log.info("The difference is "+min+" minutes.");
				calculateMinutes = min;
				if(min < 0) 
				{
					log.info("calculateMinutes less than zero");
					
					Calendar reCaldat1 = Calendar.getInstance();
					reCaldat1.set(caldate.get(Calendar.YEAR), caldate.get(Calendar.MONTH), caldate.get(Calendar.DATE), Integer.parseInt("23"), Integer.parseInt("59"));
					log.info("reCaldat1 "+reCaldat1.getTime());
					long diffMs1 =  reCaldat1.getTimeInMillis() - caldate.getTimeInMillis() ;
					long diffSec1 = diffMs1 / 1000;
					long min1 = diffSec1 / 60;
					log.info("start from1 = "+min1);
					
					Calendar reCaldat2 = Calendar.getInstance();
					reCaldat2.set(caldate.get(Calendar.YEAR), caldate.get(Calendar.MONTH), caldate.get(Calendar.DATE), Integer.parseInt("00"), Integer.parseInt("00"));
					
					/*Calendar reCaldat3 = Calendar.getInstance();
					reCaldat3.set(caldate.get(Calendar.YEAR), caldate.get(Calendar.MONTH), caldate.get(Calendar.DATE), Integer.parseInt("11"), Integer.parseInt("30"));*/
					
					long diffMs2 =  reCaldat.getTimeInMillis() - reCaldat2.getTimeInMillis() ;
					long diffSec2 = diffMs2 / 1000;
					long min2 = diffSec2 / 60;
					log.info("start from2 = "+min2);
					calculateMinutes = min2 + min1;
					log.info("The difference is "+calculateMinutes+" minutes.");
				}
		
				log.info("Final difference is "+calculateMinutes+" minutes.");
				
				
		return calculateMinutes;
	}
	
	
}
