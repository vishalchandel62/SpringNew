package com.credentek.msme.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.credentek.msme.ApplicationGenParm.ApplicationGenParameter;
import com.credentek.msme.api.dao.CustomerManagementDAO;
import com.credentek.msme.loginmodel.FirstPdf;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class CommonUtility {
	private static final Logger log = LogManager.getLogger(CommonUtility.class);
	public static final String imgTickPath= ApplicationGenParameter.BULK_BENE_FILE_UPLOAD_PATH+"/PDF/tick2.png";
	public static final String imgLogoPath =  ApplicationGenParameter.BULK_BENE_FILE_UPLOAD_PATH+"/PDF/Yes_Bank_Logo.png";
	public static final Integer caseID_length =  14;
	public static String maskString(String input) {

        if(input == null) {
          return "NULL";
        }
        return input.replaceAll(".(?=.{4})", "x");
      }

	
	public static String getCurrentTimeStamp( )
    {
	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));           
		//log.info(sdf.format(new Date()));
		return sdf.format(new Date());
    }
	public static String getReqRefNum()
	{
 
		/*
		 * Calendar calendar = Calendar.getInstance(); calendar.setTime(new Date());
		 * log.info("Calender - Time in milliseconds : "); Integer reqRefNoVal=new
		 * Integer(Integer.valueOf((int) calendar.getTimeInMillis())); //return
		 * String.valueOf(reqRefNoVal); String
		 * reqRefNo=String.valueOf(calendar.getTimeInMillis());
		 * 
		 * log.info("testing<<<<"); //Integer reqRefNoVal1=1000000000; if (reqRefNoVal <
		 * 0){ log.info("negative vaue::"); Math.abs(reqRefNoVal);
		 * log.info("test::"+Math.abs(reqRefNoVal)); }
		 * 
		 * return reqRefNo;
		 */
		String reqRefNo =""; 
		try {
			
			/*Calendar calendar = Calendar.getInstance();
			System.out.println("Calender - Time in milliseconds ");
			long timeInMili = calendar.getTimeInMillis();
			System.out.println("Milisecond = "+timeInMili);*/
			
			
			
			
			Calendar calendar = Calendar.getInstance();
	        Random randomObj = new Random();
	        calendar.setTime(new Date());
	        Integer reqRefNoVal = new Integer(Integer.valueOf((int) calendar.getTimeInMillis())); 
	        //String.valueOf(reqRefNoVal);
	        reqRefNo = String.valueOf(calendar.getTimeInMillis());
	        
	        //System.out.println("Random Number  - > "+reqRefNo);

	        reqRefNo = reqRefNo.substring(1,13);
	        
	        //System.out.println("Random Number  - > "+reqRefNo);
	        

	        String randNum1 = String.valueOf(randomObj.nextInt(10));
	        
	        reqRefNo = reqRefNo + randNum1;

	        //System.out.println("Random Number  - > " + randNum1);
	        
	        //System.out.println("Random Number  - > " + reqRefNo);
	        
	        //log.info("Random Number  - > " + reqRefNo);

			
			/*//timeInMili = timeInMili / 1000;
			
			//System.out.println("Seconds = "+timeInMili);
			
			String str = String.valueOf(timeInMili);  
			
			str = str.substring(1,10);	// Take 1 to 10 characters of millisecond
			
			System.out.println("Seconds = "+str);
			
			long threadId = Thread.currentThread().getId();
			
			Random randomObj = new Random(threadId);
            //randomobj.setSeed(threadId);
            
			
			
			String randNum = String.valueOf(randomObj.nextInt(8999)+1000); // this will generate 4 digit random number between 1000 and 9999
            
            
			
			String threadId1 = String.valueOf(Math.abs(randomobj.nextLong()));
            
			
			System.out.println("seconds after padding = "+threadId1.length());
			
			if(threadId1.length() > 4)
			{
				threadId1 = threadId1.substring(threadId1.length()-4,threadId1.length()); 
			}
			System.out.println("seconds after padding = "+threadId1);
			String result = StringUtils.leftPad(threadId1, 4, "0");
			System.out.println("seconds after padding = "+result);
			
			uniqueRef = str + result;
			
			
			uniqueRef = str + randNum;
			
			log.info("Unique Reference Number =>"+uniqueRef);*/
		
		
		}
		catch(Exception e)
		{
			//log.error("Exception in getReqRefNum() - - - "+e);
		}
		
		//log.info("Random Integer value ->  "+reqRefNo);
	    
		return reqRefNo;	    
	}
	
	public static JSONObject esbErrorHandling(JSONObject jsonObjectres,String apiResName){
		
		//log.info("In esbErrorHandling......");
		JSONObject jsonErrorObject=new JSONObject();
		JSONObject jsonSuccessErr=new JSONObject();
		JSONObject jsonObject=jsonObjectres.getJSONObject("result");
		
		String esbResStatus=(String) jsonObject.getJSONObject(apiResName).getJSONObject("ResHdr").getJSONObject("ServiceResponse").get("EsbResStatus");
		
		if(esbResStatus.equals("0")){
			//log.info("Start esbResStatus <<0>> ::");
			jsonSuccessErr.put("ResCode", "0");
			if(jsonObject.getJSONObject(apiResName).containsKey("ResBody"))
			{
				jsonSuccessErr.put("ResBody", (JSONObject) jsonObject.getJSONObject(apiResName).getJSONObject("ResBody"));
			}
			else if(jsonObject.getJSONObject(apiResName).containsKey("SaltUserId"))
			{
				jsonSuccessErr.put("ResBody",jsonObject.getJSONObject(apiResName).get("SaltUserId"));
			}
			else if("ValidateCustInfoRes".equals(apiResName)) 
			{
				
				jsonSuccessErr.put("ResBody",jsonObject.getJSONObject(apiResName));
				jsonSuccessErr.put("Status", "SUCCESS");
			}
			else
			{
				jsonSuccessErr.put("ResBody","SUCCESS");
				if(jsonObject.getJSONObject(apiResName).containsKey("CustomerId"))
				{
					jsonSuccessErr.put("CustomerId",jsonObject.getJSONObject(apiResName).getString("CustomerId"));
				}
				else
				{
					jsonSuccessErr.put("CustomerId","Not Found");
				}
			}
			
			//log.info("End esbResStatus <<0>> ::");
		}
		
		if(esbResStatus.equals("1")){
			
			//log.info("Start esbResStatus <<1>> ::");
			
			if("ValidateDCLoginRes".equals(apiResName))
			{
				JSONArray jsonErrorArr = (JSONArray) jsonObject.getJSONObject(apiResName).getJSONObject("ResHdr").getJSONArray("ErrorDetails"); 
				jsonErrorObject = jsonErrorArr.getJSONObject(0);
			}
			else if("GetCASABalanceRes".equals(apiResName)) 
			{
				if(jsonObject.getJSONObject(apiResName).getJSONObject("ResHdr").toString().contains("[{")) 
				{
					JSONArray jsonErrorArr = (JSONArray) jsonObject.getJSONObject(apiResName).getJSONObject("ResHdr").getJSONArray("ErrorDetails"); 
					jsonErrorObject = jsonErrorArr.getJSONObject(0);
				}
				else 
				{
					jsonErrorObject = jsonObject.getJSONObject(apiResName).getJSONObject("ResHdr").getJSONObject("ErrorDetails");
				}
				
			}else if("UpdateNEFTSIRes".equals(apiResName)) 
			{
				if(jsonObject.getJSONObject(apiResName).getJSONObject("ResHdr").toString().contains("[{")) 
				{
					JSONArray jsonErrorArr = (JSONArray) jsonObject.getJSONObject(apiResName).getJSONObject("ResHdr").getJSONArray("ErrorDetails"); 
					jsonErrorObject = jsonErrorArr.getJSONObject(0);
				}
				else 
				{
					jsonErrorObject = jsonObject.getJSONObject(apiResName).getJSONObject("ResHdr").getJSONObject("ErrorDetails");
				}
				
			}
			else
			{
				jsonErrorObject=(JSONObject) jsonObject.getJSONObject(apiResName).getJSONObject("ResHdr").getJSONArray("ErrorDetails").get(0);
			}
			
			
		//	log.info("ErrorDetails ===");
			jsonSuccessErr.put("ResCode", "1");
			//jsonSuccessErr.put("ErrorDetails", jsonErrorObject.get("HostErrDesc"));
			//changed by prabhat on 12-01-17
			if(" ".equals(jsonErrorObject.getJSONObject("ErrorInfo").get("HostErrDesc")))
			{
				jsonSuccessErr.put("ErrorDetails", "No response found.");
			}
			else if(jsonErrorObject.getJSONObject("ErrorInfo").containsKey("EsbErrLongDesc")) 
			{	
				jsonSuccessErr.put("ErrorDetails", jsonErrorObject.getJSONObject("ErrorInfo").get("EsbErrLongDesc"));
			}else if("ValidateRNBLoginRes".equals(apiResName))
			{
				String apiRes = jsonErrorObject.getJSONObject("ErrorInfo").getString("HostErrDesc");
				if(apiRes.contains("DIGX")){
					apiRes = apiRes.substring(apiRes.indexOf(":"), apiRes.lastIndexOf(":"));
					apiRes =apiRes.substring(1,apiRes.length());
				}
				jsonSuccessErr.put("ErrorDetails", apiRes);
			}else
			{
				if(jsonErrorObject.getJSONObject("ErrorInfo").containsKey("HostErrCode")) {
					if(jsonErrorObject.getJSONObject("ErrorInfo").get("HostErrCode").equals("90178")){
						jsonSuccessErr.put("ResCode", "4");
					}
				}
				jsonSuccessErr.put("ErrorDetails", jsonErrorObject.getJSONObject("ErrorInfo").get("HostErrDesc"));
			}
			
			//log.info("End esbResStatus <<1>> ::");
			
			//return jsonErrorObject;
		
		}else if(esbResStatus.equals("2")){
			
			//log.info("Start esbResStatus <<2>> ::");
			jsonErrorObject= (JSONObject) jsonObject.getJSONObject(apiResName).getJSONObject("ResHdr").getJSONArray("ErrorDetails").get(0);
			//log.info("ErrorDetails ===");
			jsonSuccessErr.put("ResCode", "2");
			//jsonSuccessErr.put("ErrorDetails", jsonErrorObject.get("HostErrDesc"));
			//changed by prabhat on 12-01-17
			/*jsonSuccessErr.put("ErrorDetails", jsonErrorObject.getJSONObject("ErrorInfo").get("HostErrDesc"));
			jsonSuccessErr.put("ResBody", (JSONObject) jsonObject.getJSONObject(apiResName).getJSONObject("ResBody"));
			log.info("End esbResStatus <<2::><<<"+jsonSuccessErr);*/
			
			//changed by prabhat on 15-12-17
				if(jsonErrorObject.getString("ErrorInfo").contains("[{"))
		    	{
					//log.info("In Array Group");
					jsonSuccessErr.put("ErrorDetails", "Database Error :");
					jsonSuccessErr.put("ResBody", (JSONObject) jsonObject.getJSONObject(apiResName).getJSONObject("ResBody"));
					//log.info("End esbResStatus if<<2::><<<");
		    	}
				else
				{
					//log.info("else Array Group");
					//jsonSuccessErr.put("ErrorDetails", jsonErrorObject.get("HostErrDesc"));
					//changed by prabhat on 12-01-17
					
					if(jsonErrorObject.getJSONObject("ErrorInfo").containsKey("EsbErrShortDesc"))
					{
						jsonSuccessErr.put("ErrorDetails", jsonErrorObject.getJSONObject("ErrorInfo").get("EsbErrShortDesc"));
					}
					else if(jsonErrorObject.getJSONObject("ErrorInfo").containsKey("HostErrDesc"))
					{
						jsonSuccessErr.put("ErrorDetails", jsonErrorObject.getJSONObject("ErrorInfo").get("HostErrDesc"));
					}
					else
					{
						jsonSuccessErr.put("ErrorDetails", "Database Error :");
					}
					
					jsonSuccessErr.put("ResBody", (JSONObject) jsonObject.getJSONObject(apiResName).getJSONObject("ResBody"));
					//log.info("End esbResStatus <<2::><<<");
				}
			
			//return jsonSuccessErr;
		}
		return jsonSuccessErr;
		
	}
	
	

	public static String convertBasicString(String request)
	{
		request = request.replaceAll(Pattern.quote("\\"), "");
		request = request.replaceFirst(Pattern.quote("\""),"");
		StringBuffer sb = new StringBuffer(request);
		sb.replace(request.lastIndexOf("\""), request.lastIndexOf("\"") + 1, "");
		
		return sb.toString();
	}
	
	
	
	
	
	
//	public static String generateCRM_PDF(JSONObject jsonResponse,JSONObject caseDtl, String fileName, String tempFileName,String apiResName)
//	{
//		String base64Str = "";
//
//		fileName=ApplicationGenParameter.BULK_BENE_FILE_UPLOAD_PATH+"/PDF/"+fileName;
//		tempFileName=ApplicationGenParameter.BULK_BENE_FILE_UPLOAD_PATH+"/PDF/"+tempFileName;
//		
//		StringBuilder  extraRemark =new StringBuilder();
//		Map<String, String> dataMap = new HashMap<String, String>();
//		StringBuilder  strHeader =new StringBuilder();
//		
//		//get json array for PDF generation from API response details
//		JSONObject jsonData = jsonArrayFromAPI_ReponseDetail(jsonResponse,caseDtl,dataMap,strHeader,extraRemark,apiResName);
//		
//		//Create PDF on specified path from given parameter
//		new FirstPdf(fileName,tempFileName,imgLogoPath,imgTickPath,strHeader.toString(),extraRemark.toString(),jsonData);
//
//		//get bae64data of PDF file at specified path
//		try {
//			File objFile = new File(tempFileName);
//			InputStream in = new FileInputStream(objFile);
//			byte[] fileBytes = IOUtils.toByteArray(in);
//			BASE64Encoder encoder = new BASE64Encoder();
//			base64Str = encoder.encode(fileBytes);
//			in.close();
//		}
//		catch (Exception e) {
//
//			//log.info("Exception =",e);
//		}
//
//		// After base64 generation..... Delete unwanted PDF to avoid server memory full.
//		try {
//			File formatFile = new File(fileName);
//			File tempformatFile = new File(tempFileName);
//
//			if(formatFile.delete())
//			{
//				//log.info("File deleted successfully"+fileName);
//			}
//			else
//			{
//				//log.info("**Failed to delete the file"+fileName);
//			}
//
//			if(tempformatFile.delete())
//			{
//				//log.info("File deleted successfully"+tempFileName); 
//			}
//			else
//			{
//				//log.info("**Failed to delete the file"+tempFileName);
//			}
//
//		}
//		catch (Exception e) {
//			//log.info("Exception Failed to delete =",e);
//		}
//
//		return base64Str;
//	}
	
	
	public static String getNewDateFormatFromStringDate(String strDate)
    {
		SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yy");
		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = inputFormat.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String formattedDate = outputFormat.format(date);
		
		return formattedDate;
    }
	
	
	public static JSONObject beneErrorHandling(JSONObject jsonObjectres,String apiResName)
	{
		JSONObject jsonErrorObject=new JSONObject();
		JSONObject jsonSuccessErr=new JSONObject();
		JSONObject jsonObject=jsonObjectres.getJSONObject("result");
		if(jsonObject.containsKey(apiResName))
		{
			//log.info("Start beneErrorHandling <<0>> ::");
			//jsonErrorObject= (JSONObject) jsonObject.getJSONObject(apiResName).getJSONObject("Fault").getJSONObject("Reason").getJSONObject("Text");
			jsonSuccessErr.put("ResCode", "0");
			jsonSuccessErr.put("ResBody", jsonObject);
			//log.info("End beneErrorHandling <<0>> ::");
		}else if(jsonObject.containsKey("Fault") )
		{
			//log.info("Start beneErrorHandling <<1>> ::");
			jsonErrorObject = (JSONObject) jsonObject.getJSONObject("Fault").getJSONObject("Reason");// Changed By Prabhat 30-01-17
			jsonSuccessErr.put("ResCode", "1");
			jsonSuccessErr.put("ErrorDetails", jsonErrorObject.get("Text"));
			//log.info("End beneErrorHandling <<1>> ::");
		}else{
			return jsonObjectres;
		}
		return jsonSuccessErr;
	}
	
	
	public static JSONObject beneErrorHandling1(JSONObject jsonObjectres,String apiResName)
	{
		JSONObject jsonErrorObject=new JSONObject();
		JSONObject jsonSuccessErr=new JSONObject();
		JSONObject jsonObject=jsonObjectres.getJSONObject("result");
		if(jsonObject.containsKey(apiResName))
		{
			//log.info("Start beneErrorHandling <<0>> ::"); // Changed By 2273 13-07-2020
			//jsonErrorObject= (JSONObject) jsonObject.getJSONObject(apiResName).getJSONObject("Fault").getJSONObject("Reason").getJSONObject("Text");
			jsonSuccessErr.put("ResCode", "0");
			jsonSuccessErr.put("ResBody", jsonObject);
			//log.info("End beneErrorHandling <<0>> ::");
		}else if(jsonObject.containsKey("Fault")){
			//log.info("Start beneErrorHandling <<1>> ::");
			jsonErrorObject = (JSONObject) jsonObject.getJSONObject("Fault").getJSONObject("Reason");// Changed By 2273 08-07-2020
			jsonSuccessErr.put("ResCode", "1");
			if(jsonErrorObject.getString("Text").contains("validationError")){
				String request = jsonErrorObject.getString("Text").substring(6,jsonErrorObject.getString("Text").length());
				//log.info("End beneErrorHandling request ::"+request);
				JSONObject reqJsonfrmAPI = JSONObject.fromObject(request);
				jsonSuccessErr.put("ErrorDetails", reqJsonfrmAPI.getJSONObject("message").getJSONArray("validationError").getJSONObject(0).getString("errorMessage"));
			}else if(jsonErrorObject.getString("Text").contains("relatedMessage")){
				String request = jsonErrorObject.getString("Text").substring(6,jsonErrorObject.getString("Text").length());
				//log.info("End beneErrorHandling request ::"+request);
				JSONObject reqJsonfrmAPI = JSONObject.fromObject(request);
				jsonSuccessErr.put("ErrorDetails", reqJsonfrmAPI.getJSONObject("message").getJSONArray("relatedMessage").getJSONObject(0).getString("detail"));
			}else if(jsonObject.getJSONObject("Fault").containsKey("Code")){
				String request = jsonErrorObject.getString("Text").substring(6,jsonErrorObject.getString("Text").length());//Improper Error Handling App Sec May 2023 by 2459
				//log.info("End beneErrorHandling request ::"+request);
				//*************************************************************************************
				//Exception handling for API provided Text object not in a JSON format - 27-02-2024   
				JSONObject reqJsonfrmAPI = new JSONObject();
				try
				{ 
					reqJsonfrmAPI = JSONObject.fromObject(request);
				} 
				catch (Exception e) 
				{ 
					//log.info("End beneErrorHandling request ::"+jsonErrorObject.getString("Text"));
				}
				
				if(reqJsonfrmAPI.has("message") && reqJsonfrmAPI.getJSONObject("message").has("title")) {
					jsonSuccessErr.put("ErrorDetails", reqJsonfrmAPI.getJSONObject("message").getString("title"));
				}
				else {
					jsonSuccessErr.put("ErrorDetails", "System cannot process the request currently. Please try later.");//Improper Error Handling App Sec May 2023 by 2459
					
				}
				//*************************************************************************************

			}else{
				if(jsonErrorObject.getString("Text").contains("{")){
					//log.info("start beneErrorHandling1 request ::");

					String request = jsonErrorObject.getString("Text").substring(6,jsonErrorObject.getString("Text").length());
					JSONObject reqJsonfrmAPI = JSONObject.fromObject(request);
					jsonSuccessErr.put("ErrorDetails", reqJsonfrmAPI.getString("Text").substring(6,jsonErrorObject.getString("Text").length()));
				//	log.info("End beneErrorHandling1 request ::"+request);
				}else{
					jsonSuccessErr.put("ErrorDetails", "System cannot process the request currently. Please try later.");//Improper Error Handling App Sec May 2023 by 2459
					//log.info("End beneErrorHandling1 request else ::"+jsonSuccessErr);
				}

			}
			//log.info("End beneErrorHandling <<1>> ::"+jsonSuccessErr);
		}else if(jsonObject.containsKey("ErrorDetails")){
			//log.info("Start beneErrorHandling <<1>> ::");
			jsonErrorObject = (JSONObject) jsonObject.getJSONObject("Fault").getJSONObject("Reason");// Changed By 2273 08-07-2020
			jsonSuccessErr.put("ResCode", "1");
			String request = jsonErrorObject.getString("Text").substring(6,jsonErrorObject.getString("Text").length());
			JSONObject reqJsonfrmAPI = JSONObject.fromObject(request);
			jsonSuccessErr.put("ErrorDetails", reqJsonfrmAPI.getJSONObject("message").getString("detail"));
			//log.info("End beneErrorHandling <<1>> ::"+jsonSuccessErr);
		}else{
			return jsonObjectres;
		}
		return jsonSuccessErr;
	}
	
	
	public static JSONObject esbErrorHandlingForNonRnb(JSONObject jsonObjectres,String apiResName)
	{
		JSONObject jsonSuccessErr=new JSONObject();
		JSONObject jsonObject=jsonObjectres.getJSONObject("result");
		
		if(jsonObject.containsKey("statusCode") && (jsonObject.getString("statusCode").equals("0") || jsonObject.getString("statusCode").equals("1")))
		{
			//log.info("Start rnb customer <<0>> ::");
			//jsonErrorObject= (JSONObject) jsonObject.getJSONObject(apiResName).getJSONObject("Fault").getJSONObject("Reason").getJSONObject("Text");
			jsonSuccessErr.put("ResCode", "0");
			jsonSuccessErr.put("ResBody", jsonObject.getString("statusDescription"));
			//log.info("End rnb customer <<0>> ::");
		}else
		{
			//log.info("Start non rnb customer<<1>> ::");
			jsonSuccessErr.put("ResCode", "4");
			jsonSuccessErr.put("ResBody", jsonObject.getString("statusDescription"));
			//log.info("End non rnb customer <<1>> ::");
		}
		return jsonSuccessErr;
	}
		
	
public static JSONObject jsonArrayFromAPI_ReponseDetail(JSONObject jsonResponse,JSONObject caseDtl,Map<String, String> dataMap ,StringBuilder strHeader,StringBuilder extraRemark,String apiResName) {
		
		//log.info("caseDtl "+caseDtl+"  "); 
		net.sf.json.JSONObject dataObj = new JSONObject();
		String  custName = "-",issueType="", strRemark = "-",strBodyMsg="";
		
		try {
			
			
			
			//-------------------TAT_CR------------------------------------------------------------
			JSONObject tatData=new JSONObject();
			CustomerManagementDAO customerManagementDAO=new CustomerManagementDAO();                
			tatData=customerManagementDAO.getTeamAndTurnAroundTime(caseDtl.getString("issueID"));

			String turnAroundTime=tatData.getString("turnaroundTime");
			String caseId=caseDtl.getString("CaseId");
			
			//-------------------End------------------------------------------------------------

			JSONObject jsonObject=jsonResponse.getJSONObject("result");
			String responseDate=(String) jsonObject.getJSONObject(apiResName).getJSONObject("ResHdr").getJSONObject("ServiceResponse").get("EsbResTimeStamp");
			
			
			if(caseId.length()>0 && caseDtl.has("CustomerID")) {

				//issueType = caseDtl.has("issueType") ? caseDtl.getString("issueType"):"";
				issueType="Service Request Transaction Details";
				strHeader.append(issueType);
								
				responseDate = CommonUtility.getFullMonthDateFormatFromStringDate(responseDate);
				
				strBodyMsg = "Below are your "+issueType+" performed on "+responseDate;
				
				custName=caseDtl.has("custName") ? caseDtl.getString("custName"):"";
				strRemark=caseDtl.has("reqquery") ? caseDtl.getString("reqquery"):"";
				
				// Key should be Serial Number separated by Hash(#) and Label of field
				dataMap.put(CRMReceiptHeader.CASEID, caseId);
				dataMap.put(CRMReceiptHeader.REMARK, strRemark);
				dataMap.put(CRMReceiptHeader.STATUS, "SUCCESS");
				dataMap.put(CRMReceiptHeader.TURNAROUNDTIME, turnAroundTime+" Days");//----TAT_CR----
				
				dataObj.put( "DataArray", JSONObject.fromObject(dataMap));
				
				dataObj.put( "responseDate", responseDate+"   ");
				dataObj.put( "strBodyMsg", strBodyMsg);
				dataObj.put( "CustomerID", caseDtl.getString("CustomerID"));
				dataObj.put( "custName", custName);

				extraRemark.append("");
			}

			//{"CustomerID":"999000","caseType":"Technical Queries","issueType":"Unable to view trade summary details","reqquery":"bgh n ","AuthId":"999000","shareKey":"d@dd@IPOHONE143WALI@qwertasd7d8625c73a0d3","reqMode":"Portal","loginMode":"Portal","UserFlag":"A","CaseId":"CE070122517091","custName":"Komal"}  
		}
		catch (Exception e) {
			e.printStackTrace();
			//log.info("e = caseDtl "+e+"  "); 
		}

		return dataObj;
	}
public static String getFullMonthDateFormatFromStringDate(String strDate)
{
	SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM, yyyy");
	Date date = null;
	try {
		date = inputFormat.parse(strDate);
	} catch (ParseException e) {
		e.printStackTrace();
	}
	String formattedDate = outputFormat.format(date);
	
	return formattedDate;
}
	
public interface CRMReceiptHeader  {
	String BENENAME="BENENAME"; 
	String CUSTID="CUSTID"; 
	String DESCRIPTION="DESCRIPTION"; 
	String CASEID="CASEID"; 
	String REMARK="REMARKS"; 
	String TURNAROUNDTIME="turnAroundTime";//-----TAT_CR
	String STATUS="STATUS"; 
}
	
}
