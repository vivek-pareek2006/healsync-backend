package com.hackathon.healsync.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hackathon.healsync.entity.EmotionTracker;

@Repository
public interface EmotionTrackerRepository extends JpaRepository<EmotionTracker, Integer> {
    List<EmotionTracker> findByPatientId(Integer patientId);
}
