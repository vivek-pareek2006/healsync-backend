package com.hackathon.healsync.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hackathon.healsync.dto.AdminDto;

@Service
public class AdminAuthService {
    
    @Value("${admin.email}")
    private String adminEmail;
    
    @Value("${admin.password}")
    private String adminPassword;

    public AdminDto authenticateAdmin(String email, String password) {
        if (adminEmail.equals(email) && adminPassword.equals(password)) {
            // Return admin details without password
            return new AdminDto(adminEmail, "ADMIN", "System Administrator");
        }
        return null;
    }
    
    public boolean isValidAdmin(String email) {
        return adminEmail.equals(email);
    }
}
