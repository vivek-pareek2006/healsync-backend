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
import com.hackathon.healsync.dto.DoctorScheduleDto;
import com.hackathon.healsync.dto.DoctorBlockDto;
import com.hackathon.healsync.dto.PatientDto;
import com.hackathon.healsync.dto.ScheduleRequestDto;
import com.hackathon.healsync.service.DoctorService;
import com.hackathon.healsync.service.DoctorScheduleService;

@RestController
@RequestMapping("/v1/healsync/doctor")
public class DoctorController {
    private final DoctorService doctorService;
    private final DoctorScheduleService scheduleService;

    public DoctorController(DoctorService doctorService, DoctorScheduleService scheduleService) {
        this.doctorService = doctorService;
        this.scheduleService = scheduleService;
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

    // ==========================================
    // SCHEDULE & AVAILABILITY MANAGEMENT APIs
    // ==========================================

    @PostMapping("/{doctorId}/schedule")
    public ResponseEntity<?> setDoctorSchedule(@PathVariable Integer doctorId, @RequestBody ScheduleRequestDto scheduleRequest) {
        try {
            List<DoctorScheduleDto> schedules = scheduleService.setDoctorSchedule(doctorId, scheduleRequest);
            return ResponseEntity.ok(schedules);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{doctorId}/schedule")
    public ResponseEntity<List<DoctorScheduleDto>> getDoctorSchedule(@PathVariable Integer doctorId) {
        List<DoctorScheduleDto> schedules = scheduleService.getDoctorSchedule(doctorId);
        return ResponseEntity.ok(schedules);
    }

    @PostMapping("/{doctorId}/blocks")
    public ResponseEntity<?> createBlock(@PathVariable Integer doctorId, @RequestBody DoctorBlockDto blockDto) {
        try {
            DoctorBlockDto createdBlock = scheduleService.createBlock(doctorId, blockDto);
            return ResponseEntity.ok(createdBlock);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{doctorId}/blocks")
    public ResponseEntity<List<DoctorBlockDto>> getDoctorBlocks(@PathVariable Integer doctorId) {
        List<DoctorBlockDto> blocks = scheduleService.getDoctorBlocks(doctorId);
        return ResponseEntity.ok(blocks);
    }

    @DeleteMapping("/{doctorId}/blocks/{blockId}")
    public ResponseEntity<?> removeBlock(@PathVariable Integer doctorId, @PathVariable Integer blockId) {
        boolean removed = scheduleService.removeBlock(blockId);
        if (!removed) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Block not found");
        }
        return ResponseEntity.ok("Block removed successfully");
    }

    // ==========================================
    // PATIENT MANAGEMENT APIs
    // ==========================================

    @GetMapping("/{doctorId}/patients")
    public ResponseEntity<List<PatientDto>> getDoctorPatients(@PathVariable Integer doctorId) {
        List<PatientDto> patients = doctorService.getDoctorPatients(doctorId);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{doctorId}/patients/active")
    public ResponseEntity<List<PatientDto>> getDoctorActivePatients(@PathVariable Integer doctorId) {
        List<PatientDto> patients = doctorService.getDoctorActivePatients(doctorId);
        return ResponseEntity.ok(patients);
    }
}
