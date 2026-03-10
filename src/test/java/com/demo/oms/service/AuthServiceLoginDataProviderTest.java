package com.demo.oms.service;

import com.demo.oms.exception.AuthenticationFailedException;
import com.demo.oms.external.EmailService;
import com.demo.oms.model.User;
import com.demo.oms.repository.InMemoryUserRepository;
import com.demo.oms.repository.UserRepository;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthServiceLoginDataProviderTest {

    private AuthService authService;

    @BeforeMethod
    public void setUp() {
        UserRepository userRepository = new InMemoryUserRepository();
        EmailService emailService = mock(EmailService.class);
        when(emailService.sendWelcomeEmail(any(User.class))).thenReturn(true);

        authService = new AuthService(userRepository, emailService);
        authService.register(new User(null, "Alice", "alice@example.com", "alice", "pass123"));
    }

    @DataProvider(name = "loginScenarios")
    public Object[][] loginScenarios() {
        return new Object[][]{
                {"alice", "pass123", true},
                {"alice", "wrong", false},
                {"unknown", "pass123", false},
                {"", "pass123", false},
                {"alice", "", false},
        };
    }

    @Test(dataProvider = "loginScenarios")
    public void shouldSupportMultipleCredentialScenariosUsingDataProvider(String username, String password, boolean shouldSucceed) {
        if (shouldSucceed) {
            Assert.assertNotNull(authService.login(username, password));
        } else {
            Assert.expectThrows(AuthenticationFailedException.class, () -> authService.login(username, password));
        }
    }
}
