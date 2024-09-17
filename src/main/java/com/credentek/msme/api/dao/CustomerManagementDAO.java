package com.credentek.msme.api.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.logging.log4j.LogManager;


import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.credentek.msme.ApplicationGenParm.ApplicationGenParameter;
import com.credentek.msme.database.DaoBase;
import com.credentek.msme.logincontroller.LoginController;
import com.credentek.msme.logindao.LoginDaoImpl;
import com.credentek.msme.loginmodel.UserLogin;
import com.credentek.msme.maintenance.dao.UserLoginDaoImpl;
import com.credentek.msme.utils.CommonUtility;
import com.credentek.msme.utils.GeneralService;
import com.credentek.msme.utils.PathRead;
import com.credentek.msme.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

public class CustomerManagementDAO extends DaoBase{
	
	private static final Logger log = LogManager.getLogger(CustomerManagementDAO.class);
	//Service_Type="true" for yesbank
	@Autowired
	LoginDaoImpl LoginDaoImpl;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private UserLoginDaoImpl loginDaoimpl;
	
	@Autowired
	private GeneralService generalService;
	

	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	
	public String currentts = "";
	public JSONObject reqJsonfrmAPI = null;
	public String baseURL = "";
	public String Authorization = ApplicationGenParameter.Authorization;
	public String SERVICE_TYPE = ApplicationGenParameter.SERVICE_TYPE;
	public Date date = ApplicationGenParameter.date;
	public SimpleDateFormat sdf = ApplicationGenParameter.sdf;
	public static PathRead path = PathRead.getInstance();
	private PreparedStatement pstmt = null, pstmt1 = null , pstmt2 = null , pstmt3 = null,pstmt5 = null;
	private ResultSet rset = null, rset1 = null, rset2 = null, rset3 = null,rset5 = null;
	public Base64.Decoder decoder = Base64.getDecoder(); 

	public JSONObject GetAllAccountsReq(String custId, String serviceName) {

		log.info(" calling  GetAllAccountsReq method  ");
		for (String key : ServiceCallAPI.apiURLMasterList.keySet()) {
			if (serviceName.toUpperCase().equals(key.trim().toUpperCase())) {
				baseURL = ServiceCallAPI.apiURLMasterList.get(key);
				break;
			}
		}
		
		Connection conn = null;
		String companyId = custId;
		JSONObject m_res_header = new JSONObject();
		accountDetailsForOnlineOdDao accDaoObj = null;

		Gson g3=new Gson();
		Gson gson1=new Gson();
		
		LinkedHashMap<String, Object> main_req_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> sub_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> s1_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> s11_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> s12_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> s2_header = new LinkedHashMap<String, Object>();

		s11_header.put("RequesterID", ServiceCallAPI.serverRequesterID);
		// s1_header.put("ConsumerContext", s11_header);
		s1_header.put("ConsumerContext", gson1.fromJson(g3.toJson(s11_header), s11_header.getClass()));

		s12_header.put("ServiceName", "CustomerManagement");
		s12_header.put("ReqRefNum", CommonUtility.getReqRefNum());
		s12_header.put("ReqRefTimeStamp", CommonUtility.getCurrentTimeStamp());
		s12_header.put("ServiceVersionNo", "1.0");

		s1_header.put("ServiceContext", gson1.fromJson(g3.toJson(s12_header), s12_header.getClass()));
		
		//sub_header.put("ReqHdr", s1_header);
		sub_header.put("ReqHdr", gson1.fromJson(g3.toJson(s1_header), s1_header.getClass()));
		
		s2_header.put("CustomerId", companyId);
		s2_header.put("GetSavingsAcc", true);
		s2_header.put("GetCurAcc", true);
		s2_header.put("GetFDAcc", true);
		s2_header.put("GetRDAcc", true);
		
		//sub_header.put("ReqBody", s2_header);
		sub_header.put("ReqBody", gson1.fromJson(g3.toJson(s2_header), s2_header.getClass()));
		
		//main_req_header.put("GetAllAccountsReq", sub_header);
		main_req_header.put("GetAllAccountsReq", gson1.fromJson(g3.toJson(sub_header), sub_header.getClass()));

		String request = g3.toJson(gson1.toJson(main_req_header, main_req_header.getClass()));

		 log.info("Response Header Before ==="+request);
		request = CommonUtility.convertBasicString(request);
		log.info("Response Header After==="+request);

		String reqfrmAPI = "" , apiResName = "";
		JSONObject reqJsonfrmAPI = null;
		//JSONObject m_res_header = new JSONObject();//ResBody

		if(SERVICE_TYPE.equals("true")){
			//log.info("If Service Type TRUE");

			// Go With API
			// log.info(" --> Calling ");

			// baseURL = ServiceCallAPI.ServiceURL+"/CustomerManagement/GetAllAccounts";
			// baseURL = ServiceCallAPI.ServiceURL+"/CustomerManagement/GetAllAccounts";
			// log.info("baseURL ==="+baseURL);
			
			try 
			{			
				reqfrmAPI = Utils.DataFromService(baseURL, request ,Authorization);//change by yesh
				//log.info("GetAllAccountsRes String Response<<<<<"+reqfrmAPI);
				reqJsonfrmAPI = JSONObject.fromObject(reqfrmAPI);//change by yesh
				//log.info("GetAllAccountsRes json<<<<<<<<<<"+reqJsonfrmAPI);//change by yesh
				if(reqJsonfrmAPI.containsKey("ErrorDetails"))
				{
					/*modifyJson.put("Status", "Failure");
					m_res_header.put("ResBody", reqJsonfrmAPI);
					*/
					return reqJsonfrmAPI;
				}
				apiResName = "GetAllAccountsRes";
				m_res_header=CommonUtility.esbErrorHandling(reqJsonfrmAPI,apiResName);
				
				
			} catch (JSONException e) 
			{
				log.info("JSONException---",e);			
			} catch (Exception e) {
				log.info("Exception---",e);
			}

			try {
				JSONArray AccountTypeList = new JSONArray();
				JSONObject accountDataAsset = new JSONObject();
				accountDataAsset.put("accountType", "AOD");
				accountDataAsset.put("AccountDetails", new JSONArray());
				accountDataAsset.put("accountTypeDetails", new JSONArray());

				JSONObject accountDataLib = new JSONObject();
				accountDataLib.put("accountType", "LOD");
				accountDataLib.put("AccountDetails", new JSONArray());
				accountDataLib.put("accountTypeDetails",new JSONArray());

				JSONObject accountDataEefc = new JSONObject();
				accountDataEefc.put("accountType", "EEFC");
				accountDataEefc.put("AccountDetails", new JSONArray());
				accountDataEefc.put("accountTypeDetails", new JSONArray());

				m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").add(accountDataAsset);
				m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").add(accountDataLib);
				m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").add(accountDataEefc);

				JSONArray assetOdLimit = new JSONArray();
				JSONArray libOdLimit = new JSONArray();
				JSONArray eefcLimit = new JSONArray();
				JSONArray caAccountArrayUpdated = new JSONArray();
				log.info("check...........");
				AccountTypeList = m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList");
				int sizeOfAccTypList = AccountTypeList.size();
				
				conn = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
				
				for(int i = 0;i<sizeOfAccTypList;i++) {
					if(AccountTypeList.getJSONObject(i).getString("accountType").equals("CA")){
						JSONArray caAccountArray = new JSONArray();
						caAccountArray = AccountTypeList.getJSONObject(i).getJSONArray("AccountDetails");
						int sizeOfAccArra = caAccountArray.size();

						for (int j = 0; j < sizeOfAccArra; j++) {
							boolean currentAccountFlag = false;
							log.info("accountNo.....");
							String accountNo = caAccountArray.getJSONObject(j).getString("AccountNumber");
							JSONObject odObj = m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").getJSONObject(i).getJSONArray("AccountDetails").getJSONObject(j);
							pstmt5 = conn.prepareStatement(getQuery("ACCOUNT_EXPIRY"));                      // Date check
							pstmt5.setString(1, accountNo.trim());
							rset5 = pstmt5.executeQuery();
							if (rset5.next() && rset5.getInt(1)>0) {
								//SELECT AMT_OD_LIMIT,BAL_AVAILABLE,TRIM(NVL(COD_LINE_NO,'NA')) AS COD_LINE_NO,COD_CCY FROM VW_CH_ACCT_MAST 
								//WHERE FLG_MNT_STATUS='A' AND COD_ACCT_NO = cast(? as char(48)) GROUP BY AMT_OD_LIMIT,BAL_AVAILABLE,COD_LINE_NO,COD_CCY
								pstmt = conn.prepareStatement(getQuery("FETCH_ACCOUNT_DTL_VW_CH_ACCT_MAST"));
								pstmt.setString(1, accountNo.trim());
								rset = pstmt.executeQuery();
								if (rset.next()) {
									if (rset.getString(3).equals("NA") && rset.getInt(4) == 1) {
										odObj.put("limitAmount", rset.getString(1));
										if (odObj.getString("Balance").contains("-")) {
											String bal = odObj.getString("Balance");
											bal = bal.substring(1, bal.length());
											BigDecimal fcrLimit, apiBal, fcrUtilization;                     //FCR condition 1
											fcrLimit = new BigDecimal(rset.getString(1));
											fcrUtilization = new BigDecimal(bal);
											apiBal = fcrLimit.subtract(fcrUtilization);
											odObj.put("balanceAvailable", apiBal);
											odObj.put("utilization", bal);
										}else if(odObj.getInt("Balance")>0) {
											BigDecimal fcrLimit , apiBal, fcrBal;
											fcrLimit = new BigDecimal(rset.getString(1));
											String bal = odObj.getString("Balance");                          //FCR condition 2
											apiBal = new BigDecimal(bal);
											fcrBal = fcrLimit.add(apiBal);
											odObj.put("balanceAvailable", fcrBal);
											odObj.put("utilization", "0.00");
										}else {
											BigDecimal fcrLimit , apiBal, fcrBal;
											fcrLimit = new BigDecimal(rset.getString(1));
											String bal = odObj.getString("Balance");
											apiBal = new BigDecimal(bal);                                     //FCR condition 3
											fcrBal = fcrLimit.add(apiBal);
											odObj.put("balanceAvailable", fcrBal);
											odObj.put("utilization", "0.00");
										}
										odObj.put("isChild", false);
									
									}else if(!(rset.getString(3).equals("NA")) && rset.getInt(4)==1)
									{    //          1              2          3             4                            5                    6
										//SELECT DESCRIPTION,LIMIT_AMOUNT,UTILISATION,AVAILABLE_AMOUNT,NVL(MAIN_LINE,'NA') AS MAIN_LINE,AVAILABILITY_FLAG FROM VW_LIMITS 
										//WHERE TRIM(LINE_ID) = TRIM(?) and CUSTOMER_NO = ?
										pstmt3 = conn.prepareStatement(getQuery("GET_BALANCE"));
										pstmt3.setString(1, rset.getString(3).trim());
										pstmt3.setString(2, companyId);
										rset3 = pstmt3.executeQuery();
										if(rset3.next()) {
											if(!"N".equals(rset3.getString(6).trim())) {
												if(!"NA".equals(rset3.getString(5))) {
													accDaoObj = new accountDetailsForOnlineOdDaoImpl();
													JSONObject l_childAndStandAloneDtl = accDaoObj.fetchAccountDetailsForOd(accountNo,companyId,rset.getString(3).trim(),rset3.getString(5).trim(),rset.getString(2));
													log.info("l_childAndStandAloneDtl...");												
													if(l_childAndStandAloneDtl.has("pflag") && l_childAndStandAloneDtl.getString("pflag").equals("false")) {
														log.info("if parent availibility flag = N");
														caAccountArrayUpdated.add(m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").getJSONObject(i).getJSONArray("AccountDetails").getJSONObject(j));
													}else if(l_childAndStandAloneDtl.has("cflag") && l_childAndStandAloneDtl.getString("cflag").equals("false")){
														log.info("if child availibility flag = N");
														caAccountArrayUpdated.add(m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").getJSONObject(i).getJSONArray("AccountDetails").getJSONObject(j));
													}else {
													
														log.info("if parent and child availibility flag = Y ");
														odObj.put("childData", l_childAndStandAloneDtl);
														odObj.put("isChild", true);
													}
												}else {
													log.info("in standalone fcc ");
													// FCC Standalone Condition
							
													Double balAvailFromAcctMastD;
													balAvailFromAcctMastD = rset.getDouble(2);
													BigDecimal availLimit, availBal, availUtil, balAvailFromAcctMast;
													availLimit = new BigDecimal(rset3.getString(2));
													availUtil = new BigDecimal(rset3.getString(3));
													balAvailFromAcctMast = new BigDecimal(rset.getString(2));
													if (balAvailFromAcctMastD <= 0.0) {
														odObj.put("limitAmount", availLimit);
														odObj.put("utilization", availUtil);
														availBal = availLimit.subtract(availUtil);
														String avalVal = availBal.toString();
														if (avalVal.contains("-")) {
															odObj.put("balanceAvailable", avalVal.substring(1, avalVal.length()));
														} else {
															odObj.put("balanceAvailable", avalVal);
														}
													} else if (balAvailFromAcctMastD > 0.0) {
														BigDecimal totalBal;
														odObj.put("limitAmount", availLimit);
														odObj.put("utilization", availUtil);
														availBal = availLimit.subtract(availUtil);
														totalBal = availBal.add(balAvailFromAcctMast);
														String avalVal = totalBal.toString();
														if (avalVal.contains("-")) {
															odObj.put("balanceAvailable", avalVal.substring(1, avalVal.length()));
														} else {
															odObj.put("balanceAvailable", avalVal);
														}
													}
													odObj.put("isChild", false);
												}
											} else {
												currentAccountFlag = true;
												log.info("in availibility flag = N of line ID");
											}
										} else {
											currentAccountFlag = true;
											log.info("if vw_limit date_end expired");
											caAccountArrayUpdated.add(odObj);
										}
										rset3.close();
										pstmt3.close();
									}
									if (!currentAccountFlag) {
										if (rset.getDouble(1) > 0.00 && rset.getDouble(2)>= 0.00 && rset.getInt(4)==1) {
											assetOdLimit.add(odObj);
										} else if (rset.getDouble(1) > 0.00 && rset.getDouble(2) < 0.00 && rset.getInt(4) == 1) {
											libOdLimit.add(odObj);
										} else if (rset.getInt(4) != 1) {
											eefcLimit.add(odObj);
										} else {
											log.info("if amt_od_limit = 0 ,bal_available = 0, api bal=0");
											caAccountArrayUpdated.add(odObj);
										}
									} else {
										log.info("if vw_limit date_end expired");
										caAccountArrayUpdated.add(m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").getJSONObject(i).getJSONArray("AccountDetails").getJSONObject(j));
									}
								}
								rset.close();
								pstmt.close();
							} else {
								log.info("if date expired in vw-ch-od-limit.......");
								caAccountArrayUpdated.add(m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").getJSONObject(i).getJSONArray("AccountDetails").getJSONObject(j));
							}
							m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").getJSONObject(i).getJSONArray("AccountDetails").getJSONObject(j).clear();
							
							rset5.close();
							pstmt5.close();
						}
					}
				}

				AccountTypeList = m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList");
				sizeOfAccTypList = AccountTypeList.size();
				for(int i = 0;i<sizeOfAccTypList;i++) {
					if(AccountTypeList.getJSONObject(i).getString("accountType").equals("AOD")){
						m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").getJSONObject(i).put("AccountDetails",assetOdLimit);
						m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").getJSONObject(i).put("accountTypeDetails",assetOdLimit);
					}
					else if(AccountTypeList.getJSONObject(i).getString("accountType").equals("LOD")){
						m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").getJSONObject(i).put("AccountDetails",libOdLimit);
						m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").getJSONObject(i).put("accountTypeDetails",libOdLimit);
					}
					else if(AccountTypeList.getJSONObject(i).getString("accountType").equals("EEFC")){
						m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").getJSONObject(i).put("AccountDetails",eefcLimit);
						m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").getJSONObject(i).put("accountTypeDetails",eefcLimit);
					}else if(AccountTypeList.getJSONObject(i).getString("accountType").equals("CA")){
						m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").getJSONObject(i).put("AccountDetails",caAccountArrayUpdated);
						m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").getJSONObject(i).put("accountTypeDetails",caAccountArrayUpdated);
					}
				}
				
				
			} catch(Exception e)
			{
				log.info("Exception :::",e);
			} finally {

				try {
					if (rset3 != null) { //rset3,rset,rset5,pstmt3,pstmt,pstmt5c lose in finally-2299
						rset3.close();
					}
					if (pstmt3 != null) {
						pstmt3.close();
					}
					if (rset != null) {
						rset.close();
					}
					if (pstmt != null) {
						pstmt.close();
					}
					if (rset5 != null) {
						rset5.close();
					}
					if (pstmt5 != null) {
						pstmt5.close();
					}
					
					if (conn != null ) {
						closeConnection(conn,new Object(){}.getClass().getEnclosingMethod().getName().toString());
					}
				} catch (SQLException e) {
					log.info("SQLException ===", e);
				}
			}
			log.info("end result........");
			return m_res_header;
		}
		else
		{
			log.info("If Service Type FALSE");
			JSONObject ss_res_header = new JSONObject();//accountType-FD
			JSONObject ss_res_header1 = new JSONObject();//accountType-SA
			JSONObject ss_res_header11 = new JSONObject();//accountType-CA
			JSONObject ss_res_header222 = new JSONObject();
			
			JSONObject ss_res_header3 = new JSONObject();//accountTypeDetails
			
			JSONObject sss_res_header1 = new JSONObject();//AccountNumber,SecondaryAccountHolderName,RequiredQuaterlyBalance
			//CurrentQuaterlyBalance,MMID,IFSCCode,NomineeName
			JSONObject sss_res_header2 = new JSONObject();
			JSONObject sss_res_header4 = new JSONObject();//AccountNumber,SecondaryAccountHolderName,RequiredQuaterlyBalance
			//CurrentQuaterlyBalance,MMID,IFSCCode,NomineeName
			JSONObject sss_res_header5 = new JSONObject();
			JSONObject sss_res_header6 = new JSONObject();
			JSONObject sss_res_header7 = new JSONObject();
			JSONObject sss_res_header8 = new JSONObject();
			JSONObject sss_res_header9 = new JSONObject();
			JSONObject sss_res_header10 = new JSONObject();
			
			JSONObject[] ss_res_header4 = new JSONObject[2];//For AccountDetails,FD
			JSONObject[] ss_res_header5 = new JSONObject[5];//For AccountDetails,SA
			JSONObject[] ss_res_header51 = new JSONObject[2];//For AccountDetails,CA
			JSONObject[] ss_res_header6 = new JSONObject[4];//FOR accountType,AccountDetails      ss_res_header5
			JSONArray ss_res_header81 = new JSONArray();
			
			//000340600000192, 000381311111318, 000381311111316, 000140300001602, 000381311111317 , 000381311230419
			
			sss_res_header1.put("AccountNumber", "000340600000192");
			sss_res_header1.put("Balance", "2000052.99");
			sss_res_header1.put("AccountCustomerRelationship", "SOW");
			sss_res_header1.put("BranchCode", "1");
			sss_res_header1.put("ProductCode", "281");

			sss_res_header2.put("AccountNumber", "000140104543995");
			sss_res_header2.put("Balance", "3000000.23");
			sss_res_header2.put("AccountCustomerRelationship", "SOW");
			sss_res_header2.put("BranchCode", "1");
			sss_res_header2.put("ProductCode", "404");
			
			/*sss_res_header3.put("AccountNumber", "000140104543996");
			sss_res_header3.put("Balance", "400000");
			sss_res_header3.put("AccountCustomerRelationship", "SOW");
			sss_res_header3.put("BranchCode", "1");
			sss_res_header3.put("ProductCode", "404");*/
			ss_res_header4[0]=sss_res_header1;
			ss_res_header4[1]=sss_res_header2;
			//ss_res_header4[2]=sss_res_header3;
			ss_res_header.put("accountType", "FD");
			ss_res_header.put("AccountDetails",ss_res_header4);
			ss_res_header.put("accountTypeDetails",ss_res_header4);
			
			
			
			sss_res_header4.put("AccountNumber", "000381311230419");
			sss_res_header4.put("Balance", "10000000");
			sss_res_header4.put("AccountCustomerRelationship", "SOW");
			sss_res_header4.put("MaturityDate", "1800-01-01");
			sss_res_header4.put("ProductName", "SAVINGS ACCOUNT - STAFF");
			sss_res_header4.put("NomineeName", "SUHAS PANDURANG CHAVAN");
			sss_res_header4.put("InstallmentAmount", "0");
			sss_res_header4.put("InterestRate", "0");
			sss_res_header4.put("MaturityAmount", "0");
			sss_res_header4.put("TermPeriod", "0");
			sss_res_header4.put("AccountStatus", "8");
			sss_res_header4.put("BranchCode", "1");
			sss_res_header4.put("ProductCode", "906");
			sss_res_header4.put("BookBalance", "49000");
			sss_res_header4.put("OpenDate", "2005-10-03");

			sss_res_header5.put("AccountNumber", "000190600002356");
			sss_res_header5.put("Balance", "3556000");
			sss_res_header5.put("AccountCustomerRelationship", "JOO");
			sss_res_header5.put("MaturityDate", "1800-01-01");
			sss_res_header5.put("ProductName", "SAVINGS ACCOUNT - STAFF");
			sss_res_header5.put("NomineeName", "");
			sss_res_header5.put("InstallmentAmount", "0");
			sss_res_header5.put("InterestRate", "0");
			sss_res_header5.put("MaturityAmount", "0");
			sss_res_header5.put("TermPeriod", "0");
			sss_res_header5.put("AccountStatus", "8");
			sss_res_header5.put("BranchCode", "1");
			sss_res_header5.put("ProductCode", "906");
			sss_res_header5.put("BookBalance", "4900.20");
			sss_res_header5.put("OpenDate", "2010-10-03");


			
			sss_res_header9.put("AccountNumber", "000340600001221");
			sss_res_header9.put("Balance", "4000000");
			sss_res_header9.put("AccountCustomerRelationship", "JOO");
			sss_res_header9.put("MaturityDate", "1800-01-01");
			sss_res_header9.put("ProductName", "SAVINGS ACCOUNT - STAFF");
			sss_res_header9.put("NomineeName", "");
			sss_res_header9.put("InstallmentAmount", "0");
			sss_res_header9.put("InterestRate", "0");
			sss_res_header9.put("MaturityAmount", "0");
			sss_res_header9.put("TermPeriod", "0");
			sss_res_header9.put("AccountStatus", "4");
			sss_res_header9.put("BranchCode", "1");
			sss_res_header9.put("ProductCode", "906");
			sss_res_header9.put("BookBalance", "4900.20");
			sss_res_header9.put("OpenDate", "2006-10-03");
			
			sss_res_header10.put("AccountNumber", "000340600001232");
			sss_res_header10.put("Balance", "4000000");
			sss_res_header10.put("AccountCustomerRelationship", "JOO");
			sss_res_header10.put("MaturityDate", "1800-01-01");
			sss_res_header10.put("ProductName", "SAVINGS ACCOUNT - STAFF");
			sss_res_header10.put("NomineeName", "");
			sss_res_header10.put("InstallmentAmount", "0");
			sss_res_header10.put("InterestRate", "0");
			sss_res_header10.put("MaturityAmount", "0");
			sss_res_header10.put("TermPeriod", "0");
			sss_res_header10.put("AccountStatus", "4");
			sss_res_header10.put("BranchCode", "1");
			sss_res_header10.put("ProductCode", "906");
			sss_res_header10.put("BookBalance", "4900.20");
			sss_res_header10.put("OpenDate", "2006-10-03");
			
			
			ss_res_header5[0]=sss_res_header4;
			ss_res_header5[1]=sss_res_header5;
			ss_res_header5[2]=sss_res_header8;
			ss_res_header5[3]=sss_res_header9;
			ss_res_header5[4]=sss_res_header10;
			ss_res_header1.put("accountType", "SA");
			ss_res_header1.put("AccountDetails",ss_res_header5);
			ss_res_header1.put("accountTypeDetails",ss_res_header5);
			
			
			String sql = "select BAL_AVAILABLE,COD_ACCT_NO,BAL_BOOK,COD_PROD,COD_CC_BRN,COD_ACCT_STAT from VW_CH_ACCT_MAST where COD_ACCT_NO in (select distinct cast(ACCOUNT_NO as char(48)) from COM_APPROVALMAPPING_MST where SERVICECDID like ('"+companyId+"') and ACTIVE_FLAG = 'Y')";
			try {
			conn = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());


			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			while(rset.next())
			{
			sss_res_header8.put("AccountNumber", rset.getString(2).trim());
			sss_res_header8.put("Balance", rset.getString(1).trim());
			sss_res_header8.put("AccountCustomerRelationship", "JOO");
			sss_res_header8.put("MaturityDate", "1800-01-01");
			sss_res_header8.put("ProductName", "CURRENT ACCOUNT - SME STAFF");
			sss_res_header8.put("NomineeName", "");
			sss_res_header8.put("InstallmentAmount", "0");
			sss_res_header8.put("InterestRate", "0");
			sss_res_header8.put("MaturityAmount", "0");
			sss_res_header8.put("TermPeriod", "0");
			sss_res_header8.put("AccountStatus", rset.getString(6).trim());
			sss_res_header8.put("BranchCode", rset.getString(5).trim());
			sss_res_header8.put("ProductCode", rset.getString(4).trim());
			sss_res_header8.put("BookBalance", rset.getString(3).trim());
			sss_res_header8.put("OpenDate", "2006-10-03");

			ss_res_header81.add(sss_res_header8);
			sss_res_header8.clear();
			
			ApplicationGenParameter.parameterMasterList.put(rset.getString(2).trim(),rset.getString(1).trim());
			}

			rset.close();
			pstmt.close();

			ss_res_header222.put("accountType", "CA");
			ss_res_header222.put("AccountDetails",ss_res_header81);
			ss_res_header222.put("accountTypeDetails",ss_res_header81);
			/*}*/
			
			
			//########################################################################
			sss_res_header6.put("AccountNumber", "000381311111316");
			sss_res_header6.put("Balance", "1000000");
			sss_res_header6.put("AccountCustomerRelationship", "SOW");
			sss_res_header6.put("MaturityDate", "2019-02-13");
			sss_res_header6.put("ProductName", "SAVINGS ACCOUNT - STAFF");
			sss_res_header6.put("NomineeName", "SUHAS PANDURANG CHAVAN");
			sss_res_header6.put("InstallmentAmount", "10000");
			sss_res_header6.put("InterestRate", "7.0");
			sss_res_header6.put("MaturityAmount", "269874.25");
			sss_res_header6.put("TermPeriod", "25");
			sss_res_header6.put("AccountStatus", "8");
			sss_res_header6.put("BranchCode", "1");
			sss_res_header6.put("ProductCode", "906");
			sss_res_header6.put("BookBalance", "49000");
			sss_res_header6.put("OpenDate", "2017-01-13");

			sss_res_header7.put("AccountNumber", "000190600002354");
			sss_res_header7.put("Balance", "0");
			sss_res_header7.put("AccountCustomerRelationship", "JOO");
			sss_res_header7.put("MaturityDate", "2017-11-13");
			sss_res_header7.put("ProductName", "SAVINGS ACCOUNT - STAFF");
			sss_res_header7.put("NomineeName", "");
			sss_res_header7.put("InstallmentAmount", "25000");
			sss_res_header7.put("InterestRate", "7.0");
			sss_res_header7.put("MaturityAmount", "258163.21");
			sss_res_header7.put("TermPeriod", "10");
			sss_res_header7.put("AccountStatus", "8");
			sss_res_header7.put("BranchCode", "1");
			sss_res_header7.put("ProductCode", "906");
			sss_res_header7.put("BookBalance", "4900.20");
			sss_res_header7.put("OpenDate", "2017-01-13");
			
			ss_res_header51[0]=sss_res_header6;
			ss_res_header51[1]=sss_res_header7;
			ss_res_header11.put("accountType", "RD");
			ss_res_header11.put("AccountDetails",ss_res_header51);// Changed to accountTypeDetails  to AccountDetails
			ss_res_header11.put("accountTypeDetails",ss_res_header51);
			//#########################################################################
			ss_res_header6[0]=ss_res_header;
			ss_res_header6[1]=ss_res_header1;
			ss_res_header6[2]=ss_res_header11;
			ss_res_header6[3]=ss_res_header222;
			
			//ss_res_header6[0]=ss_res_header;
			//ss_res_header6[0]=ss_res_header1;
			//ss_res_header6[2]=ss_res_header11;
			//ss_res_header6[1]=ss_res_header111;
			//ss_res_header6[0]=ss_res_header222;
			
			ss_res_header3.put("AccountTypeList", ss_res_header6);
			m_res_header.put("ResCode", "0");
			m_res_header.put("ResBody", ss_res_header3);
			
				JSONArray AccountTypeList = new JSONArray();
				JSONObject accountDataAsset = new JSONObject();
				accountDataAsset.put("accountType", "AOD");
				accountDataAsset.put("AccountDetails", new JSONArray());
				accountDataAsset.put("accountTypeDetails", new JSONArray());
				
				JSONObject accountDataLib = new JSONObject();
				accountDataLib.put("accountType", "LOD");
				accountDataLib.put("AccountDetails", new JSONArray());
				accountDataLib.put("accountTypeDetails",new JSONArray());
				
				JSONObject accountDataEefc = new JSONObject();
				accountDataEefc.put("accountType", "EEFC");
				accountDataEefc.put("AccountDetails", new JSONArray());
				accountDataEefc.put("accountTypeDetails", new JSONArray());
				
				m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").add(accountDataAsset);
				m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").add(accountDataLib);
				m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").add(accountDataEefc);
				
				JSONArray assetOdLimit = new JSONArray();
				JSONArray libOdLimit = new JSONArray();
				JSONArray eefcLimit = new JSONArray();
				JSONArray caAccountArrayUpdated = new JSONArray();
				log.info("check...........");
				AccountTypeList = m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList");
				int sizeOfAccTypList = AccountTypeList.size();
				
				for(int i = 0;i<sizeOfAccTypList;i++) {
					if(AccountTypeList.getJSONObject(i).getString("accountType").equals("CA")){
						JSONArray caAccountArray = new JSONArray();
						caAccountArray = AccountTypeList.getJSONObject(i).getJSONArray("AccountDetails");
						int sizeOfAccArra = caAccountArray.size();
						
						for(int j = 0;j<sizeOfAccArra;j++) {
							boolean currentAccountFlag=false;
							log.info("accountNo.....");
							String accountNo = caAccountArray.getJSONObject(j).getString("AccountNumber");
							JSONObject odObj = m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").getJSONObject(i).getJSONArray("AccountDetails").getJSONObject(j);
							pstmt5 = conn.prepareStatement(getQuery("ACCOUNT_EXPIRY"));                      // Date check
							pstmt5.setString(1, accountNo.trim());
							rset5 = pstmt5.executeQuery();                                          
							if(rset5.next() && rset5.getInt(1)>0) {
								//SELECT AMT_OD_LIMIT,BAL_AVAILABLE,TRIM(NVL(COD_LINE_NO,'NA')) AS COD_LINE_NO,COD_CCY FROM VW_CH_ACCT_MAST 
								//WHERE FLG_MNT_STATUS='A' AND COD_ACCT_NO = cast(? as char(48)) GROUP BY AMT_OD_LIMIT,BAL_AVAILABLE,COD_LINE_NO,COD_CCY
								pstmt = conn.prepareStatement(getQuery("FETCH_ACCOUNT_DTL_VW_CH_ACCT_MAST"));
								pstmt.setString(1, accountNo.trim());
								rset = pstmt.executeQuery();
								if(rset.next()) {
									if(rset.getString(3).equals("NA") && rset.getInt(4)==1) {
										odObj.put("limitAmount", rset.getString(1));
										if(odObj.getString("Balance").contains("-")) {
											String bal = odObj.getString("Balance");
											bal=bal.substring(1, bal.length());
											BigDecimal fcrLimit , apiBal, fcrUtilization;                     //FCR condition 1
											fcrLimit = new BigDecimal(rset.getString(1));
											fcrUtilization = new BigDecimal(bal);
											apiBal = fcrLimit.subtract(fcrUtilization);
											odObj.put("balanceAvailable", apiBal);
											odObj.put("utilization", bal);
										}else if(odObj.getInt("Balance")>0) {
											BigDecimal fcrLimit , apiBal, fcrBal;
											fcrLimit = new BigDecimal(rset.getString(1));
											String bal = odObj.getString("Balance");                          //FCR condition 2
											apiBal = new BigDecimal(bal);
											fcrBal = fcrLimit.add(apiBal);	
											odObj.put("balanceAvailable", fcrBal);
											odObj.put("utilization", "0.00");
										}else {
											BigDecimal fcrLimit , apiBal, fcrBal;
											fcrLimit = new BigDecimal(rset.getString(1));
											String bal = odObj.getString("Balance");
											apiBal = new BigDecimal(bal);                                     //FCR condition 3
											fcrBal = fcrLimit.add(apiBal);
											odObj.put("balanceAvailable", fcrBal);
											odObj.put("utilization", "0.00");
										}
										odObj.put("isChild", false);
									
									}else if(!(rset.getString(3).equals("NA")) && rset.getInt(4)==1)
									{    //          1              2          3             4                            5                    6
										//SELECT DESCRIPTION,LIMIT_AMOUNT,UTILISATION,AVAILABLE_AMOUNT,NVL(MAIN_LINE,'NA') AS MAIN_LINE,AVAILABILITY_FLAG FROM VW_LIMITS 
										//WHERE TRIM(LINE_ID) = TRIM(?) and CUSTOMER_NO = ?
										pstmt3 = conn.prepareStatement(getQuery("GET_BALANCE"));
										pstmt3.setString(1, rset.getString(3).trim());
										pstmt3.setString(2, companyId);
										rset3 = pstmt3.executeQuery();
										if(rset3.next()) {
											if(!"N".equals(rset3.getString(6).trim())) {
												if(!"NA".equals(rset3.getString(5))) {
													accDaoObj = new accountDetailsForOnlineOdDaoImpl();
													JSONObject l_childAndStandAloneDtl = accDaoObj.fetchAccountDetailsForOd(accountNo,companyId,rset.getString(3).trim(),rset3.getString(5).trim(),rset.getString(2));
													log.info("l_childAndStandAloneDtl...");												
													if(l_childAndStandAloneDtl.has("pflag") && l_childAndStandAloneDtl.getString("pflag").equals("false")) {
														log.info("if parent availibility flag = N");
														caAccountArrayUpdated.add(m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").getJSONObject(i).getJSONArray("AccountDetails").getJSONObject(j));
													}else if(l_childAndStandAloneDtl.has("cflag") && l_childAndStandAloneDtl.getString("cflag").equals("false")){
														log.info("if child availibility flag = N");
														caAccountArrayUpdated.add(m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").getJSONObject(i).getJSONArray("AccountDetails").getJSONObject(j));
													}else {
													
														log.info("if parent and child availibility flag = Y ");
														odObj.put("childData", l_childAndStandAloneDtl);
														odObj.put("isChild", true);
													}
												}else {
													log.info("in standalone fcc ");
													Double balAvailFromAcctMastD;
													balAvailFromAcctMastD=rset.getDouble(2);
													BigDecimal availLimit , availBal, availUtil,balAvailFromAcctMast;
													availLimit = new BigDecimal(rset3.getString(2));
													availUtil =  new BigDecimal(rset3.getString(3));
													balAvailFromAcctMast = new BigDecimal(rset.getString(2));
													if(balAvailFromAcctMastD <= 0.0){
														odObj.put("limitAmount", availLimit);
														odObj.put("utilization", availUtil);
														availBal = availLimit.subtract(availUtil);
														String avalVal =availBal.toString();
														if(avalVal.contains("-")) {
															odObj.put("balanceAvailable", avalVal.substring(1, avalVal.length()));
														}else {
															odObj.put("balanceAvailable", avalVal);
														}
													}else if(balAvailFromAcctMastD > 0.0) {
														BigDecimal totalBal ;
														odObj.put("limitAmount", availLimit);
														odObj.put("utilization", availUtil);
														availBal = availLimit.subtract(availUtil);
														totalBal = availBal.add(balAvailFromAcctMast);
														String avalVal =totalBal.toString();
														if(avalVal.contains("-")) {
															odObj.put("balanceAvailable", avalVal.substring(1, avalVal.length()));
														}else {
															odObj.put("balanceAvailable", avalVal);
														}
													}
													odObj.put("isChild", false);
												}
											}else {
												currentAccountFlag = true;
												log.info("in availibility flag = N of line ID");
											}
										}else {
											currentAccountFlag = true;
											log.info("if vw_limit date_end expired");
											caAccountArrayUpdated.add(odObj);
										}
										rset3.close();
										pstmt3.close();
									}
									if(!currentAccountFlag) {
										if(rset.getDouble(1)>0.00 && rset.getDouble(2)>= 0.00 && rset.getInt(4)==1) {
											assetOdLimit.add(odObj);
										}else if(rset.getDouble(1)>0.00 && rset.getDouble(2)< 0.00 && rset.getInt(4)==1) {
											libOdLimit.add(odObj);
										}else if(rset.getInt(4)!=1) {
											eefcLimit.add(odObj);
										}else {
											log.info("if amt_od_limit = 0 ,bal_available = 0, api bal=0");
											caAccountArrayUpdated.add(odObj);
										}
									}// Commented by 2157
									else {
										log.info("if vw_limit date_end expired");
										caAccountArrayUpdated.add(m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").getJSONObject(i).getJSONArray("AccountDetails").getJSONObject(j));
									}
								}
								rset.close();
								pstmt.close();
							}else {
								log.info("if date expired in vw-ch-od-limit");
								caAccountArrayUpdated.add(m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").getJSONObject(i).getJSONArray("AccountDetails").getJSONObject(j));
							}
							m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").getJSONObject(i).getJSONArray("AccountDetails").getJSONObject(j).clear();
							
							rset5.close();
							pstmt5.close();
						}
					}
				}
				
				AccountTypeList = m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList");
				sizeOfAccTypList = AccountTypeList.size();
				for(int i = 0;i<sizeOfAccTypList;i++) {
					if(AccountTypeList.getJSONObject(i).getString("accountType").equals("AOD")){
						m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").getJSONObject(i).put("AccountDetails",assetOdLimit);
						m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").getJSONObject(i).put("accountTypeDetails",assetOdLimit);
					}
					else if(AccountTypeList.getJSONObject(i).getString("accountType").equals("LOD")){
						m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").getJSONObject(i).put("AccountDetails",libOdLimit);
						m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").getJSONObject(i).put("accountTypeDetails",libOdLimit);
					}
					else if(AccountTypeList.getJSONObject(i).getString("accountType").equals("EEFC")){
						m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").getJSONObject(i).put("AccountDetails",eefcLimit);
						m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").getJSONObject(i).put("accountTypeDetails",eefcLimit);
					}else if(AccountTypeList.getJSONObject(i).getString("accountType").equals("CA")){
						m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").getJSONObject(i).put("AccountDetails",caAccountArrayUpdated);
						m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").getJSONObject(i).put("accountTypeDetails",caAccountArrayUpdated);
					}
				}

			} catch (Exception e) {
				log.info("Exception :::", e);
			} finally {

				try {
					if (rset3 != null) { //rset3,rset,rset5,pstmt3,pstmt,pstmt5c lose in finally-2299
						rset3.close();
					}
					if (pstmt3 != null) {
						pstmt3.close();
					}
					if (rset != null) {
						rset.close();
					}
					if (pstmt != null) {
						pstmt.close();
					}
					if (rset5 != null) {
						rset5.close();
					}
					if (pstmt5 != null) {
						pstmt5.close();
					}
					if (conn!=null ) {
						closeConnection(conn,new Object(){}.getClass().getEnclosingMethod().getName().toString());
					}
				} catch (SQLException e) {
				log.info("SQLException ===", e);
				}
				} 
			
			
			log.info("Response Header====");
			return m_res_header;

		
		
		}
	}

	public JSONObject getAllFDAccDtlsReq(JSONObject accInfo, String serviceName) {

		log.info(" calling  getAllFDAccDtlsReq method <<<<<<<<<<<<<<<<");
		// String Authorization = "Basic dGVzdGNsaWVudDp0ZXN0QDEyMw==";

		for (String key : ServiceCallAPI.apiURLMasterList.keySet()) {
			if (serviceName.toUpperCase().equals(key.trim().toUpperCase())) {
				baseURL = ServiceCallAPI.apiURLMasterList.get(key);
				break;
			}
		}

		Gson g3 = new Gson();
		Gson gson1 = new Gson();

		LinkedHashMap<String, Object> main_req_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> sub_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> s1_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> s11_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> s12_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> s2_header = new LinkedHashMap<String, Object>();

		s11_header.put("RequesterID", ServiceCallAPI.serverRequesterID);
		// s1_header.put("ConsumerContext", s11_header);
		s1_header.put("ConsumerContext", gson1.fromJson(g3.toJson(s11_header), s11_header.getClass()));

		s12_header.put("ServiceName", "CustomerManagement");
		s12_header.put("ReqRefNum", CommonUtility.getReqRefNum());
		s12_header.put("ReqRefTimeStamp", CommonUtility.getCurrentTimeStamp());
		s12_header.put("ServiceVersionNo", "1.0");

		s1_header.put("ServiceContext", gson1.fromJson(g3.toJson(s12_header), s12_header.getClass()));

		// sub_header.put("ReqHdr", s1_header);
		sub_header.put("ReqHdr", gson1.fromJson(g3.toJson(s1_header), s1_header.getClass()));

		s2_header.put("AccountNumber", accInfo.get("AccountNumber"));

		// sub_header.put("ReqBody", s2_header);
		sub_header.put("ReqBody", gson1.fromJson(g3.toJson(s2_header), s2_header.getClass()));

		// main_req_header.put("GetAllAccountsReq", sub_header);
		main_req_header.put("GetAllFDAccDtlsReq", gson1.fromJson(g3.toJson(sub_header), sub_header.getClass()));
		String request = g3.toJson(gson1.toJson(main_req_header, main_req_header.getClass()));
		request = CommonUtility.convertBasicString(request);

		String reqfrmAPI = "" , apiResName ="";
		JSONObject reqJsonfrmAPI = null;
		JSONObject m_res_header = new JSONObject();// ResBody
		log.info("baseURL...."+baseURL);
		if (SERVICE_TYPE.equals("true")) {

			try {
				reqfrmAPI = Utils.DataFromService(baseURL, request ,Authorization);//change by yesh
				reqJsonfrmAPI = JSONObject.fromObject(reqfrmAPI);//change by yesh
				if (reqJsonfrmAPI.containsKey("ErrorDetails")) {
					return reqJsonfrmAPI;
				}
				apiResName = "GetAllFDAccDtlsRes";
				m_res_header = CommonUtility.esbErrorHandling(reqJsonfrmAPI, apiResName);

			} catch (JSONException e) {
				log.info("JSONException---",e);
			} catch (Exception e) {
				log.info("Exception---",e);
			}

			return m_res_header;
		} else {
			JSONObject ss_res_header = new JSONObject();// accountType-FD
			JSONObject ss_res_header3 = new JSONObject();// accountTypeDetails
			JSONObject sss_res_header1 = new JSONObject();// AccountNumber,SecondaryAccountHolderName,RequiredQuaterlyBalance
			JSONObject sss_res_header11 = new JSONObject();
			JSONObject sss_res_header12 = new JSONObject();
			JSONObject sss_res_header2 = new JSONObject();
			JSONObject sss_res_header21 = new JSONObject();
			JSONObject sss_res_header22 = new JSONObject();
			// CurrentQuaterlyBalance,MMID,IFSCCode,NomineeName

			JSONObject[] ss_res_header4 = new JSONObject[3];//For AccountDetails,FD
			JSONObject[] ss_res_header5 = new JSONObject[2];//For AccountDetResBodyails,SA
			JSONObject[] ss_res_header6 = new JSONObject[2];//FOR accountType,AccountDetails

			sss_res_header1.put("AccountNumber", "000140100043994");

			sss_res_header11.put("DepositTerm1", "12");
			sss_res_header12.put("DepositTerm2", "0");
			ss_res_header4[0]=sss_res_header11;
			ss_res_header4[1]=sss_res_header12;
			sss_res_header1.put("DepositTerm", ss_res_header3);

			sss_res_header2.put("DepositTerm", "100000");
			sss_res_header1.put("InterestRate", "7.75");
			sss_res_header1.put("MaturityDate", "2018-11-17");
			sss_res_header1.put("CloseOutTaxWtheld", "0.0");
			sss_res_header1.put("CloseoutBalance", "1265.35");
			sss_res_header1.put("InterestPaidToDate", "20000.0");
			sss_res_header1.put("LienAmount", "20000.99");
			sss_res_header1.put("MaturityAmount", "23341.47");
			sss_res_header1.put("ProjectedInterest", "0.0");
			sss_res_header1.put("TermDepositAmount", "20000.58");
			sss_res_header1.put("DepositValueDate", "2017-01-13");
			sss_res_header1.put("TermDepositNumber", "1");
			sss_res_header1.put("IsFetchStatus", "true");
			sss_res_header1.put("SweepInEnabled", "Y");
			sss_res_header1.put("NextInterestPayoutDate", "2016-09-17");
			sss_res_header1.put("LastInterestPayoutDate", "2016-08-17");

			sss_res_header2.put("AccountNumber", "000140104543995");

			sss_res_header21.put("DepositTerm1", "12");
			sss_res_header22.put("DepositTerm2", "0");
			ss_res_header5[0] = sss_res_header21;
			ss_res_header5[1] = sss_res_header22;
			sss_res_header1.put("DepositTerm", ss_res_header3);

			sss_res_header2.put("DepositTerm", "100000");
			sss_res_header2.put("InterestRate", "7.75");
			sss_res_header2.put("MaturityDate", "2018-02-16");
			sss_res_header2.put("CloseOutTaxWtheld", "0.0");
			sss_res_header2.put("CloseoutBalance", "1389.89");
			sss_res_header2.put("InterestPaidToDate", "60000");
			sss_res_header2.put("LienAmount", "30000.23");
			sss_res_header2.put("MaturityAmount", "32409.00");
			sss_res_header2.put("ProjectedInterest", "0.0");
			sss_res_header2.put("TermDepositAmount", "30000.65");
			sss_res_header2.put("DepositValueDate", "2017-01-13");
			sss_res_header2.put("TermDepositNumber", "0.0");
			sss_res_header2.put("IsFetchStatus", "0.0");
			sss_res_header2.put("SweepInEnabled", "0");
			sss_res_header2.put("NextInterestPayoutDate", "false");
			sss_res_header2.put("LastInterestPayoutDate", "");

			ss_res_header6[0] = sss_res_header1;
			ss_res_header6[1] = sss_res_header2;
			ss_res_header.put("FDAccount", ss_res_header6);
			// m1_res_header.put("FDAcctDetails", s_res_header);
			m_res_header.put("ResCode", "0");
			m_res_header.put("ResBody", ss_res_header);
			/*String data ="{'result':{'GetAllFDAccDtlsRes':{'ResHdr':{'ConsumerContext':{'RequesterID':'SME'},'ServiceContext':{'ServiceName':'CustomerManagement','ReqRefNum':'1608214249827','ReqRefTimeStamp':'2020-12-17T19:40:49','ServiceVersionNo':'1.0'},'ServiceResponse':{'EsbResTimeStamp':'2020-12-17 19:40:45.799114','EsbResStatus':'1'},'ErrorDetails':[{'ErrorInfo':[{'HostErrCode':'2077','HostErrDesc':'Invalid user id'},{'HostErrCode':'2077','HostErrDesc':'Invalid user id'},{'HostErrCode':'2077','HostErrDesc':'Invalid user id'},{'HostErrCode':'2077','HostErrDesc':'Invalid user id'},{'HostErrCode':'2077','HostErrDesc':'Invalid user id'},{'HostErrCode':'2077','HostErrDesc':'Invalid user id'},{'HostErrCode':'2077','HostErrDesc':'Invalid user id'},{'HostErrCode':'2077','HostErrDesc':'Invalid user id'},{'HostErrCode':'2077','HostErrDesc':'Invalid user id'},{'HostErrCode':'2077','HostErrDesc':'Invalid user id'},{'HostErrCode':'2077','HostErrDesc':'Invalid user id'}]}]}}}}";
			reqJsonfrmAPI = JSONObject.fromObject(data);
			log.info("CreateFDAccRes String Response<<<<<"+reqJsonfrmAPI);
			//JSONObject modifyJson = new JSONObject();
			apiResName = "GetAllFDAccDtlsRes";
			m_res_header = CommonUtility.esbErrorHandling(reqJsonfrmAPI, apiResName);
			*/log.info("Response Header====" + m_res_header);
			return m_res_header;
		}
	}

	public JSONObject getTDSCertificateReq(JSONObject accInfo) {

		log.info(" calling  GetTDSCertificateReq method  <<<<<<<<<<<<<<<<");
		//String Authorization = "Basic dGVzdGNsaWVudDp0ZXN0QDEyMw==";

		Gson g3 = new Gson();
		Gson gson1 = new Gson();

		LinkedHashMap<String, Object> main_req_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> sub_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> s1_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> s11_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> s12_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> s2_header = new LinkedHashMap<String, Object>();

		s11_header.put("RequesterID", ServiceCallAPI.serverRequesterID);
		// s1_header.put("ConsumerContext", s11_header);
		s1_header.put("ConsumerContext", gson1.fromJson(g3.toJson(s11_header), s11_header.getClass()));

		s12_header.put("ServiceName", "CustomerManagement");
		s12_header.put("ReqRefNum", CommonUtility.getReqRefNum());
		s12_header.put("ReqRefTimeStamp", CommonUtility.getCurrentTimeStamp());
		s12_header.put("ServiceVersionNo", "1.0");

		s1_header.put("ServiceContext", gson1.fromJson(g3.toJson(s12_header), s12_header.getClass()));

		// sub_header.put("ReqHdr", s1_header);
		sub_header.put("ReqHdr", gson1.fromJson(g3.toJson(s1_header), s1_header.getClass()));

		s2_header.put("CustomerId", accInfo.get("CustomerId"));
		s2_header.put("CustomerName", accInfo.get("CustomerName"));
		s2_header.put("PAN", accInfo.get("PAN"));
		s2_header.put("AssessmentYear", accInfo.get("AssessmentYear"));

		// sub_header.put("ReqBody", s2_header);
		sub_header.put("ReqBody", gson1.fromJson(g3.toJson(s2_header), s2_header.getClass()));

		// main_req_header.put("GetAllAccountsReq", sub_header);
		main_req_header.put("GetTDSCertificateReq", gson1.fromJson(g3.toJson(sub_header), sub_header.getClass()));

		String request = g3.toJson(gson1.toJson(main_req_header, main_req_header.getClass()));

		// log.info("Response Header Before ==="+request);
		request = CommonUtility.convertBasicString(request);
		// log.info("Response Header ==="+request);
		String reqfrmAPI = "";
		JSONObject reqJsonfrmAPI = null;
		JSONObject m_res_header = new JSONObject();//ResBody

		// log.info("GetTDSCertificateReq:: ==="+request);
		if (SERVICE_TYPE.equals("true")) {
			// log.info("If Service Type TRUE");

			// Go With API
			// log.info(" --> Calling ");

			baseURL = ServiceCallAPI.ServiceURL + "/CustomerManagement/GetTDSCertificate";
			//log.info("baseURL ==="+baseURL);

			try {
				reqfrmAPI = Utils.DataFromService(baseURL, request ,Authorization);//change by yesh
				// log.info("GetTDSCertificateRes String Response<<<<<"+reqfrmAPI);
				reqJsonfrmAPI = JSONObject.fromObject(reqfrmAPI);//change by yesh
				//log.info("GetTDSCertificateRes json<<<<<<<<<<"+reqJsonfrmAPI);//change by yesh
				
				if(reqJsonfrmAPI.containsKey("ErrorDetails"))
				{
					/*modifyJson.put("Status", "Failure");
					m_res_header.put("ResBody", reqJsonfrmAPI);
					*/
					return reqJsonfrmAPI;
				}
				
				if(reqJsonfrmAPI.containsKey("GetTDSCertificateRes") && reqJsonfrmAPI.getJSONObject("result").getJSONObject("GetTDSCertificateRes").getJSONObject("ServiceResponse").get("EsbResStatus").equals("0"))
				{
					m_res_header.put("ResBody",reqJsonfrmAPI.get("GetTDSCertificateRes"));
					//log.info("inside if API response::"+m_res_header);
				}else if(reqJsonfrmAPI.containsKey("GetTDSCertificateRes") && reqJsonfrmAPI.getJSONObject("result").getJSONObject("GetTDSCertificateRes").getJSONObject("ServiceResponse").get("EsbResStatus").equals("1")){
					m_res_header.put("ResBody",reqJsonfrmAPI.getJSONObject("result").getJSONObject("GetTDSCertificateRes").get("ErrorDetails"));
					//log.info("inside elseif API response::"+m_res_header);
				}
				
			} catch (JSONException e) 
			{
				log.info("JSONException---",e);			
			} catch (Exception e) {
				log.info("Exception---",e);
			}
			
			//log.info("Data from API: " + m_res_header);
			
			return m_res_header;
		}
		else
		{
			//log.info("If Service Type FALSE");
			JSONObject s_res_header = new JSONObject();//AccountNumber,SecondaryAccountHolderName,RequiredQuaterlyBalance
			//CurrentQuaterlyBalance,MMID,IFSCCode,NomineeName
			s_res_header.put("AccountNumber", "000190600002179");
			s_res_header.put("SecondaryAccountHolderName", "3078BAU");
			s_res_header.put("RequiredQuaterlyBalance", "10,000.00");
			s_res_header.put("CurrentQuaterlyBalance", ".00");
			s_res_header.put("MMID", "9532281");
			s_res_header.put("IFSCCode", "YESB0000001");
			s_res_header.put("NomineeName", "SUHAS PANDURANG CHAVAN");
			m_res_header.put("ResCode", "0");
			m_res_header.put("ResBody", s_res_header);
			//log.info("Response Header===="+m_res_header);
			return m_res_header;
		}
	}
	
	
	
	/*API for fundTrasfer For CASA*/
	
	public JSONObject fundTransToCASA(JSONObject tranInfo)
	{

		log.info("Inside fundTransToCASA ===");
		JSONObject main_req_header = new JSONObject();
		JSONObject sub_header = new JSONObject();
		JSONObject tempjson = new JSONObject();
		JSONObject reqJsonfrmAPI = null;
		tempjson.put("Version", "1");
		tempjson.put("Channel", "DigitalWallet");
		tempjson.put("ExtUniqueRefId", "DWCC156");
		sub_header.put("Header", tempjson);

		tempjson.clear();
		tempjson.put("FromAccountNo", tranInfo.get("FromAccountNo").toString());
		tempjson.put("ToAccountNo", tranInfo.get("ToAccountNo").toString());
		tempjson.put("TransactionAmount", tranInfo.get("TransactionAmount").toString());
		tempjson.put("FromCustID", tranInfo.get("FromCustID").toString());
		tempjson.put("Narration", tranInfo.get("Narration").toString());
		tempjson.put("ReferenceNo1", tranInfo.get("ReferenceNo1").toString());
		tempjson.put("ReferenceNo2", tranInfo.get("ReferenceNo2").toString());
		tempjson.put("ReferenceNo3", tranInfo.get("ReferenceNo3").toString());
		tempjson.put("TXNCode", "11");
		sub_header.put("RequestPayload", tempjson);

		main_req_header.put("GenerateMMIDReq", sub_header);

		// log.info("Response Header ==="+main_req_header);

		if (SERVICE_TYPE.equals("true")) {
			// log.info("If Service Type TRUE");

			reqJsonfrmAPI = new JSONObject();
			JSONObject m_res_header = new JSONObject();// ResBody
			
			//Go With API

			//log.info(" --> Calling ");

			baseURL = ServiceCallAPI.ServiceURL+"/";
			// log.info("baseURL ==="+baseURL);
			try {
				reqJsonfrmAPI =  (JSONObject) Utils.getDataFromTolService(baseURL, main_req_header);
				if (reqJsonfrmAPI.containsKey("ResponseBody") && !reqJsonfrmAPI.get("ResponseBody").equals("")) {
					m_res_header.put("ResBody", reqJsonfrmAPI.get("ResponseBody"));
				}

			} catch (JSONException e) {
				log.info("JSONException---",e);
			} catch (Exception e) {
				log.info("Exception---",e);
			}

			//log.info("Data from API: " + reqJsonfrmAPI.toString());
			return m_res_header;
		} else {
			//log.info("If Service Type FALSE");
			net.sf.json.JSONObject m_res_header = new net.sf.json.JSONObject();//ResBody
			net.sf.json.JSONObject m1_res_header = new net.sf.json.JSONObject();//Status,ReferenceNumber
			m1_res_header.put("Status", "SUCCESS");
			m1_res_header.put("ReferenceNumber", "2414920160101000188833");
			m_res_header.put("ResCode", "0");
			m_res_header.put("ResBody", m1_res_header);

			log.info("Response Header====");
			return m_res_header;
		}

	}
	



	public String encodePin(String debitPin, String debitCard) {
		log.info("inside encode Pin");
		String debitModPin = "04" + debitPin + "FFFFFFFFFF";
		String debitCardNO = "0000" + debitCard.substring(3, debitCard.length() - 1);
		//log.info(debitModPin +"  "+ debitCardNO.toString());
		String encryptedPin = xorHex(debitModPin, debitCardNO);
		return encryptedPin;
	}

	public String xorHex(String a, String b) {
	    char[] chars = new char[a.length()];
	    for (int i = 0; i < chars.length; i++) {
	    	log.info(i);
	        chars[i] = toHex(fromHex(a.charAt(i)) ^ fromHex(b.charAt(i)));
	    }
	    return new String(chars);
	}

	private int fromHex(char c) {
	    if (c >= '0' && c <= '9') {
	        return c - '0';
	    }
	    if (c >= 'A' && c <= 'F') {
	        return c - 'A' + 10;
	    }
	    if (c >= 'a' && c <= 'f') {
	        return c - 'a' + 10;
	    }
	    throw new IllegalArgumentException();
	}

	private char toHex(int nybble) {
	    if (nybble < 0 || nybble > 15) {
	        throw new IllegalArgumentException();
	    }
	    return "0123456789ABCDEF".charAt(nybble);
	}



	
	
	@Transactional
    public String setCustomerAliasBaneMappingExitInTable(String custName) {
        log.info("In setCustomerAliasBaneMappingExitInTable Table");
        String customerID = "";
        int count = 0;

        try {
            
            // Create the query and set the parameter
            Query query = entityManager.createQuery("SELECT COUNT(*), customerId FROM RnbidAliasidMapping WHERE aliasId =:aliseId GROUP BY customerId");
            query.setParameter("aliseId", custName);
            
            // Execute the query
            Object[] result = (Object[]) query.getSingleResult();
            
            if (result != null) {
                count = ((Number) result[0]).intValue(); // Cast the first result to int
                customerID = (String) result[1]; // Cast the second result to String
            }
            
            if (count == 0) {
                customerID = custName;
            }
        } catch (Exception e) {
            log.error("Exception ===", e);
        }
        
        return customerID;
    }
	

	public String getCustIDForAlias(String aliasName)
	{
		log.info("In getCustIDForAlias Table");
		Connection conn=null;
		String custId = "";
		try
		{
			conn = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
			
			// Check Alias exists
			//SELECT_CUSTID_FOR_ALIAS=SELECT CUSTOMER_ID FROM RNBID_ALIASID_MAPPING WHERE ALIAS_ID= ?

			pstmt = conn.prepareStatement(getQuery("SELECT_CUSTID_FOR_ALIAS"));
			pstmt.setString(1, aliasName);
			rset = pstmt.executeQuery();

			if(rset.next()) {
				custId = rset.getString(1);
			} else {
				custId = aliasName;
			}

			rset.close();
			pstmt.close();

		}
		 catch (Exception e) {
				log.info("Exception ===", e);

			} finally {

				try {

					if (rset != null) {
						rset.close();
					}
					if (pstmt != null) {
						pstmt.close();
					}
					if(conn != null)
					{
						closeConnection(conn,new Object(){}.getClass().getEnclosingMethod().getName().toString());
					}

				} catch (SQLException e) {
					log.info("SQLException ===", e);
				}
			}
		
		return custId;
	}

	public JSONObject requestForSaltID(String userID,String serviceName) {
		log.info("Inside requestForSaltID ==="+ServiceCallAPI.apiURLMasterList.size());
		for(String key : ServiceCallAPI.apiURLMasterList.keySet()){
			if(serviceName.toUpperCase().equals(key.trim().toUpperCase())){
				baseURL=ServiceCallAPI.apiURLMasterList.get(key);
				break;
			}
		}
		currentts = sdf.format(date);
		currentts = currentts.replace(" ", "T");
		JSONObject m_res_header = new JSONObject();

		Gson g3=new Gson();
		Gson gson1=new Gson();

		LinkedHashMap<String, Object> main_req_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> sub_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> s1_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> s11_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> s12_header = new LinkedHashMap<String, Object>();

		s11_header.put("RequesterID", ServiceCallAPI.serverRequesterID);
		s1_header.put("ConsumerContext", gson1.fromJson(g3.toJson(s11_header), s11_header.getClass()));
		
		s12_header.put("ServiceName", "RegistrationService");
		s12_header.put("ReqRefNum",CommonUtility.getReqRefNum());
		s12_header.put("ReqRefTimeStamp",CommonUtility.getCurrentTimeStamp());
		s12_header.put("ServiceVersionNo", "1.0");
		
		s1_header.put("ServiceContext", gson1.fromJson(g3.toJson(s12_header), s12_header.getClass()));
		sub_header.put("ReqHdr", gson1.fromJson(g3.toJson(s1_header), s1_header.getClass()));
		
		sub_header.put("UserId", userID);
		//sub_header.put("Password", accInfo.getPassword());
		
		
		main_req_header.put("GetSaltUserIdReq", gson1.fromJson(g3.toJson(sub_header), sub_header.getClass()));
		
		String request = g3.toJson(gson1.toJson(main_req_header,main_req_header.getClass()));
		//log.info("Response Header Before ==="+request);
		request = CommonUtility.convertBasicString(request);
		//log.info("Response Header ==="+request);		
		String reqfrmAPI = "",apiResName = "";
		JSONObject reqJsonfrmAPI = null;
		
		if(SERVICE_TYPE.equals("true"))
		{
			try 
			{	
				reqfrmAPI = Utils.DataFromService(baseURL, request ,Authorization);
				reqJsonfrmAPI = JSONObject.fromObject(reqfrmAPI);
				if(reqJsonfrmAPI.containsKey("ErrorDetails"))
				{	
						return reqJsonfrmAPI;
				}
				
				apiResName = "GetSaltUserIdRes" ;
				m_res_header=CommonUtility.esbErrorHandling(reqJsonfrmAPI,apiResName);
				
			} catch (JSONException e) 
			{
				log.info("JSONException---",e);			
			} catch (Exception e) {
				log.info("Exception---",e);
			}
		}
		else
		{
			m_res_header.put("ResCode", "0");
			m_res_header.put("ResBody", "LOCAL");
		}

		return m_res_header;
	}

	

	
	
	//Check Portal User Register or Not
		public boolean checkValidateUserRegister(String custId){
			
			log.info("In checkValidateUserRegister Table");
			int count = 0;
			Connection conn=null;
			boolean passCount = false;
			try
			{
				conn = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
				
				
				if (!custId.matches("[0-9]+"))
				{
					pstmt = conn.prepareStatement(getQuery("SELECT_COUNT_ALIAS_RNBID"));
					pstmt.setString(1, custId);
					rset = pstmt.executeQuery();
					
					if(rset.next())
					{
						count = rset.getInt(1);
						custId = rset.getString(2);
					}
					
					rset.close();
					pstmt.close();
					
					
				}
				// Check Alias exists
				//SELECT_COUNT_ALIAS_RNBID=SELECT COUNT(*) FROM RNBID_ALIASID_MAPPING WHERE CUSTOMER_ID = ?
				//123123
				//SELECT_COUNT_FOR_PORTAL_EXIST=select count(*) from SME_APP_LOGIN_INFO where LOGIN_ID = ? and LOGIN_TYPE_PORTAL = 'Y'
				pstmt = conn.prepareStatement(getQuery("SELECT_COUNT_FOR_PORTAL_EXIST"));
				//pstmt = conn.prepareStatement(getQuery("SELECT_COUNT_ALIAS_RNBID"));
				pstmt.setString(1, custId);
				rset = pstmt.executeQuery();
				
				if(rset.next())
				{
					count = rset.getInt(1);
				}
				rset.close();
				pstmt.close();
					if(count > 0)
					{
						passCount = true;
					}
					else
					{
						passCount = false;
					}
				
			} catch (Exception e) {
				log.info("Exception ===", e);
			} finally {

				try {
					if (rset != null) {
						rset.close();
					}
					if (pstmt != null) {
						pstmt.close();
					}
					if (conn != null) {
						closeConnection(conn,new Object(){}.getClass().getEnclosingMethod().getName().toString());
					}
				} catch (SQLException e) {
					log.info("SQLException ===", e);
				}
			}
			log.info(":::Pass Count value:::>>"+passCount);
			return passCount;
		}
		
		//Check Portal User Email Validate or Not
		public JSONObject checkValidateEmailOtpValue(JSONObject p_otpInfo,String p_serviceName,JSONObject resJson){
		log.info("In checkValidateEmailOtpValue Table");
		String customerId = "";
		Connection conn=null;
		//String Authorization = "Basic dGVzdGNsaWVudDp0ZXN0QDEyMw==";
		if(p_otpInfo.containsKey("ApproverId"))
		{
			customerId = p_otpInfo.getString("ApproverId").trim();
		}
		else
		{
			customerId = p_otpInfo.getString("CustomerId").trim();
		}
		
		log.info("Request Header ===");
		
			log.info("If Service Type FALSE");

			try
			{
				conn = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
				//JSONObject s_res_header = new JSONObject();
				JSONObject m1_res_header = new JSONObject();
				pstmt = conn.prepareStatement(getQuery("SLECET_EMAIL_OTP_VERITY_SME_CUST_INFO_MAIL_OTP"));
				pstmt.setString(1, LoginController.sha1EncryptPass(String.valueOf(p_otpInfo.getString("emailOtpKey"))));
				pstmt.setString(2, customerId);
				rset = pstmt.executeQuery();
				if(rset.next())
					{
						if(rset.getString(1).equals(LoginController.sha1EncryptPass(String.valueOf(p_otpInfo.getString("emailValue")))))
						  {
							    m1_res_header.put("version", "1");
								m1_res_header.put("isValid", "true");
								m1_res_header.put("verificationFaultReason", "");
								resJson.getJSONObject("ResBody").put("verifyEmailOTPResponse", m1_res_header);
						   }
						 else
						  {
							    m1_res_header.put("version", "1");
								m1_res_header.put("isValid", "false");
								m1_res_header.put("verificationFaultReason", "Invalid OTP Key");
								
								resJson.getJSONObject("ResBody").put("verifyEmailOTPResponse", m1_res_header);
							 
						  }
						 
					}
				 else
				  {
					 	m1_res_header.put("version", "1");
						m1_res_header.put("isValid", "false");
						m1_res_header.put("verificationFaultReason", "Invalid OTP Key");
						
						resJson.getJSONObject("ResBody").put("verifyEmailOTPResponse", m1_res_header);
						
					 
				  }
					rset.close();
					pstmt.close();
				}
			 catch (Exception e) {
				log.info("Exception ===", e);
			} finally {

				try {
					if (rset != null) {
						rset.close();
					}
					if (pstmt != null) {
						pstmt.close();
					}
					if (conn != null) {
						closeConnection(conn,new Object(){}.getClass().getEnclosingMethod().getName().toString());
					}
				} catch (SQLException e) {
					log.info("SQLException ===", e);
				}
			}
			return resJson;
   }
		
		public JSONObject getBalanceOfAccount(String custId,String AccountNumber,String serviceName)
		{
			
			log.info(" calling  GetBalanceOfAccount method  ");
			//String Authorization = "Basic dGVzdGNsaWVudDp0ZXN0QDEyMw=="; //To Author all API 
			for(String key : ServiceCallAPI.apiURLMasterList.keySet()){
				if(serviceName.toUpperCase().equals(key.trim().toUpperCase())){
					baseURL=ServiceCallAPI.apiURLMasterList.get(key);
					break;
				}
			}
			
			
			JSONObject m_res_header = new JSONObject();
			
			Gson g3=new Gson();
			Gson gson1=new Gson();
			
			LinkedHashMap<String,Object> main_req_header = new LinkedHashMap<String,Object>();
			LinkedHashMap<String,Object> sub_header = new LinkedHashMap<String,Object>();
			LinkedHashMap<String,Object> s1_header = new LinkedHashMap<String,Object>();
			LinkedHashMap<String,Object> s11_header = new LinkedHashMap<String,Object>();
			LinkedHashMap<String,Object> s12_header = new LinkedHashMap<String,Object>();
			LinkedHashMap<String,Object> s2_header = new LinkedHashMap<String,Object>();
			
			/*Request:
			{
			  "GetCASABalanceReq": {
			    "ReqHdr": {
			      "ConsumerContext": { "RequesterID": "WRK" },
			      "ServiceContext": {
			        "ServiceName": "AccountManagement",
			        "ReqRefNum": "7607258090",
			        "ReqRefTimeStamp": "2016-02-03T16:06:00",
			        "ServiceVersionNo": "1.0"
			      }
			    },
			    "ReqBody": {
			      "AccountNumber": "041963500000219",
			      "TransactionBranch": "777",
			      "Channel": "BRN"
			    }
			  }
			}*/
			
			//"RequesterID", "WRK"
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
			
			//String channel=ServiceCallAPI.channel1;
			
			s2_header.put("AccountNumber", AccountNumber.trim());
			s2_header.put("TransactionBranch", "777");
			s2_header.put("Channel", "BRN");
			
			
			//sub_header.put("ReqBody", s2_header);
			sub_header.put("ReqBody", gson1.fromJson(g3.toJson(s2_header), s2_header.getClass()));
			
			//main_req_header.put("GetAllAccountsReq", sub_header);
			main_req_header.put("GetCASABalanceReq", gson1.fromJson(g3.toJson(sub_header), sub_header.getClass()));
			
			String request = g3.toJson(gson1.toJson(main_req_header,main_req_header.getClass()));
			
			
			request = CommonUtility.convertBasicString(request);

			String reqfrmAPI = "" , apiResName = "";
			JSONObject reqJsonfrmAPI = null;
			//JSONObject m_res_header = new JSONObject();//ResBody
			
			
			if(SERVICE_TYPE.equals("true"))
			{
				
				JSONObject casaBalObj=new JSONObject();
				String getAvailBal = "";
				try 
				{			
					reqfrmAPI = Utils.DataFromService(baseURL, request ,Authorization);
					
					reqJsonfrmAPI = JSONObject.fromObject(reqfrmAPI);
					
					apiResName = "GetCASABalanceRes";
					m_res_header=CommonUtility.esbErrorHandling(reqJsonfrmAPI,apiResName);
					if(m_res_header.containsKey("ResCode") && "0".equals(m_res_header.getString("ResCode"))) 
					{
						getAvailBal = m_res_header.getJSONObject("ResBody").getString("AvailableBalance");
						casaBalObj.put("balance",getAvailBal);
						casaBalObj.put("ResCode","0");
						casaBalObj.put("msg","");
					}else if(m_res_header.containsKey("ResCode") && "1".equals(m_res_header.getString("ResCode"))) {
						//getAvailBal = m_res_header.getJSONObject("ResBody").getString("AvailableBalance");
						casaBalObj.put("balance","0.00");
						casaBalObj.put("ResCode","1");
						casaBalObj.put("msg",m_res_header.getString("ErrorDetails"));
					}
					
				} catch (JSONException e) 
				{
					log.info("JSONException---",e);			
				} catch (Exception e) {
					log.info("Exception---",e);
				}
				
				
				return casaBalObj;
			}
			else
			{
				JSONObject casaBalObj=new JSONObject();
				String getAvailBal = "";
				try 
				{
				
				/*{
					  "GetCASABalanceRes": {
					    "ResHdr": {
					      "ConsumerContext": {
					        "RequesterID": "WRK"
					      },
					      "ServiceContext": {
					        "ServiceName": "AccountManagement",
					        "ReqRefNum": "7607258090",
					        "ReqRefTimeStamp": "2016-02-03T16:06:00",
					        "ServiceVersionNo": "1.0"
					      },
					      "ServiceResponse": {
					        "EsbResTimeStamp": "2019-08-30 12:31:59.861729",
					        "EsbResStatus": "0"
					      }
					    },
					    "ResBody": {
					      "Status": "0",
					      "AccountBalance": "100",
					      "AccountOperationalStatus": "SOLE OWNER",
					      "AccountStatus": "ACCOUNT OPEN REGULAR",
					      "AvailableBalance": "100",
					      "ClearBalance": "4800.05",
					      "UnClearBalance": "0",
					      "DrawingPower": "4700.05",
					      "HoldBalance": "0",
					      "MinimumBalance": "0",
					      "NetWithdrawalBalance": "46815.05"
					    }
					  }
					}*/
				JSONObject upper1Json = new JSONObject();
				JSONObject upperJson = new JSONObject();
				JSONObject ErrorDetails = new JSONObject();
				
				JSONObject innerJson = new JSONObject();
				innerJson.put("Status", "0");
				innerJson.put("AccountBalance", "10000");
				innerJson.put("AccountOperationalStatus", "SOLE OWNER");
				innerJson.put("AccountStatus", "ACCOUNT OPEN REGULAR");
				innerJson.put("AvailableBalance", ApplicationGenParameter.parameterMasterList.get(AccountNumber));
				innerJson.put("ClearBalance", "4800.05");
				innerJson.put("UnClearBalance", "0");
				innerJson.put("DrawingPower", "4700.05");
				innerJson.put("HoldBalance", "0");
				innerJson.put("MinimumBalance", "0");
				innerJson.put("NetWithdrawalBalance", "46815.05");
				
				JSONObject midJson = new JSONObject();
				JSONObject midErrorJson = new JSONObject();
				midJson.put("ResBody", innerJson);
				
				innerJson.clear();
				JSONObject mid1Json = new JSONObject();
				innerJson.put("RequesterID", ServiceCallAPI.serverRequesterID);
				mid1Json.put("ConsumerContext", innerJson);
				ErrorDetails.put("ConsumerContext", innerJson);
				
				innerJson.clear();
				innerJson.put("ServiceName", "AccountManagement");
				innerJson.put("ReqRefNum", "7607258090");
				innerJson.put("ReqRefTimeStamp", "2016-02-03T16:06:00");
				innerJson.put("ServiceVersionNo", "1.0");
				mid1Json.put("ServiceContext", innerJson);
				ErrorDetails.put("ServiceContext", innerJson);
			
				innerJson.clear();
				innerJson.put("EsbResTimeStamp", "2019-08-30 12:31:59.861729");
				innerJson.put("EsbResStatus", "0");
				mid1Json.put("ServiceResponse", innerJson);
				ErrorDetails.put("ServiceResponse", innerJson);
				
				JSONObject ErrorDtl = new JSONObject();
				JSONObject ErrorDInfo = new JSONObject();
				ErrorDInfo.put("ErrSrc", "ESB");
				ErrorDInfo.put("EsbErrCode", "5026");
				ErrorDInfo.put("EsbErrLongDesc", "6005.1.1.446.cvc-maxLength-valid: The length of value \"556566003693600003383\" is \"21\" which is not valid with respect to maxLength facet with value \"16\" for type \"#Anonymous\"../Root/XMLNSC.Caught exception and rethrowing. XML Writing Errors have occurred. A schema validation error has occurred while validating the message tree. ");
				ErrorDInfo.put("EsbErrShortDesc", "ParserException");
				ErrorDtl.put("ErrorInfo", ErrorDInfo);
				
				/*JSONObject ErrorInfo = new JSONObject();
				ErrorInfo.put("HostErrCode", "2778");
				ErrorInfo.put("HostErrDesc", "Account not found");
				
				ErrorDtl.put("ErrorInfo", ErrorInfo);*/
				
				JSONArray ErrorDetailsArray = new JSONArray();
				ErrorDetailsArray.add(ErrorDtl);
				
				ErrorDetails.put("ErrorDetails", ErrorDetailsArray);
				
				midJson.put("ResHdr", mid1Json);
				midErrorJson.put("ResHdr", ErrorDetails);
				
				upperJson.put("GetCASABalanceRes", midJson);
				//upperJson.put("GetCASABalanceRes", midErrorJson);
				
				upper1Json.put("result", upperJson);
				
				/*"ErrorDetails": [{"ErrorInfo":    {

				      "ErrSrc": "ESB",

				      "EsbErrCode": "5026",

				      "EsbErrLongDesc": "6005.1.1.446.cvc-maxLength-valid: The length of value \"556566003693600003383\" is \"21\" which is not valid with respect to maxLength facet with value \"16\" for type \"#Anonymous\"../Root/XMLNSC.Caught exception and rethrowing. XML Writing Errors have occurred. A schema validation error has occurred while validating the message tree. ",

				      "EsbErrShortDesc": "ParserException"

				   }}]*/
				
				
				
				reqfrmAPI = upper1Json.toString();
				
				reqJsonfrmAPI = JSONObject.fromObject(reqfrmAPI);
				
				apiResName = "GetCASABalanceRes";
				m_res_header = CommonUtility.esbErrorHandling(reqJsonfrmAPI, apiResName);
				
				
				/*{
					  "ResCode": "0",
					  "ResBody": {
					    "Status": "0",
					    "AccountBalance": "10000",
					    "AccountOperationalStatus": "SOLE OWNER",
					    "AccountStatus": "ACCOUNT OPEN REGULAR",
					    "AvailableBalance": "900",
					    "ClearBalance": "4800.05",
					    "UnClearBalance": "0",
					    "DrawingPower": "4700.05",
					    "HoldBalance": "0",
					    "MinimumBalance": "0",
					    "NetWithdrawalBalance": "46815.05"
					  }
					}*/
				
				if(m_res_header.containsKey("ResCode") && "0".equals(m_res_header.getString("ResCode"))) 
				{
					getAvailBal = m_res_header.getJSONObject("ResBody").getString("AvailableBalance");
					casaBalObj.put("balance",getAvailBal);
					casaBalObj.put("ResCode","0");
					casaBalObj.put("msg","");
				}else if(m_res_header.containsKey("ResCode") && "1".equals(m_res_header.getString("ResCode"))) {
					//getAvailBal = m_res_header.getJSONObject("ResBody").getString("AvailableBalance");
					casaBalObj.put("balance","0.00");
					casaBalObj.put("ResCode","1");
					casaBalObj.put("msg",m_res_header.getString("ErrorDetails"));
				}
				
			} catch (JSONException e) {
				log.info("JSONException---", e);
			} catch (Exception e) {
				log.info("Exception---", e);
			}
			
			return casaBalObj;

		}
		
	}
		
		public JSONObject getCalculatedBalance(String custId,String AccountNumber,String serviceName)
		{
			log.info(" calling  getCalculatedBalance method  "+custId+" "+serviceName);
			JSONObject balanceFromApi =new JSONObject();
			Connection conn=null;
			try {
				
				conn = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
				balanceFromApi = getBalanceOfAccount(custId,AccountNumber,serviceName);
				
				pstmt3 = conn.prepareStatement(getQuery("ACCOUNT_EXPIRY"));
				pstmt3.setString(1, AccountNumber);
				rset3 = pstmt3.executeQuery();
				if(rset3.next()) {
					//SELECT AMT_OD_LIMIT,BAL_AVAILABLE,TRIM(NVL(COD_LINE_NO,'NA')) AS COD_LINE_NO,COD_CCY FROM VW_CH_ACCT_MAST WHERE FLG_MNT_STATUS='A' 
					//AND COD_ACCT_NO = cast(? as char(48)) GROUP BY AMT_OD_LIMIT,BAL_AVAILABLE,COD_LINE_NO,COD_CCY
					pstmt = conn.prepareStatement(getQuery("FETCH_ACCOUNT_DTL_VW_CH_ACCT_MAST"));
					pstmt.setString(1, AccountNumber);
					rset = pstmt.executeQuery();
					if(rset.next()) {
						if(rset.getString(3).equals("NA") && rset.getString(4).equals("1")) 
						{
							if(balanceFromApi.containsKey("ResCode") && "0".equals(balanceFromApi.getString("ResCode"))) 
							{
								if(balanceFromApi.getString("balance").contains("-")) {
									String bal = balanceFromApi.getString("balance");
									bal=bal.substring(1, bal.length());
									BigDecimal fcrLimit , apiBal, fcrUtilization;                    // FCR condition 1
									fcrLimit = new BigDecimal(rset.getString(1));
									fcrUtilization = new BigDecimal(bal);
									apiBal = fcrLimit.subtract(fcrUtilization);
									balanceFromApi.put("balance", apiBal);
								
								}else if(balanceFromApi.getInt("balance")>0) {
									BigDecimal fcrLimit , apiBal, fcrBal;
									fcrLimit = new BigDecimal(rset.getString(1));
									String bal = balanceFromApi.getString("balance");                       // FCR condition 2
									apiBal = new BigDecimal(bal);
									fcrBal = fcrLimit.add(apiBal);
									balanceFromApi.put("balance", fcrBal);
								
								}else {
									BigDecimal fcrLimit , apiBal, fcrBal;
									fcrLimit = new BigDecimal(rset.getString(1));
									String bal = balanceFromApi.getString("balance");
									apiBal = new BigDecimal(bal);                                     // FCR condition 3
									fcrBal = fcrLimit.add(apiBal);
									balanceFromApi.put("balance", fcrBal);
								
								}
							}else if(balanceFromApi.containsKey("ResCode") && "1".equals(balanceFromApi.getString("ResCode"))) {
							
							}
						}else if(!rset.getString(3).equals("NA") && rset.getString(4).equals("1")) {
						
							//SELECT DESCRIPTION,LIMIT_AMOUNT,NVL(UTILISATION,0) AS UTILISATION,AVAILABLE_AMOUNT,NVL(MAIN_LINE,'NA') AS MAIN_LINE,AVAILABILITY_FLAG
							//FROM VW_LIMITS WHERE TRIM(LINE_ID) = TRIM(?) and CUSTOMER_NO = ? AND LINE_EXPIRY_DATE >=TO_DATE(SYSDATE, 'DD-MM-YY')
							pstmt1 = conn.prepareStatement(getQuery("GET_BALANCE"));
							pstmt1.setString(1, rset.getString(3));
							pstmt1.setString(2, custId);
							rset1 = pstmt1.executeQuery();
							if(rset1.next()) {
							
								BigDecimal lineIdLimAmt = new BigDecimal(rset1.getString(2));
								BigDecimal lineIdUtil = new BigDecimal(rset1.getString(3));
								BigDecimal lineIdNetAvailableBalance = lineIdLimAmt.subtract(lineIdUtil);
							
								if(rset1.getString(5).equals("NA")) {
									balanceFromApi.put("balance", lineIdNetAvailableBalance.toString());
									balanceFromApi.put("ResCode", 0);
									balanceFromApi.put("msg", "");
								
								}else {
								
									pstmt2 = conn.prepareStatement(getQuery("PARENT_LINE_DATA"));
									pstmt2.setString(1,rset1.getString(5));
									pstmt2.setString(2,custId);
									rset2 = pstmt2.executeQuery();
									if(rset2.next()) {
										BigDecimal parentLimAmt = new BigDecimal(rset2.getString("LIMIT_AMT"));
										BigDecimal parentUtil = new BigDecimal(rset2.getString("UTILISATION"));
										BigDecimal parentNetAvailableBalance = parentLimAmt.subtract(parentUtil);
									
										if(lineIdNetAvailableBalance.compareTo(parentNetAvailableBalance) == 1) {
											if(parentNetAvailableBalance.toString().contains("-")) {
												balanceFromApi.put("balance", "0.00");
											}else {
												balanceFromApi.put("balance", parentNetAvailableBalance.toString());
											}
										
										}else if(parentNetAvailableBalance.compareTo(lineIdNetAvailableBalance) == 1){
											if(lineIdNetAvailableBalance.toString().contains("-")) {
												balanceFromApi.put("balance", "0.00");
											}else {
												balanceFromApi.put("balance", lineIdNetAvailableBalance.toString());
											}
										}else {
											if(parentNetAvailableBalance.toString().contains("-")) {
												balanceFromApi.put("balance", "0.00");
											}else {
												balanceFromApi.put("balance", parentNetAvailableBalance.toString());
											}
										}
									}
									rset2.close();
									pstmt2.close();
									balanceFromApi.put("ResCode", "0");
									balanceFromApi.put("msg", "");	
								
								}	
							}
							rset1.close();
							pstmt1.close();
						}
					}
					rset.close();
					pstmt.close();
				}
				rset3.close();
				pstmt3.close();
			} catch (SQLException e) {
				log.info("SQLException---"+ e);
			}catch (JSONException e) {
				log.info("JSONException---"+e);
			} catch (Exception e) {
				log.info("Exception---"+e);
			}
			finally {

				try {
					if (rset2 != null) { //rset2,pstmt2,rset1,pstmt1 close in finally-2299
						rset2.close();
					}
					if (pstmt2 != null) {
						pstmt2.close();
					}
					if (rset1 != null) {
						rset1.close();
					}
					if (pstmt1 != null) {
						pstmt1.close();
					}
					if (rset != null) {
						rset.close();
					}
					if (pstmt != null) {
						pstmt.close();
					}
					if (conn != null) {
						closeConnection(conn,new Object(){}.getClass().getEnclosingMethod().getName().toString());
					}
				} catch (SQLException e) {
					log.info("SQLException ===", e);
				}
			}
			
			return balanceFromApi;
		}
		
		public JSONObject getCalculatedBalanceFromGetAll(String custId,String AccountNumber,String serviceName)
		{
			log.info(" calling  getCalculatedBalanceFromGetAll method  "+custId+" "+serviceName);
			JSONObject balanceFromApi =new JSONObject();
			boolean isBalanceFlag = false;
			
			try {
				JSONArray balanceArray= new JSONArray();
				JSONObject m_res_header =new JSONObject();
				m_res_header= GetAllAccountsReq(custId,"MCSC00");
				JSONArray AccountTypeList= new JSONArray();
				AccountTypeList = m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList");
				int sizeOfAccTypList = AccountTypeList.size();
				for(int i = 0;i<sizeOfAccTypList;i++) {
					if(AccountTypeList.getJSONObject(i).getString("accountType").equals("AOD")){
						JSONArray aODArray= new JSONArray();
						aODArray=m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").getJSONObject(i).getJSONArray("AccountDetails");
						int sizeOfAODArray= aODArray.size();
						for(int j = 0;j<sizeOfAODArray;j++) {
							balanceArray.add(aODArray.getJSONObject(j));
						}
					}
					else if(AccountTypeList.getJSONObject(i).getString("accountType").equals("LOD")){
						JSONArray aODArray= new JSONArray();
						aODArray=m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").getJSONObject(i).getJSONArray("AccountDetails");
						int sizeOfAODArray= aODArray.size();
						for(int j = 0;j<sizeOfAODArray;j++) {
							balanceArray.add(aODArray.getJSONObject(j));
						}
					}else if(AccountTypeList.getJSONObject(i).getString("accountType").equals("CA")){
						JSONArray aODArray= new JSONArray();
						aODArray=m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").getJSONObject(i).getJSONArray("AccountDetails");
						int sizeOfAODArray= aODArray.size();
						for(int j = 0;j<sizeOfAODArray;j++) {
							balanceArray.add(aODArray.getJSONObject(j));
						}
					}else if(AccountTypeList.getJSONObject(i).getString("accountType").equals("SA")){
						JSONArray aODArray= new JSONArray();
						aODArray=m_res_header.getJSONObject("ResBody").getJSONArray("AccountTypeList").getJSONObject(i).getJSONArray("AccountDetails");
						int sizeOfAODArray= aODArray.size();
						for(int j = 0;j<sizeOfAODArray;j++) {
							balanceArray.add(aODArray.getJSONObject(j));
						}
					}
				}
				int sizeOfBalanceArray = balanceArray.size();
				for(int i = 0;i<sizeOfBalanceArray;i++) {
					// Added by 2157 for check key on 02-10-2019
					if(balanceArray.getJSONObject(i).containsKey("AccountNumber") && AccountNumber.trim().equals(balanceArray.getJSONObject(i).getString("AccountNumber").trim())) 
					{
						JSONObject singleObj = new JSONObject();
						singleObj = balanceArray.getJSONObject(i);
						// Added by 2157 to check is child contains or not on 01-10-2019
						if(singleObj.containsKey("isChild")) 
						{
							if(singleObj.getBoolean("isChild") == false) {
								log.info("balance....."+singleObj.getString("balanceAvailable"));
								balanceFromApi.put("balance", singleObj.getString("balanceAvailable"));
								balanceFromApi.put("ResCode", 0);
								balanceFromApi.put("msg", "");
								isBalanceFlag = true;
							}else if(singleObj.getBoolean("isChild") == true){
								balanceFromApi.put("balance", singleObj.getJSONObject("childData").getJSONObject("StandAloneODAccountInFCC").getJSONObject("lineIdParentLine").getString("availableAmount"));
								balanceFromApi.put("ResCode", 0);
								balanceFromApi.put("msg", "");
								isBalanceFlag = true;
							}
						}
						else {
							balanceFromApi.put("balance", singleObj.getString("Balance"));
							balanceFromApi.put("ResCode", 0);
							balanceFromApi.put("msg", "");
							isBalanceFlag = true;
						}
						
						if(SERVICE_TYPE.equals("true"))
						{
							String ifscCode = getIfscCode(AccountNumber.trim());
							balanceFromApi.put("ifscCode", ifscCode);
						}
						else {
							balanceFromApi.put("ifscCode", "YESB0000001");
						}
					}
				}
				// Added by 2157 to check balance if APi is not give any balance on 02-10-2019
				if(!isBalanceFlag) 
				{
					balanceFromApi.put("balance", "0.00");
					balanceFromApi.put("ResCode", 0);
					balanceFromApi.put("msg", path.getMsg("E138"));
					String ifscCode = getIfscCode(AccountNumber.trim());
					balanceFromApi.put("ifscCode", ifscCode);
				}
				
				
			}catch (JSONException e) {
				log.info("JSONException---",e);
			} catch (Exception e) {
				log.info("Exception---"+e);
			}
			return balanceFromApi;
		}
		
		public String getIfscCode(String AccountNumber)
		{
			String ifscCode="";
			Connection conn=null;
			try{
				conn = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
		
				pstmt3 = conn.prepareStatement(getQuery("FETCH_IFSCCODE"));
				pstmt3.setString(1, AccountNumber);
				rset3 = pstmt3.executeQuery();
				if(rset3.next()) {
					ifscCode = rset3.getString(1);
				}else{
					ifscCode = AccountNumber;
				}
				rset3.close();
				pstmt3.close();
			}catch(SQLException ex) {
				log.info("sqlException...."+ex);
			}
			catch(Exception ex) {
				log.info("Exception...."+ex);
			}finally {
				if(rset3 != null) {
					try {
						rset3.close();
					} catch (SQLException e) {
						log.info("sqlException...."+e);
					}
				}
				if(pstmt3 != null) {
					try {
						pstmt3.close();
					} catch (SQLException e) {
						log.info("sqlException...."+e);
					}
				}
				if(conn != null) {
						closeConnection(conn,new Object(){}.getClass().getEnclosingMethod().getName().toString());
				}
			}
			return ifscCode;
		}
		
	public JSONObject requestForConfirmationOfRNB(String userID,String serviceName)
	{
		log.info("in requestForConfirmationOfRNB");

		for(String key : ServiceCallAPI.apiURLMasterList.keySet()){
			if(serviceName.toUpperCase().equals(key.trim().toUpperCase())){
				baseURL=ServiceCallAPI.apiURLMasterList.get(key);
				break;
			}
		}		
		currentts = sdf.format(date);
		currentts = currentts.replace(" ", "T");
		
		JSONObject m_res_header = new JSONObject();
		Gson g3=new Gson();
		Gson gson1=new Gson();
		
		LinkedHashMap<String,Object> main_req_header = new LinkedHashMap<String,Object>();
		
		main_req_header.put("RequesterID", ServiceCallAPI.serverRequesterID);
		main_req_header.put("requestRefNum",CommonUtility.getReqRefNum());
		main_req_header.put("customerID", userID);
		
		String request = g3.toJson(gson1.toJson(main_req_header,main_req_header.getClass()));
		request = CommonUtility.convertBasicString(request);
		String reqfrmAPI = "",apiResName = "";
		JSONObject reqJsonfrmAPI = null;
		
		if(SERVICE_TYPE.equals("true"))
		{
			try 
			{	
				reqfrmAPI = Utils.DataFromService(baseURL, request ,Authorization);
				reqJsonfrmAPI = JSONObject.fromObject(reqfrmAPI);
				apiResName = "ValidateRNBCustIDLockflagRes" ;
				m_res_header=CommonUtility.esbErrorHandlingForNonRnb(reqJsonfrmAPI,apiResName);
				
				if(reqJsonfrmAPI.containsKey("ErrorDetails"))
				{	
						return reqJsonfrmAPI;
				}
				
			} catch (JSONException e) 
			{
				log.error("JSONException---",e);			
			} catch (Exception e) {
				log.error("Exception---",e);
			}
		}
		else
		{
			reqJsonfrmAPI = new JSONObject();
			JSONObject result =new JSONObject();
			
			reqJsonfrmAPI.put("customerID", userID);
			reqJsonfrmAPI.put("requestRefNum", CommonUtility.getReqRefNum());
			reqJsonfrmAPI.put("lockFlag", "UNLOCK");
			reqJsonfrmAPI.put("statusCode", "1");
			reqJsonfrmAPI.put("statusDescription", "Customer is not Blocked");
			result.put("result", reqJsonfrmAPI);
			apiResName = "ValidateRNBCustIDLockflagRes" ;
			
			m_res_header=CommonUtility.esbErrorHandlingForNonRnb(result,apiResName);
		}
		log.info("response from handler "+m_res_header);
		return m_res_header;
	}
	
	public JSONObject createCRM(JSONObject caseDtl,String serviceName)
	{
		log.info(caseDtl+" in request for createCRM for "+serviceName);
		String customerID = caseDtl.getString("CustomerID").trim();
		String platform = caseDtl.getString("platform").trim();
		String remarks = caseDtl.getString("reqquery")!=null?caseDtl.getString("reqquery"):"";
		boolean mFlag=false;
		if (caseDtl.containsKey("mFlag") && caseDtl.getString("mFlag").equals("true")) {
			mFlag = true;
		}
		
		baseURL=ServiceCallAPI.apiURLMasterList.get(serviceName.toUpperCase())!=null ? ServiceCallAPI.apiURLMasterList.get(serviceName.toUpperCase()):"";		
		
		GeneralService generalService = new GeneralService(entityManager);
		
		JSONObject customerInfo = new JSONObject();
		customerInfo.put("CustomerId",customerID);
		customerInfo.put("ipAddress",caseDtl.getString("ipAddress"));
		customerInfo.put("platform",caseDtl.getString("platform"));
		customerInfo.put("header",caseDtl.getString("header"));
		customerInfo.put("mFlag",caseDtl.has("mFlag")?caseDtl.getString("mFlag"):"false");
		customerInfo.put("AuthId",caseDtl.has("AuthId")?caseDtl.getString("AuthId"):"");
		
		JSONObject m_res_header = new JSONObject();
		Gson g3=new Gson();
		Gson gson1=new Gson();
		
	/*	{
			  "CreateCaseReq": {
			    "ReqHdr": {
			      "ConsumerContext": {
			        "RequesterID": "SME"
			      },
			      "ServiceContext": {
			        "ServiceName": "CaseManagement",
			        "ReqRefNum": "999941634810171",
			        "ReqRefTimeStamp": "2021-10-21T09:56:11.5749656+05:30",
			        "ServiceVersionNo": "1.0"
			      }
			    },
			    "ReqBody": {
			      "CustomerId": "330",
			      "IssueTypeCode": "9",
			      "Source": "YES MSME",
			      "Channel": "Digital Channel",
			      "Remarks": "My remark added here"
			    }
			  }
			}*/
		
		//-------------------------------------------------

		LinkedHashMap<String, Object> main_req_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> sub_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> sub_header_body = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> s1_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> s11_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> s12_header = new LinkedHashMap<String, Object>();

		s11_header.put("RequesterID", ServiceCallAPI.serverRequesterID);
		
		s1_header.put("ConsumerContext", gson1.fromJson(g3.toJson(s11_header), s11_header.getClass()));

		s12_header.put("ServiceName", "CaseManagement");
		s12_header.put("ReqRefNum", CommonUtility.getReqRefNum());
		s12_header.put("ReqRefTimeStamp", CommonUtility.getCurrentTimeStamp());
		s12_header.put("ServiceVersionNo", "1.0");

		s1_header.put("ServiceContext", gson1.fromJson(g3.toJson(s12_header), s12_header.getClass()));

		sub_header.put("ReqHdr", gson1.fromJson(g3.toJson(s1_header), s1_header.getClass()));

		String issueTypeCode = "";
		

		
		//get issueTypeCode from issueName with help of json array from cache
//		if(caseDtl.has("issueType")) {
//			org.json.JSONObject issueNameJsonArray = ApplicationGenParameter.issueNameJsonArray;
//			String issueName = caseDtl.getString("issueType").trim();
//
//			if(issueNameJsonArray.has(issueName) && issueNameJsonArray.getJSONObject(issueName).has("ISSUEID")) {
//				issueTypeCode=issueNameJsonArray.getJSONObject(issueName).getString("ISSUEID");
//			}
//		}
				
		//---------------------------Tat CR new requirement------------
		
		JSONObject tatData=new JSONObject();
		tatData=getTeamAndTurnAroundTime(issueTypeCode);
		//String assignTeam=tatData.getString("assignTeam");
		String turnAroundTime=tatData.getString("turnaroundTime");
		
		//	------------------------
		
		log.info("response from turnAroundTime "+turnAroundTime);
		
		sub_header_body.put("CustomerId",customerID);
		sub_header_body.put("IssueTypeCode", issueTypeCode);
		sub_header_body.put("Source", "YES MSME");
		sub_header_body.put("Channel", "Digital Channel");
		sub_header_body.put("Remarks", remarks);
		
		sub_header.put("ReqBody",sub_header_body);

		main_req_header.put("CreateCaseReq", gson1.fromJson(g3.toJson(sub_header), sub_header.getClass()));

		String request = g3.toJson(gson1.toJson(main_req_header, main_req_header.getClass()));
		request = CommonUtility.convertBasicString(request);
		
		String reqfrmAPI = "",apiResName = "CreateCaseRes";
		JSONObject reqJsonfrmAPI = null;
		String caseId=""; 
		String fileName ="",tempFileName="",custName = "";
		String requestData= baseURL+"|"+request.toString()+"|"+Authorization;
		
		JSONObject caseInfo = new JSONObject();
		caseInfo.put("CustomerId",customerID);
		caseInfo.put("REMARKS",remarks);
		caseInfo.put("platform",platform);
		caseInfo.put("ISSUE_ID",issueTypeCode);
		caseInfo.put("mFlag",caseDtl.has("mFlag")?caseDtl.getString("mFlag"):"false");
		caseInfo.put("AuthId",caseDtl.has("AuthId")?caseDtl.getString("AuthId"):"");
		
		if(SERVICE_TYPE.equals("true"))
		{
			//--------------------CRM details insert into table--------------------------------
			Integer dtlId =0;
			try {
				
				dtlId = maintainCRM_Details(caseInfo,"I");
				
				log.info("dtlId="+dtlId);
				
			} catch (Exception e1) {
				log.info("CRM details insert="+e1);
			}
			
			//--------------------API Audit--------Request------------------------
			String SRN ="";
			try {
				SRN = generalService.logApiDtl(requestData,serviceName,customerInfo,"I");
			} catch (Exception e1) {
				log.info("request="+e1);
			}
			
			try 
			{	
				//if testing purpose for local
				//String request1="{\"result\":{\"CreateCaseRes\":{\"ResHdr\":{\"ConsumerContext\":{\"RequesterID\":\"SME\"},\"ServiceContext\":{\"ServiceName\":\"CaseManagement\",\"ReqRefNum\":\"999941634810171\",\"ReqRefTimeStamp\":\"2021-10-21T09:56:11.5749656+05:30\",\"ServiceVersionNo\":\"1.0\"},\"ServiceResponse\":{\"EsbResTimeStamp\":\"2022-11-17 16:48:55.928087\",\"EsbResStatus\":\"0\"}},\"ResBody\":{\"CaseID\":\"CE070122517091\"}}}}";			

				reqfrmAPI = Utils.DataFromService(baseURL, request ,Authorization);
				reqJsonfrmAPI = JSONObject.fromObject(reqfrmAPI);
				
				m_res_header=CommonUtility.esbErrorHandling(reqJsonfrmAPI,apiResName);
				
				//--------------------API Audit-------Response-------------------------
				generalService.logApiDtl(m_res_header.toString(),serviceName,customerInfo,SRN+"~U");
				
				//--------------------CRM details Update into table--------------------------------
				try {
					String csID="";
					if(m_res_header.has("ResBody") && m_res_header.getJSONObject("ResBody").has("CaseID")) {

						csID=m_res_header.getJSONObject("ResBody").getString("CaseID");
						
						//-----------------TAT Cr new responce -------
						m_res_header.getJSONObject("ResBody").put("turnAroundTime", turnAroundTime + " Days");
						//--------------------------------------------
						log.info("request m_res_header="+m_res_header);
					}
					
					caseInfo.put("API_CASE_ID",csID);
					
					if(csID!=null && !csID.isEmpty()) {
						caseInfo.put("REASON",m_res_header.getJSONObject("ResBody"));
						caseInfo.put("STATUS","SUCCESS");
					}
					else {
						if(reqJsonfrmAPI.has("ErrorDetails")) {
							caseInfo.put("REASON",reqJsonfrmAPI.get("ErrorDetails"));
						}
						else if(m_res_header.has("ErrorDetails"))  {
							caseInfo.put("REASON",m_res_header.get("ErrorDetails"));
						}
						else {
							caseInfo.put("REASON","ErrorDetails not found ");
						}
						caseInfo.put("STATUS","ERROR");
					}
					caseInfo.put("ID",dtlId);

					maintainCRM_Details(caseInfo,"U"); //update details here

				} catch (Exception e) {
					// TODO Auto-generated catch block
					log.error("JSONException---",e);	
				}
				
				log.info("reqJsonfrmAPI="+reqJsonfrmAPI);
				
				if(m_res_header.has("ResBody") && m_res_header.getJSONObject("ResBody").has("CaseID")) {

					caseId=m_res_header.getJSONObject("ResBody").getString("CaseID");
					

					//----------------------------CRM PDF generatin code start here----------------------------
					fileName = "CRM_"+customerID+"_"+caseId;
					tempFileName = "CRM_Temp_"+caseId;

					//----------------------------get customer name by Cust Id from view--------------------------
					JSONObject custDtlJson =getCustomerInfoFromCustId(customerID,mFlag);

					if(custDtlJson.has("custName") && !custDtlJson.getString("custName").equals("Not Found")) {
						custName = custDtlJson.getString("custName").trim();
					}

					caseDtl.put("CaseId", caseId);
					caseDtl.put("custName", custName);	
					caseDtl.put("issueID", issueTypeCode);	

					m_res_header.put("CaseId", caseId);
					m_res_header.put("fileName", fileName+".pdf");
					//m_res_header.put("base64Data", CommonUtility.generateCRM_PDF(reqJsonfrmAPI,caseDtl,fileName ,tempFileName,apiResName));

				}
				else {
					m_res_header.put("CaseId", caseId);
				}
				
			} catch (JSONException e) 
			{
				log.error("JSONException---",e);			
			} catch (Exception e) {
				log.error("Exception---",e);
			}
		}
		else
		{
			String newCaseId=caseIdGenerate( );
         
			String SRN = generalService.logApiDtl(requestData,serviceName,customerInfo,"I");
			
			//--------------------CRM details insert into table-------------start-------------------
			Integer dtlId =0;
			try {
				
				dtlId = maintainCRM_Details(caseInfo,"I");
				
				log.info("dtlId="+dtlId);
				
			} catch (Exception e1) {
				log.info("CRM details insert="+e1);
			}
			
			//--------------------CRM details insert into table--------------end------------------
			
			
			//Old Response
			//String respString="{\"result\":{\"CreateCaseRes\":{\"ResHdr\":{\"ConsumerContext\":{\"RequesterID\":\"WRK\"},\"ServiceContext\":{\"ServiceName\":\"CaseManagement\",\"ReqRefNum\":\"16612100542\",\"ReqRefTimeStamp\":\"2022-08-24 12:33:04.568750\",\"ServiceVersionNo\":\"1.0\"},\"ServiceResponse\":{\"EsbResTimeStamp\":\"2022-08-24 12:33:04.579142\",\"EsbResStatus\":\"1\"},\"ErrorDetails\":[{\"ErrorInfo\":{\"ErrSrc\":\"GENIE_CREATECASE_API\",\"EsbErrCode\":\"500\",\"EsbErrLongDesc\":\"Unknown reason\",\"EsbErrShortDesc\":\"Please contact administrator\"}}]}}}}";
			
			//New Response Adding TAT in response
			String respString="{\"result\":{\"CreateCaseRes\":{\"ResHdr\":{\"ConsumerContext\":{\"RequesterID\":\"SME\"},\"ServiceContext\":{\"ServiceName\":\"CaseManagement\",\"ReqRefNum\":\"999941634810171\",\"ReqRefTimeStamp\":\"2021-10-21T09:56:11.5749656+05:30\",\"ServiceVersionNo\":\"1.0\"},\"ServiceResponse\":{\"EsbResTimeStamp\":\"2022-11-17 16:48:55.928087\",\"EsbResStatus\":\"0\"}},\"ResBody\":{\"CaseID\":\""+newCaseId+"\",\"turnAroundTime\":\""+ turnAroundTime  +" Days"+ "\"}}}}";			
			
			
			JSONObject reqJsonfrmAPILocal = JSONObject.fromObject(respString);
			
			m_res_header=CommonUtility.esbErrorHandling(reqJsonfrmAPILocal,apiResName);
			
			//---------------------API Audit--------------------------------
			generalService.logApiDtl(m_res_header.toString(),serviceName,customerInfo,SRN+"~U");
			
			//--------------------CRM details Update into table----------------start----------------
			try {
				String csID="";
				if(m_res_header.has("ResBody") && m_res_header.getJSONObject("ResBody").has("CaseID")) {
					csID=m_res_header.getJSONObject("ResBody").getString("CaseID");
				}
				
				caseInfo.put("API_CASE_ID",csID);
				log.info("request="+m_res_header);
				
				if(csID!=null && !csID.isEmpty()) {
					caseInfo.put("REASON",m_res_header.getJSONObject("ResBody"));
					caseInfo.put("STATUS","SUCCESS");
				}
				else {
					
					if(reqJsonfrmAPILocal.has("ErrorDetails")) {
						caseInfo.put("REASON",reqJsonfrmAPILocal.get("ErrorDetails"));
					}
					else if(m_res_header.has("ErrorDetails"))  {
						caseInfo.put("REASON",m_res_header.get("ErrorDetails"));
					}
					else {
						caseInfo.put("REASON","ErrorDetails not found ");
					}
					
					caseInfo.put("STATUS","ERROR");
				}
				
				caseInfo.put("ID",dtlId);

				maintainCRM_Details(caseInfo,"U"); //update details here

			} catch (Exception e) {
				log.info("Exception="+e);
			}
		
			if(m_res_header.has("ResBody") && m_res_header.getJSONObject("ResBody").has("CaseID")) {
				
				caseId=m_res_header.getJSONObject("ResBody").getString("CaseID");
				
				//----------------------------CRM PDF generation code start here----------------------------
				fileName = "CRM_"+customerID+"_"+caseId;
				tempFileName = "CRM_Temp_"+caseId;
				
				//get customer name by Cust Id from view
				JSONObject custDtlJson =getCustomerInfoFromCustId(customerID,mFlag);
			
				if(custDtlJson.has("custName") && !custDtlJson.getString("custName").equals("Not Found")) {
					custName = custDtlJson.getString("custName").trim();
				}
				
				caseDtl.put("CaseId", caseId);
				caseDtl.put("custName", custName);
				caseDtl.put("issueID", issueTypeCode);	
				
				m_res_header.put("CaseId", caseId);
				m_res_header.put("fileName", fileName+".pdf");
				//m_res_header.put("base64Data", CommonUtility.generateCRM_PDF(reqJsonfrmAPILocal,caseDtl,fileName ,tempFileName,apiResName));
			}
			else {
				m_res_header.put("CaseId", caseId);
			}
			
		}
		return m_res_header;
	}
	

	public JSONObject caseTrack(JSONObject caseDtl,String serviceName)
	{
		log.info(caseDtl+" in request For caseTrack for "+serviceName);
		String customerID = caseDtl.getString("CustomerID").trim();
		String caseId = caseDtl.has("CaseId")? caseDtl.getString("CaseId").trim():"";
		String fromDate =  caseDtl.has("StartDate")? caseDtl.getString("StartDate").trim():"";
		String toDate =caseDtl.has("EndDate")? caseDtl.getString("EndDate").trim():"";
		GeneralService generalService = new GeneralService(entityManager);
		String SRN ="";
		
		JSONObject customerInfo = new JSONObject();
		customerInfo.put("CustomerId",customerID);
		customerInfo.put("ipAddress",caseDtl.getString("ipAddress"));
		customerInfo.put("platform",caseDtl.getString("platform"));
		customerInfo.put("header",caseDtl.getString("header"));
		customerInfo.put("mFlag",caseDtl.has("mFlag")?caseDtl.getString("mFlag"):"false");
		customerInfo.put("AuthId",caseDtl.has("AuthId")?caseDtl.getString("AuthId"):"");
		
		baseURL=ServiceCallAPI.apiURLMasterList.get(serviceName.toUpperCase())!=null ? ServiceCallAPI.apiURLMasterList.get(serviceName.toUpperCase()):"";
				
		JSONObject m_res_header = new JSONObject();
		Gson g3=new Gson();
		Gson gson1=new Gson();
		
	/*	{
		  "GetCasesForCustomerReq": {
		    "ReqHdr": {
		      "ConsumerContext": {
		        "RequesterID": "SME"
		      },
		      "ServiceContext": {
		        "ServiceName": "CaseManagement",
		        "ReqRefNum": "999941634810171",
		        "ReqRefTimeStamp": "2021-10-21T09:56:11.5749656+05:30",
		        "ServiceVersionNo": "1.0"
		      }
		    },
		    "ReqBody": {
		      "CustomerId": "330",
		      
		      "CaseId": "12121212121212",// not in use
		      ----or-------
		      "CaseCreationDateFilter": {
		        "StartDate": "2021-05-05",
		        "EndDate": "2021-05-21"
		      }
		      
		    }
		  }
		}*/
		
		//-------------------------------------------------

		LinkedHashMap<String, Object> main_req_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> sub_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> sub_header_body = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> sub_sub_header_body = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> s1_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> s11_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> s12_header = new LinkedHashMap<String, Object>();

		s11_header.put("RequesterID", ServiceCallAPI.serverRequesterID);
		
		s1_header.put("ConsumerContext", gson1.fromJson(g3.toJson(s11_header), s11_header.getClass()));

		s12_header.put("ServiceName", "CaseManagement");
		s12_header.put("ReqRefNum", CommonUtility.getReqRefNum());
		s12_header.put("ReqRefTimeStamp", CommonUtility.getCurrentTimeStamp());
		s12_header.put("ServiceVersionNo", "1.0");

		s1_header.put("ServiceContext", gson1.fromJson(g3.toJson(s12_header), s12_header.getClass()));

		sub_header.put("ReqHdr", gson1.fromJson(g3.toJson(s1_header), s1_header.getClass()));

		sub_header_body.put("CustomerId",customerID);
		
		if(CommonUtility.caseID_length.equals(caseId.length())) {// pass any one from case id or Date Period for track case
			sub_header_body.put("CaseId", caseId);// not in use currently
		}
		//------------------or--------------------------
		else{
			sub_sub_header_body.put("StartDate", CommonUtility.getNewDateFormatFromStringDate(fromDate));
			sub_sub_header_body.put("EndDate", CommonUtility.getNewDateFormatFromStringDate(toDate));
			
			sub_header_body.put("CaseCreationDateFilter", gson1.fromJson(g3.toJson(sub_sub_header_body), sub_sub_header_body.getClass()));
		}
		
				
		sub_header.put("ReqBody",sub_header_body);

		main_req_header.put("GetCasesForCustomerReq", gson1.fromJson(g3.toJson(sub_header), sub_header.getClass()));

		String request = g3.toJson(gson1.toJson(main_req_header, main_req_header.getClass()));
		request = CommonUtility.convertBasicString(request);
		
		String reqfrmAPI = "",apiResName = "GetCasesForCustomerRes";
		JSONObject reqJsonfrmAPI = null;
		String requestData= baseURL+"|"+request.toString()+"|"+Authorization;
		
		if(SERVICE_TYPE.equals("true"))
		{
			//--------------------API Audit--------Request------------------------
			try {
				SRN = generalService.logApiDtl(requestData,serviceName,customerInfo,"I");
			} catch (Exception e1) {
				log.info("request="+e1);
			}
			
			try 
			{	
				reqfrmAPI = Utils.DataFromService(baseURL, request ,Authorization);
				reqJsonfrmAPI = JSONObject.fromObject(reqfrmAPI);
				m_res_header=CommonUtility.esbErrorHandling(reqJsonfrmAPI,apiResName);

				//------------------------------------------JSON response changes starts here---------------------------------------------------
				processAPIResponseForFrontEnd(m_res_header,"CaseDetailsList");
				
				//--------------------API Audit-------Response-------------------------
				
				JSONObject m_res_If_error = new JSONObject();
				
				if(m_res_header.has("ResCode") && m_res_header.getString("ResCode").equals("0")) {
					m_res_If_error.put("STATUS","Response processed Successfully");// to avoid large response storing into DB
				}
				else {
					m_res_If_error = m_res_header;
				}
				
				generalService.logApiDtl(m_res_If_error.toString(),serviceName,customerInfo,SRN+"~U");
				
			} catch (JSONException e) 
			{
				log.error("JSONException---",e);			
			} catch (Exception e) {
				log.error("Exception---",e);
			}
		}
		else
		{
			//--------------------API Audit--------Request------------------------
			try {
				SRN = generalService.logApiDtl(requestData,serviceName,customerInfo,"I");
				
			} catch (Exception e1) {
				log.info("request="+e1);
			}
			
			// SUCCESS JSON response
			String respString = "{\"result\":{\"GetCasesForCustomerRes\":{\"ResHdr\":{\"ConsumerContext\":{\"RequesterID\":\"SME\"},\"ServiceContext\":{\"ServiceName\":\"CaseManagement\",\"ReqRefNum\":\"6686597253691\",\"ReqRefTimeStamp\":\"2022-11-17T10:05:25\",\"ServiceVersionNo\":\"1.0\"},\"ServiceResponse\":{\"EsbResTimeStamp\":\"2022-11-17 10:05:28.533263\",\"EsbResStatus\":\"0\"}},\"ResBody\":{\"CaseDetailsList\":[{\"ID\":\"CS050922000022\",\"CreatedDate\":\"05-09-2022 12:44:05 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"05-09-2022 12:44:05 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Source account details related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS051122000011\",\"CreatedDate\":\"05-11-2022 12:02:26 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"10-11-2022 11:00:06 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Statement downloading inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS051122000013\",\"CreatedDate\":\"05-11-2022 12:32:58 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"10-11-2022 11:00:06 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Payment related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS230922000003\",\"CreatedDate\":\"23-09-2022 12:47:48 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"23-09-2022 12:47:48 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"MMID generation inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS230922000004\",\"CreatedDate\":\"23-09-2022 12:48:39 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"23-09-2022 12:48:39 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"MMID generation inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS230922000006\",\"CreatedDate\":\"23-09-2022 12:49:05 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"23-09-2022 12:49:05 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"MMID generation inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS250822000245\",\"CreatedDate\":\"25-08-2022 05:45:23 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"25-08-2022 05:45:23 PM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Unable to generate/ view virtual debit card\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS250822000251\",\"CreatedDate\":\"25-08-2022 05:49:23 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"25-08-2022 05:49:23 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Source account details related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS050922000043\",\"CreatedDate\":\"05-09-2022 04:36:00 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"05-09-2022 04:36:00 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Source account details related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS111122000012\",\"CreatedDate\":\"11-11-2022 03:21:37 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"16-11-2022 11:00:24 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Other technical queries\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS170922000002\",\"CreatedDate\":\"17-09-2022 10:42:09 AM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"17-09-2022 10:42:09 AM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Delay in payment of inward remittances\",\"ProductTypeCode\":\"CASA\"},{\"ID\":\"CS170922000003\",\"CreatedDate\":\"17-09-2022 12:15:38 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"17-09-2022 12:15:38 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Source account details related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS120922000061\",\"CreatedDate\":\"12-09-2022 04:57:42 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"12-09-2022 04:57:42 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Source account details related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS120922000064\",\"CreatedDate\":\"12-09-2022 05:05:44 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"12-09-2022 05:05:44 PM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Unable to authorized bulk transaction\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS120922000065\",\"CreatedDate\":\"12-09-2022 05:06:23 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"12-09-2022 05:06:23 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Source account details related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS250822000240\",\"CreatedDate\":\"25-08-2022 05:42:11 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"25-08-2022 05:42:11 PM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Unable to book FD/ RD\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS250822000237\",\"CreatedDate\":\"25-08-2022 05:40:20 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"25-08-2022 05:40:20 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Statement downloading inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS250822000239\",\"CreatedDate\":\"25-08-2022 05:42:09 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"25-08-2022 05:42:09 PM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Unable to authorized single transaction\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS250822000241\",\"CreatedDate\":\"25-08-2022 05:43:29 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"25-08-2022 05:43:29 PM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Unable to create/ modify maker details\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS250822000247\",\"CreatedDate\":\"25-08-2022 05:46:40 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"25-08-2022 05:46:40 PM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Unable to upload document\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS141022000003\",\"CreatedDate\":\"14-10-2022 03:14:34 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"14-10-2022 03:14:34 PM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Unable to pay tax\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS091122000005\",\"CreatedDate\":\"09-11-2022 12:15:53 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"14-11-2022 11:00:08 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Source account details related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS260922000002\",\"CreatedDate\":\"26-09-2022 11:39:54 AM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"26-09-2022 11:39:54 AM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Unable to authorized single transaction\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS041022000064\",\"CreatedDate\":\"04-10-2022 12:17:27 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"04-10-2022 12:17:27 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Payment related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS181022000001\",\"CreatedDate\":\"18-10-2022 09:17:08 AM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"18-10-2022 09:17:08 AM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Source account details related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS181022000004\",\"CreatedDate\":\"18-10-2022 10:33:13 AM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"18-10-2022 10:33:13 AM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Unable to upload document\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS181022000005\",\"CreatedDate\":\"18-10-2022 10:41:07 AM\",\"StatusText\":\"CLOSED\",\"ModifiedDate\":\"18-10-2022 10:41:07 AM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Unable to authorized bulk transaction\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS290822000001\",\"CreatedDate\":\"29-08-2022 09:39:07 AM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"29-08-2022 09:39:07 AM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Source account details related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS050922000021\",\"CreatedDate\":\"05-09-2022 12:37:29 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"05-09-2022 12:37:29 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Source account details related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS101122000058\",\"CreatedDate\":\"10-11-2022 06:20:58 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"10-11-2022 06:20:58 PM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Delay in payment of inward remittances\",\"ProductTypeCode\":\"CASA\"},{\"ID\":\"CS041022000062\",\"CreatedDate\":\"04-10-2022 12:11:31 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"04-10-2022 12:11:31 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"MMID generation inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS041022000065\",\"CreatedDate\":\"04-10-2022 12:20:24 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"04-10-2022 12:20:24 PM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Unable to fetch CVV\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS131022000001\",\"CreatedDate\":\"13-10-2022 09:41:06 AM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"13-10-2022 09:41:06 AM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Delay in payment of inward remittances\",\"ProductTypeCode\":\"CASA\"},{\"ID\":\"CS171022000016\",\"CreatedDate\":\"17-10-2022 04:23:43 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"17-10-2022 04:23:43 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Source account details related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS171022000017\",\"CreatedDate\":\"17-10-2022 04:24:59 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"17-10-2022 04:24:59 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Source account details related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS230922000005\",\"CreatedDate\":\"23-09-2022 12:49:16 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"23-09-2022 12:49:16 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"MMID generation inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS071122000030\",\"CreatedDate\":\"07-11-2022 04:55:00 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"12-11-2022 11:00:06 PM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Unable to upload document\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS071122000003\",\"CreatedDate\":\"07-11-2022 10:49:06 AM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"12-11-2022 11:00:06 PM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Not able to view transaction limits\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS071122000024\",\"CreatedDate\":\"07-11-2022 04:04:41 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"12-11-2022 11:00:06 PM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Unable to authorized single transaction\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS140922000032\",\"CreatedDate\":\"14-09-2022 03:38:52 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"14-09-2022 03:38:52 PM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Unable to fetch CVV\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS200922000087\",\"CreatedDate\":\"20-09-2022 06:31:57 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"20-09-2022 06:31:57 PM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Delay in payment of inward remittances\",\"ProductTypeCode\":\"CASA\"},{\"ID\":\"CS071122000031\",\"CreatedDate\":\"07-11-2022 04:57:07 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"12-11-2022 11:00:06 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Source account details related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS270922000002\",\"CreatedDate\":\"27-09-2022 10:15:23 AM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"27-09-2022 10:15:23 AM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Delay in payment of inward remittances\",\"ProductTypeCode\":\"CASA\"},{\"ID\":\"CS141022000002\",\"CreatedDate\":\"14-10-2022 03:13:55 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"14-10-2022 03:13:55 PM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Delay in payment of inward remittances\",\"ProductTypeCode\":\"CASA\"},{\"ID\":\"CS141022000006\",\"CreatedDate\":\"14-10-2022 04:43:07 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"14-10-2022 04:43:07 PM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Unable to pay tax\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS250822000250\",\"CreatedDate\":\"25-08-2022 05:48:15 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"25-08-2022 05:48:15 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Payment related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS250822000232\",\"CreatedDate\":\"25-08-2022 05:34:14 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"25-08-2022 05:34:14 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"MMID generation inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS250822000238\",\"CreatedDate\":\"25-08-2022 05:40:44 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"25-08-2022 05:40:44 PM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Unable to authorized bulk transaction\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS181022000002\",\"CreatedDate\":\"18-10-2022 09:18:14 AM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"18-10-2022 09:18:14 AM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Unable to fetch CVV\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS281022000001\",\"CreatedDate\":\"28-10-2022 09:32:02 AM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"28-10-2022 09:32:02 AM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Source account details related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS250822000243\",\"CreatedDate\":\"25-08-2022 05:44:48 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"25-08-2022 05:44:48 PM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Unable to fetch CVV\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS250822000249\",\"CreatedDate\":\"25-08-2022 05:48:23 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"25-08-2022 05:48:23 PM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Unable to view trade summary details\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS120922000068\",\"CreatedDate\":\"12-09-2022 05:10:34 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"12-09-2022 05:10:34 PM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Delay in payment of inward remittances\",\"ProductTypeCode\":\"CASA\"},{\"ID\":\"CS120922000070\",\"CreatedDate\":\"12-09-2022 05:11:25 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"12-09-2022 05:11:25 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Source account details related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS140922000034\",\"CreatedDate\":\"14-09-2022 04:13:56 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"14-09-2022 04:13:56 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Source account details related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS041122000012\",\"CreatedDate\":\"04-11-2022 01:43:55 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"09-11-2022 10:59:50 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Source account details related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS041122000013\",\"CreatedDate\":\"04-11-2022 01:45:43 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"04-11-2022 01:45:43 PM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Delay in payment of inward remittances\",\"ProductTypeCode\":\"CASA\"},{\"ID\":\"CS041122000014\",\"CreatedDate\":\"04-11-2022 01:48:21 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"09-11-2022 10:59:50 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Positive pay technical query\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS041122000015\",\"CreatedDate\":\"04-11-2022 02:04:16 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"09-11-2022 10:59:51 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Source account details related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS041122000016\",\"CreatedDate\":\"04-11-2022 02:05:37 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"04-11-2022 02:05:37 PM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Delay in payment of inward remittances\",\"ProductTypeCode\":\"CASA\"},{\"ID\":\"CS041122000017\",\"CreatedDate\":\"04-11-2022 02:09:22 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"09-11-2022 10:59:51 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Source account details related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS051122000004\",\"CreatedDate\":\"05-11-2022 11:14:58 AM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"10-11-2022 11:00:07 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"MMID generation inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS051122000008\",\"CreatedDate\":\"05-11-2022 11:51:54 AM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"10-11-2022 11:00:07 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Other technical queries\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS051122000010\",\"CreatedDate\":\"05-11-2022 11:56:41 AM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"10-11-2022 11:00:07 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Source account details related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS051122000012\",\"CreatedDate\":\"05-11-2022 12:21:18 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"10-11-2022 11:00:07 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"MMID generation inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS250822000233\",\"CreatedDate\":\"25-08-2022 05:37:36 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"25-08-2022 05:37:36 PM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Not able to view transaction limits\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS250822000235\",\"CreatedDate\":\"25-08-2022 05:39:19 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"25-08-2022 05:39:19 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Positive pay technical query\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS041022000066\",\"CreatedDate\":\"04-10-2022 12:24:40 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"04-10-2022 12:24:40 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Source account details related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS071122000001\",\"CreatedDate\":\"07-11-2022 10:14:00 AM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"12-11-2022 11:00:06 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Payment related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS041022000067\",\"CreatedDate\":\"04-10-2022 12:28:40 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"04-10-2022 12:28:40 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Payment related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS051122000005\",\"CreatedDate\":\"05-11-2022 11:20:54 AM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"10-11-2022 11:00:07 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Other technical queries\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS051122000009\",\"CreatedDate\":\"05-11-2022 11:53:14 AM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"10-11-2022 11:00:07 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"MMID generation inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS051122000006\",\"CreatedDate\":\"05-11-2022 11:36:37 AM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"10-11-2022 11:00:07 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Source account details related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS051122000007\",\"CreatedDate\":\"05-11-2022 11:44:04 AM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"05-11-2022 11:44:04 AM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Delay in payment of inward remittances\",\"ProductTypeCode\":\"CASA\"},{\"ID\":\"CS091122000006\",\"CreatedDate\":\"09-11-2022 12:46:15 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"14-11-2022 11:00:09 PM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Unable to authorized bulk transaction\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS091122000007\",\"CreatedDate\":\"09-11-2022 01:01:54 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"14-11-2022 11:00:09 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"MMID generation inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS051122000002\",\"CreatedDate\":\"05-11-2022 11:01:28 AM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"10-11-2022 11:00:08 PM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Unable to fetch CVV\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS160922000011\",\"CreatedDate\":\"16-09-2022 05:04:05 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"16-09-2022 05:04:05 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Source account details related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS111122000005\",\"CreatedDate\":\"11-11-2022 12:28:05 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"16-11-2022 11:00:33 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Source account details related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS111122000006\",\"CreatedDate\":\"11-11-2022 12:28:40 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"11-11-2022 12:28:40 PM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Delay in payment of inward remittances\",\"ProductTypeCode\":\"CASA\"},{\"ID\":\"CS111122000008\",\"CreatedDate\":\"11-11-2022 12:44:23 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"16-11-2022 11:00:33 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Other technical queries\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS160922000010\",\"CreatedDate\":\"16-09-2022 04:57:36 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"16-09-2022 04:57:36 PM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Not able to view transaction limits\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS121022000002\",\"CreatedDate\":\"12-10-2022 10:03:34 AM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"12-10-2022 10:03:34 AM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Source account details related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS130922000001\",\"CreatedDate\":\"13-09-2022 08:12:47 AM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"13-09-2022 08:12:47 AM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Source account details related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS150922000005\",\"CreatedDate\":\"15-09-2022 11:18:39 AM\",\"StatusText\":\"CLOSED\",\"ModifiedDate\":\"15-09-2022 11:56:58 AM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Unable to pay tax\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS270922000001\",\"CreatedDate\":\"27-09-2022 10:15:13 AM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"27-09-2022 10:15:13 AM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Delay in payment of inward remittances\",\"ProductTypeCode\":\"CASA\"},{\"ID\":\"CS141122000021\",\"CreatedDate\":\"14-11-2022 01:05:33 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"14-11-2022 01:05:33 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Other technical queries\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS190922000004\",\"CreatedDate\":\"19-09-2022 10:59:19 AM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"19-09-2022 10:59:19 AM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Payment related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS190922000010\",\"CreatedDate\":\"19-09-2022 11:13:00 AM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"19-09-2022 11:13:00 AM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Unable to authorized bulk transaction\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS190922000011\",\"CreatedDate\":\"19-09-2022 11:15:23 AM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"19-09-2022 11:15:23 AM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Payment related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS260822000016\",\"CreatedDate\":\"26-08-2022 10:37:28 AM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"26-08-2022 10:37:28 AM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Source account details related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS050922000028\",\"CreatedDate\":\"05-09-2022 03:33:10 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"05-09-2022 03:33:10 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Source account details related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS260822000023\",\"CreatedDate\":\"26-08-2022 03:58:01 PM\",\"StatusText\":\"CLOSED\",\"ModifiedDate\":\"29-08-2022 07:05:45 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Source account details related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS250822000234\",\"CreatedDate\":\"25-08-2022 05:37:55 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"25-08-2022 05:37:55 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Other technical queries\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS250822000236\",\"CreatedDate\":\"25-08-2022 05:39:28 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"25-08-2022 05:39:28 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Statement downloading inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS250822000242\",\"CreatedDate\":\"25-08-2022 05:43:23 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"25-08-2022 05:43:23 PM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Unable to delete beneficiary details\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS250822000246\",\"CreatedDate\":\"25-08-2022 05:45:31 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"25-08-2022 05:45:31 PM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Unable to pay tax\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS250822000248\",\"CreatedDate\":\"25-08-2022 05:46:54 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"25-08-2022 05:46:54 PM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Unable to delete beneficiary details\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS290922000368\",\"CreatedDate\":\"29-09-2022 05:27:54 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"06-10-2022 11:00:10 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Inquiry on opening of ISA Account\",\"ProductTypeCode\":\"Mutual Funds\"},{\"ID\":\"CS190922000018\",\"CreatedDate\":\"19-09-2022 12:44:42 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"19-09-2022 12:51:55 PM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Unable to fetch CVV\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS260822000022\",\"CreatedDate\":\"26-08-2022 03:33:53 PM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"26-08-2022 03:33:53 PM\",\"Category\":\"Query\",\"IssueTypeCode\":\"Source account details related inquiry\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS200922000001\",\"CreatedDate\":\"20-09-2022 10:34:32 AM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"20-09-2022 10:34:32 AM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Unable to pay tax\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS280922000010\",\"CreatedDate\":\"28-09-2022 11:21:15 AM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"28-09-2022 11:21:15 AM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Unable to authorized bulk transaction\",\"ProductTypeCode\":\"Yes MSME\"},{\"ID\":\"CS190922000003\",\"CreatedDate\":\"19-09-2022 10:53:04 AM\",\"StatusText\":\"OPEN\",\"ModifiedDate\":\"19-09-2022 10:53:04 AM\",\"Category\":\"Complaint\",\"IssueTypeCode\":\"Unable to book FD/ RD\",\"ProductTypeCode\":\"Yes MSME\"}]}}}}";
			
			// Error JSON response
			//String respString="{\"result\":{\"GetCasesForCustomerRes\":{\"ResHdr\":{\"ConsumerContext\":{\"RequesterID\":\"WRK\"},\"ServiceContext\":{\"ServiceName\":\"CaseManagement\",\"ReqRefNum\":\"165555600\",\"ReqRefTimeStamp\":\"2022-08-23T14:01:12\",\"ServiceVersionNo\":\"1.0\"},\"ServiceResponse\":{\"EsbResTimeStamp\":\"2022-08-24 12:30:31.493473\",\"EsbResStatus\":\"1\"},\"ErrorDetails\":[{\"ErrorInfo\":{\"HostErrDesc\":\"Customerid Invalid\"}}]}}}}";
			
			JSONObject jsonResponse = JSONObject.fromObject(respString);
			m_res_header=CommonUtility.esbErrorHandling(jsonResponse,apiResName);
			
			//------------------------------------------JSON response changes starts here---------------------------------------------------
			processAPIResponseForFrontEnd(m_res_header,"CaseDetailsList");
			
			//--------------------API Audit-------Response-------------------------

			JSONObject m_res_If_error = new JSONObject();
			
			if(m_res_header.has("ResCode") && m_res_header.getString("ResCode").equals("0")) {
				m_res_If_error.put("STATUS","Reponse processed Successfully");// to avoid large response storing into DB
			}
			else {
				m_res_If_error = m_res_header;
			}
			generalService.logApiDtl(m_res_If_error.toString(),serviceName,customerInfo,SRN+"~U");
			
		}
		log.info("response from handler "+m_res_header);
		return m_res_header;
	}
	
	public JSONObject caseTrackByCaseId(JSONObject caseDtl,String serviceName)
	{
		log.info(caseDtl+" in request For caseTrack for "+serviceName);
		String customerID = caseDtl.getString("CustomerID").trim();
		String caseId = caseDtl.has("CaseId")? caseDtl.getString("CaseId").trim():"";
		GeneralService generalService = new GeneralService(entityManager);
		String SRN ="";
		
		JSONObject customerInfo = new JSONObject();
		customerInfo.put("CustomerId",customerID);
		customerInfo.put("ipAddress",caseDtl.getString("ipAddress"));
		customerInfo.put("platform",caseDtl.getString("platform"));
		customerInfo.put("header",caseDtl.getString("header"));
		customerInfo.put("mFlag",caseDtl.has("mFlag")?caseDtl.getString("mFlag"):"false");
		customerInfo.put("AuthId",caseDtl.has("AuthId")?caseDtl.getString("AuthId"):"");
		
		baseURL=ServiceCallAPI.apiURLMasterList.get(serviceName.toUpperCase())!=null ? ServiceCallAPI.apiURLMasterList.get(serviceName.toUpperCase()):"";
				
		JSONObject m_res_header = new JSONObject();
		Gson g3=new Gson();
		Gson gson1=new Gson();
		
	/*	{
		  "GetCaseStatusReq": {
		    "ReqHdr": {
		      "ConsumerContext": {
		        "RequesterID": "SME"
		      },
		      "ServiceContext": {
		        "ServiceName": "CaseManagement",
		        "ReqRefNum": "999941634810171",
		        "ReqRefTimeStamp": "2021-10-21T09:56:11.5749656+05:30",
		        "ServiceVersionNo": "1.0"
		      }
		    },
		    "ReqBody": {
		      "CaseID": "CE250621512792"
		    }
		  }
		}*/
		
		//-------------------------------------------------

		LinkedHashMap<String, Object> main_req_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> sub_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> sub_header_body = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> s1_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> s11_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> s12_header = new LinkedHashMap<String, Object>();

		s11_header.put("RequesterID", ServiceCallAPI.serverRequesterID);
		
		s1_header.put("ConsumerContext", gson1.fromJson(g3.toJson(s11_header), s11_header.getClass()));

		s12_header.put("ServiceName", "CaseManagement");
		s12_header.put("ReqRefNum", CommonUtility.getReqRefNum());
		s12_header.put("ReqRefTimeStamp", CommonUtility.getCurrentTimeStamp());
		s12_header.put("ServiceVersionNo", "1.0");

		s1_header.put("ServiceContext", gson1.fromJson(g3.toJson(s12_header), s12_header.getClass()));

		sub_header.put("ReqHdr", gson1.fromJson(g3.toJson(s1_header), s1_header.getClass()));

		sub_header_body.put("CaseID", caseId);
				
		sub_header.put("ReqBody",sub_header_body);

		main_req_header.put("GetCaseStatusReq", gson1.fromJson(g3.toJson(sub_header), sub_header.getClass()));

		String request = g3.toJson(gson1.toJson(main_req_header, main_req_header.getClass()));
		request = CommonUtility.convertBasicString(request);
		
		String reqfrmAPI = "",apiResName = "GetCaseStatusRes";
		JSONObject reqJsonfrmAPI = null;
		String requestData= baseURL+"|"+request.toString()+"|"+Authorization;

		//----------TAT_CR--------------------------------------
		
		JSONObject tatResult = new JSONObject();
		tatResult=getTurnaroundTimeByApiCaseId(caseDtl.getString("CaseId"));
		String turnAroundTime=tatResult.getString("turnaroundTime");
		//----------End--------------------------------------

		if(SERVICE_TYPE.equals("true"))
		{
			//--------------------API Audit--------Request------------------------
			try {
				SRN = generalService.logApiDtl(requestData,serviceName,customerInfo,"I");
			} catch (Exception e1) {
				log.info("request="+e1);
			}
			
			try 
			{	
				
				//String respString1 = "{\"result\":{\"GetCaseStatusRes\":{\"ResHdr\":{\"ConsumerContext\":{\"RequesterID\":\"WRK\"},\"ServiceContext\":{\"ServiceName\":\"CaseManagement\",\"ReqRefNum\":\"999941634810171\",\"ReqRefTimeStamp\":\"2021-10-21T09:56:11.5749656+05:30\",\"ServiceVersionNo\":\"1.0\"},\"ServiceResponse\":{\"EsbResTimeStamp\":\"2022-01-07 16:49:47.597977\",\"EsbResStatus\":\"0\"}},\"ResBody\":{\"CaseStatus\":{\"ID\":\"CE050521001182\",\"CreatedDate\":\"2021-06-25T06:09:11Z\",\"ModifiedDate\":\"2021-06-25T06:09:19Z\",\"StatusText\":\"Open\"}}}}}";

				reqfrmAPI = Utils.DataFromService(baseURL, request,Authorization);
				reqJsonfrmAPI = JSONObject.fromObject(reqfrmAPI);
				m_res_header=CommonUtility.esbErrorHandling(reqJsonfrmAPI,apiResName);

				// -----------------TAT Cr new response -------
				m_res_header.getJSONObject("ResBody").getJSONObject("CaseStatus").put("turnAroundTime",turnAroundTime + " Days");

				// --------------------------------------------

				// ------------------------------------------JSON response changes starts here---------------------------------------------------
				processAPIResponseForFrontEnd(m_res_header, "CaseStatus");

				// --------------------API Audit-------Response-------------------------

				generalService.logApiDtl(m_res_header.toString(), serviceName, customerInfo, SRN + "~U");
				
			} catch (JSONException e) 
			{
				log.error("JSONException---",e);			
			} catch (Exception e) {
				log.error("Exception---",e);
			}
		}
		else
		{	
			String newCaseId = caseDtl.getString("CaseId");
			log.info("tatResult result="+tatResult);
			//--------------------API Audit--------Request------------------------
			try {
				SRN = generalService.logApiDtl(requestData,serviceName,customerInfo,"I");
				
			} catch (Exception e1) {
				log.info("request="+e1);
			}
			
			// SUCCESS JSON response
			String respString = "{\"result\":{\"GetCaseStatusRes\":{\"ResHdr\":{\"ConsumerContext\":{\"RequesterID\":\"WRK\"},\"ServiceContext\":{\"ServiceName\":\"CaseManagement\",\"ReqRefNum\":\"999941634810171\",\"ReqRefTimeStamp\":\"2021-10-21T09:56:11.5749656+05:30\",\"ServiceVersionNo\":\"1.0\"},\"ServiceResponse\":{\"EsbResTimeStamp\":\"2022-01-07 16:49:47.597977\",\"EsbResStatus\":\"0\"}},\"ResBody\":{\"CaseStatus\":{\"ID\":\""+newCaseId+"\",\"turnAroundTime\":\""+ turnAroundTime  +" Days"+ "\",\"CreatedDate\":\"2021-06-25T06:09:11Z\",\"ModifiedDate\":\"2021-06-25T06:09:19Z\",\"StatusText\":\"Open\"}}}}}";
			
			
			// Error JSON response
			//String respString="{\"result\":{\"GetCaseStatusRes\":{\"ResHdr\":{\"ConsumerContext\":{\"RequesterID\":\"WRK\"},\"ServiceContext\":{\"ServiceName\":\"CaseManagement\",\"ReqRefNum\":\"165555600\",\"ReqRefTimeStamp\":\"2022-08-23T14:01:12\",\"ServiceVersionNo\":\"1.0\"},\"ServiceResponse\":{\"EsbResTimeStamp\":\"2022-08-24 12:30:31.493473\",\"EsbResStatus\":\"1\"},\"ErrorDetails\":[{\"ErrorInfo\":{\"HostErrDesc\":\"Case Id Invalid\"}}]}}}}";
			//String respString =  "{\"result\":{\"GetCaseStatusRes\":{\"ResHdr\":{\"ConsumerContext\":{\"RequesterID\":\"SME\"},\"ServiceContext\":{\"ServiceName\":\"CaseManagement\",\"ReqRefNum\":\"6680774635878\",\"ReqRefTimeStamp\":\"2022-11-10T16:21:03\",\"ServiceVersionNo\":\"1.0\"},\"ServiceResponse\":{\"EsbResTimeStamp\":\"2022-11-10 16:21:04.475008\",\"EsbResStatus\":\"1\"},\"ErrorDetails\":[{\"ErrorInfo\":{\"HostErrCode\":\"No Record Found\"}}]}}}}";
			
			JSONObject jsonResponse = JSONObject.fromObject(respString);
			m_res_header=CommonUtility.esbErrorHandling(jsonResponse,apiResName);
			
			//------------------------------------------JSON response changes starts here---------------------------------------------------
			processAPIResponseForFrontEnd(m_res_header,"CaseStatus");
			
			//--------------------API Audit-------Response-------------------------
			
			generalService.logApiDtl(m_res_header.toString(),serviceName,customerInfo,SRN+"~U");
			
		}
		log.info("response from handler "+m_res_header);
		return m_res_header;
	}
	

	private void processAPIResponseForFrontEnd(JSONObject m_res_header,String bodyArrName) {
		
		log.info("response from API "+m_res_header);
		Connection conn=null;
		
		try {
			conn = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
			org.json.JSONArray modifiedJsonArray = new org.json.JSONArray();
			if(m_res_header.has("ResCode") && m_res_header.getString("ResCode").equals("0")) {

				String arrayString="";
				if("CaseDetailsList".equals(bodyArrName)) {
					arrayString=m_res_header.getJSONObject("ResBody").getString(bodyArrName);// track case by date period
				}
				else {
					arrayString="["+m_res_header.getJSONObject("ResBody").getString(bodyArrName)+"]";//track case by case id----> convert Single json object to Json Array for commom response to display on front end
				}
				
				org.json.JSONArray jsonArray = new org.json.JSONArray(arrayString);
				for(int i=0;i<jsonArray.length();i++)
				{
					org.json.JSONObject rec = jsonArray.getJSONObject(i);
				
					try {
						pstmt = conn.prepareStatement(getQuery("SELECT_SME_CRM_CASE_DTL"));
						pstmt.setString(1, rec.getString("ID"));
						rset = pstmt.executeQuery();
						
						if(rset.next())// if no caseid found in DB
						{
						 rec.put("IssueTypeName",rset.getString(2));
						 rec.put("category",  rset.getString(3));
						 rec.put("CaseType",rset.getString(4));
						 rec.put("ProductTypeCode", "YES MSME");
						 rec.put("UserRemarks",  rset.getString(5));
						 rec.put("turnAroundTime",  rset.getString(6)+" Days");//TAT_CR
						 
						}
						else {// if no caseid exist in DB
							 rec.put("IssueTypeName","");
							 rec.put("category",  "");
							 rec.put("CaseType","");
							 rec.put("ProductTypeCode", "YES MSME");
							 rec.put("UserRemarks",  "Remark Not Found");
						}
						
					} catch (org.json.JSONException e) {
						log.info("JSONException from handler ="+e);
					} catch (SQLException e) {
						log.info("SQLException from handler ="+e);
					}

					modifiedJsonArray.put(rec);
				}
				
				//org.json.JSONArray sortedJsonArray = sortJsonArray(modifiedJsonArray); // pass sorted json to CaseDetailsList for sortedlist form backend to front end
				
				m_res_header.getJSONObject("ResBody").put("CaseDetailsList", modifiedJsonArray.toString());
			}
			else {
				m_res_header.put("ErrorDetails","No Record Found");
			}
		} catch (org.json.JSONException e) {
			log.info(e);
		}
		
		finally {
			try {

				if (rset != null) {
					rset.close();		 
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if(conn!=null){
					closeConnection(conn,new Object(){}.getClass().getEnclosingMethod().getName().toString());
				}
			}
			catch(Exception e) {
				log.info(e);
			}
		}
		
	}

	private org.json.JSONArray sortJsonArray(org.json.JSONArray modifiedJsonArray) {
		org.json.JSONArray sortedJsonArray = new org.json.JSONArray();

		List<org.json.JSONObject> jsonValues = new ArrayList<org.json.JSONObject>();
		for (int i = 0; i < modifiedJsonArray.length(); i++) {
		    jsonValues.add(modifiedJsonArray.getJSONObject(i));
		}
		log.info(" jsonValues ="+ jsonValues);
		Collections.sort( jsonValues, new Comparator<org.json.JSONObject>() {
		    //You can change "Name" with "ID" if you want to sort by ID
		    private static final String KEY_NAME = "CreatedDate";

		    @Override
		    public int compare(org.json.JSONObject a, org.json.JSONObject b) {
		        String valA = new String();
		        String valB = new String();

		        try {
		            valA = (String) a.get(KEY_NAME);
		            valB = (String) b.get(KEY_NAME);
		        } 
		        catch (JSONException e) {
		            //do something
		        }

		        return -valA.compareTo(valB);
		        //if you want to change the sort order, simply use the following:
		        //return -valA.compareTo(valB);
		    }
		});

		for (int i = 0; i < modifiedJsonArray.length(); i++) {
		    sortedJsonArray.put(jsonValues.get(i));
		}
		
		log.info(" sortedJsonArray ="+ sortedJsonArray);
		return sortedJsonArray;
	}
	
	public Integer maintainCRM_Details(JSONObject caseInfo, String flag)
	{
		PreparedStatement pstmt= null;
		ResultSet rset = null;
		Integer idCrmDtl=0;
		Connection conn = null;
		try{
			log.info(flag+"Inside maintainCRM_Details ----- "+caseInfo);
			conn = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
			if("I".equals(flag)){

				boolean mFlag=false;
				if (caseInfo.containsKey("mFlag") && caseInfo.getString("mFlag").equals("true")) {
					mFlag = true;
				}
				
				conn = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
				
				String companyID="";
				if(mFlag)	
					companyID=	ApplicationGenParameter.companyCustomerMapList.get(caseInfo.getString("AuthId").trim());
				else
					companyID=	ApplicationGenParameter.companyCustomerMapList.get(caseInfo.getString("CustomerId").trim());
				
				pstmt =conn.prepareStatement(getQuery("SELECT_SEQ_FOR_CRM_CASE_DTL"));
				rset = pstmt.executeQuery();
				if (rset.next()) {
					idCrmDtl = rset.getInt(1);
				}
				rset.close();
				pstmt.close();

				//ID,CUSTOMER_ID,ISSUE_ID,REMARKS,CREATE_DATETIME,PLATFORM,STATUS) VALUES(?,?,?,?,SYSDATE,?);
				pstmt = conn.prepareStatement(getQuery("INSERT_SME_CRM_CASE_DTL"));
				pstmt.setString(1,idCrmDtl.toString());
				pstmt.setString(2,caseInfo.getString("CustomerId"));
				pstmt.setString(3,companyID);
				pstmt.setString(4,caseInfo.getString("ISSUE_ID"));
				pstmt.setString(5,caseInfo.getString("REMARKS"));
				pstmt.setString(6,caseInfo.getString("platform"));
				int i = pstmt.executeUpdate();
				pstmt.close();
				log.info("maintainCRM_Details inserted....."+i );
			}else{
				 // UPDATE SME_CRM_CASE_DTL SET API_CASE_ID=?,STATUS=?,REASON=?,UPDATE_DATETIME=SYSDATE WHERE ID = ?
				pstmt = conn.prepareStatement(getQuery("UPDATE_CASEID_SME_CRM_CASE_DTL"));
				pstmt.setString(1,caseInfo.getString("API_CASE_ID"));
				pstmt.setString(2,caseInfo.getString("STATUS"));
				pstmt.setString(3,caseInfo.getString("REASON"));
				pstmt.setString(4,caseInfo.getString("ID"));
				int i = pstmt.executeUpdate();
				pstmt.close();

				log.info("maintainCRM_Details updated....."+i );
			}
		}
		catch(Exception e) {
			idCrmDtl=0;
			log.info(e);
		}
		finally {
			try {

				if (rset != null) {
					rset.close();		 
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if(conn!=null){
					//close with closeConnection method 2299 - 22032021
					closeConnection(conn,new Object(){}.getClass().getEnclosingMethod().getName().toString());
				}
			}
			catch(Exception e) {
				log.info(e);
			}
		}
		return idCrmDtl;
	}
	
	public JSONObject getCustomerInfoFromCustId(String customerId,boolean mFlag)
	{
		log.info(mFlag+"||||||||||||||getCustomerInfoFromCustId||||||||"+customerId);
		JSONObject custDtlJson = new JSONObject();
		
		Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rset=null;
		try{
			conn = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
			
			custDtlJson.put("custId", customerId);
			
			if(mFlag) {
				pstmt = conn.prepareStatement(getQuery("SELECT_MAKER_DTL"));
				pstmt.setString(1, customerId.trim().toUpperCase());
			}
			else {
				pstmt = conn.prepareStatement(getQuery("GET_CUSTOMERNAME"));
				pstmt.setString(1, customerId);
			}
			rset = pstmt.executeQuery();
			
			if(rset.next())
			{
				custDtlJson.put("custName",rset.getString(1));
			}
			else
			{
				custDtlJson.put("custName", "Not Found");
			}
			
			rset.close();
			pstmt.close();
		}
		catch(SQLException se)
		{
			log.info("SQLException :::",se);
		}
		catch(Exception e)
		{
			log.info("Exception :::",e);
		}
		finally
		{
			try
			{
				if(rset != null)
				{
					rset.close();
				}
				if(pstmt != null)
				{
					pstmt.close();
				}
				if(conn!=null){
					  closeConnection(conn,new Object(){}.getClass().getEnclosingMethod().getName().toString());
				}
			}
			catch(Exception se)
			{
				log.info("EXception :::", se);
			}
		}
		log.info("||||||||||||||custDtlJson response||||||||"+custDtlJson);
		
		return custDtlJson;
	}
	
	
//------------------------------------TAT METHOD START ------------------------------------------
	
// ------------------------------------1.TAT BY SELECT_TURNAROUND_TIME-----------------------

	public JSONObject getTeamAndTurnAroundTime(String issueID) {
		log.info(issueID);
		JSONObject custDtlJson = new JSONObject();

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			conn = getConnection(new Object() {
			}.getClass().getEnclosingMethod().getName().toString());

			pstmt = conn.prepareStatement(getQuery("SELECT_TURNAROUND_TIME"));
			pstmt.setString(1, issueID);

			rset = pstmt.executeQuery();
			if (rset.next()) {
				custDtlJson.put("assignTeam", rset.getString(1));
				custDtlJson.put("turnaroundTime", rset.getString(2));
			}
			rset.close();
			pstmt.close();
		} catch (SQLException se) {
			log.info("SQLException :::", se);
		} catch (Exception e) {
			log.info("Exception :::", e);
		} finally {
			try {
				if (rset != null) {
					rset.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					closeConnection(conn, new Object() {
					}.getClass().getEnclosingMethod().getName().toString());
				}
			} catch (Exception se) {
				log.info("EXception :::", se);
			}
		}

		return custDtlJson;
	}

// ------------------------------------2.TAT BY SELECT_TURNAROUND_TIME_BY_API_CASEID-----------------------

	public JSONObject getTurnaroundTimeByApiCaseId(String apiCaseId) {
		JSONObject result = new JSONObject();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection(new Object() {
			}.getClass().getEnclosingMethod().getName().toString());

			pstmt = conn.prepareStatement(getQuery("SELECT_TURNAROUND_TIME_BY_API_CASEID"));
			pstmt.setString(1, apiCaseId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result.put("turnaroundTime", rs.getString("TURNAROUND_TIME"));
			}
		} catch (SQLException se) {
			log.info("SQLException: ", se);
		} catch (Exception e) {
			log.info("Exception: ", e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				log.info("Exception while closing resources: ", e);
			}
		}

		return result;
	}

//------------------------------------3.TAT BY SELECT_ISSUE_DETAILS_FOR_CUSTOMER-----------------------

	public JSONObject getIssueDetailsForCustomer(String customerId) {
		JSONObject ResBody = new JSONObject();

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection(new Object() {
			}.getClass().getEnclosingMethod().getName().toString());

			pstmt = conn.prepareStatement(getQuery("SELECT_ISSUE_DETAILS_FOR_CUSTOMER"));

			pstmt.setString(1, customerId);
			rs = pstmt.executeQuery();
			JSONArray issueArray = new JSONArray();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");

			while (rs.next()) {
				JSONObject issueDetails = new JSONObject();
				issueDetails.put("issueId", rs.getInt("ISSUE_ID"));
				issueDetails.put("IssueTypeName", rs.getString("ISSUE_NAME"));
				issueDetails.put("category", rs.getString("CATEGORY_TYPE"));
				issueDetails.put("caseName", rs.getString("CASE_NAME"));
				issueDetails.put("UserRemarks", rs.getString("REMARKS"));
				issueDetails.put("assignTeam", rs.getString("ASSIGN_TEAM"));
				issueDetails.put("turnAroundTime", rs.getString("TURNAROUND_TIME") + " Days");
				issueDetails.put("ID", rs.getString("API_CASE_ID"));
				issueDetails.put("ProductTypeCode", "YES MSME");
				issueDetails.put("StatusText", rs.getString("STATUS"));
				issueDetails.put("CreatedDate", sdf.format(rs.getTimestamp("CREATE_DATETIME")));

				issueArray.add(issueDetails);
			}

			ResBody.put("CaseDetailsList", issueArray);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return ResBody;
	}
	//------------------------------------3.TAT CASE_ID_GENERATE for else part testing purpose----------------------

	
	public String caseIdGenerate() {
		String turnAroundTime = "7";
		Random random = new Random();
		int randomNumber = random.nextInt(10000000); // Generates a random number between 0 and 9999999

		// Pad the random number with zeros to ensure fixed length
		String paddedRandomNumber = String.format("%07d", randomNumber); // Ensuring it's 7 digits long

		// Generate a unique identifier combining timestamp and random number
		String caseID = "CE" + System.currentTimeMillis() + paddedRandomNumber;

		// Trim the timestamp if it exceeds 14 characters
		if (caseID.length() > 14) {
			caseID = caseID.substring(0, 14);
		} else if (caseID.length() < 14) {
			int diff = 14 - caseID.length();
			for (int i = 0; i < diff; i++) {
				caseID += "0";
			}
		}
		return caseID;
	}
	
//------------------------------------TAT METHOD End ------------------------------------------------------
	
	//Added by swapnil for RNB Onboarding 			
	public JSONObject  UpdateRNBRegDetailsReq(JSONObject registerData,String serviceName)
	{
		log.info("Inside UpdateRNBRegDetailsReq ==="+registerData);	

		baseURL=ServiceCallAPI.apiURLMasterList.get(serviceName.toUpperCase())!=null ? ServiceCallAPI.apiURLMasterList.get(serviceName.toUpperCase()):"";
		registerData.put("ipAddress",ApplicationGenParameter.custIpAddMapList.get(registerData.getString("customerID")));
		
		GeneralService generalService = new GeneralService(entityManager);
		ObjectMapper objectMapper = new ObjectMapper();
		Gson g3 = new Gson();
		Gson gson1 = new Gson();

		LinkedHashMap<String, Object> main_req_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> sub_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> sub_header1 = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> s1_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> s11_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> s12_header = new LinkedHashMap<String, Object>();

		/*	{
			  "UpdateRNBRegDetailsReq": {
			    "ReqHdr": {
			      "ConsumerContext": {
			        "RequesterID": "WRK"
			      },
			      "ServiceContext": {
			        "ServiceName": "CustomerManagement",
			        "ReqRefNum": "24493631626548394602",
			        "ReqRefTimeStamp": "2022-02-17T14:39:53",
			        "ServiceVersionNo": "1.0"
			      }
			    },
			    "ReqBody": {
			      "CustomerId": "330",
			      "Password": null,
			      "ChannelId": "MB"
			    }
			  }
			}*/

		s11_header.put("RequesterID", ServiceCallAPI.serverRequesterID);
		s1_header.put("ConsumerContext", gson1.fromJson(g3.toJson(s11_header), s11_header.getClass()));

		s12_header.put("ServiceName", "CustomerManagement");
		s12_header.put("ReqRefNum", CommonUtility.getReqRefNum());
		s12_header.put("ReqRefTimeStamp", CommonUtility.getCurrentTimeStamp());
		s12_header.put("ServiceVersionNo", "1.0");

		s1_header.put("ServiceContext", gson1.fromJson(g3.toJson(s12_header), s12_header.getClass()));

		sub_header.put("ReqHdr", gson1.fromJson(g3.toJson(s1_header), s1_header.getClass()));

		sub_header1.put("CustomerId", registerData.getString("customerID"));

		String ss= (registerData.has("custPass") && !registerData.getString("custPass").isEmpty()) ?registerData.getString("custPass"):null;
		
		sub_header1.put("Password", ss);
		sub_header1.put("ChannelId", "MB");

		String request = null ;
		try {
			//sub_header.put("ReqBody", gson1.fromJson(g3.toJson(sub_header1), sub_header1.getClass()));
			sub_header.put("ReqBody", objectMapper.writeValueAsString(sub_header1));
			main_req_header.put("UpdateRNBRegDetailsReq", gson1.fromJson(g3.toJson(sub_header), sub_header.getClass()));
			//main_req_header.put("UpdateRNBRegDetailsReq", objectMapper.writeValueAsString(sub_header) );
			request = g3.toJson(gson1.toJson(main_req_header, main_req_header.getClass()));
			//request = objectMapper.writeValueAsString(main_req_header);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		request = CommonUtility.convertBasicString(request);
		request = request.replaceAll(Pattern.quote("}\""), "}");// due to objectMapper extra " will be removed
		request = request.replaceAll(Pattern.quote("\"{"),"{");// due to objectMapper extra " will be removed
		
		String reqfrmAPI = "";
		String apiResName = "";
		JSONObject reqJsonfrmAPI = null;
		JSONObject m_res_header = new JSONObject();
		JSONObject customerInfo = new JSONObject();

		customerInfo.put("CustomerId", registerData.has("customerID")?registerData.getString("customerID"):"");
		customerInfo.put("ipAddress", registerData.has("ipAddress")?registerData.getString("ipAddress"):"");

		if (registerData.has("loginMode")) {
			customerInfo.put("platform", registerData.getString("loginMode"));
		} else if (registerData.has("platform")) {
			customerInfo.put("platform", registerData.getString("platform"));
		} else {
			customerInfo.put("platform", "");
		}

		customerInfo.put("header", registerData.getString("header"));
		log.info("request = "+request);
		String requestData = baseURL + "|" + request.toString() + "|" + Authorization;
		if (SERVICE_TYPE.equals("true")) 
		{
			try {
				//maintain audit for API call---request
				String SRN = generalService.logApiDtl(requestData, serviceName, customerInfo, "I");

				reqfrmAPI = Utils.DataFromService(baseURL, request, Authorization);
				reqJsonfrmAPI = JSONObject.fromObject(reqfrmAPI);
				apiResName = "UpdateRNBRegDetailsRes";
				m_res_header = CommonUtility.esbErrorHandling(reqJsonfrmAPI, apiResName);
				
				//maintain audit for API call---response
				generalService.logApiDtl(m_res_header.toString(), serviceName, customerInfo, SRN + "~U");
				log.info("UpdateRNBRegDetailsRes response---");

			} catch (JSONException e) {
				log.info("JSONException---", e);
			} catch (Exception e) {
				log.info("Exception---", e);
			}
		}else {
			try {
				String SRN = generalService.logApiDtl(requestData, serviceName, customerInfo, "I");

				//For Success
			//	reqfrmAPI="{\"result\":{\"UpdateRNBRegDetailsRes\":{\"ResHdr\":{\"ConsumerContext\":{\"RequesterID\":\"WRK\"},\"ServiceContext\":{\"ServiceName\":\"CustomerManagement\",\"ReqRefNum\":\"24493631626548394602\",\"ReqRefTimeStamp\":\"2022-02-17T14:39:53\",\"ServiceVersionNo\":\"1.0\"},\"ServiceResponse\":{\"EsbResTimeStamp\":\"2022-06-15 17:36:54.632357\",\"EsbResStatus\":\"0\"}},\"ResBody\":{\"CustomerId\":\"330\"}}}}";

				//reqfrmAPI="{\"result\":{\"UpdateRNBRegDetailsRes\":{\"ResHdr\":{\"ConsumerContext\":{\"RequesterID\":\"WRK\"},\"ServiceContext\":{\"ServiceName\":\"CustomerManagement\",\"ReqRefNum\":\"24493631626548394602\",\"ReqRefTimeStamp\":\"2022-02-17T14:39:53\",\"ServiceVersionNo\":\"1.0\"},\"ServiceResponse\":{\"EsbResTimeStamp\":\"2022-06-15 17:36:54.632357\",\"EsbResStatus\":\"0\"}}}}}";
				//For Fail
				//reqfrmAPI="{\"result\":{\"UpdateRNBRegDetailsRes\":{\"ResHdr\":{\"ConsumerContext\":{\"RequesterID\":\"WRK\"},\"ServiceContext\":{\"ServiceName\":\"CustomerManagement\",\"ReqRefNum\":\"24493631626548394602\",\"ReqRefTimeStamp\":\"2022-02-17T14:39:53\",\"ServiceVersionNo\":\"1.0\"},\"ServiceResponse\":{\"EsbResTimeStamp\":\"2022-06-15 17:36:54.632357\",\"EsbResStatus\":\"0\"}}}}}";
				//For Fail
				reqfrmAPI="{\"result\":{\"UpdateRNBRegDetailsRes\":{\"ResHdr\":{\"ConsumerContext\":{\"RequesterID\":\"WRK\"},\"ServiceContext\":{\"ServiceName\":\"CustomerManagement\",\"ReqRefNum\":\"24493631626548394602\",\"ReqRefTimeStamp\":\"2022-02-17T14:39:53\",\"ServiceVersionNo\":\"1.0\"},\"ServiceResponse\":{\"EsbResTimeStamp\":\"2022-06-15 17:36:54.632357\",\"EsbResStatus\":\"1\"}}}}}";
				reqJsonfrmAPI = JSONObject.fromObject(reqfrmAPI);
				apiResName = "UpdateRNBRegDetailsRes";
				m_res_header = CommonUtility.esbErrorHandling(reqJsonfrmAPI, apiResName);

				generalService.logApiDtl(m_res_header.toString(), serviceName, customerInfo, SRN + "~U");

			} catch (JSONException e) {
				log.info("JSONException---", e);
			} catch (Exception e) {
				log.info("Exception---", e);
			}
		}
		return m_res_header;

	}
	
	 private String getBaseURLForService(String serviceName) {
	        for (String key : ServiceCallAPI.apiURLMasterList.keySet()) {
	            if (serviceName.toUpperCase().equals(key.trim().toUpperCase())) {
	                return ServiceCallAPI.apiURLMasterList.get(key);
	            }
	        }
	        return "";
	    }
	 
	 
	 public JSONObject registerWithRNB(UserLogin accInfo, String serviceName) {

		    JSONObject m_res_header = new JSONObject();// ResBody

		    EntityManager localEntityManager = entityManagerFactory.createEntityManager().unwrap(Session.class);

		    try {

		        for (String key : ServiceCallAPI.apiURLMasterList.keySet()) {
		            if (serviceName.toUpperCase().equals(key.trim().toUpperCase())) {
		                baseURL = ServiceCallAPI.apiURLMasterList.get(key);
		                break;
		            }
		        }

		        currentts = ApplicationGenParameter.sdf.format(ApplicationGenParameter.date);
		        currentts = currentts.replace(" ", "T");
		        // log.info("Current Time Stamp ==="+currentts);
		        String aliasId = "";
		        // String Authorization = "Basic dGVzdGNsaWVudDp0ZXN0QDEyMw==";
		        // As discussed with Vishwas, issue raised on bugzilla 5100 (Prabhat on 14 DEC
		        // 2018)

		        if (!"LOCAL".equals(path.getPath("serverType")))// For UAT API Call
		        {
		            if (accInfo.getRetailBankUserID().trim().matches("[0-9]+")) {

		                String custId = accInfo.getRetailBankUserID().trim();
		                String sqlQuery = "SELECT codCustId FROM MvCiCustmastAll WHERE codCustId = :custId";

		                List<String> results = entityManager.createNativeQuery(sqlQuery).setParameter("custId", custId) .getResultList();

		                if (!results.isEmpty()) {
		                    String codCustId = results.get(0);
		                    accInfo.setRetailBankUserID(codCustId.trim());
		                }
		            }

		        }
		        Gson g3 = new Gson();
		        Gson gson1 = new Gson();

		        LinkedHashMap<String, Object> main_req_header = new LinkedHashMap<String, Object>();
		        LinkedHashMap<String, Object> sub_header = new LinkedHashMap<String, Object>();
		        LinkedHashMap<String, Object> s1_header = new LinkedHashMap<String, Object>();
		        LinkedHashMap<String, Object> s11_header = new LinkedHashMap<String, Object>();
		        LinkedHashMap<String, Object> s12_header = new LinkedHashMap<String, Object>();

		        s11_header.put("RequesterID", ServiceCallAPI.serverRequesterID);
		        s1_header.put("ConsumerContext", gson1.fromJson(g3.toJson(s11_header), s11_header.getClass()));

		        s12_header.put("ServiceName", "RegistrationService");
		        s12_header.put("ReqRefNum", CommonUtility.getReqRefNum());
		        s12_header.put("ReqRefTimeStamp", CommonUtility.getCurrentTimeStamp());
		        s12_header.put("ServiceVersionNo", "1.0");

		        s1_header.put("ServiceContext", gson1.fromJson(g3.toJson(s12_header), s12_header.getClass()));
		        sub_header.put("ReqHdr", gson1.fromJson(g3.toJson(s1_header), s1_header.getClass()));

		        sub_header.put("UserId", accInfo.getRetailBankUserID());
		        sub_header.put("Password", accInfo.getPassword());
		        aliasId = accInfo.getRetailBankUserID();

		        main_req_header.put("ValidateRNBLoginReq", gson1.fromJson(g3.toJson(sub_header), sub_header.getClass()));

		        String request = g3.toJson(gson1.toJson(main_req_header, main_req_header.getClass()));
		        request = CommonUtility.convertBasicString(request);
		        String reqfrmAPI = "", apiResName = "";
		        JSONObject reqJsonfrmAPI = null;

		        JSONObject modifyJson = new JSONObject();

		        if (SERVICE_TYPE.equals("true")) {
		            // log.info("If Service Type TRUE");

		            // Go With API
		            // log.info(" --> Calling ");

		            // baseURL = "https://10.0.45.87:1443/RegistrationService/ValidateRNBLogin";
		            log.info("baseURL ===" + baseURL);
		            try {
		                reqfrmAPI = Utils.DataFromService(baseURL, request, Authorization);

		                // log.info("ValidateRNBLoginRes String Response<<<<<"+reqfrmAPI);
		                reqJsonfrmAPI = JSONObject.fromObject(reqfrmAPI);
		                // log.info("ValidateRNBLoginRes json<<<<<<<<<<"+reqJsonfrmAPI);
		                if (reqJsonfrmAPI.containsKey("ErrorDetails")) {

		                    String hqlUpdateApiDetail = "UPDATE SmeUserDropOffDtl SET requestDt=SYSDATE,errorLog = :errorLog WHERE customerId = :customerid "
		                            + "AND regModeUsed=:reg_mode_used AND companyId=:company_id AND transactionChannel=:transaction_channel";
		                    
		                    Query updateQuery = localEntityManager.createQuery(hqlUpdateApiDetail);
		                    updateQuery.setParameter("errorLog", reqJsonfrmAPI.toString());
		                    updateQuery.setParameter("customerid", accInfo.getRetailBankUserID().trim());
		                    updateQuery.setParameter("reg_mode_used", accInfo.getUserRegisterType());
		                    updateQuery.setParameter("company_id", accInfo.getCompanyID());
		                    updateQuery.setParameter("transaction_channel", accInfo.getUserType());

		                    localEntityManager.getTransaction().begin();
		                    updateQuery.executeUpdate();
		                    localEntityManager.getTransaction().commit();

		                    // connection.close(); //comment no need new con create and close

		                    return reqJsonfrmAPI;
		                }
		                log.info("ValidateRNBLoginRes json<<<<<<<<<<" + reqJsonfrmAPI);
		                apiResName = "ValidateRNBLoginRes";
		                m_res_header = CommonUtility.esbErrorHandling(reqJsonfrmAPI, apiResName);

		                log.info("ValidateRNBLoginRes json Res Body<<<<<<<<<<" + m_res_header);
		                if (m_res_header.containsKey("ResBody") && m_res_header.getString("ResBody").equals("SUCCESS")) {

		                    if (!accInfo.getRetailBankUserID().trim().matches("[0-9]+")) {
		                        accInfo.setCompanyIDForAlisId(m_res_header.getString("CustomerId"));
		                        accInfo.setRetailBankUserID(m_res_header.getString("CustomerId"));
		                        // setCustomerAliasBaneMappingIntoTable(accInfo.getRetailBankUserID(), aliasId);
		                    }
		                    modifyJson.put("Status", "SUCCESS");
		                    // modifyJson.put("CustomerId", m_res_header.getString("CustomerId"));
		                    m_res_header.put("ResBody", modifyJson);
		                } else if (m_res_header.containsKey("ResCode") && m_res_header.getString("ResCode").equals("1")) {
		                    modifyJson.put("Status", "Failure");
		                    modifyJson.put("ErrorDetails", m_res_header.getString("ErrorDetails"));
		                    m_res_header.put("ResBody", modifyJson);

		                    String hqlUpdateApiDetail = "UPDATE SmeUserDropOffDtl SET requestDt=SYSDATE,errorLog = :errorLog WHERE customerId = :customerid "
		                            + "AND regModeUsed=:reg_mode_used AND companyId=:company_id AND transactionChannel=:transaction_channel";
		                    
		                    Query updateQuery = localEntityManager.createQuery(hqlUpdateApiDetail);
		                    updateQuery.setParameter("errorLog", reqJsonfrmAPI.toString());
		                    updateQuery.setParameter("customerid", accInfo.getRetailBankUserID().trim());
		                    updateQuery.setParameter("reg_mode_used", accInfo.getUserRegisterType());
		                    updateQuery.setParameter("company_id", accInfo.getCompanyID());
		                    updateQuery.setParameter("transaction_channel", accInfo.getUserType());

		                    localEntityManager.getTransaction().begin();
		                    updateQuery.executeUpdate();
		                    localEntityManager.getTransaction().commit();

		                } else {
		                    modifyJson.put("Status", "Failure");
		                    m_res_header.put("ResBody", modifyJson);

		                    String hqlUpdateApiDetail = "UPDATE SmeUserDropOffDtl SET requestDt=SYSDATE,errorLog = :errorLog WHERE customerId = :customerid "
		                            + "AND regModeUsed=:reg_mode_used AND companyId=:company_id AND transactionChannel=:transaction_channel";
		                    
		                    Query updateQuery = localEntityManager.createQuery(hqlUpdateApiDetail);
		                    updateQuery.setParameter("errorLog", reqJsonfrmAPI.toString());
		                    updateQuery.setParameter("customerid", accInfo.getRetailBankUserID().trim());
		                    updateQuery.setParameter("reg_mode_used", accInfo.getUserRegisterType());
		                    updateQuery.setParameter("company_id", accInfo.getCompanyID());
		                    updateQuery.setParameter("transaction_channel", accInfo.getUserType());

		                    localEntityManager.getTransaction().begin();
		                    updateQuery.executeUpdate();
		                    localEntityManager.getTransaction().commit();

		                }
		            } catch (JSONException e) {
		                log.info("JSONException---", e);
		            } catch (Exception e) {
		                log.info("Exception---", e);
		            }
		        } else {

		            m_res_header = loginDaoimpl.checkValidRegistration(accInfo, "RNB");
		            if (m_res_header.containsKey("ResBody")
		                    && m_res_header.getJSONObject("ResBody").getString("Status").equals("SUCCESS")) {

		                if (!accInfo.getRetailBankUserID().trim().matches("[0-9]+")) {
		                    accInfo.setCompanyIDForAlisId(
		                            m_res_header.getJSONObject("ResBody").getString("RnbCustomerID").trim());
		                    accInfo.setRetailBankUserID(
		                            m_res_header.getJSONObject("ResBody").getString("RnbCustomerID").trim());
		                    // setCustomerAliasBaneMappingIntoTable(accInfo.getRetailBankUserID(),aliasId);

		                }

		                modifyJson.put("Status", "SUCCESS");
		                // modifyJson.put("CustomerId", m_res_header.getString("CustomerId"));
		                m_res_header.put("ResBody", modifyJson);

		            } else {

		                modifyJson.put("Status", "Failure");
		                m_res_header.put("ResBody", modifyJson);

		                try {
		                    String hqlUpdateApiDetail = "UPDATE SmeUserDropOffDtl SET requestDt=SYSDATE,errorLog = :errorLog WHERE customerId = :customerid "
		                            + "AND regModeUsed=:reg_mode_used AND companyId=:company_id AND transactionChannel=:transaction_channel";
		                    
		                    Query updateQuery = localEntityManager.createQuery(hqlUpdateApiDetail);
		                    updateQuery.setParameter("errorLog", reqJsonfrmAPI.toString());
		                    updateQuery.setParameter("customerid", accInfo.getRetailBankUserID().trim());
		                    updateQuery.setParameter("reg_mode_used", accInfo.getUserRegisterType());
		                    updateQuery.setParameter("company_id", accInfo.getCompanyID());
		                    updateQuery.setParameter("transaction_channel", accInfo.getUserType());

		                    localEntityManager.getTransaction().begin();
		                    updateQuery.executeUpdate();
		                    localEntityManager.getTransaction().commit();

		                } catch (Exception e) {
		                    log.info(e);
		                }

		            }

		            log.info("Response Header====" + m_res_header);

		        }
		    } catch (Exception e) {
		        log.info("Exception ===", e);
		    } finally {

		    }

		    return m_res_header;
		}




public JSONObject requestForVerifyOTP(JSONObject otpInfo, String serviceName) {
		log.info("Inside requestForVerifyOTP ===");
		for (String key : ServiceCallAPI.apiURLMasterList.keySet()) {
			if (serviceName.toUpperCase().equals(key.trim().toUpperCase())) {
				baseURL = ServiceCallAPI.apiURLMasterList.get(key);
				break;
			}
		}
		String customerId = "";
		/*
		 * { "verifyOTP": { "version": "1", "customerID": "35212", "otp": { "otpKey":
		 * "lqdwnvw0zi0w", "otpValue": "111111" } } }
		 */
		// String Authorization = "Basic dGVzdGNsaWVudDp0ZXN0QDEyMw==";
		if (otpInfo.containsKey("ApproverId")) {
			customerId = otpInfo.getString("ApproverId").trim();
		} else {
			customerId = otpInfo.getString("CustomerId").trim();
		}

		Gson g3 = new Gson();
		Gson gson1 = new Gson();
		LinkedHashMap<String, Object> main_req_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> sub_header = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> s12_header = new LinkedHashMap<String, Object>();

		sub_header.put("version", "1");
		sub_header.put("customerID", customerId);

		s12_header.put("otpKey", otpInfo.getString("otpKey"));
		s12_header.put("otpValue", otpInfo.getString("otpValue"));

		sub_header.put("otp", gson1.fromJson(g3.toJson(s12_header), s12_header.getClass()));
		main_req_header.put("verifyOTP", gson1.fromJson(g3.toJson(sub_header), sub_header.getClass()));

		String request = g3.toJson(gson1.toJson(main_req_header, main_req_header.getClass()));
		log.info("request Header Before ===" + request);
		request = CommonUtility.convertBasicString(request);
		log.info("request Header ===" + request);
		String reqfrmAPI = "", apiResName = "";
		JSONObject reqJsonfrmAPI = null;
		JSONObject m_res_header = new JSONObject();// ResBody

		log.info("Request Header ===");

		if (SERVICE_TYPE.equals("true")) {
			log.info("If Service Type TRUE");

			// Go With API
			// log.info(" --> Calling ");

			// baseURL = "https://10.0.45.87:1443/OTPServiceJSON/verifyOTP";

			/*
			 * {"verifyOTPResponse": { "version": "1", "isValid": "false",
			 * "verificationFaultReason": "Invalid OTP Key or Maximum Attempts Reached" }}
			 */
			// ==="+baseURL);
			try {
				reqfrmAPI = Utils.DataFromService(baseURL, request, Authorization);
				log.info("verifyOTPResponse String Response<<<<<" + reqfrmAPI);
				reqJsonfrmAPI = JSONObject.fromObject(reqfrmAPI);
				log.info("verifyOTPResponse json<<<<<<<<<<" + reqJsonfrmAPI);

				// ----------------------------------For Dummy OTP for Google play stored
				// issue---------------
				/*
				 * if(customerId.equals("515151") &&
				 * otpInfo.getString("otpValue").equals("222222")) { m_res_header = new
				 * UserLoginDaoImpl().validateOTP_222222(otpInfo);
				 * log.info("Dummy OTP for Google play stored issue"+m_res_header); return
				 * m_res_header; }
				 */

				if (reqJsonfrmAPI.containsKey("ErrorDetails")) {
					/*
					 * modifyJson.put("Status", "Failure"); m_res_header.put("ResBody",
					 * reqJsonfrmAPI);
					 */
					return reqJsonfrmAPI;
				}
				log.info("verifyOTPResponse json<<<<<<<<<<" + reqJsonfrmAPI);
				apiResName = "verifyOTPResponse";
				if ("OBDX".equals(ServiceCallAPI.serverTypeApi)) {
					m_res_header = CommonUtility.beneErrorHandling1(reqJsonfrmAPI, apiResName);
				} else {
					m_res_header = CommonUtility.beneErrorHandling(reqJsonfrmAPI, apiResName);
				}

				// log.info("verifyOTPResponse json Res Body<<<<<<<<<<"+m_res_header);
				// log.info("m_res_header---",m_res_header);
				// Added by 2459 for audit activity details of customer OTP verified
				// successfully
				if (m_res_header.containsKey("ResBody")
						&& m_res_header.getJSONObject("ResBody").containsKey("verifyOTPResponse") && m_res_header
								.getJSONObject("ResBody").getJSONObject("verifyOTPResponse").containsKey("isValid")) {
					if ((m_res_header.has("ResCode") && m_res_header.getString("ResCode").equals("0")
							|| m_res_header.has("ResCode") && m_res_header.getString("ResCode").equals("2"))
							&& m_res_header.getJSONObject("ResBody").getJSONObject("verifyOTPResponse")
									.getString("isValid").equals("true")) {
						JSONObject auditJson = new JSONObject();
						auditJson.put("serviceId", serviceName);
						auditJson.put("customerId", customerId);
						generalService.addCustomerAuditDtl(auditJson);
					}
				}

			} catch (JSONException e) {
				log.info("JSONException---", e);
			} catch (Exception e) {
				log.info("Exception---", e);
			}

			log.info("Data from API: " + m_res_header.toString());

			return m_res_header;
		} else {
			log.info("If Service Type FALSE");

			// ----------------------------------For Dummy OTP for Google play stored
			// issue---------------
			/*
			 * if(customerId.equals("515151") &&
			 * otpInfo.getString("otpValue").equals("222222")) { m_res_header = new
			 * UserLoginDaoImpl().validateOTP_222222(otpInfo);
			 * log.info("Dummy OTP for Google play stored issue"+m_res_header); return
			 * m_res_header; }
			 */

			/*
			 * JSONObject temp = new JSONObject(); JSONObject temp1 = new JSONObject();
			 * temp.put("version", "1"); temp.put("isValid", "false");
			 * temp.put("verificationFaultReason",
			 * "Invalid OTP Key or Maximum Attempts Reached");
			 * 
			 * temp.put("isValid", "true"); temp.put("verificationFaultReason", "");
			 * temp1.put("verifyOTPResponse", temp);
			 * 
			 * m_res_header.put("ResCode", "0"); m_res_header.put("ResBody", temp1);
			 */

			m_res_header = loginDaoimpl.validateOTP(otpInfo);
			// log.info("m_res_header---",m_res_header);
			// Added by 2459 for audit activity details of customer OTP verified
			// successfully
			if (m_res_header.containsKey("ResBody")
					&& m_res_header.getJSONObject("ResBody").containsKey("verifyOTPResponse") && m_res_header
							.getJSONObject("ResBody").getJSONObject("verifyOTPResponse").containsKey("isValid")) {
				if ((m_res_header.has("ResCode") && m_res_header.getString("ResCode").equals("0")
						|| m_res_header.has("ResCode") && m_res_header.getString("ResCode").equals("2"))
						&& m_res_header.getJSONObject("ResBody").getJSONObject("verifyOTPResponse").getString("isValid")
								.equals("true")) {
					JSONObject auditJson = new JSONObject();
					auditJson.put("serviceId", serviceName);
					auditJson.put("customerId", customerId);
					generalService.addCustomerAuditDtl(auditJson);
				}
			}

			log.info("Response Header====" + m_res_header);
			return m_res_header;
		}

	}

	 
	 
	 
	 
	
}
	
	
