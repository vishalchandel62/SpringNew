package com.credentek.msme.loginmodel;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "NOTIFICATION", schema = "YES_SME1")
public class Notification {

    @Id
    @Column(name = "NOTIFICATION_ID", length = 20)
    private String notificationId;

    @Column(name = "NOTIFICATION_TAG_NAME", length = 255)
    private String notificationTagName;

    @Column(name = "NOTIFICATION_DESCRIPTION", length = 1000)
    private String notificationDescription;

    @Column(name = "CUSTOMER_ID", length = 20)
    private String customerId;

    @Column(name = "CREATED_ON")
    private LocalDateTime createdOn;

    @Column(name = "SEND_STATUS", length = 1)
    private String sendStatus;

    @Column(name = "MODULE_NAME", length = 50)
    private String moduleName;

    // Getters and Setters
    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationTagName() {
        return notificationTagName;
    }

    public void setNotificationTagName(String notificationTagName) {
        this.notificationTagName = notificationTagName;
    }

    public String getNotificationDescription() {
        return notificationDescription;
    }

    public void setNotificationDescription(String notificationDescription) {
        this.notificationDescription = notificationDescription;
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

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
