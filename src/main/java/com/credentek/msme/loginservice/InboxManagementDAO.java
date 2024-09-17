package com.credentek.msme.loginservice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.credentek.msme.ApplicationGenParm.ApplicationGenParameter;
import com.credentek.msme.database.DaoBase;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class InboxManagementDAO extends DaoBase {
	
	
	private static final Logger log = LogManager.getLogger(InboxManagementDAO.class);
	public String SERVICE_TYPE = ApplicationGenParameter.SERVICE_TYPE;
	public Date date = ApplicationGenParameter.date;
	public SimpleDateFormat sdf = ApplicationGenParameter.sdf;
	public String currentts = "";
	public JSONObject reqJsonfrmAPI = null;
	public String baseURL = "";
	public String apiResName = "";
	public String DOC_FILE_PATH = ApplicationGenParameter.UPLOAD_PATH;
	public String PROMO_IMG_UP_PATH = ApplicationGenParameter.PROMO_IMG_UP_PATH;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	
	   @Transactional
	    public JSONObject getInboxDetails(JSONObject jsonObj) {
	        JSONObject m_res_header = new JSONObject();
	        String companyId = "";
	        
	        try {
	            // Retrieve companyId based on the provided CustomerId
	            if (ApplicationGenParameter.companyCustomerMapList.get(jsonObj.getString("CustomerId").trim()) != null) {
	                companyId = ApplicationGenParameter.companyCustomerMapList.get(jsonObj.getString("CustomerId").trim()).trim();
	                if (companyId == null || companyId.trim().isEmpty()) {
	                    if (jsonObj.has("MakerId")) {
	                        jsonObj.put("MakerId", ApplicationGenParameter.makerAliasMapList.get(jsonObj.getString("MakerId")));
	                        companyId = jsonObj.getString("MakerId").split("_")[0];
	                    } else {
	                        m_res_header.put("ResCode", "1");
	                        m_res_header.put("ErrorDetails", "Company Id has not found for this Customer Id.");
	                        return m_res_header;
	                    }
	                }
	            } else {
	                m_res_header.put("ResCode", "1");
	                m_res_header.put("ErrorDetails", "Company Id has not found for this Customer Id.");
	                return m_res_header;
	            }

	            JSONObject finalJson = new JSONObject();
	            JSONArray offArray = new JSONArray();

	            // Create a native query to fetch offers
	            String offerQuery = "SELECT offerId, offerTagName, offerDescription, customerId, " +
	                    "TO_CHAR(createdOn, 'DD-MON-YYYY'), TO_CHAR(validTo, 'DD-MON-YYYY'), readFlag, moduleName " +
	                    "FROM Offer " +
	                    "WHERE customerId IN (:customerId, 'NA') AND createdOn >= SYSDATE - 7 " +
	                    "ORDER BY createdOn DESC";
	            
	            Query query = entityManager.createQuery(offerQuery);
	            query.setParameter("customerId", jsonObj.getString("CustomerId").trim());
	            
	            List<Object[]> resultList = query.getResultList();
	            for (Object[] row : resultList) {
	                JSONObject OfferJson = new JSONObject();
	                OfferJson.put("InboxId", row[0].toString());
	                OfferJson.put("InboxTagName", row[1].toString());
	                OfferJson.put("InboxDesc", row[2].toString());
	                OfferJson.put("CreadtedDate", row[4].toString());
	                OfferJson.put("ValidDate", row[5].toString());
	                OfferJson.put("ReadFlag", "Y".equals(row[6]));

	                if ( "Y".equals(row[7])) {
						OfferJson.put("ReadFlag", true);
					} else {
						OfferJson.put("ReadFlag", false);
					}
					
					offArray.add(OfferJson);
	            }

	            finalJson.put("InboxCount", resultList.size());
	            finalJson.put("InboxInfo", offArray);
	            m_res_header.put("ResCode", "0");
	            m_res_header.put("ResBody", finalJson);

	        } catch (Exception e) {
	            // Handle exceptions and log them
	            // log.error("Exception occurred", e);
	            m_res_header.put("ResCode", "1");
	            m_res_header.put("ErrorDetails", e.getMessage());
	        }

	        return m_res_header;
	    }

}
