package com.huawei.hostingtrial.domain;


import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Entity
@Table(name = "authority")
public class Authority {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
    @NotNull
    @Size(min = 8, max = 50)
    @Pattern(regexp = "^ROLE_[A-Z]*")
    @Column(name = "authority")
    private String authority;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "authorities")
    private Set<User> users;
  
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Long getId() {
		return id;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}
    
}
