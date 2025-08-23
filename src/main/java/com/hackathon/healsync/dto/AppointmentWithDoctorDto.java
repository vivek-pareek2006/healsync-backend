package com.hackathon.healsync.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Enhanced appointment DTO that includes complete doctor and patient information
 * Specifically designed to fix frontend display issues where doctor information is missing
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentWithDoctorDto {
    // Appointment Fields
    private Integer id; // Fixed ID field - using scheduleId as primary key
    private Integer scheduleId; // Original database primary key
    private Integer doctorId;
    private Integer patientId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String doctorNotes;
    private String prescription;
    
    // Doctor Information (populated via JOIN)
    private String doctorName;
    private String doctorSpecialty; // Corrected spelling from speaciality
    private String doctorEmail;
    private String doctorMobileNo;
    private String doctorBio;
    private String doctorShift;
    
    // Patient Information (populated via JOIN)
    private String patientName;
    private String patientEmail;
    private String patientMobileNo;
    private Integer patientAge;
    private String patientGender;
    
    // Constructor that sets id = scheduleId for frontend compatibility
    public AppointmentWithDoctorDto(Integer scheduleId, Integer doctorId, Integer patientId, 
                                  LocalDateTime startTime, LocalDateTime endTime, String status, 
                                  String doctorNotes, String prescription, String doctorName, 
                                  String doctorSpecialty, String doctorEmail, String doctorMobileNo,
                                  String doctorBio, String doctorShift, String patientName, 
                                  String patientEmail, String patientMobileNo, Integer patientAge, 
                                  String patientGender) {
        this.scheduleId = scheduleId;
        this.id = scheduleId; // Fix: Use scheduleId as the appointment ID for frontend
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.doctorNotes = doctorNotes;
        this.prescription = prescription;
        this.doctorName = doctorName;
        this.doctorSpecialty = doctorSpecialty;
        this.doctorEmail = doctorEmail;
        this.doctorMobileNo = doctorMobileNo;
        this.doctorBio = doctorBio;
        this.doctorShift = doctorShift;
        this.patientName = patientName;
        this.patientEmail = patientEmail;
        this.patientMobileNo = patientMobileNo;
        this.patientAge = patientAge;
        this.patientGender = patientGender;
    }
    
    // Ensure id is always synchronized with scheduleId
    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
        this.id = scheduleId;
    }
    
    public Integer getId() {
        return this.scheduleId; // Always return scheduleId as the ID
    }
    
    public void setId(Integer id) {
        this.id = id;
        this.scheduleId = id;
    }
}
