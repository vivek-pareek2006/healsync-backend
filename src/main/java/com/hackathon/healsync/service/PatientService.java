package com.hackathon.healsync.service;

import com.hackathon.healsync.dto.PatientDto;
import com.hackathon.healsync.entity.Patient;
import com.hackathon.healsync.mapper.PatientMapper;
import com.hackathon.healsync.repository.PatientRepository;
import org.springframework.stereotype.Service;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public PatientDto addPatient(PatientDto patientDto) {
        Patient patient = PatientMapper.toEntity(patientDto);
        Patient savedPatient = patientRepository.save(patient);
        return PatientMapper.toDto(savedPatient);
    }
}
