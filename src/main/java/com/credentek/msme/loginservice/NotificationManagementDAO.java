package com.credentek.msme.loginservice;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


import com.credentek.msme.database.DaoBase;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class NotificationManagementDAO extends DaoBase{
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public NotificationManagementDAO(EntityManager entityManager)
	{
		this.entityManager=entityManager;
	}
	
	


	@Transactional
	public JSONObject getNotificationDetails(JSONObject jsonObj) {
	    JSONObject m_res_header = new JSONObject();
	    JSONObject finalJson = new JSONObject();
	    JSONArray notiArray = new JSONArray();

	    try {
	        String customerId = jsonObj.getString("CustomerId").trim();

	        // Convert Instant to LocalDateTime for JPQL
	        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
	        LocalDateTime currentTime = LocalDateTime.now();

	        // JPQL query to fetch notifications
	        String notificationQuery = "SELECT n.notificationId, n.notificationTagName, n.notificationDescription, " +
	                "n.createdOn, n.moduleName " +
	                "FROM Notification n " +
	                "WHERE ((n.customerId IS NULL OR n.customerId = '') AND n.sendStatus = 'N') " +
	                "OR (n.customerId = :customerId AND n.sendStatus = 'N') " +
	                "AND n.createdOn >= :sevenDaysAgo " +
	                "ORDER BY n.createdOn DESC";

	        Query query = entityManager.createQuery(notificationQuery);
	        query.setParameter("customerId", customerId);
	        query.setParameter("sevenDaysAgo", sevenDaysAgo);

	        List<Object[]> resultList = query.getResultList();
	        for (Object[] row : resultList) {
	            JSONObject NotifyJson = new JSONObject();
	            NotifyJson.put("NotiId", row[0].toString());
	            NotifyJson.put("NotiTagName", row[1].toString());
	            String notiDes = row[2].toString().replace("|br|", "\n");
	            NotifyJson.put("NotiDesc", notiDes);
	            
	            // Format the date in Java
	            LocalDateTime createdOn = (LocalDateTime) row[3];
	            String formattedDate = createdOn.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss"));
	            NotifyJson.put("CreadtedDate", formattedDate);

	            NotifyJson.put("ReadFlag", false);
	            NotifyJson.put("ModuleName", row[4].toString());
	            notiArray.add(NotifyJson);
	        }

	        // JPQL query to count unread messages
	        String unreadCountQuery = "SELECT COUNT(o) FROM Offer o WHERE o.readFlag = 'N' " +
	                                  "AND o.createdOn >= :sevenDaysAgo " +
	                                  "AND o.customerId = :customerId";
	        Query countQuery = entityManager.createQuery(unreadCountQuery);
	        countQuery.setParameter("customerId", customerId);
	        countQuery.setParameter("sevenDaysAgo", sevenDaysAgo);

	        int inboxCount = ((Number) countQuery.getSingleResult()).intValue();
	        finalJson.put("InboxCount", inboxCount);

	        finalJson.put("NotificationInfo", notiArray);
	        m_res_header.put("ResCode", "0");
	        m_res_header.put("ResBody", finalJson);

	    } catch (Exception e) {
	        e.printStackTrace();
	        // Handle exception, log it, etc.
	    }

	    return m_res_header;
	}


}
