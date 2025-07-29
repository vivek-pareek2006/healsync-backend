package com.hackathon.healsync.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class EmotionTrackerDto {
    private Integer id;
    private Integer patientId;
    private String emotion;
    private Integer painLevel;
    private LocalDateTime createdAt;
}
