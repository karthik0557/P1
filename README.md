# AutoQA Pro – E-Commerce Test Automation Framework

## Tech Stack
| Tool | Version |
|------|---------|
| Java | 11+ |
| Selenium WebDriver | 4.18.1 |
| TestNG | 7.9.0 |
| WebDriverManager | 5.7.0 |
| ExtentReports | 5.1.1 |
| Maven | 3.8+ |

---

## Project Structure

```
AutoQAPro/
├── pom.xml
├── testng.xml
├── screenshots/                        ← Auto-created on test failure
├── test-output/
│   └── ExtentReport.html               ← Auto-generated after run
└── src/
    ├── main/java/com/autoqa/
    │   ├── config/
    │   │   └── ConfigReader.java        ← Reads config.properties
    │   ├── pages/
    │   │   ├── BasePage.java            ← Shared WebDriverWait utilities
    │   │   ├── HomePage.java
    │   │   ├── LoginPage.java
    │   │   ├── RegisterPage.java
    │   │   ├── ProductPage.java
    │   │   ├── CartPage.java
    │   │   └── CheckoutPage.java
    │   ├── utils/
    │   │   ├── DriverManager.java       ← ThreadLocal WebDriver
    │   │   ├── ScreenshotUtil.java
    │   │   ├── ExtentReportManager.java
    │   │   └── RetryAnalyzer.java
    │   └── listeners/
    │       └── TestListener.java        ← ITestListener
    └── test/
        ├── java/com/autoqa/
        │   ├── dataproviders/
        │   │   └── JsonDataProvider.java
        │   └── tests/
        │       ├── BaseTest.java        ← @BeforeMethod / @AfterMethod
        │       ├── AuthTest.java        ← Module 1
        │       ├── ProductTest.java     ← Module 2
        │       ├── CartTest.java        ← Module 3
        │       ├── CheckoutTest.java    ← Module 4
        │       └── FormValidationTest.java ← Module 5
        └── resources/
            ├── config.properties
            └── loginData.json
```

---

## Eclipse Setup (Step by Step)

### 1. Import the project
1. Open Eclipse → **File → Import → Maven → Existing Maven Projects**
2. Browse to the `AutoQAPro` folder → Click **Finish**
3. Eclipse will auto-download all dependencies (takes ~1–2 min)

### 2. Install TestNG plugin (if not already installed)
1. **Help → Eclipse Marketplace**
2. Search for **TestNG** → Install **TestNG for Eclipse**
3. Restart Eclipse

### 3. Update config.properties
Edit `src/test/resources/config.properties`:
```properties
browser=chrome          # chrome or firefox
base.url=https://automationexercise.com
timeout=15
headless=false          # set true for headless mode
```

### 4. Add valid login credentials to loginData.json
Edit `src/test/resources/loginData.json`:
- Replace `testuser@example.com` / `Test@1234` with a real registered account on automationexercise.com
- Register one manually at https://automationexercise.com → Signup/Login

### 5. Run the suite

**Option A – Maven (required by hackathon):**
```bash
mvn test
```

**Option B – Eclipse (right-click):**
- Right-click `testng.xml` → **Run As → TestNG Suite**

---

## Key Design Decisions

| Rule | How it's handled |
|------|-----------------|
| No `Thread.sleep()` | All waits use `WebDriverWait` / `FluentWait` |
| No hardcoded URLs | Everything read from `config.properties` via `ConfigReader` |
| No WebDriver in test methods | `DriverManager` + `BaseTest` handle lifecycle |
| POM enforced | All locators/actions in Page classes only |
| Screenshot on failure | `TestListener` captures + attaches to ExtentReport |
| Parallel safe | `ThreadLocal<WebDriver>` in `DriverManager` |
| Data-driven | `loginData.json` read by `JsonDataProvider` |
| Retry on failure | `RetryAnalyzer` retries once |

---

## After Running
- **HTML Report:** `test-output/ExtentReport.html` – open in any browser
- **Screenshots:** `screenshots/` – named as `TestName_yyyyMMdd_HHmmss.png`
