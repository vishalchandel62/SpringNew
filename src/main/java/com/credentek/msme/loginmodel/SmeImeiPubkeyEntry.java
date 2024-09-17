package com.credentek.msme.loginmodel;

import jakarta.persistence.*;

@Entity
@Table(name = "SME_IMEI_PUBKEY_ENTRY", schema = "YES_SME1")
public class SmeImeiPubkeyEntry {

    @Id
    @Column(name = "IMEI_NO", length = 50)
    private String imeiNo;

    @Column(name = "PUB_KEY", length = 500)
    private String pubKey;

    // Getters and Setters

    public String getImeiNo() {
        return imeiNo;
    }

    public void setImeiNo(String imeiNo) {
        this.imeiNo = imeiNo;
    }

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }
}
