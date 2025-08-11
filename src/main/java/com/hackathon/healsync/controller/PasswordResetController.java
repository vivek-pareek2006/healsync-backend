package com.hackathon.healsync.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hackathon.healsync.dto.PatientDto;

@RestController
@RequestMapping("/v1/healsync/password")
public class PasswordResetController {
    private final com.hackathon.healsync.service.PasswordResetService service;

    public PasswordResetController(com.hackathon.healsync.service.PasswordResetService service) {
        this.service = service;
    }

    @PostMapping("/forgot")
    public ResponseEntity<?> forgot(@RequestBody PatientDto dto) {
        service.start(dto.getEmail());
        return ResponseEntity.ok("If an account exists, we have sent instructions.");
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resend(@RequestBody PatientDto dto) {
        service.resend(dto.getEmail());
        return ResponseEntity.ok("If an account exists, we have sent instructions.");
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody VerifyDto dto) {
        boolean ok = service.verify(dto.getEmail(), dto.getOtp());
        return ok ? ResponseEntity.ok("OTP verified") : ResponseEntity.badRequest().body("Invalid or expired OTP");
    }

    @PostMapping("/reset")
    public ResponseEntity<?> reset(@RequestBody ResetDto dto) {
        boolean ok = service.reset(dto.getEmail(), dto.getOtp(), dto.getNewPassword());
        return ok ? ResponseEntity.ok("Password updated") : ResponseEntity.badRequest().body("Unable to reset password");
    }

    public static class VerifyDto {
        private String email;
        private String otp;
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getOtp() { return otp; }
        public void setOtp(String otp) { this.otp = otp; }
    }

    public static class ResetDto {
        private String email;
        private String otp;
        private String newPassword;
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getOtp() { return otp; }
        public void setOtp(String otp) { this.otp = otp; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }
}
