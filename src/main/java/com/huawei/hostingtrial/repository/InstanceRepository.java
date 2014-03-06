package com.huawei.hostingtrial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huawei.hostingtrial.domain.Instance;
import com.huawei.hostingtrial.domain.InstanceTypeEnum;

public interface InstanceRepository extends JpaRepository<Instance, Long>{
	
	List<Instance> findByInstanceType(InstanceTypeEnum instanceType);
}
