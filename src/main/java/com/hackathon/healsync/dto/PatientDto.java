package com.hackathon.healsync.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDto {
    private Integer patientId;
    private String patientName;
    private Integer patientAge;
    private String gender;
    private String mobileNo;
    private String email;
    private String password;
    private List<Integer> treatmentPlanIds;

    // Explicit getters and setters for IDE compatibility
    public Integer getPatientId() { return patientId; }
    public void setPatientId(Integer patientId) { this.patientId = patientId; }
    
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    
    public Integer getPatientAge() { return patientAge; }
    public void setPatientAge(Integer patientAge) { this.patientAge = patientAge; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getMobileNo() { return mobileNo; }
    public void setMobileNo(String mobileNo) { this.mobileNo = mobileNo; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public List<Integer> getTreatmentPlanIds() { return treatmentPlanIds; }
    public void setTreatmentPlanIds(List<Integer> treatmentPlanIds) { this.treatmentPlanIds = treatmentPlanIds; }
}
