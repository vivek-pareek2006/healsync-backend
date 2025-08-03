package com.hackathon.healsync.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyServiceDto {
    private Integer doctorId;
    private String doctorName;
    private String speaciality;
    private String mobileNo;
    private String email;
    private String currentShift;
    private boolean isOnCall;
    private LocalDateTime availableUntil;
    private String location;
    private String emergencyType;

    // Explicit getters and setters for IDE compatibility
    public Integer getDoctorId() { return doctorId; }
    public void setDoctorId(Integer doctorId) { this.doctorId = doctorId; }
    
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    
    public String getSpeaciality() { return speaciality; }
    public void setSpeaciality(String speaciality) { this.speaciality = speaciality; }
    
    public String getMobileNo() { return mobileNo; }
    public void setMobileNo(String mobileNo) { this.mobileNo = mobileNo; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getCurrentShift() { return currentShift; }
    public void setCurrentShift(String currentShift) { this.currentShift = currentShift; }
    
    public boolean isOnCall() { return isOnCall; }
    public void setOnCall(boolean onCall) { isOnCall = onCall; }
    
    public LocalDateTime getAvailableUntil() { return availableUntil; }
    public void setAvailableUntil(LocalDateTime availableUntil) { this.availableUntil = availableUntil; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getEmergencyType() { return emergencyType; }
    public void setEmergencyType(String emergencyType) { this.emergencyType = emergencyType; }
}
