package com.demo.auth;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthServiceMockTest {

    @Test
    public void testRegisterWithMockEmail() {

        UserRepository repo = new UserRepository();

        EmailService mockEmail = mock(EmailService.class);
        when(mockEmail.sendWelcomeEmail(anyString())).thenReturn(true);

        AuthService service = new AuthService(repo, mockEmail);

        boolean result = service.register("john", "123");

        Assert.assertTrue(result);
        verify(mockEmail, times(1)).sendWelcomeEmail("john");
    }

    @Test
    public void testRegisterRejectsNullsWithoutCallingEmail() {

        UserRepository repo = new UserRepository();

        EmailService mockEmail = mock(EmailService.class);

        AuthService service = new AuthService(repo, mockEmail);

        boolean result = service.register(null, null);

        Assert.assertFalse(result);
        verify(mockEmail, times(0)).sendWelcomeEmail(anyString());
    }
}
