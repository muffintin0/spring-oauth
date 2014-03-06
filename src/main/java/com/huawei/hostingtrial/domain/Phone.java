package com.huawei.hostingtrial.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "phone")
public class Phone {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@NotNull
	@Pattern(regexp="^[0-9+]*$")
	@Column(name = "number", unique = true)
    private String number;

	@NotNull
	@Column(name = "is_activated")
    private Boolean isActivated = false;

    @ManyToOne
    @Column(name = "developer")
    private User developer;
    
    @ManyToOne
    @Column(name = "company")
    private Company company;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "phones")
    private Set<Application> applications;
    
	public Set<Application> getApplications() {
		return applications;
	}

	public void setApplications(Set<Application> applications) {
		this.applications = applications;
	}

	public Long getId() {
		return id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Boolean getIsActivated() {
		return isActivated;
	}

	public void setIsActivated(Boolean isActivated) {
		this.isActivated = isActivated;
	}

	public User getDeveloper() {
		return developer;
	}

	public void setDeveloper(User developer) {
		this.developer = developer;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}    
 
}
