package com.demo.oms.service;

import com.demo.oms.exception.AuthenticationFailedException;
import com.demo.oms.exception.UserAlreadyExistsException;
import com.demo.oms.external.EmailService;
import com.demo.oms.model.User;
import com.demo.oms.repository.InMemoryUserRepository;
import com.demo.oms.repository.UserRepository;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthServiceTest {

    private UserRepository userRepository;
    private EmailService emailService;
    private AuthService authService;

    @BeforeMethod
    public void setUp() {
        userRepository = new InMemoryUserRepository();
        emailService = mock(EmailService.class);
        when(emailService.sendWelcomeEmail(any(User.class))).thenReturn(true);

        authService = new AuthService(userRepository, emailService);
    }

    @Test
    public void shouldRegisterUserSuccessfully() {
        User user = new User(null, "Alice Perera", "alice@example.com", "alice", "pass123");

        User saved = authService.register(user);

        Assert.assertNotNull(saved.getUserId());
        Assert.assertTrue(userRepository.existsByUsername("alice"));
        Assert.assertTrue(userRepository.existsByEmail("alice@example.com"));
        verify(emailService, times(1)).sendWelcomeEmail(saved);
    }

    @Test
    public void shouldNotRegisterDuplicateUsername() {
        authService.register(new User(null, "Alice", "alice@example.com", "alice", "pass123"));

        Assert.expectThrows(UserAlreadyExistsException.class, () ->
                authService.register(new User(null, "Another", "another@example.com", "alice", "pass456"))
        );
    }

    @Test
    public void shouldLoginSuccessfullyWithCorrectCredentials() {
        authService.register(new User(null, "Alice", "alice@example.com", "alice", "pass123"));

        User loggedIn = authService.login("alice", "pass123");

        Assert.assertNotNull(loggedIn);
        Assert.assertEquals(loggedIn.getUsername(), "alice");
    }

    @Test
    public void shouldFailLoginWithIncorrectPassword() {
        authService.register(new User(null, "Alice", "alice@example.com", "alice", "pass123"));

        Assert.expectThrows(AuthenticationFailedException.class, () -> authService.login("alice", "wrong"));
    }
}
