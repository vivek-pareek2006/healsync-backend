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
public class SmtpPasswordChangeMailer implements PasswordChangeMailer {
    private static final Logger log = LoggerFactory.getLogger(SmtpPasswordChangeMailer.class);
    private final ApplicationContext ctx;

    public SmtpPasswordChangeMailer(ApplicationContext ctx) { this.ctx = ctx; }

    @Override
    public void sendPasswordChanged(String to) {
        try {
            Class<?> smmClass = Class.forName("org.springframework.mail.SimpleMailMessage");
            Object msg = smmClass.getDeclaredConstructor().newInstance();
            smmClass.getMethod("setTo", String[].class).invoke(msg, (Object) new String[]{to});
            smmClass.getMethod("setFrom", String.class).invoke(msg, "healsync.customercare@gmail.com");
            smmClass.getMethod("setSubject", String.class).invoke(msg, "Your HealSync password was changed");
            String body = "Hello,\n\nYour HealSync account password was changed just now. If this wasn’t you, please reset your password immediately and contact support.\n\nThanks,\nThe HealSync Team";
            smmClass.getMethod("setText", String.class).invoke(msg, body);

            Class<?> jmsClass = Class.forName("org.springframework.mail.javamail.JavaMailSender");
            Object mailSender = ctx.getBean(jmsClass);
            jmsClass.getMethod("send", smmClass).invoke(mailSender, msg);
            log.info("Password-changed email sent to {}", to);
        } catch (BeansException be) {
            log.error("JavaMailSender bean not available: {}", be.getMessage(), be);
        } catch (ReflectiveOperationException ex) {
            log.error("Failed to send password-changed email to {}: {}", to, ex.getMessage(), ex);
        }
    }
}
