package com.hackathon.healsync.repository;

import com.hackathon.healsync.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    List<ChatSession> findByDoctorIdOrPatientId(Integer doctorId, Integer patientId);
    ChatSession findByAppointmentId(Integer appointmentId);
}
