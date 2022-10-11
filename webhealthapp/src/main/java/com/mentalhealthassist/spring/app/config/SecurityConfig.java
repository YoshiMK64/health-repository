package com.mentalhealthassist.spring.app.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	//add reference to our security data source 
	private DataSource secDataSource;
	
	
	//constructor injection
	@Autowired
	public SecurityConfig(DataSource secDataSource) {
		this.secDataSource = secDataSource;
	}
	
	
	//details manager
	@Bean 
	public UserDetailsManager userDetailsManager() {
		
		return new JdbcUserDetailsManager(secDataSource);
		
	}
	
	//assigning the access rights
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		return http
			.authorizeRequests(configurer -> 
				configurer
					.antMatchers("favicon.ico").permitAll() 		//allow all access to favicon
					.antMatchers("flowers.jpg").permitAll()			//allow all access to header image (testing if needed) 
					.antMatchers("/").permitAll()           		//all access to read pages
					.antMatchers("/admin/**").hasRole("ADMIN")		//ADMIN Role access to admin-index
					.antMatchers("/condition/admin/**").hasRole("ADMIN")	//ADMIN acess to data base editing pages
					.antMatchers("/symptom/admin/**").hasRole("ADMIN")
					.antMatchers("/treatment/admin/**").hasRole("ADMIN")
					)
			
			.formLogin(configurer -> 
				configurer
					.loginPage("/showMyLoginPage")				//controller mapping for login page
					.loginProcessingUrl("/authenticateTheUser")	//spring security request authenticate users from DB
					.permitAll())
			
			.logout(configurer -> 
				configurer				//allow all to logout
					.permitAll())
			
			.exceptionHandling(configurer -> 	
				configurer				//custom access denied page
					.accessDeniedPage("/accessDenied"))
					
			.build();
	}

}
