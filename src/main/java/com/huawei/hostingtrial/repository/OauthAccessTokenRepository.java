package com.huawei.hostingtrial.repository;

import java.util.List;

import com.huawei.hostingtrial.domain.OauthAccessToken;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OauthAccessTokenRepository extends JpaRepository<OauthAccessToken, String>{
	
	List<OauthAccessToken> findByClientId(String clientId);
	
	int countByClientId(String clientId);
}
