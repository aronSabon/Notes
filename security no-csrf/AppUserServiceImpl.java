package com.apptechnosoft.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppUserServiceImpl implements AppUserService {
	@Autowired
	AppUserRepository appUserRepository;
		@Override
		public void addUser(AppUser appUser) {
			// TODO Auto-generated method stub
			appUserRepository.save(appUser);
		}
		@Override
		public AppUser findByUsernameAndPassword(String username, String password) {
			// TODO Auto-generated method stub
			return appUserRepository.findByUsernameAndPassword(username, password);
		}
		@Override
		public AppUser findByUsername(String username) {
			// TODO Auto-generated method stub
			return appUserRepository.findByUsername(username);
		}
		@Override
		public List<AppUser> getAllUser() {
			// TODO Auto-generated method stub
			return appUserRepository.findAll();
		}
		@Override
		public AppUser getUserById(int id) {
			// TODO Auto-generated method stub
			return appUserRepository.findById(id).get();
		}
		@Override
		public AppUser updateUser(AppUser appUser) {
			// TODO Auto-generated method stub
			return appUserRepository.save(appUser);
		}
		@Override
		public void deleteUserById(int id) {
			// TODO Auto-generated method stub
			appUserRepository.deleteById(id);
		}


}