# TestNG Demos (Auth + Online Order Management System)

Small, realistic Java 17 + Maven project(s) to demonstrate TestNG:

- Assertions
- Fixtures (`@BeforeMethod`, `@AfterMethod`)
- Mocking/Stubbing (Mockito)
- Test reports (TestNG `test-output/`)
- CI/CD (GitHub Actions workflow)

## Requirements

- Java 17+
- Maven 3.9+

## Run tests

```bash
mvn test
```

## Where to find reports

After `mvn test`:

- TestNG HTML reports: `test-output/index.html` and `test-output/emailable-report.html`
- (Optional) Surefire reports (may exist depending on runner): `target/surefire-reports/`

## Project layout

- Main code: `src/main/java/com/demo/auth/`
- OMS main code: `src/main/java/com/demo/oms/`
- Tests: `src/test/java/com/demo/auth/`
- OMS tests: `src/test/java/com/demo/oms/`
- TestNG suite: `src/test/resources/testng.xml`
- CI: `.github/workflows/ci.yml`

## Run the OMS demo flow

Run the main method in `src/main/java/com/demo/oms/DemoRunner.java` from VS Code (Java extension) to see:

- Product setup
- User registration + login
- Order creation + checkout
- Payment receipt + order summary
