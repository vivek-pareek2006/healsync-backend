package com.hackathon.healsync.service;

import com.hackathon.healsync.dto.EmotionTrackerDto;
import com.hackathon.healsync.entity.EmotionTracker;
import com.hackathon.healsync.mapper.EmotionTrackerMapper;
import com.hackathon.healsync.repository.EmotionTrackerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmotionTrackerService {
    private final EmotionTrackerRepository emotionTrackerRepository;

    public EmotionTrackerService(EmotionTrackerRepository emotionTrackerRepository) {
        this.emotionTrackerRepository = emotionTrackerRepository;
    }

    public EmotionTrackerDto saveEmotion(EmotionTrackerDto dto) {
        EmotionTracker entity = EmotionTrackerMapper.toEntity(dto);
        entity.setCreatedAt(LocalDateTime.now());
        EmotionTracker saved = emotionTrackerRepository.save(entity);
        return EmotionTrackerMapper.toDto(saved);
    }

    public List<EmotionTrackerDto> getEmotionsByPatientId(Integer patientId) {
        return emotionTrackerRepository.findByPatientId(patientId)
                .stream()
                .map(EmotionTrackerMapper::toDto)
                .collect(Collectors.toList());
    }
}
