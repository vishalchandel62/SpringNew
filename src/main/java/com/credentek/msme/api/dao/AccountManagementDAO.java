package com.credentek.msme.api.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

import com.credentek.msme.ApplicationGenParm.ApplicationGenParameter;
import com.credentek.msme.database.DaoBase;
import com.credentek.msme.utils.CommonUtility;
import com.credentek.msme.utils.Utils;
import com.google.gson.Gson;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class AccountManagementDAO extends DaoBase {
	public String Authorization = ApplicationGenParameter.Authorization;
	public String SERVICE_TYPE = "false";
	public Date date;
	public SimpleDateFormat sdf;
	
	
public AccountManagementDAO(){
		
		date = new Date();
		sdf = new SimpleDateFormat("yyyy-dd-MM hh:mm:ss");//2001-10-26T21:32:52
		try{
			if(ApplicationGenParameter.parameterMasterList.get("SERVICE_TYPE").trim() != null)
			{
				//log.info("SERVICE_TYPE ==="+SERVICE_TYPE);
				SERVICE_TYPE = ApplicationGenParameter.parameterMasterList.get("SERVICE_TYPE").trim();
			}
			else
			{
				SERVICE_TYPE = "false";
			}
		}catch(Exception e)
		{
			//log.info("Exception==="+e);
		}	
	}
	
	public JSONObject getFDRDProductRates(JSONObject accInfo,String serviceName)
	{
		String baseURL="";
		//log.info(" calling  getFDRDProductRates method <<<<<<<<<<<<<<<<");
		//String Authorization  = "Basic dGVzdGNsaWVudDp0ZXN0QDEyMw==";
		for(String key : ServiceCallAPI.apiURLMasterList.keySet()){
			if(serviceName.toUpperCase().equals(key.trim().toUpperCase())){
				baseURL=ServiceCallAPI.apiURLMasterList.get(key);
				break;
			}
		}
		
		String apiResName="";
		String productType = "";
		String months = "";
		String days = "";
		
		String companyId = "";
		JSONObject m_res_header = new JSONObject();//ResBody
		if(ApplicationGenParameter.companyCustomerMapList.get(accInfo.get("CustomerId")) != null)
		{
			companyId  = ApplicationGenParameter.companyCustomerMapList.get(accInfo.get("CustomerId")).trim();
			if(companyId==null || companyId.trim().equals("")||companyId.trim().isEmpty()){
				if(accInfo.has("MakerId")){
					accInfo.put("MakerId", ApplicationGenParameter.makerAliasMapList.get(accInfo.getString("MakerId")));
					companyId=accInfo.getString("MakerId").split("_")[0];
				}else
				{
					m_res_header.put("ResCode","1");
					m_res_header.put("ErrorDetails","Company Id has not found for this Customer Id.");
					
					return m_res_header;
				}
			}
		}
		else
		{
			m_res_header.put("ResCode","1");
			m_res_header.put("ErrorDetails","Company Id has not found for this Customer Id.");
			
			return m_res_header;
		}
		
		
		Gson g3=new Gson();
		Gson gson1=new Gson();
		
		LinkedHashMap<String,Object> main_req_header = new LinkedHashMap<String,Object>();
		LinkedHashMap<String,Object> sub_header = new LinkedHashMap<String,Object>();
		LinkedHashMap<String,Object> s1_header = new LinkedHashMap<String,Object>();
		LinkedHashMap<String,Object> s11_header = new LinkedHashMap<String,Object>();
		LinkedHashMap<String,Object> s12_header = new LinkedHashMap<String,Object>();
		LinkedHashMap<String,Object> s2_header = new LinkedHashMap<String,Object>();
		LinkedHashMap<String,Object> s21_header = new LinkedHashMap<String,Object>();
		
		if("FD".equals(accInfo.getString("ProductType")))
		{
			productType = "TD";
		}
		else
		{
			productType = "RD";
		}
		
		if(accInfo.containsKey("Months"))
		{
			months = accInfo.getString("Months");
		}
		else
		{
			months = "12";
		}
//		Days
		if(accInfo.containsKey("Days"))
		{
			days = accInfo.getString("Days");
		}
		else
		{
			days = "1";
		}
		
		
		s11_header.put("RequesterID", ServiceCallAPI.serverRequesterID);
		//s1_header.put("ConsumerContext", s11_header);
		s1_header.put("ConsumerContext", gson1.fromJson(g3.toJson(s11_header), s11_header.getClass()));
		
		s12_header.put("ServiceName", "AccountManagement");
		s12_header.put("ReqRefNum",CommonUtility.getReqRefNum());
		s12_header.put("ReqRefTimeStamp",CommonUtility.getCurrentTimeStamp());
		s12_header.put("ServiceVersionNo", "1.0");
		
		s1_header.put("ServiceContext", gson1.fromJson(g3.toJson(s12_header), s12_header.getClass()));
		
		//sub_header.put("ReqHdr", s1_header);
		sub_header.put("ReqHdr", gson1.fromJson(g3.toJson(s1_header), s1_header.getClass()));
		
		s21_header.put("Months",months);
		s21_header.put("Days",days);
		
		s2_header.put("CustomerId", companyId);
		s2_header.put("TermPeriod", gson1.fromJson(g3.toJson(s21_header), s21_header.getClass()));
		s2_header.put("ProductType", productType);
		
		//sub_header.put("ReqBody", s2_header);
		sub_header.put("ReqBody", gson1.fromJson(g3.toJson(s2_header), s2_header.getClass()));
		
		//main_req_header.put("GetAllAccountsReq", sub_header);
		main_req_header.put("GetFDRDProductRatesReq", gson1.fromJson(g3.toJson(sub_header), sub_header.getClass()));
		
		String request = g3.toJson(gson1.toJson(main_req_header,main_req_header.getClass()));
		
		//change by yesh
		//String request = "{"+"\"GetFDRDProductRatesReq\":{\"ReqHdr\":{\"ConsumerContext\":{\"RequesterID\":\"WRK\"},\"ServiceContext\":{\"ServiceName\":\"AccountManagement\",\"ReqRefNum\":\""+CommonUtility.getReqRefNum()+"\",\"ReqRefTimeStamp\":\""+CommonUtility.getCurrentTimeStamp()+"\",\"ServiceVersionNo\":\"1.0\"}},\"ReqBody\":{\"CustomerId\":\"3078\",\"TermPeriod\":{\"Months\":\"12\"},\"ProductType\":\"RD\"}}}";
		
		//log.info("request Header ===");
		request = CommonUtility.convertBasicString(request);
		//log.info("request Header After===");
		String reqfrmAPI = "";
		JSONObject reqJsonfrmAPI = new JSONObject();
		//JSONObject m_res_header = new JSONObject();//ResBody
		
		if(SERVICE_TYPE.equals("true"))
		{
			try 
			{			
				reqfrmAPI = Utils.DataFromService(baseURL, request ,Authorization);//change by yesh
				//log.info("GetFDRDProductRatesRes String Response<<<<<");
				
				reqJsonfrmAPI = JSONObject.fromObject(reqfrmAPI);//change by yesh
				//log.info("GetFDRDProductRatesRes json<<<<<<<<<<");//change by yesh
				if(reqJsonfrmAPI.containsKey("ErrorDetails"))
				{
					return reqJsonfrmAPI;
				}
				
				apiResName = "GetFDRDProductRatesRes" ;
				m_res_header=CommonUtility.esbErrorHandling(reqJsonfrmAPI,apiResName);
				
				//log.info("GetFDRDProductRates json Res Body<<<<<<<<<<");
				if(m_res_header.getString("ResCode").equals("1") && m_res_header.getString("ErrorDetails").equals("No response found."))
				{
					m_res_header.put("ErrorDetails", "No rates found for selected tenure.");
				}
				
			} catch (JSONException e) 
			{
				//log.info("JSONException---",e);			
			} catch (Exception e) {
				//log.info("Exception---",e);
			}
			
			
			return m_res_header;
			
		}
		else
		{

			//log.info("If Service Type FALSE");
			
			
			if(productType.equals("TD"))
			{
		        JSONObject ss_res_header2 = new JSONObject();
		        JSONObject temp = new JSONObject();
		        JSONObject temp1 = new JSONObject();
		        JSONObject temp2 = new JSONObject();
		        JSONArray tempA = new JSONArray();
		        JSONArray tempA1 = new JSONArray();
				temp.put("ProductCode", "405");
				temp.put("ProductName", "FD - REINVESTMENT STAFF");
				//temp.put("FDProductSlabDetail", "");
				temp1.put("Months", "12");
				temp1.put("Days", "0");
				temp2.put("MinTermPeriod", temp1);
				temp1.clear();
				temp1.put("Months", "12");
				temp1.put("Days", "9");
				temp2.put("MaxTermPeriod", temp1);
				temp2.put("InterestRate", "8.5");
				temp2.put("MinAmount", "1");
				temp2.put("MaxAmount", "10000000000");
				tempA.add(temp2);
				temp.put("FDProductSlabDetail", tempA);
				
				
				tempA1.add(temp);
				
				temp.clear();
				temp1.clear();
				temp2.clear();
				tempA.clear();
				temp.put("ProductCode", "440");
				temp.put("ProductName", "FD - PAYOUT STAFF");
				temp.put("FDProductSlabDetail", "");
				temp1.put("Months", "11");
				temp1.put("Days", "2");
				temp2.put("MinTermPeriod", temp1);
				temp1.clear();
				temp1.put("Months", "1");
				temp1.put("Days", "8");
				temp2.put("MaxTermPeriod", temp1);
				temp2.put("InterestRate", "7.5");
				temp2.put("MinAmount", "1");
				temp2.put("MaxAmount", "10000000000");
				tempA.add(temp2);
				temp.put("FDProductSlabDetail", tempA);
				temp1.clear();
				temp2.clear();
				temp1.put("Months", "15");
				temp1.put("Days", "2");
				temp2.put("MinTermPeriod", temp1);
				temp1.clear();
				temp1.put("Months", "15");
				temp1.put("Days", "8");
				temp2.put("MaxTermPeriod", temp1);
				temp2.put("InterestRate", "6.5");
				temp2.put("MinAmount", "1");
				temp2.put("MaxAmount", "10000000000");
				tempA.add(temp2);
				temp1.clear();
				temp2.clear();
				temp1.put("Months", "18");
				temp1.put("Days", "2");
				temp2.put("MinTermPeriod", temp1);
				temp1.clear();
				temp1.put("Months", "18");
				temp1.put("Days", "8");
				temp2.put("MaxTermPeriod", temp1);
				temp2.put("InterestRate", "9.5");
				temp2.put("MinAmount", "1");
				temp2.put("MaxAmount", "10000000000");
				tempA.add(temp2);
				temp.put("FDProductSlabDetail", tempA);
				
				temp1.clear();
				temp2.clear();
				temp.put("ProductCode", "440");
				temp.put("ProductName", "FD - PAYOUT STAFF");
				//temp.put("FDProductSlabDetail", "");
				temp1.put("Months", "14");
				temp1.put("Days", "0");
				temp2.put("MinTermPeriod", temp1);
				temp1.clear();
				temp1.put("Months", "16");
				temp1.put("Days", "8");
				temp2.put("MaxTermPeriod", temp1);
				temp2.put("InterestRate", "7.5");
				temp2.put("MinAmount", "1");
				temp2.put("MaxAmount", "10000000000");
				tempA.add(temp2);
				temp.put("FDProductSlabDetail", tempA);
				
				tempA1.add(temp);
				
				temp1.clear();
				temp2.clear();
				temp.put("ProductCode", "444");
				temp.put("ProductName", "FD - REINVESTMENT STAFF FOR 411");
				//temp.put("FDProductSlabDetail", "");
				temp1.put("Months", "14");
				temp1.put("Days", "0");
				temp2.put("MinTermPeriod", temp1);
				temp1.clear();
				temp1.put("Months", "14");
				temp1.put("Days", "9");
				temp2.put("MaxTermPeriod", temp1);
				temp2.put("InterestRate", "6.5");
				temp2.put("MinAmount", "1");
				temp2.put("MaxAmount", "10000000000");
				tempA.add(temp2);
				temp.put("FDProductSlabDetail", tempA);
				
				tempA1.add(temp);
				
				ss_res_header2.put("Product", tempA1);
				 m_res_header.put("ResCode", "0");
			        m_res_header.put("ResBody", ss_res_header2);
			        
			       /* m_res_header.put("ResCode", "1");
			        m_res_header.put("ErrorDetails", "No response found.");*/
			        
			        
			        if(m_res_header.getString("ResCode").equals("1") && m_res_header.getString("ErrorDetails").equals("No response found."))
					{
						m_res_header.put("ErrorDetails", "No rates found for selected tenure.");
					}
					return m_res_header;
			}
			else
			{

			 	JSONObject sss_res_header1 = new JSONObject();
		        JSONObject sss_res_header2 = new JSONObject();
		        JSONObject ss_res_header2 = new JSONObject();
		        JSONObject[] ss_res_header1 = new JSONObject[2];
		        //JSONObject m_res_header = new JSONObject();
		       
		        sss_res_header1.put("ProductCode", "929");
		        sss_res_header1.put("ProductName", "RD-Senior Citizens");
		        sss_res_header1.put("InterestRate", "8.0");
		        sss_res_header1.put("TermPeriod", "12");
		       
		        sss_res_header2.put("ProductCode", "928");
		        sss_res_header2.put("ProductName", "RD-Resident Citizens");
		        sss_res_header2.put("InterestRate", "7.5");
		        sss_res_header2.put("TermPeriod", "8");
		       
		        ss_res_header1[0]=sss_res_header1;
		        ss_res_header1[1]=sss_res_header2;
		       
		       
		        ss_res_header2.put("Product", ss_res_header1);
		        m_res_header.put("ResCode", "0");
		        m_res_header.put("ResBody", ss_res_header2);
				return m_res_header;
			}
			
			
		
			
		}
		
	}
	

}
