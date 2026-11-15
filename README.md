# Java BDD Automation Framework — Selenium + Cucumber + TestNG + Maven + Jenkins

A starter end-to-end UI automation framework using Java 17, Selenium, Cucumber Gherkin, TestNG, Maven, Page Object Model, scenario hooks, screenshots on failure, and Jenkins Declarative Pipeline.

## 1. Prerequisites

- JDK 17
- Maven 3.8+
- Chrome or Firefox installed
- Jenkins with Pipeline, Git, HTML Publisher, and JUnit plugins
- Jenkins agent must have the selected browser installed. Selenium Manager resolves the matching driver automatically on a machine with browser/network access.

## 2. Import and run locally

1. Extract the ZIP.
2. Import the folder into IntelliJ IDEA/Eclipse as an existing Maven project.
3. Confirm Java 17 is selected.
4. Run all scenarios:

   ```bash
   mvn clean test
   ```

5. Run only smoke scenarios:

   ```bash
   mvn clean test -Dcucumber.filter.tags="@smoke"
   ```

6. Run headless:

   ```bash
   mvn clean test -Dheadless=true
   ```

7. Choose browser:

   ```bash
   mvn clean test -Dbrowser=firefox -Dheadless=true
   ```

8. Override application URL:

   ```bash
   mvn clean test -DbaseUrl=https://www.saucedemo.com/
   ```

## 3. Framework flow

`TestRunner` (TestNG) -> Cucumber feature -> Hooks (`@Before`) -> DriverFactory -> Step Definitions -> Page Object -> Selenium WebDriver.

After each scenario, `@After` captures a screenshot if the scenario failed and closes the browser. The driver uses `ThreadLocal` to support safe per-thread ownership if parallel execution is enabled later. Parallel execution is deliberately disabled in this starter until test data and reporting are made parallel-safe.

## 4. Project structure

```text
src/test/java/com/ctm/
  driver/       DriverFactory, ConfigReader
  hooks/        Cucumber lifecycle hooks
  pages/        Page Object classes
  runner/       TestNG Cucumber runner
  steps/        Gherkin step definitions
src/test/resources/
  features/     Gherkin feature files
  config.properties
Jenkinsfile
pom.xml
```

## 5. Add a new scenario

Add a scenario to `src/test/resources/features/login.feature`, then implement any new Gherkin step in `LoginSteps.java`. Keep browser locators and page interactions inside page classes; keep business-readable workflow assertions in step definitions.

Example:

```gherkin
@smoke
Scenario: Verify invalid login
  Given the user opens the SauceDemo login page
  When the user logs in with username "bad_user" and password "bad_password"
  Then the login error should contain "Username and password do not match"
```

## 6. Jenkins setup

### Install/configure Jenkins tools

In **Manage Jenkins -> Tools**, configure:
- JDK installation name: `JDK17`
- Maven installation name: `Maven3`

Install plugins:
- Pipeline
- Git
- JUnit
- HTML Publisher

The Jenkins agent must have Chrome (or Firefox) installed. For Linux agents, install the browser and its system dependencies before running the job. Keep `HEADLESS=true` for a non-GUI agent. Selenium Manager downloads the matching driver; allow outbound access to the Selenium Manager endpoints or pre-provision the driver.

### Create the pipeline job

1. Push this project to a Git repository.
2. In Jenkins, select **New Item -> Pipeline**.
3. Under Pipeline, select **Pipeline script from SCM**.
4. Select Git and enter the repository URL and credentials if private.
5. Set Script Path to `Jenkinsfile`.
6. Save and click **Build with Parameters**.
7. Select browser, headless mode, and Cucumber tag expression.

Pipeline stages: Checkout -> Environment -> Clean and Test. The `post` section publishes Cucumber's JUnit XML to Jenkins and archives HTML/JSON/XML reports.

## 7. Important implementation notes

- Do not commit real credentials. Use Jenkins Credentials and environment variables for authenticated applications.
- Replace SauceDemo URL, locators, and test data with your AUT details.
- Avoid mixing implicit and explicit waits; this framework uses explicit waits.
- Do not add sleeps. Wait for a meaningful condition.
- Keep assertions in steps or dedicated assertion helpers, not in page object methods.
- The included ExtentReports dependency is optional and not wired into the starter; Cucumber HTML/JSON/JUnit outputs are already configured.
- This is a starter framework, not a production-ready parallel/distributed framework. Add stable test data management, environment profiles, retry policy (only for known transient failures), secrets handling, and Grid/cloud execution when required.

## 8. Troubleshooting

- `NoSuchDriverException`: verify browser is installed and Selenium Manager can access the network, or preconfigure the driver.
- Browser fails on Linux: use headless mode and install browser OS dependencies.
- No tests found: confirm `TestRunner.java` name and Maven Surefire includes.
- Tag expression issue: use valid Cucumber expressions such as `@smoke`, `@smoke or @negative`, or `not @wip`.
- Jenkins HTML report missing: verify HTML Publisher plugin is installed and the Cucumber HTML file exists at `target/cucumber-reports/cucumber.html`.
