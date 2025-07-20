package com.hackathon.healsync.mapper;

import com.hackathon.healsync.entity.Disease;
import com.hackathon.healsync.dto.DiseaseDto;

public class DiseaseMapper {
    public static DiseaseDto toDto(Disease entity) {
        DiseaseDto dto = new DiseaseDto();
        dto.setDiseaseId(entity.getDiseaseId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPrecaution(entity.getPrecaution());
        return dto;
    }

    public static Disease toEntity(DiseaseDto dto) {
        Disease entity = new Disease();
        entity.setDiseaseId(dto.getDiseaseId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrecaution(dto.getPrecaution());
        return entity;
    }
}
