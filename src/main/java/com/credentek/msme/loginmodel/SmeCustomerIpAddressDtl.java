package com.credentek.msme.loginmodel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "SME_CUSTOMER_IP_ADDRESS_DTL", schema = "YES_SME1")
public class SmeCustomerIpAddressDtl {

    @Id
    @Column(name = "CUSTOMER_IP", length = 100)
    private String customerIp;

    @Column(name = "IP_ADDRESS", length = 50)
    private String ipAddress;

    @Column(name = "REQUEST_MODE", length = 1)
    private String requestMode;

    // Getters and Setters

    public String getCustomerIp() {
        return customerIp;
    }

    public void setCustomerIp(String customerIp) {
        this.customerIp = customerIp;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getRequestMode() {
        return requestMode;
    }

    public void setRequestMode(String requestMode) {
        this.requestMode = requestMode;
    }
}
