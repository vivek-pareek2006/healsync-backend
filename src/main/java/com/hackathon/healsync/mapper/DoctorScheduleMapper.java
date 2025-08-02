package com.hackathon.healsync.mapper;

import com.hackathon.healsync.dto.DoctorScheduleDto;
import com.hackathon.healsync.entity.DoctorSchedule;
import org.springframework.stereotype.Component;

@Component
public class DoctorScheduleMapper {

    public DoctorScheduleDto toDto(DoctorSchedule schedule) {
        if (schedule == null) {
            return null;
        }
        
        DoctorScheduleDto dto = new DoctorScheduleDto();
        dto.setScheduleId(schedule.getScheduleId());
        dto.setDoctorId(schedule.getDoctor().getDoctorId());
        dto.setDoctorName(schedule.getDoctor().getName());
        dto.setDayOfWeek(schedule.getDayOfWeek());
        dto.setShiftType(schedule.getShiftType());
        dto.setStartTime(schedule.getStartTime());
        dto.setEndTime(schedule.getEndTime());
        dto.setIsActive(schedule.getIsActive());
        
        return dto;
    }

    public DoctorSchedule toEntity(DoctorScheduleDto dto) {
        if (dto == null) {
            return null;
        }
        
        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setScheduleId(dto.getScheduleId());
        schedule.setDayOfWeek(dto.getDayOfWeek());
        schedule.setShiftType(dto.getShiftType());
        schedule.setStartTime(dto.getStartTime());
        schedule.setEndTime(dto.getEndTime());
        schedule.setIsActive(dto.getIsActive());
        
        return schedule;
    }
}
