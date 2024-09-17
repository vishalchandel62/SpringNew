package com.credentek.msme.loginmodel;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "COM_SERVICE_MST", schema = "YES_SME1")
public class ComServiceMst {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "SERVICE_CD", length = 10)
    private String serviceCd;

    @Column(name = "SERVICE_DESC", length = 100)
    private String serviceDesc;

    @Column(name = "AUTH_STATUS", length = 1)
    private String authStatus;

    @Column(name = "MAKER_CD", length = 10)
    private String makerCd;

    @Column(name = "MAKER_DT")
    @Temporal(TemporalType.DATE)
    private Date makerDt;

    @Column(name = "MAKER_CAL_DT")
    @Temporal(TemporalType.DATE)
    private Date makerCalDt;

    @Column(name = "AUTHOR_CD", length = 10)
    private String authorCd;

    @Column(name = "AUTHOR_DT")
    @Temporal(TemporalType.DATE)
    private Date authorDt;

    @Column(name = "AUTHOR_CAL_DT")
    @Temporal(TemporalType.DATE)
    private Date authorCalDt;

    @Column(name = "ACTIVE_FLAG", length = 1)
    private String activeFlag;

    @Column(name = "LAST_CHANGE_ID")
    private Long lastChangeId;

    @Column(name = "BANK_ID", length = 7)
    private String bankId;

    @Column(name = "SERVICE_TYPE", length = 10)
    private String serviceType;

    // Getters and Setters

    public String getServiceCd() {
        return serviceCd;
    }

    public void setServiceCd(String serviceCd) {
        this.serviceCd = serviceCd;
    }

    public String getServiceDesc() {
        return serviceDesc;
    }

    public void setServiceDesc(String serviceDesc) {
        this.serviceDesc = serviceDesc;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }

    public String getMakerCd() {
        return makerCd;
    }

    public void setMakerCd(String makerCd) {
        this.makerCd = makerCd;
    }

    public Date getMakerDt() {
        return makerDt;
    }

    public void setMakerDt(Date makerDt) {
        this.makerDt = makerDt;
    }

    public Date getMakerCalDt() {
        return makerCalDt;
    }

    public void setMakerCalDt(Date makerCalDt) {
        this.makerCalDt = makerCalDt;
    }

    public String getAuthorCd() {
        return authorCd;
    }

    public void setAuthorCd(String authorCd) {
        this.authorCd = authorCd;
    }

    public Date getAuthorDt() {
        return authorDt;
    }

    public void setAuthorDt(Date authorDt) {
        this.authorDt = authorDt;
    }

    public Date getAuthorCalDt() {
        return authorCalDt;
    }

    public void setAuthorCalDt(Date authorCalDt) {
        this.authorCalDt = authorCalDt;
    }

    public String getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(String activeFlag) {
        this.activeFlag = activeFlag;
    }

    public Long getLastChangeId() {
        return lastChangeId;
    }

    public void setLastChangeId(Long lastChangeId) {
        this.lastChangeId = lastChangeId;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}
