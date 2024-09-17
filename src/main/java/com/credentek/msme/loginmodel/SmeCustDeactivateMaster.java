package com.credentek.msme.loginmodel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "SME_CUST_DEACTIVATE_MASTER", schema = "YES_SME1")
public class SmeCustDeactivateMaster {

    @Column(name = "LAST_CHANGE_ID")
    private BigDecimal lastChangeId;

    @Id
    @Column(name = "ID")
    private BigDecimal id;

    @Column(name = "CUSTOMER_ID", length = 200)
    private String customerId;

    @Column(name = "COMPANY_ID", length = 50)
    private String companyId;

    @Column(name = "DE_ACTIVATE_FLAG", length = 1)
    private String deActivateFlag;

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

    // Getters and Setters

    public BigDecimal getLastChangeId() {
        return lastChangeId;
    }

    public void setLastChangeId(BigDecimal lastChangeId) {
        this.lastChangeId = lastChangeId;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCompanyId() {
        return companyId;
    }

}
