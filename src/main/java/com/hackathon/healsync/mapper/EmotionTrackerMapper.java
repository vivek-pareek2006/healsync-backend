package com.hackathon.healsync.mapper;

import com.hackathon.healsync.entity.EmotionTracker;
import com.hackathon.healsync.dto.EmotionTrackerDto;

public class EmotionTrackerMapper {
    public static EmotionTrackerDto toDto(EmotionTracker entity) {
        EmotionTrackerDto dto = new EmotionTrackerDto();
        dto.setId(entity.getId());
        dto.setPatientId(entity.getPatientId());
        dto.setEmotion(entity.getEmotion());
        dto.setPainLevel(entity.getPainLevel());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public static EmotionTracker toEntity(EmotionTrackerDto dto) {
        EmotionTracker entity = new EmotionTracker();
        entity.setId(dto.getId());
        entity.setPatientId(dto.getPatientId());
        entity.setEmotion(dto.getEmotion());
        entity.setPainLevel(dto.getPainLevel());
        entity.setCreatedAt(dto.getCreatedAt());
        return entity;
    }
}
