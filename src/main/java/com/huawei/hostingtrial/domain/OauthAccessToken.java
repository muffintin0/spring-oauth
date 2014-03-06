package com.huawei.hostingtrial.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "oauth_access_token")
public class OauthAccessToken {
	
    /**
     */
    @Id
    @Column(name = "token_id", unique = true, nullable = false)
    private String tokenId;
    
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private byte[] token;
    
    @NotNull
    @Column(name = "authentication_id")
    private String authenticationId;
    
    @NotNull
    @Column(name = "username")
    private String username;
    
    @NotNull
    @Column(name = "client_id")
    private String clientId;
 
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private byte[] authentication;
    
    @NotNull
    @Column(name = "refresh_token")
    private String refreshToken;
    
    /**
     */
    @Column(name = "access_token_validity")
    private String accessTokenValidity;

    /**
     */
    @Column(name = "refresh_token_validity")
    private String refreshTokenValidity;

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public byte[] getToken() {
		return token;
	}

	public void setToken(byte[] token) {
		this.token = token;
	}

	public String getAuthenticationId() {
		return authenticationId;
	}

	public void setAuthenticationId(String authenticationId) {
		this.authenticationId = authenticationId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public byte[] getAuthentication() {
		return authentication;
	}

	public void setAuthentication(byte[] authentication) {
		this.authentication = authentication;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
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
}
