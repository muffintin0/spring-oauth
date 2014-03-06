package com.huawei.hostingtrial.domain;

import java.util.Date;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Table(name="application")
public class Application {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
    public Long getId() {
		return id;
	}

	@NotNull
	@Pattern(regexp="^[A-Za-z0-9_-]*$")
	@Column(name = "name")
    private String name;

	@Column(name = "scope")
    private String scope;

    @NotEmpty
    @Column(name = "web_server_redirect_uri")
    private String webServerRedirectUri;

    @Column(name = "resource_ids")
    private String resources;

    @NotEmpty
    @Column(name = "description")
    private String description;

    @NotEmpty
    @Column(name="client_id", unique = true)
    private String clientId;

	@Size(max = 64)
    @Column(name="client_secret")
    private String clientSecret;

    @NotNull
    @ManyToOne
    @Column(name = "developer")
    private User developer;
    
    @NotNull
    @Column(name = "is_public")
    private Boolean isPublic = true;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", nullable = false)
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated", nullable = false)
    private Date updated;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name="application_phone", joinColumns = { 
			@JoinColumn(name = "application_id", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "phone_id", 
					nullable = false, updatable = false) })
    private Set<Phone> phones;
    
    public Set<Phone> getPhones() {
		return phones;
	}

	public void setPhones(Set<Phone> phones) {
		this.phones = phones;
	}

	@PrePersist
    protected void onCreate() {
      created = updated = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
      updated = new Date();
    }
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getWebServerRedirectUri() {
		return webServerRedirectUri;
	}

	public void setWebServerRedirectUri(String webServerRedirectUri) {
		this.webServerRedirectUri = webServerRedirectUri;
	}

	public String getResources() {
		return resources;
	}

	public void setResources(String resources) {
		this.resources = resources;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public User getDeveloper() {
		return developer;
	}

	public void setDeveloper(User developer) {
		this.developer = developer;
	}

	public Boolean getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
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
