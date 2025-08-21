package com.hackathon.healsync.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatMessageDto {
    private Long id;
    private Long chatSessionId;
    private Integer senderId;
    private Integer receiverId;
    private String message;
    private LocalDateTime sentAt;
    private boolean read;
}
