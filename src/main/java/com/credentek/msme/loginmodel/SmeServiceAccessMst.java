package com.credentek.msme.loginmodel;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "SME_SERVICE_ACCESS_MST", schema = "YES_SME1")
public class SmeServiceAccessMst {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CUSTOMER_ID", length = 30)
    private String customerId;

    @Column(name = "GROUP_SERVICE_CODE", length = 20)
    private String groupServiceCode;

    @Column(name = "GROUP_SERVICE_STATUS", length = 1)
    private String groupServiceStatus;

    @Column(name = "AUTH_STATUS", length = 1)
    private String authStatus;

    @Column(name = "AUTHOR_CAL_DT")
    @Temporal(TemporalType.DATE)
    private Date authorCalDt;

    @Column(name = "AUTHOR_CD", length = 20)
    private String authorCd;

    @Column(name = "AUTHOR_DT")
    @Temporal(TemporalType.DATE)
    private Date authorDt;

    @Column(name = "LAST_CHANGE_ID", length = 20)
    private String lastChangeId;

    @Column(name = "MAKER_CAL_DT")
    @Temporal(TemporalType.DATE)
    private Date makerCalDt;

    @Column(name = "MAKER_CD", length = 20)
    private String makerCd;

    @Column(name = "MAKER_DT")
    @Temporal(TemporalType.DATE)
    private Date makerDt;

    @Column(name = "ACTIVE_FLAG", length = 1)
    private String activeFlag;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getGroupServiceCode() {
        return groupServiceCode;
    }

    public void setGroupServiceCode(String groupServiceCode) {
        this.groupServiceCode = groupServiceCode;
    }

    public String getGroupServiceStatus() {
        return groupServiceStatus;
    }

    public void setGroupServiceStatus(String groupServiceStatus) {
        this.groupServiceStatus = groupServiceStatus;
    }

    public String getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }

    public Date getAuthorCalDt() {
        return authorCalDt;
    }

    public void setAuthorCalDt(Date authorCalDt) {
        this.authorCalDt = authorCalDt;
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

    public String getLastChangeId() {
        return lastChangeId;
    }
    
    public void setLastChangeId(String lastChangeId) {
        this. lastChangeId = lastChangeId;
    }
    
}
