package com.credentek.msme.database;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.credentek.msme.utils.PathRead;

public class DataSourceBase {
	
	
	//private static final Log log = LogFactory.getLog(DataSourceBase.class);
		private static final Logger log = LogManager.getLogger(DataSourceBase.class);
		private static DataSource dataSource;
		private static DataSource beneMsgSendDataSource;
		private static String yesAmazonFlag;
		private static DataSource yesAmazondataSource;
		private static Context initContext;
		private String datasourceName;

		static {
			try {
				initContext = new InitialContext();
				PathRead path = PathRead.getInstance();
				dataSource = (DataSource) initContext.lookup(path.getPath("dataSource").trim());
				beneMsgSendDataSource = (DataSource) initContext.lookup(path.getPath("beneMsgSendDataSource").trim());
				yesAmazonFlag = path.getPath("YES_AMAZON_ACTIVE_FLAG").trim();
				log.info("yesAmazonFlag ==="+yesAmazonFlag);
				
				if(yesAmazonFlag.equals("True"))
				{
					yesAmazondataSource= (DataSource) initContext.lookup(path.getPath("yesAmazondataSource").trim());	
					log.info("YES AMAZON Data Source Created ==="+path.getPath("yesAmazondataSource"));
				}
				
	            		
	            log.info("SME Data Source Created ==="+path.getPath("dataSource"));
	            log.info("---------- SME BACK END (REST SERVICES) UAT  RELEASE --------- VERSION  15.2.0---------- ON DATE 28 May 2024  Default Limit SetUp CR Changes UAT");
	            
		        log.info("---------- SME BACK END (REST SERVICES) UAT  RELEASE --------- VERSION  15.1.0---------- ON DATE 24 April 2024  Third_Party_FD CR Changes UAT");
	 		
		        log.info("---------- SME BACK END (REST SERVICES) UAT  RELEASE --------- VERSION  14.9.0---------- ON DATE 13 FEB 2024  TAT and DPSC Guidelines  CR");
	            
	            
	            log.info("---------- SME BACK END (REST SERVICES) UAT  RELEASE --------- VERSION  8.2.3---------- ON DATE 29 JAN 2024  Payout Frequency issue ");
	            
	            log.info("---------- SME BACK END (REST SERVICES) Live  RELEASE --------- VERSION  7.6.0---------- ON DATE 2 JAN 2024  SIM BINDING ");

	            
	            log.info("---------- SME BACK END (REST SERVICES) Live  RELEASE --------- VERSION  7.5.0---------- ON DATE 30 nov 2023  EFRM (RD & ENQUE ) AND STRONG MPIN TPIN ");

	            
	            log.info("---------- SME BACK END (REST SERVICES) Live  RELEASE --------- VERSION  7.4.0---------- ON DATE 28 nov 2023  consolated Patch by AMc unwanted credit alert + unable to download, + slowness + duplicated recode in report ");

	            log.info("---------- SME BACK END (REST SERVICES) Live  RELEASE --------- VERSION  7.4.0---------- ON DATE 3 nov 2023  consolated Patch by AMc unwanted credit alert + unable to download, + slowness + duplicated recode in report ");

	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE --------- VERSION 8.2.2 ---------- ON DATE 1 nov 2023  consolated Patch by AMc ");

	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE --------- VERSION 7.3.0 ---------- ON DATE 16 OCT 2023  ENABLE FULL ACCESSS ");

	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE --------- VERSION 14.4.0 ---------- ON DATE 09 OCT 2023  ENABLE FULL ACCESSS ");
	            log.info("---------- SME BACK END (REST SERVICES) Prod RELEASE --------- VERSION 7.2.0 ---------- ON DATE 01 Septmber 2023  1.  UPI IFSC, Email ID and PIN code changes while UPI onboarding. 2. Forgot password and Unblock changes for alias id. 2. AMC CR Product Code (236,249) 4 .park flag removed and Duplicate check removed by AMC");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE --------- VERSION 14.1.0 ---------- ON DATE 28 August 2023   1.  UPI IFSC, Email ID and PIN code changes while UPI onboarding. 2. Forgot password and Unblock changes for alias id. 3. AMC CR Product Code (236,249) 4 Park flag remove for bulk and salary");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE --------- VERSION 14.0.0 ---------- ON DATE 17 August 2023   1.  UPI IFSC, Email ID and PIN code changes while UPI onboarding. 2. Forgot password and Unblock changes for alias id. 3. AMC CR Product Code (236,249)");
	            log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE --------- VERSION 7.1.0 ---------- ON DATE 31 July 2023  1.  APPSEC points –a.       Improper Error Handling b.       Username Enumeration 2. AMC CR Product Code (204,210,240)");
	            log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE --------- VERSION 7.0.0 ---------- ON DATE 13 July 2023  1. SOBO 2. Cooling period,3. Group payment verification 4. UPI Collect – (Onboarding, Static QR, Dynamic QR and Collect Request) 5. Block/ Unblock 6. Log4j for portal,7 Yes Connect Icon, 8 MSME Admin - MCC code master maker & MCC code master author  9 MSME Admin - UPI Onboarding Reports 10 MSME Admin - SOBO Scheduler Report at 13-07-2023");
	            log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 6.4.0 ---------- ON DATE 26th May 2023  1> SMS communication after maker creation in Yes MSME");
	            log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 6.3.0 ---------- ON DATE 10th May 2023  Consolidated Release for mention bugs and CR's - 1) YES MSME || Disconnect in Customer Registration, 2) GetAllBeneficiaries API calls during application login, 3) Session expired issue for Invalid IFSC while doing Fund Transfer, 4) Implementation of PAN Number Masking in the Application Logs, 5) Product code - 237 addition in Yes MSME");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 7.9.2 ---------- ON DATE 04th May 2023  Pincode Masking, Maker details page");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 7.9.1 ---------- ON DATE 20th APR 2023  Pincode Masking,login page,view limit page, create maker page");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 7.9.0 ---------- ON DATE 12 APR 2023  Merge code for Add new product code,remove GetAllBeneficiaries API calls during application login, Disconnect in Customer Registration issue, PAN Card Masking in log, Session Expired error message issue and Terms condition page issue");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 7.8.1 ---------- ON DATE 21 MAR 2023  Merge code for remove GetAllBeneficiaries API calls during application login, Disconnect in Customer Registration issue, PAN Card Masking in log, Session Expired error message issue and Terms condition page issue");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 7.8.0 ---------- ON DATE 06 MAR 2023  Remove GetAllBeneficiaries API calls during application login");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 7.7.0 ---------- ON DATE 27 FEB 2023  Disconnect in Customer Registration issue");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 7.6.2 ---------- ON DATE 07 FEB 2023  PAN Card Masking in log");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 7.6.1 ---------- ON DATE 06 FEB 2023  Session Expired error message issue--");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 7.6.0 ---------- ON DATE 20 JAN 2023  Development CR View Limit--");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 6.1.1 ---------- ON DATE 3rd JAN 2023  Development CR merger with salary and bulk transaction report issue +FUND transfer issue for maker login --------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 7.5.0 ---------- ON DATE 23rd Dec 2022 Development CR merger with salary and bulk transaction report issue --------");
	           
	            log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ----------VERSION 6.1.0------- ----- 1> UI/UX modifications. 2> CRM module for customer queries. \n" + 
	            		"3> Limits on the number of Makers that can be added. 4> Limits on the number of transactions that can be created by the Maker in a dayPayment Advice & SMS details. 5> Payment Advice for transactions.");
	           
	            log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ----------VERSION 7.4.0------- -----1> Enabling customers to have full access even after registering using Debit card/ Auth signatory details. 2> UI/UX modifications. 3> CRM module for customer queries. \n" + 
	            		"4> Limits on the number of Makers that can be added. 5> Limits on the number of transactions that can be created by the Maker in a dayPayment Advice & SMS details. 6> Payment Advice for transactions.");
	            
	            log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 6.0.0---------- ON DATE 18 Nov 2022 1> Enabling customer to have full access even after registering using Debit card/ Auth signatory details 2> UI/UX modifications 3> CRM module for customer queries "
	            					 +" 4> Limits on number of Makers that can be added 5> Limits on number of transactions that can be created by the Maker in a day 6> Payment Advice & SMS details");
	            log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 5.0.2 ---------- ON DATE 27 oct 2022 Duplicate ref number for bulk upload issue --------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 7.1.2 ---------- ON DATE 19 oct 2022 Duplicate ref number for bulk upload issue --------");
	            log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 5.0.1 ---------- ON DATE 10 oct 2022 IMPS issue --------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 7.1.1 ---------- ON DATE 06 oct 2022 IMPS issue --------");
	            log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ----------VERSION 5.0.0 ---------- 1> IMPS dynamic limit from DB 2> Bulk transfer file upload mobile number non-mandatory (Portal and Mobile services) 3> Bulk transfer Unified file format (header non-mandatory)  (Portal and Mobile services) "
	            				+ "4> Sorting & Smart Search for Manage Beneficiary  (Portal and  2 JSP pages) 5> Change Login Button UI and Removed Hindi & English videos  logo and link (Portal and  1 JSP pages) 6> Avoid OTP flooding attack (Mobile services) "
	            				+ "7> New Error code change  (JSP Pages, Portal, IPA , APK and Mobile services) 8> AMC changes for prod code(207 , 208) 9> AMC changes for unique reference number");
	            log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 4.1.1 ---------- ON DATE 17 March 2022  -Update log4j file creation limit and file limit");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 4.0.3 ---------- ON DATE 03 March 2022  -Update log4j file creation limit and file limit");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 4.1.0 ---------- ON DATE 03 March 2022 Live CR for Download as pdf in transction summay for adhoc payment and to register bebeficiary ");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 03 Feb 2022 Live issue merged of Channel Id for Bill Payment and log4j");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 06 August 2021 Live issue merged of PAN card in create fd");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 05 August 2021 RNB Customer beneficiary merged with FD changes, Account Statement Changes");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 20 April 2021 RNB Customer beneficiary");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 07 April 2021 Merged code with AMC");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 27 March 2021 Chnage - log path changed for olympiad environment");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 24 March 2021 Chnage - Account statement height");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 23 March 2021 Chnage - Account statement changes for address format (country,pincode)");
	            log.info("---------- SME-AMC BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.11 ---------- ON DATE 22 MAR 2021 Thread Stuck - set connection timeout to 2 min at API level and replace try with resource");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 18 March 2021 Chnage - Account statement changes for filter");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 17 March 2021 Chnage - Account statement changes for filter");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 16 March 2021 Chnage - Account statement changes");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 15 March 2021 Chnage - BANK to Bank in AllTransactionMaintainDaoImpl -> sendMsgToBeneficiary");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.11 ---------- ON DATE 12 MAR 2021 with amc latest build chnage in msgToBeneficiary - BANK-Bank");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 11 March 2021 part2 Account STatement Changes as per the new requirement (Yes msme logi on left side and customer details)");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 11 March 2021 Account STatement Changes as per the new requirement (Yes msme logi on left side and customer details)");
	            log.info("---------- SME-AMC BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.11 ---------- ON DATE 10 MAR 2021 pass customerid against alias id in RNB(sqlexception handle), LoanDetails, High Connection count handle in debit and authorized signatory ");
	            log.info("---------- SME-AMC BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 04 MAR 2021 pass customerid against alias id to dropoff table if customer reg using alias id ");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 27 FEB 2021 fetchBill,makerLogin");
	            log.info("---------- SME-AMC BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 19 FEB 2021 High Active Connection Count ");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 17 FEB 2021 frequency Release");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 15 FEB 2021 log path Release");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 04 FEB 2021 YESUAT Release");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 02 FEB 2021 billdesk response handling internal http");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 29 JAN 2021 billdesk release internal http");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 21 JAN 2021 billdesk release");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 20 JAN 2021 group breakups android cases");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 20 JAN 2021 group breakups live issue");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 14 JAN 2021 cheque remarks column added");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 11 JAN 2021 change pin request old and new pin in loweer case");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 10 JAN 2021 sal changes in transactin summary and sensitive logs hidden in transactio for manage cards");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 09 JAN 2021 Positive Pay in Trasnaction Summary bugs");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 09 JAN 2021 Positive Pay in Trasnaction Summary");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 09 JAN 2021 Salary and bulk ifsc changes");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 08 JAN 2021 Positive Pay Change as per new FSD");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 06 JAN 2021 part 3 fetch cvvdecryption issue and cvv rersponse error");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 05 JAN 2021 part 3 base url extra appednong commneted");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 05 JAN 2021 errror handing in api call of debit card and cvv");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 05 JAN 2021 result key added in our static json of manage card");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 02 JAN 2021 debbit card info list url change-");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 01 JAN 2021 (manage card, account statement mail font changes)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 25 DEC 2020 (manage cardsresponse(fetch cvv debit card list and change pin )--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 23 DEC 2020 (download all transaction summary issue)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 22 DEC 2020 (account statement issue,transaction summary issue)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 19 DEC 2020 (all issue raised by YBL)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 17 DEC 2020 (group breakups issue for service type true)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 16 DEC 2020 (pdf changes)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 16 DEC 2020 (group break up)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 14 DEC 2020 (group break up release,image offer changes,Account pdf changes)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 12 DEC 2020 (group break up release with image offer changes)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 09 DEC 2020 (image offer release)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 09 DEC 2020 (response handling)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 04 DEC 2020 (images path and base url code missing release)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 04 DEC 2020 (change in time stamp)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 03 DEC 2020 (path change and response change)--------");
	            log.info("---------- SME-AMC BACK END (REST SERVICES) UAT RELEASE ------ VERSION 1.2.10 ---------- ON DATE 25 NOV 2020 (GST limit setup issue and SMTP connection timeout for live issue thread stuck)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.9 ---------- ON DATE 24 NOV 2020 ( customer activity entry and drop of changes)");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.9 ---------- ON DATE 20 NOV 2020 ( double entry in drop_off table NVL handling)");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.9 ---------- ON DATE 20 NOV 2020 ( double entry in drop_off table)");
	            log.info("---------- SME-AMC BACK END (REST SERVICES) UAT RELEASE ------ VERSION 1.2.10 ---------- ON DATE 18 NOV 2020 ( SMTP connection timeout for live issue thread stuck)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.9 ---------- ON DATE 17 NOV 2020 ( Own Account Issue)");
	            log.info("---------- SME-AMC BACK END (REST SERVICES) UAT RELEASE ------ VERSION 1.2.10 ---------- ON DATE 12 NOV 2020 ( finally handle for all rest service)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.9 ---------- ON DATE 10 NOV 2020 ( Added extra log for FD)");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.9 ---------- ON DATE 07 NOV 2020 ( Login issue)");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.9 ---------- ON DATE 07 NOV 2020 ( SI insertion, response handled for 500,maker initiated checker approved)");
	            log.info("---------- SME-AMC BACK END (REST SERVICES) UAT RELEASE ------ VERSION 1.2.10 ---------- ON DATE 4 NOV 2020 ( JDBC COnnection and Logged in with another device(token validation change-handle numberformatException and GroupBene)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.9 ---------- ON DATE 03 NOV 2020 ( SI Issue,OpenFD issue, RNB fail cases not getting listed in centralized table)");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.9 ---------- ON DATE 31 OCT 2020 Forgot pasword will not be inserted in SMW Drop off table");
	            log.info("---------- SME-AMC BACK END (REST SERVICES) UAT RELEASE ------ VERSION 1.2.10 ---------- ON DATE 28 OCT 2020 ( JDBC COnnection and Logged in with another device(token validation change))--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.9 ---------- ON DATE 23 OCT 2020 ( log updated OBDX code)--------");
	            log.info("---------- SME-AMC BACK END (REST SERVICES) UAT RELEASE ------ VERSION 1.2.10 ---------- ON DATE 22 OCT 2020 (Login with another device and destination account fetch(except FD acc) issue)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.9 ---------- ON DATE 21 OCT 2020 ( Merged with OBDX code)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.9 ---------- ON DATE 20 OCT 2020 ( Live release)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.9 ---------- ON DATE 13 OCT 2020 ( Requester Id changes with YESUAT)--------");
	            log.info("---------- SME-AMC BACK END (REST SERVICES) UAT RELEASE ------ VERSION 1.2.10 --------- ON DATE 12 OCT 2020 ( Duplicate transaction request of pay to registered beneficiaryyyyyyyyyyy)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.9 ---------- ON DATE 12 OCT 2020 ( Requester Id changes)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.9 ---------- ON DATE 06 OCT 2020 ( YESUAT Relese sync with obdx live release and SIT module)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.9 ---------- ON DATE 06 OCT 2020 ( OBDX and YESUAT merged in obdx release)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.9 ---------- ON DATE 02 OCT 2020 ( OBDX salt id changes with new api call UAT release 09:28)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.9 ---------- ON DATE 02 OCT 2020 ( OBDX salt id changes with new api call UAT release)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.9 ---------- ON DATE 28 SEP 2020 ( DROP OFF SIT Release)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.9 ---------- ON DATE 24 SEP 2020 ( OBDX salt id changes live code Release UAT release)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.9 ---------- ON DATE 24 SEP 2020 ( YESUAT UAT with live code Release code cleared from insert query)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.9 ---------- ON DATE 24 SEP 2020 ( YESUAT UAT with live code Release)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.9 ---------- ON DATE 22 SEP 2020 ( YESUAT UAT Release)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.9 ---------- ON DATE 21 SEP 2020 ( OBDX UAT Release)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.9 ---------- ON DATE 17 SEP 2020 ( LIVE YESUAT UAT Release)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.9 ---------- ON DATE 11 SEP 2020 ( DLOD SIT Release)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.9 ---------- ON DATE 11 SEP 2020 ( Beneficairy mobile no insert NA after authorization Release and AMC)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.8 ---------- ON DATE 10 SEP 2020 ( xlsx,xls column Release)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.8 ---------- ON DATE 08 SEP 2020 ( ASLC Release)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.8 ---------- ON DATE 07 SEP 2020 ( DLOD Release)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.8 ---------- ON DATE 07 SEP 2020 ( Merged with AMC Release)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.8 ---------- ON DATE 04 SEP 2020 ( NON-RNB LIVE Release for UAT Environment)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.8 ---------- ON DATE 31 AUG 2020 ( YESUAT Release)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.8 ---------- ON DATE 28 AUG 2020 ( Print proper status report of SI (remove blank row))--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.8 ---------- ON DATE 27 AUG 2020 ( Bulk-Salary xls,xlsx file change(Date and Header remove))--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.8 ---------- ON DATE 24 AUG 2020 ( RNB cust entry into non-rnb)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.8 ---------- ON DATE 21 AUG 2020 ( E-mail issue)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.8 ---------- ON DATE 18 AUG 2020 ( Non-Rnb UAT release and xlsx,xls file SIT Release)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.8 ---------- ON DATE 14 AUG 2020 ( Non-rnb error code validation  )--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.8 ---------- ON DATE 10 AUG 2020 ( manage E-code,Non-RNB beneficiary add  )--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.8 ---------- ON DATE 12 AUG 2020 ( Non-RNB SI change )--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 22 JUL 2020 ( Changes for E-collect of query without trim )--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 22 JUL 2020 ( Changes for E-collect of query )--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 13 JUL 2020 ( Issue Resolved  )--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 13 JUL 2020 ( Phase 4 Part B Release  )--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 13 JUL 2020 ( TPT and FT add bene in DB,Response Format change, OBDX no of bene change )--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 09 JUL 2020 ( TPT and FT add bene in DB)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 09 JUL 2020 ( index out of bound for add bene in DB)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 08 JUL 2020 ( OBDX Release for add beneficiary transfer type)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 07 JUL 2020 ( OBDX Release for bad request of delete beneficiary )--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 06 JUL 2020 ( FT file type unable to upload issue resolved )--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 04 JUL 2020 ( OBDX Release with the raised issue of YBL )--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 04 JUL 2020 ( ASLC Release 2v )--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 16 Jun 2020 ( Deactivate api Encryption Key change )--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 12 Jun 2020 ( Deactivate api Encryption file change )--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.6 ---------- ON DATE 08 JUN 2020 SME API Modification Op mode O approval changes--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.6 ---------- ON DATE 08 JUN 2020 SME API Flag changes in SUM excluding I & P--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.6 ---------- ON DATE 06 JUN 2020 SME API Modification ExtRef key missed In FT2 to P2A--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.6 ---------- ON DATE 05 JUN 2020 SME API Modification In FT2 to P2A--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.6 ---------- ON DATE 04 JUN 2020 SME API Modification In FT2 to P2A--------");
		        log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 20 May 2020 ( Key Missed in API)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 20 May 2020 ( Missed OWN ACCOOUNT CHANGE AND E-collect count)--------");
	            
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.6 --------- ON DATE 20 May 2020 LIVE Release SME Prod Issue(Holiday,OWN ACCOUNT,IMPS) with AMC Release --------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 29 May 2020 ( Heap Memory Thread UAT Release )--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 29 May 2020 ( API Modification UAT Release )--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 27 May 2020 ( Heap memory utili UAT Release )--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 25 May 2020 ( E-collect bene list UAT Release )--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 23 May 2020 ( OD Renewal offer UAT Release )--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 20 May 2020 ( Key Missed in API)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 20 May 2020 ( Missed OWN ACCOOUNT CHANGE AND E-collect count)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 19 May 2020 ( E-collect issue raised by YYBL and Live code )--------");
		        log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.6 ---------- ON DATE 15 May 2020 SME Prod Change in Adhoc request--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.6 ---------- ON DATE 10 May 2020 UAT release SME Prod Issue--------");






	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 30 Apr 2020 ( Deactivate-api test)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 30 Apr 2020 ( ODCPV csv file change and mobile number validation)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 28 Apr 2020 (E-collect please try again issue changed key as per the api amount as transamount and refe number as bank ref num) --------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 28 Apr 2020 (E-collect please try again issue, Time stam for Send msg,alter column for all transaction) --------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 23 Apr 2020 (E-collect API change,NEFT Condition,RTGS Condition,IFSC code fetch,In condition,Insert in Timetime regarding send message to beneficiary) --------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 17 Apr 2020 (Checker Approver UAT Release) --------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 16 Apr 2020 (E-collect UAT/OBDX Release) --------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 15 Apr 2020 (SI Please try again 6:18 PM) --------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 15 Apr 2020 (SI Please try again 5:12 PM) --------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 15 Apr 2020 (SI Please try again) --------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 10 Apr 2020 (UAT Issue e-collect flag true 16:00) --------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 10 Apr 2020 (UAT Issue Centralized Data saving for create fd) --------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 09 Apr 2020 (UAT Issue Centralized Data saving for create fd) --------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 07 Apr 2020 (UAT Issue Centralized Data saving,send msg to beneficiary,ODCPV Login,E-collect issue insufficient right) --------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 04 Apr 2020 (UAT Issue Centralized Data saving for pay to beneficiary) --------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 02 Apr 2020 (UAT Issue Centralized Data saving) --------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 18 Mar 2020 (API Rejection,Insert API response in New table,double Transaction) --------");log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.6 ---------- ON DATE 17 MAR 2020 OBDX release --------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.6 ---------- ON DATE 12 MAR 2020 YESUAT release --------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.6 ---------- ON DATE 11 MAR 2020 UAT release Phase4 PartB Ecollect --------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 04 MAR 2020 UAT release Positive Pay Authorisation date Issue 12-54 --------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 04 MAR 2020 UAT release Positive Pay Authorisation date Issue --------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 03 MAR 2020 UAT release Positive Pay Authorisation date Issue --------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 03 MAR 2020 UAT release with removed log --------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 02 MAR 2020 UAT release for OBDX For Header --------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 02 MAR 2020 UAT release for beneficiary List --------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 28 FEB 2020 UAT release for OBDX --------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 27 FEB 2020 UAT release Positve Pay Cheque Datee Query (Added to_date in all insert queries of positive pay)");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 26 FEB 2020 UAT release for  / to - to ensure correct json is created for sme_txn_audit_activity_log(change in query SELECT_FROM_SME_TXN_APPROVAL_ACTIVITY_LOG)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 25 FEB 2020 UAT release for Positive Pay Updated Approach --------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 25 FEB 2020 UAT release for OBDX and YESUAT --------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 20 Feb 2020 YESUAT Release --------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 19 Feb 2020 OBDX Release for beneficiary--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 18 Feb 2020 Appsec release log missed and Bug-Resolved--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 17 Feb 2020 Appsec release--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 07 Feb 2020 YESUAT Release with change in JNDI name and added two field in header and client id in API--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 04 Feb 2020 ODCPV Release and Req/Res hide--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 29 Jan 2020 Positive Pay Bug--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 23 Jan 2020 Sme Bank Limit--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 20 Jan 2020 Yesuat changes--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 03 Jan 2020 POS Updated Changes--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 02 Jan 2020 OBDX release Login Issue--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.2 (phase 3 NEFT 24*7 Transaction and Holiday Msg Resolved Bug updated)---------- ON DATE 16 DEC 2019 (18:58 PM)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.2 (phase 3 NEFT 24*7 Transaction and Holiday Msg Resolved Bug)---------- ON DATE 16 DEC 2019 (16:30 PM)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.2 (phase 3 NEFT 24*7 Transaction and Holiday Msg )---------- ON DATE 15 DEC 2019 (8:30 AM)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 12 Dec 2019 OBDX release--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 29 November  Positve Pay--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.5 (SMS Alert and Login Details)---------- ON DATE 27 NOV 2019 (19:50 PM)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.2 (phase 3 updated release of calculation )---------- ON DATE 4 NOV 2019 (12:30 PM)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.3 (To Merge Phase 4)---------- ON DATE 17 OCT 2019 (12:47 AM)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.2 (UAT consolidated release)---------- ON DATE 11 OCT 2019 (12:30 PM)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.2 ()---------- ON DATE 02 OCT 2019 (04:06 AM)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.2 (Force Update)---------- ON DATE 23 SEP 2019 (21:06 PM)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.2 (10 Observation mail patch)---------- ON DATE 16 SEP 2019 (16:01 PM)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.2 (Limit Utilization changes and new API call getCasaBalance)---------- ON DATE 13 SEP 2019 (20:01)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.2 (Dashboard query changes)---------- ON DATE 09 SEP 2019 (13:01 PM)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.2 (Double Transaction Extra column added,OD CPV Process Flag Y)---------- ON DATE 05 SEP 2019 (16:01 PM)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.2 (OD CPV Process Flag Y)---------- ON DATE 03 SEP 2019 (15:01 PM)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.2 (OD CPV Process)---------- ON DATE 30 AUG 2019 (17:01 PM)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.2 (Yes Amazon UAT)---------- ON DATE 23 AUG 2019 (19:01 PM)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.2 (Parameter Master MAP list load by Thread, Double TXN,Help Section,Purg Activity with extra log,User Limit Setup ,live code added, Portal Dashboard,ASLC Patch,DASHBOARD dynamic, help description, eefc logic,bugzilla issue,mailChanges of limit,dashboard design,dashboard design space found query change,availibilityFlag,Daily limit check,Email format,Key Value Change,Availability Flag Found N In DB and Intraction done with vw_ch_od_limit table and added NA,Added in show of Utilization in FCR and FCC View and Bug)---------- ON DATE 16 AUG 2019 (20:01 PM)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.2 (Parameter Master MAP list load by Thread, Double TXN,Help Section,Purg Activity with extra log,User Limit Setup ,live code added, Portal Dashboard,ASLC Patch,DASHBOARD dynamic, help description, eefc logic,bugzilla issue,mailChanges of limit,dashboard design,dashboard design space found query change,availibilityFlag,Daily limit check,Email format,Key Value Change,Availability Flag Found N In DB and Intraction done with vw_ch_od_limit table and added NA)---------- ON DATE 08 AUG 2019 (20:01 PM)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.2 (Parameter Master MAP list load by Thread, Double TXN,Help Section,Purg Activity with extra log,User Limit Setup ,live code added, Portal Dashboard,ASLC Patch,DASHBOARD dynamic, help description, eefc logic,bugzilla issue,mailChanges of limit,dashboard design,dashboard design space found query change,availibilityFlag,Daily limit check,Email format,Key Value Change)---------- ON DATE 05 AUG 2019 (19:00 PM)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.2 (Parameter Master MAP list load by Thread, Double TXN,Help Section,Purg Activity with extra log,User Limit Setup ,live code added, Portal Dashboard,ASLC Patch,DASHBOARD dynamic, help description, eefc logic,bugzilla issue,mailChanges of limit,dashboard design,dashboard design space found query change)---------- ON DATE 26 JULY 2019 (16:23 PM)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.2 (Parameter Master MAP list load by Thread, Double TXN,Help Section,Purg Activity with extra log,User Limit Setup ,live code added, Portal Dashboard,ASLC Patch,DASHBOARD dynamic, help description, eefc logic,bugzilla issue,mailChanges of limit,dashboard design,dashboard design space found query change)---------- ON DATE 25 JULY 2019 (19:15 PM)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.2 (Parameter Master MAP list load by Thread, Double TXN,Help Section,Purg Activity with extra log,User Limit Setup ,live code added, Portal Dashboard,ASLC Patch,DASHBOARD dynamic, help description, eefc logic,bugzilla issue,mailChanges of limit,dashboard design)---------- ON DATE 25 JULY 2019 (02:15 AM)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.2 (Parameter Master MAP list load by Thread, Double TXN,Help Section,Purg Activity with extra log,User Limit Setup ,live code added, Portal Dashboard,ASLC Patch,DASHBOARD dynamic, help description, eefc logic,bugzilla issue,mailChanges of limit)---------- ON DATE 22 JULY 2019 (21:15 PM)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.2 (Parameter Master MAP list load by Thread, Double TXN,Help Section,Purg Activity with extra log,User Limit Setup ,live code added, Portal Dashboard,ASLC Patch,DASHBOARD dynamic, help description, eefc logic)---------- ON DATE 19 JULY 2019 (14:15 PM)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.2 (Parameter Master MAP list load by Thread, Double TXN,Help Section,Purg Activity with extra log,User Limit Setup ,live code added,ASLC Patch, Portal Dashboard,)---------- ON DATE 12 JULY 2019 (16:00 PM)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.2 (Parameter Master MAP list load by Thread, Double TXN,Help Section, Portal Dashboard,Purg Activity with extra log,User Limit Setup ,live code added,ASLC Patch)---------- ON DATE 10 JULY 2019 (15:00 PM)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.2 (Parameter Master MAP list load by Thread, Double TXN,Help Section, Portal Dashboard,Purg Activity with extra log,User Limit Setup ,live code added)---------- ON DATE 08 JULY 2019 (15:00 PM)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.2.2 (Parameter Master MAP list load by Thread, Double TXN,Help Section, Portal Dashboard,Purg Activity with extra log,User Limit Setup ,live code added)---------- ON DATE 29 JUNE 2019 (20:45 PM)--------");
	            
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.1.12 (Parameter Master MAP list load by Thread, Double TXN,Help Section, Portal Dashboard,Purg Activity with extra log)---------- ON DATE 10 JUNE 2019 (20:45 PM)--------");
	            log.info("---------- SME BACK END (REST SERVICES) UAT RELEASE ---------- VERSION 1.1.12 (Parameter Master MAP list load by Thread, Double TXN)---------- ON DATE 30 APR 2019 (19:30 PM)--------");
	            log.info("---------- SME-AMC+SME Dev BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 1.2.11 ---------- ON DATE 15 MAR 2021 pass customerid against alias id in RNB(sqlexception handle), LoanDetails, High Connection count handle in debit and authorized signatory + SME development BANK-Bank");
	            log.info("---------- SME-AMC BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 1.2.12 ---------- ON DATE 30 MAR 2021 Thread Stuck - set connection timeout to 1 min at API level and replace try with resource");
	            
	            log.info("---------- LIVE RELEASE ---------- VERSION 1.2.12 ---------- CURRENT PATH=/home/YESMSMEAPP/CREDENTEK/AMC/LIVE/30032021/SME_MOBILE_WL8.war");
	            log.info("---------- LIVE RELEASE ---------- VERSION 1.2.11 ---------- ROLLBACK PATH=/home/YESMSMEAPP/CREDENTEK/AMC/LIVE/15032021/SME_MOBILE_WL8.war");
	            
	            log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 17 FEB 2021 ( Production Release for dummy)--------");
	            log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 04 FEB 2021 ( YESUAT Release)--------");
	            log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 20 JAN 2021 ( Phase 6 Live release for android issue)--------");
	            log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 16 JAN 2021 ( Phase 6 Live release)--------");
	            log.info("---------- SME-AMC BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 17 NOV 2020 (finally handle for all rest service,token validation,jdbc connection counter and logs)--------");
	            log.info("---------- SME-AMC BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 1.2.10 ---------- ON DATE 27 OCT 2020 (Login with another device and destination account fetch(except FD acc), enable logs for no authorizer maintained issue)--------");
	            log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 1.2.9 ---------- ON DATE 20 OCT 2020 ( Live release)--------");
	            log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 1.2.9 ---------- ON DATE 11 SEP 2020 ( OBDX live release)--------");
	            log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 1.2.9 ---------- ON DATE 11 SEP 2020 ( Beneficairy mobile no insert NA after authorization Release and AMC)--------");
	            log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 1.2.5 ---------- ON DATE 26 JUL 2020 ( Changes for E-collect of query without trim )--------");
	            log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 1.2.7 ---------- ON DATE 19 JUL 2020 LIVE Release FOR PHASE 4 PART B --------");
	            log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 1.2.6 ---------- ON DATE 09 Jun 2020 LIVE Release API Modification --------");
	            log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 1.2.6 ---------- ON DAT(Date will disclose later) LIVE Release SME Prod Issue(Holiday,OWN ACCOUNT,IMPS) with AMC Release --------");
	            log.info("---------- DEPLOYED ON "+ "25-JAN-2018"  +"Incremental-----------");
	            //log.info("---------- Re-DEPLOYED ON "+ new Date() +"-----------");
				
				log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 1.2.6 ---------- ON DATE 13 May 2020 LIVE Release SME Prod Issue of OWN ACC,IMPS,Adhoc --------");
	            log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 1.2.6 ---------- ON DATE 07 Mar 2020 LIVE Release --------");
	            log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 1.2.5 (phase 3 NEFT 24*7 Transaction and Holiday Msg Resolved Bug updated)---------- ON DATE 20 DEC 2019 (12:15 PM)--------");
	            log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 1.2.2 (Live Phase 3 updated release)---------- ON DATE 12 NOV 2019 (14:30 PM)--------");
		        log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 1.2.2 (Live Phase 3 release)---------- ON DATE 14 OCT 2019 (13:30 PM)--------");
		        log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 1.2.1 (MCLR Display)---------- ON DATE 19062019 (13:00 PM)--------");
				log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 1.2.1 (SALARY and BULK FILE UPLOAD AND FD Check, Msg modified req by saurbha sir(TPIN and Password Expiry))---------- ON DATE 03-06-2019 --------");
				log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 1.2.0 (Online OD Patch)---------- ON DATE 09-04-2019 --------");
				log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 1.1.1 (Worklight 8, Customer Feedback, EEFC Account Changes and Third registration changes Changes in other performance Pre prod)---------- ON DATE 24 JAN 2018 --------");
				log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 1.1.1 (Worklight 8, Customer Feedback, EEFC Account Changes and Third registration changes)---------- ON DATE 21 DEC 2018 --------");
				log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 1.1.1 (For Live issue like iPhone x and Empty Object)---------- ON DATE 23 OCT 2018 --------");
				log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 1.1.2 (Email Functionality)---------- ON DATE 29 OCT 2018 --------");
				log.info("---------- SME BACK END (REST SERVICES) LIVE RELEASE ---------- VERSION 1.1.2 (Comapny Id Change In Fund Transfer API)---------- ON DATE 5 Dec 2018 --------");
				
				log.info("---------- SME APK (FRONT END ANDROID) UAT RELEASE ---------- VERSION 1.1 ---------- ON DATE 21 DEC 2018 --------");
				log.info("---------- SME IPA (FRONT END IOS) UAT RELEASE ---------- VERSION 1.1 ---------- ON DATE 21 DEC 2018 --------");
				log.info("---------- DEPLOYED ON "+ "24-JAN-2018"  +"Incremental-----------");
			//	log.info("---------- Re-DEPLOYED ON "+ new Date() +"-----------");
				
				
			} catch (Exception e) {
				log.info("Exception :::",e);
			}
		}

		public static DataSource getDataSource() {
			return dataSource;
		}
		public static DataSource getYesAmazonDataSource() {
			return yesAmazondataSource;
		}
		public static DataSource getBeneMsgSendDataSource() {
			return beneMsgSendDataSource;
		}
		
		public String getDatasourceName() {
			return datasourceName;
		}

		public void setDatasourceName(String datasourceName) {
			this.datasourceName = datasourceName;
		}
	

}
