package com.hackathon.healsync.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hackathon.healsync.entity.AppointmentStatus;

@Repository
public interface AppointmentStatusRepository extends JpaRepository<AppointmentStatus, Integer> {
    @Query("SELECT a FROM AppointmentStatus a WHERE a.doctorId = :doctorId AND ((a.startTime < :endDateTime AND a.endTime > :startDateTime))")
    List<AppointmentStatus> findConflictingAppointments(Integer doctorId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    // Derived queries for fetching appointments
    List<AppointmentStatus> findByPatientIdOrderByStartTimeDesc(Integer patientId);

    List<AppointmentStatus> findByDoctorIdOrderByStartTimeDesc(Integer doctorId);

    List<AppointmentStatus> findByDoctorIdAndStartTimeBetweenOrderByStartTimeAsc(Integer doctorId, LocalDateTime start, LocalDateTime end);

    // Advanced filtering query
    @Query("SELECT a FROM AppointmentStatus a WHERE " +
           "(:patientId IS NULL OR a.patientId = :patientId) AND " +
           "(:doctorId IS NULL OR a.doctorId = :doctorId) AND " +
           "(:status IS NULL OR a.status = :status) AND " +
           "(:startDate IS NULL OR a.startTime >= :startDate) AND " +
           "(:endDate IS NULL OR a.endTime <= :endDate) " +
           "ORDER BY a.startTime DESC")
    List<AppointmentStatus> findByFilters(@Param("patientId") Integer patientId,
                                         @Param("doctorId") Integer doctorId,
                                         @Param("status") String status,
                                         @Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate,
                                         Pageable pageable);

    // Upcoming appointments queries
    @Query("SELECT a FROM AppointmentStatus a WHERE a.patientId = :patientId AND a.startTime BETWEEN :now AND :futureDate ORDER BY a.startTime ASC")
    List<AppointmentStatus> findUpcomingByPatient(@Param("patientId") Integer patientId,
                                                 @Param("now") LocalDateTime now,
                                                 @Param("futureDate") LocalDateTime futureDate);

    @Query("SELECT a FROM AppointmentStatus a WHERE a.doctorId = :doctorId AND a.startTime BETWEEN :now AND :futureDate ORDER BY a.startTime ASC")
    List<AppointmentStatus> findUpcomingByDoctor(@Param("doctorId") Integer doctorId,
                                                @Param("now") LocalDateTime now,
                                                @Param("futureDate") LocalDateTime futureDate);

    @Query("SELECT a FROM AppointmentStatus a WHERE a.startTime BETWEEN :now AND :futureDate ORDER BY a.startTime ASC")
    List<AppointmentStatus> findAllUpcoming(@Param("now") LocalDateTime now,
                                           @Param("futureDate") LocalDateTime futureDate);

    // Statistics queries
    @Query("SELECT a FROM AppointmentStatus a WHERE a.doctorId = :doctorId AND a.startTime BETWEEN :start AND :end")
    List<AppointmentStatus> findByDoctorIdAndDateRange(@Param("doctorId") Integer doctorId,
                                                      @Param("start") LocalDateTime start,
                                                      @Param("end") LocalDateTime end);

    @Query("SELECT a FROM AppointmentStatus a WHERE a.startTime BETWEEN :start AND :end")
    List<AppointmentStatus> findByDateRange(@Param("start") LocalDateTime start,
                                           @Param("end") LocalDateTime end);

    // Search queries
    @Query("SELECT a FROM AppointmentStatus a WHERE a.patientId = :patientId AND " +
           "(LOWER(a.doctorNotes) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(a.status) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(a.prescription) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<AppointmentStatus> searchPatientAppointments(@Param("patientId") Integer patientId,
                                                     @Param("query") String query,
                                                     Pageable pageable);

    @Query("SELECT a FROM AppointmentStatus a WHERE a.doctorId = :doctorId AND " +
           "(LOWER(a.doctorNotes) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(a.status) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(a.prescription) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<AppointmentStatus> searchDoctorAppointments(@Param("doctorId") Integer doctorId,
                                                    @Param("query") String query,
                                                    Pageable pageable);

    @Query("SELECT a FROM AppointmentStatus a WHERE " +
           "LOWER(a.doctorNotes) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(a.status) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(a.prescription) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<AppointmentStatus> searchAllAppointments(@Param("query") String query,
                                                 Pageable pageable);

    // Find appointments by doctor ID for a specific date (day)
    @Query("SELECT a FROM AppointmentStatus a WHERE a.doctorId = :doctorId AND a.startTime >= :startOfDay AND a.startTime < :endOfDay")
    List<AppointmentStatus> findByDoctorIdAndDate(@Param("doctorId") Integer doctorId,
                                                 @Param("startOfDay") LocalDateTime startOfDay,
                                                 @Param("endOfDay") LocalDateTime endOfDay);
}
