package com.hackathon.healsync.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hackathon.healsync.service.AppointmentService;

@RestController
@RequestMapping("/v1/healsync/book")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/appointment")
    public ResponseEntity<String> bookAppointment(@RequestParam("speciality") String speciality,
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

        String result = appointmentService.bookAppointment(patientId, speciality, start, end);
        return ResponseEntity.ok(result);
    }
}
