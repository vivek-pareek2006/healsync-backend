package com.hackathon.healsync.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hackathon.healsync.dto.AppointmentResponseDto;
import com.hackathon.healsync.service.AppointmentService;

@RestController
@RequestMapping("/v1/healsync/book")
public class AppointmentController {
    
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelAppointment(@RequestParam("appointmentId") Integer appointmentId,
                                                   @RequestParam("doctorId") Integer doctorId) {
        boolean cancelled = appointmentService.cancelAppointmentByDoctor(appointmentId, doctorId);
        if (cancelled) {
            return ResponseEntity.ok("Appointment cancelled successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found or doctor not authorized.");
        }
    }

    @PostMapping("/appointment")
    public ResponseEntity<?> bookAppointment(@RequestParam(value = "speaciality", required = false) String speaciality,
                                             @RequestParam(value = "specialty", required = false) String specialty,
                                             @RequestParam("startDateTime") String startDateTime,
                                             @RequestParam("endDateTime") String endDateTime,
                                             @RequestParam("patientId") Integer patientId) {
        if (speaciality == null || speaciality.isBlank()) {
            speaciality = specialty; // accept alias
        }
        LocalDateTime start;
        LocalDateTime end;
        try {
            start = LocalDateTime.parse(startDateTime);
            end = LocalDateTime.parse(endDateTime);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid dateTime format. Use ISO format.");
        }

        AppointmentResponseDto result = appointmentService.bookAppointment(patientId, speaciality, start, end);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No doctor available for the given speaciality and time range.");
        }
        return ResponseEntity.ok(result);
    }

    // List appointments for a patient
    @GetMapping("/patient/appointments")
    public ResponseEntity<?> listPatientAppointments(@RequestParam("patientId") Integer patientId) {
        return ResponseEntity.ok(appointmentService.listAppointmentsForPatient(patientId));
    }

    // List appointments for a doctor
    @GetMapping("/doctor/appointments")
    public ResponseEntity<?> listDoctorAppointments(@RequestParam("doctorId") Integer doctorId) {
        return ResponseEntity.ok(appointmentService.listAppointmentsForDoctor(doctorId));
    }

    // Get single appointment
    @GetMapping("/appointment")
    public ResponseEntity<?> getAppointment(@RequestParam("appointmentId") Integer appointmentId) {
        var appt = appointmentService.getAppointment(appointmentId);
        if (appt == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found");
        return ResponseEntity.ok(appt);
    }

    // Reschedule appointment by doctor or patient
    @PostMapping("/reschedule")
    public ResponseEntity<?> reschedule(@RequestParam("appointmentId") Integer appointmentId,
                                        @RequestParam("requesterId") Integer requesterId,
                                        @RequestParam("requesterRole") String requesterRole, // DOCTOR or PATIENT
                                        @RequestParam("newStartDateTime") String newStartDateTime,
                                        @RequestParam("newEndDateTime") String newEndDateTime) {
        try {
            LocalDateTime newStart = LocalDateTime.parse(newStartDateTime);
            LocalDateTime newEnd = LocalDateTime.parse(newEndDateTime);
            AppointmentResponseDto res = appointmentService.rescheduleAppointment(appointmentId, requesterId, requesterRole, newStart, newEnd);
            if (res == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to reschedule (not found/unauthorized/conflict)");
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid dateTime format. Use ISO.");
        }
    }

    // Patient initiated cancellation
    @PostMapping("/cancel/patient")
    public ResponseEntity<?> cancelByPatient(@RequestParam("appointmentId") Integer appointmentId,
                                             @RequestParam("patientId") Integer patientId) {
        boolean ok = appointmentService.patientCancel(appointmentId, patientId);
        if (!ok) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found or not owned by patient");
        return ResponseEntity.ok("Appointment cancelled by patient.");
    }

    // Add or update doctor notes
    @PostMapping("/notes")
    public ResponseEntity<?> addDoctorNotes(@RequestParam("appointmentId") Integer appointmentId,
                                            @RequestParam("doctorId") Integer doctorId,
                                            @RequestParam("notes") String notes) {
        boolean ok = appointmentService.addOrUpdateDoctorNotes(appointmentId, doctorId, notes);
        if (!ok) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found or doctor not authorized");
        return ResponseEntity.ok("Notes updated.");
    }
}
