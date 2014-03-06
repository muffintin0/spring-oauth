package com.huawei.hostingtrial.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "company")
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@NotNull
	@Column(name = "name", unique = true)
	private String name;
	
	@NotNull
	@Column(name = "description")
	private String description;
	
	@Min(value=0)
	@Column(name = "total_phone_numbers")
	private Integer totalPhoneNumbers=0;
	
    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "company")
    private Set<User> members = new HashSet<User>();
    
    @OneToMany(fetch=FetchType.EAGER,cascade = CascadeType.ALL, mappedBy = "company")
    private Set<Phone> phones = new HashSet<Phone>();	

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", nullable = false)
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated", nullable = false)
    private Date updated;

    public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getTotalPhoneNumbers() {
		return totalPhoneNumbers;
	}

	public void setTotalPhoneNumbers(Integer totalPhoneNumbers) {
		this.totalPhoneNumbers = totalPhoneNumbers;
	}

	public Set<User> getMembers() {
		return members;
	}

	public void setMembers(Set<User> members) {
		this.members = members;
	}

	public Set<Phone> getPhones() {
		return phones;
	}

	public void setPhones(Set<Phone> phones) {
		this.phones = phones;
	}

	public Date getCreated() {
		return created;
	}

	public Date getUpdated() {
		return updated;
	}

	@PrePersist
    protected void onCreate() {
      created = updated = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
      updated = new Date();
    }
    
}
