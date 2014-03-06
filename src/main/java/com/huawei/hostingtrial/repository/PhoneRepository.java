package com.huawei.hostingtrial.repository;

import java.util.List;

import com.huawei.hostingtrial.domain.Company;
import com.huawei.hostingtrial.domain.Phone;
import com.huawei.hostingtrial.domain.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneRepository extends JpaRepository<Phone, Long>{
	List<Phone> findByDeveloper(User user, Pageable pageable);
	
	List<Phone> findByDeveloper(User user);

	Page<Phone> findByCompany(Company company, Pageable pageable);
	
	List<Phone> findByCompany(Company company);
	
	int countByDeveloper(User user);
	
	int countByCompany(Company company);
	
	int countByIsActivated(Boolean isActivated);
	
	List<Phone> findByisActivated(Boolean isActivated, Pageable pageable);
	
	List<Phone> findByisActivated(Boolean isActivated);
}
