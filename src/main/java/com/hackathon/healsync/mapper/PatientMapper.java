package com.hackathon.healsync.mapper;

import com.hackathon.healsync.entity.Patient;
import com.hackathon.healsync.dto.PatientDto;
import java.util.stream.Collectors;

public class PatientMapper {
    public static PatientDto toDto(Patient entity) {
        PatientDto dto = new PatientDto();
        dto.setPatientId(entity.getPatientId());
        dto.setPatientName(entity.getPatientName());
        dto.setPatientAge(entity.getPatientAge());
        dto.setGender(entity.getGender());
        dto.setMobileNo(entity.getMobileNo());
        dto.setEmail(entity.getEmail());
        if (entity.getTreatmentPlans() != null) {
            dto.setTreatmentPlanIds(entity.getTreatmentPlans().stream().map(tp -> tp.getTreatmentId()).collect(Collectors.toList()));
        }
        return dto;
    }

    public static Patient toEntity(PatientDto dto) {
        Patient entity = new Patient();
        entity.setPatientId(dto.getPatientId());
        entity.setPatientName(dto.getPatientName());
        entity.setPatientAge(dto.getPatientAge());
        entity.setGender(dto.getGender());
        entity.setMobileNo(dto.getMobileNo());
        entity.setEmail(dto.getEmail());
        // TreatmentPlans mapping can be handled separately if needed
        return entity;
    }
}
