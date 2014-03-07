package com.huawei.hostingtrial.service;

import java.security.Principal;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huawei.hostingtrial.domain.User;
import com.huawei.hostingtrial.domain.Authority;
import com.huawei.hostingtrial.repository.AuthorityRepository;
import com.huawei.hostingtrial.repository.UserRepository;

@Component("userService")
@Transactional(readOnly=true)
public class UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AuthorityRepository authorityRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public void saveDefaultUserAndPermission(User user) {
        //create SHA-256 hased password
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setImsPassword(new BCryptPasswordEncoder().encode(user.getImsPassword()));
        //set permission as read
        user.setScope("read");
        //set authorization grant type as implicit
        user.setAuthorizedGrantTypes("implicit");
        //set up default authority
        addUserAuthority(user, "ROLE_USER");
        //save user
        userRepository.save(user);
    }

    public void addUserAuthority(User user, String role){
        //set up default authority
        Authority authority = authorityRepository.findByAuthority(role);
        user.addAuthority(authority);
    }
    
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
