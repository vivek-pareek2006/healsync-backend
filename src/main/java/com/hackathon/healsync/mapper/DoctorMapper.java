package com.hackathon.healsync.mapper;

import com.hackathon.healsync.entity.Doctor;
import com.hackathon.healsync.dto.DoctorDto;

public class DoctorMapper {
    public static DoctorDto toDto(Doctor entity) {
        DoctorDto dto = new DoctorDto();
        dto.setDoctorId(entity.getDoctorId());
        dto.setName(entity.getName());
        dto.setSpeaciality(entity.getSpeaciality());
        dto.setEmail(entity.getEmail());
        dto.setMobileNo(entity.getMobileNo());
        dto.setBio(entity.getBio());
        dto.setShift(entity.getShift());
        return dto;
    }

    public static Doctor toEntity(DoctorDto dto) {
        Doctor entity = new Doctor();
        entity.setDoctorId(dto.getDoctorId());
        entity.setName(dto.getName());
        entity.setSpeaciality(dto.getSpeaciality());
        entity.setEmail(dto.getEmail());
        entity.setMobileNo(dto.getMobileNo());
        entity.setBio(dto.getBio());
        entity.setShift(dto.getShift());
        return entity;
    }
}
