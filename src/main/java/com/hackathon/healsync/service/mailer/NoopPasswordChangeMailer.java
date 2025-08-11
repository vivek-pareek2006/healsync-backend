package com.hackathon.healsync.service.mailer;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Profile({"dev", "test"})
@Slf4j
public class NoopPasswordChangeMailer implements PasswordChangeMailer {
    @Override
    public void sendPasswordChanged(String to) {
        log.info("[NOOP MAILER] Would send password-changed notification to {}", to);
    }
}
