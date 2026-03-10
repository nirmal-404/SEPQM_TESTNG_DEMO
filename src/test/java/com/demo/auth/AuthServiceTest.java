package com.demo.auth;

import org.testng.Assert;
import org.testng.annotations.Test;

public class AuthServiceTest {

    @Test
    public void testSuccessfulLogin() {

        UserRepository repo = new UserRepository();
        EmailService email = new EmailService();
        AuthService service = new AuthService(repo, email);

        service.register("dishan", "1234");

        boolean result = service.login("dishan", "1234");

        Assert.assertTrue(result);
    }

    @Test
    public void testLoginFailsForUnknownUser() {

        UserRepository repo = new UserRepository();
        EmailService email = new EmailService();
        AuthService service = new AuthService(repo, email);

        boolean result = service.login("unknown", "1234");

        Assert.assertFalse(result);
    }
}
