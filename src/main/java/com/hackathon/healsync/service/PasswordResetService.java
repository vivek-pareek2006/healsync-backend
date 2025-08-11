package com.hackathon.healsync.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hackathon.healsync.entity.Patient;
import com.hackathon.healsync.repository.PatientRepository;
import com.hackathon.healsync.service.mailer.PasswordChangeMailer;

@Service
public class PasswordResetService {
    private final EmailOtpService otpService;
    private final PatientRepository patientRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final PasswordChangeMailer passwordChangeMailer;

    public PasswordResetService(EmailOtpService otpService, PatientRepository patientRepository, PasswordChangeMailer passwordChangeMailer) {
        this.otpService = otpService;
        this.patientRepository = patientRepository;
        this.passwordChangeMailer = passwordChangeMailer;
    }

    public void start(String email) {
        // Always respond OK for privacy; still send OTP if account exists
        if (email == null || email.isBlank()) return;
        String normalized = email.toLowerCase();
        if (patientRepository.existsByEmail(normalized)) {
            otpService.startPasswordReset(normalized);
        }
    }

    public void resend(String email) {
        if (email == null || email.isBlank()) return;
        String normalized = email.toLowerCase();
        if (patientRepository.existsByEmail(normalized)) {
            otpService.resendPasswordReset(normalized);
        }
    }

    public boolean verify(String email, String otp) {
        return otpService.verifyPasswordReset(email, otp);
    }

    public boolean reset(String email, String otp, String newPassword) {
    boolean ok = otpService.consumePasswordReset(email, otp);
        if (!ok) return false;
    Patient p = patientRepository.findFirstByEmailOrderByPatientIdDesc(email.toLowerCase());
        if (p == null) return false;
        p.setPassword(encoder.encode(newPassword));
    patientRepository.save(p);
    try { passwordChangeMailer.sendPasswordChanged(p.getEmail()); } catch (Exception ignored) {}
        return true;
    }
}
