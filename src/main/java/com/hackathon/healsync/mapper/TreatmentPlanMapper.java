package com.hackathon.healsync.mapper;

import com.hackathon.healsync.entity.TreatmentPlan;
import com.hackathon.healsync.dto.TreatmentPlanDto;
import java.util.List;
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
            List<Integer> medicineIds = entity.getTreatmentMedicines().stream()
                .map(tm -> tm.getTreatmentMedID())
                .collect(Collectors.toList());
            dto.setTreatmentMedicineIds(medicineIds);
        }
        // TODO: Calculate bill from treatment medicines
        // dto.setBill(entity.getBill());
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
        // TODO: Handle bill field - maybe add to entity or calculate separately
        // entity.setBill(dto.getBill());
        // Patient and TreatmentMedicines mapping can be handled separately if needed
        return entity;
    }
}
