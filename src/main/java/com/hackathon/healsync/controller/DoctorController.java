package com.hackathon.healsync.controller;

import com.hackathon.healsync.dto.DoctorDto;
import com.hackathon.healsync.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/healsync/doctor")
public class DoctorController {
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping("/add")
    public ResponseEntity<DoctorDto> addDoctor(@RequestBody DoctorDto doctorDto) {
        DoctorDto savedDoctor = doctorService.addDoctor(doctorDto);
        return ResponseEntity.ok(savedDoctor);
    }
}
