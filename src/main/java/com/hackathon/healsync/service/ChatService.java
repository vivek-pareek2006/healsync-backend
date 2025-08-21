package com.hackathon.healsync.service;

import com.hackathon.healsync.dto.ChatSessionDto;
import com.hackathon.healsync.dto.ChatMessageDto;
import com.hackathon.healsync.entity.ChatSession;
import com.hackathon.healsync.entity.ChatMessage;
import com.hackathon.healsync.repository.ChatSessionRepository;
import com.hackathon.healsync.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {
    @Autowired
    private ChatSessionRepository chatSessionRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public ChatSessionDto createSession(Integer doctorId, Integer patientId, Integer appointmentId) {
        ChatSession session = new ChatSession();
        session.setDoctorId(doctorId);
        session.setPatientId(patientId);
        session.setAppointmentId(appointmentId);
        session.setCreatedAt(LocalDateTime.now());
        ChatSession saved = chatSessionRepository.save(session);
        ChatSessionDto dto = new ChatSessionDto();
        dto.setId(saved.getId());
        dto.setDoctorId(saved.getDoctorId());
        dto.setPatientId(saved.getPatientId());
        dto.setAppointmentId(saved.getAppointmentId());
        dto.setCreatedAt(saved.getCreatedAt());
        return dto;
    }

    public ChatMessageDto sendMessage(Long chatSessionId, Integer senderId, Integer receiverId, String message) {
        ChatMessage msg = new ChatMessage();
        msg.setChatSessionId(chatSessionId);
        msg.setSenderId(senderId);
        msg.setReceiverId(receiverId);
        msg.setMessage(message);
        msg.setSentAt(LocalDateTime.now());
        msg.setRead(false);
        ChatMessage saved = chatMessageRepository.save(msg);
        ChatMessageDto dto = new ChatMessageDto();
        dto.setId(saved.getId());
        dto.setChatSessionId(saved.getChatSessionId());
        dto.setSenderId(saved.getSenderId());
        dto.setReceiverId(saved.getReceiverId());
        dto.setMessage(saved.getMessage());
        dto.setSentAt(saved.getSentAt());
        dto.setRead(saved.isRead());
        return dto;
    }

    public List<ChatMessageDto> getChatHistory(Long chatSessionId) {
        List<ChatMessage> messages = chatMessageRepository.findByChatSessionIdOrderBySentAtAsc(chatSessionId);
        return messages.stream().map(msg -> {
            ChatMessageDto dto = new ChatMessageDto();
            dto.setId(msg.getId());
            dto.setChatSessionId(msg.getChatSessionId());
            dto.setSenderId(msg.getSenderId());
            dto.setReceiverId(msg.getReceiverId());
            dto.setMessage(msg.getMessage());
            dto.setSentAt(msg.getSentAt());
            dto.setRead(msg.isRead());
            return dto;
        }).collect(Collectors.toList());
    }

    public List<ChatSessionDto> getSessionsForUser(Integer userId) {
        List<ChatSession> sessions = chatSessionRepository.findByDoctorIdOrPatientId(userId, userId);
        return sessions.stream().map(session -> {
            ChatSessionDto dto = new ChatSessionDto();
            dto.setId(session.getId());
            dto.setDoctorId(session.getDoctorId());
            dto.setPatientId(session.getPatientId());
            dto.setAppointmentId(session.getAppointmentId());
            dto.setCreatedAt(session.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());
    }

    public void markMessageAsRead(Long messageId) {
        ChatMessage msg = chatMessageRepository.findById(messageId)
            .orElseThrow(() -> new IllegalArgumentException("Message not found"));
        msg.setRead(true);
        chatMessageRepository.save(msg);
    }
}
