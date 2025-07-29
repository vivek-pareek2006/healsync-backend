package com.hackathon.healsync.controller;

import com.hackathon.healsync.dto.EmotionTrackerDto;
import com.hackathon.healsync.service.EmotionTrackerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/healsync/emotion")
public class EmotionTrackerController {
    private final EmotionTrackerService emotionTrackerService;

    public EmotionTrackerController(EmotionTrackerService emotionTrackerService) {
        this.emotionTrackerService = emotionTrackerService;
    }

    @PostMapping("/track")
    public ResponseEntity<EmotionTrackerDto> trackEmotion(@RequestBody EmotionTrackerDto dto) {
        EmotionTrackerDto saved = emotionTrackerService.saveEmotion(dto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<EmotionTrackerDto>> getEmotions(@PathVariable Integer patientId) {
        List<EmotionTrackerDto> emotions = emotionTrackerService.getEmotionsByPatientId(patientId);
        return ResponseEntity.ok(emotions);
    }
}
