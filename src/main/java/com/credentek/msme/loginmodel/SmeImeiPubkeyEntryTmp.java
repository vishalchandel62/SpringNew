package com.credentek.msme.loginmodel;

import jakarta.persistence.*;

@Entity
@Table(name = "SME_IMEI_PUBKEY_ENTRY_TMP", schema = "YES_SME1")
public class SmeImeiPubkeyEntryTmp {
	@Id
    @Column(name = "IMEI_NO", length = 50)
    private String imeiNo;

    @Column(name = "PUB_KEY", length = 500)
    private String pubKey;

    @Column(name = "TOKEN", length = 100)
    private String token;

    // Default constructor
    public SmeImeiPubkeyEntryTmp() {
    }

    // Parameterized constructor
    public SmeImeiPubkeyEntryTmp(String imeiNo, String pubKey, String token) {
        this.imeiNo = imeiNo;
        this.pubKey = pubKey;
        this.token = token;
    }

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "SmeImeiPubkeyEntryTmp{" +
               "imeiNo='" + imeiNo + '\'' +
               ", pubKey='" + pubKey + '\'' +
               ", token='" + token + '\'' +
               '}';
    }
}
