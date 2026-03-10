package com.demo.demo;

import com.demo.oms.external.EmailService;
import com.demo.oms.external.PaymentGateway;
import com.demo.oms.model.Order;
import com.demo.oms.model.Product;
import com.demo.oms.model.User;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * MEMBER 3 DEMO: Mockito Mocking & Stubbing Showcase
 * 
 * Mocking is essential for testing code that depends on external services:
 * - Database connections
 * - Email services
 * - Payment gateways
 * - Web APIs
 * - File systems
 * 
 * Benefits:
 * - Tests run faster (no real I/O operations)
 * - Tests are reliable (no network failures)
 * - Can test error scenarios easily
 * - No side effects (won't send real emails!)
 */
public class MockingShowcaseTest {

    private EmailService emailService;
    private PaymentGateway paymentGateway;
    private User testUser;
    private Product testProduct;

    @BeforeMethod
    public void setUp() {
        // Create real objects for testing
        testUser = new User("U1", "Alice", "alice@test.com", "alice", "pass123");
        testProduct = new Product("P1", "Laptop", "15-inch", new BigDecimal("999.99"), 10);
        
        // Create mock objects (no real implementation needed!)
        emailService = mock(EmailService.class);
        paymentGateway = mock(PaymentGateway.class);
        
        System.out.println("✓ Mock objects created for tests");
    }

    /**
     * DEMO 3.1: Basic Mocking - Create Mock Object
     * 
     * Why mock? We want to test our order service without actually sending emails!
     */
    @Test(priority = 1)
    public void demonstrateBasicMocking() {
        System.out.println("\n=== DEMO 3.1: Basic Mocking ===");
        
        // Without mocking: would need real email server, credentials, etc.
        // With mocking: just create a fake object!
        
        EmailService mockEmail = mock(EmailService.class);
        
        // The mock object exists and can be used
        Assert.assertNotNull(mockEmail, "Mock should be created");
        
        // But it does nothing by default (returns null/false/0)
        boolean result = mockEmail.sendWelcomeEmail(testUser);
        Assert.assertFalse(result, "Mock returns false by default");
        
        System.out.println("✓ Mock object created and behaves as expected");
    }

    /**
     * DEMO 3.2: Stubbing - Define Mock Behavior with when().thenReturn()
     * 
     * Stubbing lets you control what the mock returns
     */
    @Test(priority = 2)
    public void demonstrateStubbing() {
        System.out.println("\n=== DEMO 3.2: Stubbing ===");
        
        // Tell mock what to return when method is called
        when(emailService.sendWelcomeEmail(any(User.class))).thenReturn(true);
        
        // Now our mock behaves the way we want
        boolean result = emailService.sendWelcomeEmail(testUser);
        Assert.assertTrue(result, "Stubbed method should return true");
        
        System.out.println("✓ Stubbing successful - mock returned expected value");
    }

    /**
     * DEMO 3.3: Argument Matchers - Match Any Argument
     * 
     * Use matchers to stub methods regardless of input parameters
     */
    @Test(priority = 3)
    public void demonstrateArgumentMatchers() {
        System.out.println("\n=== DEMO 3.3: Argument Matchers ===");
        
        // any(Class.class) - matches any object of that type
        when(emailService.sendWelcomeEmail(any(User.class))).thenReturn(true);
        
        // anyString() - matches any string
        when(emailService.sendPasswordResetEmail(anyString())).thenReturn(true);
        
        // Works with any user
        User user1 = new User("U1", "Alice", "alice@test.com", "alice", "pass");
        User user2 = new User("U2", "Bob", "bob@test.com", "bob", "pass");
        
        Assert.assertTrue(emailService.sendWelcomeEmail(user1));
        Assert.assertTrue(emailService.sendWelcomeEmail(user2));
        Assert.assertTrue(emailService.sendPasswordResetEmail("alice@test.com"));
        Assert.assertTrue(emailService.sendPasswordResetEmail("bob@test.com"));
        
        System.out.println("✓ Argument matchers work for multiple inputs");
    }

    /**
     * DEMO 3.4: Specific Argument Matching
     * 
     * Stub different return values for different inputs
     */
    @Test(priority = 4)
    public void demonstrateSpecificArgumentMatching() {
        System.out.println("\n=== DEMO 3.4: Specific Argument Matching ===");
        
        User validUser = new User("U1", "Valid", "valid@test.com", "valid", "pass");
        User invalidUser = new User("U2", "Invalid", "invalid@test.com", "invalid", "pass");
        
        // Different return values for different users
        when(emailService.sendWelcomeEmail(validUser)).thenReturn(true);
        when(emailService.sendWelcomeEmail(invalidUser)).thenReturn(false);
        
        Assert.assertTrue(emailService.sendWelcomeEmail(validUser), "Valid user should succeed");
        Assert.assertFalse(emailService.sendWelcomeEmail(invalidUser), "Invalid user should fail");
        
        System.out.println("✓ Specific argument matching works correctly");
    }

    /**
     * DEMO 3.5: Verification - Verify Method Was Called
     * 
     * Check that our code actually uses the mocked service
     */
    @Test(priority = 5)
    public void demonstrateVerification() {
        System.out.println("\n=== DEMO 3.5: Verification ===");
        
        when(emailService.sendWelcomeEmail(any(User.class))).thenReturn(true);
        
        // Call the service
        emailService.sendWelcomeEmail(testUser);
        emailService.sendWelcomeEmail(testUser);
        
        // Verify it was called (at least once)
        verify(emailService, atLeastOnce()).sendWelcomeEmail(testUser);
        
        // Verify exact number of calls
        verify(emailService, times(2)).sendWelcomeEmail(testUser);
        
        System.out.println("✓ Verification successful - method was called expected times");
    }

    /**
     * DEMO 3.6: Verify With Argument Matchers
     */
    @Test(priority = 6)
    public void demonstrateVerificationWithMatchers() {
        System.out.println("\n=== DEMO 3.6: Verification with Matchers ===");
        
        emailService.sendWelcomeEmail(testUser);
        
        // Verify with any User (not specific instance)
        verify(emailService, times(1)).sendWelcomeEmail(any(User.class));
        
        System.out.println("✓ Verification with matchers successful");
    }

    /**
     * DEMO 3.7: Verify Method Was Never Called
     * 
     * Important for testing error scenarios
     */
    @Test(priority = 7)
    public void demonstrateVerifyNever() {
        System.out.println("\n=== DEMO 3.7: Verify Never Called ===");
        
        // In case of validation failure, email should NOT be sent
        // (We don't actually call the method)
        
        // Verify email was never sent
        verify(emailService, never()).sendWelcomeEmail(any(User.class));
        verify(emailService, never()).sendPasswordResetEmail(anyString());
        
        System.out.println("✓ Verified methods were never called (as expected)");
    }

    /**
     * DEMO 3.8: Stubbing Void Methods with doNothing()
     * 
     * Sometimes methods don't return anything
     */
    @Test(priority = 8)
    public void demonstrateVoidMethodStubbing() {
        System.out.println("\n=== DEMO 3.8: Void Method Stubbing ===");
        
        // For void methods, use doNothing()
        doNothing().when(emailService).logEmailSent(anyString());
        
        // Call the void method - no exception thrown
        emailService.logEmailSent("Email sent to alice@test.com");
        
        // Verify it was called
        verify(emailService, times(1)).logEmailSent(anyString());
        
        System.out.println("✓ Void method stubbed and verified");
    }

    /**
     * DEMO 3.9: Stubbing Exceptions with thenThrow()
     * 
     * Test how your code handles failures
     */
    @Test(priority = 9)
    public void demonstrateExceptionStubbing() {
        System.out.println("\n=== DEMO 3.9: Exception Stubbing ===");
        
        // Simulate email service failure
        when(emailService.sendWelcomeEmail(any(User.class)))
            .thenThrow(new RuntimeException("Email server down"));
        
        // Verify exception is thrown
        Assert.expectThrows(RuntimeException.class, () -> {
            emailService.sendWelcomeEmail(testUser);
        });
        
        System.out.println("✓ Exception stubbing works - tested error scenario");
    }

    /**
     * DEMO 3.10: Stubbing Exceptions with doThrow() for Void Methods
     */
    @Test(priority = 10)
    public void demonstrateVoidMethodExceptionStubbing() {
        System.out.println("\n=== DEMO 3.10: Void Method Exception Stubbing ===");
        
        // For void methods, use doThrow()
        doThrow(new RuntimeException("Logging failed"))
            .when(emailService).logEmailSent(anyString());
        
        // Verify exception is thrown
        Assert.expectThrows(RuntimeException.class, () -> {
            emailService.logEmailSent("test message");
        });
        
        System.out.println("✓ Void method exception stubbing successful");
    }

    /**
     * DEMO 3.11: doReturn() for Stubbing (Alternative Syntax)
     */
    @Test(priority = 11)
    public void demonstrateDoReturn() {
        System.out.println("\n=== DEMO 3.11: doReturn() Syntax ===");
        
        // Alternative to when().thenReturn()
        doReturn(true).when(emailService).sendWelcomeEmail(any(User.class));
        
        boolean result = emailService.sendWelcomeEmail(testUser);
        Assert.assertTrue(result);
        
        System.out.println("✓ doReturn() syntax works correctly");
    }

    /**
     * DEMO 3.12: Real-World Scenario - Payment Processing
     * 
     * Mock payment gateway to test order processing without real transactions
     */
    @Test(priority = 12)
    public void demonstrateRealWorldPaymentScenario() {
        System.out.println("\n=== DEMO 3.12: Real-World Payment Scenario ===");
        
        Order order = new Order("ORD-1", testUser);
        order.setTotalAmount(new BigDecimal("999.99"));
        
        // Stub successful payment
        when(paymentGateway.processPayment(anyString(), any(BigDecimal.class)))
            .thenReturn("PAYMENT-SUCCESS-12345");
        
        // Stub successful email
        when(emailService.sendOrderConfirmation(any(User.class), any(Order.class)))
            .thenReturn(true);
        
        // Simulate order processing
        String paymentId = paymentGateway.processPayment("CARD-123", order.getTotalAmount());
        boolean emailSent = emailService.sendOrderConfirmation(testUser, order);
        
        // Assertions
        Assert.assertNotNull(paymentId, "Payment should be processed");
        Assert.assertTrue(emailSent, "Confirmation email should be sent");
        
        // Verify both services were called
        verify(paymentGateway, times(1)).processPayment(anyString(), any(BigDecimal.class));
        verify(emailService, times(1)).sendOrderConfirmation(testUser, order);
        
        System.out.println("✓ Real-world scenario tested without external dependencies!");
        System.out.println("  → No actual payment charged");
        System.out.println("  → No actual email sent");
        System.out.println("  → Test runs in milliseconds, not seconds");
    }

    /**
     * DEMO 3.13: Real-World Scenario - Payment Failure
     */
    @Test(priority = 13)
    public void demonstratePaymentFailureScenario() {
        System.out.println("\n=== DEMO 3.13: Payment Failure Scenario ===");
        
        Order order = new Order("ORD-2", testUser);
        order.setTotalAmount(new BigDecimal("999.99"));
        
        // Stub payment failure
        when(paymentGateway.processPayment(anyString(), any(BigDecimal.class)))
            .thenThrow(new RuntimeException("Card declined"));
        
        // Verify exception is thrown
        Assert.expectThrows(RuntimeException.class, () -> {
            paymentGateway.processPayment("CARD-INVALID", order.getTotalAmount());
        });
        
        // Verify email was NOT sent after payment failure
        verify(emailService, never()).sendOrderConfirmation(any(User.class), any(Order.class));
        
        System.out.println("✓ Payment failure scenario tested correctly");
        System.out.println("  → No real payment attempt made");
        System.out.println("  → Confirmed email is not sent on failure");
    }

    /**
     * DEMO 3.14: Spy - Partial Mocking (Real Object with Some Mocked Methods)
     */
    @Test(priority = 14)
    public void demonstrateSpy() {
        System.out.println("\n=== DEMO 3.14: Spy (Partial Mocking) ===");
        
        // Spy uses real object but allows selective stubbing
        Product realProduct = new Product("P1", "Laptop", "15-inch", new BigDecimal("999.99"), 10);
        Product spyProduct = spy(realProduct);
        
        // Real method calls work normally
        Assert.assertEquals(spyProduct.getName(), "Laptop");
        Assert.assertEquals(spyProduct.getStockQuantity(), 10);
        
        // But we can stub specific methods
        when(spyProduct.getPrice()).thenReturn(new BigDecimal("799.99")); // Discounted!
        
        Assert.assertEquals(spyProduct.getPrice(), new BigDecimal("799.99"), "Stubbed price");
        Assert.assertEquals(spyProduct.getName(), "Laptop", "Real method still works");
        
        System.out.println("✓ Spy allows partial mocking of real objects");
    }
}
