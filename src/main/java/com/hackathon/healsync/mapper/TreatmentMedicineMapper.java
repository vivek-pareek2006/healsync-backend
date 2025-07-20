package com.hackathon.healsync.mapper;

import com.hackathon.healsync.entity.TreatmentMedicine;
import com.hackathon.healsync.dto.TreatmentMedicineDto;

public class TreatmentMedicineMapper {
    public static TreatmentMedicineDto toDto(TreatmentMedicine entity) {
        TreatmentMedicineDto dto = new TreatmentMedicineDto();
        dto.setTreatmentMedID(entity.getTreatmentMedID());
        dto.setTreatmentID(entity.getTreatmentID());
        dto.setDosage(entity.getDosage());
        dto.setTiming(entity.getTiming());
        dto.setMedicineName(entity.getMedicineName());
        dto.setUsageInfo(entity.getUsageInfo());
        dto.setSideEffect(entity.getSideEffect());
        dto.setTreatmentPlanId(entity.getTreatmentPlan() != null ? entity.getTreatmentPlan().getTreatmentId() : null);
        return dto;
    }

    public static TreatmentMedicine toEntity(TreatmentMedicineDto dto) {
        TreatmentMedicine entity = new TreatmentMedicine();
        entity.setTreatmentMedID(dto.getTreatmentMedID());
        entity.setTreatmentID(dto.getTreatmentID());
        entity.setDosage(dto.getDosage());
        entity.setTiming(dto.getTiming());
        entity.setMedicineName(dto.getMedicineName());
        entity.setUsageInfo(dto.getUsageInfo());
        entity.setSideEffect(dto.getSideEffect());
        // TreatmentPlan mapping can be handled separately if needed
        return entity;
    }
}
