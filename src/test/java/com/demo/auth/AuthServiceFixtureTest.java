package com.demo.auth;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AuthServiceFixtureTest {

    private AuthService service;

    @BeforeMethod
    public void setup() {
        UserRepository repo = new UserRepository();
        EmailService email = new EmailService();

        service = new AuthService(repo, email);

        System.out.println("Setup executed");
    }

    @Test
    public void loginTest() {

        service.register("user1", "pass");

        boolean result = service.login("user1", "pass");

        Assert.assertTrue(result);
    }

    @AfterMethod
    public void teardown() {
        System.out.println("Test finished");
    }
}
