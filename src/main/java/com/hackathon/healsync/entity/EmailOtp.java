package com.hackathon.healsync.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "email_otp")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailOtp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email; // normalized lower-case

    @Column(nullable = false)
    private String otpHash; // hash of OTP, not plaintext

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private int attempts = 0; // verification attempts

    @Column(nullable = false)
    private int resendCount = 0;

    private LocalDateTime lastSentAt;

    @Column(nullable = false)
    private String purpose = "REGISTER"; // future: LOGIN_2FA, RESET_PASSWORD

    @Column(nullable = false)
    private String status = "PENDING"; // PENDING, VERIFIED, EXPIRED, LOCKED
}
