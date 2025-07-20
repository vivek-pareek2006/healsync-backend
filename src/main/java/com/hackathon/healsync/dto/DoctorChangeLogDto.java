package com.hackathon.healsync.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DoctorChangeLogDto {
    private Integer changeLogId;
    private Integer patientId;
    private Integer oldDoctorID;
    private Integer newDoctorId;
    private String reason;
    private LocalDateTime changeDate;
}
