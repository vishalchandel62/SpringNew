package com.credentek.msme.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.sf.json.JSONException;

public class DaoBase implements Dao{
	

	//private static final Log log = LogFactory.getLog(DAOBase.class);
	private static final Logger log = LogManager.getLogger(DaoBase.class);
	Connection connection = null;
	Connection connection1 = null;
	Connection beneMsgConnection = null;
	static int count=0;
	static String names="";
	static String names1="";
	static int connectionCount=0;
	
	public Connection getConnection(String methodName){
		
		DataSource dataSource = DataSourceBase.getDataSource();
		
		//Connection connection = null;
		if (dataSource != null) {
			try {
				//log.info("Go for Get Connection.....");
				connection = dataSource.getConnection();
				connectionCount++;
				//log.info("After Getting Connection ......"+connectionCount+" "+methodName);
			} catch (SQLException e) {
				//log.info(e);
			}
		}
		return connection;
	}
	
	public Connection getConnectionYesAmazon(String methodName) {

		DataSource dataSource1 = DataSourceBase.getYesAmazonDataSource();
		
		//Connection connection = null;
		if (dataSource1 != null) {
			try {
				connection1 = dataSource1.getConnection();
				connectionCount++;
				//log.info("After Getting YesAmazon Connection ......"+connectionCount+" "+methodName);
			} catch (SQLException e) {
				//log.info(e);
			}
		}
		return connection1;
	}
	
	public Connection getConnectionBeneMsgSend(String methodName) {

		DataSource beneMsgSendDataSource = DataSourceBase.getBeneMsgSendDataSource();
		
		//Connection connection = null;
		if (beneMsgSendDataSource != null) {
			try {
				beneMsgConnection = beneMsgSendDataSource.getConnection();
				connectionCount++;
				log.info("After Getting BeneMsgSend Connection ......"+connectionCount+" "+methodName);
			} catch (SQLException e) {
				log.info(e);
			}
		}
		return beneMsgConnection;
	}
	
	public void closeConnection(Connection con,String methodName){
		//log.info("In closeConnection function");
		try {
			
		} catch (JSONException e) {
			//log.info("error="+e);
		}
		
	}
	
	
	public String getQuery(String queryID) {

		ResourceBundle resourceBundle = ResourceBundle.getBundle("SQLRepository");
		String queryString = resourceBundle.getString(queryID);

	//	log.info("Query string for ID " + queryID + " = ");

		return queryString;
	}

	protected void finalize() throws Throwable {

		try {
			if (connection != null) 
			{
			 	closeConnection(connection,new Object(){}.getClass().getEnclosingMethod().getName().toString());
				//connection = null;
			}if (connection1 != null) {
				connection1.close();
				connection1 = null;
			}
		} catch (Exception ex) 
		{
			//log.info("ex ="+ex);
		} 
		finally {
			super.finalize();
		}
	}

}
