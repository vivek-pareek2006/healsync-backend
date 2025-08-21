package com.hackathon.healsync.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatSessionDto {
    private Long id;
    private Integer doctorId;
    private Integer patientId;
    private Integer appointmentId;
    private LocalDateTime createdAt;
}
