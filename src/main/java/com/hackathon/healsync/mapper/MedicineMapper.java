package com.hackathon.healsync.mapper;

import com.hackathon.healsync.entity.Medicine;
import com.hackathon.healsync.dto.MedicineDto;

public class MedicineMapper {
    public static MedicineDto toDto(Medicine entity) {
        MedicineDto dto = new MedicineDto();
        dto.setMedicineId(entity.getMedicineId());
        dto.setName(entity.getName());
        dto.setUsageInfo(entity.getUsageInfo());
        dto.setSideEffect(entity.getSideEffect());
        return dto;
    }

    public static Medicine toEntity(MedicineDto dto) {
        Medicine entity = new Medicine();
        entity.setMedicineId(dto.getMedicineId());
        entity.setName(dto.getName());
        entity.setUsageInfo(dto.getUsageInfo());
        entity.setSideEffect(dto.getSideEffect());
        return entity;
    }
}
