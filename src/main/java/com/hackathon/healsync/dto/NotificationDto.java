package com.hackathon.healsync.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationDto {
    private Integer notificationId;
    private String sentFrom;
    private String sentTo;
    private String message;
    private LocalDateTime timestamp;
}
