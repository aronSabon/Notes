package com.apptechnosoft.security;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
	    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
	 
	    System.out.println("User authenticated: " + authentication.getName());
        System.out.println("Authorities: " + authentication.getAuthorities());
	    authorities.forEach(authority -> System.out.println("Granted Authority: " + authority.getAuthority()));
	    response.sendRedirect("/dashboard");
	}

}