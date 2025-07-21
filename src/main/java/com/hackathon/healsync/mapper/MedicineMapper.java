package com.hackathon.healsync.mapper;

import com.hackathon.healsync.dto.MedicineDto;
import com.hackathon.healsync.entity.Medicine;

public class MedicineMapper {
    public static Medicine toEntity(MedicineDto dto) {
        Medicine medicine = new Medicine();
        medicine.setMedicineId(dto.getMedicineId());
        medicine.setName(dto.getName());
        medicine.setUsage(dto.getUsage());
        medicine.setSideEffect(dto.getSideEffect());
        return medicine;
    }

    public static MedicineDto toDto(Medicine entity) {
        MedicineDto dto = new MedicineDto();
        dto.setMedicineId(entity.getMedicineId());
        dto.setName(entity.getName());
        dto.setUsage(entity.getUsage());
        dto.setSideEffect(entity.getSideEffect());
        return dto;
    }
}
