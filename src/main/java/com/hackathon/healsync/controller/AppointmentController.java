package com.hackathon.healsync.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> bookAppointment(@RequestParam("speaciality") String speaciality,
                                             @RequestParam("startDateTime") String startDateTime,
                                             @RequestParam("endDateTime") String endDateTime,
                                             @RequestParam("patientId") Integer patientId) {
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
}
