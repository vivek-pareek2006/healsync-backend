package com.hackathon.healsync.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hackathon.healsync.entity.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    List<Doctor> findBySpeacialityAndShift(String speaciality, String shift);
    List<Doctor> findBySpeaciality(String speaciality);
    Doctor findByEmail(String email);
    
    // Alias methods for correct spelling (Spring Data JPA will map to the actual field)
    default List<Doctor> findBySpecialtyAndShift(String specialty, String shift) {
        return findBySpeacialityAndShift(specialty, shift);
    }
    
    default List<Doctor> findBySpecialty(String specialty) {
        return findBySpeaciality(specialty);
    }
}
