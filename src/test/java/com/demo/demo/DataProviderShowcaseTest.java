package com.demo.demo;

import com.demo.oms.model.Product;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;

/**
 * MEMBER 1 DEMO: TestNG DataProvider Showcase
 * 
 * DataProviders allow you to run the same test with multiple sets of input data.
 * This is essential for:
 * - Testing edge cases (boundary values)
 * - Testing multiple scenarios without duplicating code
 * - Improving test coverage efficiently
 */
public class DataProviderShowcaseTest {

    /**
     * DEMO 2.1: Basic DataProvider - Testing User Input Validation
     */
    @DataProvider(name = "usernameValidation")
    public Object[][] provideUsernameTestData() {
        return new Object[][]{
            // {username, isValid, description}
            {"alice", true, "Valid lowercase username"},
            {"Alice123", true, "Valid alphanumeric username"},
            {"john_doe", true, "Valid username with underscore"},
            {"", false, "Empty username"},
            {"ab", false, "Too short (min 3 chars)"},
            {"a".repeat(51), false, "Too long (max 50 chars)"},
            {"user@name", false, "Contains invalid character @"},
            {"user name", false, "Contains space"},
            {null, false, "Null username"}
        };
    }

    @Test(dataProvider = "usernameValidation", priority = 1)
    public void testUsernameValidation(String username, boolean shouldBeValid, String description) {
        System.out.println("Testing: " + description + " -> '" + username + "'");
        
        boolean isValid = isValidUsername(username);
        
        Assert.assertEquals(isValid, shouldBeValid, 
            String.format("Validation failed for: %s (username: '%s')", description, username));
    }

    /**
     * DEMO 2.2: DataProvider for Price Calculations
     */
    @DataProvider(name = "priceCalculations")
    public Object[][] providePriceTestData() {
        return new Object[][]{
            // {price, quantity, expectedTotal}
            {new BigDecimal("10.00"), 1, new BigDecimal("10.00")},
            {new BigDecimal("10.00"), 5, new BigDecimal("50.00")},
            {new BigDecimal("19.99"), 3, new BigDecimal("59.97")},
            {new BigDecimal("0.01"), 100, new BigDecimal("1.00")},
            {new BigDecimal("999.99"), 2, new BigDecimal("1999.98")}
        };
    }

    @Test(dataProvider = "priceCalculations", priority = 2)
    public void testPriceCalculations(BigDecimal unitPrice, int quantity, BigDecimal expectedTotal) {
        System.out.println(String.format("Testing: %s x %d = %s", unitPrice, quantity, expectedTotal));
        
        BigDecimal actualTotal = unitPrice.multiply(new BigDecimal(quantity));
        
        Assert.assertEquals(actualTotal, expectedTotal,
            String.format("Price calculation failed: %s x %d should equal %s", unitPrice, quantity, expectedTotal));
    }

    /**
     * DEMO 2.3: DataProvider for Product Stock Validation
     */
    @DataProvider(name = "stockScenarios")
    public Object[][] provideStockTestData() {
        return new Object[][]{
            // {currentStock, orderQuantity, shouldSucceed, scenario}
            {10, 5, true, "Normal order within stock"},
            {10, 10, true, "Order exactly equals stock"},
            {10, 1, true, "Small order"},
            {10, 11, false, "Order exceeds stock"},
            {0, 1, false, "Out of stock"},
            {5, 0, false, "Zero quantity order"},
            {5, -1, false, "Negative quantity order"}
        };
    }

    @Test(dataProvider = "stockScenarios", priority = 3)
    public void testStockAvailability(int currentStock, int orderQuantity, boolean shouldSucceed, String scenario) {
        System.out.println(String.format("Testing: %s (Stock: %d, Order: %d)", scenario, currentStock, orderQuantity));
        
        Product product = new Product("P1", "Test Product", "Test", new BigDecimal("10.00"), currentStock);
        
        boolean canFulfill = canFulfillOrder(product, orderQuantity);
        
        Assert.assertEquals(canFulfill, shouldSucceed, scenario);
    }

    /**
     * DEMO 2.4: DataProvider for Discount Calculations (Complex Business Logic)
     */
    @DataProvider(name = "discountTiers")
    public Object[][] provideDiscountTestData() {
        return new Object[][]{
            // {orderAmount, expectedDiscountPercent, tier}
            {new BigDecimal("50.00"), 0, "No discount (< $100)"},
            {new BigDecimal("100.00"), 5, "Bronze tier (>= $100)"},
            {new BigDecimal("250.00"), 10, "Silver tier (>= $250)"},
            {new BigDecimal("500.00"), 15, "Gold tier (>= $500)"},
            {new BigDecimal("1000.00"), 20, "Platinum tier (>= $1000)"},
            {new BigDecimal("99.99"), 0, "Just below Bronze threshold"},
            {new BigDecimal("249.99"), 5, "Just below Silver threshold"}
        };
    }

    @Test(dataProvider = "discountTiers", priority = 4)
    public void testDiscountTierCalculation(BigDecimal orderAmount, int expectedDiscountPercent, String tier) {
        System.out.println(String.format("Testing: %s - Order: $%s -> %d%% discount", 
            tier, orderAmount, expectedDiscountPercent));
        
        int actualDiscount = calculateDiscountPercent(orderAmount);
        
        Assert.assertEquals(actualDiscount, expectedDiscountPercent,
            String.format("Discount calculation failed for %s with order amount $%s", tier, orderAmount));
    }

    /**
     * DEMO 2.5: DataProvider with Multiple Methods (Method Parameter)
     */
    @DataProvider(name = "emailValidation")
    public Object[][] provideEmailTestData() {
        return new Object[][]{
            {"alice@example.com", true},
            {"john.doe@company.co.uk", true},
            {"user+tag@domain.com", true},
            {"invalid.email", false},
            {"@example.com", false},
            {"user@", false},
            {"user @example.com", false},
            {"", false},
            {null, false}
        };
    }

    @Test(dataProvider = "emailValidation", priority = 5)
    public void testEmailValidation(String email, boolean shouldBeValid) {
        System.out.println(String.format("Testing email: '%s' (Expected: %s)", 
            email, shouldBeValid ? "Valid" : "Invalid"));
        
        boolean isValid = isValidEmail(email);
        
        Assert.assertEquals(isValid, shouldBeValid,
            String.format("Email validation failed for: '%s'", email));
    }

    /**
     * DEMO 2.6: DataProvider for Password Strength Testing
     */
    @DataProvider(name = "passwordStrength")
    public Object[][] providePasswordTestData() {
        return new Object[][]{
            // {password, expectedStrength, description}
            {"Pass123!", "STRONG", "Contains upper, lower, digit, special"},
            {"password", "WEAK", "Only lowercase"},
            {"PASSWORD", "WEAK", "Only uppercase"},
            {"12345678", "WEAK", "Only digits"},
            {"Pass1234", "MEDIUM", "Upper, lower, digit (no special)"},
            {"pass@123", "MEDIUM", "Lower, digit, special (no upper)"},
            {"abc", "WEAK", "Too short"},
            {"P@ss1", "WEAK", "Too short despite complexity"},
            {"MySecureP@ssw0rd2024!", "STRONG", "Long and complex"}
        };
    }

    @Test(dataProvider = "passwordStrength", priority = 6)
    public void testPasswordStrength(String password, String expectedStrength, String description) {
        System.out.println(String.format("Testing: %s -> '%s'", description, password));
        
        String actualStrength = calculatePasswordStrength(password);
        
        Assert.assertEquals(actualStrength, expectedStrength,
            String.format("Password strength mismatch for: %s", description));
    }

    // ==================== Helper Methods ====================

    private boolean isValidUsername(String username) {
        if (username == null || username.isEmpty()) return false;
        if (username.length() < 3 || username.length() > 50) return false;
        return username.matches("^[a-zA-Z0-9_]+$");
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    private boolean canFulfillOrder(Product product, int quantity) {
        if (quantity <= 0) return false;
        return product.getStockQuantity() >= quantity;
    }

    private int calculateDiscountPercent(BigDecimal amount) {
        if (amount.compareTo(new BigDecimal("1000")) >= 0) return 20;
        if (amount.compareTo(new BigDecimal("500")) >= 0) return 15;
        if (amount.compareTo(new BigDecimal("250")) >= 0) return 10;
        if (amount.compareTo(new BigDecimal("100")) >= 0) return 5;
        return 0;
    }

    private String calculatePasswordStrength(String password) {
        if (password == null || password.length() < 8) return "WEAK";
        
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*[0-9].*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
        
        int complexityScore = 0;
        if (hasUpper) complexityScore++;
        if (hasLower) complexityScore++;
        if (hasDigit) complexityScore++;
        if (hasSpecial) complexityScore++;
        
        if (complexityScore >= 4) return "STRONG";
        if (complexityScore >= 2) return "MEDIUM";
        return "WEAK";
    }
}
