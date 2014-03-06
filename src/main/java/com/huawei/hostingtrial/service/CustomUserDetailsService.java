package com.huawei.hostingtrial.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huawei.hostingtrial.domain.Authority;
import com.huawei.hostingtrial.repository.UserRepository;


/**
 * A custom {@link UserDetailsService} where user information
 * is retrieved from a JPA repository
 */
@Service
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {
		
	@Autowired
	UserRepository userRepository;
	
	/**
	 * Returns a populated {@link UserDetails} object. 
	 * The username is first retrieved from the database and then mapped to 
	 * a {@link UserDetails} object.
	 */
	//first check web identity, if not found use ims identity
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		com.huawei.hostingtrial.domain.User userRecord;
		
		//the following is not implemented in database currently
		//boolean enabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;
		
		try {
			//username could be web identity or ims identity
			int findUser = userRepository.countByUsername(username);
			
			//this is web identity
			if (findUser==1){
				userRecord = userRepository.findByUsername(username);
				//for web identity authorization
				return new User(
						userRecord.getUsername(),
						userRecord.getPassword(),
						userRecord.getEnabled(),
						accountNonExpired,
						credentialsNonExpired,
						accountNonLocked,
						getAuthorities(userRecord));
				
			} else {
				//this is ims identity
				//if this fails just throw the username not found exception
				userRecord = userRepository.findByImsUsername(username);
				//for ims identity authorization
				return new User(
						userRecord.getImsUsername(),
						userRecord.getImsPassword(),
						userRecord.getEnabled(),
						accountNonExpired,
						credentialsNonExpired,
						accountNonLocked,
						getAuthorities(userRecord));					
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Retrieves a collection of {@link GrantedAuthority} based on a numerical role
	 * @param role the numerical role
	 * @return a collection of {@link GrantedAuthority
	 */
	public Collection<? extends GrantedAuthority> getAuthorities(com.huawei.hostingtrial.domain.User userRecord) {
		Set<Authority> authorities = userRecord.getAuthorities();
		List<String> roles = new ArrayList<String>();
		for( Authority authority : authorities){
			roles.add(authority.getAuthority());
		}
		
		List<GrantedAuthority> authList = getGrantedAuthorities(roles);
		return authList;
	}
	
	
	/**
	 * Wraps {@link String} roles to {@link SimpleGrantedAuthority} objects
	 * @param roles {@link String} of roles
	 * @return list of granted authorities
	 */
	public static List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}
}
