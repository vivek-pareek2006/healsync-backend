package com.hackathon.healsync.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hackathon.healsync.entity.EmailOtp;

public interface EmailOtpRepository extends JpaRepository<EmailOtp, Long> {
    Optional<EmailOtp> findTopByEmailAndPurposeOrderByIdDesc(String email, String purpose);
    long countByEmailAndLastSentAtAfter(String email, LocalDateTime after);
}
