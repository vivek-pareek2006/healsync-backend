package com.hackathon.healsync.controller;

import com.hackathon.healsync.dto.PatientDto;
import com.hackathon.healsync.dto.DoctorDto;
import com.hackathon.healsync.dto.EmergencyServiceDto;
import com.hackathon.healsync.service.PatientService;
import com.hackathon.healsync.service.DoctorService;
import com.hackathon.healsync.service.EmergencyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserDiscoveryController {

    private final PatientService patientService;
    private final DoctorService doctorService;
    private final EmergencyService emergencyService;

    public UserDiscoveryController(PatientService patientService, 
                                 DoctorService doctorService,
                                 EmergencyService emergencyService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.emergencyService = emergencyService;
    }

    // ==========================================
    // USER & DOCTOR DISCOVERY APIs
    // ==========================================

    @PostMapping("/patients")
    public ResponseEntity<PatientDto> registerPatient(@RequestBody PatientDto patientDto) {
        PatientDto savedPatient = patientService.addPatient(patientDto);
        return ResponseEntity.ok(savedPatient);
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorDto>> getAllDoctors() {
        List<DoctorDto> doctors = doctorService.getAllDoctorPublicProfiles();
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/doctors/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable Integer id) {
        DoctorDto doctor = doctorService.getDoctorById(id);
        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }
        return ResponseEntity.ok(doctor);
    }

    @GetMapping("/emergency-services")
    public ResponseEntity<List<EmergencyServiceDto>> getEmergencyServices() {
        List<EmergencyServiceDto> emergencyDoctors = emergencyService.getEmergencyDoctors();
        return ResponseEntity.ok(emergencyDoctors);
    }

    @GetMapping("/emergency-services/{speciality}")
    public ResponseEntity<List<EmergencyServiceDto>> getEmergencyServicesBySpeciality(@PathVariable String speciality) {
        List<EmergencyServiceDto> emergencyDoctors = emergencyService.getEmergencyDoctorsBySpeciality(speciality);
        return ResponseEntity.ok(emergencyDoctors);
    }

    // Additional utility endpoints for better user discovery
    @GetMapping("/doctors/search")
    public ResponseEntity<List<DoctorDto>> searchDoctorsBySpeciality(@RequestParam String speciality) {
        List<DoctorDto> doctors = doctorService.getAllDoctorPublicProfiles();
        List<DoctorDto> filteredDoctors = doctors.stream()
            .filter(doctor -> doctor.getSpeaciality().toLowerCase().contains(speciality.toLowerCase()))
            .toList();
        return ResponseEntity.ok(filteredDoctors);
    }

    @GetMapping("/doctors/available")
    public ResponseEntity<List<DoctorDto>> getAvailableDoctors() {
        // Returns all doctors that have active schedules
        List<DoctorDto> doctors = doctorService.getAllDoctorPublicProfiles();
        return ResponseEntity.ok(doctors);
    }
}
