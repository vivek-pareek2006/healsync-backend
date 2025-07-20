package com.hackathon.healsync.mapper;

import com.hackathon.healsync.entity.Notification;
import com.hackathon.healsync.dto.NotificationDto;

public class NotificationMapper {
    public static NotificationDto toDto(Notification entity) {
        NotificationDto dto = new NotificationDto();
        dto.setNotificationId(entity.getNotificationId());
        dto.setSentFrom(entity.getSentFrom());
        dto.setSentTo(entity.getSentTo());
        dto.setMessage(entity.getMessage());
        dto.setTimestamp(entity.getTimestamp());
        return dto;
    }

    public static Notification toEntity(NotificationDto dto) {
        Notification entity = new Notification();
        entity.setNotificationId(dto.getNotificationId());
        entity.setSentFrom(dto.getSentFrom());
        entity.setSentTo(dto.getSentTo());
        entity.setMessage(dto.getMessage());
        entity.setTimestamp(dto.getTimestamp());
        return entity;
    }
}
