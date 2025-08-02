package com.hackathon.healsync.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDto {
    private Integer doctorId;
    private String name;
    private String speaciality;
    private String email;
    private String mobileNo;
    private String bio;
    private String shift;
    private String password;

    // Explicit getters and setters for IDE compatibility
    public Integer getDoctorId() { return doctorId; }
    public void setDoctorId(Integer doctorId) { this.doctorId = doctorId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getSpeaciality() { return speaciality; }
    public void setSpeaciality(String speaciality) { this.speaciality = speaciality; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getMobileNo() { return mobileNo; }
    public void setMobileNo(String mobileNo) { this.mobileNo = mobileNo; }
    
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    
    public String getShift() { return shift; }
    public void setShift(String shift) { this.shift = shift; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
