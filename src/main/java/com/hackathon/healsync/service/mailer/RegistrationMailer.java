package com.hackathon.healsync.service.mailer;

import com.hackathon.healsync.dto.PatientDto;

public interface RegistrationMailer {
    void sendRegistrationEmail(PatientDto patient);
}
