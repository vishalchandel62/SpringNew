package com.credentek.msme.loginmodel;
import jakarta.persistence.*;

@Entity
@Table(name = "RNBID_ALIASID_MAPPING", schema = "YES_SME1")
public class RnbidAliasidMapping {

    @Id
    @Column(name = "ALIAS_ID", length = 50)
    private String aliasId;

    @Column(name = "CUSTOMER_ID", length = 15)
    private String customerId;

    // Getters and Setters

    public String getAliasId() {
        return aliasId;
    }

    public void setAliasId(String aliasId) {
        this.aliasId = aliasId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
