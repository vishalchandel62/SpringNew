package com.credentek.msme.logindao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.json.JsonException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.credentek.msme.ApplicationGenParm.ApplicationGenParameter;
import com.credentek.msme.api.dao.CustomerManagementDAO;
import com.credentek.msme.database.DaoBase;
import com.credentek.msme.loginmodel.AccountDetails;
import com.credentek.msme.loginmodel.AppLoginInfo;
import com.credentek.msme.loginmodel.SmeAppLoginInfo;
import com.credentek.msme.loginmodel.SmeAppLoginInfoLogInfo;
import com.credentek.msme.loginmodel.UserLogin;
import com.credentek.msme.maintenance.dao.FRM_Integration;
import com.credentek.msme.utils.GeneralService;
import com.credentek.msme.utils.PathRead;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import net.sf.json.JSONException;

@Repository
public class LoginDaoImpl extends DaoBase implements LoginDao {

    
	private final EntityManager entityManager;

	@Autowired
	public LoginDaoImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	private static final Logger log = Logger.getLogger(LoginDaoImpl.class.getName());
	private final SessionFactory sessionFactory = null;
	public static PathRead path = PathRead.getInstance();
	Connection connection = null;
	public JSONObject getAllAccDetails(AccountDetails acc_dtl) {
		JSONObject s2_header = new JSONObject();
		return s2_header;
	}

	@Transactional
	public String getCompanyId(String custId) {
		String companyId = null;

		try {
			// Execute the query using EntityManager
			companyId = entityManager.createQuery("select companyId from SmeRegUserInfo where custId =:custID")
					.setParameter("custID", custId).getSingleResult().toString();

			if (companyId == null || companyId.trim().isEmpty()) {
				// If companyId is null or empty, execute the CUSTOMER_COMPANY_ID query
				String modifiedQuery = "select distinct(codCust) from VwChAcctCustXref \" +\n"
						+ "                    \"where codAcctCustRel = 'SOW' AND codProd NOT IN (404) \" +\n"
						+ "                    \"and codAcctNo in (select codAcctNo from VwChAcctCustXref where codCust =:codCust)";
				companyId = entityManager.createQuery(modifiedQuery).setParameter("codCust", custId).getSingleResult()
						.toString();
			}

			// Store companyId in ApplicationGenParameter.companyCustomerMapList
			ApplicationGenParameter.companyCustomerMapList.put(custId, companyId);

		} catch (Exception e) {
			// Handle exception (log or rethrow)
			e.printStackTrace(); // Replace with proper logging
		}

		return companyId;
	}

	

	
// Other helper methods for handling specific queries or operations
	public void getLoginFailureCountWithSession(Session session, String loginID, int logOutCnt) {
		log.info("In getLoginFailureCount");

		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			// Fetch the AppLoginInfo entity
			AppLoginInfo appLoginInfo = session.get(AppLoginInfo.class, loginID.trim());

			if (appLoginInfo != null) {
				// Update entity fields
				appLoginInfo.setLastFailureAttemptTime(new Date()); // Assuming you set current timestamp
				appLoginInfo.setLoginCount(logOutCnt + 1);

				// Save or update the entity
				session.saveOrUpdate(appLoginInfo);

				log.info("Updated login failure count for loginID: " + loginID);
			} else {
				// log.warn("AppLoginInfo not found for loginID: " + loginID);
			}

			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			// log.error("Exception occurred: ", e);
		}
	}

	
	 @Transactional
	  public net.sf.json.JSONObject validCheckerForPortal(net.sf.json.JSONObject  loginInfo, UserLogin credential) {

		 net.sf.json.JSONObject  resmsg = new net.sf.json.JSONObject ();
		  EntityManager localEntityManager = entityManagerFactory.createEntityManager().unwrap(Session.class);
	        try {

	            int logOutCnt = 0;
	            String portalPass = "";
	            String userLoc = "";
	            String userblock = "";
	            String username = "";
	            String lastLogin = "";
	            boolean logMode = false;
	            String logOutMode = "";
	            String loginID = loginInfo.getString("loginID");
	           // SmeAppLoginInfo appLoginInfo = new SmeAppLoginInfo();
	            // Insert into SME_APP_LOGIN_INFO_LOG_INFO
	          //  SmeAppLoginInfoLogInfo smeAppLoginInfoLogInfo = new SmeAppLoginInfoLogInfo();
	            
	            Query loginInfoQuery = localEntityManager.createQuery(" FROM SmeAppLoginInfo WHERE loginId =:loginID");
	            loginInfoQuery.setParameter("loginID", loginID);

	            List<SmeAppLoginInfo> smeAppLoginInfoLogInfoList = loginInfoQuery.getResultList();
	         
	            // part II INSERT INTO SmeAppLoginInfoLogInfo  
	            
	            localEntityManager.getTransaction().begin();
	            for(SmeAppLoginInfo smeAppLoginInfo : smeAppLoginInfoLogInfoList) {
	            	
	            	SmeAppLoginInfoLogInfo smeAppLoginInfoLogInfo = new SmeAppLoginInfoLogInfo();
	            	
	            	smeAppLoginInfoLogInfo.setLoginId(smeAppLoginInfo.getLoginId());
	                smeAppLoginInfoLogInfo.setUsername(smeAppLoginInfo.getUsername());
	                smeAppLoginInfoLogInfo.setLoginDate(smeAppLoginInfo.getLoginDate());
	                smeAppLoginInfoLogInfo.setLogoutDate(smeAppLoginInfo.getLogoutDate());
	                smeAppLoginInfoLogInfo.setLogoutMode(smeAppLoginInfo.getLogoutMode());
	                smeAppLoginInfoLogInfo.setExceptionCd(smeAppLoginInfo.getExceptionCd());
	                smeAppLoginInfoLogInfo.setPassword(smeAppLoginInfo.getPassword());
	                smeAppLoginInfoLogInfo.setDeviceName(smeAppLoginInfo.getDeviceName());
	                smeAppLoginInfoLogInfo.setDevicePlatform(smeAppLoginInfo.getDevicePlatform());
	                smeAppLoginInfoLogInfo.setDeviceUuid(smeAppLoginInfo.getDeviceUuid());
	                smeAppLoginInfoLogInfo.setDeviceVersion(smeAppLoginInfo.getDeviceVersion());
	                smeAppLoginInfoLogInfo.setDeviceDate(smeAppLoginInfo.getDeviceDate());
	                smeAppLoginInfoLogInfo.setDeviceImei(smeAppLoginInfo.getDeviceImei());
	                smeAppLoginInfoLogInfo.setToken(smeAppLoginInfo.getToken());
	                smeAppLoginInfoLogInfo.setLoginName(smeAppLoginInfo.getLoginName());
	                smeAppLoginInfoLogInfo.setLastRequestTime(smeAppLoginInfo.getLastRequestTime());
	                smeAppLoginInfoLogInfo.setRegId(smeAppLoginInfo.getRegId());
	                smeAppLoginInfoLogInfo.setLoginCount(smeAppLoginInfo.getLoginCount());
	                smeAppLoginInfoLogInfo.setLastFailureAttemptTime(smeAppLoginInfo.getLastFailureAttemptTime());
	                smeAppLoginInfoLogInfo.setUserLocked(smeAppLoginInfo.getUserLocked());
	                smeAppLoginInfoLogInfo.setFirstLogin(smeAppLoginInfo.getFirstLogin());
	                smeAppLoginInfoLogInfo.setMpinCount(smeAppLoginInfo.getMpinCount());
	                smeAppLoginInfoLogInfo.setPortalPass(smeAppLoginInfo.getPortalPass());
	                smeAppLoginInfoLogInfo.setUserType(smeAppLoginInfo.getUserType());
	                smeAppLoginInfoLogInfo.setSessionId(smeAppLoginInfo.getSessionId());
	                smeAppLoginInfoLogInfo.setLoginTypeMobile(smeAppLoginInfo.getLoginTypeMobile());
	                smeAppLoginInfoLogInfo.setLoginTypePortal(smeAppLoginInfo.getLoginTypePortal());
	                smeAppLoginInfoLogInfo.setReqToken(smeAppLoginInfo.getReqToken());
	                smeAppLoginInfoLogInfo.setLoginAddedId(smeAppLoginInfo.getLoginAddedId());
	                smeAppLoginInfoLogInfo.setIsmdmsearchflag1(smeAppLoginInfo.getIsmdmsearchflag());
	                smeAppLoginInfoLogInfo.setUserBlocked(smeAppLoginInfo.getUserBlocked());
	                smeAppLoginInfoLogInfo.setNewEncFlag(smeAppLoginInfo.getNewEncFlag());
	                smeAppLoginInfoLogInfo.setNewEncDate(smeAppLoginInfo.getNewEncDate());
	                smeAppLoginInfoLogInfo.setWrongOtpAttempt(smeAppLoginInfo.getWrongOtpAttempt());
	                smeAppLoginInfoLogInfo.setWrongOtpAttemptDateTime(smeAppLoginInfo.getWrongOtpAttemptDateTime());
	            	
	            	localEntityManager.persist(smeAppLoginInfoLogInfo);
	            	
	            }
	        
	            //loginInfoQuery.executeUpdate();
	            localEntityManager.getTransaction().commit();
	            
	            Query selectQuery = entityManager.createQuery("SELECT logoutMode, loginCount, portalPass,userLocked, username, logoutDate, userLocked FROM SmeAppLoginInfo WHERE loginId = :loginID");
	            selectQuery.setParameter("loginID", loginID);

	            Object[] result = (Object[]) selectQuery.getSingleResult();

	            if (result != null) {
//	
	            	logMode = false;
					log.info(result[0] + "");
					logOutMode = result[0] == null ? "" : result[0].toString();
					logOutCnt = result[1] == null ? 0 : Integer.parseInt(result[1].toString());
					portalPass = result[2] == null ? "" : result[2].toString();
					userLoc = result[3] == null ? "" : result[3].toString();
					username = result[4] == null ? "" : result[4].toString();
					lastLogin = result[5] == null ? "" : result[4].toString();

					userblock = result[6] == null ? "" : result[4].toString();

				
	            } else {
	                resmsg.put("STATUS", "Error");
	                resmsg.put("localReg", "N");
	                resmsg.put("Portal_Pass", "NotValid");
	                resmsg.put("Message", "Invalid Credential.");
	                return resmsg;
	            }

	            insertIpAddress(connection,loginInfo.getString("loginID"), loginInfo.getString("ipAddress"), loginInfo.getString("requestFromFlag"));

	            if (logOutMode.equals("F")) {
	                GeneralService gnrlService = new GeneralService(entityManager);
	                boolean sessionExpTime = gnrlService.sessionValid(loginInfo.getString("loginID"));
	                if (sessionExpTime) {
	                    Query updateQuery = entityManager.createQuery("UPDATE SmeAppLoginInfo SET logoutMode = 'T' WHERE loginId = :loginID");
	                    updateQuery.setParameter("loginID",loginID);
	                    int qCount = updateQuery.executeUpdate();
	                    localEntityManager.getTransaction().commit();
	                    if (qCount > 0) {
	                        logMode = true;
	                    }
	                } else {
	                    resmsg.put("STATUS", "Error");
	                    resmsg.put("Portal_Pass", "NotValid");
	                    resmsg.put("Message", "User already logged In.");
	                }
	            }

	            if (logMode==false) {
	                selectQuery = entityManager.createQuery("SELECT logoutMode, portalPass FROM SmeAppLoginInfo WHERE loginId = :loginId");
	                selectQuery.setParameter("loginId",loginID);
	                result = (Object[]) selectQuery.getSingleResult();

	                if (result != null) {
	                	  logOutMode = (String) result[0];
	                    portalPass = (String.valueOf(result[1].toString()));
	                }
	            }

	            if (logOutMode.equals("T")){
	            	String serviceName = "MCSC0001";

		            if (portalPass != null && !portalPass.isEmpty()) {
		                resmsg.put("Portal_Pass", "Valid");
		                resmsg.put("Username", username);
		                resmsg.put("lastLogIn", lastLogin);
	                    Query selectUserDataQuery = entityManager.createQuery(
		                        "SELECT codCustId, namCustFull, refCustTelex, refCustEmail, ssc, serviceRm, assetRm,serviceRmEmailId, "
		                        + "serviceRmMob, assetRmMob, assetRmEmailId, assetRmName, serviceRmName FROM MV_Ci_CustmastAll"
		                        + " WHERE codCustId = :loginId" );
	                    selectUserDataQuery.setParameter("loginId", loginID);
	                    List<Object[]> userDataList = selectUserDataQuery.getResultList();
	                    
	                    if (userDataList != null && !userDataList.isEmpty()) {
	                    	Object[] userData = userDataList.get(0); // Assuming there is only one result

	                    	resmsg.put("CustName", (String) userData[1]);
	                    	resmsg.put("CustContact", (String) userData[2]);
	                    	resmsg.put("CustEmail", (String) userData[3]);
	                    	resmsg.put("ServRMID", (String) userData[5]);
	                    	resmsg.put("ServMobRM", (String) userData[8]);
	                    	resmsg.put("ServEmailRM", (String) userData[7]);
	                    	//resmsg.put("AssetRMID",   (String) userData[6]);
	                    	resmsg.put("AssetRMID",   String.valueOf( userData[6]));
	                    	resmsg.put("AssetMobRM", (String) userData[9]);
	                    	resmsg.put("AssetEmailRM", (String) userData[10]);
	                    	resmsg.put("AssetRmName", (String) userData[11]);
	                    	resmsg.put("ServiceRmName", (String) userData[12]);
	                    }

	                    if ("Y".equals(userblock)) {
	                        FRM_Integration.requestEnqueueAPI(loginInfo, "F", "L");
	                        resmsg.put("Portal_Pass", "NotValid");
	                        resmsg.put("STATUS", "Error");
	                        resmsg.put("Message", "User is blocked.");
	                    } else {
	                        if ("Y".equals(userLoc)) {
	                            FRM_Integration.requestEnqueueAPI(loginInfo, "F", "L");
	                            resmsg.put("Portal_Pass", "NotValid");
	                            resmsg.put("STATUS", "Error");
	                            resmsg.put("Message", "Account is locked due to maximum number of invalid attempts.");
	                        } else {
	                            if (!portalPass.equals(credential.getPassword())) {
	                                resmsg.put("Portal_Pass", "NotValid");
	                                if (logOutCnt < 5) {
	                                    FRM_Integration.requestEnqueueAPI(loginInfo, "F", "A");
	                                    getLoginFailureCountWithConn(connection,credential.getLoginID(), logOutCnt);
	                                    resmsg.put("STATUS", "Error");
	                                    resmsg.put("Message", "Invalid Credential! " + (4 - logOutCnt) + " attempts left out of 5");
	                                } else {
	                                    FRM_Integration.requestEnqueueAPI(loginInfo, "F", "L");
	                                    lockedUserWithConn(connection,credential.getLoginID());
	                                    resmsg.put("STATUS", "Error");
	                                    resmsg.put("Message", "Account is locked due to maximum number of invalid attempts.");
	                                }
	                            } else {
	                                if (logOutCnt >= 5) {
	                                    FRM_Integration.requestEnqueueAPI(loginInfo, "F", "L");
	                                    resmsg.put("Portal_Pass", "NotValid");
	                                    resmsg.put("STATUS", "Error");
	                                    resmsg.put("Message", "Account is locked due to maximum number of invalid attempts.");
	                                } else {
	                                    log.info("Call RNB <<<<<<<<<<<<<<<<<<<:::::" + loginInfo.toString());
	                                    if ("Y".equals(ApplicationGenParameter.FRMSTATUS) && loginInfo.has("FRM_SECOND_AUTH_FLAG")) {
	                                        if (loginInfo.has("MPIN")) {
	                                            loginInfo.put("CustomerId", loginInfo.getString("loginID"));
	                                            String[] arr = validateOTP2ndAuth(loginInfo);
	                                            if ("true".equals(arr[0].trim())) {
	                                                FRM_Integration.requestEnqueueAPI(loginInfo, "S", "A");
	                                            } else {
	                                                resmsg.put("Portal_Pass", "WRONGOTP");
	                                                resmsg.put("Message", "Invalid OTP!!");
	                                                FRM_Integration.requestEnqueueAPI(loginInfo, "F", "A");
	                                            }
	                                            resmsg.put("FRM_SECOND_AUTH_FLAG", "FALSE");
	                                        } else {
	                                            String riskBand = FRM_Integration.requestRD_API(loginInfo, "A");
	                                            log.info("Call riskBand <<<<<<<<<<<<<<<<<<<:::::" + riskBand);
	                                            if (getSecondFactorByRisk(riskBand, FRM_Integration.DeviceType.portal)) {
	                                                resmsg.put("FRM_SECOND_AUTH_FLAG", "TRUE");
	                                                resmsg.put("Message", "User Second Auth Required");
	                                            } else {
	                                                resmsg.put("FRM_SECOND_AUTH_FLAG", "FALSE");
	                                                FRM_Integration.requestEnqueueAPI(loginInfo, "S", "A");
	                                            }
	                                        }
	                                    } else {
	                                        FRM_Integration.requestEnqueueAPI(loginInfo, "S", "A");
	                                    }
	                                    Query updateQuery = entityManager.createQuery(
	                                    	    "UPDATE SmeAppLoginInfo SET lastRequestTime = :currentTime, loginDate = :currentTime, loginCount = 0, logoutMode = 'T' WHERE loginId = :loginID");
	                                    	updateQuery.setParameter("currentTime", new Date());
	                                    	updateQuery.setParameter("loginID", loginInfo.getString("loginID"));
	                                    	updateQuery.executeUpdate();
	                                }
	                            }
	                        }
	                    }
	                } else {
	                    resmsg = new CustomerManagementDAO().registerWithRNB(credential, serviceName + "_A");
	                    resmsg.put("STATUS", "Error");
	                    resmsg.put("Portal_Pass", "NotValid");
	                }
	            } else {
	                resmsg.put("STATUS", "Error");
	                resmsg.put("Portal_Pass", "NotValid");
	                resmsg.put("Message", "Invalid Credential.");
	            }
	        } catch (Exception e) {
	          //  log.error("Error in validCheckerForPortal", e);
	        	e.printStackTrace();
	            resmsg.put("STATUS", "Error");
	            resmsg.put("Portal_Pass", "NotValid");
	            resmsg.put("Message", "Error occurred: " + e.getMessage());
	        }
	        return resmsg;
	    }
	


	@Override
	public JSONObject handleBlockUnblockWorkFlow(String customerId, String userType, String usrReq) {
		log.info("in handleBlockUnblockWorkFlow");
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String userLock = "";
		String userBlock = "";
		JSONObject jsonObj = new JSONObject();
		try {
			connection = getConnection(new Object() {
			}.getClass().getEnclosingMethod().getName().toString());

			pstmt = connection.prepareStatement(getQuery("GET_SINGLE_CUSTOMER_DATA_FROM_LOGIN_TABLE"));
			pstmt.setString(1, customerId);
			pstmt.setString(2, usrReq);
			rset = pstmt.executeQuery();
			while (rset.next()) {
				userLock = rset.getString(1);
				userBlock = rset.getString(2);
			}

			if ((!userLock.isEmpty() && userLock != null) && (!userBlock.isEmpty() && userBlock != null)) {
				if (userBlock.equals("Y") || userLock.equals("Y")) {
					if (userType.equals("UB")) {
						jsonObj.put("FlowFlag", true);
					} else {
						jsonObj.put("FlowFlag", false);
						jsonObj.put("Message", path.getMsg("UNBL002"));
						jsonObj.put("ErrorCode", "UNBL002");
					}
				} else if ((userBlock.equals("N") && userLock.equals("N"))) {
					if (userType.equals("UB")) {
						jsonObj.put("FlowFlag", false);
						jsonObj.put("Message", path.getMsg("UNBL003"));
						jsonObj.put("ErrorCode", "UNBL003");
					} else {
						jsonObj.put("FlowFlag", true);
					}
				}
			} else {
				jsonObj.put("FlowFlag", false);
				jsonObj.put("Message", path.getMsg("UNBL004"));
				jsonObj.put("ErrorCode", "UNBL004");
			}

		} catch (Exception e) {
			// log.info("Exception ===", e);
		} finally {

			try {
				
			} catch (JSONException e) {
				// log.info("SQLException ===", e);
			}
		}

		return jsonObj;
	}


	public String getCustIDForAlias(String aliasName) {
		String custId = aliasName;

		try {

			// Use HQL query to retrieve customerId for given aliasId
			Query query = entityManager.createQuery("SELECT customerId FROM RnbidAliasIdMapping WHERE aliasId= :aliasName");
			query.setParameter("aliasName", aliasName);
			Object result = query.getSingleResult();
			if (result != null) {
				custId = result.toString(); // Convert result to string if it's not null
			}

		} catch (Exception e) {
		}

		return custId;
	}

	public boolean checkCustomerExistInXREF(String customerID) {
		boolean existFlag = false;

		try {
			// Execute the query using EntityManager
			Query query = entityManager.createQuery(
					"select count(*) from VwChAcctCustXref where codCust = :custId AND codProd NOT IN (404) "
					+ "AND codAcctCustRel not in ('SOW','GUR','JOF','JOO','JAO','JAF','DEV','GUA','NOM','SOL','THR','VAL','COB','OTH','PAR','PRO')");
			query.setParameter("custId", customerID);

			Object result = query.getSingleResult();

			// Get the count result
			int count = ((Number) result).intValue();

			// Check if count is greater than zero
			if (count > 0) {
				existFlag = true;
			}

		} catch (Exception e) {
			// Handle exception (log or rethrow)
			e.printStackTrace(); // Replace with proper logging
		}

		return existFlag;
	}

	public boolean validateLoginRequest(String requestToken) {
        boolean logRequest = false;
        EntityManager localEntityManager = entityManagerFactory.createEntityManager().unwrap(Session.class);
        try {
            // Check if the requestToken already exists
            Query query = entityManager.createQuery("SELECT COUNT(*) FROM SmeRequest WHERE requestId = :request_id");
            query.setParameter("request_id", requestToken);

            // Execute the query and check the result
            Long count = ((Number) query.getSingleResult()).longValue();
            if (count > 0) {
                // If record exists, return false as per your logic
                return logRequest;
            } else {
                // If record does not exist, insert a new record
             
                Query query1 = localEntityManager.createQuery("INSERT INTO SmeRequest (requestId) VALUES (:request_id)");
                query1.setParameter("request_id", requestToken);
                localEntityManager.getTransaction().begin();
                int insertCnt = query1.executeUpdate();
                localEntityManager.getTransaction().commit();

              

                // If insertion is successful, set logRequest to true
                if (insertCnt > 0) {
                    logRequest = true;
                }
            }
        } catch (Exception e) {
            // Handle exception (you may want to log this exception)
            e.printStackTrace();
        }

        return logRequest;
    }
	
	public boolean userValidate(String loginID, String loginType) {
		boolean loginFlag = false;

		try {
			String hqlQuery = "";
			if ("PORTAL".equals(loginType)) {
				hqlQuery = "SELECT COUNT(*) FROM SmeAppLoginInfo WHERE loginId = :loginID AND userType = 'A' AND loginTypePortal = 'Y'";
			} else if ("MOBILE".equals(loginType)) {
				hqlQuery = "SELECT COUNT(*) FROM SmeAppLoginInfo WHERE loginId = :loginID AND userType = 'A' AND loginTypeMobile = 'Y'";
			} else {
				return loginFlag;
			}

			Query query = entityManager.createQuery(hqlQuery);
			query.setParameter("loginID", loginID);
			long count = ((Number) query.getSingleResult()).longValue();

			if (count > 0) {
				log.info("Login type valid.");
				loginFlag = true;
			}

		} catch (Exception e) {
			// Optionally rethrow or handle the exception
		}

		return loginFlag;
	}

	  @Transactional
	    public void insertSessionId(String userId, String sessionId) {
	        log.info("Calling insertSessionId method with userId: " + userId);
	        EntityManager localEntityManager = entityManagerFactory.createEntityManager().unwrap(Session.class);
	        localEntityManager.getTransaction().begin();
	        try {
	            // Define the native SQL update query
	            String sqlUpdate = "UPDATE SmeAppLoginInfo SET sessionId = :sessionId WHERE loginId = :userId";

	            // Create the Query object
	            Query query = localEntityManager.createQuery(sqlUpdate);
	            query.setParameter("sessionId", sessionId);
	            query.setParameter("userId", userId);

	            // Execute the update
	            int updatedEntities = query.executeUpdate();
	            localEntityManager.getTransaction().commit();
	            log.info("Number of entities updated: " + updatedEntities);

	        } catch (Exception e) {
	            //log.error("Exception occurred while updating sessionId", e);
	            // Optionally, you can throw a custom exception or handle it as needed
	        }
	    }

	  public net.sf.json.JSONObject getServiceCodeForCustomer(String userID) {
		    net.sf.json.JSONObject resmsg= new   net.sf.json.JSONObject();

		    try {
		        StringBuilder serviceCodeBuild = new StringBuilder();

		        // Create JPQL query
		        String jpql = "SELECT DISTINCT s.groupServiceCode FROM SmeServiceAccessMst s "
		                + "WHERE s.customerId = :customer_id AND s.activeFlag = 'Y'";

		        Query query = entityManager.createQuery(jpql);
		        query.setParameter("customer_id", userID);

		        log.info("In getServiceCodeForCustomer with userID: " + userID);

		        // Execute the query and retrieve the result list
		        List<String> results = query.getResultList();

		        // Build the service codes string
		        for (String serviceCode : results) {
		            serviceCodeBuild.append(serviceCode).append("|");
		        }

		        // Remove the trailing '|'
		        if (serviceCodeBuild.length() > 0) {
		            serviceCodeBuild.setLength(serviceCodeBuild.length() - 1);
		        }

		        // Check if serviceCodeBuild is empty
		        if (serviceCodeBuild.length() == 0) {
		            log.info("No service codes found");
		            resmsg.put("status", "1");
		            resmsg.put("message", "BT02|BT00|BT01|DU01|SL00|SL01|SL03|FT00|FT01|FT02|FT03|SI00|FD00|SB03|GB00|SB00|SB01|SB02|SL02|SLE01|SLE02|GB01|GB02|GT01|SB03|");
		        } else {
		            log.info("Service codes found: " + serviceCodeBuild.toString());
		            resmsg.put("status", "1");
		            resmsg.put("message", serviceCodeBuild.toString());
		        }

		    } catch (Exception e) {
		    	e.printStackTrace();
		        //log.error("Exception occurred: ", e);
		        resmsg.put("status", "0");
		        resmsg.put("message", "Exception occurred.");
		    }

		    log.info("Response message: " + resmsg.toString());
		    return resmsg;
		}

	
	  public boolean updateTokenDB(String methodName, String token, String p_UserId) {
		    log.info("Processing updateTokenDB for p_UserId: " + p_UserId);
		    boolean resflag = false;
		    Long l_UpdatedSeq = 0L;
		    EntityManager localEntityManager = null;

		    try {
		        localEntityManager = entityManagerFactory.createEntityManager();
		       

		        if ("SMEMCS005".equalsIgnoreCase(methodName) || "SMEMCS002".equalsIgnoreCase(methodName)) {
		            // JPQL query to insert into SecurityLogDetail
		            String insertJpql = "INSERT INTO MsmeSecurityLogDetail (seq, userid, logindt, requesttime, serviceid, sessionActive, hostSessionId) " +
		                "VALUES (nextval('msme_securitylog_dtl_seq'), :userid, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, :serviceid, 'Y', :hostSessionId)";
		            Query insertQuery = localEntityManager.createNativeQuery(insertJpql);
		            insertQuery.setParameter("userid", p_UserId);
		            insertQuery.setParameter("serviceid", methodName.trim());
		            insertQuery.setParameter("hostSessionId", token.trim());
		            localEntityManager.getTransaction().begin();
		            insertQuery.executeUpdate();
		          
		        } else {
		            // Retrieve the max sequence for the given userId and serviceId
		        	 String maxSeqJpql = "SELECT MAX(s.seq) FROM MsmeSecurityLogDetail s WHERE s.userId = :userId AND s.sessionActive = 'Y' AND s.serviceId = :serviceId";
		        	    Query maxSeqQuery = localEntityManager.createQuery(maxSeqJpql);
		        	    maxSeqQuery.setParameter("userId", p_UserId.trim().toUpperCase());
		        	    maxSeqQuery.setParameter("serviceId", methodName.trim());
		        	    Object result = maxSeqQuery.getSingleResult();
		        	    l_UpdatedSeq = (result != null) ? (Long) result : 0L;
		        	    System.out.println("Current seq: " + l_UpdatedSeq);

		        	    // Calculate the time difference in Java
		        	    LocalDateTime now = LocalDateTime.now();
		        	    LocalDateTime requestTime = now; // This should be replaced with the actual request time from your entity

		        	    Duration duration = Duration.between(requestTime, now);
		        	    long timeDifference = duration.toMillis();
		        	    String updateJpql = "UPDATE MsmeSecurityLogDetail s SET s.responseTime = :responseTime, " +
		        	            "s.timeDifference = :timeDifference, s.hostSessionId = :hostSessionId WHERE s.seq = :seq";
		        	        Query updateQuery = localEntityManager.createQuery(updateJpql);

		        	        // Create the current Timestamp
		        	        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		        	        updateQuery.setParameter("responseTime", currentTimestamp);
		        	        updateQuery.setParameter("timeDifference", timeDifference);
		        	        updateQuery.setParameter("hostSessionId", token.trim());
		        	        updateQuery.setParameter("seq", l_UpdatedSeq);

		        	        // Begin transaction, execute update, and commit
		        	        localEntityManager.getTransaction().begin();
		        	        int rowsUpdated = updateQuery.executeUpdate();
		        	        localEntityManager.getTransaction().commit();

		        	        System.out.println("Number of rows updated: " + rowsUpdated);
		        }

		        // Update SmeAppLoginInfo
		        String updateTokenJpql = "UPDATE SmeAppLoginInfo s SET s.reqToken = :reqToken WHERE s.loginId = :loginId";
		        Query updateTokenQuery = localEntityManager.createQuery(updateTokenJpql);
		        updateTokenQuery.setParameter("reqToken", token.trim());
		        updateTokenQuery.setParameter("loginId", p_UserId.trim().toUpperCase());
		        
		        localEntityManager.getTransaction().begin();
		        updateTokenQuery.executeUpdate();
		        localEntityManager.getTransaction().commit();
		        resflag = true;

		    } catch (Exception e) {
		        e.printStackTrace();
		        if (localEntityManager != null && localEntityManager.getTransaction().isActive()) {
		            localEntityManager.getTransaction().rollback();
		        }
		      //  log.error("Error updating token in DB", e);

		    } finally {
		        if (localEntityManager != null && localEntityManager.isOpen()) {
		            localEntityManager.close();
		        }
		        log.info("########################:::Response::::::");
		    }

		    return resflag;
		}

	
	  @Transactional
	    public void getLoginFailureCountWithConn(Connection connection ,String loginID, int logOutCnt) {
	        try {
	            String sql = "UPDATE SmeAppLoginInfo SET lastFailureAttemptTime = sysdate, loginCount = :loginCount WHERE loginId = :loginId";
	            entityManager.createNativeQuery(sql)
	                .setParameter("loginCount", logOutCnt + 1)
	                .setParameter("loginId", loginID.trim())
	                .executeUpdate();
	        } catch (Exception e) {
	            // Log the exception
	            //log.info("Exception ===", e);
	        }
	    }

	  @Transactional
	    public void lockedUserWithConn(Connection connection,String loginID) {
	        try {
	            String sql = "UPDATE SmeAppLoginInfo SET userBlocked='Y' WHERE loginId = :loginId";
	            entityManager.createNativeQuery(sql)
	                .setParameter("loginId", loginID)
	                .executeUpdate();
	        } catch (Exception e) {
	            // Log the exception
	          //  log.info("Exception ===", e);
	        }
	    }
	  
	  
	



	

	 @Transactional
	    public JSONObject logoutUser(String logInID) {
	        JSONObject resmsg = new JSONObject();

	        try {
	            // Update SMEAppLoginInfo
	            AppLoginInfo loginInfo = entityManager.find(AppLoginInfo.class, logInID.trim());
	            if (loginInfo != null) {
	                loginInfo.setLogoutDate(new Timestamp(System.currentTimeMillis()));
	                loginInfo.setLogoutMode("T");
	                loginInfo.setSessionId("");
	                entityManager.merge(loginInfo);
	            }

	            // Delete SMEOnlineSessionDtl
	            Query deleteQuery = entityManager.createQuery(
	                    "DELETE FROM SMEOnlineSessionDtl WHERE companyId = :companyId AND customerId = :customerId");
	            deleteQuery.setParameter("companyId", ApplicationGenParameter.companyCustomerMapList.get(logInID.trim()));
	            deleteQuery.setParameter("customerId", logInID.trim());
	            int sessCntD = deleteQuery.executeUpdate();

	            if (sessCntD > 0) {
	                log.info("Deleted entry for sessionCnt logoutUser " + logInID);
	            }

	            // Update MSMESecurityLogDtl
	            Query updateQuery = entityManager.createQuery(
	                    "UPDATE MSMESecurityLogDtl SET timeDifference = 0, sessionActive = 'N' WHERE userId = :userId AND requestTime > CURRENT_TIMESTAMP - INTERVAL '2' DAY");
	            updateQuery.setParameter("userId", logInID.trim());
	            int tokenUpdated = updateQuery.executeUpdate();

	            resmsg.put("ResCode", "0");
	            resmsg.put("ResBody", "SUCCESS");

	        } catch (Exception e) {
	            resmsg.put("STATUS", "Error");
	            resmsg.put("Message", "Exception Occurred");
	           // log.error("Exception occurred while logging out user", e);
	        }

	        return resmsg;
	    }
	 @Transactional
	 public void insertIpAddress(Connection connection, String customerId, String ipAddress, String requestMode) {
	     log.info("In insertIpAddress");
	     EntityManager localEntityManager = entityManagerFactory.createEntityManager();
	     try {
	         String l_customerId = customerId.trim().toUpperCase();
	         String l_ipAddress = ipAddress.trim();
	         int count = 0;
	         localEntityManager.getTransaction().begin();

	         // Check if record exists using HQL
	         Query countQuery = localEntityManager.createQuery("SELECT COUNT(*) FROM SmeCustomerIpAddressDtl WHERE customerIp = :customerIp");
	         countQuery.setParameter("customerIp", l_customerId);
	         count = ((Number) countQuery.getSingleResult()).intValue();

	         if (count > 0) {
	             // Update record
	             if (ipAddress.equals(l_ipAddress)) {
	                 l_ipAddress = "NA";
	             }
	             Query updateQuery = localEntityManager.createQuery("UPDATE SmeCustomerIpAddressDtl SET ipAddress = :ipAddress, requestMode = :requestMode WHERE customerIp = :customerIp");
	             updateQuery.setParameter("ipAddress", l_ipAddress);
	             updateQuery.setParameter("requestMode", requestMode);
	             updateQuery.setParameter("customerIp", l_customerId);
	             updateQuery.executeUpdate();
	         } else {
	             // Insert new record
	             Query insertQuery = localEntityManager.createQuery("INSERT INTO SmeCustomerIpAddressDtl (customerIp, ipAddress, requestMode) VALUES (:customerIp, :ipAddress, :requestMode)");
	             insertQuery.setParameter("customerIp", l_customerId);
	             insertQuery.setParameter("ipAddress", l_ipAddress);
	             insertQuery.setParameter("requestMode", requestMode);
	             insertQuery.executeUpdate();
	         }

	         // Commit transaction
	         localEntityManager.getTransaction().commit();

	         // Update the application parameter map
	         ApplicationGenParameter.custIpAddMapList.put(l_customerId, l_ipAddress);
	         log.info("ApplicationGenParameter updated successfully.");

	     } catch (Exception e) {
	         // Rollback transaction in case of error
	         if (localEntityManager.getTransaction().isActive()) {
	             localEntityManager.getTransaction().rollback();
	         }
	      //   log.error("Exception occurred while inserting/updating IP address: ", e);
	         throw e; // Re-throw the exception to handle it appropriately higher up the call stack
	     } finally {
	         // Close EntityManager
	         localEntityManager.close();
	     }
	 }


  //FRM integration----2459
  		public String[] validateOTP2ndAuth(net.sf.json.JSONObject reqBeneDtl) {

  			String[] userDetails = new String[2];
  				
  			log.info("In validOTP function.....");

  			String otp = reqBeneDtl.get("MPIN").toString();
  			String custID = reqBeneDtl.getString("loginID");
  			String[] otpArr = otp.split("-");
  			log.info(":::otp key........");
  			reqBeneDtl.put("otpKey", otpArr[0]);
  			reqBeneDtl.put("otpValue", otpArr[1]);

  			CustomerManagementDAO custInfo = new CustomerManagementDAO();
  			net.sf.json.JSONObject resJson = custInfo.requestForVerifyOTP(reqBeneDtl, "MCSC0003");

  			log.info(":::return json::::::");
  			
  			

  			log.info("hhhhhhhhhhh::");
  			if (resJson.getString("ResCode").equals("0") && !resJson.getJSONObject("ResBody")
  					.getJSONObject("verifyOTPResponse").getString("isValid").equals("false")) {
  				userDetails[0] = "true";
  				userDetails[1] = "0";
  				
  			} else {
  				userDetails[0] = "false";
  			}
  			log.info(":::userDetails::::::"+userDetails);
  			return userDetails;


  		}
  		
  		 @Transactional
  	    public boolean getSecondFactorByRisk(String riskBand, String deviceType) {
  	        boolean secondFactRequired = false;

  	        try {
  	            // Create the native query
  	            Query query = entityManager.createNativeQuery(
  	                "SELECT SECOND_FACTOR FROM SME_FRM_BAND_FACTOR WHERE RISK_BAND = :riskBand AND DEVICE_TYPE = :deviceType");
  	            query.setParameter("riskBand", riskBand);
  	            query.setParameter("deviceType", deviceType);

  	            // Execute the query and get the result
  	            String secFact = (String) query.getSingleResult();

  	            // Check if second factor is required
  	            if ("Y".equals(secFact)) {
  	                secondFactRequired = true;
  	            }

  	           // log.info("RiskBand: {} - DeviceType: {} - SecondFactorRequired: {}", riskBand, deviceType, secondFactRequired);

  	        } catch (Exception e) {
  	           // log.error("Error in getSecondFactorByRisk", e);
  	        }

  	        return secondFactRequired;
  	    }
  		 

  		
  		public net.sf.json.JSONObject validUserForMaker(net.sf.json.JSONObject indata) {

  			log.info("validUser MAKER===");
  			String firstLoginStatus = "",
  					regID = "", 
  					userLockStatus = "";
  			String logOutTime = "";
  			int logOutCnt = 0;

  			EntityManager localEntityManager = entityManagerFactory.createEntityManager().unwrap(Session.class);

  			net.sf.json.JSONObject resmsg = new 	net.sf.json.JSONObject();

  			try {

  				String sqlQuery = "INSERT INTO SmeAppLoginInfoLogInfo SELECT * FROM SmeAppLoginInfo WHERE loginId = :loginID";
  				Query query = localEntityManager.createQuery(sqlQuery);

  				query.setParameter("loginID", indata.getString("loginID").trim().toUpperCase());
  				localEntityManager.getTransaction().begin();
  				query.executeUpdate();
  				localEntityManager.getTransaction().commit();

  				String sqlSelect = "SELECT regId, loginCount, userLocked, firstLogin "
  						+ "FROM SmeAppLoginInfo WHERE loginId = :login_id";
  				Query selectQuery = localEntityManager.createQuery(sqlSelect);
  				selectQuery.setParameter("login_id", indata.getString("loginID").trim().toUpperCase());

  				// Get results//(String.valueOf( result[3].toString()))
  				Object[] result = (Object[]) selectQuery.getSingleResult();
  				if (result != null) {
  					regID = (String) result[0];
  					logOutCnt=	Integer.parseInt(result[1].toString());
  				  // logOutCnt = result[1] != null ? Integer.parseInt(result[1].toString()) : 0;  	
  					userLockStatus = (String.valueOf(result[2].toString()));
  					firstLoginStatus = (String.valueOf(result[3].toString()));
  				} else {
  					resmsg.put("STATUS", "Error");
  					resmsg.put("Message", "Not Registered With This ID.");
  				}

  				if (userLockStatus.equals("Y") && (logOutCnt > 5)) {
  					resmsg.put("STATUS", "Error");
  					resmsg.put("Message", "Account is locked due to maximum number of invalid attempt.");
  				} else {
  					int count = 0;
  					if (firstLoginStatus.equals("Y") || firstLoginStatus.equals("P")) {

  						insertIpAddress(connection, indata.getString("loginID"), indata.getString("ipAddress"),
  								indata.getString("requestFromFlag"));

  						// First Time Login
  						// CHECK_VALID_USER2=SELECT
  						// COUNT(*),LOGIN_COUNT,TO_CHAR(LOGOUT_DATE, 'DD MON YYYY
  						// HH24:MI:SS') FROM SME_APP_LOGIN_INFO WHERE LOGIN_ID = ?
  						// AND PASSWORD = ? GROUP BY LOGIN_COUNT,
  						// TO_CHAR(LOGOUT_DATE, 'DD MON YYYY HH24:MI:SS')

  						String sqlSelect1 = "SELECT COUNT(*), loginCount, TO_CHAR(logoutDate, 'DD MON YYYY HH24:MI:SS'), regId, userType "
  								+ "FROM SmeAppLoginInfo " + "WHERE loginId = :login_id AND password = :password "
  								+ "GROUP BY loginCount, TO_CHAR(logoutDate, 'DD MON YYYY HH24:MI:SS'), regId, userType";

  						// Create and execute the query
  						Query selectQuery1 = localEntityManager.createQuery(sqlSelect1);
  						selectQuery1.setParameter("login_id", indata.getString("loginID").trim().toUpperCase());
  						selectQuery1.setParameter("password", indata.getString("password"));

  						// Fetch results
  						Object[] result1 = (Object[]) selectQuery1.getSingleResult();

  						// Extract values
  						count = ((Number) result1[0]).intValue();
  						logOutCnt = ((Number) result1[1]).intValue();
  						logOutTime = (String) result1[2];

  						if (count > 0) {
  							net.sf.json.JSONObject temp = new net.sf.json.JSONObject();
  							if (firstLoginStatus.equals("Y")) {
  								temp.put("MakerStatus", "1");
  								temp.put("MakerMsg", path.getMsg("S113"));
  							} else {
  								temp.put("MakerStatus", "2");
  								temp.put("MakerMsg", path.getMsg("S114"));
  							}

  							resmsg.put("STATUS", "SUCCESS");
  							resmsg.put("Message", temp);

  							String sqlSelect2 = "UPDATE SmeAppLoginInfo SET lastRequestTime = sysdate, loginDate = sysdate,"
  									+ "loginCount = :login_count, logoutMode = :logout_mode  WHERE regId = :reg_id";

  							Query selectQuery2 = localEntityManager.createQuery(sqlSelect2);

  							selectQuery2.setParameter("login_count", 0);
  							selectQuery2.setParameter("logout_mode", "T");
  							selectQuery2.setParameter("reg_id", regID);

  							localEntityManager.getTransaction().begin();
  							selectQuery2.executeUpdate();
  							localEntityManager.getTransaction().commit();

  						} else {
  							if (logOutCnt < 4) {

  								log.info("login Failure with count :::" + indata.getString("loginID") + "Count :::"
  										+ logOutCnt);
  								getLoginFailureCountWithConn(connection, indata.getString("loginID"), logOutCnt);
  								resmsg.put("STATUS", "Error");
  								if ((4 - logOutCnt) == 1) {
  									resmsg.put("Message",
  											"Invalid Credential ! " + (4 - logOutCnt) + " attempt left out of 5");
  								} else {
  									resmsg.put("Message",
  											"Invalid Credential ! " + (4 - logOutCnt) + " attempts left out of 5");
  								}

  								resmsg.put("Message", "Invalid Credential !");// App sec user enumaration resolved App sec
  																				// May 2023

  							} else {
  								log.info("logout count in else part :::" + indata.getString("loginID"));
  								lockedUserWithConn(connection,indata.getString("loginID"));
  								resmsg.put("STATUS", "Error");
  								resmsg.put("Message", "Account is locked due to maximum number of invalid attempt.");
  							}
  						}
  					} else {
  						insertIpAddress(connection, indata.getString("loginID"), indata.getString("ipAddress"),
  								indata.getString("requestFromFlag"));
  						
  						net.sf.json.JSONObject temp = new net.sf.json.JSONObject ();

  						String sqlSelect3 = "SELECT COUNT(*),loginCount,TO_CHAR(logoutDate, 'DD MON YYYY HH24:MI:SS'),regId, userType"
  								+ " FROM SmeAppLoginInfo WHERE loginId = :login_id AND password = :password"
  								+ " GROUP BY loginCount, TO_CHAR(logoutDate, 'DD MON YYYY HH24:MI:SS'),regId, userType";

  						Query query1 = entityManager.createQuery(sqlSelect3);

  						query1.setParameter("login_id", indata.getString("loginID").trim().toUpperCase());
  						query1.setParameter("password", indata.getString("password"));

  						Object[] result2 = (Object[]) query1.getSingleResult();

  						// Extract values
  						count =Integer.parseInt(result[0].toString());  
  						logOutCnt = result[1] != null ? Integer.parseInt(result[1].toString()) : 0;  
  						logOutTime = (String) result2[2];
  						regID = (String) result2[3];

  						if (count > 0) {
  							log.info("In count ====" + count);
  							if (logOutCnt < 5) {
  								log.info("In logOutCnt ====" + logOutCnt);

  								String sqlSelect2 = "UPDATE SmeAppLoginInfo SET lastRequestTime = sysdate, loginDate = sysdate,"
  										+ "loginCount = :login_count, logoutMode = :logout_mode  WHERE regId = :reg_id";

  								Query selectQuery2 = localEntityManager.createQuery(sqlSelect2);

  								selectQuery2.setParameter("login_count", 0);
  								selectQuery2.setParameter("logout_mode", "T");
  								selectQuery2.setParameter("reg_id", regID);

  								localEntityManager.getTransaction().begin();
  								selectQuery2.executeUpdate();
  								localEntityManager.getTransaction().commit();

  								// check user already logged in or not
  								// SELECT_MAKER_DTL=select
  								// SMEMAKERNAME,SMEMAILID,SMEMAKERCD,MOBNO from
  								// COM_MAKER_MST where MAKER_ALIAS_NAME = ?
  								String sqlSelect4 = "SELECT sMeMakerName, sMeEmailId, sMeMakerCd, mobNo "
  										+ "FROM ComMakerMst "
  										+ "WHERE makerAliasName = :maker_alias_name "
  										+ "AND activeFlag = 'Y'";

  								// Create and execute the query
  								Query selectQuery3 = entityManager.createQuery(sqlSelect4);
  								selectQuery3.setParameter("maker_alias_name", indata.getString("loginID").trim().toUpperCase());

  								// Fetch results
  								Object[] result3 = (Object[]) selectQuery3.getSingleResult();

  								// Extract values
  								String makerName = (String) result3[0];
  								String makerEmail = (String) result3[1]; // Corrected: directly cast to String
  								String authId = (String) result3[2]; // Corrected: directly cast to String
  								String makerMob = (String) result3[3]; // Corrected: directly cast to String

  								// Optionally, print or use the extracted values
  								System.out.println("Maker Name: " + makerName);
  								System.out.println("Maker Email: " + makerEmail);
  								System.out.println("Auth ID: " + authId);
  								System.out.println("Maker Mobile: " + makerMob);
  					
  								resmsg.put("MakerStatus", "0");
  								resmsg.put("MakerName", makerName);
  								resmsg.put("MakerEmail", makerEmail);
  								resmsg.put("AuthId", authId.split("_")[0]);
  								resmsg.put("MakerMob", makerMob);
  								resmsg.put("MakerMsg", path.getMsg("S109"));

  								String sqlSelect5 = "SELECT serviceCd FROM ComServiceMakerMst "
  										+ "WHERE sMeMakerCd = :smemakercd AND activeFlag = 'Y'";

  								// Create and execute the query
  								Query selectQuery4 = entityManager.createQuery(sqlSelect5);
  								selectQuery4.setParameter("smemakercd", indata.getString("loginID").trim().toUpperCase());

  								// Fetch results
  								Object result4 = selectQuery4.getSingleResult();

  								// Extract value
  								String makerServiceCD = result4 != null ? (String) result4 : "";
  								temp.put("MakerServiceCD", makerServiceCD);

  								// Handle last login time
  								if (logOutTime != null) {
  									resmsg.put("STATUS", "SUCCESS");
  									temp.put("lastLogIn", logOutTime);
  								} else {
  									resmsg.put("STATUS", "SUCCESS");
  									temp.put("lastLogIn", "NA");
  								}

  								resmsg.put("Message", temp);

  							} else {
  								log.info("Else logOutCnt ====" + logOutCnt);
  								// lockedUser(indata.getString("loginID"));
  								lockedUserWithConn(connection,indata.getString("loginID"));
  								resmsg.put("STATUS", "Error");
  								resmsg.put("Message", path.getMsg("E133"));// App sec user enumaration resolved App sec May
  																			// 2023
  							}
  						} else {
  							if (logOutCnt < 5) {
  								log.info("login Failure with count :::" + indata.getString("loginID") + "Count :::"
  										+ logOutCnt);

  								getLoginFailureCountWithConn(connection,indata.getString("loginID"), logOutCnt);
  								resmsg.put("STATUS", "Error");
  								if ((4 - logOutCnt) == 1) {
  									resmsg.put("Message",
  											"Invalid Credential ! " + (4 - logOutCnt) + " attempt left out of 5");
  								} else {
  									resmsg.put("Message",
  											"Invalid Credential ! " + (4 - logOutCnt) + " attempts left out of 5");
  								}
  							} else {
  								log.info("logout count in else part :::" + indata.getString("loginID"));
  								// lockedUser(indata.getString("loginID"));
  								lockedUserWithConn(connection,indata.getString("loginID"));
  								resmsg.put("STATUS", "Error");
  								resmsg.put("Message", "Account is locked due to maximum number of invalid attempt.");
  							}
  							resmsg.put("Message", "Invalid Credential !");// App sec user enumaration resolved App sec May
  																			// 2023
  						}

  					}
  				}

  				return resmsg;
  			} catch (Exception e) {
  				//log.info("Exception ===", e);
  				resmsg.put("STATUS", "Error");
  				resmsg.put("Message", "Exception Occured");
  				return resmsg;
  			} finally {

  				try {

  				} catch (JsonException e) {
  					//log.info("SQLException ===", e);
  				}
  			}

  		}
  		
  		
  	   public boolean isUserCredentialExist(UserLogin userlogin) {
  	        log.info("In isUserCredentialExist function .....");
  	        boolean loginflag = false;

  	        try {
  	            // Define the native query to count matching records
  	            String queryHql = "SELECT COUNT(*) FROM MV_Ci_CustmastAll WHERE codCustId = :cod_cust_id AND refCustTelex = :ref_cust_telex AND "
  	                    + "UPPER(refCustEmail) = UPPER(:ref_cust_email)";
  	            
  	            // Create the query using EntityManager
  	            Query countQuery = entityManager.createQuery(queryHql);
  	            
  	            // Set the parameters
  	            countQuery.setParameter("cod_cust_id", userlogin.getLoginID());
  	            countQuery.setParameter("ref_cust_telex", "91" + userlogin.getUserContactNo());
  	            countQuery.setParameter("ref_cust_email", userlogin.getUserEmail().trim());
  	            
  	            // Execute the query and get the result
  	            Number count = (Number) countQuery.getSingleResult();
  	            
  	            // Check if the count is greater than 0
  	            if (count != null && count.intValue() > 0) {
  	                log.info("Customer details valid ....");
  	                loginflag = true;
  	            }

  	        } catch (Exception e) {
  	          // log.error("Exception occurred while validating user credentials", e);
  	        }

  	        log.info("userValidate Result: " + loginflag);
  	        return loginflag;
  	    }

  		
  	 @Transactional
     public boolean checkCustomerDeactivationFlag(String customerID) {
         log.info("In checkCustomerDeactivationFlag method for customerID: " + customerID);
         boolean existFlag = false;
         String companyId = null;
         try {
             // Assuming getCompanyId is used to initialize companyId. Adjust as needed.
             companyId = new LoginDaoImpl(entityManager).getCompanyId(customerID.trim());
             
             // Construct the native query
             String sql = "SELECT customerId FROM SmeCustDeactivateMaster WHERE companyId =:companyId AND activeFlag = 'Y' AND deActivateFlag = 'A'";
             
             // Create the native query
             Query query = entityManager.createQuery(sql);
             query.setParameter("companyId", companyId);

             // Execute the query and retrieve the results
             List<String> deactivateCustomerIdsList = query.getResultList();
             
             if (deactivateCustomerIdsList != null && !deactivateCustomerIdsList.isEmpty()) {
                 existFlag = deactivateCustomerIdsList.contains(customerID);
             }

             log.info("existFlag === " + existFlag);
         } catch (Exception e) {
            // log.info("Exception ===", e);
         }
         
         return existFlag;
     }
  		
  		
  			 
	
	
}