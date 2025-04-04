package com.apptechnosoft.security;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
	AppUser  findByUsername(String username);
	AppUser findByUsernameAndPassword(String username,String Password);
	
}