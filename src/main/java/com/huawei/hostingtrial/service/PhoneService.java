package com.huawei.hostingtrial.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huawei.hostingtrial.domain.Phone;
import com.huawei.hostingtrial.domain.User;
import com.huawei.hostingtrial.repository.PhoneRepository;

@Component("phoneService")
@Transactional(readOnly = true)
public class PhoneService {

	private static final Logger logger = LoggerFactory.getLogger(PhoneService.class);
	
	@Value("#{'${app.phoneService.developerMaxPhoneNumbers}'}")
	private String maxPhoneNumbers;
	
	@Autowired
	PhoneRepository phoneRepository;
	
	@Transactional(readOnly = false)
	public void allocatePhones(User developer, int requestNum){
		List<Phone> phones = phoneRepository.findByisActivated(false, new PageRequest(0, requestNum));
		for(Phone phone : phones){
			phone.setIsActivated(true);
			phone.setDeveloper(developer);
			phone.setCompany(developer.getCompany());
			phoneRepository.save(phone);
			logger.info("allocate new phone "+phone.getNumber()+" to "+developer.getUsername());
		}			
			
	}
	
	@Transactional(readOnly=false)
	public void freePhones(User developer){
		List<Phone> phones = phoneRepository.findByDeveloper(developer);
		for(Phone phone : phones){
			freePhone(phone);
		}
		logger.info("release "+developer.getUsername()+"'s all phone numbers");
	}

	@Transactional(readOnly=false)
	public void freePhones(User developer, int num){
		List<Phone> phones = phoneRepository.findByDeveloper(developer, new PageRequest(0, num));
		for(Phone phone : phones){
			freePhone(phone);
		}
		logger.info("release "+developer.getUsername()+"'s "+num+" phone numbers");
	}
	
	@Transactional(readOnly=false)
	public void changePhoneNums(User developer, int newNum){
		int requestNum = newNum - phoneRepository.countByDeveloper(developer);
		
		checkAvailable(requestNum);
		//do not check quote when allocate first time
		if(developer.getTotalPhoneNumbers()!= null){
			checkQuotes(developer, requestNum);
		}
		
		if(requestNum<0){
			freePhones(developer,requestNum*(-1));
		} else {
			allocatePhones(developer, requestNum);
		}
		logger.info("change "+developer.getUsername()+" 's phone numbers to "+newNum);
	}
	
	private void checkAvailable(int requestNum){
		int freePhoneNums = phoneRepository.countByIsActivated(false);
		//if there is not enough numbers as per requested, throw exception
		if (freePhoneNums < requestNum){
			throw new RuntimeException("Not enough numbers to allocate");
		}		
	}
	
	private void checkQuotes(User developer, int requestNum){
		int allocatedNums = phoneRepository.countByDeveloper(developer);
		//this is for demo
		if ( allocatedNums+requestNum > Integer.parseInt(maxPhoneNumbers) ){
			throw new RuntimeException("Can not request more than your quotes (5 numbers)");			
		}
	}
	
	private void freePhone(Phone phone){
		phone.setDeveloper(null);
		phone.setIsActivated(false);
		phone.setCompany(null);
		phoneRepository.save(phone);		
	}
}
