package com.demo.auth;

public class EmailService {

    public boolean sendWelcomeEmail(String username) {
        System.out.println("Sending email to " + username);
        return true;
    }
}
