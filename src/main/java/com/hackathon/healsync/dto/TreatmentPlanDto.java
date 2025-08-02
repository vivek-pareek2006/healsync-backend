package com.hackathon.healsync.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class TreatmentPlanDto {
    private Integer treatmentId;
    private Integer diseaseId;
    private Integer doctorId;
    private Integer patientId;
    private String status;
    private LocalDate startDate;
    private String notes;
    private List<Integer> treatmentMedicineIds;
    private BigDecimal bill;
    
    // Explicit getters and setters for IDE compatibility
    public Integer getTreatmentId() { return treatmentId; }
    public void setTreatmentId(Integer treatmentId) { this.treatmentId = treatmentId; }
    
    public Integer getDiseaseId() { return diseaseId; }
    public void setDiseaseId(Integer diseaseId) { this.diseaseId = diseaseId; }
    
    public Integer getDoctorId() { return doctorId; }
    public void setDoctorId(Integer doctorId) { this.doctorId = doctorId; }
    
    public Integer getPatientId() { return patientId; }
    public void setPatientId(Integer patientId) { this.patientId = patientId; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public List<Integer> getTreatmentMedicineIds() { return treatmentMedicineIds; }
    public void setTreatmentMedicineIds(List<Integer> treatmentMedicineIds) { this.treatmentMedicineIds = treatmentMedicineIds; }
    
    public BigDecimal getBill() { return bill; }
    public void setBill(BigDecimal bill) { this.bill = bill; }
}
