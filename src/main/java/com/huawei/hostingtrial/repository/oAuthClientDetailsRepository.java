package com.huawei.hostingtrial.repository;

import java.util.List;

import com.huawei.hostingtrial.domain.OauthClientDetails;

import org.springframework.data.jpa.repository.JpaRepository;

public interface oAuthClientDetailsRepository extends JpaRepository<OauthClientDetails, String>{
	
	List<OauthClientDetails> findByClientId(String clientId);
	
	int countByClientId(String clientId);
}
