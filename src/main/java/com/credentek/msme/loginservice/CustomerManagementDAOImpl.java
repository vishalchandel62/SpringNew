package com.credentek.msme.loginservice;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.credentek.msme.ApplicationGenParm.ApplicationGenParameter;
import com.credentek.msme.database.DaoBase;
import com.credentek.msme.logindao.LoginDaoImpl;
import com.credentek.msme.loginmodel.AccountDetails;
import com.credentek.msme.maintenance.dao.UserLoginDaoImpl;
import com.credentek.msme.utils.HibernateUtil;
import com.credentek.msme.utils.PathRead;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

import org.hibernate.Transaction;

public class CustomerManagementDAOImpl extends DaoBase implements CustomerManagementDAO{
	
	@Autowired
	private EntityManager entityManager;
	
	
	@Autowired
	public CustomerManagementDAOImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	
	public static PathRead path = PathRead.getInstance();
	public JSONObject getAllAccDetails(AccountDetails acc_dtl) {
		JSONObject s2_header = new JSONObject();
		return s2_header;
	}

	
	
	 @Transactional
	    public net.sf.json.JSONObject handleBlockUnblockWorkFlow(String customerId, String userType, String usrReq) {
	        net.sf.json.JSONObject jsonObj = new net.sf.json.JSONObject();
	        try {
	            // Create the query using EntityManager
	            String sql = "SELECT userLocked, userBlocked FROM SmeAppLoginInfo WHERE loginId = :customerId AND userType = :usrReq";
	            Query query = entityManager.createQuery(sql);
	            query.setParameter("customerId", customerId);
	            query.setParameter("usrReq", usrReq);

	            // Execute the query and handle no result scenario
	            Object[] resultArray = null;
	            try {
	                resultArray = (Object[]) query.getSingleResult();
	            } catch (NoResultException e) {
	                // No result found; handle this scenario appropriately
	                jsonObj.put("FlowFlag", false);
	                jsonObj.put("Message", path.getMsg("UNBL004"));
	                jsonObj.put("ErrorCode", "UNBL004");
	                return jsonObj;
	            }

	            if (resultArray != null) {
	                String userLock = (String.valueOf( resultArray[0].toString()));
	                
	                String userBlock = (String.valueOf( resultArray[1].toString()));

	                if (userLock != null && userBlock != null) {
	                    if ("Y".equals(userBlock) || "Y".equals(userLock)) {
	                        if ("UB".equals(userType)) {
	                            jsonObj.put("FlowFlag", true);
	                        } else {
	                            jsonObj.put("FlowFlag", false);
	                            jsonObj.put("Message", path.getMsg("UNBL002"));
	                            jsonObj.put("ErrorCode", "UNBL002");
	                        }
	                    } else if ("N".equals(userBlock) && "N".equals(userLock)) {
	                        if ("UB".equals(userType)) {
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
	            } else {
	                jsonObj.put("FlowFlag", false);
	                jsonObj.put("Message", path.getMsg("UNBL004"));
	                jsonObj.put("ErrorCode", "UNBL004");
	            }
	        } catch (Exception e) {
	            // log.error("Exception occurred: ", e);
	            jsonObj.put("FlowFlag", false);
	            jsonObj.put("Message", "Exception occurred");
	        }
	        return jsonObj;
	    }



}
