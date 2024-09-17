package com.credentek.msme.loginmodel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "SME_APP_LOGIN_INFO_TMP", schema = "YES_SME1")
public class SmeAppLoginInfoTmp implements Serializable {

    @Id
    @Column(name = "LOGIN_ID", nullable = false, length = 80)
    private String loginId;

    @Column(name = "USERNAME", length = 50)
    private String username;

    @Column(name = "LOGIN_DATE")
    private LocalDateTime loginDate;

    @Column(name = "LOGOUT_DATE")
    private LocalDateTime logoutDate;

    @Column(name = "LOGOUT_MODE", length = 20)
    private String logoutMode;

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
    private String token;

    @Column(name = "LOGIN_NAME", length = 50)
    private String loginName;

    @Column(name = "LAST_REQUEST_TIME")
    private LocalDateTime lastRequestTime;

    @Column(name = "REG_ID", length = 20)
    private String regId;

    @Column(name = "LOGIN_COUNT")
    private Integer loginCount;

    @Column(name = "LAST_FAILURE_ATTEMPT_TIME")
    private LocalDateTime lastFailureAttemptTime;

    @Column(name = "USER_LOCKED", length = 1)
    private String userLocked;

    @Column(name = "FIRST_LOGIN", length = 1)
    private String firstLogin;

    @Column(name = "MPIN_COUNT")
    private Integer mpinCount;

    @Column(name = "PORTAL_PASS", length = 100)
    private String portalPass;

    @Column(name = "USER_TYPE", length = 20)
    private String userType;

    @Column(name = "SESSION_ID", length = 100)
    private String sessionId;

    @Column(name = "LOGIN_TYPE_MOBILE", length = 1)
    private String loginTypeMobile;

    @Column(name = "LOGIN_TYPE_PORTAL", length = 1)
    private String loginTypePortal;

    @Column(name = "REQ_TOKEN", length = 100)
    private String reqToken;

    @Column(name = "LOGIN_ADDED_ID")
    private Long loginAddedId;

    @Column(name = "ISMDMSEARCHFLAG", length = 1)
    private String isMdmSearchFlag;

    @Column(name = "USER_BLOCKED", length = 1)
    private String userBlocked;

    @Column(name = "NEW_ENC_FLAG", length = 1)
    private String newEncFlag;

    @Column(name = "NEW_ENC_DATE")
    private LocalDateTime newEncDate;

    @Column(name = "WRONG_OTP_ATTEMPT")
    private Integer wrongOtpAttempt;

    @Column(name = "WRONG_OTP_ATTEMPT_DATE_TIME")
    private LocalDateTime wrongOtpAttemptDateTime;

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

    public LocalDateTime getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(LocalDateTime loginDate) {
        this.loginDate = loginDate;
    }

    public LocalDateTime getLogoutDate() {
        return logoutDate;
    }

    public void setLogoutDate(LocalDateTime logoutDate) {
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

    public LocalDateTime getLastRequestTime() {
        return lastRequestTime;
    }

    public void setLastRequestTime(LocalDateTime lastRequestTime) {
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

    public LocalDateTime getLastFailureAttemptTime() {
        return lastFailureAttemptTime;
    }

    public void setLastFailureAttemptTime(LocalDateTime lastFailureAttemptTime) {
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
}
    
