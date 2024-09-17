package com.credentek.msme.loginmodel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "COM_RNB_USER_MST")
public class COMRnbUserMst {

    @Id
    @Column(name = "RNBID")
    private String rnbId;

	@Column(name = "RNB_MOB_NO")
    private String rnbMobNo;

    @Column(name = "OTP_VALUE")
    private String otpValue;
    
    @Column(name = "OTP_KEY")
    private String otpKey;


    public String getOtpKey() {
		return otpKey;
	}

	public void setOtpKey(String otpKey) {
		this.otpKey = otpKey;
	}

	
    public String getRnbId() {
		return rnbId;
	}

	public void setRnbId(String rnbId) {
		this.rnbId = rnbId;
	}

	public String getRnbMobNo() {
		return rnbMobNo;
	}

	public void setRnbMobNo(String rnbMobNo) {
		this.rnbMobNo = rnbMobNo;
	}

	public String getOtpValue() {
		return otpValue;
	}

	public void setOtpValue(String otpValue) {
		this.otpValue = otpValue;
	}

    // Getters and Setters
}