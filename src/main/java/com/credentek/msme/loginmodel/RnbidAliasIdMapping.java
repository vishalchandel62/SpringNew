package com.credentek.msme.loginmodel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "RNBID_ALIASID_MAPPING", schema = "YES_SME1")
@IdClass(RnbidAliasIdMapping.class)
public class RnbidAliasIdMapping {

    @Id
    @Column(name = "ALIAS_ID", length = 50)
    private String aliasId;

    @Id
    @Column(name = "CUSTOMER_ID", length = 15)
    private String customerId;

    // Default constructor
    public RnbidAliasIdMapping() {}

    // Parameterized constructor
    public RnbidAliasIdMapping(String aliasId, String customerId) {
        this.aliasId = aliasId;
        this.customerId = customerId;
    }

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

    @Override
    public String toString() {
        return "RnbidAliasIdMapping{" +
                "aliasId='" + aliasId + '\'' +
                ", customerId='" + customerId + '\'' +
                '}';
    }
}
