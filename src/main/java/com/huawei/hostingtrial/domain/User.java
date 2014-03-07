package com.huawei.hostingtrial.domain;


import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.persistence.Column;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@NotNull
	@Column(name = "username", unique = true)
	@Pattern(regexp="^[A-Za-z0-9_-]*$")
    private String username;

	@NotNull
	@Column(name = "first_name")
	private String firstName;
	
	@NotNull
	@Column(name = "last_name")
	private String lastName;
	
	@NotNull
	@Email
	@Column(name = "email", unique = true)
    private String email;

	@NotNull
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "account_type")
	private UserAccountTypeEnum accountType;
	
    @ManyToOne
    @JoinColumn(name = "company")
    private Company company; 

    @NotNull
    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "description", length= 5000)
    private String description;
    
    @NotNull
    @Column(name = "enabled")
    private Boolean enabled = false;

    @Column(name = "resource_ids")
    private String resources;

    @Column(name = "scope")
    private String scope;

    @Column(name = "ims_username")
    private String imsUsername;

    @Column(name = "ims_password")
    private String imsPassword;
    
    @NotNull
    @Column(name = "is_developer")
    private Boolean isDeveloper = false;
    
    @NotNull
    @Column(name = "authorized_grant_types")
    private String authorizedGrantTypes;
    
    @Min(value=0)
    @Column(name = "total_phone_numbers")
    private Integer totalPhoneNumbers = 0;
    
    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "developer")
    private Set<Application> applications = new HashSet<Application>();
 
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", nullable = false)
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated", nullable = false)
    private Date updated;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name="user_authority", joinColumns = { 
			@JoinColumn(name = "user_id", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "authority_id", 
					nullable = false, updatable = false) })
    private Set<Authority> authorities;
    
    
	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	public void addAuthority(Authority authority) {
		this.authorities.add(authority);
	}
	
	@PrePersist
    protected void onCreate() {
      created = updated = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
      updated = new Date();
    }
    
    public Date getCreated() {
		return created;
	}

	public Date getUpdated() {
		return updated;
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserAccountTypeEnum getAccountType() {
		return accountType;
	}

	public void setAccountType(UserAccountTypeEnum accountType) {
		this.accountType = accountType;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getResources() {
		return resources;
	}

	public void setResources(String resources) {
		this.resources = resources;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getImsUsername() {
		return imsUsername;
	}

	public void setImsUsername(String imsUsername) {
		this.imsUsername = imsUsername;
	}

	public String getImsPassword() {
		return imsPassword;
	}

	public void setImsPassword(String imsPassword) {
		this.imsPassword = imsPassword;
	}

	public Boolean getIsDeveloper() {
		return isDeveloper;
	}

	public void setIsDeveloper(Boolean isDeveloper) {
		this.isDeveloper = isDeveloper;
	}

	public String getAuthorizedGrantTypes() {
		return authorizedGrantTypes;
	}

	public void setAuthorizedGrantTypes(String authorizedGrantTypes) {
		this.authorizedGrantTypes = authorizedGrantTypes;
	}
	
	public Integer getTotalPhoneNumbers() {
		return totalPhoneNumbers;
	}

	public void setTotalPhoneNumbers(Integer totalPhoneNumbers) {
		this.totalPhoneNumbers = totalPhoneNumbers;
	}

	public Set<Application> getApplications() {
		return applications;
	}

	public void setApplications(Set<Application> applications) {
		this.applications = applications;
	}

	public Set<Phone> getPhones() {
		return phones;
	}

	public void setPhones(Set<Phone> phones) {
		this.phones = phones;
	}

	@OneToMany(fetch=FetchType.EAGER,cascade = CascadeType.ALL, mappedBy = "developer")
    private Set<Phone> phones = new HashSet<Phone>();
    
}
