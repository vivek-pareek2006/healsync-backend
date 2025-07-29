package com.hackathon.healsync.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hackathon.healsync.dto.DoctorDto;
import com.hackathon.healsync.entity.Doctor;
import com.hackathon.healsync.mapper.DoctorMapper;
import com.hackathon.healsync.repository.DoctorRepository;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
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
}
