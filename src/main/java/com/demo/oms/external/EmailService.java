package com.demo.oms.external;

import com.demo.oms.model.Order;
import com.demo.oms.model.User;

public class EmailService {

    public boolean sendWelcomeEmail(User user) {
        if (user == null) {
            return false;
        }

        System.out.println("[EmailService] Welcome email sent to " + user.getEmail());
        return true;
    }

    public boolean sendOrderConfirmation(User user, Order order) {
        if (user == null || order == null) {
            return false;
        }

        System.out.println("[EmailService] Order confirmation sent to " + user.getEmail() + " for order " + order.getOrderId());
        return true;
    }

    public boolean sendPasswordResetEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        System.out.println("[EmailService] Password reset email sent to " + email);
        return true;
    }

    public void logEmailSent(String message) {
        System.out.println("[EmailService] Log: " + message);
    }
}
