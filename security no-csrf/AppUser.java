package com.apptechnosoft.security;

import com.apptechnosoft.constant.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class AppUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String username;
	private String firstName;
	private String lastName;
	private String password;
	private String confirmPassword;
	@Enumerated(EnumType.STRING)
	@Column(length = 20) 
	private UserRole role; 
}