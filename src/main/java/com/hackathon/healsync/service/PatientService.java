package com.hackathon.healsync.service;

import com.hackathon.healsync.dto.PatientDto;
import com.hackathon.healsync.entity.Patient;
import com.hackathon.healsync.mapper.PatientMapper;
import com.hackathon.healsync.repository.PatientRepository;
import com.hackathon.healsync.service.mailer.RegistrationMailer;
import org.springframework.stereotype.Service;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final RegistrationMailer registrationMailer;

    public PatientService(PatientRepository patientRepository, RegistrationMailer registrationMailer) {
        this.patientRepository = patientRepository;
        this.registrationMailer = registrationMailer;
    }

    public PatientDto addPatient(PatientDto patientDto) {
        Patient patient = PatientMapper.toEntity(patientDto);
        Patient savedPatient = patientRepository.save(patient);
        PatientDto result = PatientMapper.toDto(savedPatient);
        // Fire-and-forget email; failures should not block registration
        try { registrationMailer.sendRegistrationEmail(result); } catch (Exception ignored) {}
        return result;
    }

    public PatientDto updatePatient(Integer patientId, PatientDto patientDto) {
        Patient patient = patientRepository.findById(patientId)
            .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        // Update fields
        patient.setPatientName(patientDto.getPatientName());
        patient.setPatientAge(patientDto.getPatientAge());
        patient.setGender(patientDto.getGender());
        patient.setMobileNo(patientDto.getMobileNo());
        patient.setEmail(patientDto.getEmail());
        if (patientDto.getPassword() != null && !patientDto.getPassword().isEmpty()) {
            patient.setPassword(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(patientDto.getPassword()));
        }
        Patient saved = patientRepository.save(patient);
        return PatientMapper.toDto(saved);
    }

    public void deletePatient(Integer patientId) {
        Patient patient = patientRepository.findById(patientId)
            .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        patientRepository.delete(patient);
    }
}
