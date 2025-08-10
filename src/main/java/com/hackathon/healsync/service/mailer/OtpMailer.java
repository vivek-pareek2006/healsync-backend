package com.hackathon.healsync.service.mailer;

public interface OtpMailer {
    void sendOtpEmail(String to, String otp);
}
