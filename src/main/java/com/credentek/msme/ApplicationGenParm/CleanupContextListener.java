package com.credentek.msme.ApplicationGenParm;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletContextEvent;




@Component
public class CleanupContextListener implements ApplicationListener<ApplicationEvent>{
	
	
	public static final Logger log = LogManager.getLogger("scheduledexecutorservice.CleanupContextListener");
	
	public static boolean run = false;
	List<String> threadList;
	@SuppressWarnings({ "deprecation"+ "unused" })
	
	
	@Autowired
	EntityManager entityManager;
	
	public void contextDestroyed(ServletContextEvent arg0) 
	{
		try {
		//run = true;
		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		log.info("threadSet :" + threadSet);
		Iterator<Thread> iterator = threadSet.iterator();
		for (int i = 0; iterator.hasNext(); i++)
		{
			Thread t = iterator.next();
			/*Iterator<String> itr = threadList.iterator();
			while(itr.hasNext())
			{
				String value = itr.next();
				if(t.getName().contains(value.split("~")[1].trim()))	// Should be current
				{
					System.out.println("::::::::Stopping:::::::::: Thread Name :"+ t.getName() +" :: State :" +t.getState());
					new PurgingActivityDaoImpl().updateThreadStatus(t.getName().split("-")[1], "STOP"+ "CONTEXT DESTROYED"+ ""+ "");
					t.stop();
				}
			}*/
		}
		log.info("Context Destroyed");
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.info("Exception", e);
		}
		
	}

	public void contextInitialized() 
	{
		
		log.info("Context Initialized");
		
		//log.info(entityManager);
		
		//JavaScheduledExecutorServiceExample.main123();
		
		threadList = new ArrayList<String>();
		InetAddress ip;
		try {
			
			new ApplicationGenParameter(entityManager);
			ip = InetAddress.getLocalHost();
			log.info("Current IP address : ");

			String runServerIp = "" + ip.getHostAddress();
			log.info("runServerIp :" + runServerIp);
			log.info("ActiveServer :" + ApplicationGenParameter.activeServerList);
			for (Map.Entry<String, String> entryMaplist : ApplicationGenParameter.activeServerList.entrySet()) {
				if (entryMaplist.getValue().equals(runServerIp)) {
					log.info("entryMaplist key :" + entryMaplist.getKey());
					threadList.add(entryMaplist.getKey());
				}
			}

			if (threadList.size() == 0) {
				log.info("No thread allocated to this server.");
			} else {
				JavaScheduledExecutorServiceExample scheduleObj = new JavaScheduledExecutorServiceExample(threadList);
				scheduleObj.setScheduledExecutorThread();
			}
			
		} catch (UnknownHostException e) {
			log.info("UnknownHostException"+ e);

		} catch (Exception e1) {
			log.info("Exception"+ e1);
		}

	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		contextInitialized();
		
	}



}

