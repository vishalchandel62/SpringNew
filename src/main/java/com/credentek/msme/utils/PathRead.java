package com.credentek.msme.utils;

import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PathRead {
	

	//private static final Log log = LogFactory.getLog(PathRead.class);
	private static final Logger log = LogManager.getLogger(PathRead.class);
	private static PathRead pathReadObj = null;
	ResourceBundle resourceBundleCommon = null;
	ResourceBundle resourceBundleMessage = null;
	public static ResourceBundle resourceBundleEnvi = null;
	
	PathRead()
	{
		resourceBundleEnvi = ResourceBundle.getBundle("EnvironmentFlag");
		String environmentFlag = resourceBundleEnvi.getString("ENVIRONMENT");
		log.info("In environmentFlag of PathRead....."+environmentFlag);
	    /*File file = new File("/weblogic/Oracle/Middleware/user_projects/domains/SMEAPPUAT/servers/work/log/environmentPathFile.txt"); 
	    List<String> environmentVariable = new ArrayList<String>(); 
	    Scanner sc = null;
		try {
			sc = new Scanner(file);
		    while (sc.hasNextLine()) 
		    {
		    	environmentVariable.add(sc.nextLine()); 
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			log.info("File not found on location.....");
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			log.info("File is empty on location.....");
		} 
	    
		environmentFlag=environmentVariable.get(0);
		String environmentPath=environmentVariable.get(1);
	    

*/
		if("LOCAL".equalsIgnoreCase(environmentFlag)) 
		{
		resourceBundleCommon = ResourceBundle.getBundle("CommonPathLocal");
		}
		else if("LOCAL_WEBLOGIC".equalsIgnoreCase(environmentFlag)) 
		{
		resourceBundleCommon = ResourceBundle.getBundle("CommonPathLocalWebLogic");
		} 
		else if("UAT".equalsIgnoreCase(environmentFlag)) 
		{
		resourceBundleCommon = ResourceBundle.getBundle("CommonPathUAT");
		}
		else if("OBDX".equalsIgnoreCase(environmentFlag)) 
		{
		resourceBundleCommon = ResourceBundle.getBundle("CommonPathOBDX");
		}
		else if("YESUAT".equalsIgnoreCase(environmentFlag)) 
		{
		resourceBundleCommon = ResourceBundle.getBundle("CommonPathYESUAT");
		}
		else if("LIVE".equalsIgnoreCase(environmentFlag)) 
		{
		resourceBundleCommon = ResourceBundle.getBundle("CommonPathLIVE");
		}


		resourceBundleMessage = ResourceBundle.getBundle("messages");
		
		/*
		 //By Written By Neha
		 Properties props = new Properties();
		InputStream config = getClass().getResourceAsStream("/log4j.properties");
		try {
			props.load(config);
			config.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		props.setProperty("log4j.appender.sme.File",environmentPath);
	    LogManager.resetConfiguration();
	    PropertyConfigurator.configure(props);
	    */
		
	    /*
	     //By Written By Ashish
	     
	    SimpleLayout Layout = new SimpleLayout();
	    RollingFileAppender appender;
	    
	    try {
			appender=new RollingFileAppender(Layout, environmentPath, true);
			Logger.getRootLogger().addAppender(appender);
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    		
	    */
	   // log.info("NEW Logger");
	    
	    
	}
	
	public static PathRead getInstance()
	{
		if(pathReadObj==null)
		{
			pathReadObj = new PathRead();
		}
		return pathReadObj;
	}
	
	public String getPath(String queryID) {
		//log.info("In getPath() of PathRead");
		String queryString = resourceBundleCommon.getString(queryID);
		return queryString;
	}
	
	public String getMsg(String queryID) {
		//log.info("In getMsg() of PathRead");
		String queryString = resourceBundleMessage.getString(queryID);
		return queryString;
	}

	/*public String getProcessorData(String queryID) {
		log.info("In getProcessorData() of PathRead");
		String queryString = resourceBundleProcessor.getString(queryID);
		return queryString;
	}*/

}
