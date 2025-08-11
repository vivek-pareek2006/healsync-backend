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
        String otp = generateOtp();
        String hash = hashOtp(otp);
        EmailOtp record = new EmailOtp();
        record.setEmail(normalized);
        record.setOtpHash(hash);
        record.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        record.setAttempts(0);
        record.setResendCount(0);
        record.setLastSentAt(LocalDateTime.now());
        record.setPurpose("REGISTER");
        record.setStatus("PENDING");
        repo.save(record);

    sendOtpEmail(normalized, otp);
        return "OTP sent";
    }

    public boolean verify(String email, String otp) {
        String normalized = normalizeEmail(email);
        var recordOpt = repo.findTopByEmailAndPurposeOrderByIdDesc(normalized, "REGISTER");
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

    public String resend(String email) {
        String normalized = normalizeEmail(email);
        // Always generate and send a fresh OTP (no cooldown, no resend cap)
        return startRegistration(normalized);
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
