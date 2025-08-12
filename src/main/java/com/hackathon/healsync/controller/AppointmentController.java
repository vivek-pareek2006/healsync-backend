package com.hackathon.healsync.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hackathon.healsync.dto.AppointmentResponseDto;
import com.hackathon.healsync.entity.AppointmentStatus;
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

    // Filter appointments with multiple criteria
    @GetMapping("/filter")
    public ResponseEntity<List<AppointmentStatus>> filterAppointments(
        @RequestParam(required = false) Integer patientId,
        @RequestParam(required = false) Integer doctorId,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        
        List<AppointmentStatus> appointments = appointmentService.filterAppointments(
            patientId, doctorId, status, startDate, endDate, page, size);
        return ResponseEntity.ok(appointments);
    }

    // Check doctor availability for specific time slot
    @GetMapping("/check-availability")
    public ResponseEntity<Map<String, Object>> checkDoctorAvailability(
        @RequestParam Integer doctorId,
        @RequestParam String startDateTime,
        @RequestParam String endDateTime) {
        
        Map<String, Object> availability = appointmentService.checkDoctorAvailability(
            doctorId, startDateTime, endDateTime);
        return ResponseEntity.ok(availability);
    }

    // Get available time slots for a specialty on a specific date
    @GetMapping("/available-slots")
    public ResponseEntity<List<Map<String, Object>>> getAvailableSlots(
        @RequestParam String specialty,
        @RequestParam String date,
        @RequestParam(defaultValue = "60") int durationMinutes) {
        
        List<Map<String, Object>> slots = appointmentService.getAvailableSlots(
            specialty, date, durationMinutes);
        return ResponseEntity.ok(slots);
    }

    // Get upcoming appointments for next N days
    @GetMapping("/upcoming")
    public ResponseEntity<List<AppointmentStatus>> getUpcomingAppointments(
        @RequestParam(required = false) Integer patientId,
        @RequestParam(required = false) Integer doctorId,
        @RequestParam(defaultValue = "7") int daysAhead) {
        
        List<AppointmentStatus> appointments = appointmentService.getUpcomingAppointments(
            patientId, doctorId, daysAhead);
        return ResponseEntity.ok(appointments);
    }

    // Bulk cancel multiple appointments
    @PostMapping("/bulk-cancel")
    public ResponseEntity<Map<String, Object>> bulkCancelAppointments(
        @RequestBody List<Integer> appointmentIds,
        @RequestParam Integer requesterId,
        @RequestParam String requesterRole,
        @RequestParam(required = false) String reason) {
        
        Map<String, Object> result = appointmentService.bulkCancelAppointments(
            appointmentIds, requesterId, requesterRole, reason);
        return ResponseEntity.ok(result);
    }

    // Get appointment statistics and analytics
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getAppointmentStatistics(
        @RequestParam(required = false) Integer doctorId,
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate) {
        
        Map<String, Object> stats = appointmentService.getAppointmentStatistics(
            doctorId, startDate, endDate);
        return ResponseEntity.ok(stats);
    }

    // Confirm appointment (doctor confirms the appointment)
    @PostMapping("/confirm")
    public ResponseEntity<String> confirmAppointment(
        @RequestParam Integer appointmentId,
        @RequestParam Integer doctorId) {
        
        boolean success = appointmentService.confirmAppointment(appointmentId, doctorId);
        if (success) {
            return ResponseEntity.ok("Appointment confirmed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not allowed or appointment not found");
        }
    }

    // Mark appointment as completed with notes and prescription
    @PostMapping("/complete")
    public ResponseEntity<String> completeAppointment(
        @RequestParam Integer appointmentId,
        @RequestParam Integer doctorId,
        @RequestParam(required = false) String notes,
        @RequestParam(required = false) String prescription) {
        
        boolean success = appointmentService.completeAppointment(appointmentId, doctorId, notes, prescription);
        if (success) {
            return ResponseEntity.ok("Appointment completed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not allowed or appointment not found");
        }
    }

    // Search appointments by patient/doctor name or appointment details
    @GetMapping("/search")
    public ResponseEntity<List<AppointmentStatus>> searchAppointments(
        @RequestParam String query,
        @RequestParam(required = false) Integer userId,
        @RequestParam(required = false) String userRole,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        
        List<AppointmentStatus> appointments = appointmentService.searchAppointments(
            query, userId, userRole, page, size);
        return ResponseEntity.ok(appointments);
    }
}
