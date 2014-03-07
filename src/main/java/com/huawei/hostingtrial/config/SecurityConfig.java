package com.huawei.hostingtrial.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.huawei.hostingtrial.service.CustomUserDetailsService;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// @formatter:off
		/*
		auth.userDetailsService(customUserDetailsService).dataSource(dataSource).usersByUsernameQuery("select username,password, enabled from users where username=?")
			.authoritiesByUsernameQuery("select u.username, ur.authority from users u, user_roles ur where u.user_id = ur.user_id and u.username =?  ")
			.passwordEncoder(shaPasswordEncoder); */
		auth.userDetailsService(customUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
		//auth.inMemoryAuthentication().withUser("admin").password("admin")
		//		.roles("ADMIN", "USER").and().withUser("user").password("user")
		//		.roles("USER");
		// @formatter:on
	}

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
          .ignoring()
             .antMatchers("/resources/**","/webjars/**","/css/**","/images/**"); 
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http.authorizeRequests().antMatchers("/login").permitAll()
				.anyRequest().fullyAuthenticated()
			.and().formLogin().loginPage("/login").defaultSuccessUrl("/")
				.failureUrl("/login?error")
			.and().logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.and()
				.exceptionHandling().accessDeniedPage("/access?error");

	}
	
}
