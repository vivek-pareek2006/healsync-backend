package com.hackathon.healsync.service;

import com.hackathon.healsync.dto.DoctorScheduleDto;
import com.hackathon.healsync.dto.DoctorBlockDto;
import com.hackathon.healsync.dto.ScheduleRequestDto;
import com.hackathon.healsync.entity.Doctor;
import com.hackathon.healsync.entity.DoctorSchedule;
import com.hackathon.healsync.entity.DoctorBlock;
import com.hackathon.healsync.mapper.DoctorScheduleMapper;
import com.hackathon.healsync.mapper.DoctorBlockMapper;
import com.hackathon.healsync.repository.DoctorRepository;
import com.hackathon.healsync.repository.DoctorScheduleRepository;
import com.hackathon.healsync.repository.DoctorBlockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoctorScheduleService {

    private final DoctorScheduleRepository scheduleRepository;
    private final DoctorBlockRepository blockRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorScheduleMapper scheduleMapper;
    private final DoctorBlockMapper blockMapper;

    public DoctorScheduleService(DoctorScheduleRepository scheduleRepository, 
                               DoctorBlockRepository blockRepository,
                               DoctorRepository doctorRepository,
                               DoctorScheduleMapper scheduleMapper,
                               DoctorBlockMapper blockMapper) {
        this.scheduleRepository = scheduleRepository;
        this.blockRepository = blockRepository;
        this.doctorRepository = doctorRepository;
        this.scheduleMapper = scheduleMapper;
        this.blockMapper = blockMapper;
    }

    @Transactional
    public List<DoctorScheduleDto> setDoctorSchedule(Integer doctorId, ScheduleRequestDto scheduleRequest) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            throw new RuntimeException("Doctor not found");
        }
        
        Doctor doctor = doctorOpt.get();
        
        // Deactivate existing schedules
        List<DoctorSchedule> existingSchedules = scheduleRepository.findByDoctorAndIsActiveTrue(doctor);
        existingSchedules.forEach(schedule -> schedule.setIsActive(false));
        scheduleRepository.saveAll(existingSchedules);
        
        // Create new schedules
        List<DoctorSchedule> newSchedules = scheduleRequest.getShifts().stream()
            .map(shift -> {
                DoctorSchedule schedule = new DoctorSchedule();
                schedule.setDoctor(doctor);
                schedule.setDayOfWeek(DoctorSchedule.DayOfWeek.valueOf(shift.getDayOfWeek().toUpperCase()));
                schedule.setShiftType(shift.getShiftType());
                schedule.setStartTime(shift.getStartTime());
                schedule.setEndTime(shift.getEndTime());
                schedule.setIsActive(true);
                return schedule;
            })
            .collect(Collectors.toList());
        
        List<DoctorSchedule> savedSchedules = scheduleRepository.saveAll(newSchedules);
        
        return savedSchedules.stream()
            .map(scheduleMapper::toDto)
            .collect(Collectors.toList());
    }

    public List<DoctorScheduleDto> getDoctorSchedule(Integer doctorId) {
        List<DoctorSchedule> schedules = scheduleRepository.findByDoctorDoctorIdAndIsActiveTrue(doctorId);
        return schedules.stream()
            .map(scheduleMapper::toDto)
            .collect(Collectors.toList());
    }

    public DoctorBlockDto createBlock(Integer doctorId, DoctorBlockDto blockDto) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            throw new RuntimeException("Doctor not found");
        }
        
        Doctor doctor = doctorOpt.get();
        DoctorBlock block = blockMapper.toEntity(blockDto);
        block.setDoctor(doctor);
        block.setIsActive(true);
        
        DoctorBlock savedBlock = blockRepository.save(block);
        return blockMapper.toDto(savedBlock);
    }

    public List<DoctorBlockDto> getDoctorBlocks(Integer doctorId) {
        List<DoctorBlock> blocks = blockRepository.findByDoctorDoctorIdAndIsActiveTrue(doctorId);
        return blocks.stream()
            .map(blockMapper::toDto)
            .collect(Collectors.toList());
    }

    public boolean removeBlock(Integer blockId) {
        Optional<DoctorBlock> blockOpt = blockRepository.findById(blockId);
        if (blockOpt.isEmpty()) {
            return false;
        }
        
        DoctorBlock block = blockOpt.get();
        block.setIsActive(false);
        blockRepository.save(block);
        return true;
    }
}
