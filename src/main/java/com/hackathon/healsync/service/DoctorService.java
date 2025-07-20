package com.hackathon.healsync.service;

import com.hackathon.healsync.dto.DoctorDto;
import com.hackathon.healsync.entity.Doctor;
import com.hackathon.healsync.mapper.DoctorMapper;
import com.hackathon.healsync.repository.DoctorRepository;
import org.springframework.stereotype.Service;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public DoctorDto addDoctor(DoctorDto doctorDto) {
        Doctor doctor = DoctorMapper.toEntity(doctorDto);
        Doctor savedDoctor = doctorRepository.save(doctor);
        return DoctorMapper.toDto(savedDoctor);
    }
}
