package com.credentek.msme.loginmodel;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "SME_USER_DROP_OFF_DTL", schema = "YES_SME1")
@IdClass(SmeUserDropOffDtl.class)
public class SmeUserDropOffDtl implements Serializable {

    @Id
    @Column(name = "CUSTOMER_ID", length = 100)
    private String customerId;

    @Id
    @Column(name = "COMPANY_ID", length = 100)
    private String companyId;

    @Id
    @Column(name = "REG_MODE_USED", length = 200)
    private String regModeUsed;

    @Id
    @Column(name = "TRANSACTION_CHANNEL", length = 100)
    private String transactionChannel;

    @Column(name = "REQUEST_DT")
    private LocalDateTime requestDt;

    @Column(name = "ERROR_LOG", length = 1000)
    private String errorLog;

    // Getters and Setters
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getRegModeUsed() {
        return regModeUsed;
    }

    public void setRegModeUsed(String regModeUsed) {
        this.regModeUsed = regModeUsed;
    }

    public String getTransactionChannel() {
        return transactionChannel;
    }

    public void setTransactionChannel(String transactionChannel) {
        this.transactionChannel = transactionChannel;
    }

    public LocalDateTime getRequestDt() {
        return requestDt;
    }

    public void setRequestDt(LocalDateTime requestDt) {
        this.requestDt = requestDt;
    }

    public String getErrorLog() {
        return errorLog;
    }

    public void setErrorLog(String errorLog) {
        this.errorLog = errorLog;
    }
}