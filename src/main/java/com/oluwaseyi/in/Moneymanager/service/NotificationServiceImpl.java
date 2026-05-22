package com.oluwaseyi.in.Moneymanager.service;

import com.oluwaseyi.in.Moneymanager.entity.Budget;
import com.oluwaseyi.in.Moneymanager.entity.Profile;
import com.oluwaseyi.in.Moneymanager.interfaces.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final JavaMailSender mailSender;

    public NotificationServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void notifyBudgetExceeded(Profile profile, Budget budget, Double total) {
        String subject = "Budget exceeded for category: " + budget.getCategory();
        String text = String.format("Hi %s,\n\nYou've spent %.2f in category '%s' this month. Your budget is %.2f.\n\n- MoneyManager",
                profile.getName(), total, budget.getCategory(), budget.getAmount());

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(profile.getEmail());
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            logger.info("Sent budget-exceeded email to {}", profile.getEmail());
        } catch (Exception ex) {
            logger.warn("Failed to send email, falling back to log. Reason: {}", ex.getMessage());
            logger.info("Notification: {} - {}", subject, text);
        }
    }
}
