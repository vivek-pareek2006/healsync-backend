package com.hackathon.healsync.service.mailer;

public interface PasswordChangeMailer {
    void sendPasswordChanged(String to);
}
