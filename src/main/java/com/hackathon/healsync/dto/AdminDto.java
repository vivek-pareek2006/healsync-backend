package com.hackathon.healsync.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDto {
    private String email;
    private String password;
    private String role;
    private String name;
    
    // Constructor for response (without password)
    public AdminDto(String email, String role, String name) {
        this.email = email;
        this.role = role;
        this.name = name;
        this.password = null; // Never include password in response
    }
}
