package com.hackathon.healsync.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hackathon.healsync.dto.DoctorDto;
import com.hackathon.healsync.dto.PatientDto;
import com.hackathon.healsync.entity.AppointmentStatus;
import com.hackathon.healsync.entity.Doctor;
import com.hackathon.healsync.entity.Patient;
import com.hackathon.healsync.mapper.DoctorMapper;
import com.hackathon.healsync.mapper.PatientMapper;
import com.hackathon.healsync.repository.AppointmentStatusRepository;
import com.hackathon.healsync.repository.DoctorRepository;
import com.hackathon.healsync.repository.PatientRepository;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final AppointmentStatusRepository appointmentStatusRepository;
    private final PatientRepository patientRepository;

    public DoctorService(DoctorRepository doctorRepository, 
                        AppointmentStatusRepository appointmentStatusRepository,
                        PatientRepository patientRepository) {
        this.doctorRepository = doctorRepository;
        this.appointmentStatusRepository = appointmentStatusRepository;
        this.patientRepository = patientRepository;
    }

    public DoctorDto addDoctor(DoctorDto doctorDto) {
        Doctor doctor = DoctorMapper.toEntity(doctorDto);
        if (doctor.getPassword() != null && !doctor.getPassword().isEmpty()) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            doctor.setPassword(encoder.encode(doctor.getPassword()));
        }
        Doctor savedDoctor = doctorRepository.save(doctor);
        return DoctorMapper.toDto(savedDoctor);
    }

    public DoctorDto getDoctorById(Integer doctorId) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        return doctorOpt.map(DoctorMapper::toDto).orElse(null);
    }

    public DoctorDto updateDoctor(Integer doctorId, DoctorDto doctorDto) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            return null;
        }
        Doctor doctor = doctorOpt.get();
        // Update fields
        doctor.setName(doctorDto.getName());
        doctor.setSpeaciality(doctorDto.getSpeaciality());
        doctor.setEmail(doctorDto.getEmail());
        doctor.setMobileNo(doctorDto.getMobileNo());
        doctor.setBio(doctorDto.getBio());
        doctor.setShift(doctorDto.getShift());
        if (doctorDto.getPassword() != null && !doctorDto.getPassword().isEmpty()) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            doctor.setPassword(encoder.encode(doctorDto.getPassword()));
        }
        Doctor updatedDoctor = doctorRepository.save(doctor);
        return DoctorMapper.toDto(updatedDoctor);
    }

    public boolean deleteDoctor(Integer doctorId) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            return false;
        }
        doctorRepository.deleteById(doctorId);
        return true;
    }

    public List<DoctorDto> getAllDoctorPublicProfiles() {
        return doctorRepository.findAll().stream()
                .map(DoctorMapper::toDto)
                .peek(dto -> dto.setPassword(null)) // Hide password in public profile
                .collect(Collectors.toList());
    }

    /**
     * Get all patients who have appointments with this doctor (all time)
     */
    public List<PatientDto> getDoctorPatients(Integer doctorId) {
        // Get all appointments for this doctor
        List<AppointmentStatus> appointments = appointmentStatusRepository.findByDoctorIdOrderByStartTimeDesc(doctorId);
        
        // Extract unique patient IDs
        Set<Integer> patientIds = appointments.stream()
                .map(AppointmentStatus::getPatientId)
                .collect(Collectors.toSet());
        
        // Get patient details for each unique patient ID
        return patientIds.stream()
                .map(patientRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(PatientMapper::toDto)
                .peek(dto -> dto.setPassword(null)) // Hide password for privacy
                .collect(Collectors.toList());
    }

    /**
     * Get patients who have active/upcoming appointments with this doctor
     */
    public List<PatientDto> getDoctorActivePatients(Integer doctorId) {
        LocalDateTime now = LocalDateTime.now();
        
        // Get appointments for this doctor that are not completed or cancelled
        List<AppointmentStatus> activeAppointments = appointmentStatusRepository.findByDoctorIdOrderByStartTimeDesc(doctorId)
                .stream()
                .filter(appointment -> {
                    String status = appointment.getStatus();
                    boolean isActiveStatus = !status.contains("cancelled") && 
                                           !status.equals("completed") && 
                                           !status.equals("no_show");
                    boolean isFutureOrRecent = appointment.getStartTime().isAfter(now.minusDays(30)); // Recent or future
                    return isActiveStatus && isFutureOrRecent;
                })
                .collect(Collectors.toList());
        
        // Extract unique patient IDs from active appointments
        Set<Integer> activePatientIds = activeAppointments.stream()
                .map(AppointmentStatus::getPatientId)
                .collect(Collectors.toSet());
        
        // Get patient details for each unique active patient ID
        return activePatientIds.stream()
                .map(patientRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(PatientMapper::toDto)
                .peek(dto -> dto.setPassword(null)) // Hide password for privacy
                .collect(Collectors.toList());
    }
}
