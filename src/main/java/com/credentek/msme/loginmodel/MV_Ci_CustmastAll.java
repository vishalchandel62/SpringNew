package com.credentek.msme.loginmodel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "MV_CI_CUSTMAST_ALL", schema = "YES_SME1")
public class MV_Ci_CustmastAll {

    @Id
    @Column(name = "COD_CUST_ID")
    private BigDecimal codCustId;

    @Column(name = "NAM_CUST_FULL", length = 300)
    private String namCustFull;

    @Column(name = "REF_CUST_TELEX", length = 15)
    private String refCustTelex;

    @Column(name = "REF_CUST_EMAIL", length = 100)
    private String refCustEmail;

    @Column(name = "SSC", length = 200)
    private String ssc;

    @Column(name = "SERVICE_RM", length = 100)
    private String serviceRm;

    @Column(name = "ASSET_RM")
    private BigDecimal assetRm;

    @Column(name = "SERVICE_RM_EMAILID", length = 100)
    private String serviceRmEmailId;

    @Column(name = "SERVICE_RM_MOB", length = 20)
    private String serviceRmMob;

    @Column(name = "ASSET_RM_MOB", length = 20)
    private String assetRmMob;

    @Column(name = "ASSET_RM_EMAILID", length = 100)
    private String assetRmEmailId;

    @Column(name = "ASSET_RM_NAME", length = 100)
    private String assetRmName;

    @Column(name = "SERVICE_RM_NAME", length = 100)
    private String serviceRmName;

    @Column(name = "TXT_CUSTADR_ADD1", length = 105)
    private String txtCustadrAdd1;

    @Column(name = "TXT_CUSTADR_ADD2", length = 105)
    private String txtCustadrAdd2;

    @Column(name = "TXT_CUSTADR_ADD3", length = 105)
    private String txtCustadrAdd3;

    @Column(name = "NAM_CUSTADR_CITY", length = 105)
    private String namCustadrCity;

    @Column(name = "NAM_CUSTADR_STATE", length = 105)
    private String namCustadrState;

    @Column(name = "NAM_CUSTADR_CNTRY", length = 105)
    private String namCustadrCntry;

    @Column(name = "TXT_CUSTADR_ZIP", length = 105)
    private String txtCustadrZip;

    @Column(name = "COD_CC_BRN")
    private BigDecimal codCcBrn;

    @Column(name = "COD_CC_HOMEBRN")
    private BigDecimal codCcHomeBrn;

    @Column(name = "NAM_PERMADR_CNTRY", length = 20)
    private String namPermadrCntry;

    @Column(name = "TXT_PERMADR_ZIP", length = 20)
    private String txtPermadrZip;

    // Getters and Setters

    public BigDecimal getCodCustId() {
        return codCustId;
    }

    public void setCodCustId(BigDecimal codCustId) {
        this.codCustId = codCustId;
    }

    public String getNamCustFull() {
        return namCustFull;
    }

    public void setNamCustFull(String namCustFull) {
        this.namCustFull = namCustFull;
    }

    public String getRefCustTelex() {
        return refCustTelex;
    }

    public void setRefCustTelex(String refCustTelex) {
        this.refCustTelex = refCustTelex;
    }

    public String getRefCustEmail() {
        return refCustEmail;
    }

    public void setRefCustEmail(String refCustEmail) {
        this.refCustEmail = refCustEmail;
    }

    public String getSsc() {
        return ssc;
    }

    public void setSsc(String ssc) {
        this.ssc = ssc;
    }

    public String getServiceRm() {
        return serviceRm;
    }

    public void setServiceRm(String serviceRm) {
        this.serviceRm = serviceRm;
    }

    public BigDecimal getAssetRm() {
        return assetRm;
    }

    public void setAssetRm(BigDecimal assetRm) {
        this.assetRm = assetRm;
    }

    public String getServiceRmEmailId() {
        return serviceRmEmailId;
    }

    public void setServiceRmEmailId(String serviceRmEmailId) {
        this.serviceRmEmailId = serviceRmEmailId;
    }

    public String getServiceRmMob() {
        return serviceRmMob;
    }

    public void setServiceRmMob(String serviceRmMob) {
        this.serviceRmMob = serviceRmMob;
    }

    public String getAssetRmMob() {
        return assetRmMob;
    }

    public void setAssetRmMob(String assetRmMob) {
        this.assetRmMob = assetRmMob;
    }

    public String getAssetRmEmailId() {
        return assetRmEmailId;
    }

    public void setAssetRmEmailId(String assetRmEmailId) {
        this.assetRmEmailId = assetRmEmailId;
    }

    public String getAssetRmName() {
        return assetRmName;
    }

    public void setAssetRmName(String assetRmName) {
        this.assetRmName = assetRmName;
    }

    public String getServiceRmName() {
        return serviceRmName;
    }

    public void setServiceRmName(String serviceRmName) {
        this.serviceRmName = serviceRmName;
    }

    public String getTxtCustadrAdd1() {
        return txtCustadrAdd1;
    }

    public void setTxtCustadrAdd1(String txtCustadrAdd1) {
        this.txtCustadrAdd1 = txtCustadrAdd1;
    }

    public String getTxtCustadrAdd2() {
        return txtCustadrAdd2;
    }

    public void setTxtCustadrAdd2(String txtCustadrAdd2) {
        this.txtCustadrAdd2 = txtCustadrAdd2;
    }

    public String getTxtCustadrAdd3() {
        return txtCustadrAdd3;
    }

    public void setTxtCustadrAdd3(String txtCustadrAdd3) {
        this.txtCustadrAdd3 = txtCustadrAdd3;
    }

    public String getNamCustadrCity() {
        return namCustadrCity;
    }

    public void setNamCustadrCity(String namCustadrCity) {
        this.namCustadrCity = namCustadrCity;
    
    }
    }
