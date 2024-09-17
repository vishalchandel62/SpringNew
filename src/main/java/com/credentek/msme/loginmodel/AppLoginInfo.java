package com.credentek.msme.loginmodel;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "SME_APP_LOGIN_INFO")
public class AppLoginInfo {
    @Id
    @Column(name = "REG_ID")
    private String regId;
    
    @Column(name = "LOGIN_COUNT")
    private int loginCount;
    
    @Column(name = "USER_LOCKED")
    private String userLocked;
    
    @Column(name = "FIRST_LOGIN")
    private String firstLogin;
    
    @Column(name = "LOGOUT_MODE")
    private String logoutMode;
    
    @Column(name = "SESSION_ID")
    private String sessionId;
//
//    @Column(name = "LOGOUT_COUNT")
//    private int logOutCount;

    public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Column(name = "PORTAL_PASS")
    private String portalPass;
	
	
	@Column(name = "USERNAME")
    private String username;

    @Column(name = "LOGOUT_DATE")
    private Date logoutDate;

    @Column(name = "USER_BLOCKED")
    private String userBlock;
    
    @Column(name = "LAST_FAILURE_ATTEMPT_TIME")
    private Date lastFailureAttemptTime;

private String userLocation;

    public String getUserLocation() {
	return userLocation;
}
public void setUserLocation(String userLocation) {
	this.userLocation = userLocation;
}
	public Date getLastFailureAttemptTime() {
		return lastFailureAttemptTime;
	}
	public void setLastFailureAttemptTime(Date lastFailureAttemptTime) {
		this.lastFailureAttemptTime = lastFailureAttemptTime;
	}


    
    private String LastLogin;
    
	 public String getLastLogin() {
		return LastLogin;
	}
	public void setLastLogin(String lastLogin) {
		LastLogin = lastLogin;
	}
    
    public String getRegId() {
		return regId;
	}

	public String getLogoutMode() {
		return logoutMode;
	}

	public void setLogoutMode(String logoutMode) {
		this.logoutMode = logoutMode;
	}

//	public int getLogOutCount() {
//		return logOutCount;
//	}
//
//	public void setLogOutCount(int logOutCount) {
//		this.logOutCount = logOutCount;
//	}

	public String getPortalPass() {
		return portalPass;
	}

	public void setPortalPass(String portalPass) {
		this.portalPass = portalPass;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getLogoutDate() {
		return logoutDate;
	}

	public void setLogoutDate(Date logoutDate) {
		this.logoutDate = logoutDate;
	}

	public String getUserBlock() {
		return userBlock;
	}

	public void setUserBlock(String userBlock) {
		this.userBlock = userBlock;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}

	public int getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
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

	
	
	
	
    


   
    
}
