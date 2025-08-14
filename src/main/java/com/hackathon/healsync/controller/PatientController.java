package com.hackathon.healsync.controller;

import com.hackathon.healsync.dto.PatientDto;
import com.hackathon.healsync.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    
        /**
         * Update patient details
         */
        @PutMapping("/{patientId}")
        public ResponseEntity<?> updatePatient(@PathVariable Integer patientId, @RequestBody PatientDto patientDto) {
            try {
                PatientDto updated = patientService.updatePatient(patientId, patientDto);
                return ResponseEntity.ok(updated);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            } catch (Exception e) {
                return ResponseEntity.internalServerError().body("Failed to update patient: " + e.getMessage());
            }
        }
    
        /**
         * Delete patient
         */
        @DeleteMapping("/{patientId}")
        public ResponseEntity<?> deletePatient(@PathVariable Integer patientId) {
            try {
                patientService.deletePatient(patientId);
                return ResponseEntity.ok().body("Patient deleted successfully");
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            } catch (Exception e) {
                return ResponseEntity.internalServerError().body("Failed to delete patient: " + e.getMessage());
            }
        }
}
