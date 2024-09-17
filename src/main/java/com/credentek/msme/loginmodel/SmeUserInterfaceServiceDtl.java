package com.credentek.msme.loginmodel;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "SME_USER_INTERFACE_SERVICE_DTL", schema = "YES_SME1")
public class SmeUserInterfaceServiceDtl {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "your_sequence_generator")
    @SequenceGenerator(name = "your_sequence_generator", sequenceName = "LOGIN_ADDED_SEQ", allocationSize = 1)
    @Column(name = "NEXT_CHANGED_SEQ")
    private Long nextChangedSeq;

    @Column(name = "SERVICE_NAME", length = 30)
    private String serviceName;

    @Column(name = "LOGIN_TYPE", length = 1)
    private String loginType;

    @Column(name = "LOGIN_VIA_FLAG", length = 1)
    private String loginViaFlag;

    @Column(name = "ERROR_EXIST_FLAG", length = 1, columnDefinition = "VARCHAR2(1 BYTE) DEFAULT 'N'")
    private String errorExistFlag;

    @Column(name = "IP_ADDRESS", length = 60)
    private String ipAddress;

    public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Long getLoginActiveId() {
		return loginActiveId;
	}

	public void setLoginActiveId(Long loginActiveId) {
		this.loginActiveId = loginActiveId;
	}

	public Date getNavigateDt() {
		return navigateDt;
	}

	public void setNavigateDt(Date date) {
		this.navigateDt = date;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	@Column(name = "LOGIN_ACTIVE_ID")
    private Long loginActiveId;

    @Column(name = "NAVIGATE_DT")
    private Date navigateDt;

    @Column(name = "CUSTOMER_ID", length = 30)
    private String customerId;

    // Getters and Setters

    public Long getNextChangedSeq() {
        return nextChangedSeq;
    }

    public void setNextChangedSeq(Long nextChangedSeq) {
        this.nextChangedSeq = nextChangedSeq;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getLoginViaFlag() {
        return loginViaFlag;
    }

    public void setLoginViaFlag(String loginViaFlag) {
        this.loginViaFlag = loginViaFlag;
    }

    public String getErrorExistFlag() {
        return errorExistFlag;
    }

    public void setErrorExistFlag(String errorExistFlag)
    {
    	this.errorExistFlag=errorExistFlag;
    			}
    }
