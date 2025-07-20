package com.hackathon.healsync.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentStatusDto {
    private Integer scheduleId;
    private Integer doctorId;
    private Integer patientId;
    private LocalDateTime startTIme;
    private LocalDateTime endTIme;
    private String status;
    private String doctorNotes;
}
