# 🎯 Complete TestNG Demo Plan (10 Minutes)

## Session Overview
**Date:** March 8, 2026  
**Project:** SEPQM_TestNG_DEMO - Online Order Management System  
**Total Duration:** 10 minutes  
**Team Size:** 4 members  

---

## 📦 Pre-Demo Setup Checklist

### ✅ Before Starting (5 minutes before demo)

1. **Verify Environment**
   ```bash
   java -version    # Should show Java 17+
   mvn -version     # Should show Maven 3.9+
   ```

2. **Run Tests to Ensure Everything Works**
   ```bash
   cd F:\Projects\SEPQM_TestNG_DEMO
   mvn clean test
   ```
   - Should see: "Tests run: 26+, Failures: 0"

3. **Open Key Files in Tabs** (for quick navigation during demo)
   - `src/test/java/com/demo/demo/AssertionShowcaseTest.java`
   - `src/test/java/com/demo/demo/DataProviderShowcaseTest.java`
   - `src/test/java/com/demo/demo/FixtureShowcaseTest.java`
   - `src/test/java/com/demo/demo/MockingShowcaseTest.java`
   - `src/test/resources/testng.xml`
   - `.github/workflows/ci.yml`
   - `test-output/index.html`

4. **Prepare Terminal Windows**
   - Terminal 1: For running tests
   - Terminal 2: Backup terminal
   - Browser: Open GitHub Actions page (if demonstrating CI/CD live)

---

# 👥 MEMBER 1: Assertions & Data Providers (2.5 minutes)

## 🎯 Learning Objectives
- Understand different assertion types in TestNG
- Learn how DataProviders eliminate test duplication
- See real-world validation scenarios

## 📝 Demo Script

### Part A: Assertions (1.5 minutes)

**Opening Statement:**
> "Assertions are the foundation of testing. They verify that our code produces expected results. Let me show you the different types available in TestNG."

#### Step 1: Show AssertionShowcaseTest.java (30 seconds)
```java
// Point to file: src/test/java/com/demo/demo/AssertionShowcaseTest.java

// Highlight these key sections:
```

**What to Say:**
- "This test class demonstrates 8 types of assertions"
- "We'll focus on the most important ones"

#### Step 2: Run Assertion Tests (20 seconds)
```bash
mvn test -Dtest=AssertionShowcaseTest
```

**What to Say:**
- "Notice how each test validates different aspects"
- "All 8 assertions passed - let's look at a few"

#### Step 3: Explain Key Assertions (40 seconds)

**Navigate to these methods and explain:**

1. **Basic Assertions** (Line ~28)
   ```java
   Assert.assertTrue(isUserActive, "User should be active");
   Assert.assertEquals(actualUsername, expectedUsername, "Username mismatch");
   ```
   - "These are your most common assertions"
   - "Always include custom messages for clarity"

2. **Exception Assertions** (Line ~73)
   ```java
   Assert.expectThrows(IllegalArgumentException.class, () -> {
       product.setStockQuantity(-10);
   });
   ```
   - "Modern way to test for expected exceptions"
   - "Useful for validation testing"

3. **Soft Assertions** (Line ~110)
   ```java
   SoftAssert softAssert = new SoftAssert();
   softAssert.assertEquals(user.getName(), "Alice");
   softAssert.assertAll(); // MUST call this!
   ```
   - "Soft assertions don't stop on first failure"
   - "Collects all failures and reports at end"

---

### Part B: Data Providers (1.0 minute)

**Transition Statement:**
> "Now imagine testing login with 100 different username/password combinations. Writing 100 separate test methods would be insane. That's where DataProviders save us."

#### Step 4: Show DataProvider Example (30 seconds)
```java
// Point to: src/test/java/com/demo/demo/DataProviderShowcaseTest.java

// Highlight this pattern (Line ~23):
@DataProvider(name = "usernameValidation")
public Object[][] provideUsernameTestData() {
    return new Object[][]{
        {"alice", true, "Valid lowercase username"},
        {"", false, "Empty username"},
        {"ab", false, "Too short (min 3 chars)"}
        // 9 test cases total!
    };
}

@Test(dataProvider = "usernameValidation")
public void testUsernameValidation(String username, boolean shouldBeValid, String description) {
    boolean isValid = isValidUsername(username);
    Assert.assertEquals(isValid, shouldBeValid, description);
}
```

**What to Say:**
- "One DataProvider method returns multiple test scenarios"
- "One test method runs 9 times with different inputs"
- "1 test method generates 9 test cases!"

#### Step 5: Run DataProvider Tests (30 seconds)
```bash
mvn test -Dtest=DataProviderShowcaseTest#testUsernameValidation
```

**What to Say:**
- "Look at the output - 9 tests executed from 1 method"
- "Tests: Empty username, too short, special characters, null"
- "This is perfect for boundary value testing"

**Real-World Example:**
- "In our login page, we test 5 credential scenarios with one DataProvider"
- "Reduced 5 duplicate test methods to 1"

---

## 💡 Key Takeaways for Member 1
1. **Assertions verify expected results** - Use descriptive messages
2. **Multiple assertion types** - assertEquals, assertTrue, expectThrows, soft assertions
3. **DataProviders eliminate duplication** - One test method, many scenarios
4. **Perfect for validation testing** - Email formats, passwords, boundary values

---

# 👥 MEMBER 2: Fixtures (Setup & Teardown) (2.5 minutes)

## 🎯 Learning Objectives
- Understand test lifecycle management
- Learn when to use each fixture type
- See the execution order in action

## 📝 Demo Script

### Opening Statement
> "Tests need proper setup and cleanup. TestNG provides fixtures that run at different points in the test lifecycle. Let me show you all levels."

### Step 1: Show Fixture Hierarchy (30 seconds)

**Draw/Show on screen:**
```
Test Lifecycle:
@BeforeSuite      ← Runs ONCE for entire suite
  @BeforeTest     ← Runs ONCE per <test> group
    @BeforeClass  ← Runs ONCE per test class
      @BeforeMethod  ← Runs BEFORE EACH test
        → Test Method 1
      @AfterMethod   ← Runs AFTER EACH test
      @BeforeMethod
        → Test Method 2
      @AfterMethod
    @AfterClass   ← Runs ONCE after all tests in class
  @AfterTest
@AfterSuite       ← Runs ONCE after entire suite
```

**What to Say:**
- "Six levels of fixtures"
- "Most common: @BeforeMethod and @AfterMethod"
- "Let's see them in action"

### Step 2: Run Fixture Demo (1.0 minute)

```bash
mvn test -Dtest=FixtureShowcaseTest
```

**While running, point to console output:**

```
========================================
@BeforeSuite: Initializing Test Suite
========================================

@BeforeTest: Setting up test group

========================================
@BeforeClass: Setting up FixtureShowcaseTest
→ Created product catalog with 3 products
========================================

► @BeforeMethod [Test #1]: Setting up test environment
  → Created fresh test user: testuser1
  → Running: testProductAvailability()
  ✓ Test passed
◄ @AfterMethod [Test #1]: Cleaning up
  → Test execution time: 15ms

► @BeforeMethod [Test #2]: Setting up test environment
  → Created fresh test user: testuser2
  → Running: testUserCreation()
```

**What to Say:**
- "See the order? Suite → Test → Class → Method"
- "Each test gets fresh data from @BeforeMethod"
- "@AfterMethod tracks execution time"

### Step 3: Explain Use Cases (1.0 minute)

#### Open FixtureShowcaseTest.java

**@BeforeSuite / @AfterSuite** (Line ~36)
```java
@BeforeSuite
public void setUpTestSuite() {
    System.out.println("→ Loading configuration...");
    System.out.println("→ Establishing shared resources...");
}
```
**When to use:**
- Database connections
- Loading config files
- Starting servers

---

**@BeforeClass / @AfterClass** (Line ~60)
```java
@BeforeClass
public void setUpTestClass() {
    productCatalog = new ArrayList<>();
    productCatalog.add(new Product("P1", "Laptop", ...));
}
```
**When to use:**
- Creating shared test data (used by all tests)
- Heavy initialization that's expensive to repeat

---

**@BeforeMethod / @AfterMethod** (Line ~83, ~153)
```java
@BeforeMethod
public void setUpTestMethod() {
    // Create FRESH user for each test
    testUser = new User("U" + testMethodCounter, ...);
}

@AfterMethod
public void tearDownTestMethod() {
    long duration = testEndTime - testStartTime;
    System.out.println("Test execution time: " + duration + "ms");
    testUser = null; // Cleanup
}
```
**When to use:**
- Test isolation - fresh data per test
- Cleanup after each test
- Performance tracking

---

### Step 4: Demonstrate Test Isolation (30 seconds)

**Show test method** (Line ~123):
```java
@Test(priority = 3)
public void testIsolationBetweenTests() {
    String originalUsername = testUser.getUsername();
    testUser.setUsername("modified_user");
    
    System.out.println("→ Modified username from '" + originalUsername + "' to '" + testUser.getUsername() + "'");
    System.out.println("→ This change won't affect other tests");
}
```

**What to Say:**
- "This test modifies the user object"
- "But next test gets a fresh user from @BeforeMethod"
- "That's test isolation - no tests affect each other"

---

## 💡 Key Takeaways for Member 2
1. **Six fixture levels** - Choose the right one for each scenario
2. **@BeforeMethod/AfterMethod most common** - Fresh data per test
3. **Test isolation** - Each test independent of others
4. **Performance tracking** - Use @AfterMethod to measure execution time

---

# 👥 MEMBER 3: Mocking & Stubbing (2.5 minutes)

## 🎯 Learning Objectives
- Understand why mocking is essential
- Learn Mockito syntax (mock, when, verify)
- See real-world scenarios without external dependencies

## 📝 Demo Script

### Opening Statement
> "Imagine testing order checkout that sends emails and charges credit cards. Every test run would spam customers and charge real money! Mocking lets us test without side effects."

### Step 1: The Problem (20 seconds)

**Draw scenario on screen:**
```
❌ Without Mocking:
OrderService → EmailService → Real SMTP Server → Customer gets 100 emails!
             → PaymentGateway → Visa API → Customer charged $100!

✅ With Mocking:
OrderService → Mock EmailService → No emails sent
             → Mock PaymentGateway → No charges
```

**What to Say:**
- "Tests would be slow, unreliable, and have real-world side effects"
- "Mocking creates fake objects that we control"

### Step 2: Basic Mocking Demo (1.5 minutes)

**Open MockingShowcaseTest.java**

#### A. Create Mock (Line ~43)
```java
EmailService mockEmail = mock(EmailService.class);
```
**What to Say:** "Creates a fake EmailService object"

#### B. Stubbing - Define Behavior (Line ~60)
```java
when(emailService.sendWelcomeEmail(any(User.class))).thenReturn(true);
```
**What to Say:** 
- "Tell mock what to return when method is called"
- "`any(User.class)` matches any User object"

#### C. Verification - Confirm Method Called (Line ~144)
```java
emailService.sendWelcomeEmail(testUser);
verify(emailService, times(1)).sendWelcomeEmail(testUser);
```
**What to Say:**
- "Verify the method was actually called"
- "Can check: never(), times(n), atLeastOnce()"

### Step 3: Run Mocking Tests (30 seconds)

```bash
mvn test -Dtest=MockingShowcaseTest#demonstrateRealWorldPaymentScenario
```

**Show this test** (Line ~315):
```java
@Test
public void demonstrateRealWorldPaymentScenario() {
    // Stub successful payment
    when(paymentGateway.processPayment(anyString(), any(BigDecimal.class)))
        .thenReturn("PAYMENT-SUCCESS-12345");
    
    // Stub successful email
    when(emailService.sendOrderConfirmation(any(User.class), any(Order.class)))
        .thenReturn(true);
    
    // Test order processing
    String paymentId = paymentGateway.processPayment("CARD-123", order.getTotalAmount());
    boolean emailSent = emailService.sendOrderConfirmation(testUser, order);
    
    // Verify both services were called
    verify(paymentGateway, times(1)).processPayment(anyString(), any(BigDecimal.class));
    verify(emailService, times(1)).sendOrderConfirmation(testUser, order);
}
```

**What to Say:**
- "✓ No actual payment charged"
- "✓ No actual email sent"
- "✓ Test runs instantly"
- "✓ Can test error scenarios easily"

### Step 4: Error Scenario (30 seconds)

**Show this test** (Line ~348):
```java
@Test
public void demonstratePaymentFailureScenario() {
    // Stub payment failure
    when(paymentGateway.processPayment(anyString(), any(BigDecimal.class)))
        .thenThrow(new RuntimeException("Card declined"));
    
    // Should throw exception
    Assert.expectThrows(RuntimeException.class, () -> {
        paymentGateway.processPayment("CARD-INVALID", order.getTotalAmount());
    });
    
    // Verify email was NOT sent
    verify(emailService, never()).sendOrderConfirmation(any(User.class), any(Order.class));
}
```

**What to Say:**
- "We can easily test failure scenarios"
- "Verify email is NOT sent when payment fails"
- "Impossible to test this reliably with real payment gateway"

### Step 5: Real Project Example (20 seconds)

**Open existing test:** `src/test/java/com/demo/auth/AuthServiceMockTest.java`

```java
@Test
public void testRegisterWithMockEmail() {
    EmailService mockEmail = mock(EmailService.class);
    when(mockEmail.sendWelcomeEmail(anyString())).thenReturn(true);
    
    AuthService service = new AuthService(repo, mockEmail);
    boolean result = service.register("john", "123");
    
    verify(mockEmail, times(1)).sendWelcomeEmail("john");
}
```

**What to Say:**
- "This is how we use mocking in our real project"
- "Tests AuthService without needing email server"

---

## 💡 Key Takeaways for Member 3
1. **Mocking isolates code from dependencies** - No side effects
2. **Three key operations:** mock() → when() → verify()
3. **Test fast and reliably** - No network, no database, no emails
4. **Test error scenarios** - Simulate failures easily

---

# 👥 MEMBER 4: CI/CD & Test Reporting (2.5 minutes)

## 🎯 Learning Objectives
- Understand automated testing in CI/CD
- Learn GitHub Actions workflow
- Explore TestNG rich HTML reports

## 📝 Demo Script

### Part A: CI/CD with GitHub Actions (1.5 minutes)

#### Opening Statement
> "Manual testing is error-prone. CI/CD runs tests automatically on every code change. Let me show you our GitHub Actions workflow."

#### Step 1: Show Workflow File (30 seconds)

**Open `.github/workflows/ci.yml`**

```yaml
name: TestNG CI workflow

on:
  push:
    branches: [ "main" ]
  pull_request:
    branches: [ "main" ]

jobs:
  test:
    runs-on: ubuntu-latest
    
    steps:
      - name: Checkout code
        uses: actions/checkout@v4
      
      - name: Setup Java 17
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '17'
      
      - name: Build and Run tests
        run: mvn -B clean test
      
      - name: Publish TestNG results to dashboard
        uses: dorny/test-reporter@v2
        with:
          path: test-output/junitreports/**/*.xml
          reporter: java-junit
      
      - name: Upload TestNG Reports
        uses: actions/upload-artifact@v6
        with:
          name: testng-test-output
          path: test-output
      
      - name: Send Email on Failure
        if: failure()
        uses: dawidd6/action-send-mail@v11
```

**What to Say:**
- "Triggers on every push or pull request"
- "Sets up Java, runs Maven tests"
- "Publishes results to GitHub Actions dashboard"
- "Uploads HTML reports as artifacts"
- "Sends email if tests fail"

#### Step 2: Show Live CI/CD (30 seconds)

**Option A - If you have GitHub repo:**
- Open browser to GitHub repository
- Click "Actions" tab
- Show recent workflow run
- Click on a run to show:
  - Test execution log
  - Test results summary
  - Downloadable artifacts

**Option B - Screenshot/Recording:**
- Show pre-recorded CI/CD run
- Point out key features

**What to Say:**
- "Every code push triggers automatic testing"
- "Developers get instant feedback"
- "Failed tests block merge to main branch"

#### Step 3: CI/CD Benefits (30 seconds)

**List on screen:**
```
✅ Benefits of CI/CD Testing:
1. Catches bugs early (before production)
2. Automatic - no manual intervention needed
3. Consistent environment (same every time)
4. Fast feedback (5-10 minutes)
5. Test history tracked
6. Email notifications on failure
7. Blocks bad code from merging
```

**What to Say:**
- "Manual testing is slow and error-prone"
- "CI/CD tests every change automatically"
- "Our workflow completes in under 2 minutes"

---

### Part B: TestNG Test Reports (1.0 minute)

#### Step 4: Generate Reports (20 seconds)

```bash
mvn clean test
```

**What to Say:**
- "TestNG generates rich HTML reports automatically"
- "No configuration needed"
- "Reports appear in test-output/ folder"

#### Step 5: Show HTML Reports (40 seconds)

**Open in browser:** `test-output/index.html`

**Walk through key sections:**

1. **Summary Dashboard**
   - Total tests: 26
   - Passed: 26 (100%)
   - Failed: 0
   - Skipped: 0
   - Success rate: 100%
   - Total time: ~3 seconds

2. **Test Hierarchy**
   - Suite: TestNGDemoSuite
   - Tests: Legacy Auth Demo, OMS Auth Tests, Order Tests, etc.
   - Classes: Expandable list of test classes
   - Methods: Individual test methods

3. **Failed Tests Section** (if any)
   - Stack traces
   - Error messages
   - Screenshots (if configured)

4. **Chronological View**
   - Timeline of test execution
   - Duration per test

5. **Charts & Graphs**
   - Pass/Fail pie chart
   - Execution time distribution

**Navigate to:** `test-output/emailable-report.html`

**What to Say:**
- "This is the email-friendly summary report"
- "Can be sent to stakeholders automatically"
- "Clean, concise results"

---

### Step 6: XML Reports for CI/CD (30 seconds)

**Show:** `test-output/junitreports/`

```
TEST-com.demo.auth.AuthServiceTest.xml
TEST-com.demo.oms.service.OrderServiceTest.xml
...
```

**Open one XML file briefly:**
```xml
<testsuite tests="4" failures="0" errors="0" time="0.523">
  <testcase classname="com.demo.auth.AuthServiceTest" 
            name="testSuccessfulLogin" time="0.145"/>
</testsuite>
```

**What to Say:**
- "Machine-readable format for CI/CD tools"
- "GitHub Actions uses these to display results"
- "Standard JUnit XML format - works with any CI tool"

---

### Step 7: Report Features Demo (30 seconds)

**Show advanced features:**

1. **Grouping** (testng.xml)
```xml
<suite name="TestNGDemoSuite">
  <test name="OMS Auth Tests">
    <classes>
      <class name="com.demo.oms.service.AuthServiceTest"/>
    </classes>
  </test>
</suite>
```
- "Tests organized by feature/module"

2. **Real-time Results**
- "Tests results appear as they execute"
- "Don't need to wait for all tests to finish"

3. **Automatic Screenshots** (can be configured)
- "TestNG can take screenshots on failure"
- "Helpful for UI tests"

---

## 💡 Key Takeaways for Member 4

### CI/CD:
1. **Automation is key** - Tests run on every code change
2. **Fast feedback** - Catches bugs in minutes, not days
3. **GitHub Actions** - Easy to set up, powerful features
4. **Email notifications** - Team stays informed

### Reporting:
1. **Rich HTML reports** - index.html for detailed analysis
2. **Email-friendly** - emailable-report.html for stakeholders
3. **XML for integration** - Works with any CI/CD tool
4. **Real-time results** - No waiting for full suite

---

# 🎬 Demo Closing (30 seconds)

## Wrap-Up Statement
> "In 10 minutes, we've shown you TestNG's power:  
> - **Assertions** validate results  
> - **Data Providers** eliminate duplication  
> - **Fixtures** manage test lifecycle  
> - **Mocking** isolates dependencies  
> - **CI/CD** automates everything  
> - **Reports** provide insights  
> 
> This project demonstrates a real online order management system with 26 tests, all passing, all automated. TestNG makes testing efficient and maintainable."

## Q&A Prep
**Anticipated Questions:**

Q: "Can TestNG run tests in parallel?"  
A: "Yes! Add `parallel="methods"` to testng.xml"

Q: "How does TestNG compare to JUnit?"  
A: "TestNG has more features: DataProviders, flexible fixtures, better grouping, built-in reporting"

Q: "Can we use TestNG for UI testing?"  
A: "Absolutely! Works great with Selenium WebDriver"

---

# 📝 Additional Resources

## Files to Review Before Demo
1. `README.md` - Project overview
2. `pom.xml` - Dependencies (TestNG 7.10.2, Mockito 5.11.0)
3. Test files in `src/test/java/com/demo/demo/`
4. CI workflow in `.github/workflows/ci.yml`

## Quick Commands Reference
```bash
# Run all tests
mvn clean test

# Run specific test class
mvn test -Dtest=AssertionShowcaseTest

# Run specific test method
mvn test -Dtest=DataProviderShowcaseTest#testUsernameValidation

# Generate reports (automatic with mvn test)
# Results in: test-output/index.html

# Skip tests
mvn clean install -DskipTests
```

## Demonstration Tips
1. **Keep terminals ready** - Pre-open 2 terminal windows
2. **Have files open** - Don't waste time navigating
3. **Practice timing** - Rehearse to stay within 2.5 min per member
4. **Backup plan** - Have screenshots if live demo fails
5. **Console output** - Make font larger for visibility
6. **Focus on concepts** - Don't get lost in code details

---

# ✅ Success Criteria

Your demo will be successful if you:
- [ ] Demonstrate all 4 required features clearly
- [ ] Stay within 10-minute time limit
- [ ] Show working code (run actual tests)
- [ ] Explain real-world benefits
- [ ] Answer 1-2 questions confidently

---

**Good luck with your demonstration! 🚀**
