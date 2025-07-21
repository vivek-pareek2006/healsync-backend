package com.hackathon.healsync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hackathon.healsync.entity.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Patient findByEmail(String email);
}
