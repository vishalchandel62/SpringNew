package com.credentek.msme.loginmodel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "SME_CUST_INFO_MAIL_OTP")
public class CustInfoMailOtp {

    @Id
    @Column(name = "CUSTOMER_ID")
    private String customerId;

    public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getEmailOtpKey() {
		return emailOtpKey;
	}

	public void setEmailOtpKey(String emailOtpKey) {
		this.emailOtpKey = emailOtpKey;
	}

	public String getEmailOtpValue() {
		return emailOtpValue;
	}

	public void setEmailOtpValue(String emailOtpValue) {
		this.emailOtpValue = emailOtpValue;
	}

	public java.util.Date getEmailTriggeredDate() {
		return emailTriggeredDate;
	}

	public void setEmailTriggeredDate(java.util.Date emailTriggeredDate) {
		this.emailTriggeredDate = emailTriggeredDate;
	}

	@Column(name = "EMAIL_OTP_KEY")
    private String emailOtpKey;

    @Column(name = "EMAIL_OTP_VALUE")
    private String emailOtpValue;

    @Column(name = "EMAIL_TRIGGERED_DT")
    private java.util.Date emailTriggeredDate;

    // Getters and Setters
}