package com.hackathon.healsync.service.mailer;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Profile({"dev", "test"})
@Slf4j
public class NoopOtpMailer implements OtpMailer {
    @Override
    public void sendOtpEmail(String to, String otp) {
    log.info("[NOOP MAILER] Sending professional OTP email to {} with code {} (expires in 10 minutes)", to, otp);
    }
}
