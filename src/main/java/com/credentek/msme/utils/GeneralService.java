package com.credentek.msme.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.text.DateFormat;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.credentek.msme.ApplicationGenParm.ApplicationGenParameter;
import com.credentek.msme.database.DaoBase;
import com.credentek.msme.logindao.LoginDaoImpl;
import com.credentek.msme.loginmodel.SmeUserInterfaceServiceDtl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import jakarta.websocket.Session;
import net.sf.json.JSONObject;

@Service
public class GeneralService extends DaoBase{
	
	
	private static final Logger log = LogManager.getLogger(GeneralService.class);

	//Connection connection=null;
	//PreparedStatement pstmt;
	//ResultSet rset;
	 @PersistenceContext
	private  EntityManager entityManager;
	


	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	public GeneralService(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	
	 public String getShareKey(String token) {
	        String IMEI_NO = "";
	        String shareKey = "";

	        try {
	            // Query to get DEVICE_IMEI based on TOKEN
	            String sql = "SELECT DEVICE_IMEI FROM SME_APP_LOGIN_INFO WHERE TOKEN = :token";
	            Query query = entityManager.createNativeQuery(sql);
	            query.setParameter("token", token);
	            
	            Object result = query.getSingleResult();
	            if (result != null) {
	                IMEI_NO = (String) result;
	            }

	            // Only proceed if IMEI_NO is not empty
	            if (IMEI_NO != null && !IMEI_NO.isEmpty()) {
	                // Query to get PUB_KEY based on IMEI_NO
	                sql = "SELECT PUB_KEY FROM SME_IMEI_PUBKEY_ENTRY WHERE IMEI_NO = :imeiNo";
	                query = entityManager.createNativeQuery(sql);
	                query.setParameter("imeiNo", IMEI_NO);
	                
	                result = query.getSingleResult();
	                if (result != null) {
	                    shareKey = (String) result;
	                }
	            }

	            return shareKey;
	        } catch (Exception e) {
	            log.info("Exception ===", e);
	            return null;
	        }
	    }
	 
	public String getShareKeyTmp(String token) {
		String shareKey = "";
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
			pstmt = connection.prepareStatement(getQuery("GET_IMEI_TMP"));
			pstmt.setString(1, token);
			rset = pstmt.executeQuery();
			if (rset.next()) {
				shareKey = rset.getString(1);
			}
			rset.close();
			pstmt.close();
			
			return shareKey;
		} catch (Exception e) {
			log.info("Exception ===", e);
			return null;
		} finally {

			try {
				if (rset != null) {
					rset.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection!=null) {
					closeConnection(connection,new Object(){}.getClass().getEnclosingMethod().getName().toString());
				}
			} catch (SQLException e) {
				log.info("SQLException ===", e);
			}
		}

	}

	public String getShareKeyAtLogIn(String imei) {
		String shareKey = "";
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			//assign conn in try 2299 - 22032021
			connection=getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
			pstmt = connection.prepareStatement(getQuery("GET_KEY"));
			pstmt.setString(1, imei);
			rset = pstmt.executeQuery();
			if (rset.next()) {
				shareKey = rset.getString(1);
			}
			rset.close();
			pstmt.close();

			return shareKey;
		} catch (Exception e) {
			log.info("Exception ===", e);
			return null;
		} finally {

			try {
				if (rset != null) {
					rset.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection!=null) {
					closeConnection(connection,new Object(){}.getClass().getEnclosingMethod().getName().toString());
				}
			} catch (SQLException e) {
				log.info("SQLException ===", e);
			}
		}

	}

	public String getTokenAtLogIn(String imei) {
		String token = "";
		String objRefernce = this.toString();
		StringBuffer bufferToken = new StringBuffer(
				objRefernce.substring(objRefernce.indexOf("@") + 1, objRefernce.length()));
		token = bufferToken.toString();
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
			pstmt = connection.prepareStatement(getQuery("UPDATE_TOKEN"));
			pstmt.setString(1, token);
			pstmt.setString(2, imei);
			int count = pstmt.executeUpdate();
			pstmt.close();
			if (count > 0) {
				return token;
			} else {
				return "";
			}

		} catch (Exception e) {
			log.info("Exception ===", e);
			return null;
		} finally {

			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection!=null) {
					closeConnection(connection,new Object(){}.getClass().getEnclosingMethod().getName().toString());
				}
			} catch (SQLException e) {
				log.info("SQLException ===", e);
			}
		}

	}
	@Transactional
	public void insertKeyForIMEI(String imei, String key, String token) {
	    EntityManager localEntityManager = null;

	    try {
	        // Get the EntityManager instance
	        localEntityManager = entityManagerFactory.createEntityManager();

	        // DELETE operation
	        Query deleteNativeQuery = localEntityManager.createQuery("DELETE FROM SmeImeiPubkeyEntryTmp WHERE imeiNo = :imei");
	        deleteNativeQuery.setParameter("imei", imei);
	        localEntityManager.getTransaction().begin();
	        deleteNativeQuery.executeUpdate();

	        // INSERT operation
	        Query insertNativeQuery = localEntityManager.createQuery("INSERT INTO SmeImeiPubkeyEntryTmp (imeiNo, pubKey, token) VALUES (:imei, :key, :token)");
	        insertNativeQuery.setParameter("imei", imei);
	        insertNativeQuery.setParameter("key", key);
	        insertNativeQuery.setParameter("token", token);
	        insertNativeQuery.executeUpdate();
	        localEntityManager.getTransaction().commit();
	    } catch (Exception e) {
	        if (localEntityManager != null && localEntityManager.getTransaction().isActive()) {
	            localEntityManager.getTransaction().rollback();
	        }
	        log.error("Exception occurred:", e);
	    } finally {
	        if (localEntityManager != null) {
	            localEntityManager.close();
	        }
	    }
	}
	
	@Transactional
	public boolean checkHoliday() {
	    boolean flag = false;
	    try {
	        // Check if a holiday exists today
	        String holidayExistQuery = "SELECT COUNT(*) FROM SfmsNhlNodeHolidayLst WHERE nhlDate = current_date";
	        long count = entityManager.createQuery(holidayExistQuery, Long.class).getSingleResult();
	        if (count > 0) {
	            flag = true;
	        }

	        // Fetch maker alias mappings and store them in ApplicationGenParameter.makerAliasMapList
	        Query query = entityManager.createQuery("SELECT sMeMakerCd, makerAliasName FROM ComMakerMst WHERE activeFlag = 'Y'");
	        List<Object[]> results = query.getResultList();
	        for (Object[] result : results) {
	            ApplicationGenParameter.makerAliasMapList.put((String) result[1], (String) result[0]);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        // log.error("Exception occurred in checkHoliday", e);
	        throw new RuntimeException("Exception occurred in checkHoliday", e);
	    }
	    return flag;
	}


	@Transactional
	public boolean checkPassExpiry(String loginId, String type) {
	    log.info("In checkPassExpiry function =");
	    boolean flag = false;
	    int dateDiff = 0;

	    try {
	        // Determine the SQL query based on the type
	        String sql;
	        if ("Portal".equals(type)) {
	            sql = "SELECT remainDateForExpireP AS DATE_DIFF FROM SmeRegistrationDateDtl WHERE regId = :regId";
	        } else if ("Mobile".equals(type)) {
	            sql = "SELECT remainDateForExpireM  AS DATE_DIFF FROM SmeRegistrationDateDtl WHERE regId = :regId";
	        } else {
	            throw new IllegalArgumentException("Invalid type: " + type);
	        }

	        // Create and execute the native query
	        Query query = entityManager.createQuery(sql);
	        query.setParameter("regId", loginId.trim());

	        // Execute the query and get the result
	        Object result = query.getSingleResult();
	        if (result != null) {
	            // Assuming the result is of type java.sql.Date or java.util.Date
	            java.util.Date expireDate = (java.util.Date) result;
	            java.util.Date currentDate = new java.util.Date();
	            
	            // Calculate the difference in days
	            long diffInMillis = expireDate.getTime() - currentDate.getTime();
	            dateDiff = (int) (diffInMillis / (1000 * 60 * 60 * 24));
	        }
//	        if (result != null) {
//	            dateDiff = ((Number) result).intValue();
//	        }

	        log.info("Registration date difference ::: " + dateDiff);

	        // Check if dateDiff is within the expiry threshold
	        int passwordExpiryDays = Integer.parseInt(ApplicationGenParameter.PASSWORD_EXPIRY_DAYS);
	        if (!(dateDiff <= passwordExpiryDays && dateDiff >= 0)) {
	            flag = true;
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        log.error("Exception occurred in checkPassExpiry", e);
	    }

	    log.info("flag =" + flag);
	    return flag;
	}


	// Set Alias Id of Customer Id at time of expiry password at portal side on 03122018 (Prabhat)
	 @Transactional
	 public String getAliasId(String loginId) {
	     String aliasId = "NA";
	     try {
	         // Query to fetch the maximum aliasId
	         String queryStr = "SELECT COALESCE(MAX(a.aliasId), 'NA') FROM RnbidAliasIdMapping a WHERE a.customerId = :loginId";
	         
	         // Create and execute the query
	         Query query = entityManager.createQuery(queryStr);
	         query.setParameter("loginId", loginId.trim().toUpperCase());
	         
	         // Get the result
	         Object result = query.getSingleResult();
	         if (result != null) {
	             aliasId = result.toString();
	         }
	     } catch (Exception e) {
	         // Log and handle the exception
	         log.error("Exception occurred in getAliasId", e);
	         throw new RuntimeException("Exception occurred in getAliasId", e);
	     }
	     return aliasId;
	 }

	
	public void updateKeyForIMEI(String imei, String key, String token) {
		log.info("In updateKeyForIMEI function");
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
			// UPDATE_PUBKEY=update SME_IMEI_PUBKEY_ENTRY set PUB_KEY = ? where
			// IMEI_NO = ?
			pstmt = connection.prepareStatement(getQuery("UPDATE_PUBKEY"));
			pstmt.setString(1, key);
			pstmt.setString(2, imei);
			pstmt.executeUpdate();
			pstmt.close();

			// UPDATE_TOKEN=UPDATE SME_APP_LOGIN_INFO SET TOKEN = ? WHERE
			// DEVICE_IMEI = ?
			pstmt = connection.prepareStatement(getQuery("UPDATE_TOKEN"));
			pstmt.setString(1, token);
			pstmt.setString(2, imei);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e) {
			log.info("Exception ===", e);
		} finally {

			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection!=null ) {
					closeConnection(connection,new Object(){}.getClass().getEnclosingMethod().getName().toString());
				}
			} catch (SQLException e) {
				log.info("SQLException ===", e);
			}
		}

	}

	//Validate OTP Generation allowed only after 2 minutes
	public boolean validate_OTP_Generation( String customerId,String serviceId){

		boolean validOTP = true;
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		int otpReGenerationSec = 120;// %Sec

		String otpRegeTime = ApplicationGenParameter.OTP_REGENERATION_TIME;
		if (otpRegeTime != null && !"".equals(otpRegeTime) && !"NA".equals(otpRegeTime) ) { 
			otpReGenerationSec = Integer.parseInt(otpRegeTime);
		}

		log.info("validate_OTP_Generation :::" + " customerId :::" + customerId+" serviceId :::" + serviceId+" otpReGenerationSec :::" + otpReGenerationSec);

		java.util.Date reqDate = null;
		Date lastOTP_date = null;
		Date lastOTP_verfiy_date = null;
		String strLastOTP_generate_date = "";
		String strReqOTP_generate_date = "";
		String strLastOTP_Verify_date = "";
		int diffsec = 0;
		
		// Previous Service call date time
		DateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		try {
			connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
			pstmt = connection.prepareStatement(getQuery("LAST_SERVICE_CALL"));
			pstmt.setString(1, customerId);
			pstmt.setString(2, serviceId);
			rset = pstmt.executeQuery();
			if (rset.next()) {
				strLastOTP_generate_date = rset.getString(1);
				strReqOTP_generate_date = rset.getString(2);
			}
			rset.close();
			pstmt.close();
			
			pstmt = connection.prepareStatement(getQuery("LAST_SERVICE_CALL"));
			pstmt.setString(1, customerId);
			pstmt.setString(2, "MCSC0003");
			rset = pstmt.executeQuery();
			if (rset.next()) {
				strLastOTP_Verify_date = rset.getString(1);
			}
			rset.close();
			pstmt.close();

			// if OTP Successful verification done of Last OTP then allow new OTP generation within 2 minutes
			if(strLastOTP_Verify_date!=null && !strLastOTP_Verify_date.equals("") &&  strLastOTP_generate_date!=null && !strLastOTP_generate_date.equals("")) {
				lastOTP_verfiy_date = (Date) sdf.parse(strLastOTP_Verify_date);
				lastOTP_date = (Date) sdf.parse(strLastOTP_generate_date);
				
				if(lastOTP_verfiy_date.after(lastOTP_date)) {
					log.info("strLastOTP_generate_date="+strLastOTP_generate_date+" lastOTP_date ===" + lastOTP_date+" lastOTP_verfiy_date ===" + lastOTP_verfiy_date+" strLastOTP_Verify_date ====" );
					return true;
				}
				
			}
			
			//Validate OTP Generation allowed only after 2 minutes
			if (strLastOTP_generate_date!=null && !strLastOTP_generate_date.equals("") && strReqOTP_generate_date!=null &&  !strReqOTP_generate_date.equals("")) {
				
				lastOTP_date = (Date) sdf.parse(strLastOTP_generate_date);
				reqDate = sdf.parse(strReqOTP_generate_date);

				long prev_t = lastOTP_date.getTime();
				long cur_t1 = reqDate.getTime();
				
				long diffMilliSec = cur_t1 - prev_t;
				
				diffsec = (int) (diffMilliSec / 1000.0);
				
				if (diffsec>0 && diffsec < otpReGenerationSec) { 
					validOTP  = false;
				}
			}
			
			log.info("strLastOTP_generate_date="+strLastOTP_generate_date+" lastOTP_date ===" + lastOTP_date+" Current reqDate ===" + reqDate+" diffsec ====" + diffsec+" validOTP="+validOTP);
			
			 
		} catch (Exception e) {
			log.info("Exception ===", e);
		}finally{
			try {
				if(rset!=null) {
					rset.close(); 
				}
				if(pstmt!=null) {
					pstmt.close();
				}
				if(connection!=null) {
					closeConnection(connection,new Object(){}.getClass().getEnclosingMethod().getName().toString());
				}
			}catch (SQLException e1) {
				log.info("SQLException ===", e1);		
				}
		}
		return validOTP; 
	}
	
	public boolean sessionTimeOut(String loginID) {
		log.info("sessionTimeOut1 :::" + "loginId :::" + loginID);
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		int g_time_out_period = 15;// %min

		String l_sessionTimeout = ApplicationGenParameter.SESSION_TIME;
		if (l_sessionTimeout != null && !"".equals(l_sessionTimeout)) {
			g_time_out_period = Integer.parseInt(l_sessionTimeout);
		}

		log.info("g_time_out_period :::" + g_time_out_period);

		Date preReqTime = null;
		String s_date = "";
		int diffSeconds = 0;
		int sessionExit = 0;
		Date reqTime = new Date();
		// Previous Request date time
		DateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		try {
			//assign conn in try 2299 - 22032021
			connection = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
			pstmt = connection.prepareStatement(getQuery("SELECT_REQ_TIME"));
			pstmt.setString(1, loginID);
			rset = pstmt.executeQuery();
			if (rset.next()) {
				s_date = rset.getString(1);
			}
			rset.close();
			pstmt.close();

			if (!s_date.equals("") && !s_date.equals("null")) {
				preReqTime = (Date) sdf.parse(s_date);

				log.info("preReqTime ===" + preReqTime);
				log.info("reqTime ===" + reqTime);

				long prev_t = preReqTime.getTime();
				long prev_d = preReqTime.getDate();
				long prev_m = preReqTime.getMonth();
				long prev_y = preReqTime.getYear();
				log.info("d :::" + prev_d);
				log.info("d prev_m :::" + prev_m);
				log.info("d prev_y :::" + prev_y);
				// Current Request date and time
				long cur_d1 = reqTime.getDate();
				long cur_m1 = reqTime.getMonth();
				long cur_y1 = reqTime.getYear();
				long cur_t1 = reqTime.getTime();// 1483345842622 - 1483345972634
				log.info("d1 :::" + cur_d1);
				log.info("d cur_m1 :::" + cur_m1);
				log.info("d cur_y1 :::" + cur_y1);
				if (prev_d == cur_d1 && prev_m == cur_m1 && prev_y == cur_y1) {
					log.info(".............True.............." + prev_t + "-----------------------" + cur_t1);

					// long l1 = 1483345972634;
					// long l = 1483345972634L - 1483345842622L;
					long diffMilliSec = cur_t1 - prev_t;
					log.info("long data ===" + diffMilliSec);
					diffSeconds = (int) (diffMilliSec / 60000.0);
					log.info("Min ====" + diffSeconds);
				}

				if (diffSeconds > g_time_out_period) {
					log.info("if(diffSeconds > g_time_out_period)");
					// preReqTime = null;
					sessionExit = 0;
					//new UserLoginDaoImpl().logoutUser(loginID);
					//return false;
				} else {
					log.info("if(diffSeconds > g_time_out_period) else");
					if(prev_d != cur_d1)
					{
						sessionExit = 0;
					}
					else
					{
						// UPDATE_REQ_TIME=UPDATE SME_APP_LOGIN_INFO SET
						// LAST_REQUEST_TIME = sysdate WHERE LOGIN_ID = ?
						pstmt = connection.prepareStatement(getQuery("UPDATE_REQ_TIME"));
						// pstmt.setDate(1, java.sql.Date.valueOf(""+reqTime));
						pstmt.setString(1, loginID);
						pstmt.executeUpdate();
						pstmt.close();
						sessionExit = 1;
						return true;
					}
					
					
					
				}
			}

			
			 if(sessionExit == 0) {
				 new LoginDaoImpl(entityManager).logoutUser(loginID);
				 return false;
			 } 

		} catch (ParseException pe) {
			log.info("ParseException ===", pe);
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
				if (connection!=null) {
					closeConnection(connection,new Object(){}.getClass().getEnclosingMethod().getName().toString());
				}
			} catch (SQLException e) {
				log.info("SQLException ===", e);
			}
		}
		return false;

	}

	public boolean checkConnection() {
		boolean flag = false;
		Connection conn = null;

		try {
			conn = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
			if (conn != null) {
				flag = true;
			}
		} catch (Exception ex) {
			log.info("SQL Exception occured");
		} finally {
			if (conn != null) {
				flag = true;
					closeConnection(conn,new Object(){}.getClass().getEnclosingMethod().getName().toString());
			}
		}

		return flag;
	}


    @Transactional
    public boolean sessionValid(String loginID) {
        boolean sessionTOFlag = false;
        int g_time_out_period = 15;

        String l_sessionTimeout = ApplicationGenParameter.SESSION_TIME;
        if (l_sessionTimeout != null && !"".equals(l_sessionTimeout)) {
            g_time_out_period = Integer.parseInt(l_sessionTimeout);
        }

        Date preReqTime = null;
        String s_date = "";
        int diffSeconds = 0;
        Date reqTime = new Date();
        DateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

        try {
            Query selectQuery = entityManager.createNativeQuery("SELECT TO_CHAR(LAST_REQUEST_TIME, 'DD-MON-YYYY HH24:MI:SS') FROM SME_APP_LOGIN_INFO WHERE LOGIN_ID = ?");
            selectQuery.setParameter(1, loginID);
            s_date = (String) selectQuery.getSingleResult();

            if (!s_date.isEmpty() && !"null".equals(s_date)) {
                preReqTime = sdf.parse(s_date);
                long diffMilliSec = reqTime.getTime() - preReqTime.getTime();
                diffSeconds = (int) (diffMilliSec / 60000.0);
            }

            if (diffSeconds > g_time_out_period) {
                sessionTOFlag = true;
            }

        } catch (Exception e) {
            log.info("Exception ===", e);
        }
        return sessionTOFlag;
    }
		

		
		public String logApiDtl(String requestData,String serviceName,JSONObject customerInfo,String flag) {
			
			PreparedStatement pstmt= null;
			ResultSet rset = null;
			Integer SRN=0;
			Connection conn = null;
			try{
				log.info("Inside logApiDtl ----- "+customerInfo);
				
				boolean mFlag=false;
				if (customerInfo.containsKey("mFlag") && customerInfo.getString("mFlag").equals("true")) {
					mFlag = true;
				}
				
				conn = getConnection(new Object(){}.getClass().getEnclosingMethod().getName().toString());
				
				String companyID="";
				if(mFlag)
					companyID=	ApplicationGenParameter.companyCustomerMapList.get(customerInfo.getString("AuthId").trim());
				else
					companyID=	ApplicationGenParameter.companyCustomerMapList.get(customerInfo.getString("CustomerId").trim());
				
				
				
				if (flag.isEmpty()) {

					pstmt = conn.prepareStatement(getQuery("SELECT_SRN_IF_EMPTY"));
					pstmt.setString(1, customerInfo.getString("CustomerId").trim());
					rset = pstmt.executeQuery();
					if (rset.next()) {
						SRN = rset.getInt(1);
						flag = SRN + "~U";
					}
					rset.close();
					pstmt.close();
				}
				
				if("I".equals(flag)){

					pstmt =conn.prepareStatement(getQuery("SELECT_SEQ_FOR_API_DTL"));
					rset = pstmt.executeQuery();
					if (rset.next()) {
						SRN = rset.getInt(1);
					}
					rset.close();
					pstmt.close();
					

					pstmt = conn.prepareStatement(getQuery("INSERT_API_DTL"));
					pstmt.setString(1,SRN.toString());
					pstmt.setString(2,customerInfo.getString("CustomerId").trim());
					pstmt.setString(3,companyID);
					pstmt.setString(4,requestData.toString());
					pstmt.setString(5,serviceName);
					pstmt.setString(6,customerInfo.getString("ipAddress"));
					pstmt.setString(7,customerInfo.getString("platform"));
					pstmt.setString(8,customerInfo.getString("header"));
					int i = pstmt.executeUpdate();
					pstmt.close();
					log.info("logApiDtl inserted....."+i );
				}else{
					pstmt = conn.prepareStatement(getQuery("UPDATE_API_DTL"));
					pstmt.setString(1,requestData.toString());
					pstmt.setString(2,flag.split("~")[0]);
					pstmt.setString(3,customerInfo.getString("CustomerId").trim());
					int i = pstmt.executeUpdate();
					pstmt.close();

					log.info("logApiDtl updated....."+i );
				}
			}
			catch(Exception e) {
				log.info("logApiDtl updated.....",e);
				e.printStackTrace();
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
			log.info("logApiDtl SRN....."+SRN);
			return SRN.toString();
		}


		
	
		@Transactional
		public void addCustomerAuditDtl(JSONObject custJson) {
			EntityManager localEntityManager = null;
			try {
				// Create the EntityManager
				localEntityManager = entityManagerFactory.createEntityManager();
				localEntityManager.getTransaction().begin();

				Long nextSeqVal = null;

				// Determine the serviceId and perform appropriate operations
				if ("SMEMCS002".equals(custJson.getString("serviceId"))
						|| "SMEMCS005".equals(custJson.getString("serviceId"))) {

					// Get the next sequence value using native query
					Query sequenceQuery = localEntityManager.createNativeQuery("SELECT LOGIN_ADDED_SEQ.NEXTVAL FROM dual");
					nextSeqVal = ((Number) sequenceQuery.getSingleResult()).longValue();

					// Update SmeAppLoginInfo using JPQL
					String updateJpql = "UPDATE SmeAppLoginInfo s SET s.loginAddedId = :nextSeqVal WHERE s.loginId = :loginId";
					Query updateQuery = localEntityManager.createQuery(updateJpql);
					updateQuery.setParameter("nextSeqVal", nextSeqVal);
					updateQuery.setParameter("loginId", custJson.getString("customerId"));
					updateQuery.executeUpdate();
				}

				// Retrieve data from SmeAppLoginInfo
				String loginInfoJpql = "SELECT s.loginAddedId, s.userType FROM SmeAppLoginInfo s WHERE s.loginId = :loginId";
				Query loginInfoQuery = localEntityManager.createQuery(loginInfoJpql);
				loginInfoQuery.setParameter("loginId", custJson.getString("customerId"));
				Object[] loginInfoResult = (Object[]) loginInfoQuery.getSingleResult();
				long l_loginAddedId = ((Number) loginInfoResult[0]).longValue();
				String l_userType = (String) loginInfoResult[1];

				// Retrieve data from SmeCustomerIpAddressDtl
				String ipAddressJpql = "SELECT i.ipAddress, i.requestMode FROM SmeCustomerIpAddressDtl i WHERE i.customerIp = :customerIp";
				Query ipAddressQuery = localEntityManager.createQuery(ipAddressJpql);
				ipAddressQuery.setParameter("customerIp", custJson.getString("customerId"));
				List<Object[]> ipAddressResult = ipAddressQuery.getResultList();
				String l_ipAddress = null;
				String l_loginMode = null;

				for (Object[] tmpObj : ipAddressResult) {
					l_ipAddress = (String) tmpObj[0];
					l_loginMode = String.valueOf(tmpObj[1]);
				}

				// Retrieve or calculate NEXT_CHANGED_SEQ from SmeUserInterfaceServiceDtl
				int l_currentSeq = 0;
				if (!("SMEMCS002".equals(custJson.getString("serviceId"))
				        || "SMEMCS005".equals(custJson.getString("serviceId")))) {
				    String maxSeqJpql = "SELECT COALESCE(MAX(s.nextChangedSeq), 0) FROM SmeUserInterfaceServiceDtl s WHERE s.loginActiveId = :loginActiveId";
				    Query maxSeqQuery = localEntityManager.createQuery(maxSeqJpql);
				    maxSeqQuery.setParameter("loginActiveId", l_loginAddedId);

				    // Retrieve the result as Long
				    Long maxSeqResult = (Long) maxSeqQuery.getSingleResult();
				    l_currentSeq = maxSeqResult != null ? maxSeqResult.intValue() : 0;
				}

				// Prepare current timestamp in Java
				Instant now = Instant.now(); // or use java.sql.Timestamp if needed
				java.sql.Timestamp currentTimestamp = java.sql.Timestamp.from(now);

				// Insert into SmeUserInterfaceServiceDtl using JPQL
				String insertJpql = "INSERT INTO SmeUserInterfaceServiceDtl (nextChangedSeq, serviceName, loginType, loginViaFlag, errorExistFlag, ipAddress, loginActiveId, navigateDt, customerId) "
						+ "VALUES (:nextChangedSeq, :serviceName, :loginType, :loginViaFlag, :errorExistFlag, :ipAddress, :loginActiveId, :navigateDt, :customerId)";
				Query insertQuery = localEntityManager.createQuery(insertJpql);
				insertQuery.setParameter("nextChangedSeq", l_currentSeq + 1);
				insertQuery.setParameter("serviceName", custJson.getString("serviceId"));
				insertQuery.setParameter("loginType", l_userType);
				insertQuery.setParameter("loginViaFlag", l_loginMode);
				insertQuery.setParameter("errorExistFlag", "N");
				insertQuery.setParameter("ipAddress", l_ipAddress);
				insertQuery.setParameter("loginActiveId", l_loginAddedId);
				insertQuery.setParameter("navigateDt", currentTimestamp); // Use current timestamp
				insertQuery.setParameter("customerId", custJson.getString("customerId"));

				insertQuery.executeUpdate();
				localEntityManager.getTransaction().commit();
			} catch (Exception e) {
				// Handle exception properly
				e.printStackTrace();
				if (localEntityManager != null && localEntityManager.getTransaction().isActive()) {
					localEntityManager.getTransaction().rollback();
				}
				// Log error if you have a logger
			} finally {
				if (localEntityManager != null) {
					localEntityManager.close();
				}
			}
		}

}
