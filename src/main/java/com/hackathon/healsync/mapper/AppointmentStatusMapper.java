package com.hackathon.healsync.mapper;

import com.hackathon.healsync.entity.AppointmentStatus;
import com.hackathon.healsync.dto.AppointmentStatusDto;

public class AppointmentStatusMapper {
    public static AppointmentStatusDto toDto(AppointmentStatus entity) {
        AppointmentStatusDto dto = new AppointmentStatusDto();
        dto.setScheduleId(entity.getScheduleId());
        dto.setDoctorId(entity.getDoctorId());
        dto.setPatientId(entity.getPatientId());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setStatus(entity.getStatus());
        dto.setDoctorNotes(entity.getDoctorNotes());
        return dto;
    }

    public static AppointmentStatus toEntity(AppointmentStatusDto dto) {
        AppointmentStatus entity = new AppointmentStatus();
        entity.setScheduleId(dto.getScheduleId());
        entity.setDoctorId(dto.getDoctorId());
        entity.setPatientId(dto.getPatientId());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        entity.setStatus(dto.getStatus());
        entity.setDoctorNotes(dto.getDoctorNotes());
        return entity;
    }
}
