package com.hackathon.healsync.service.mailer;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.hackathon.healsync.dto.PatientDto;

import lombok.extern.slf4j.Slf4j;

@Component
@Profile({"dev", "test"})
@Slf4j
public class NoopRegistrationMailer implements RegistrationMailer {
    @Override
    public void sendRegistrationEmail(PatientDto p) {
        log.info("[NOOP MAILER] Registration email to {} (id: {}, name: {}, age: {}, gender: {}, phone: {}, email: {})",
            p.getEmail(), p.getPatientId(), p.getPatientName(), p.getPatientAge(), p.getGender(), p.getMobileNo(), p.getEmail());
    }
}
