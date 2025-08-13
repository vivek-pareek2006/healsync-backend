package com.hackathon.healsync.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class TreatmentPlanListDto {
    private Integer treatmentId;
    private Integer diseaseId;
    private String diseaseName;
    private Integer doctorId;
    private String doctorName;
    private String doctorSpecialty;
    private Integer patientId;
    private String patientName;
    private String status;
    private LocalDate startDate;
    private String notes;
    private List<TreatmentMedicineDto> medicines;
    
    // Explicit getters and setters for IDE compatibility
    public Integer getTreatmentId() { return treatmentId; }
    public void setTreatmentId(Integer treatmentId) { this.treatmentId = treatmentId; }
    
    public Integer getDiseaseId() { return diseaseId; }
    public void setDiseaseId(Integer diseaseId) { this.diseaseId = diseaseId; }
    
    public String getDiseaseName() { return diseaseName; }
    public void setDiseaseName(String diseaseName) { this.diseaseName = diseaseName; }
    
    public Integer getDoctorId() { return doctorId; }
    public void setDoctorId(Integer doctorId) { this.doctorId = doctorId; }
    
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    
    public String getDoctorSpecialty() { return doctorSpecialty; }
    public void setDoctorSpecialty(String doctorSpecialty) { this.doctorSpecialty = doctorSpecialty; }
    
    public Integer getPatientId() { return patientId; }
    public void setPatientId(Integer patientId) { this.patientId = patientId; }
    
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public List<TreatmentMedicineDto> getMedicines() { return medicines; }
    public void setMedicines(List<TreatmentMedicineDto> medicines) { this.medicines = medicines; }
}
