package com.hackathon.healsync.controller;

import com.hackathon.healsync.dto.TreatmentPlanListDto;
import com.hackathon.healsync.service.TreatmentPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/doctors/{doctorId}/treatment-plans")
public class DoctorTreatmentPlanController {

    private final TreatmentPlanService treatmentPlanService;

    public DoctorTreatmentPlanController(TreatmentPlanService treatmentPlanService) {
        this.treatmentPlanService = treatmentPlanService;
    }

    /**
     * Get all treatment plans created by a specific doctor
     */
    @GetMapping
    public ResponseEntity<?> getDoctorTreatmentPlans(@PathVariable Integer doctorId) {
        try {
            List<TreatmentPlanListDto> treatmentPlans = treatmentPlanService.getTreatmentPlansByDoctor(doctorId);
            return ResponseEntity.ok(treatmentPlans);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to retrieve doctor's treatment plans: " + e.getMessage());
        }
    }
}
