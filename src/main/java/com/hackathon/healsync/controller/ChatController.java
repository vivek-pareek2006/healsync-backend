package com.hackathon.healsync.controller;

import com.hackathon.healsync.dto.ChatSessionDto;
import com.hackathon.healsync.dto.ChatMessageDto;
import com.hackathon.healsync.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @PostMapping("/session")
    public ResponseEntity<ChatSessionDto> createSession(@RequestBody ChatSessionDto dto) {
        ChatSessionDto session = chatService.createSession(dto.getDoctorId(), dto.getPatientId(), dto.getAppointmentId());
        return ResponseEntity.ok(session);
    }

    @PostMapping("/messages")
    public ResponseEntity<ChatMessageDto> sendMessage(@RequestBody ChatMessageDto dto) {
        ChatMessageDto msg = chatService.sendMessage(dto.getChatSessionId(), dto.getSenderId(), dto.getReceiverId(), dto.getMessage());
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/messages")
    public ResponseEntity<List<ChatMessageDto>> getChatHistory(@RequestParam Long chatSessionId) {
        List<ChatMessageDto> history = chatService.getChatHistory(chatSessionId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/sessions")
    public ResponseEntity<List<ChatSessionDto>> getSessionsForUser(@RequestParam Integer userId) {
        List<ChatSessionDto> sessions = chatService.getSessionsForUser(userId);
        return ResponseEntity.ok(sessions);
    }

    @PutMapping("/messages/{messageId}/read")
    public ResponseEntity<?> markMessageAsRead(@PathVariable Long messageId) {
        chatService.markMessageAsRead(messageId);
        return ResponseEntity.ok().body("Message marked as read");
    }
}
