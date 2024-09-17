package com.credentek.msme.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Formatter;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.credentek.msme.ApplicationGenParm.ApplicationGenParameter;
import com.credentek.msme.api.dao.ServiceCallAPI;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;


public class Utils {

	

	static final Logger logger = LogManager.getLogger(Utils.class);

	//static final String baseURL = "https://<serverip>:<port>/corp/BANKAWAY?Action.SMS.Init.001=Y&Action.Init.ChannelId=S&smsAccessChannelId=S&smsBankId=ICI&smsRequestFrom=CSAM&"; 
	
	public static String[] parseString(String inString, String token)
	{
		String[] outString = {};

		outString = inString.split(token);

		return outString; 
	}
	/**
	 * Added by Vinod Rai 
	 * @param urlString
	 * @param param
	 * @return response string
	 */

	
	private static final String SCRIPT_KEYWORD1 = "<SCRIPT";
	private static final String SCRIPT_KEYWORD2 = "< SCRIPT";
	private static final String SCRIPT_KEYWORD3 = "%3CSCRIPT";
	private static final String SCRIPT_KEYWORD4 = "%3C%20SCRIPT";

	private static final String LINK_KEYWORD1 = "<A";
	private static final String LINK_KEYWORD2 = "< A";
	private static final String LINK_KEYWORD3 = "%3CA";
	private static final String LINK_KEYWORD4 = "%3C%20A";

	private static final String IMG_KEYWORD1 = "%3CIMG";
	private static final String IMG_KEYWORD2 = "%3C%20IMG";
	private static final String IMG_KEYWORD3 = "<IMG";
	private static final String IMG_KEYWORD4 = "< IMG";
	private static final String IMG_KEYWORD5 = "<IMG%20SRC%";
	private static final String IMG_KEYWORD6 = "%20SRC%";

	private static final String FRAME_KEYWORD1 = "<frame";
	private static final String FRAME_KEYWORD2 = "< frame";
	private static final String FRAME_KEYWORD3 = "%3Cframe";
	private static final String FRAME_KEYWORD4 = "%3C%20frame";

	private static final String INPUT_KEYWORD1 = "<input";
	private static final String INPUT_KEYWORD2 = "< input";
	
	public static  String CLIENT_ID = "";
	public static String CLIENT_PWD = "";
	

	public static String getDataFromIpsService(String urlString, String param)
	{
		String url = urlString + param;
		logger.info("URL : " + url.toString());

		StringBuffer response = new StringBuffer(500);		

		try {				
			URL urlObj = new URL(url.toString());

			//java.net.URL url = new URL(null,url1 ,new sun.net.www.protocol.http.Handler() );
			HttpURLConnection httpUrlCon = (HttpURLConnection) urlObj.openConnection();

			logger.info("Http URL connection is created ");

			httpUrlCon.setConnectTimeout(600000); // 600000 milli second 
			httpUrlCon.setReadTimeout(600000);
			logger.info("set connection timeout");
			BufferedReader inputStreamBuffer = new BufferedReader(new InputStreamReader(httpUrlCon.getInputStream()));

			logger.info("get input stream from http connection URL ");

			String inputLine = null;

			while ((inputLine = inputStreamBuffer.readLine()) != null) {
				response.append(inputLine);
			}
			inputStreamBuffer.close();								

			httpUrlCon.disconnect();

		} catch (MalformedURLException e) {					
			logger.error(e);
		} catch (IOException e) {					
			logger.error(e);
		} catch (Exception e){
			logger.error(e);
		}

		String data = response.toString();

		if(data.contains("&"))
		{
			logger.info("replace all &");
			data = data.replaceAll("&", "&amp;");
		}
		logger.info("response read successfuly...");

		//logger.info("------------------\nResponse from IPS Service : " + data + "\n-------------------");
		return data.trim();

	}
	public static Object getDataFromTolService(String urlString, JSONObject jsonParam)
	{		
		logger.info("URL : " );
		//logger.info("params : " + jsonParam.toString());

		StringBuffer resString = new StringBuffer(500);		

//		try 
//		{
//			Client client = Client.create();
//			WebResource webResource = client.resource(urlString);
//
//			ClientResponse response = (ClientResponse) webResource.type("application/json").post(ClientResponse.class, jsonParam.toString());
//
//			if (response.getStatus() != 200) {
//				logger.info("Failed : HTTP error code : "+ response.getStatus());	
//				throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
//			}
//			resString.append((String) response.getEntity(String.class));
//
//		} catch (Exception e) {
//			logger.info(e.toString());		 
//		}			

		logger.info("response: ");
		Object jsonRes = null;

//		try 
//		{
//			jsonRes = (Object) JSONSerializer.toJSON( resString.toString() );
//
//		} catch (JSONException e){
//			logger.error(e);
//		}catch (Exception e) {
//			logger.error(e);
//		}	

		return jsonRes;		
	} 
	public static String getDataFromService(String urlString, String param)
	{
		StringBuffer buffer = new StringBuffer();

		try 
		{

			//URL url = new URL(urlString);
			//URL url = new URL(urlString);					//Commented and Modified by Mahesh on 01-AUG-2013 for Class Cast Exception
//			java.net.URL url = new URL(null, urlString , new sun.net.www.protocol.https.Handler());			//Commented and Modified by Mahesh on 01-AUG-2013 for Class Cast Exception

			java.net.URL url = new URL(urlString);
			
			logger.info("URL : " + urlString);
			logger.info("Params : " + param.toString());

			//HttpsURLConnection  urlConnection  = (HttpsURLConnection) url.openConnection();

			HttpsURLConnection  urlConnection  = (HttpsURLConnection) url.openConnection();

			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setInstanceFollowRedirects(false);			
			urlConnection.setRequestMethod("POST");
			urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			urlConnection.setRequestProperty("charset", "utf-8");
			//urlConnection.setRequestProperty("Content-Length", "" + Integer.toString(urlParam.getBytes().length));
			urlConnection.setUseCaches(false);

			//System.setProperty("javax.net.ssl.trustStore", "/WLApp/CMBWL/CMB/data/key.jks"); 
			urlConnection.setHostnameVerifier(new HostVerifier());

			DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());

			wr.writeBytes(param);
			wr.flush();


			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

			String out;

			while((out = bufferedReader.readLine()) != null)
			{
				buffer.append(out);
				//System.out.println(out);
			}

			wr.close();
			bufferedReader.close();

			urlConnection.disconnect();			

		}
		catch(Exception e) 
		{
			logger.error(e);
			//e.printStackTrace();
		}

		String data = buffer.toString();

		/*if(data.contains("&"))
		{
			data = data.replaceAll("&", "&amp;");
		}*/

		logger.info("Data recessive from service : ");
		return data;

	}

	public static final Date toDate(String dateStr, String inputFormat)
	{
		Date outDate = null;

		try
		{
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(inputFormat);

			if(!dateStr.equals("") && dateStr != null)
			{
				outDate = simpleDateFormat.parse(dateStr);
			}
		}
		catch (Exception e) 
		{
			logger.error(e);
		}

		return outDate;
	}

	public static String stringToHex(byte bytes[])
	{ 
		String a= "a";

		StringBuilder sb = new StringBuilder(a);
		Formatter formatter = new Formatter(sb);  

		for (byte b : bytes) 
		{  
			formatter.format("%08X", b);  
		}

		return sb.toString();
	}

	public static KeyPair getKeyPair(String alias, String password, KeyStore keyStore)
	{
		KeyPair kp = null;

		try
		{
			Key key = keyStore.getKey(alias, password.toCharArray());

			if (key instanceof PrivateKey) 
			{

				// Get certificate of public key
				Certificate cert = keyStore.getCertificate(alias);


				// Get public key
				PublicKey publicKey = cert.getPublicKey();


				// Return a key pair
				kp = new KeyPair(publicKey, (PrivateKey) key);

			}
		}
		catch (Exception e) 
		{
			logger.error(e);

		}

		return kp;

	}

	public static byte[] encrypt(byte[] dataBytes, SecretKey key, String algorithm)
	{
		byte[] a = {};
		try
		{

			//Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
			//Cipher cipher = Cipher.getInstance(algorithm, "BC");      //commented for IBM java-------------2121----comment provider to allow algo search in all areas
			Cipher cipher = Cipher.getInstance(algorithm);

			// SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);

			return cipher.doFinal(dataBytes);
		}
		catch (Exception e) 
		{
			logger.error(e);			
		}

		return a ;
	}

	

	public static byte[] encryptKey(byte[] text, PublicKey key, String algorithm) 
	{
		byte[] cipherText = null;

		try 
		{
			// get an RSA cipher object and print the provider
			final Cipher cipher = Cipher.getInstance(algorithm);
			// encrypt the plain text using the public key
			cipher.init(Cipher.ENCRYPT_MODE, key);
			cipherText = cipher.doFinal(text);
		}
		catch (Exception e) 
		{
			logger.error(e);		    	 	
		}
		return cipherText;
	}
	
	/** 
     * Added by Ashitosh Rajguru
     * Returns response from the Https api
     * @param urlString, body, Authorization
     * @return jsonObj
     */
	public static String DataFromService(String urlString, String body,String Authorization)
	{
		
		// PAN CARD MASKING FOR APPSEC POINT
		// Implementation of PAN Number Masking in the Application Logs
		
 		net.sf.json.JSONObject duplicateRequestBodyInfo = new net.sf.json.JSONObject();
		
 		duplicateRequestBodyInfo.put("requestBody",body);
		//	logger.info("userAuthentication 1" + duplicateRequestBodyInfo);
 		try {
 			
 			if(duplicateRequestBodyInfo.getJSONObject("requestBody").has("CreateFDAccReq") && duplicateRequestBodyInfo.getJSONObject("requestBody").getJSONObject("CreateFDAccReq").has("ReqBody"))
 			{			
 				if(duplicateRequestBodyInfo.getJSONObject("requestBody").getJSONObject("CreateFDAccReq").getJSONObject("ReqBody").has("PAN"))
 				{
 					//log.info("userAuthentication 1" + duplicatecustInfo);
 					// to mask the last digit of pan card.
 					duplicateRequestBodyInfo.getJSONObject("requestBody").getJSONObject("CreateFDAccReq").getJSONObject("ReqBody").replace("PAN",duplicateRequestBodyInfo.getJSONObject("requestBody").getJSONObject("CreateFDAccReq").getJSONObject("ReqBody").getString("PAN").replaceAll("\\w(?=\\w{4})", "*"));
			
 					// to remove pan card from the log 
 					logger.info("userAuthentication " + duplicateRequestBodyInfo);
			
 					//duplicatecustInfo.getJSONObject("custInfo").remove("custPanCardNo");
 				}
 			}
		}catch(Exception e)
		{
			logger.error("Exception in PAN card masking"+e);
		}
		
		
		logger.info("Start of DataFromService inside Utils: urlString....Url.........."+urlString+".....Request......"+duplicateRequestBodyInfo+"......Authorization....."+Authorization);
		
		
		
		JSONObject jsonObj = new JSONObject();
		try {
			logger.info("ServiceCallAPI.serverTypeApi: "+ServiceCallAPI.serverTypeApi);
			logger.info("URL: ");
			
			java.net.URL url = new URL(urlString);		
			logger.info("URL HOST: ");   
			HttpsURLConnection  connection  = (HttpsURLConnection) url.openConnection();
			connection.setRequestProperty("Content-Type", "application/json");
			
			    
			//if("YESUAT".equals(ServiceCallAPI.serverTypeApi)) {					
			if("OBDX".equals(ServiceCallAPI.serverTypeApi)) {
				 Base64.Decoder decoder = Base64.getDecoder();  
				 
				 CLIENT_ID = new String(decoder.decode(ApplicationGenParameter.parameterMasterList.get("CLIENT_HEADER_ID")));
				 CLIENT_PWD = new String(decoder.decode(ApplicationGenParameter.parameterMasterList.get("CLIENT_HEADER_PWD")));
				
				connection.setRequestProperty("X-IBM-Client-Id",CLIENT_ID);
				connection.setRequestProperty("X-IBM-Client-Secret",CLIENT_PWD);
			}
			
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			if(Authorization !=null)
			{
				connection.setRequestProperty("Authorization", Authorization);
				logger.info("Authorization inside DataFromService: ");
			}

			connection.setHostnameVerifier(new HostVerifier()); 	

			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

			wr.writeBytes(body);
			
			wr.flush();
			wr.close();
			
			//added by 2299 to set connection and read timeout at api level on 22032021
			int timeoutConnection = 120000;  //2 mins
			connection.setConnectTimeout(timeoutConnection);
			connection.setReadTimeout(timeoutConnection);
			logger.info("connection timeout: "+connection.getConnectTimeout()); 
			
			if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
				logger.info("Http response is OK.");
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuffer res = new StringBuffer(); 
				String line;
				while ((line = reader.readLine()) != null) {
					res.append(line);
				}
				reader.close();


				jsonObj.put("result", res.toString());
				logger.info("Response inside DataFromService: ");

			} else {
				// Server returned HTTP error code.
				int code=connection.getResponseCode();
				String status=String.valueOf(code);
				logger.info("Response Code: "+status);
				/*JSONObject statusObj = new JSONObject();
				statusObj.put("ErrorInfo", status);
				*/
				jsonObj.put("ResCode", "1");
				jsonObj.put("ErrorDetails", "Sorry,something went wrong : Please try after some time.");
				//jsonObj.put("Error",statusObj);
				return jsonObj.toString();
			}
		} catch (Exception e) {


			logger.info("Exception: "+e.getMessage());

			logger.error("Exception :::",e);
			/*JSONObject statusObj = new JSONObject();
			statusObj.put("ErrorInfo", e.getClass()+" "+e.getMessage());
			jsonObj.put("Error",statusObj);*/
			jsonObj.put("ResCode", "1");
			jsonObj.put("ErrorDetails", e.getClass()+" "+e.getMessage());
			logger.info("Error Object: "+jsonObj);
			return jsonObj.toString();
		}


		String response=jsonObj.toString();
		response=response.replace("\\/","\\\\/");
		logger.info("Response: "+response);
		return response;
	}
	
	public static String DataFromServiceFrm(String urlString,String eventId, String body)
	{
		logger.info("Start of DataFromServiceFrm inside Utils: urlString....Url.........."+urlString+".....Request......"+body);
		
		JSONObject jsonObj = new JSONObject();
		try {
			logger.info("URL: "+urlString);
			
			java.net.URL url = new URL(urlString);		
			logger.info("URL HOST: ");   
			
			
			HttpsURLConnection  connection  = (HttpsURLConnection) url.openConnection();
		//	Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ApplicationGenParameter.UPIPRXIP, new Integer(ApplicationGenParameter.UPIPRXPRT)));
		//	HttpsURLConnection  connection  = (HttpsURLConnection) url.openConnection(proxy);
			
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");

			connection.setRequestProperty("eventtype", "nft");
			connection.setRequestProperty("eventsubtype", "iblogin");
			connection.setRequestProperty("event-name","nft_iblogin");
			connection.setRequestProperty("event_id",eventId);//use a method here
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setHostnameVerifier(new HostVerifier()); 	

			logger.info("getRequestProperties: "+connection.getRequestProperties());
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

			wr.writeBytes(body);
			wr.flush();
			wr.close();
			
			int timeoutConnection = 120000;  //2 mins
			connection.setConnectTimeout(timeoutConnection);
			connection.setReadTimeout(timeoutConnection);
			logger.info("connection timeout: "+connection.getConnectTimeout()); 
			
			if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
				logger.info("Http response is OK.");
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuffer res = new StringBuffer(); 
				String line;
				while ((line = reader.readLine()) != null) {
					res.append(line);
				}
				reader.close();

				jsonObj.put("result", res.toString());
				logger.info("Response inside DataFromService: ");

			} else {
				int code=connection.getResponseCode();
				String status=String.valueOf(code);
				logger.info("Response Code: "+status);
				jsonObj.put("ResCode", "1");
				jsonObj.put("ErrorDetails", "Sorry,something went wrong : Please try after some time.");
				return jsonObj.toString();
			}
		} catch (Exception e) {

			logger.info("Exception: "+e.getMessage());
			jsonObj.put("ResCode", "1");
			jsonObj.put("ErrorDetails", e.getClass()+" "+e.getMessage());
			logger.info("Error Object: "+jsonObj);
			return jsonObj.toString();
		}

		String response=jsonObj.toString();
		response=response.replace("\\/","\\\\/");
		logger.info("Response: "+response);
		return response;
	}
	
	
	public static String DataFromServiceHttp(String urlString, String body,String Authorization)
	{
		logger.info("Inside DataFromServiceHttp: "+urlString+" body"+body);

		JSONObject jsonObj = new JSONObject();
		try {

			logger.info("URL: ");
			URL url = new URL(urlString);		
			HttpURLConnection  connection  = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(120000); // Request for 2 Minutes
			if(Authorization !=null)
			{
				connection.setRequestProperty("Authorization", Authorization);
				logger.info("Authorization: "+Authorization);
			}
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(body);
			logger.info("Request body: "+body);
			wr.flush();
			wr.close();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				logger.info("Http response is ok");
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuffer res = new StringBuffer(); 
				String line;
				while ((line = reader.readLine()) != null) {
					res.append(line);
				}
				reader.close();
				jsonObj.put("result", res.toString());
				logger.info("Response: "+res.toString());

			} else {
				int code=connection.getResponseCode();
				String status=String.valueOf(code);
				logger.info("Response code: "+status);
				JSONObject statusObj = new JSONObject();
				statusObj.put("ErrorInfo", status);
				jsonObj.put("Error",statusObj);
				return jsonObj.toString();
			}

		} catch (Exception e) {
			logger.error("Exception: "+e);
			JSONObject statusObj = new JSONObject();
			statusObj.put("ErrorInfo", e.getClass()+" "+e.getMessage());
			jsonObj.put("Error",statusObj);
			String response=jsonObj.toString();
			response=response.replace("\\/","\\\\/");
			return response;
		}


		logger.info("End of DataFromServiceHttp: ");
		return jsonObj.toString();
	}
	
	public static String DataFromServiceInternalHttp(String urlString, String body,String Authorization)
	{
		logger.info("Inside DataFromServiceInternalHttp: "+urlString+" body"+body);

		JSONObject jsonObj = new JSONObject();
		try {

			URL url = new URL(urlString);		
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			  
			 
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Accept", "application/json");
		           
		          
			/*logger.info("URL: ");
			
			HttpURLConnection  connection  = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");*/
			connection.setConnectTimeout(120000); // Request for 2 Minutes
			
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(body);
			logger.info("Request body: "+body);
			wr.flush();
			wr.close();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				logger.info("Http response is ok");
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuffer res = new StringBuffer(); 
				String line;
				while ((line = reader.readLine()) != null) {
					res.append(line);
				}
				reader.close();
				jsonObj.put("result", res.toString());
				logger.info("Response: "+res.toString());

			} else {
				int code=connection.getResponseCode();
				String status=String.valueOf(code);
				logger.info("Response code: "+status);
				JSONObject statusObj = new JSONObject();
				statusObj.put("ErrorInfo", status);
				jsonObj.put("Error",statusObj);
				return jsonObj.toString();
			}

		} catch (Exception e) {
			logger.error("Exception: "+e);
			JSONObject statusObj = new JSONObject();
			statusObj.put("ErrorInfo", e.getClass()+" "+e.getMessage());
			jsonObj.put("Error",statusObj);
			String response=jsonObj.toString();
			response=response.replace("\\/","\\\\/");
			return response;
		}


		logger.info("End of DataFromServiceHttp: ");
		return jsonObj.toString();
	}

	/** 
     * Added by Ashitosh Rajguru
     * To check whether input consiste of scripting tags
     * @param decValue
     * @return xssAttackFlag
     */
	public static boolean checkScriptTag(String decValue)
	{
		logger.info("Start of checkScriptTag of Utils: "+decValue);
		boolean xssAttackFlag = false;

		String tagFound = "";

		try
		{
			if(!"".equals(decValue) && null!=decValue)
			{
				decValue = decValue.toUpperCase();

			}

			if(StringUtils.containsIgnoreCase(decValue, SCRIPT_KEYWORD1))
			{
				tagFound=SCRIPT_KEYWORD1;
				xssAttackFlag = true;
			}
			else if( StringUtils.containsIgnoreCase(decValue,SCRIPT_KEYWORD2))
			{
				tagFound=SCRIPT_KEYWORD2;
				xssAttackFlag = true;
			}
			else if( StringUtils.containsIgnoreCase(decValue,SCRIPT_KEYWORD3))
			{
				tagFound=SCRIPT_KEYWORD3;
				xssAttackFlag = true;
			}
			else if(StringUtils.containsIgnoreCase(decValue,SCRIPT_KEYWORD4))
			{
				tagFound=SCRIPT_KEYWORD4;
				xssAttackFlag = true;
			}
			else if(StringUtils.containsIgnoreCase(decValue,LINK_KEYWORD1))
			{
				tagFound=LINK_KEYWORD1;
				xssAttackFlag = true;
			} 
			else if( StringUtils.containsIgnoreCase(decValue,LINK_KEYWORD2))
			{
				tagFound=LINK_KEYWORD2;
				xssAttackFlag = true;
			}
			else if(StringUtils.containsIgnoreCase(decValue,LINK_KEYWORD3))
			{
				tagFound=LINK_KEYWORD3;
				xssAttackFlag = true;
			}
			else if( StringUtils.containsIgnoreCase(decValue,LINK_KEYWORD4))
			{
				tagFound=LINK_KEYWORD2;
				xssAttackFlag = true;
			}
			else if( StringUtils.containsIgnoreCase(decValue,IMG_KEYWORD3))
			{
				tagFound=IMG_KEYWORD4;
				xssAttackFlag = true;
			}
			else if( StringUtils.containsIgnoreCase(decValue,IMG_KEYWORD1))
			{
				tagFound=IMG_KEYWORD1;
				xssAttackFlag = true;
			}
			else if(StringUtils.containsIgnoreCase(decValue,IMG_KEYWORD2))
			{
				tagFound=IMG_KEYWORD2;
				xssAttackFlag = true;
			}
			else if( StringUtils.containsIgnoreCase(decValue,IMG_KEYWORD4))
			{
				tagFound=IMG_KEYWORD4;
				xssAttackFlag = true;
			} 
			else if( StringUtils.containsIgnoreCase(decValue,IMG_KEYWORD5))
			{
				tagFound=IMG_KEYWORD5;
				xssAttackFlag = true;
			}
			else if( StringUtils.containsIgnoreCase(decValue,IMG_KEYWORD6))
			{
				tagFound=IMG_KEYWORD6;
				xssAttackFlag = true;
			}

			else if( StringUtils.containsIgnoreCase(decValue,FRAME_KEYWORD1))
			{
				tagFound=FRAME_KEYWORD1;
				xssAttackFlag = true;
			}
			else if(StringUtils.containsIgnoreCase(decValue,FRAME_KEYWORD2))
			{
				tagFound=FRAME_KEYWORD2;
				xssAttackFlag = true;
			}
			else if( StringUtils.containsIgnoreCase(decValue,FRAME_KEYWORD3))
			{
				tagFound=FRAME_KEYWORD3;
				xssAttackFlag = true;
			}
			else if( StringUtils.containsIgnoreCase(decValue,FRAME_KEYWORD4))
			{
				tagFound=FRAME_KEYWORD4;
				xssAttackFlag = true;
			}
			else if( StringUtils.containsIgnoreCase(decValue,INPUT_KEYWORD1))
			{
				tagFound=INPUT_KEYWORD1;
				xssAttackFlag = true;
			}
			else if( StringUtils.containsIgnoreCase(decValue,INPUT_KEYWORD2))
			{
				tagFound=INPUT_KEYWORD2;
				xssAttackFlag = true;
			}

			if(xssAttackFlag)
			{
				logger.info("Possible XSS attack detected! A '"+ tagFound +"' tag was found in the request - parameter decValue :: '" +decValue + "' of your action." );
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (Exception e) {
			logger.error("Exception :"+e);
			return false;
		}
	}

	
public static void setResponseHeaders(HttpServletResponse httpResponse){
		
		if(httpResponse != null)
		{
			httpResponse.setHeader("Cache-control", "no-cache, no-store,must-revalidate");
			httpResponse.setHeader("Pragma", "no-cache");
			httpResponse.setHeader("X-UA-Compatible", "IE=edge,chrome=1");
			httpResponse.setHeader("X-Frame-Options","SAMEORIGIN");
			httpResponse.setHeader("X-XSS-Protection","1;mode=block");
			httpResponse.setHeader("X-Content-Type-Options"," nosniff");
			httpResponse.setHeader("Expires", "-1");
			httpResponse.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");

		}
}
	
	
	public static String infoLogSizeValidator(String logInfo)
	{
		String finalLogInfo = "";
		int size = 200;
		if(logInfo.length() > size)
		{
			finalLogInfo = logInfo.substring(0,size) + "   ..............."+logInfo.length()+" Remaining......";
		}
		else
		{
			finalLogInfo = logInfo;
		}
		return finalLogInfo;
	}
	
	
	public static boolean checkSpecialCharacterr(String input){
		boolean flag=false;
		String[] specialChars={"~","`","!","@","#","$","%","^","&","*","(",")","_","-","+","=",":",";","\"","'",",",".","<",">","?","/","{","}","[","]","|","\\"};
		for(String ss:specialChars ){
			if(input.contains(ss)){
				flag=true;
				break;
			}
		}
		return flag;
		
	}
public static String formatDate(Date inputDate, String outputFormat) {
		
		if(inputDate == null)
			return "";
		
		SimpleDateFormat sdf = new SimpleDateFormat(outputFormat);
		String outDate = "";

		outDate = sdf.format(inputDate);

		return outDate;
	}

	public static String convertDateFormats(String inputDate, String inputFormat, String outputFormat) {
		
		SimpleDateFormat sdfInput = new SimpleDateFormat(inputFormat);
		SimpleDateFormat sdfOutput = new SimpleDateFormat(outputFormat);
		Date date;
		String outDate = "";

		if (!inputDate.trim().equals("")) {
			try {
				date = sdfInput.parse(inputDate);

				outDate = sdfOutput.format(date);
			} catch (Exception e) {
				logger.error(e);
			}
		}

		return outDate;
	}
	
	public static String GeneratePDF(String header, String resultStr, String path,String labelCnt) {
		ByteArrayOutputStream pdfBytesStream = null; 
		logger.info("in GeneratePDF() : "+path);
		//CustomerDAO dao = new CustomerDAOImpl();
		//String resultStr = dao.printStatement(ajaxXmlData);
		String bankApps = "Yes Bank Ltd.";
  	  	try {
  	  		
  	  		String footer = "Yes Bank Pvt Ltd.";
  	
  	  		File newFile = new File(path);
  	  		
  	  		resultStr=resultStr.replaceAll("<br>", "<br></br>");
			OutputStream file = new FileOutputStream(newFile);
			pdfBytesStream = CreatePDFStream("", header, resultStr, footer,"","",labelCnt);
			byte[] bytes = pdfBytesStream.toByteArray();
			file.write(bytes);
			file.close();
			logger.info("in GeneratePDF sucessful : "+path);
  		} catch (Exception e) {
  			logger.error("file creation failed: ",e);
  			return "FAIL";
  		}
  		return "SUCCESS";
	}
	
	public static  ByteArrayOutputStream CreatePDFStream(String custName, String header, String htmlIn, String footer, String fileName, String orientation,String labelCnt) {

		logger.info("Creating PDF Stream");

		StringBuffer htmlstringbuf = new StringBuffer();
		ByteArrayOutputStream pdffinalos = new ByteArrayOutputStream();
		htmlstringbuf.append(htmlIn.toString());

		int screenWidth = 768;
		int screenHeight = 1024;
		
		try 
		{
			/*HttpServletRequest request = ServletActionContext.getRequest();*/
			//String directory = request.getSession().getServletContext().getRealPath("");
			
			String directory = ApplicationGenParameter.BULK_BENE_FILE_UPLOAD_PATH+"/PDF/";
			String imagePath = ApplicationGenParameter.BULK_BENE_FILE_UPLOAD_PATH+"/PDF/";
			
			// Modified By 2054 on 27-Dec-2012
			String htmlstring = "";
			if(Integer.parseInt(labelCnt) > 11 )
			{
				htmlstring = "<body style='width:100%'><head><style language='text/css'>@page{margin-top:1cm;margin-left:8%;margin-bottom:1.5cm;@bottom-left{ white-space: pre;text-align:right; content: '"+footer+ "'; font:10px Arial, Helvetica, sans-serif;}}@page:first {margin-top: 3.7cm;}body{font-family:Arial;font-size: 8pt;padding:0;margin:0;}@page land { size:landscape; } @page port { size:portrait; } .landscapePage { page:land; } .portraitPage { page:port; }table { -fs-table-paginate: paginate;border-collpase:collapse;} table.viewTableClass {width:100%;font-family:Arial;font-size: 8pt;border: 0px solid black;} table.bottomTableClass {width:100%;font-family:Zurich BT;font-size: 6pt;border: 0px solid black;} table.viewTableClass thead tr.heading th {background-color: #cb842e;border: 1px solid #FFF;font-size: 6pt;padding: 4px;text-align: center;page-break-inside: avoid;} table.viewTableClass thead tr th {background-color:#AA0F0F;font-size: 6pt;padding: 4px;text-align: center;page-break-inside: avoid;color: #FFFFFF;} table.viewTableClass tfoot tr td {background-color: #cb842e;border: 1px solid #FFF;font-size: 6pt;padding: 4px;text-align: center;color: #FFFFFF;} table.viewTableClass tfoot tr.summary td {text-align: right;}table.viewTableClass thead tr.headerClass th {background-color: #cb842e;border: 1px solid #FFF;font-size: 7pt;padding: 4px;text-align: center;color: #FFFFFF;page-break-inside: avoid;}table.viewTableClass thead tr.openClosePos th {background-color: #cb842e;border: 1px solid #FFF;font-size: 6pt;padding: 4px;text-align: center;color: #FFFFFF;}table.viewTableClass thead tr.summary th {background-color: #cb842e;border: 1px solid #FFF;font-size: 6pt;padding: 2px;text-align: right;}table.viewTableClass thead tr .header {	background-image: url(bg.gif);background-repeat: no-repeat;background-position: center right;cursor: pointer;}table.viewTableClass tbody tr td.totNumTd,td.visaNumTd,td.mcNumTd {text-align: right;}table.viewTableClass tbody tr.summary td {text-align: right;}table.viewTableClass tbody tr.matc td {background-color: #FFFFFF;text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td.left {background-color: #FFFFFF;text-align: left;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td {background-color: #FFFFFF;text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td.olive {color: #000000;background-color: #FACC2E;text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td.black {color: #FFF;background-color: black;text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td.yellow {background-color: #F3F781;text-align: right;font-weight: bold;color: #FFF;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td.red {font-family:arial;font-size: 6pt;color: #FFF;	background-color: #AA0F0F;	text-align: right;font-weight: bold;}table.viewTableClass tbody tr.matc td.green {color: #FFF;background-color: #AA0F0F;text-align: right;font-weight: bold;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td.pink {color: black;background-color: pink;text-align: center;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td.violet {background-color: #FDB8C3;text-align: right;color : black;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td.nocolor {text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td.acDtl {text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td.hidden {display:none;width: 0;}table.viewTableClass tbody tr.matc td.res_yw {background-color: #FFFF00;text-align: right;font-weight: bold;color: #000;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.pend td {background-color: #E3E8FA;text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass thead tr.headerClass th.green {background-color: #EBFFEB;border: 1px solid #FFF;font-size: 6pt;padding: 4px;color: black;text-align: center;}table.viewTableClass thead tr.headerClass th.pink {background-color: #FFF2E5;border: 1px solid #FFF;font-size: 6pt;padding: 4px;color: black;text-align: center;}table.viewTableClass thead tr.headerClass th.mehandi {background-color: #948B54;border: 1px solid #FFF;font-size: 6pt;padding: 4px;color: #FFF;text-align: center;}table.viewTableClass thead tr.headerClass th.yellow {background-color: #D2E0B2;border: 1px solid #FFF;	font-size: 6pt;	padding: 4px;color: black;text-align: center;}table.viewTableClass thead tr.headerSection th.green {background-color: #003A00;border: 1px solid #FFF;font-size: 6pt;padding: 4px;color: #FFF;text-align: center;}table.viewTableClass thead tr.headerSection th.brown {background-color: #603000;border: 1px solid #FFF;	font-size: 6pt;	padding: 4px;color: #FFF;text-align: center;}table.viewTableClass thead tr.headerSection th.mehandi {background-color: #948B54;border: 1px solid #FFF;font-size: 6pt;padding: 4px;color: #FFF;text-align: center;}table.viewTableClass tbody tr.matched {text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.unmatched {text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass tbody td {padding: 4px;background-color: #FFF;	cursor: pointer;}table.viewTableClass tbody tr.pend td.blue {color: #FFF;background-color: #FFF;text-align: right;font-weight: bold;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.pend td.olive {color: #000000;background-color: #FACC2E;text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.pend td.black {color: #FFF;background-color: black;text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.pend td.yellow {background-color: #F3F781;text-align: right;font-weight: bold;color: #FFF;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.pend td.red {color: #FFF;background-color: #AA0F0F;text-align: right;font-weight: bold;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.pend td.green {color: #FFF;background-color: #AA0F0F;text-align: right;font-weight: bold;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.pend td.pink {color: black;background-color: pink;text-align: center;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.pend td.violet {background-color: #FDB8C3;text-align: right;color : black;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.pend td.nocolor {text-align: right;font-family:arial;font-size: 6pt;} table.viewTableClass tbody tr.pend td.hidden {display:none;width: 0;}table.viewTableClass tbody tr.pend td.res_yw {background-color: #FFFF00;text-align: right;font-weight: bold;color: #000;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr td.cnt {text-align: right;background: #D6D6C2;-moz-box-shadow:inset 0px 8px 1px rgba(255,255,255,.5);-webkit-box-shadow: inset 0px 8px 1px rgba(255,255,255,.5); box-shadow:inset 0px 8px 1px rgba(255,255,255,.5);} .heading{ font-family: arial; background-color:#fff; font-size: 7pt;font-weight: bold;color: black;margin: 0px;padding: 0px;height: 20px;}table.endTableClass thead tr td{text-align: center;font-family:arial;font-size: 6pt;}table.reportTableClass {font-family: arial;background-color: gray;font-size: 7pt;border: 1px solid black;}table.reportTableClass thead tr.heading th {background-color: #fff;border: 1px solid #FFF;font-size: 7pt;padding: 6px;text-align: left;page-break-inside: avoid;}table.reportTableClass thead tr th {background-color: #cb842e;border: 1px solid #FFF;font-size: 6pt;padding: 4px;text-align: center;page-break-inside: avoid;}table.reportTableClass tbody tr.matc td {background-color: #FFFFFF;text-align: left;font-family: arial;font-size: 6pt;}table.reportTableClass tbody tr.matc td.nu {background-color: #FFFFFF;text-align: right;font-family: arial;font-size: 6pt;}</style></head>";
			}
			else if(Integer.parseInt(labelCnt) > 10 ) 
			{
				htmlstring = "<body style='width:100%'><head><style language='text/css'>@page{margin-top:1cm;margin-left:10%;margin-bottom:1.5cm;@bottom-left{ white-space: pre;text-align:right; content: '"+footer+ "'; font:10px Arial, Helvetica, sans-serif;}}@page:first {margin-top: 3.7cm;}body{font-family:Arial;font-size: 8pt;padding:0;margin:0;}@page land { size:landscape; } @page port { size:portrait; } .landscapePage { page:land; } .portraitPage { page:port; }table { -fs-table-paginate: paginate;border-collpase:collapse;} table.viewTableClass {width:100%;font-family:Arial;font-size: 8pt;border: 0px solid black;} table.bottomTableClass {width:100%;font-family:Zurich BT;font-size: 6pt;border: 0px solid black;} table.viewTableClass thead tr.heading th {background-color: #cb842e;border: 1px solid #FFF;font-size: 6pt;padding: 4px;text-align: center;page-break-inside: avoid;} table.viewTableClass thead tr th {background-color:#AA0F0F;font-size: 6pt;padding: 4px;text-align: center;page-break-inside: avoid;color: #FFFFFF;} table.viewTableClass tfoot tr td {background-color: #cb842e;border: 1px solid #FFF;font-size: 6pt;padding: 4px;text-align: center;color: #FFFFFF;} table.viewTableClass tfoot tr.summary td {text-align: right;}table.viewTableClass thead tr.headerClass th {background-color: #cb842e;border: 1px solid #FFF;font-size: 7pt;padding: 4px;text-align: center;color: #FFFFFF;page-break-inside: avoid;}table.viewTableClass thead tr.openClosePos th {background-color: #cb842e;border: 1px solid #FFF;font-size: 6pt;padding: 4px;text-align: center;color: #FFFFFF;}table.viewTableClass thead tr.summary th {background-color: #cb842e;border: 1px solid #FFF;font-size: 6pt;padding: 2px;text-align: right;}table.viewTableClass thead tr .header {	background-image: url(bg.gif);background-repeat: no-repeat;background-position: center right;cursor: pointer;}table.viewTableClass tbody tr td.totNumTd,td.visaNumTd,td.mcNumTd {text-align: right;}table.viewTableClass tbody tr.summary td {text-align: right;}table.viewTableClass tbody tr.matc td {background-color: #FFFFFF;text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td.left {background-color: #FFFFFF;text-align: left;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td {background-color: #FFFFFF;text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td.olive {color: #000000;background-color: #FACC2E;text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td.black {color: #FFF;background-color: black;text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td.yellow {background-color: #F3F781;text-align: right;font-weight: bold;color: #FFF;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td.red {font-family:arial;font-size: 6pt;color: #FFF;	background-color: #AA0F0F;	text-align: right;font-weight: bold;}table.viewTableClass tbody tr.matc td.green {color: #FFF;background-color: #AA0F0F;text-align: right;font-weight: bold;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td.pink {color: black;background-color: pink;text-align: center;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td.violet {background-color: #FDB8C3;text-align: right;color : black;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td.nocolor {text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td.acDtl {text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td.hidden {display:none;width: 0;}table.viewTableClass tbody tr.matc td.res_yw {background-color: #FFFF00;text-align: right;font-weight: bold;color: #000;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.pend td {background-color: #E3E8FA;text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass thead tr.headerClass th.green {background-color: #EBFFEB;border: 1px solid #FFF;font-size: 6pt;padding: 4px;color: black;text-align: center;}table.viewTableClass thead tr.headerClass th.pink {background-color: #FFF2E5;border: 1px solid #FFF;font-size: 6pt;padding: 4px;color: black;text-align: center;}table.viewTableClass thead tr.headerClass th.mehandi {background-color: #948B54;border: 1px solid #FFF;font-size: 6pt;padding: 4px;color: #FFF;text-align: center;}table.viewTableClass thead tr.headerClass th.yellow {background-color: #D2E0B2;border: 1px solid #FFF;	font-size: 6pt;	padding: 4px;color: black;text-align: center;}table.viewTableClass thead tr.headerSection th.green {background-color: #003A00;border: 1px solid #FFF;font-size: 6pt;padding: 4px;color: #FFF;text-align: center;}table.viewTableClass thead tr.headerSection th.brown {background-color: #603000;border: 1px solid #FFF;	font-size: 6pt;	padding: 4px;color: #FFF;text-align: center;}table.viewTableClass thead tr.headerSection th.mehandi {background-color: #948B54;border: 1px solid #FFF;font-size: 6pt;padding: 4px;color: #FFF;text-align: center;}table.viewTableClass tbody tr.matched {text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.unmatched {text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass tbody td {padding: 4px;background-color: #FFF;	cursor: pointer;}table.viewTableClass tbody tr.pend td.blue {color: #FFF;background-color: #FFF;text-align: right;font-weight: bold;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.pend td.olive {color: #000000;background-color: #FACC2E;text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.pend td.black {color: #FFF;background-color: black;text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.pend td.yellow {background-color: #F3F781;text-align: right;font-weight: bold;color: #FFF;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.pend td.red {color: #FFF;background-color: #AA0F0F;text-align: right;font-weight: bold;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.pend td.green {color: #FFF;background-color: #AA0F0F;text-align: right;font-weight: bold;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.pend td.pink {color: black;background-color: pink;text-align: center;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.pend td.violet {background-color: #FDB8C3;text-align: right;color : black;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.pend td.nocolor {text-align: right;font-family:arial;font-size: 6pt;} table.viewTableClass tbody tr.pend td.hidden {display:none;width: 0;}table.viewTableClass tbody tr.pend td.res_yw {background-color: #FFFF00;text-align: right;font-weight: bold;color: #000;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr td.cnt {text-align: right;background: #D6D6C2;-moz-box-shadow:inset 0px 8px 1px rgba(255,255,255,.5);-webkit-box-shadow: inset 0px 8px 1px rgba(255,255,255,.5); box-shadow:inset 0px 8px 1px rgba(255,255,255,.5);} .heading{ font-family: arial; background-color:#fff; font-size: 7pt;font-weight: bold;color: black;margin: 0px;padding: 0px;height: 20px;}table.endTableClass thead tr td{text-align: center;font-family:arial;font-size: 6pt;}table.reportTableClass {font-family: arial;background-color: gray;font-size: 7pt;border: 1px solid black;}table.reportTableClass thead tr.heading th {background-color: #fff;border: 1px solid #FFF;font-size: 7pt;padding: 6px;text-align: left;page-break-inside: avoid;}table.reportTableClass thead tr th {background-color: #cb842e;border: 1px solid #FFF;font-size: 6pt;padding: 4px;text-align: center;page-break-inside: avoid;}table.reportTableClass tbody tr.matc td {background-color: #FFFFFF;text-align: left;font-family: arial;font-size: 6pt;}table.reportTableClass tbody tr.matc td.nu {background-color: #FFFFFF;text-align: right;font-family: arial;font-size: 6pt;}</style></head>";
			}
			else
			{
				htmlstring = "<body style='width:100%'><head><style language='text/css'>@page{margin-top:1cm;margin-left:12%;margin-bottom:1.5cm;@bottom-left{ white-space: pre;text-align:right; content: '"+footer+ "'; font:10px Arial, Helvetica, sans-serif;}}@page:first {margin-top: 3.7cm;}body{font-family:Arial;font-size: 8pt;padding:0;margin:0;}@page land { size:landscape; } @page port { size:portrait; } .landscapePage { page:land; } .portraitPage { page:port; }table { -fs-table-paginate: paginate;border-collpase:collapse;} table.viewTableClass {width:100%;font-family:Arial;font-size: 8pt;border: 0px solid black;} table.bottomTableClass {width:100%;font-family:Zurich BT;font-size: 6pt;border: 0px solid black;} table.viewTableClass thead tr.heading th {background-color: #cb842e;border: 1px solid #FFF;font-size: 6pt;padding: 4px;text-align: center;page-break-inside: avoid;} table.viewTableClass thead tr th {background-color:#AA0F0F;font-size: 6pt;padding: 4px;text-align: center;page-break-inside: avoid;color: #FFFFFF;} table.viewTableClass tfoot tr td {background-color: #cb842e;border: 1px solid #FFF;font-size: 6pt;padding: 4px;text-align: center;color: #FFFFFF;} table.viewTableClass tfoot tr.summary td {text-align: right;}table.viewTableClass thead tr.headerClass th {background-color: #cb842e;border: 1px solid #FFF;font-size: 7pt;padding: 4px;text-align: center;color: #FFFFFF;page-break-inside: avoid;}table.viewTableClass thead tr.openClosePos th {background-color: #cb842e;border: 1px solid #FFF;font-size: 6pt;padding: 4px;text-align: center;color: #FFFFFF;}table.viewTableClass thead tr.summary th {background-color: #cb842e;border: 1px solid #FFF;font-size: 6pt;padding: 2px;text-align: right;}table.viewTableClass thead tr .header {	background-image: url(bg.gif);background-repeat: no-repeat;background-position: center right;cursor: pointer;}table.viewTableClass tbody tr td.totNumTd,td.visaNumTd,td.mcNumTd {text-align: right;}table.viewTableClass tbody tr.summary td {text-align: right;}table.viewTableClass tbody tr.matc td {background-color: #FFFFFF;text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td.left {background-color: #FFFFFF;text-align: left;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td {background-color: #FFFFFF;text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td.olive {color: #000000;background-color: #FACC2E;text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td.black {color: #FFF;background-color: black;text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td.yellow {background-color: #F3F781;text-align: right;font-weight: bold;color: #FFF;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td.red {font-family:arial;font-size: 6pt;color: #FFF;	background-color: #AA0F0F;	text-align: right;font-weight: bold;}table.viewTableClass tbody tr.matc td.green {color: #FFF;background-color: #AA0F0F;text-align: right;font-weight: bold;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td.pink {color: black;background-color: pink;text-align: center;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td.violet {background-color: #FDB8C3;text-align: right;color : black;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td.nocolor {text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td.acDtl {text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.matc td.hidden {display:none;width: 0;}table.viewTableClass tbody tr.matc td.res_yw {background-color: #FFFF00;text-align: right;font-weight: bold;color: #000;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.pend td {background-color: #E3E8FA;text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass thead tr.headerClass th.green {background-color: #EBFFEB;border: 1px solid #FFF;font-size: 6pt;padding: 4px;color: black;text-align: center;}table.viewTableClass thead tr.headerClass th.pink {background-color: #FFF2E5;border: 1px solid #FFF;font-size: 6pt;padding: 4px;color: black;text-align: center;}table.viewTableClass thead tr.headerClass th.mehandi {background-color: #948B54;border: 1px solid #FFF;font-size: 6pt;padding: 4px;color: #FFF;text-align: center;}table.viewTableClass thead tr.headerClass th.yellow {background-color: #D2E0B2;border: 1px solid #FFF;	font-size: 6pt;	padding: 4px;color: black;text-align: center;}table.viewTableClass thead tr.headerSection th.green {background-color: #003A00;border: 1px solid #FFF;font-size: 6pt;padding: 4px;color: #FFF;text-align: center;}table.viewTableClass thead tr.headerSection th.brown {background-color: #603000;border: 1px solid #FFF;	font-size: 6pt;	padding: 4px;color: #FFF;text-align: center;}table.viewTableClass thead tr.headerSection th.mehandi {background-color: #948B54;border: 1px solid #FFF;font-size: 6pt;padding: 4px;color: #FFF;text-align: center;}table.viewTableClass tbody tr.matched {text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.unmatched {text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass tbody td {padding: 4px;background-color: #FFF;	cursor: pointer;}table.viewTableClass tbody tr.pend td.blue {color: #FFF;background-color: #FFF;text-align: right;font-weight: bold;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.pend td.olive {color: #000000;background-color: #FACC2E;text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.pend td.black {color: #FFF;background-color: black;text-align: right;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.pend td.yellow {background-color: #F3F781;text-align: right;font-weight: bold;color: #FFF;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.pend td.red {color: #FFF;background-color: #AA0F0F;text-align: right;font-weight: bold;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.pend td.green {color: #FFF;background-color: #AA0F0F;text-align: right;font-weight: bold;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.pend td.pink {color: black;background-color: pink;text-align: center;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.pend td.violet {background-color: #FDB8C3;text-align: right;color : black;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr.pend td.nocolor {text-align: right;font-family:arial;font-size: 6pt;} table.viewTableClass tbody tr.pend td.hidden {display:none;width: 0;}table.viewTableClass tbody tr.pend td.res_yw {background-color: #FFFF00;text-align: right;font-weight: bold;color: #000;font-family:arial;font-size: 6pt;}table.viewTableClass tbody tr td.cnt {text-align: right;background: #D6D6C2;-moz-box-shadow:inset 0px 8px 1px rgba(255,255,255,.5);-webkit-box-shadow: inset 0px 8px 1px rgba(255,255,255,.5); box-shadow:inset 0px 8px 1px rgba(255,255,255,.5);} .heading{ font-family: arial; background-color:#fff; font-size: 7pt;font-weight: bold;color: black;margin: 0px;padding: 0px;height: 20px;}table.endTableClass thead tr td{text-align: center;font-family:arial;font-size: 6pt;}table.reportTableClass {font-family: arial;background-color: gray;font-size: 7pt;border: 1px solid black;}table.reportTableClass thead tr.heading th {background-color: #fff;border: 1px solid #FFF;font-size: 7pt;padding: 6px;text-align: left;page-break-inside: avoid;}table.reportTableClass thead tr th {background-color: #cb842e;border: 1px solid #FFF;font-size: 6pt;padding: 4px;text-align: center;page-break-inside: avoid;}table.reportTableClass tbody tr.matc td {background-color: #FFFFFF;text-align: left;font-family: arial;font-size: 6pt;}table.reportTableClass tbody tr.matc td.nu {background-color: #FFFFFF;text-align: right;font-family: arial;font-size: 6pt;}</style></head>";
			}
			
			 htmlstring += htmlstringbuf
						.toString()
						.replaceAll("&nbsp;", " ")
						.replaceAll("&", "&amp;")
						.replaceAll("null", " ")
						.replaceAll(
								"<\\?xml version=\"1.0\" encoding=\"UTF-8\"\\?>",
								" ")
						.replaceAll("Â", "<br></br>")
						.replaceAll(
								"<\\?xml version = '1.0' encoding = 'UTF-8'\\?>",
								" ") + "</body>";
			
			ByteArrayOutputStream pdfos = new ByteArrayOutputStream();
			logger.info("Before getting path ITextRenderer ");
			ITextRenderer renderer = null;
			
			//BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,BaseFont.WINANSI, BaseFont.EMBEDDED);
			logger.info("Before BaseFont.createFont() ");
			BaseFont bf = null;
			PdfReader reader = null;
			PdfStamper stamper = null;
			
			try
			{
				logger.info("In try ITextRenderer ");
				renderer = new ITextRenderer();
				logger.info("after ITextRenderer class call");
				
				logger.info("Before getFontResolver().addFontDirectory() ");
				renderer.getFontResolver().addFontDirectory(directory + "arial.ttf",true);
				bf = BaseFont.createFont(BaseFont.HELVETICA,BaseFont.WINANSI, BaseFont.EMBEDDED);
				
				renderer.setDocumentFromString(htmlstring);
				logger.info("ImagePath = "+imagePath);
				
				renderer.layout();
				renderer.createPDF(pdfos);
				reader = new PdfReader(new ByteArrayInputStream(pdfos.toByteArray()));
				stamper = new PdfStamper(reader, pdffinalos);
				logger.info("Current Font Directory = " + directory);
				
			}
			catch (Exception e) {
				logger.error("Exception - ",e);
			}finally{
				logger.error("finally executed 291216");
			}
			
			PdfContentByte over;
			// Commented because - Toolkit.getDefaultToolkit().getScreenSize() not working on weblogic server
			
			int ximg1 = 0;
			int yimg1 = 0;
			int ximg2 = 0;
			int yimg2 = 0;
			int yPointHeader = 0;
			int headerCenterPt = 0;
						
			int imgPosY = screenHeight - 700;	
			int headerPosY = screenHeight - 30;	
			
			if(screenHeight >= 500 && screenHeight < 768)
			{
				imgPosY = screenHeight + 145;
				headerPosY = screenHeight + 190;				
			}
			else if(screenHeight >= 768 && screenHeight < 900)
			{
				imgPosY = screenHeight - 55;
				headerPosY = screenHeight - 20;
			}
			else if(screenHeight >= 900 && screenHeight < 1000)
			{
				imgPosY = screenHeight - 155;
				headerPosY = screenHeight - 110;
			}
			else if(screenHeight >= 1000)
			{
				imgPosY = screenHeight - 295;
				headerPosY = screenHeight - 250;
			}
			if(orientation.equals("landscape"))
			{
				ximg1 = 20;
				yimg1 = 400;
				ximg2 = 570;
				yimg2 = 530;
				yPointHeader = 555;
				headerCenterPt = 0;
			}
			else
			{
				ximg1 = 20;
				ximg2 = 20;
				headerCenterPt = 0;
				
				yimg1 = imgPosY;
				yimg2 = imgPosY;
				yPointHeader = headerPosY;
			}
			String imageName = "";	
			Image img1 = null;
			Image img2 = null;
			try 
			{
				//imageName = imagePath+ApplicationGenParameter.parameterMasterList.get("PDF_LOGO");
				imageName = imagePath+"Yes_Bank_Logo.png"; //tick2
				img1 = Image.getInstance(imageName);
				if ((new File(imageName)).exists()) {
					logger.info("Adding " + imageName + " to PDF");
					img1.setAbsolutePosition(350, 500);
				}	
				
				imageName = imagePath+"tick2.png"; //tick2
				img2 = Image.getInstance(imageName);
				if ((new File(imageName)).exists()) {
					logger.info("Adding " + imageName + " to PDF");
					img2.setAbsolutePosition(0, 0);
				}	
				
			} 
			catch (FileNotFoundException e) {
				logger.info("File " + imageName + " does not exists");
			}	
			
			// image on first page only
			// second page onwards will have image + customer name
			over = stamper.getUnderContent(1);
			over.addImage(img2);
			over.addImage(img1);
			
			
			over.beginText();
			over.setFontAndSize(bf, 9);
			String [] headerArray = header.split("~");
			
			int yPoint = yPointHeader;
			for(int i = 0; i < headerArray.length; i++)
			{
				over.showTextAligned(PdfContentByte.ALIGN_CENTER, headerArray[i], headerCenterPt, yPoint, 0);
				yPoint -= 15;
				over.newlineText();
			}
			
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, custName, 45,720, 0);
			over.setFontAndSize(bf,7);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, "", 28,30, 0);
			over.endText();
			
			//img.setAbsolutePosition(50, 728);
			int total = reader.getNumberOfPages() + 1;

			for (int i = 2; i < total; i++) {
				over = stamper.getUnderContent(i);
				//over.addImage(img1);
				over.addImage(img2);
				
				over.beginText();
				over.setFontAndSize(bf, 9);
				yPoint = yPointHeader ;
				for(int j = 0; j < headerArray.length; j++)
				{
					over.showTextAligned(PdfContentByte.ALIGN_CENTER, headerArray[j], headerCenterPt, yPoint, 0);
					yPoint -= 15;
					over.newlineText();
				}
				over.showTextAligned(PdfContentByte.ALIGN_CENTER, custName, 45,720, 0);
				over.setFontAndSize(bf, 7);
				//check if footer is not coming
				//over.showTextAligned(PdfContentByte.ALIGN_LEFT, footer, 28,30, 0);
				over.endText();
			}
			stamper.close();
			pdfos.close();
		} catch (Exception e) {
			logger.error("Creating ByteArrayOutputStream - Exception while writing a file -", e);
		}
		return pdffinalos;
	}
	
	public static char[] OTP(int len,int type)
    {
		logger.info("Generating OTP using random() : ");
		logger.info("You OTP is : ");
 
        String numbers = "";
        // Using numeric values
        if(type == 1) 
        {
        	numbers = "0123456789";
        }
        else 
        {
        	numbers = "abcdeABC267fghiDE01jklmFGHIJ345nopqKLMNO67rstuPQRST34vwxyzUV89WXYZ2";
        }
       
        // Using random method
        Random rndm_method = new Random();
 
        char[] otp = new char[len];
 
        for (int i = 0; i < len; i++)
        {
            // Use of charAt() method : to get character value
            // Use of nextInt() as it is scanning the value as int
            otp[i] =
             numbers.charAt(rndm_method.nextInt(numbers.length()));
        }
        logger.info("You OTP is : ");
        return otp;
    }
}
class HostVerifier implements HostnameVerifier
{
	public boolean verify(String arg0, SSLSession arg1) {
		// TODO Auto-generated method stub
		return false;
	}
}
