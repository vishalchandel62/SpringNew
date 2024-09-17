package com.credentek.msme.loginmodel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "VW_CH_ACCT_CUST_XREF", schema = "YES_SME1")
@IdClass(VwChAcctCustXref.class)
public class VwChAcctCustXref {

    @Id
    @Column(name = "COD_ACCT_NO", length = 48, nullable = false)
    private String codAcctNo;

    @Column(name = "COD_CUST")
    private Long codCust;

    @Column(name = "COD_ACCT_CUST_REL", length = 27)
    private String codAcctCustRel;

    @Column(name = "COD_PROD")
    private Long codProd;

    // Default constructor
    public VwChAcctCustXref() {}

    // Parameterized constructor
    public VwChAcctCustXref(String codAcctNo, Long codCust, String codAcctCustRel, Long codProd) {
        this.codAcctNo = codAcctNo;
        this.codCust = codCust;
        this.codAcctCustRel = codAcctCustRel;
        this.codProd = codProd;
    }

    // Getters and Setters
    public String getCodAcctNo() {
        return codAcctNo;
    }

    public void setCodAcctNo(String codAcctNo) {
        this.codAcctNo = codAcctNo;
    }

    public Long getCodCust() {
        return codCust;
    }

    public void setCodCust(Long codCust) {
        this.codCust = codCust;
    }

    public String getCodAcctCustRel() {
        return codAcctCustRel;
    }

    public void setCodAcctCustRel(String codAcctCustRel) {
        this.codAcctCustRel = codAcctCustRel;
    }

    public Long getCodProd() {
        return codProd;
    }

    public void setCodProd(Long codProd) {
        this.codProd = codProd;
    }

    @Override
    public String toString() {
        return "VwChAcctCustXref{" +
                "codAcctNo='" + codAcctNo + '\'' +
                ", codCust=" + codCust +
                ", codAcctCustRel='" + codAcctCustRel + '\'' +
                ", codProd=" + codProd +
                '}';
    }
}
