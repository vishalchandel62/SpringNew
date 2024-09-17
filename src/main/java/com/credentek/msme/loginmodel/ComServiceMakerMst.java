package com.credentek.msme.loginmodel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "COM_SERVICE_MAKER_MST", schema = "YES_SME1")
public class ComServiceMakerMst {

    @Id
    @Column(name = "SMEMAKERCD", length = 40, nullable = false)
    private String sMeMakerCd;

    @Column(name = "SERVICE_CD", length = 200)
    private String serviceCd;

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

    @Column(name = "SERVICE_TYPE_TEMP", length = 1)
    private String serviceTypeTemp;

    // Getters and Setters

    public String getSMEMAKERCD() {
        return sMeMakerCd;
    }

    public void setSMEMAKERCD(String sMeMakerCd) {
        this.sMeMakerCd = sMeMakerCd;
    }

    public String getSERVICE_CD() {
        return serviceCd;
    }

    public void setSERVICE_CD(String serviceCd) {
        this.serviceCd = serviceCd;
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

    public String getSERVICE_TYPE_TEMP() {
        return serviceTypeTemp;
    }

    public void setSERVICE_TYPE_TEMP(String serviceTypeTemp) {
        this.serviceTypeTemp = serviceTypeTemp;
    }
}
