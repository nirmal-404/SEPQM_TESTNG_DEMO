package com.demo.demo;

import com.demo.oms.model.Product;
import com.demo.oms.model.User;
import org.testng.Assert;
import org.testng.annotations.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * MEMBER 2 DEMO: TestNG Fixtures (Setup & Teardown) Showcase
 * 
 * Fixtures are methods that run before/after tests to set up test environment.
 * TestNG provides multiple fixture levels:
 * - @BeforeSuite / @AfterSuite - Runs once for entire test suite
 * - @BeforeTest / @AfterTest - Runs once per <test> tag in testng.xml
 * - @BeforeClass / @AfterClass - Runs once per test class
 * - @BeforeMethod / @AfterMethod - Runs before/after EACH test method
 */
public class FixtureShowcaseTest {

    // Shared test data
    private List<Product> productCatalog;
    private User testUser;
    private int testMethodCounter = 0;
    private long testStartTime;

    /**
     * DEMO 2.1: @BeforeSuite - Runs ONCE before entire test suite
     * Use for: Database connections, loading config files, initializing shared resources
     */
    @BeforeSuite
    public void setUpTestSuite() {
        System.out.println("\n========================================");
        System.out.println("@BeforeSuite: Initializing Test Suite");
        System.out.println("========================================");
        System.out.println("→ Loading configuration...");
        System.out.println("→ Establishing shared resources...");
        System.out.println("→ Test suite ready!\n");
    }

    /**
     * DEMO 2.2: @BeforeTest - Runs once per <test> tag in testng.xml
     * Use for: Setting up test-specific configuration
     */
    @BeforeTest
    public void setUpTest() {
        System.out.println("\n----------------------------------------");
        System.out.println("@BeforeTest: Setting up test group");
        System.out.println("----------------------------------------");
    }

    /**
     * DEMO 2.3: @BeforeClass - Runs ONCE before all test methods in this class
     * Use for: Creating objects that can be reused across all test methods
     */
    @BeforeClass
    public void setUpTestClass() {
        System.out.println("\n========================================");
        System.out.println("@BeforeClass: Setting up FixtureShowcaseTest");
        System.out.println("========================================");
        
        // Initialize shared test data
        productCatalog = new ArrayList<>();
        productCatalog.add(new Product("P1", "Laptop", "15-inch", new BigDecimal("999.99"), 10));
        productCatalog.add(new Product("P2", "Mouse", "Wireless", new BigDecimal("19.99"), 50));
        productCatalog.add(new Product("P3", "Keyboard", "Mechanical", new BigDecimal("89.99"), 30));
        
        System.out.println("→ Created product catalog with " + productCatalog.size() + " products");
        System.out.println("→ Test class ready!\n");
    }

    /**
     * DEMO 2.4: @BeforeMethod - Runs BEFORE EACH test method
     * Use for: Creating fresh test data, resetting state, starting timers
     */
    @BeforeMethod
    public void setUpTestMethod() {
        testMethodCounter++;
        testStartTime = System.currentTimeMillis();
        
        // Create fresh user for each test to ensure test isolation
        testUser = new User("U" + testMethodCounter, "TestUser" + testMethodCounter, 
                           "user" + testMethodCounter + "@test.com", 
                           "testuser" + testMethodCounter, "pass123");
        
        System.out.println("\n► @BeforeMethod [Test #" + testMethodCounter + "]: Setting up test environment");
        System.out.println("  → Created fresh test user: " + testUser.getUsername());
        System.out.println("  → Test start time: " + testStartTime);
    }

    /**
     * DEMO 2.5: Test Method 1 - Testing Product Availability
     */
    @Test(priority = 1)
    public void testProductAvailability() {
        System.out.println("  → Running: testProductAvailability()");
        
        // Test uses shared productCatalog (from @BeforeClass)
        Assert.assertEquals(productCatalog.size(), 3, "Should have 3 products");
        
        Product laptop = productCatalog.get(0);
        Assert.assertTrue(laptop.getStockQuantity() > 0, "Laptop should be in stock");
        
        System.out.println("  ✓ Test passed: Product catalog is available");
    }

    /**
     * DEMO 2.6: Test Method 2 - Testing User Creation
     */
    @Test(priority = 2)
    public void testUserCreation() {
        System.out.println("  → Running: testUserCreation()");
        
        // Test uses fresh testUser (from @BeforeMethod)
        Assert.assertNotNull(testUser, "User should be created");
        Assert.assertNotNull(testUser.getUserId(), "User should have ID");
        Assert.assertTrue(testUser.getEmail().contains("@"), "Email should be valid");
        
        System.out.println("  ✓ Test passed: User created successfully");
    }

    /**
     * DEMO 2.7: Test Method 3 - Testing Test Isolation
     */
    @Test(priority = 3)
    public void testIsolationBetweenTests() {
        System.out.println("  → Running: testIsolationBetweenTests()");
        
        // Each test gets a FRESH user (not reused from previous tests)
        // This demonstrates test isolation through @BeforeMethod
        String originalUsername = testUser.getUsername();
        testUser.setUsername("modified_user");
        
        System.out.println("  → Modified username from '" + originalUsername + "' to '" + testUser.getUsername() + "'");
        System.out.println("  → This change won't affect other tests (each gets fresh data)");
        
        Assert.assertEquals(testUser.getUsername(), "modified_user");
        System.out.println("  ✓ Test passed: Test isolation works correctly");
    }

    /**
     * DEMO 2.8: Test Method 4 - Long Running Test
     */
    @Test(priority = 4)
    public void testLongRunningOperation() throws InterruptedException {
        System.out.println("  → Running: testLongRunningOperation()");
        
        // Simulate long-running operation
        System.out.println("  → Simulating database operation...");
        Thread.sleep(500); // Sleep for 500ms
        
        Assert.assertNotNull(testUser, "User should exist after operation");
        System.out.println("  ✓ Test passed: Operation completed successfully");
    }

    /**
     * DEMO 2.9: @AfterMethod - Runs AFTER EACH test method
     * Use for: Cleanup, logging results, releasing resources, recording metrics
     */
    @AfterMethod
    public void tearDownTestMethod() {
        long testEndTime = System.currentTimeMillis();
        long duration = testEndTime - testStartTime;
        
        System.out.println("◄ @AfterMethod [Test #" + testMethodCounter + "]: Cleaning up");
        System.out.println("  → Test execution time: " + duration + "ms");
        System.out.println("  → Releasing test user: " + testUser.getUsername());
        
        // Cleanup
        testUser = null;
        
        System.out.println("  → Cleanup complete");
    }

    /**
     * DEMO 2.10: @AfterClass - Runs ONCE after all test methods in this class
     * Use for: Releasing shared resources, final cleanup
     */
    @AfterClass
    public void tearDownTestClass() {
        System.out.println("\n========================================");
        System.out.println("@AfterClass: Tearing down FixtureShowcaseTest");
        System.out.println("========================================");
        System.out.println("→ Total tests executed: " + testMethodCounter);
        System.out.println("→ Clearing product catalog...");
        
        productCatalog.clear();
        productCatalog = null;
        
        System.out.println("→ Test class cleanup complete!\n");
    }

    /**
     * DEMO 2.11: @AfterTest - Runs once per <test> tag in testng.xml
     */
    @AfterTest
    public void tearDownTest() {
        System.out.println("\n----------------------------------------");
        System.out.println("@AfterTest: Tearing down test group");
        System.out.println("----------------------------------------");
    }

    /**
     * DEMO 2.12: @AfterSuite - Runs ONCE after entire test suite
     * Use for: Closing database connections, generating reports, final cleanup
     */
    @AfterSuite
    public void tearDownTestSuite() {
        System.out.println("\n========================================");
        System.out.println("@AfterSuite: Test Suite Completed");
        System.out.println("========================================");
        System.out.println("→ Closing shared resources...");
        System.out.println("→ Generating final reports...");
        System.out.println("→ All tests complete!\n");
    }

    /**
     * BONUS: Advanced Fixture Feature - @BeforeGroups / @AfterGroups
     * Run before/after specific test groups
     */
    @BeforeGroups("database-tests")
    public void setUpDatabaseTests() {
        System.out.println("\n→ @BeforeGroups: Setting up database connection for database-tests group");
    }

    @Test(groups = "database-tests", priority = 5)
    public void testDatabaseOperation() {
        System.out.println("  → Running: testDatabaseOperation() [database-tests group]");
        Assert.assertTrue(true, "Database test executed");
    }

    @AfterGroups("database-tests")
    public void tearDownDatabaseTests() {
        System.out.println("← @AfterGroups: Closing database connection for database-tests group\n");
    }
}
