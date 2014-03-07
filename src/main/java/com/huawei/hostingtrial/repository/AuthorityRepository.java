package com.huawei.hostingtrial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.huawei.hostingtrial.domain.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

	Authority findByAuthority(String authority);
	
}
