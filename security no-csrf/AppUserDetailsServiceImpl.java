package com.apptechnosoft.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	AppUserRepository appUserRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    AppUser appUser = appUserRepository.findByUsername(username);
	    if (appUser == null) {
	        throw new UsernameNotFoundException("User not found with username: " + username);
	    }
	    // Log the details to check if the user is fetched correctly
	    System.out.println("User found: " + appUser.getUsername() + ", Role: " + appUser.getRole());

	    // Map user details to Spring Security UserDetails
	    return User.builder()
	            .username(appUser.getUsername())
	            .password(appUser.getPassword())
	            .roles(appUser.getRole().name()) // No need for "ROLE_" prefix here; Spring adds it automatically
	            .build();
	}

}