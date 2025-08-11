package com.hackathon.healsync.service.mailer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"!dev", "!test"})
@ConditionalOnClass(name = {
    "org.springframework.mail.javamail.JavaMailSender",
    "org.springframework.mail.SimpleMailMessage"
})
public class SmtpOtpMailer implements OtpMailer {
    private static final Logger log = LoggerFactory.getLogger(SmtpOtpMailer.class);
    private final ApplicationContext ctx;

    public SmtpOtpMailer(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void sendOtpEmail(String to, String otp) {
        try {
            Class<?> smmClass = Class.forName("org.springframework.mail.SimpleMailMessage");
            Object msg = smmClass.getDeclaredConstructor().newInstance();
            smmClass.getMethod("setTo", String[].class).invoke(msg, (Object) new String[]{to});
            smmClass.getMethod("setFrom", String.class).invoke(msg, "healsync.customercare@gmail.com");
            smmClass.getMethod("setSubject", String.class).invoke(msg, "HealSync Verification Code");
            String body = new StringBuilder()
                .append("Hello,\n\n")
                .append("Your HealSync verification code is: ").append(otp).append("\n\n")
                .append("This code will expire in 10 minutes.\n\n")
                .append("For your security:\n")
                .append("- Do not share this code with anyone.\n")
                .append("- If you didn’t request this, you can safely ignore this email.\n\n")
                .append("Thanks,\n")
                .append("The HealSync Team\n")
                .append("healsync.customercare@gmail.com")
                .toString();
            smmClass.getMethod("setText", String.class).invoke(msg, body);

            Class<?> jmsClass = Class.forName("org.springframework.mail.javamail.JavaMailSender");
            Object mailSender = ctx.getBean(jmsClass);
            jmsClass.getMethod("send", smmClass).invoke(mailSender, msg);
            log.info("OTP email sent to {}", to);
        } catch (BeansException be) {
            log.error("JavaMailSender bean not available: {}", be.getMessage(), be);
        } catch (ReflectiveOperationException ex) {
            log.error("Failed to send OTP email to {}: {}", to, ex.getMessage(), ex);
        }
    }
}
