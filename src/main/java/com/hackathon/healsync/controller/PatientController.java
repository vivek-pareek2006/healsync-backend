package com.hackathon.healsync.controller;

import com.hackathon.healsync.dto.PatientDto;
import com.hackathon.healsync.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/healsync/patient")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/add")
    public ResponseEntity<PatientDto> addPatient(@RequestBody PatientDto patientDto) {
        PatientDto savedPatient = patientService.addPatient(patientDto);
        return ResponseEntity.ok(savedPatient);
    }
}
