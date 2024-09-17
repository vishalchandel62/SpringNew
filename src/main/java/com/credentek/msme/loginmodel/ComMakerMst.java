package com.credentek.msme.loginmodel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "COM_MAKER_MST", schema = "YES_SME1")
public class ComMakerMst {

    @Id
    @Column(name = "SMEMAKERCD", length = 80, nullable = false)
    private String sMeMakerCd;

    @Column(name = "SMEMAKERNAME", length = 35)
    private String sMeMakerName;

    @Column(name = "SMEMAILID", length = 60)
    private String sMeEmailId;

    @Column(name = "MOBNO", length = 100)
    private String mobNo;

    @Column(name = "AUTHORIZER_CUST_ID", length = 35)
    private String authorizerCustId;

    @Column(name = "ID")
    private Long id;

    @Column(name = "AUTH_STATUS", length = 1)
    private String authStatus;

    @Column(name = "MAKER_CD", length = 10)
    private String makerCd;

    @Column(name = "MAKER_DT")
    private Date makerDt;

    @Column(name = "MAKER_CAL_DT")
    private Date makerCalDt;

    @Column(name = "AUTHOR_CD", length = 10)
    private String authorCd;

    @Column(name = "AUTHOR_DT")
    private Date authorDt;

    @Column(name = "AUTHOR_CAL_DT")
    private Date authorCalDt;

    @Column(name = "ACTIVE_FLAG", length = 1)
    private String activeFlag;

    @Column(name = "LAST_CHANGE_ID")
    private Long lastChangeId;

    @Column(name = "PASSWORD", length = 40)
    private String password;

    @Column(name = "PLAINTEXT", length = 20)
    private String plainText;

    @Column(name = "MAKER_ALIAS_NAME", length = 40)
    private String makerAliasName;

    @Column(name = "SALUTATION", length = 10)
    private String salutation;

    @Column(name = "TOKEN", length = 200)
    private String token;

    @Column(name = "INTIATED_TIME")
    private Timestamp initiatedTime;

    @Column(name = "FORGO_PASS_FLAG", length = 2)
    private String forgoPassFlag = "N"; // Default value

    @Column(name = "CNT_MAKER_HIT")
    private Long cntMakerHit = (long) 5; // Default value

    @Column(name = "TRANSACTIONCNT")
    private Long transactionCnt;

    @Column(name = "AUTHOR_TIME")
    private Timestamp authorTime;

    // Getters and Setters

    public String getSMEMAKERCD() {
        return sMeMakerCd;
    }

    public void setSMEMAKERCD(String sMeMakerCd) {
        this.sMeMakerCd = sMeMakerCd;
    }

    public String getSMEMAKERNAME() {
        return sMeMakerName;
    }

    public void setSMEMAKERNAME(String sMeMakerName) {
        this.sMeMakerName = sMeMakerName;
    }

    public String getSMEMAILID() {
        return sMeEmailId;
    }

    public void setSMEMAILID(String sMeEmailId) {
        this.sMeEmailId = sMeEmailId;
    }

    public String getMOBNO() {
        return mobNo;
    }

    public void setMOBNO(String mobNo) {
        this.mobNo = mobNo;
    }

    public String getAUTHORIZER_CUST_ID() {
        return authorizerCustId;
    }

    public void setAUTHORIZER_CUST_ID(String authorizerCustId) {
        this.authorizerCustId = authorizerCustId;
    }

    public Long getID() {
        return id;
    }

    public void setID(Long id) {
        this.id = id;
    }

    public String getAUTH_STATUS() {
        return authStatus;
    }

    public void setAUTH_STATUS(String authStatus) {
        this.authStatus = authStatus;
    }

    public String getMAKER_CD() {
        return makerCd;
    }

    public void setMAKER_CD(String makerCd) {
        this.makerCd = makerCd;
    }

    public Date getMAKER_DT() {
        return makerDt;
    }

    public void setMAKER_DT(Date makerDt) {
        this.makerDt = makerDt;
    }

    public Date getMAKER_CAL_DT() {
        return makerCalDt;
    }

    public void setMAKER_CAL_DT(Date makerCalDt) {
        this.makerCalDt = makerCalDt;
    }

    public String getAUTHOR_CD() {
        return authorCd;
    }

    public void setAUTHOR_CD(String authorCd) {
        this.authorCd = authorCd;
    }

    public Date getAUTHOR_DT() {
        return authorDt;
    }

    public void setAUTHOR_DT(Date authorDt) {
        this.authorDt = authorDt;
    }

    public Date getAUTHOR_CAL_DT() {
        return authorCalDt;
    }

    public void setAUTHOR_CAL_DT(Date authorCalDt) {
        this.authorCalDt = authorCalDt;
    }

    public String getACTIVE_FLAG() {
        return activeFlag;
    }

    public void setACTIVE_FLAG(String activeFlag) {
        this.activeFlag = activeFlag;
    }

    public Long getLAST_CHANGE_ID() {
        return lastChangeId;
    }

    public void setLAST_CHANGE_ID(Long lastChangeId) {
        this.lastChangeId = lastChangeId;
    }

    public String getPASSWORD() {
        return password;
    }

    public void setPASSWORD(String password) {
        this.password = password;
    }

    public String getPLAINTEXT() {
        return plainText;
    }

    public void setPLAINTEXT(String plainText) {
        this.plainText = plainText;
    }

    public String getMAKER_ALIAS_NAME() {
        return makerAliasName;
    }

    public void setMAKER_ALIAS_NAME(String makerAliasName) {
        this.makerAliasName = makerAliasName;
    }

    public String getSALUTATION() {
        return salutation;
    }

    public void setSALUTATION(String salutation) {
        this.salutation = salutation;
    }

    public String getTOKEN() {
        return token;
    }

    public void setTOKEN(String token) {
        this.token = token;
    }

    public Timestamp getINTIATED_TIME() {
        return initiatedTime;
    }

    public void setINTIATED_TIME(Timestamp initiatedTime) {
        this.initiatedTime = initiatedTime;
    }

    public String getFORGO_PASS_FLAG() {
        return forgoPassFlag;
    }

    public void setFORGO_PASS_FLAG(String forgoPassFlag) {
        this.forgoPassFlag = forgoPassFlag;
    }

    public Long getCNT_MAKER_HIT() {
        return cntMakerHit;
    }

    public void setCNT_MAKER_HIT(Long cntMakerHit) {
        this.cntMakerHit = cntMakerHit;
    }

    public Long getTRANSACTIONCNT() {
        return transactionCnt;
    }

    public void setTRANSACTIONCNT(Long transactionCnt) {
        this.transactionCnt = transactionCnt;
    }

    public Timestamp getAUTHOR_TIME() {
        return authorTime;
    }

    public void setAUTHOR_TIME(Timestamp authorTime) {
        this.authorTime = authorTime;
    }
}
