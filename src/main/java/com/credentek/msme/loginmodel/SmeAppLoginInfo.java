package com.credentek.msme.loginmodel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "SME_APP_LOGIN_INFO", schema = "YES_SME1")
public class SmeAppLoginInfo {

    @Id
    @Column(name = "LOGIN_ID", length = 80, nullable = false)
    private String loginId;
    
   
    @Column(name = "LOGIN_ADDED_ID")
    private Long loginAddedId;


    @Column(name = "USERNAME", length = 50)
    private String username;

    @Column(name = "LOGIN_DATE")
    private Timestamp loginDate;

    @Column(name = "LOGOUT_DATE")
    private Timestamp logoutDate;

    @Column(name = "LOGOUT_MODE", length = 20)
    private String logoutMode = "T"; // Default value

    @Column(name = "EXCEPTION_CD", length = 50)
    private String exceptionCd;

    @Column(name = "PASSWORD", length = 50)
    private String password;

    @Column(name = "DEVICE_NAME", length = 100)
    private String deviceName;

    @Column(name = "DEVICE_PLATFORM", length = 100)
    private String devicePlatform;

    @Column(name = "DEVICE_UUID", length = 50)
    private String deviceUuid;

    @Column(name = "DEVICE_VERSION", length = 20)
    private String deviceVersion;

    @Column(name = "DEVICE_DATE", length = 20)
    private String deviceDate;

    @Column(name = "DEVICE_IMEI", length = 50)
    private String deviceImei;

    @Column(name = "TOKEN", length = 100)
    private String token = "NA"; // Default value

    @Column(name = "LOGIN_NAME", length = 50)
    private String loginName;

    @Column(name = "LAST_REQUEST_TIME")
    private Timestamp lastRequestTime;

    @Column(name = "REG_ID", length = 20)
    private String regId;

    @Column(name = "LOGIN_COUNT")
    private Integer loginCount = 0; // Default value

    @Column(name = "LAST_FAILURE_ATTEMPT_TIME")
    private Timestamp lastFailureAttemptTime;

    @Column(name = "USER_LOCKED", length = 1)
    private String userLocked = "N"; // Default value

    @Column(name = "FIRST_LOGIN", length = 1)
    private String firstLogin = "N"; // Default value

    @Column(name = "MPIN_COUNT")
    private Integer mpinCount = 0; // Default value

    @Column(name = "PORTAL_PASS", length = 100)
    private String portalPass;

    @Column(name = "USER_TYPE", length = 20)
    private String userType;

    @Column(name = "SESSION_ID", length = 100)
    private String sessionId;

    @Column(name = "LOGIN_TYPE_MOBILE", length = 1)
    private String loginTypeMobile = "N"; // Default value

    @Column(name = "LOGIN_TYPE_PORTAL", length = 1)
    private String loginTypePortal = "N"; // Default value

    @Column(name = "REQ_TOKEN", length = 100)
    private String reqToken;

   
    @Column(name = "ISMDMSEARCHFLAG", length = 1)
    private String ismdmsearchflag = "N"; // Default value

    @Column(name = "USER_BLOCKED", length = 1)
    private String userBlocked = "N"; // Default value

    @Column(name = "NEW_ENC_FLAG", length = 1)
    private String newEncFlag;

    @Column(name = "NEW_ENC_DATE")
    private Timestamp newEncDate;

    @Column(name = "WRONG_OTP_ATTEMPT")
    private Integer wrongOtpAttempt = 0; // Default value

    @Column(name = "WRONG_OTP_ATTEMPT_DATE_TIME")
    private Timestamp wrongOtpAttemptDateTime;
    
 

    // Getters and Setters
    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Timestamp loginDate) {
        this.loginDate = loginDate;
    }

    public Timestamp getLogoutDate() {
        return logoutDate;
    }

    public void setLogoutDate(Timestamp logoutDate) {
        this.logoutDate = logoutDate;
    }

    public String getLogoutMode() {
        return logoutMode;
    }

    public void setLogoutMode(String logoutMode) {
        this.logoutMode = logoutMode;
    }

    public String getExceptionCd() {
        return exceptionCd;
    }

    public void setExceptionCd(String exceptionCd) {
        this.exceptionCd = exceptionCd;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDevicePlatform() {
        return devicePlatform;
    }

    public void setDevicePlatform(String devicePlatform) {
        this.devicePlatform = devicePlatform;
    }

    public String getDeviceUuid() {
        return deviceUuid;
    }

    public void setDeviceUuid(String deviceUuid) {
        this.deviceUuid = deviceUuid;
    }

    public String getDeviceVersion() {
        return deviceVersion;
    }

    public void setDeviceVersion(String deviceVersion) {
        this.deviceVersion = deviceVersion;
    }

    public String getDeviceDate() {
        return deviceDate;
    }

    public void setDeviceDate(String deviceDate) {
        this.deviceDate = deviceDate;
    }

    public String getDeviceImei() {
        return deviceImei;
    }

    public void setDeviceImei(String deviceImei) {
        this.deviceImei = deviceImei;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Timestamp getLastRequestTime() {
        return lastRequestTime;
    }

    public void setLastRequestTime(Timestamp lastRequestTime) {
        this.lastRequestTime = lastRequestTime;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public Timestamp getLastFailureAttemptTime() {
        return lastFailureAttemptTime;
    }

    public void setLastFailureAttemptTime(Timestamp lastFailureAttemptTime) {
        this.lastFailureAttemptTime = lastFailureAttemptTime;
    }

    public String getUserLocked() {
        return userLocked;
    }

    public void setUserLocked(String userLocked) {
        this.userLocked = userLocked;
    }

    public String getFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(String firstLogin) {
        this.firstLogin = firstLogin;
    }

    public Integer getMpinCount() {
        return mpinCount;
    }

    public void setMpinCount(Integer mpinCount) {
        this.mpinCount = mpinCount;
    }

    public String getPortalPass() {
        return portalPass;
    }

    public void setPortalPass(String portalPass) {
        this.portalPass = portalPass;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getLoginTypeMobile() {
        return loginTypeMobile;
    }

    public void setLoginTypeMobile(String loginTypeMobile) {
        this.loginTypeMobile = loginTypeMobile;
    }

    public String getLoginTypePortal() {
        return loginTypePortal;
    }

    public void setLoginTypePortal(String loginTypePortal) {
        this.loginTypePortal = loginTypePortal;
    }

    public String getReqToken() {
        return reqToken;
    }

    public void setReqToken(String reqToken) {
        this.reqToken = reqToken;
    }

    public String getUserBlocked() {
		return userBlocked;
	}

	public void setUserBlocked(String userBlocked) {
		this.userBlocked = userBlocked;
	}

	public String getNewEncFlag() {
		return newEncFlag;
	}

	public void setNewEncFlag(String newEncFlag) {
		this.newEncFlag = newEncFlag;
	}

	public Timestamp getNewEncDate() {
		return newEncDate;
	}

	public void setNewEncDate(Timestamp newEncDate) {
		this.newEncDate = newEncDate;
	}

	public Integer getWrongOtpAttempt() {
		return wrongOtpAttempt;
	}

	public void setWrongOtpAttempt(Integer wrongOtpAttempt) {
		this.wrongOtpAttempt = wrongOtpAttempt;
	}

	public Timestamp getWrongOtpAttemptDateTime() {
		return wrongOtpAttemptDateTime;
	}

	public void setWrongOtpAttemptDateTime(Timestamp wrongOtpAttemptDateTime) {
		this.wrongOtpAttemptDateTime = wrongOtpAttemptDateTime;
	}

	public Long getLoginAddedId() {
        return loginAddedId;
    }

    public void setLoginAddedId(Long loginAddedId) {
        this.loginAddedId = loginAddedId;
    }

    public String getIsmdmsearchflag() {
        return ismdmsearchflag;
    }

    public void setIsmdmsearchflag(String ismdmsearchflag) {
        this.ismdmsearchflag = ismdmsearchflag;
    }
  


}