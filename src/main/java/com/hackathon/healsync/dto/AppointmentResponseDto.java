package com.hackathon.healsync.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class AppointmentResponseDto {
    private Integer appointmentId;
    private Integer doctorId;
    private String doctorName;
    private Integer patientId;
    private String patientName;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;
}
