package com.credentek.msme.maintenance.dao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.credentek.msme.ApplicationGenParm.ApplicationGenParameter;
import com.credentek.msme.database.DaoBase;
import com.credentek.msme.utils.GeneralService;
import com.credentek.msme.utils.Utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class FRM_Integration extends DaoBase {


	static final Logger log = LogManager.getLogger(FRM_Integration.class);
	public static String SERVICE_TYPE = ApplicationGenParameter.SERVICE_TYPE;
	
	public interface DeviceType  {
		String portal="PORTAL"; 
		String mobile="MOBILE"; 
	}
	
	
	
	private static  EntityManager entityManager;
	
	@Autowired
	public FRM_Integration(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	

	@Autowired
	private GeneralService generalService;
	
	
	
	 public static void requestEnqueueAPI(net.sf.json.JSONObject loginInfo, String succ_fail_flg, String login_id_status) {

	        try {
	            log.info("FRM Status" + loginInfo.toString());

	            if ("Y".equals(ApplicationGenParameter.FRMSTATUS)) {
	                String emptyVal = "";
	                String eventID = "IBL_MDS-" + getPartRefNum();
	                JSONObject reqBody = new JSONObject();
	                JSONObject msgBody = new JSONObject();
	                UserLoginDaoImpl userLoginDaoImpl = new UserLoginDaoImpl(entityManager);

	                String addr_net_local = loginInfo.has("ipAddress") ? loginInfo.getString("ipAddress") : emptyVal;
	                if (addr_net_local != null && addr_net_local.equals("NA")) {
	                    addr_net_local = emptyVal;
	                }

	                reqBody.put("host_id", "F");
	                reqBody.put("source", "MSME");
	                reqBody.put("keys", loginInfo.getString("loginID"));
	                reqBody.put("system_user_id", ApplicationGenParameter.SYSTEM_USER_ID);
	                reqBody.put("sys_time", getCurrentTimeStampDateFirstNew());
	                reqBody.put("event_id", eventID);
	                reqBody.put("user_id", loginInfo.getString("loginID"));
	                reqBody.put("cust_id", loginInfo.getString("loginID"));
	                reqBody.put("addr_network", emptyVal);
	                reqBody.put("addr_net_local", addr_net_local);
	                reqBody.put("ip_country", emptyVal);
	                reqBody.put("ip_city", emptyVal);
	                reqBody.put("succ_fail_flg", succ_fail_flg);
	                reqBody.put("error_code", emptyVal);
	                reqBody.put("error_desc", emptyVal);
	                reqBody.put("2fa_status", emptyVal);
	                reqBody.put("2fa_mode", emptyVal);
	                reqBody.put("device_type", loginInfo.getString("device_type"));
	                reqBody.put("device_id", loginInfo.getString("device_id"));

	                if (loginInfo.has("device_type") && loginInfo.getString("device_type").equals("Desktop")) {
	                    loginInfo.put("platform", "Portal");
	                    loginInfo.put("header", "");

	                    reqBody.put("browser_lang", loginInfo.getString("browser_lang"));
	                    reqBody.put("browser_time_zone", loginInfo.getString("browser_time_zone"));
	                    reqBody.put("browser_time_zone_hour", loginInfo.getString("browser_time_zone_hour"));
	                    reqBody.put("browser_name", loginInfo.getString("browser_name"));
	                    reqBody.put("user_agent", loginInfo.getString("user_agent"));
	                    reqBody.put("cookie_date_time_stamp", getNewDateFormatFromStringDate5(loginInfo.getString("cookie_date_time_stamp")));
	                    reqBody.put("session_id", loginInfo.getString("session_id"));
	                    reqBody.put("screen_res", loginInfo.getString("screen_res"));

	                    reqBody.put("device_bound_flag", emptyVal);
	                    reqBody.put("device_id_date", getNewDateFormatFromStringDate5(loginInfo.getString("device_id_date")));
	                    reqBody.put("first_login", userLoginDaoImpl.getFirstLogin("SMEMCS005", loginInfo.getString("loginID")));
	                } else {
	                    loginInfo.put("platform", "MOBILE");
	                    loginInfo.put("header", "");

	                    reqBody.put("browser_lang", emptyVal);
	                    reqBody.put("browser_time_zone", emptyVal);
	                    reqBody.put("browser_time_zone_hour", emptyVal);
	                    reqBody.put("browser_name", emptyVal);
	                    reqBody.put("user_agent", emptyVal);
	                    reqBody.put("cookie_date_time_stamp", emptyVal);
	                    reqBody.put("session_id", emptyVal);
	                    reqBody.put("screen_res", emptyVal);

	                    reqBody.put("device_bound_flag", 1);
	                    reqBody.put("device_id_date", getNewDateFormatFromStringDate6(userLoginDaoImpl.getDeviceIdDate(loginInfo.getString("loginID"))));
	                    reqBody.put("first_login", userLoginDaoImpl.getFirstLogin("SMEMCS002", loginInfo.getString("loginID")));
	                }

	                reqBody.put("java_status", emptyVal);
	                reqBody.put("js_status", emptyVal);
	                reqBody.put("mobileOS", loginInfo.getString("mobileOS"));
	                reqBody.put("mobileEmulator", emptyVal);
	                reqBody.put("user_loan_only_flag", "N");
	                reqBody.put("user_cc_only_flag", "N");
	                reqBody.put("login_id_status", login_id_status);
	                reqBody.put("cust_segment", emptyVal);
	                reqBody.put("device_id_count", emptyVal);
	                reqBody.put("dist_bt_curr_last_used_ip", emptyVal);
	                reqBody.put("time_slot", emptyVal);
	                reqBody.put("risk_band", emptyVal);
	                reqBody.put("system_password", emptyVal);
	                reqBody.put("eventts", getCurrentTimeStampDateFirstNew());

	                msgBody.put("msgBody", reqBody);
	                msgBody.put("source", "MSME");
	                msgBody.put("eventts", getCurrentTimeStampDateFirstNew());
	                msgBody.put("keys", loginInfo.getString("loginID"));
	                msgBody.put("eventtype", "nft");
	                msgBody.put("eventsubtype", "iblogin");
	                msgBody.put("event-name", "nft_iblogin");
	                msgBody.put("event_id", eventID);

	                // API Audit: Request
	                String SRN = "";
	                try {
	                    loginInfo.put("CustomerId", loginInfo.getString("loginID"));
	                    SRN = new GeneralService(entityManager).logApiDtl(msgBody.toString(), "FRMENQUEURL", loginInfo, "I");
	                } catch (Exception e1) {
	                    log.info("request=" + e1);
	                }

	                // Call the service
	                Utils.DataFromServiceFrm(ApplicationGenParameter.FRMENQUEURL, eventID, msgBody.toString());

	                // API Audit: Response
	                new GeneralService(entityManager).logApiDtl("true", "FRMENQUEURL", loginInfo, SRN + "~U");
	            } else {
	                log.info("FRM Status" + ApplicationGenParameter.FRMSTATUS);
	            }
	        } catch (Exception e) {
	            log.info("Exception in getReqRefNum() - - - ", e);
	        }
	    }
	
	
	public static String requestRD_API(net.sf.json.JSONObject parJson,String login_id_status) {
		JSONObject msgBody = new JSONObject();
		String rdResp="";
		try {
			log.info("FRM Status" + parJson.toString());
			
			if("Y".equals(ApplicationGenParameter.FRMSTATUS)){
				
				String emptyVal="";
				String eventID= "IBL_RDA_MDS-"+getPartRefNum();
				UserLoginDaoImpl userLoginDaoImpl= new UserLoginDaoImpl(entityManager);
				JSONObject reqBody = new JSONObject();
				String addr_net_local = parJson.has("ipAddress")?parJson.getString("ipAddress"):emptyVal;
				if(addr_net_local!=null && addr_net_local.equals("NA")){
					addr_net_local = emptyVal;
				}
				
				reqBody.put("host_id", "F");
				reqBody.put("source","MSME");
				reqBody.put("keys", parJson.getString("loginID"));
				reqBody.put("system_user_id",ApplicationGenParameter.SYSTEM_USER_ID );// value fetch from genral paramter(Decide after YBL call)
				reqBody.put("sys_time",getCurrentTimeStampDateFirstNew());
				reqBody.put("event_id", eventID);
				reqBody.put("user_id",  parJson.getString("loginID"));
				reqBody.put("cust_id",  parJson.getString("loginID"));
				reqBody.put("addr_network", emptyVal);
				reqBody.put("addr_net_local",addr_net_local);
				reqBody.put("ip_country",emptyVal );
				reqBody.put("ip_city", emptyVal);
				reqBody.put("succ_fail_flg", emptyVal);//(create a method )
				reqBody.put("error_code",emptyVal);
				reqBody.put("error_desc", emptyVal);
				reqBody.put("2fa_status", emptyVal);
				reqBody.put("2fa_mode", emptyVal);
				reqBody.put("device_type", parJson.getString("device_type"));
				reqBody.put("device_id",   parJson.getString("device_id"));
				
				if(parJson.has("device_type") && parJson.getString("device_type").equals("Desktop")) {
					parJson.put("platform", "Portal");
					parJson.put("header","");
					
					reqBody.put("browser_lang", parJson.getString("browser_lang"));	
					reqBody.put("browser_time_zone",  parJson.getString("browser_time_zone"));
					reqBody.put("browser_time_zone_hour", parJson.getString("browser_time_zone_hour"));
					reqBody.put("browser_name", parJson.getString("browser_name"));
					reqBody.put("user_agent",parJson.getString("user_agent"));
					reqBody.put("cookie_date_time_stamp",getNewDateFormatFromStringDate5(parJson.getString("cookie_date_time_stamp")));
					reqBody.put("session_id", parJson.getString("session_id"));//Mobile - Null,Portal - Browser session id. need discuss
					reqBody.put("screen_res", parJson.getString("screen_res"));
					
					reqBody.put("device_bound_flag",emptyVal);
					reqBody.put("device_id_date",  getNewDateFormatFromStringDate5(parJson.getString("device_id_date")));//Mobile - Device binding date.Portal - Cookie creation date. need to be discuss
					reqBody.put("first_login", userLoginDaoImpl.getFirstLogin("SMEMCS005",parJson.getString("loginID")));//Y For new user (Registration).     N For old user.
				}
				else {
					parJson.put("platform", "MOBILE");
					parJson.put("header","");
					
					reqBody.put("browser_lang", emptyVal);	
					reqBody.put("browser_time_zone",  emptyVal);
					reqBody.put("browser_time_zone_hour", emptyVal);
					reqBody.put("browser_name", emptyVal);
					reqBody.put("user_agent",emptyVal);
					reqBody.put("cookie_date_time_stamp",emptyVal);
					reqBody.put("session_id", emptyVal);
					reqBody.put("screen_res", emptyVal);
					
					reqBody.put("device_bound_flag",1);//Mobile - 1 Portal - Null  need discuss(create method this)
					reqBody.put("device_id_date",  getNewDateFormatFromStringDate6(userLoginDaoImpl.getDeviceIdDate(parJson.getString("loginID"))));//Mobile - Device binding date.Portal - Cookie creation date. need to be discuss
					reqBody.put("first_login", userLoginDaoImpl.getFirstLogin("SMEMCS002",parJson.getString("loginID")));//Y - For new user (Registration).N - For old user.
				
				}
				
				reqBody.put("java_status",emptyVal);
				reqBody.put("js_status", emptyVal);
				reqBody.put("mobileOS",parJson.getString("mobileOS"));//Device OS need discuss
				reqBody.put("mobileEmulator", emptyVal);
				reqBody.put("user_loan_only_flag","N");	
				reqBody.put("user_cc_only_flag", "N");
				reqBody.put("login_id_status",login_id_status);//Current login status.  ACTIVE/LOCKED (create method for this )
				reqBody.put("cust_segment", emptyVal);//NULL
				reqBody.put("device_id_count",emptyVal);
				reqBody.put("dist_bt_curr_last_used_ip",emptyVal );//Current login status.  need discuss
				reqBody.put("time_slot",emptyVal);//null
				reqBody.put("risk_band",emptyVal );//null
				reqBody.put("system_password", emptyVal); // Static or Null Need to be discuss with YBL
				reqBody.put("eventts", getCurrentTimeStampDateFirstNew());//Same as system_time
	
				msgBody.put("msgBody",reqBody);
				
				msgBody.put("source", "MSME");
				msgBody.put("eventts", getCurrentTimeStampDateFirstNew());
				msgBody.put("keys", parJson.getString("loginID"));
				msgBody.put("eventtype", "nft");
				msgBody.put("eventsubtype", "iblogin");
				msgBody.put("event-name","nft_iblogin");
				msgBody.put("event_id",eventID);//use a method here
				
				if (SERVICE_TYPE.equals("true")) {
					
					//--------------------API Audit--------Request------------------------
					String SRN ="";
					try {
						log.info("msgBody="+msgBody);
						parJson.put("CustomerId",parJson.getString("loginID") );
						SRN = new GeneralService(entityManager).logApiDtl(msgBody.toString(),"FRMRDURL",parJson,"I");
					} catch (Exception e1) {
						log.info("request="+e1);
					}
					//--------------------API Audit--------Request------------------------
					
					rdResp = Utils.DataFromServiceFrm(ApplicationGenParameter.FRMRDURL,eventID,msgBody.toString());
					
					//--------------------API Audit--------Response------------------------
					new GeneralService(entityManager).logApiDtl(rdResp,"FRMRDURL",parJson,SRN+"~U");
					//--------------------API Audit--------Response------------------------
					
				}
				else {
					
					//--------------------API Audit--------Request------------------------
					String SRN ="";
					try {
						parJson.put("CustomerId",parJson.getString("loginID") );
						SRN = new GeneralService(entityManager).logApiDtl(msgBody.toString(),"FRMRDURL",parJson,"I");
					} catch (Exception e1) {
						log.info("request="+e1);
					}
					//--------------------API Audit--------Request------------------------
					
					log.info("Frm RD...Url.........."+ApplicationGenParameter.FRMRDURL+"   eventID = "+eventID+".....Request......"+msgBody);
					rdResp = "allow|D|S003_DEVICE_ID_TOPX|IBL_RDA_5236666262636367";
					
					//--------------------API Audit--------Response------------------------
					new GeneralService(entityManager).logApiDtl(rdResp,"FRMRDURL",parJson,SRN+"~U");
					//--------------------API Audit--------Response------------------------
				}
				
				rdResp = rdResp.split("\\|")[1];
				
			}
			else {
				log.info("FRM Status" + ApplicationGenParameter.FRMSTATUS);
			}
		} catch (Exception e) {
			e.printStackTrace();
			 log.info("Exception in getReqRefNum() - - - " , e);
		}
		return rdResp;
		
		
	}
	
	public static String getCurrentTimeStampDateFirstNew() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
		return sdf.format(new Date());
	}
		
	public static String getPartRefNum() {

		String reqRefNo = "";
		try {
			Calendar calendar = Calendar.getInstance();
			Random randomObj = new Random();
			calendar.setTime(new Date());
			
			reqRefNo = String.valueOf(calendar.getTimeInMillis());
			String randNum1 = String.valueOf(randomObj.nextInt(10));
			
			reqRefNo = reqRefNo + randNum1;

		} catch (Exception e) {
			 log.info("Exception in getReqRefNum() - - - " + e);
		}
		return reqRefNo;
	}
	
	
	public static String getNewDateFormatFromStringDate5(String strDate)
    {
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEEE MMM dd yyyy HH:mm:ss 'GMT'z");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
        Date date = null;
        try {
            date = inputFormat.parse(strDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        String formattedDate = outputFormat.format(date);
        return formattedDate;
    }
	
	

	public static String getNewDateFormatFromStringDate6(String strDate)
    {
		log.info("strDate= "+strDate);
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
        Date date = null;
        try {
            date = inputFormat.parse(strDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        String formattedDate = outputFormat.format(date);
        log.info("formattedDate= "+formattedDate);
        return formattedDate;
    }
	
	
	
	
	
	
	
	
}
