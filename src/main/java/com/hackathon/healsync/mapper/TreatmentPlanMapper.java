package com.hackathon.healsync.mapper;

import com.hackathon.healsync.entity.TreatmentPlan;
import com.hackathon.healsync.dto.TreatmentPlanDto;
import java.util.stream.Collectors;

public class TreatmentPlanMapper {
    public static TreatmentPlanDto toDto(TreatmentPlan entity) {
        TreatmentPlanDto dto = new TreatmentPlanDto();
        dto.setTreatmentId(entity.getTreatmentId());
        dto.setDiseaseId(entity.getDiseaseId());
        dto.setDoctorId(entity.getDoctorId());
        dto.setPatientId(entity.getPatient() != null ? entity.getPatient().getPatientId() : null);
        dto.setStatus(entity.getStatus());
        dto.setStartDate(entity.getStartDate());
        dto.setNotes(entity.getNotes());
        if (entity.getTreatmentMedicines() != null) {
            dto.setTreatmentMedicineIds(entity.getTreatmentMedicines().stream().map(tm -> tm.getTreatmentMedID()).collect(Collectors.toList()));
        }
        dto.setBill(entity.getBill());
        return dto;
    }

    public static TreatmentPlan toEntity(TreatmentPlanDto dto) {
        TreatmentPlan entity = new TreatmentPlan();
        entity.setTreatmentId(dto.getTreatmentId());
        entity.setDiseaseId(dto.getDiseaseId());
        entity.setDoctorId(dto.getDoctorId());
        entity.setStatus(dto.getStatus());
        entity.setStartDate(dto.getStartDate());
        entity.setNotes(dto.getNotes());
        entity.setBill(dto.getBill());
        // Patient and TreatmentMedicines mapping can be handled separately if needed
        return entity;
    }
}
