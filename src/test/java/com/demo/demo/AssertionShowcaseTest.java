package com.demo.demo;

import com.demo.oms.model.Product;
import com.demo.oms.model.User;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * MEMBER 1 DEMO: TestNG Assertions Showcase
 * 
 * This class demonstrates various assertion types available in TestNG:
 * - Basic assertions (assertEquals, assertTrue, assertFalse)
 * - Null checks (assertNull, assertNotNull)
 * - Exception assertions (expectThrows)
 * - Collection assertions
 * - Soft assertions (continue testing after failures)
 * - Custom assertion messages
 */
public class AssertionShowcaseTest {

    /**
     * DEMO 1.1: Basic Boolean Assertions
     */
    @Test(priority = 1)
    public void demonstrateBasicBooleanAssertions() {
        boolean isUserActive = true;
        boolean isUserBanned = false;

        // Basic boolean assertions
        Assert.assertTrue(isUserActive, "User should be active");
        Assert.assertFalse(isUserBanned, "User should not be banned");
        
        System.out.println("✓ Basic boolean assertions passed");
    }

    /**
     * DEMO 1.2: Equality Assertions
     */
    @Test(priority = 2)
    public void demonstrateEqualityAssertions() {
        String expectedUsername = "alice";
        String actualUsername = "alice";
        
        int expectedCount = 5;
        int actualCount = 5;
        
        BigDecimal expectedPrice = new BigDecimal("19.99");
        BigDecimal actualPrice = new BigDecimal("19.99");

        // Equality checks
        Assert.assertEquals(actualUsername, expectedUsername, "Username mismatch");
        Assert.assertEquals(actualCount, expectedCount, "Count mismatch");
        Assert.assertEquals(actualPrice, expectedPrice, "Price mismatch");
        
        // Not equals
        Assert.assertNotEquals(actualUsername, "bob", "Should be different users");
        
        System.out.println("✓ Equality assertions passed");
    }

    /**
     * DEMO 1.3: Null Checks
     */
    @Test(priority = 3)
    public void demonstrateNullAssertions() {
        User validUser = new User("U1", "Alice", "alice@test.com", "alice", "pass");
        User nullUser = null;
        
        // Null assertions
        Assert.assertNotNull(validUser, "User should not be null");
        Assert.assertNull(nullUser, "User should be null");
        
        // Useful for checking created objects
        Assert.assertNotNull(validUser.getUserId(), "User ID should be assigned");
        
        System.out.println("✓ Null assertions passed");
    }

    /**
     * DEMO 1.4: Exception Assertions (Modern Way)
     */
    @Test(priority = 4)
    public void demonstrateExceptionAssertions() {
        Product product = new Product("P1", "Mouse", "Wireless", new BigDecimal("19.99"), 5);
        
        // Assert that specific exception is thrown
        Assert.expectThrows(IllegalArgumentException.class, () -> {
            product.setStockQuantity(-10); // Negative stock should throw exception
        });
        
        // Can also capture and verify exception message
        IllegalArgumentException exception = Assert.expectThrows(
            IllegalArgumentException.class,
            () -> product.setStockQuantity(-5)
        );
        
        Assert.assertTrue(
            exception.getMessage().contains("Stock quantity cannot be negative"),
            "Exception message should mention negative stock"
        );
        
        System.out.println("✓ Exception assertions passed");
    }

    /**
     * DEMO 1.5: Collection Assertions
     */
    @Test(priority = 5)
    public void demonstrateCollectionAssertions() {
        List<String> productCategories = Arrays.asList("Electronics", "Books", "Clothing");
        List<String> expectedCategories = Arrays.asList("Electronics", "Books", "Clothing");
        
        // List equality
        Assert.assertEquals(productCategories, expectedCategories, "Categories should match");
        
        // Check if list contains specific item
        // Assert.assertTrue(productCategories.contains("Electronics"), "Should contain Electronics");
        Assert.assertFalse(productCategories.contains("Electronics"), "Should contain Electronics");
        
        // Size checks
        Assert.assertEquals(productCategories.size(), 3, "Should have 3 categories");
        
        System.out.println("✓ Collection assertions passed");
    }

    /**
     * DEMO 1.6: Soft Assertions (Continue Testing After Failures)
     * 
     * Unlike hard assertions, soft assertions don't stop test execution immediately.
     * All assertions are checked, then reported at the end.
     */
    @Test(priority = 6)
    public void demonstrateSoftAssertions() {
        SoftAssert softAssert = new SoftAssert();
        
        User user = new User("U1", "Alice Smith", "alice@test.com", "alice", "pass123");
        
        // These will all be checked even if some fail
        softAssert.assertEquals(user.getUserId(), "U1", "User ID check");
        softAssert.assertEquals(user.getName(), "Alice Smith", "Name check");
        softAssert.assertTrue(user.getEmail().contains("@"), "Email should contain @");
        softAssert.assertNotNull(user.getUsername(), "Username should not be null");
        
        // IMPORTANT: Must call assertAll() to trigger actual test failure if any soft assertion failed
        softAssert.assertAll();
        
        System.out.println("✓ All soft assertions passed");
    }

    /**
     * DEMO 1.7: Custom Assertion Messages
     * 
     * Good assertion messages make test failures easier to debug
     */
    @Test(priority = 7)
    public void demonstrateCustomAssertionMessages() {
        Product product = new Product("P1", "Gaming Mouse", "Wireless RGB", new BigDecimal("49.99"), 15);
        
        // Bad: No message
        // Assert.assertEquals(product.getName(), "Gaming Mouse");
        
        // Good: Clear message explaining what's being tested and why
        Assert.assertEquals(
            product.getName(), 
            "Gaming Mouse",
            "Product name should match the name provided during construction"
        );
        
        Assert.assertTrue(
            product.getPrice().compareTo(BigDecimal.ZERO) > 0,
            "Product price must be positive for valid products"
        );
        
        Assert.assertTrue(
            product.getStockQuantity() >= 0,
            "Stock quantity cannot be negative - inventory must be valid"
        );
        
        System.out.println("✓ Custom message assertions passed");
    }

    /**
     * DEMO 1.8: Real-World Business Logic Assertions
     */
    @Test(priority = 8)
    public void demonstrateBusinessLogicAssertions() {
        Product laptop = new Product("P100", "Laptop", "15-inch", new BigDecimal("999.99"), 10);
        
        // Business rule: Products over $500 should be marked as premium
        boolean isPremium = laptop.getPrice().compareTo(new BigDecimal("500")) > 0;
        Assert.assertTrue(isPremium, "Products over $500 should be classified as premium");
        
        // Business rule: Electronic items should have warranty
        boolean hasWarranty = laptop.getDescription().length() > 0;
        Assert.assertTrue(hasWarranty, "Electronic products must have description/warranty info");
        
        // Business rule: Minimum stock threshold for expensive items
        int minStockForExpensiveItems = 5;
        Assert.assertTrue(
            laptop.getStockQuantity() >= minStockForExpensiveItems,
            String.format("Expensive products should maintain minimum stock of %d units", minStockForExpensiveItems)
        );
        
        System.out.println("✓ Business logic assertions passed");
    }
}
