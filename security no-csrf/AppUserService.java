package com.apptechnosoft.security;

import java.util.List;

public interface AppUserService {
	void addUser(AppUser appUser);
	AppUser findByUsernameAndPassword(String username, String password);
	AppUser  findByUsername(String username);
	List<AppUser> getAllUser();
	AppUser getUserById(int id);
	AppUser updateUser(AppUser appUser);
	void deleteUserById(int id);

}