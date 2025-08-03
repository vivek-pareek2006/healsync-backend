package com.hackathon.healsync.service;

import com.hackathon.healsync.dto.EmergencyServiceDto;
import com.hackathon.healsync.entity.Doctor;
import com.hackathon.healsync.entity.DoctorSchedule;
import com.hackathon.healsync.repository.DoctorRepository;
import com.hackathon.healsync.repository.DoctorScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmergencyService {

    private final DoctorRepository doctorRepository;
    private final DoctorScheduleRepository scheduleRepository;

    public EmergencyService(DoctorRepository doctorRepository, DoctorScheduleRepository scheduleRepository) {
        this.doctorRepository = doctorRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public List<EmergencyServiceDto> getEmergencyDoctors() {
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek currentDay = now.getDayOfWeek();
        LocalTime currentTime = now.toLocalTime();

        List<Doctor> allDoctors = doctorRepository.findAll();
        
        return allDoctors.stream()
            .map(doctor -> {
                EmergencyServiceDto emergencyDto = new EmergencyServiceDto();
                emergencyDto.setDoctorId(doctor.getDoctorId());
                emergencyDto.setDoctorName(doctor.getName());
                emergencyDto.setSpeaciality(doctor.getSpeaciality());
                emergencyDto.setMobileNo(doctor.getMobileNo());
                emergencyDto.setEmail(doctor.getEmail());
                emergencyDto.setCurrentShift(doctor.getShift());
                emergencyDto.setLocation("Hospital Main Campus"); // Default location
                
                // Check if doctor is currently on call
                List<DoctorSchedule> schedules = scheduleRepository.findByDoctorAndIsActiveTrue(doctor);
                boolean isOnCall = schedules.stream()
                    .anyMatch(schedule -> 
                        schedule.getDayOfWeek().name().equals(currentDay.name()) &&
                        (schedule.getShiftType() == DoctorSchedule.ShiftType.ON_CALL ||
                         (currentTime.isAfter(schedule.getStartTime()) && currentTime.isBefore(schedule.getEndTime())))
                    );
                
                emergencyDto.setOnCall(isOnCall);
                
                // Set availability until end of current shift or next day if on call
                if (isOnCall) {
                    emergencyDto.setAvailableUntil(now.plusHours(24)); // Available for 24 hours if on call
                    emergencyDto.setEmergencyType("General Emergency");
                } else {
                    emergencyDto.setAvailableUntil(now.plusHours(8)); // Available for regular hours
                    emergencyDto.setEmergencyType("Scheduled Emergency");
                }
                
                return emergencyDto;
            })
            .filter(EmergencyServiceDto::isOnCall) // Only return doctors who are currently on call
            .collect(Collectors.toList());
    }

    public List<EmergencyServiceDto> getEmergencyDoctorsBySpeciality(String speciality) {
        return getEmergencyDoctors().stream()
            .filter(doctor -> doctor.getSpeaciality().equalsIgnoreCase(speciality))
            .collect(Collectors.toList());
    }
}
