package com.credentek.msme.loginmodel;
import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "SME_REGISTRATION_DATE_DTL", schema = "YES_SME1")
public class SmeRegistrationDateDtl {

    @Id
    @Column(name = "REG_ID", length = 100, nullable = false)
    private String regId;

    @Column(name = "PORTAL_REG_DATE")
    private Timestamp portalRegDate;

    @Column(name = "MOBILE_REG_DATE")
    private Timestamp mobileRegDate;

    @Column(name = "REMAIN_DATE_FOR_EXPIRE_M")
    private Timestamp remainDateForExpireM;

    @Column(name = "REMAIN_DATE_FOR_EXPIRE_P")
    private Timestamp remainDateForExpireP;

    // Default constructor
    public SmeRegistrationDateDtl() {
    }

    // Parameterized constructor
    public SmeRegistrationDateDtl(String regId, Timestamp portalRegDate, Timestamp mobileRegDate, 
                                  Timestamp remainDateForExpireM, Timestamp remainDateForExpireP) {
        this.regId = regId;
        this.portalRegDate = portalRegDate;
        this.mobileRegDate = mobileRegDate;
        this.remainDateForExpireM = remainDateForExpireM;
        this.remainDateForExpireP = remainDateForExpireP;
    }

    // Getters and Setters
    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public Timestamp getPortalRegDate() {
        return portalRegDate;
    }

    public void setPortalRegDate(Timestamp portalRegDate) {
        this.portalRegDate = portalRegDate;
    }

    public Timestamp getMobileRegDate() {
        return mobileRegDate;
    }

    public void setMobileRegDate(Timestamp mobileRegDate) {
        this.mobileRegDate = mobileRegDate;
    }

    public Timestamp getRemainDateForExpireM() {
        return remainDateForExpireM;
    }

    public void setRemainDateForExpireM(Timestamp remainDateForExpireM) {
        this.remainDateForExpireM = remainDateForExpireM;
    }

    public Timestamp getRemainDateForExpireP() {
        return remainDateForExpireP;
    }

    public void setRemainDateForExpireP(Timestamp remainDateForExpireP) {
        this.remainDateForExpireP = remainDateForExpireP;
    }

    @Override
    public String toString() {
        return "SmeRegistrationDateDtl{" +
               "regId='" + regId + '\'' +
               ", portalRegDate=" + portalRegDate +
               ", mobileRegDate=" + mobileRegDate +
               ", remainDateForExpireM=" + remainDateForExpireM +
               ", remainDateForExpireP=" + remainDateForExpireP +
               '}';
    }
}
