package com.credentek.msme.loginservice;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.credentek.msme.database.DaoBase;
import com.credentek.msme.utils.PathRead;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

public class PasswordChecker extends DaoBase {
	

	private static final Logger log = LogManager.getLogger(PasswordChecker.class);
	PathRead path = PathRead.getInstance();
	
	 @PersistenceContext
	  private EntityManager entityManager;

	    @Transactional
	    public String checkPass(String userID, String inputPass) {
	        String[] TYPES = new String[5];
	        String[] UTYPES = new String[5];
	        String sql;
	        int index = -1;
	        String message = "";
	        boolean flag_m = false;

	        try {
	            // Fetch existing passwords
	            sql = "SELECT PASS1, PASS2, PASS3, PASS4, PASS5 FROM PASSWORD_COUNTER WHERE CUST_ID=:userId AND ACTIVE_FLAG = 'Y'";
	            Query query = entityManager.createNativeQuery(sql);
	            query.setParameter("userId", userID);

	            Object[] result = (Object[]) query.getSingleResult();
	            for (int i = 0; i < result.length; i++) {
	                TYPES[i] = (String) result[i];
	            }

	            // Find the first "NA" position
	            for (int i = 0; i < TYPES.length; i++) {
	                if (TYPES[i].equals("NA")) {
	                    index = i;
	                    break;
	                }
	            }

	            // Handle the first time entry case
	            if (index == -1) {
	                // First time entry
	                sql = "INSERT INTO PASSWORD_COUNTER (CUST_ID, PASS1, ACTIVE_FLAG, MODIFY_DT) VALUES (:userId, :inputPass, 'Y', SYSDATE)";
	                query = entityManager.createNativeQuery(sql);
	                query.setParameter("userId", userID);
	                query.setParameter("inputPass", inputPass);
	                query.executeUpdate();
	                message = "Updated Successfully.";
	                return message;
	            }

	            // Check for the input password in the last five
	            StringBuilder checkBuff = new StringBuilder();
	            for (int i = 0; i < TYPES.length; i++) {
	                checkBuff.append(TYPES[i]);
	            }

	            if (checkBuff.toString().contains(inputPass)) {
	                flag_m = false;
	                message = "3~Already Used in Previous Five, please use another one.";
	            } else {
	                // Update the password list
	                UTYPES[0] = inputPass;
	                System.arraycopy(TYPES, 0, UTYPES, 1, TYPES.length - 1);

	                sql = "UPDATE PASSWORD_COUNTER SET PASS1 =:pass1, PASS2 =:pass2, PASS3 =:pass3, PASS4 =:pass4, PASS5 =:pass5, ACTIVE_FLAG = 'Y', MODIFY_DT = SYSDATE WHERE CUST_ID =:custId";
	                query = entityManager.createNativeQuery(sql);
	                query.setParameter("pass1", UTYPES[0]);
	                query.setParameter("pass2", UTYPES[1]);
	                query.setParameter("pass3", UTYPES[2]);
	                query.setParameter("pass4", UTYPES[3]);
	                query.setParameter("pass5", UTYPES[4]);
	                query.setParameter("custId", userID);
	                query.executeUpdate();
	                flag_m = true;
	                message = "Updated Successfully.";
	            }
	        } catch (Exception e) {
	            // Handle exception
	            e.printStackTrace();
	            message = "An error occurred: " + e.getMessage();
	        }
	        return message;
	    }
	

}
