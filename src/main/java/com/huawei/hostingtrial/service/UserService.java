package com.huawei.hostingtrial.service;

import java.security.Principal;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huawei.hostingtrial.domain.User;
import com.huawei.hostingtrial.domain.Authority;
import com.huawei.hostingtrial.repository.UserRepository;

@Service
@Transactional(readOnly=true)
public class UserService {

	@Autowired
	UserRepository userRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	//generic method to get user, name could be username or ims_username
	public User getUser(String name){
		User user = userRepository.findByUsername(name);
		if (user==null) user = userRepository.findByImsUsername(name);
		return user;
	}

	public User getUser(Principal currentUser){
		if (currentUser==null) return null;
		return getUser(currentUser.getName());
	}
	
    //check if currentUser has privilege to work on username 
    public boolean crudPrivilege(User user, Principal currentUser){
    	if (user==null || currentUser== null) return false;
    	User loginUser = getUser(currentUser.getName());
        //only admin user or owner have crudPrivilege
        if (user.getId().equals(loginUser.getId()) || isAdmin(currentUser)) {
            return true;
        }    	
        return false;
    }
    
    //check if current user is admin
    public boolean isAdmin(User user) {
    	if (user==null){
    		return false;
    	}
    	return checkRole(user, "ROLE_ADMIN");
    }
    
    public boolean isAdmin(Principal currentUser){
    	if (currentUser==null) return false;
    	User user = getUser(currentUser.getName());
    	return isAdmin(user); 	
    }
    
    public boolean isDeveloper(User user){
    	if (user==null) return false;
    	return checkRole(user, "ROLE_DEVELOPER");
    }
    
    public boolean isDeveloper(Principal currentUser){
    	if (currentUser==null) return false;
    	User user = getUser(currentUser.getName());
    	return isDeveloper(user);
    }
    
    private boolean checkRole(User user, String role){
    	Set<Authority> authorities = user.getAuthorities();
    	for(Authority authority : authorities){
    		if (authority.getAuthority().equals(role)) return true;
    	}
    	return false;
    }
	
}
