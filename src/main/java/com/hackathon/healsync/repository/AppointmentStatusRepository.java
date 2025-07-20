package com.hackathon.healsync.repository;

import com.hackathon.healsync.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentStatusRepository extends JpaRepository<AppointmentStatus, Integer> {
    @Query("SELECT a FROM AppointmentStatus a WHERE a.doctorId = :doctorId AND ((a.startTIme < :endDateTime AND a.endTIme > :startDateTime))")
    List<AppointmentStatus> findConflictingAppointments(Integer doctorId, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
