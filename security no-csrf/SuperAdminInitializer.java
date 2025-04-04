package com.apptechnosoft.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.apptechnosoft.constant.UserRole;

@Component
public class SuperAdminInitializer implements CommandLineRunner {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if a SUPERADMIN already exists
        if (appUserRepository.findByUsername("superadmin") == null) {
            // Create a SUPERADMIN user
            AppUser superAdmin = new AppUser();
            superAdmin.setUsername("superadmin");
            superAdmin.setPassword(passwordEncoder.encode("superadmin")); // Hardcoded password (use a strong one)
            superAdmin.setRole(UserRole.ADMIN);

            // Save to the database
            appUserRepository.save(superAdmin);
            System.out.println("SuperAdmin user created successfully!");
        }
    }
}