package com.hackathon.healsync.repository;

import com.hackathon.healsync.entity.TreatmentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TreatmentPlanRepository extends JpaRepository<TreatmentPlan, Integer> {
    
    // Find all treatment plans created by a specific doctor
    List<TreatmentPlan> findByDoctorIdOrderByStartDateDesc(Integer doctorId);
    
    // Find all treatment plans for a specific patient
    @Query("SELECT tp FROM TreatmentPlan tp WHERE tp.patient.patientId = :patientId ORDER BY tp.startDate DESC")
    List<TreatmentPlan> findByPatientIdOrderByStartDateDesc(@Param("patientId") Integer patientId);
    
    // Find treatment plans by patient ID with doctor information
    @Query("SELECT tp FROM TreatmentPlan tp " +
           "LEFT JOIN FETCH tp.patient " +
           "WHERE tp.patient.patientId = :patientId " +
           "ORDER BY tp.startDate DESC")
    List<TreatmentPlan> findByPatientIdWithDoctorInfo(@Param("patientId") Integer patientId);
}
