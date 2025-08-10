package com.hackathon.healsync.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hackathon.healsync.service.EmailOtpService;

@RestController
@RequestMapping("/v1/healsync/otp")
public class EmailOtpController {

    private final EmailOtpService emailOtpService;

    public EmailOtpController(EmailOtpService emailOtpService) {
        this.emailOtpService = emailOtpService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestParam("email") String email) {
        String msg = emailOtpService.startRegistration(email);
        return ResponseEntity.ok(msg);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam("email") String email,
                                    @RequestParam("otp") String otp) {
        boolean ok = emailOtpService.verify(email, otp);
        return ok ? ResponseEntity.ok("OTP Verified") : ResponseEntity.badRequest().body("Invalid or expired OTP");
    }

    @GetMapping("/resend")
    public ResponseEntity<?> resend(@RequestParam("email") String email) {
        String msg = emailOtpService.resend(email);
        return ResponseEntity.ok(msg);
    }
}
