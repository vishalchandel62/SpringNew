package com.credentek.msme.loginmodel;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "SFMS_NHL_NODE_HOLIDAY_LST", schema = "YES_SME1")
public class SfmsNhlNodeHolidayLst {

    @Id
    @Column(name = "NHL_DATE")
    @Temporal(TemporalType.DATE)
    private Date nhlDate;

    @Column(name = "NHL_IFSC", length = 11, nullable = false)
    private String nhlIfsc;

    @Column(name = "NHL_DESC", length = 100, nullable = false)
    private String nhlDesc;

    @Column(name = "NHL_MODIFIED_BY", length = 8, nullable = false)
    private String nhlModifiedBy;

    @Column(name = "DM_LSTUPDDT")
    @Temporal(TemporalType.DATE)
    private Date dmLstupddt;

    // Default constructor
    public SfmsNhlNodeHolidayLst() {
    }

    // Parameterized constructor
    public SfmsNhlNodeHolidayLst(Date nhlDate, String nhlIfsc, String nhlDesc, String nhlModifiedBy, Date dmLstupddt) {
        this.nhlDate = nhlDate;
        this.nhlIfsc = nhlIfsc;
        this.nhlDesc = nhlDesc;
        this.nhlModifiedBy = nhlModifiedBy;
        this.dmLstupddt = dmLstupddt;
    }

    // Getters and Setters
    public Date getNhlDate() {
        return nhlDate;
    }

    public void setNhlDate(Date nhlDate) {
        this.nhlDate = nhlDate;
    }

    public String getNhlIfsc() {
        return nhlIfsc;
    }

    public void setNhlIfsc(String nhlIfsc) {
        this.nhlIfsc = nhlIfsc;
    }

    public String getNhlDesc() {
        return nhlDesc;
    }

    public void setNhlDesc(String nhlDesc) {
        this.nhlDesc = nhlDesc;
    }

    public String getNhlModifiedBy() {
        return nhlModifiedBy;
    }

    public void setNhlModifiedBy(String nhlModifiedBy) {
        this.nhlModifiedBy = nhlModifiedBy;
    }

    public Date getDmLstupddt() {
        return dmLstupddt;
    }

    public void setDmLstupddt(Date dmLstupddt) {
        this.dmLstupddt = dmLstupddt;
    }

    @Override
    public String toString() {
        return "SfmsNhlNodeHolidayLst{" +
               "nhlDate=" + nhlDate +
               ", nhlIfsc='" + nhlIfsc + '\'' +
               ", nhlDesc='" + nhlDesc + '\'' +
               ", nhlModifiedBy='" + nhlModifiedBy + '\'' +
               ", dmLstupddt=" + dmLstupddt +
               '}';
    }
}
