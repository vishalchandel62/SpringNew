package com.credentek.msme.loginmodel;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "SME_REG_USER_INFO_TMP", schema = "YES_SME1")
public class SmeRegUserInfoTmp implements Serializable {

    @Id
    @Column(name = "CUST_ID", nullable = false, length = 20)
    private String custId;

    @Column(name = "DEBIT_CARD_INFO", length = 16)
    private String debitCardInfo;

    @Column(name = "CUST_PASS", length = 50)
    private String custPass;

    @Column(name = "CUST_NAME", length = 100)
    private String custName;

    @Column(name = "CUST_ADD", length = 200)
    private String custAdd;

    @Column(name = "COMPANY_ID", length = 10)
    private String companyId;

    @Column(name = "COMPANY_NAME", length = 200)
    private String companyName;

    @Column(name = "RM_ID", length = 20)
    private String rmId;

    @Column(name = "HAS_MAKER", length = 1)
    private String hasMaker;

    @Column(name = "REGISTERED_DT")
    private Date registeredDt;

    @Column(name = "MPIN")
    private Integer mpin;

    @Column(name = "APIN")
    private Integer apin;

    @Column(name = "REG_ID", length = 20)
    private String regId;

    @Column(name = "REG_MODE", length = 10)
    private String regMode;

    // Getters and Setters
    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getDebitCardInfo() {
        return debitCardInfo;
    }

    public void setDebitCardInfo(String debitCardInfo) {
        this.debitCardInfo = debitCardInfo;
    }

    public String getCustPass() {
        return custPass;
    }

    public void setCustPass(String custPass) {
        this.custPass = custPass;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustAdd() {
        return custAdd;
    }

    public void setCustAdd(String custAdd) {
        this.custAdd = custAdd;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRmId() {
        return rmId;
    }

    public void setRmId(String rmId) {
        this.rmId = rmId;
    }

    public String getHasMaker() {
        return hasMaker;
    }

    public void setHasMaker(String hasMaker) {
        this.hasMaker = hasMaker;
    }

    public Date getRegisteredDt() {
        return registeredDt;
    }

    public void setRegisteredDt(Date registeredDt) {
        this.registeredDt = registeredDt;
    }

    public Integer getMpin() {
        return mpin;
    }

    public void setMpin(Integer mpin) {
        this.mpin = mpin;
    }

    public Integer getApin() {
        return apin;
    }

    public void setApin(Integer apin) {
        this.apin = apin;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getRegMode() {
        return regMode;
    }

    public void setRegMode(String regMode) {
        this.regMode = regMode;
    }
}
