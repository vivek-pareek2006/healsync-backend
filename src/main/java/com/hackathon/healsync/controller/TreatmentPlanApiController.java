package com.hackathon.healsync.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hackathon.healsync.dto.TreatmentPlanRequestDto;
import com.hackathon.healsync.dto.TreatmentPlanResponseDto;
import com.hackathon.healsync.service.TreatmentPlanService;

@RestController
@RequestMapping("/v1/healsync/treatment-plan")
public class TreatmentPlanApiController {

    private final TreatmentPlanService treatmentPlanService;

    public TreatmentPlanApiController(TreatmentPlanService treatmentPlanService) {
        this.treatmentPlanService = treatmentPlanService;
    }

    /**
     * Get all treatment plans (for doctor dashboard)
     */
    @GetMapping("/all")
    public ResponseEntity<List<TreatmentPlanResponseDto>> getAllTreatmentPlans() {
        try {
            List<TreatmentPlanResponseDto> treatmentPlans = treatmentPlanService.getAllTreatmentPlans();
            return ResponseEntity.ok(treatmentPlans);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get treatment plan by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<TreatmentPlanResponseDto> getTreatmentPlanById(@PathVariable Integer id) {
        try {
            TreatmentPlanResponseDto treatmentPlan = treatmentPlanService.getTreatmentPlanById(id);
            if (treatmentPlan == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(treatmentPlan);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Create treatment plan (alternative endpoint with different path)
     */
    @PostMapping("/create")
    public ResponseEntity<?> createTreatmentPlan(@RequestBody TreatmentPlanCreateRequestDto requestDto) {
        try {
            // Convert the create request to the standard request format
            TreatmentPlanRequestDto standardRequest = new TreatmentPlanRequestDto();
            standardRequest.setDoctorId(requestDto.getDoctorId());
            standardRequest.setDiseaseId(requestDto.getDiseaseId());
            standardRequest.setStatus(requestDto.getStatus());
            standardRequest.setNotes(requestDto.getNotes());
            standardRequest.setMedicines(requestDto.getMedicines());

            TreatmentPlanResponseDto response = treatmentPlanService.createTreatmentPlan(
                requestDto.getPatientId(), standardRequest);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to create treatment plan: " + e.getMessage());
        }
    }

    /**
     * DTO for the alternative create endpoint that includes patientId in body
     */
    public static class TreatmentPlanCreateRequestDto {
        private Integer patientId;
        private Integer doctorId;
        private Integer diseaseId;
        private String status;
        private String notes;
        private List<TreatmentPlanRequestDto.MedicineDto> medicines;

        // Getters and Setters
        public Integer getPatientId() { return patientId; }
        public void setPatientId(Integer patientId) { this.patientId = patientId; }

        public Integer getDoctorId() { return doctorId; }
        public void setDoctorId(Integer doctorId) { this.doctorId = doctorId; }

        public Integer getDiseaseId() { return diseaseId; }
        public void setDiseaseId(Integer diseaseId) { this.diseaseId = diseaseId; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }

        public List<TreatmentPlanRequestDto.MedicineDto> getMedicines() { return medicines; }
        public void setMedicines(List<TreatmentPlanRequestDto.MedicineDto> medicines) { this.medicines = medicines; }
    }
}
