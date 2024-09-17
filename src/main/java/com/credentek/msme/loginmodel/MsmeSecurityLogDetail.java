package com.credentek.msme.loginmodel;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "MSME_SECURITYLOG_DTL", schema = "YES_SME1")
public class MsmeSecurityLogDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "logDetailSeq")
    @SequenceGenerator(name = "logDetailSeq", sequenceName = "msme_securitylog_dtl_seq", allocationSize = 1)
    @Column(name = "SEQ")
    private Long seq;

    @Column(name = "USERID", length = 24)
    private String userId;

   // @Column(name = "LOGINDT")
  //  private Instant loginDt;

   // @Column(name = "REQUESTTIME")
    //private Instant requestTime;

   // @Column(name = "RESPONSETIME")
   // private Instant responseTime;
    
    @Column(name = "LOGINDT")
      private Timestamp loginDt;
    
    @Column(name = "REQUESTTIME")
    private Timestamp requesttime;

    @Column(name = "RESPONSETIME")
    private Timestamp responseTime;


    @Column(name = "SERVICEID", length = 100)
    private String serviceId;

    @Column(name = "TIMEDIFFERENCE", length = 20)
    private String timeDifference;

    @Column(name = "SESSION_ACTIVE", length = 1, columnDefinition = "VARCHAR2(1 BYTE) DEFAULT 'N'")
    private String sessionActive;

    @Column(name = "HOST_SESSIONID", length = 500)
    private String hostSessionId;

    // Getters and Setters

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

//    public Instant getLoginDt() {
//        return loginDt;
//    }
//
//    public void setLoginDt(Instant loginDt) {
//        this.loginDt = loginDt;
//    }
//
//    public Instant getRequestTime() {
//        return requestTime;
//    }
//
//    public void setRequestTime(Instant requestTime) {
//        this.requestTime = requestTime;
//    }
//
//    public Instant getResponseTime() {
//        return responseTime;
//    }
//
//    public void setResponseTime(Instant responseTime) {
//        this.responseTime = responseTime;
//    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getTimeDifference() {
        return timeDifference;
    }

    public void setTimeDifference(String timeDifference) {
        this.timeDifference = timeDifference;
    }

    public String getSessionActive() {
        return sessionActive;
    }

    public void setSessionActive(String sessionActive) {
        this.sessionActive = sessionActive;
    }

    public Timestamp getLoginDt() {
		return loginDt;
	}

	public void setLoginDt(Timestamp loginDt) {
		this.loginDt = loginDt;
	}

	public Timestamp getRequesttime() {
		return requesttime;
	}

	public void setRequesttime(Timestamp requesttime) {
		this.requesttime = requesttime;
	}

	public Timestamp getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Timestamp responseTime) {
		this.responseTime = responseTime;
	}

	public String getHostSessionId() {
        return hostSessionId;
    }

    public void setHostSessionId(String hostSessionId) {
        this.hostSessionId = hostSessionId;
    }
}
