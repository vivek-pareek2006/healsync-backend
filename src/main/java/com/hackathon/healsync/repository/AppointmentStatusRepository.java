package com.hackathon.healsync.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hackathon.healsync.entity.AppointmentStatus;

@Repository
public interface AppointmentStatusRepository extends JpaRepository<AppointmentStatus, Integer> {
    @Query("SELECT a FROM AppointmentStatus a WHERE a.doctorId = :doctorId AND ((a.startTime < :endDateTime AND a.endTime > :startDateTime))")
    List<AppointmentStatus> findConflictingAppointments(Integer doctorId, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
