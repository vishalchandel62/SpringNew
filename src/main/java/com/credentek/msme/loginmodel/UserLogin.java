package com.credentek.msme.loginmodel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class UserLogin {
	@Id
	private Long id;
	private String loginID;
	private String debitCardNo;
	private String retailBankUserID;
	private String debitCardPin;
	private String password;
	private String MPIN;
	private String APIN;
	private String IMEI_NO;
	private String deviceID;
	private String deviceName;
	private String deviceVer;
	private String devicePlat;
	private String token;
	private String shareKey;
	private String userName;
	private String userContactNo;
	private String userEmail;
	private String serviceRM;
	private String assetRM;
	private String SSC;
	private String deregFlag;
	private String servRMmob;
	private String servRMemail;
	private String assetRMmob;
	private String assetRMemail;
	private String companyID;
	private String companyIDForAlisId;
	private String userType;
	private String regID;
	private String loginTypeFlag;
	private String loginDate;
	private String logOutDate;
	private String logOutMode;
	private String exceptionCd;
	private String deviceDate;
	private String lastReqTime;
	private String lastFailureAttemptTime;
	private String firstLogin;
	private String sessionID;
	private String expiryDate;
	private String ipAddress;
	private String loginReqMode;
	private String userRegisterType;
	private String custPanNo;
	private String custDOB;
	private String otpKey;
	private String keyFromUIEncrypted;
	

	public String getKeyFromUIEncrypted() {
		return keyFromUIEncrypted;
	}
	public void setKeyFromUIEncrypted(String keyFromUIEncrypted) {
		this.keyFromUIEncrypted = keyFromUIEncrypted;
	}
	private String LastLogin;
	 public String getLastLogin() {
		return LastLogin;
	}
	public void setLastLogin(String lastLogin) {
		LastLogin = lastLogin;
	}

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	private String assetRMName;
	private String serviceRMName;

	public String getUserRegisterType() {
		return userRegisterType;
	}
	public void setUserRegisterType(String userRegisterType) {
		this.userRegisterType = userRegisterType;
	}
	public String getAssetRMName() {
		return assetRMName;
	}
	public void setAssetRMName(String assetRMName) {
		this.assetRMName = assetRMName;
	}
	public String getServiceRMName() {
		return serviceRMName;
	}
	public void setServiceRMName(String serviceRMName) {
		this.serviceRMName = serviceRMName;
	}
	public String getOtpKey() {
		return otpKey;
	}
	public void setOtpKey(String otpKey) {
		this.otpKey = otpKey;
	}
	public String getCustPanNo() {
		return custPanNo;
	}
	public void setCustPanNo(String custPanNo) {
		this.custPanNo = custPanNo;
	}
	public String getCustDOB() {
		return custDOB;
	}
	public void setCustDOB(String custDOB) {
		this.custDOB = custDOB;
	}
	public String getLoginReqMode() {
		return loginReqMode;
	}
	public void setLoginReqMode(String loginReqMode) {
		this.loginReqMode = loginReqMode;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getSessionID() {
		return sessionID;
	}
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	public String getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(String loginDate) {
		this.loginDate = loginDate;
	}
	public String getRegID() {
		return regID;
	}
	public void setRegID(String regID) {
		this.regID = regID;
	}
	public String getLoginTypeFlag() {
		return loginTypeFlag;
	}
	public void setLoginTypeFlag(String loginTypeFlag) {
		this.loginTypeFlag = loginTypeFlag;
	}
	
	public String getLogOutDate() {
		return logOutDate;
	}
	public void setLogOutDate(String logOutDate) {
		this.logOutDate = logOutDate;
	}
	public String getLogOutMode() {
		return logOutMode;
	}
	public void setLogOutMode(String logOutMode) {
		this.logOutMode = logOutMode;
	}
	public String getExceptionCd() {
		return exceptionCd;
	}
	public void setExceptionCd(String exceptionCd) {
		this.exceptionCd = exceptionCd;
	}
	public String getDeviceDate() {
		return deviceDate;
	}
	public void setDeviceDate(String deviceDate) {
		this.deviceDate = deviceDate;
	}
	public String getLastReqTime() {
		return lastReqTime;
	}
	public void setLastReqTime(String lastReqTime) {
		this.lastReqTime = lastReqTime;
	}
	public String getLastFailureAttemptTime() {
		return lastFailureAttemptTime;
	}
	public void setLastFailureAttemptTime(String lastFailureAttemptTime) {
		this.lastFailureAttemptTime = lastFailureAttemptTime;
	}
	public String getFirstLogin() {
		return firstLogin;
	}
	public void setFirstLogin(String firstLogin) {
		this.firstLogin = firstLogin;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getCompanyIDForAlisId() {
		return companyIDForAlisId;
	}
	public void setCompanyIDForAlisId(String companyIDForAlisId) {
		this.companyIDForAlisId = companyIDForAlisId;
	}
	public String getCompanyID() {
		return companyID;
	}
	public void setCompanyID(String companyID) {
		this.companyID = companyID;
	}
	public String getServRMmob() {
		return servRMmob;
	}
	public void setServRMmob(String servRMmob) {
		this.servRMmob = servRMmob;
	}
	public String getServRMemail() {
		return servRMemail;
	}
	public void setServRMemail(String servRMemail) {
		this.servRMemail = servRMemail;
	}
	public String getAssetRMmob() {
		return assetRMmob;
	}
	public void setAssetRMmob(String assetRMmob) {
		this.assetRMmob = assetRMmob;
	}
	public String getAssetRMemail() {
		return assetRMemail;
	}
	public void setAssetRMemail(String assetRMemail) {
		this.assetRMemail = assetRMemail;
	}
	public String getDeregFlag() {
		return deregFlag;
	}
	public void setDeregFlag(String deregFlag) {
		this.deregFlag = deregFlag;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserContactNo() {
		return userContactNo;
	}
	public void setUserContactNo(String userContactNo) {
		this.userContactNo = userContactNo;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getServiceRM() {
		return serviceRM;
	}
	public void setServiceRM(String serviceRM) {
		this.serviceRM = serviceRM;
	}
	public String getAssetRM() {
		return assetRM;
	}
	public void setAssetRM(String assetRM) {
		this.assetRM = assetRM;
	}
	public String getSSC() {
		return SSC;
	}
	public void setSSC(String sSC) {
		SSC = sSC;
	}
	public String getLoginID() {
		return loginID;
	}
	public void setLoginID(String loginID) {
		this.loginID = loginID;
	}
	public String getDebitCardNo() {
		return debitCardNo;
	}
	public void setDebitCardNo(String debitCardNo) {
		this.debitCardNo = debitCardNo;
	}
	public String getRetailBankUserID() {
		return retailBankUserID;
	}
	public void setRetailBankUserID(String retailBankUserID) {
		this.retailBankUserID = retailBankUserID;
	}
	public String getDebitCardPin() {
		return debitCardPin;
	}
	public void setDebitCardPin(String debitCardPin) {
		this.debitCardPin = debitCardPin;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMPIN() {
		return MPIN;
	}
	public void setMPIN(String mPIN) {
		MPIN = mPIN;
	}
	
	public String getShareKey() {
		return shareKey;
	}
	public void setShareKey(String shareKey) {
		this.shareKey = shareKey;
	}
	public String getAPIN() {
		return APIN;
	}
	public void setAPIN(String aPIN) {
		APIN = aPIN;
	}
	public String getIMEI_NO() {
		return IMEI_NO;
	}
	public void setIMEI_NO(String iMEI_NO) {
		IMEI_NO = iMEI_NO;
	}
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getDeviceVer() {
		return deviceVer;
	}
	public void setDeviceVer(String deviceVer) {
		this.deviceVer = deviceVer;
	}
	public String getDevicePlat() {
		return devicePlat;
	}
	public void setDevicePlat(String devicePlat) {
		this.devicePlat = devicePlat;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	@Override
	public String toString() {
		return "UserLogin [debitCardNo=" + debitCardNo + ", retailBankUserID=" + retailBankUserID + ", debitCardPin="
				+ debitCardPin + ", password=" + password + ", MPIN=" + MPIN + ", APIN=" + APIN + ", IMEI_NO=" + IMEI_NO
				+ ", deviceID=" + deviceID + ", deviceName=" + deviceName + ", deviceVer=" + deviceVer + ", devicePlat="
				+ devicePlat + ", token=" + token + ", loginID=" + loginID + ", shareKey=" + shareKey + ", userName="
				+ userName + ", userContactNo=" + userContactNo + ", userEmail=" + userEmail + ", serviceRM="
				+ serviceRM + ", assetRM=" + assetRM + ", SSC=" + SSC + ", deregFlag=" + deregFlag + ", servRMmob="
				+ servRMmob + ", servRMemail=" + servRMemail + ", assetRMmob=" + assetRMmob + ", assetRMemail="
				+ assetRMemail + ", companyID=" + companyID + ", companyIDForAlisId=" + companyIDForAlisId
				+ ", userType=" + userType + ", regID=" + regID + ", loginTypeFlag=" + loginTypeFlag + ", loginDate="
				+ loginDate + ", logOutDate=" + logOutDate + ", logOutMode=" + logOutMode + ", exceptionCd="
				+ exceptionCd + ", deviceDate=" + deviceDate + ", lastReqTime=" + lastReqTime
				+ ", lastFailureAttemptTime=" + lastFailureAttemptTime + ", firstLogin=" + firstLogin + ", sessionID="
				+ sessionID + ", expiryDate=" + expiryDate + ", ipAddress=" + ipAddress + ", loginReqMode="
				+ loginReqMode + ", custPanNo=" + custPanNo + ", custDOB=" + custDOB + ", otpKey=" + otpKey + "]";
	}
	
	
	
	
	
	

}
