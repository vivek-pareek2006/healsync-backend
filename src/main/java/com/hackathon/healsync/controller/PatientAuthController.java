package com.hackathon.healsync.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hackathon.healsync.dto.PatientDto;
import com.hackathon.healsync.service.PatientAuthService;

@RestController
@RequestMapping("/v1/healsync/patient")
public class PatientAuthController {
    private final PatientAuthService patientAuthService;

    public PatientAuthController(PatientAuthService patientAuthService) {
        this.patientAuthService = patientAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody PatientDto loginDto) {
        PatientDto patient = patientAuthService.login(loginDto.getEmail(), loginDto.getPassword());
        if (patient != null) {
            return ResponseEntity.ok(patient);
        } else {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }
}
