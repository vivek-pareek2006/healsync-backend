package com.hackathon.healsync.controller;

import com.hackathon.healsync.dto.TreatmentPlanRequestDto;
import com.hackathon.healsync.dto.TreatmentPlanResponseDto;
import com.hackathon.healsync.dto.TreatmentPlanListDto;
import com.hackathon.healsync.service.TreatmentPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/patients/{patientId}/treatment-plans")
public class TreatmentPlanController {

    private final TreatmentPlanService treatmentPlanService;

    public TreatmentPlanController(TreatmentPlanService treatmentPlanService) {
        this.treatmentPlanService = treatmentPlanService;
    }

    @PostMapping
    public ResponseEntity<?> createTreatmentPlan(
            @PathVariable Integer patientId,
            @RequestBody TreatmentPlanRequestDto requestDto) {
        try {
            TreatmentPlanResponseDto response = treatmentPlanService.createTreatmentPlan(patientId, requestDto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to create treatment plan: " + e.getMessage());
        }
    }

    /**
     * Get all treatment plans for a specific patient with different doctors
     */
    @GetMapping
    public ResponseEntity<?> getPatientTreatmentPlans(@PathVariable Integer patientId) {
        try {
            List<TreatmentPlanListDto> treatmentPlans = treatmentPlanService.getTreatmentPlansByPatient(patientId);
            return ResponseEntity.ok(treatmentPlans);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to retrieve treatment plans: " + e.getMessage());
        }
    }
}