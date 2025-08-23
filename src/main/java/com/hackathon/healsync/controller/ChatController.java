package com.hackathon.healsync.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hackathon.healsync.dto.ChatMessageDto;
import com.hackathon.healsync.dto.ChatSessionDto;
import com.hackathon.healsync.service.ChatService;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;

    // Test endpoint to verify controller is working
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Chat API is working!");
    }

    @PostMapping("/session")
    public ResponseEntity<?> createSession(@RequestBody ChatSessionDto dto) {
        try {
            ChatSessionDto session = chatService.createSession(dto.getDoctorId(), dto.getPatientId(), dto.getAppointmentId());
            return ResponseEntity.ok(session);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating chat session: " + e.getMessage());
        }
    }

    @PostMapping("/messages")
    public ResponseEntity<?> sendMessage(@RequestBody ChatMessageDto dto) {
        try {
            ChatMessageDto msg = chatService.sendMessage(dto.getChatSessionId(), dto.getSenderId(), dto.getReceiverId(), dto.getMessage());
            return ResponseEntity.ok(msg);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error sending message: " + e.getMessage());
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<?> getChatHistory(@RequestParam Long chatSessionId) {
        try {
            List<ChatMessageDto> history = chatService.getChatHistory(chatSessionId);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting chat history: " + e.getMessage());
        }
    }

    @GetMapping("/sessions")
    public ResponseEntity<?> getSessionsForUser(@RequestParam Integer userId) {
        try {
            List<ChatSessionDto> sessions = chatService.getSessionsForUser(userId);
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting chat sessions: " + e.getMessage());
        }
    }

    @PutMapping("/messages/{messageId}/read")
    public ResponseEntity<?> markMessageAsRead(@PathVariable Long messageId) {
        try {
            chatService.markMessageAsRead(messageId);
            return ResponseEntity.ok().body("Message marked as read");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error marking message as read: " + e.getMessage());
        }
    }
}
