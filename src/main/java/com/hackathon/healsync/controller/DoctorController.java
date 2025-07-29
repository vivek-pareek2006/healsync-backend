package com.hackathon.healsync.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hackathon.healsync.dto.DoctorDto;
import com.hackathon.healsync.service.DoctorService;

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

    @GetMapping("/{doctorId}")
    public ResponseEntity<?> getDoctor(@PathVariable Integer doctorId) {
        DoctorDto doctor = doctorService.getDoctorById(doctorId);
        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }
        return ResponseEntity.ok(doctor);
    }

    @PutMapping("/{doctorId}")
    public ResponseEntity<?> updateDoctor(@PathVariable Integer doctorId, @RequestBody DoctorDto doctorDto) {
        DoctorDto updated = doctorService.updateDoctor(doctorId, doctorDto);
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{doctorId}")
    public ResponseEntity<?> deleteDoctor(@PathVariable Integer doctorId) {
        boolean deleted = doctorService.deleteDoctor(doctorId);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }
        return ResponseEntity.ok("Doctor deleted successfully");
    }

    @GetMapping("/public-profiles")
    public ResponseEntity<List<DoctorDto>> getAllDoctorPublicProfiles() {
        List<DoctorDto> doctors = doctorService.getAllDoctorPublicProfiles();
        return ResponseEntity.ok(doctors);
    }
}
