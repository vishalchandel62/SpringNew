package com.credentek.msme.loginmodel;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "OFFER", schema = "YES_SME1")
public class Offer {

    @Id
    @Column(name = "OFFER_ID", length = 20)
    private String offerId;

    @Column(name = "OFFER_TAG_NAME", length = 255)
    private String offerTagName;

    @Column(name = "OFFER_DESCRIPTION", length = 1000)
    private String offerDescription;

    @Column(name = "CUSTOMER_ID", length = 20)
    private String customerId;

    @Column(name = "CREATED_ON")
    private LocalDateTime createdOn;

    @Column(name = "VALID_TO")
    private LocalDateTime validTo;

    @Column(name = "READ_FLAG", length = 1)
    private String readFlag;

    @Column(name = "MODULE_NAME", length = 50)
    private String moduleName;

    // Getters and Setters
    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getOfferTagName() {
        return offerTagName;
    }

    public void setOfferTagName(String offerTagName) {
        this.offerTagName = offerTagName;
    }

    public String getOfferDescription() {
        return offerDescription;
    }

    public void setOfferDescription(String offerDescription) {
        this.offerDescription = offerDescription;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDateTime validTo) {
        this.validTo = validTo;
    }

    public String getReadFlag() {
        return readFlag;
    }

    public void setReadFlag(String readFlag) {
        this.readFlag = readFlag;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
