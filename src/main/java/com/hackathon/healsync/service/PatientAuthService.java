package com.hackathon.healsync.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hackathon.healsync.dto.PatientDto;
import com.hackathon.healsync.entity.Patient;
import com.hackathon.healsync.mapper.PatientMapper;
import com.hackathon.healsync.repository.PatientRepository;

@Service
public class PatientAuthService {
    private final PatientRepository patientRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public PatientAuthService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public PatientDto login(String email, String password) {
        Patient patient = patientRepository.findByEmail(email);
        if (patient != null && passwordEncoder.matches(password, patient.getPassword())) {
            return PatientMapper.toDto(patient);
        }
        return null;
    }
}
