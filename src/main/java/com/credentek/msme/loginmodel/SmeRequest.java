package com.credentek.msme.loginmodel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "SME_REQUEST", schema = "YES_SME1")
@IdClass(SmeRequest.class)
public class SmeRequest {

    @Id
    @Column(name = "REQUEST_ID", length = 50, nullable = false)
    private String requestId;

    // Default constructor
    public SmeRequest() {}

    // Parameterized constructor
    public SmeRequest(String requestId) {
        this.requestId = requestId;
    }

    // Getter and Setter
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "SmeRequest{" +
                "requestId='" + requestId + '\'' +
                '}';
    }
}
