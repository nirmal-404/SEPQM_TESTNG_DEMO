package com.demo.auth;

public class AuthService {

    private final UserRepository repo;
    private final EmailService emailService;

    public AuthService(UserRepository repo, EmailService emailService) {
        this.repo = repo;
        this.emailService = emailService;
    }

    public boolean register(String username, String password) {

        if (username == null || password == null) {
            return false;
        }

        User user = new User(username, password);

        repo.save(user);
        emailService.sendWelcomeEmail(username);

        return true;
    }
    

    public boolean login(String username, String password) {

        User user = repo.findByUsername(username);

        if (user == null) {
            return false;
        }

        return user.getPassword().equals(password);
    }
}
