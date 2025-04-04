package com.apptechnosoft.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.apptechnosoft.constant.UserRole;

@Configuration
public class SecurityConfig {

 @Autowired
 private AppUserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(customizer -> customizer.disable())
//		http.csrf(Customizer.withDefaults())

                .authorizeHttpRequests(request -> request

                        .requestMatchers("/login","/fonts/**","/css/**", "/js/**",
                        		"/images/**","/vendors/**","/webjars/**","/assets/**").permitAll()
                        .requestMatchers("/dashboard","/").hasAnyRole(UserRole.ADMIN.name(),UserRole.DOCTOR.name(),UserRole.PATIENT.name())
//                        .requestMatchers("/reception/**","/admin/subject/**").hasAnyRole(UserRole.ADMIN.name(),UserRole.RECEPTIONIST.name(),UserRole.ACCOUNTANT.name())
//                        .requestMatchers("/account/**").hasAnyRole(UserRole.ADMIN.name(),UserRole.ACCOUNTANT.name())
                        .requestMatchers("/admin/**").hasAnyRole(UserRole.ADMIN.name()))
                .formLogin(form -> form
                        .loginPage("/login")             // URL of the login page
                        .loginProcessingUrl("/login")    // URL where login form is submitted
                        .successHandler(new CustomAuthenticationSuccessHandler()) // Custom success handler
                        .failureHandler(new CustomAuthenticationFailureHandler()) // Set the failure handler

                        .permitAll())
                .logout(logout -> logout
                		.logoutUrl("/logout") // URL to trigger logout
                	    .logoutSuccessUrl("/login") // Redirect to a clean login page after logout
                	    .invalidateHttpSession(true) // Invalidate session on logout
                	    .deleteCookies("JSESSIONID") // Remove session cookie
                        .permitAll());
        http.sessionManagement(session -> 
        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
               .maximumSessions(1) // Limit to one session per user
               .expiredUrl("/login?logout") // Redirect expired sessions
    );


        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
            http.getSharedObject(AuthenticationManagerBuilder.class);
        
        authenticationManagerBuilder
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
        
        
        
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

}
