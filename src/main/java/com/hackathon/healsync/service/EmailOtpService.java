package com.hackathon.healsync.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hackathon.healsync.entity.EmailOtp;
import com.hackathon.healsync.repository.EmailOtpRepository;
import com.hackathon.healsync.service.mailer.OtpMailer;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailOtpService {
    private final SecureRandom random = new SecureRandom();
    private final EmailOtpRepository repo;
    private final OtpMailer mailer;

    public EmailOtpService(EmailOtpRepository repo, OtpMailer mailer) {
        this.repo = repo;
        this.mailer = mailer;
    }

    public String startRegistration(String email) {
        String normalized = normalizeEmail(email);
    // Cooldown removed to allow multiple OTP sends back-to-back
        return createAndSendOtp(normalized, "REGISTER");
    }

    public boolean verify(String email, String otp) {
        String normalized = normalizeEmail(email);
        return verifyInternal(normalized, otp, "REGISTER");
    }

    public String resend(String email) {
        String normalized = normalizeEmail(email);
        // Always generate and send a fresh OTP (no cooldown, no resend cap)
        return createAndSendOtp(normalized, "REGISTER");
    }

    // Password Reset variants
    public String startPasswordReset(String email) {
        String normalized = normalizeEmail(email);
        return createAndSendOtp(normalized, "PASSWORD_RESET");
    }

    public String resendPasswordReset(String email) {
        String normalized = normalizeEmail(email);
        return createAndSendOtp(normalized, "PASSWORD_RESET");
    }

    public boolean verifyPasswordReset(String email, String otp) {
        String normalized = normalizeEmail(email);
        return verifyInternal(normalized, otp, "PASSWORD_RESET");
    }

    // Consume password reset OTP (works if OTP is PENDING or already VERIFIED)
    public boolean consumePasswordReset(String email, String otp) {
        String normalized = normalizeEmail(email);
        return consumeInternal(normalized, otp, "PASSWORD_RESET");
    }

    private String createAndSendOtp(String normalizedEmail, String purpose) {
        String otp = generateOtp();
        String hash = hashOtp(otp);
        EmailOtp record = new EmailOtp();
        record.setEmail(normalizedEmail);
        record.setOtpHash(hash);
        record.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        record.setAttempts(0);
        record.setResendCount(0);
        record.setLastSentAt(LocalDateTime.now());
        record.setPurpose(purpose);
        record.setStatus("PENDING");
        repo.save(record);
        sendOtpEmail(normalizedEmail, otp);
        return "OTP sent";
    }

    private boolean verifyInternal(String normalizedEmail, String otp, String purpose) {
        var recordOpt = repo.findTopByEmailAndPurposeOrderByIdDesc(normalizedEmail, purpose);
        if (recordOpt.isEmpty()) return false;
        var record = recordOpt.get();
        if (!"PENDING".equals(record.getStatus())) return false;
        if (LocalDateTime.now().isAfter(record.getExpiresAt())) { record.setStatus("EXPIRED"); repo.save(record); return false; }
        if (record.getAttempts() >= 5) { record.setStatus("LOCKED"); repo.save(record); return false; }
        record.setAttempts(record.getAttempts() + 1);
        boolean ok = verifyHash(otp, record.getOtpHash());
        if (ok) { record.setStatus("VERIFIED"); repo.save(record); return true; }
        repo.save(record);
        return false;
    }

    private boolean consumeInternal(String normalizedEmail, String otp, String purpose) {
        var recordOpt = repo.findTopByEmailAndPurposeOrderByIdDesc(normalizedEmail, purpose);
        if (recordOpt.isEmpty()) return false;
        var record = recordOpt.get();
        if (LocalDateTime.now().isAfter(record.getExpiresAt())) { record.setStatus("EXPIRED"); repo.save(record); return false; }
        // Allow consumption if PENDING or already VERIFIED
        String st = record.getStatus();
        if (!"PENDING".equals(st) && !"VERIFIED".equals(st)) return false;
        boolean ok = verifyHash(otp, record.getOtpHash());
        if (!ok) return false;
        record.setStatus("CONSUMED");
        repo.save(record);
        return true;
    }

    private void sendOtpEmail(String to, String otp) { mailer.sendOtpEmail(to, otp); }

    private String generateOtp() { return String.format(Locale.ROOT, "%06d", random.nextInt(1_000_000)); }

    // Placeholder hash/verify; replace with BCrypt/Argon2 if desired
    private String hashOtp(String otp) { return Integer.toHexString(otp.hashCode()); }
    private boolean verifyHash(String otp, String hash) { return Integer.toHexString(otp.hashCode()).equals(hash); }

    private String normalizeEmail(String email) {
        return StringUtils.hasText(email) ? email.trim().toLowerCase(Locale.ROOT) : "";
    }
}
