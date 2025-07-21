package com.hackathon.healsync.service;

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
}
