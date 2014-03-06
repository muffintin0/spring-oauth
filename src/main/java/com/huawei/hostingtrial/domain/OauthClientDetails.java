package com.huawei.hostingtrial.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "oauth_client_details")
public class OauthClientDetails {

    /**
     */
    @Id
    @Column(name = "client_id", unique = true, nullable = false)
    private String clientId;

    /**
     */
    @Column(name = "resource_ids")
    private String resourceIds;

    /**
     */
    @Column(name = "client_secret")
    private String clientSecret;

    /**
     */
    @Column(name = "scope")
    private String scope;

    /**
     */
    @Column(name = "authorized_grant_types")
    private String authorizedGrantTypes;

    /**
     */
    @Column(name = "web_server_redirect_uri")
    private String webServerRedirectUri;

    /**
     */
    @Column(name="authorities")
    private String authorities;

    /**
     */
    @Column(name = "access_token_validity")
    private String accessTokenValidity;

    /**
     */
    @Column(name = "refresh_token_validity")
    private String refreshTokenValidity;

    /**
     */
    @Column(name = "additional_information")
    private String additionalInformation;

	public String getClientId() {
		return clientId;
	}

	public String getResourceIds() {
		return resourceIds;
	}

	public void setResourceIds(String resourceIds) {
		this.resourceIds = resourceIds;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getAuthorizedGrantTypes() {
		return authorizedGrantTypes;
	}

	public void setAuthorizedGrantTypes(String authorizedGrantTypes) {
		this.authorizedGrantTypes = authorizedGrantTypes;
	}

	public String getWebServerRedirectUri() {
		return webServerRedirectUri;
	}

	public void setWebServerRedirectUri(String webServerRedirectUri) {
		this.webServerRedirectUri = webServerRedirectUri;
	}

	public String getAuthorities() {
		return authorities;
	}

	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}

	public String getAccessTokenValidity() {
		return accessTokenValidity;
	}

	public void setAccessTokenValidity(String accessTokenValidity) {
		this.accessTokenValidity = accessTokenValidity;
	}

	public String getRefreshTokenValidity() {
		return refreshTokenValidity;
	}

	public void setRefreshTokenValidity(String refreshTokenValidity) {
		this.refreshTokenValidity = refreshTokenValidity;
	}

	public String getAdditionalInformation() {
		return additionalInformation;
	}

	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}
    
}
