package com.huawei.hostingtrial.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name="instance")
public class Instance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
    public Long getId() {
		return id;
	}

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "sub_division_code")
    private String subDivisionCode;

    @Column(name = "sub_divison_name")
    private String subDivisonName;

    @Column(name = "city")
    private String city;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Transient
    public int responseTime;

    @Transient
    public Double distance;

    @Column(name = "instance_type")
	@Enumerated(EnumType.ORDINAL)    
    private InstanceTypeEnum instanceType;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", nullable = false)
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated", nullable = false)
    private Date updated;

	@PrePersist
    protected void onCreate() {
      created = updated = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
      updated = new Date();
    }

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getSubDivisionCode() {
		return subDivisionCode;
	}

	public void setSubDivisionCode(String subDivisionCode) {
		this.subDivisionCode = subDivisionCode;
	}

	public String getSubDivisonName() {
		return subDivisonName;
	}

	public void setSubDivisonName(String subDivisonName) {
		this.subDivisonName = subDivisonName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public InstanceTypeEnum getInstanceType() {
		return instanceType;
	}

	public void setInstanceType(InstanceTypeEnum instanceType) {
		this.instanceType = instanceType;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}
}
