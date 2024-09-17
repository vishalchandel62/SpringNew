package com.credentek.msme.api.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.credentek.msme.database.DaoBase;
import com.credentek.msme.loginmodel.ChildOdInFcc;
import com.credentek.msme.loginmodel.LineIdParentLine;
import com.credentek.msme.loginmodel.StandAloneOdInFcc;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class accountDetailsForOnlineOdDaoImpl extends DaoBase implements accountDetailsForOnlineOdDao {
	
private static final Logger log = LogManager.getLogger(accountDetailsForOnlineOdDaoImpl.class);
	
	@Override
	public JSONObject fetchAccountDetailsForOd(String account,String custId,String lineId,String ParentLineID,String balFromAcctMast) 
	{
		log.info("In fetchAccountDetailsForOd function >>>>");
		JSONObject allAccountDtl = new JSONObject();
		Connection connection = null;
		PreparedStatement pstmt1 = null,pstmt2 = null,pstmt3 = null;
		ResultSet rset1 = null,rset2 = null,rset3 = null;
		try {
			String accountList = account;
			connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
			String l_parentLine = ParentLineID;
			String companyId = custId;
					
						
						StandAloneOdInFcc saOdInFccObject = new StandAloneOdInFcc();
						LineIdParentLine lineData = new LineIdParentLine();
						Double lessAmt = 0.00 ;
						//Double p_utilization = 0.00;
						Double p_netAvailableBalance = 0.00;
						//Double l_utilization = 0.00;
						Double c_netAvailableBalance = 0.00;
						//PARENT_LINE_DATA=SELECT LIMIT_AMOUNT as LIMIT_AMT, UTILISATION, DESCRIPTION FROM 
						//VW_LIMITS WHERE TRIM(LINE_ID) =TRIM(?) AND CUSTOMER_NO =? AND MAIN_LINE IS NULL AND 
						//AVAILABILITY_FLAG='Y'
						pstmt2 = connection.prepareStatement(getQuery("PARENT_LINE_DATA"));
						pstmt2.setString(1,l_parentLine);
						pstmt2.setString(2,companyId);
						rset2 = pstmt2.executeQuery();
						if(rset2.next()) {
								lineData.setLimitAmount(rset2.getString("LIMIT_AMT").trim());
								String maxUtil = rset2.getString("UTILISATION").trim();
								if(maxUtil.contains("-")) {
									lineData.setUtilization(maxUtil.substring(1, maxUtil.length()));
								}else {
									lineData.setUtilization(maxUtil);
								}
								Double plimAmt = rset2.getDouble("LIMIT_AMT");
								Double plimUtil = rset2.getDouble("UTILISATION");
								p_netAvailableBalance = plimAmt-plimUtil;
								if(p_netAvailableBalance > 0) {
									lessAmt = p_netAvailableBalance;
								}else {
									lessAmt = 0.00;
								}
								lineData.setDescription(rset2.getString("DESCRIPTION").trim());
								/*
								 * rset2.close(); pstmt2.close();
								 */
						
								//CALCULATION_LINE_ID=SELECT LIMIT_AMOUNT,UTILISATION,TRIM(MAIN_LINE) AS MAIN_LINE  
								//FROM VW_LIMITS WHERE CUSTOMER_NO = ? AND TRIM(LINE_ID) = TRIM(?) AND AVAILABILITY_FLAG='Y'
								pstmt1 = connection.prepareStatement(getQuery("CALCULATION_LINE_ID"));
								pstmt1.setString(1, companyId);
								pstmt1.setString(2, lineId);
								rset1 = pstmt1.executeQuery();
								if(rset1.next()) {
									Double c_limAmt = rset1.getDouble("LIMIT_AMOUNT");
									Double c_limUtil = rset1.getDouble("UTILISATION");
									c_netAvailableBalance = c_limAmt-c_limUtil;
									if(c_netAvailableBalance > lessAmt) {
								
									}else {
										lessAmt = c_netAvailableBalance;
									}
							
									Double balFromAcctMastD = new Double(balFromAcctMast);
									if(balFromAcctMastD<=0.0) {
										if(lessAmt<=0.0) {
											lineData.setAvailableAmount("0.00");
										}else if(p_netAvailableBalance < c_netAvailableBalance){
											lineData.setAvailableAmount(p_netAvailableBalance.toString());
										}else {
											lineData.setAvailableAmount(lessAmt.toString());
										}
									}else if(balFromAcctMastD>0.0) {
										BigDecimal balFromAcctMastB = new BigDecimal(balFromAcctMast);
										if(lessAmt<=0.0) {
											lineData.setAvailableAmount(balFromAcctMastB.toString());
										}else if(p_netAvailableBalance < c_netAvailableBalance){
											p_netAvailableBalance=p_netAvailableBalance+balFromAcctMastD;
											lineData.setAvailableAmount(p_netAvailableBalance.toString());
										}else {
											lessAmt=lessAmt+balFromAcctMastD;
											lineData.setAvailableAmount(lessAmt.toString());
										}
									}
									
									
									saOdInFccObject.setLineIdParentLine(lineData);
									
						
									pstmt3 = connection.prepareStatement(getQuery("GET_CHILD"));
									pstmt3.setString(1, companyId.trim());
									pstmt3.setString(2, l_parentLine);
									rset3 = pstmt3.executeQuery();
									ArrayList<ChildOdInFcc> childOdArray = new ArrayList<ChildOdInFcc>();
									while(rset3.next()) {
										ChildOdInFcc childOd = new ChildOdInFcc();
										childOd.setChildLimitAmount(rset3.getString("LIMIT_AMT").trim());
										String c_maxUtil = rset3.getString("UTILISATION").trim();
										if(c_maxUtil.contains("-")) {
											childOd.setChildUtilization(c_maxUtil.substring(1, c_maxUtil.length()));
										}else {
											childOd.setChildUtilization(c_maxUtil);
										}
								    
										BigDecimal childLimit , childBal, childUtil;
										childLimit = new BigDecimal(rset3.getString("LIMIT_AMT"));
										childUtil = new BigDecimal(rset3.getString("UTILISATION"));
										childBal = childLimit.subtract(childUtil);
										String bal = childBal.toString();
										if(bal.contains("-")) {
											childOd.setChildAvailableAmount(bal.substring(1, bal.length()));
										}else {
											childOd.setChildAvailableAmount(bal);
										}
									
										childOd.setChildDescription(rset3.getString("DESCRIPTION").trim());
										childOdArray.add(childOd);
									}
								
									saOdInFccObject.getLineIdParentLine().setChildOd(childOdArray);;
									saOdInFccObject.setLineId(lineId);
									saOdInFccObject.setAccountNumber(accountList);
									rset3.close();
									pstmt3.close();
									allAccountDtl.put("StandAloneODAccountInFCC", saOdInFccObject);
				
								}else {
									allAccountDtl.put("cflag","false");
								}
								rset1.close();
								pstmt1.close();
						}else {
							allAccountDtl.put("pflag","false");
						}
						rset2.close();
						pstmt2.close();

		} catch (SQLException e) {
			//log.info("SQLException.....",e);
		}
		finally {
			//log.info("finalJson.....");
			try {
				
			} catch (JSONException e) {
				//log.info("exception in connection closing time.....");
			}
		}
		return allAccountDtl;
	}


	@Override
	public String makeListOfAccount(HashSet<String> listAsccount) {
		Iterator<String> it = listAsccount.iterator();
		String listOfAccountDB = "" ; 
		while (it.hasNext()) {
			String itVal = it.next();
			listOfAccountDB = itVal +","+ listOfAccountDB;
		}
		
		return listOfAccountDB.substring(0,listOfAccountDB.length()-1);
	}


	@SuppressWarnings("finally")
	@Override
	public String formatAllSrn(String singleBeneJson, String GroupBeneJson) {
		//log.info("in formatAllSrn method");
		JSONObject formatSingleBeneJson =JSONObject.fromObject(singleBeneJson);
		JSONObject formatGroupBeneJson = JSONObject.fromObject(GroupBeneJson);
		JSONArray singleBeneArray = formatSingleBeneJson.getJSONObject("ResBody").getJSONObject("getAllBeneficiariesResponse").getJSONObject("beneficiaryArray").getJSONArray("getbeneficiaryDetail");
		JSONArray groupBeneArray = formatGroupBeneJson.getJSONObject("ResBody").getJSONArray("GroupDetails");
		JSONObject returnObj1 = new JSONObject();
		JSONObject returnObj = new JSONObject();
		try {
			//log.info("singleBeneArray.........");
		for(int i = 0;i<groupBeneArray.size();i++) {
			JSONArray groupArray = groupBeneArray.getJSONObject(i).getJSONArray("BeneDetails");
			for(int j = 0;j<groupArray.size();j++) {
				for(int k = 0;k<singleBeneArray.size();k++) {
					if(groupArray.getJSONObject(j).containsKey("customerID")) {
						if(groupArray.getJSONObject(j).getString("beneficiaryID").equals(singleBeneArray.getJSONObject(k).getString("beneficiaryID")) && groupArray.getJSONObject(j).getString("customerID").equals(singleBeneArray.getJSONObject(k).getString("customerID"))) {
							groupArray.getJSONObject(j).put("SRN", singleBeneArray.getJSONObject(k).getString("SRN"));
						}
					}else {
						if(groupArray.getJSONObject(j).getString("beneficiaryID").equals(singleBeneArray.getJSONObject(k).getString("beneficiaryID"))) {
							groupArray.getJSONObject(j).put("SRN", singleBeneArray.getJSONObject(k).getString("SRN"));
							groupArray.getJSONObject(j).put("customerID", singleBeneArray.getJSONObject(k).getString("customerID"));
						}
					}
					
				}
			}
			groupBeneArray.getJSONObject(i).put("BeneDetails",groupArray);
		}
		
		returnObj1.put("GroupDetails", groupBeneArray);
		
		returnObj.put("ResBody", returnObj1);
		returnObj.put("ResCode", formatGroupBeneJson.getString("ResCode"));
		}catch(Exception e) {
			//log.info("Exception occured",e);
		}finally{
			return returnObj.toString();
		}
	}
	
	


}
