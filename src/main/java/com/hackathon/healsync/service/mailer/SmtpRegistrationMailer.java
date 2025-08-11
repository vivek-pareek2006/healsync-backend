package com.hackathon.healsync.service.mailer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.hackathon.healsync.dto.PatientDto;

@Component
@Profile({"!dev", "!test"})
@ConditionalOnClass(name = {
    "org.springframework.mail.javamail.JavaMailSender",
    "org.springframework.mail.SimpleMailMessage"
})
public class SmtpRegistrationMailer implements RegistrationMailer {
    private static final Logger log = LoggerFactory.getLogger(SmtpRegistrationMailer.class);
    private final ApplicationContext ctx;

    public SmtpRegistrationMailer(ApplicationContext ctx) { this.ctx = ctx; }

    @Override
    public void sendRegistrationEmail(PatientDto p) {
        try {
            Class<?> smmClass = Class.forName("org.springframework.mail.SimpleMailMessage");
            Object msg = smmClass.getDeclaredConstructor().newInstance();
            smmClass.getMethod("setTo", String[].class).invoke(msg, (Object) new String[]{p.getEmail()});
            smmClass.getMethod("setFrom", String.class).invoke(msg, "healsync.customercare@gmail.com");
            smmClass.getMethod("setSubject", String.class).invoke(msg, "Welcome to HealSync");

            String body = new StringBuilder()
                .append("Hello ").append(nullToEmpty(p.getPatientName())).append(",\n\n")
                .append("Your registration with HealSync was successful. Here are your details:\n")
                .append("- Patient ID: ").append(p.getPatientId()).append("\n")
                .append("- Name: ").append(nullToEmpty(p.getPatientName())).append("\n")
                .append("- Age: ").append(p.getPatientAge() == null ? "" : p.getPatientAge()).append("\n")
                .append("- Gender: ").append(nullToEmpty(p.getGender())).append("\n")
                .append("- Phone: ").append(maskPhone(p.getMobileNo())).append("\n")
                .append("- Email: ").append(nullToEmpty(p.getEmail())).append("\n\n")
                .append("Thank you for joining HealSync. We're excited to support your health journey!\n\n")
                .append("Regards,\n")
                .append("The HealSync Team\n")
                .append("healsync.customercare@gmail.com")
                .toString();

            smmClass.getMethod("setText", String.class).invoke(msg, body);

            Class<?> jmsClass = Class.forName("org.springframework.mail.javamail.JavaMailSender");
            Object mailSender = ctx.getBean(jmsClass);
            jmsClass.getMethod("send", smmClass).invoke(mailSender, msg);
            log.info("Registration email sent to {}", p.getEmail());
        } catch (BeansException be) {
            log.error("JavaMailSender bean not available: {}", be.getMessage(), be);
        } catch (ReflectiveOperationException ex) {
            log.error("Failed to send registration email to {}: {}", p.getEmail(), ex.getMessage(), ex);
        }
    }

    private static String nullToEmpty(String s) { return s == null ? "" : s; }
    private static String maskPhone(String s) {
        if (s == null || s.length() < 4) return nullToEmpty(s);
        String last4 = s.substring(s.length() - 4);
        return "****-**" + last4;
    }
}
