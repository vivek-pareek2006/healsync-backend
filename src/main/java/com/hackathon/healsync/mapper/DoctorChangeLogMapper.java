package com.hackathon.healsync.mapper;

import com.hackathon.healsync.entity.DoctorChangeLog;
import com.hackathon.healsync.dto.DoctorChangeLogDto;

public class DoctorChangeLogMapper {
    public static DoctorChangeLogDto toDto(DoctorChangeLog entity) {
        DoctorChangeLogDto dto = new DoctorChangeLogDto();
        dto.setChangeLogId(entity.getChangeLogId());
        dto.setPatientId(entity.getPatientId());
        dto.setOldDoctorID(entity.getOldDoctorID());
        dto.setNewDoctorId(entity.getNewDoctorId());
        dto.setReason(entity.getReason());
        dto.setChangeDate(entity.getChangeDate());
        return dto;
    }

    public static DoctorChangeLog toEntity(DoctorChangeLogDto dto) {
        DoctorChangeLog entity = new DoctorChangeLog();
        entity.setChangeLogId(dto.getChangeLogId());
        entity.setPatientId(dto.getPatientId());
        entity.setOldDoctorID(dto.getOldDoctorID());
        entity.setNewDoctorId(dto.getNewDoctorId());
        entity.setReason(dto.getReason());
        entity.setChangeDate(dto.getChangeDate());
        return entity;
    }
}
