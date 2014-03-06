package com.huawei.hostingtrial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.huawei.hostingtrial.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	User findByUsername(String username);
	
	User findByImsUsername(String imsUsername);
	
	int countByUsername(String username);
	
	int countByImsUsername(String imsUsername);
		
}
