package com.huawei.hostingtrial.repository;

import java.util.List;

import com.huawei.hostingtrial.domain.Application;
import com.huawei.hostingtrial.domain.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
	Page<Application> findByDeveloper(User user, Pageable pageable);
	
	List<Application> findByDeveloper(User user);
	
	Application findByClientId(String clientId);
	
	int countByDeveloper(User user);
	
}
