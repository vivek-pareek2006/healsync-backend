package com.hackathon.healsync.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hackathon.healsync.dto.AppointmentResponseDto;
import com.hackathon.healsync.entity.AppointmentStatus;
import com.hackathon.healsync.service.AppointmentService;

@RestController
@RequestMapping("/api/appointment")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AppointmentApiController {
    
    private final AppointmentService appointmentService;

    public AppointmentApiController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // Get appointments for a patient - matches your frontend URL
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getPatientAppointments(@PathVariable Integer patientId) {
        try {
            List<AppointmentStatus> appointments = appointmentService.listAppointmentsForPatient(patientId);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error fetching appointments: " + e.getMessage());
        }
    }

    // Get appointments for a doctor
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<?> getDoctorAppointments(@PathVariable Integer doctorId) {
        try {
            List<AppointmentStatus> appointments = appointmentService.listAppointmentsForDoctor(doctorId);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error fetching appointments: " + e.getMessage());
        }
    }

    // Get single appointment
    @GetMapping("/{appointmentId}")
    public ResponseEntity<?> getAppointment(@PathVariable Integer appointmentId) {
        try {
            var appointment = appointmentService.getAppointment(appointmentId);
            if (appointment == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found");
            }
            return ResponseEntity.ok(appointment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error fetching appointment: " + e.getMessage());
        }
    }

    // Book new appointment
    @PostMapping("/book")
    public ResponseEntity<?> bookAppointment(@RequestBody Map<String, Object> appointmentData) {
        try {
            String specialty = (String) appointmentData.get("specialty");
            Integer doctorId = appointmentData.get("doctorId") != null ? 
                Integer.parseInt(appointmentData.get("doctorId").toString()) : null;
            String startDateTime = (String) appointmentData.get("startDateTime");
            String endDateTime = (String) appointmentData.get("endDateTime");
            Integer patientId = Integer.parseInt(appointmentData.get("patientId").toString());

            // Validate that either specialty or doctorId is provided
            if ((specialty == null || specialty.isBlank()) && doctorId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Either 'specialty' or 'doctorId' must be provided");
            }

            LocalDateTime start = LocalDateTime.parse(startDateTime);
            LocalDateTime end = LocalDateTime.parse(endDateTime);

            AppointmentResponseDto result;
            
            if (doctorId != null) {
                result = appointmentService.bookAppointmentWithDoctor(patientId, doctorId, start, end);
            } else {
                result = appointmentService.bookAppointment(patientId, specialty, start, end);
            }
            
            if (result == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No doctor available for the given time slot");
            }
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error booking appointment: " + e.getMessage());
        }
    }

    // Cancel appointment
    @PostMapping("/{appointmentId}/cancel")
    public ResponseEntity<?> cancelAppointment(@PathVariable Integer appointmentId, 
                                               @RequestBody Map<String, Object> cancelData) {
        try {
            Integer doctorId = cancelData.get("doctorId") != null ? 
                Integer.parseInt(cancelData.get("doctorId").toString()) : null;
            Integer patientId = cancelData.get("patientId") != null ? 
                Integer.parseInt(cancelData.get("patientId").toString()) : null;
            String role = (String) cancelData.get("role");

            boolean cancelled = false;
            
            if ("DOCTOR".equals(role) && doctorId != null) {
                cancelled = appointmentService.cancelAppointmentByDoctor(appointmentId, doctorId);
            } else if ("PATIENT".equals(role) && patientId != null) {
                cancelled = appointmentService.patientCancel(appointmentId, patientId);
            }
            
            if (cancelled) {
                return ResponseEntity.ok(Map.of("message", "Appointment cancelled successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Appointment not found or unauthorized");
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error cancelling appointment: " + e.getMessage());
        }
    }

    // Update appointment (reschedule)
    @PostMapping("/{appointmentId}/reschedule")
    public ResponseEntity<?> rescheduleAppointment(@PathVariable Integer appointmentId,
                                                   @RequestBody Map<String, Object> rescheduleData) {
        try {
            Integer requesterId = Integer.parseInt(rescheduleData.get("requesterId").toString());
            String requesterRole = (String) rescheduleData.get("requesterRole");
            String newStartDateTime = (String) rescheduleData.get("newStartDateTime");
            String newEndDateTime = (String) rescheduleData.get("newEndDateTime");

            LocalDateTime newStart = LocalDateTime.parse(newStartDateTime);
            LocalDateTime newEnd = LocalDateTime.parse(newEndDateTime);
            
            AppointmentResponseDto result = appointmentService.rescheduleAppointment(
                appointmentId, requesterId, requesterRole, newStart, newEnd);
                
            if (result == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Unable to reschedule appointment");
            }
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error rescheduling appointment: " + e.getMessage());
        }
    }

    // Get available time slots
    @GetMapping("/available-slots")
    public ResponseEntity<?> getAvailableSlots(@RequestParam String specialty,
                                               @RequestParam String date,
                                               @RequestParam(defaultValue = "60") int durationMinutes) {
        try {
            List<Map<String, Object>> slots = appointmentService.getAvailableSlots(specialty, date, durationMinutes);
            return ResponseEntity.ok(slots);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error fetching available slots: " + e.getMessage());
        }
    }

    // Get upcoming appointments
    @GetMapping("/upcoming")
    public ResponseEntity<?> getUpcomingAppointments(@RequestParam(required = false) Integer patientId,
                                                     @RequestParam(required = false) Integer doctorId,
                                                     @RequestParam(defaultValue = "7") int daysAhead) {
        try {
            List<AppointmentStatus> appointments = appointmentService.getUpcomingAppointments(patientId, doctorId, daysAhead);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error fetching upcoming appointments: " + e.getMessage());
        }
    }

    // Check doctor availability
    @GetMapping("/check-availability")
    public ResponseEntity<?> checkDoctorAvailability(@RequestParam Integer doctorId,
                                                     @RequestParam String startDateTime,
                                                     @RequestParam String endDateTime) {
        try {
            Map<String, Object> availability = appointmentService.checkDoctorAvailability(doctorId, startDateTime, endDateTime);
            return ResponseEntity.ok(availability);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error checking availability: " + e.getMessage());
        }
    }

    // Search appointments
    @GetMapping("/search")
    public ResponseEntity<?> searchAppointments(@RequestParam String query,
                                               @RequestParam(required = false) Integer userId,
                                               @RequestParam(required = false) String userRole,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        try {
            List<AppointmentStatus> appointments = appointmentService.searchAppointments(query, userId, userRole, page, size);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error searching appointments: " + e.getMessage());
        }
    }

    // Get appointment statistics
    @GetMapping("/statistics")
    public ResponseEntity<?> getAppointmentStatistics(@RequestParam(required = false) Integer doctorId,
                                                      @RequestParam(required = false) String startDate,
                                                      @RequestParam(required = false) String endDate) {
        try {
            Map<String, Object> stats = appointmentService.getAppointmentStatistics(doctorId, startDate, endDate);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error fetching statistics: " + e.getMessage());
        }
    }
}
