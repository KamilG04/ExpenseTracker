package org.budget_manager.observer;



import java.math.BigDecimal;

public class EmailNotifier implements IBudgetObserver {
    private final String emailAddress;

    public EmailNotifier(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public void update(BigDecimal current, BigDecimal limit) {
        if (limit != null && current.compareTo(limit) > 0) {
            sendEmail("Budget Alert",
                    "Your current spending (" + current + ") has exceeded the budget limit (" + limit + ")");
        }
    }

    private void sendEmail(String subject, String message) {
        // Implementation for sending emails
        System.out.println("Email sent to " + emailAddress + ": " + subject + " - " + message);
    }
}
