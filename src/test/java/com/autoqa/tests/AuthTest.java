package com.autoqa.tests;

import com.autoqa.dataproviders.TestDataProvider;
import com.autoqa.pages.HomePage;
import com.autoqa.pages.LoginPage;
import com.autoqa.pages.RegisterPage;
import com.autoqa.utils.RetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.UUID;

/**
 * Test Module 1 – User Authentication
 */
public class AuthTest extends BaseTest {

    // ── @DataProvider reading from Excel file (Good-to-Have requirement) ──────
    @DataProvider(name = "loginDataExcel", parallel = false)
    public Object[][] loginDataFromExcel() {
        return TestDataProvider.getLoginDataFromExcel();
    }

    // ── @DataProvider reading from JSON file ──────────────────────────────────
    @DataProvider(name = "loginDataJson", parallel = false)
    public Object[][] loginDataFromJson() {
        return TestDataProvider.getLoginDataFromJson();
    }

    // ── TC_AUTH_01 & TC_AUTH_02 – data-driven from EXCEL ─────────────────────
    @Test(dataProvider = "loginDataExcel",
          description = "Data-driven login using Excel – valid and invalid credentials",
          retryAnalyzer = RetryAnalyzer.class)
    public void testLoginFromExcel(String email, String password,
                                   String expectedResult, String description) {
        HomePage  homePage  = new HomePage();
        LoginPage loginPage = new LoginPage();

        Assert.assertTrue(homePage.isHomePageLoaded(),
                "Home page did not load. Case: " + description);

        homePage.clickSignupLoginLink();
        loginPage.login(email, password);

        if ("pass".equalsIgnoreCase(expectedResult)) {
            Assert.assertTrue(homePage.isLoggedIn(),
                    "Expected successful login. Case: " + description);
        } else {
            String error = loginPage.getLoginErrorMessage();
            Assert.assertFalse(error.isEmpty(),
                    "Expected error message for invalid login. Case: " + description);
        }
    }

    // ── TC_AUTH_03 – Logout ───────────────────────────────────────────────────
    @Test(description = "Verify logout redirects user back to home page",
          retryAnalyzer = RetryAnalyzer.class)
    public void testLogout() {
        HomePage  homePage  = new HomePage();
        LoginPage loginPage = new LoginPage();

        homePage.clickSignupLoginLink();
        Object[][] data = TestDataProvider.getLoginDataFromExcel();
        loginPage.login((String) data[0][0], (String) data[0][1]);

        Assert.assertTrue(homePage.isLoggedIn(),
                "Pre-condition: user must be logged in");

        homePage.clickLogout();

        Assert.assertTrue(homePage.isHomePageLoaded(),
                "Home page should load after logout");
        Assert.assertFalse(homePage.isLoggedIn(),
                "Logged-in indicator must disappear after logout");
    }

    // ── TC_AUTH_04 – Registration ─────────────────────────────────────────────
    @Test(description = "Verify successful registration of a new unique user",
          retryAnalyzer = RetryAnalyzer.class)
    public void testNewUserRegistration() {
        HomePage     homePage     = new HomePage();
        LoginPage    loginPage    = new LoginPage();
        RegisterPage registerPage = new RegisterPage();

        homePage.clickSignupLoginLink();

        String uniqueEmail = "testuser_"
                + UUID.randomUUID().toString().substring(0, 8) + "@qatest.com";

        loginPage.enterSignupName("QA TestUser");
        loginPage.enterSignupEmail(uniqueEmail);
        loginPage.clickSignupButton();

        registerPage.fillRegistrationForm(
                "Test@1234", "QA", "TestUser",
                "123 Main Street", "California",
                "Los Angeles", "90001", "9876543210");
        registerPage.clickCreateAccount();

        Assert.assertTrue(registerPage.isAccountCreated(),
                "Account Created message should display after registration");
    }

    // ── TC_AUTH_05 – Empty name validation ────────────────────────────────────
    @Test(description = "Submit signup with empty name – should stay on login page")
    public void testRegistrationEmptyNameValidation() {
        HomePage  homePage  = new HomePage();
        LoginPage loginPage = new LoginPage();

        homePage.clickSignupLoginLink();
        loginPage.enterSignupName("");
        loginPage.enterSignupEmail("valid@email.com");
        loginPage.clickSignupButton();

        Assert.assertTrue(loginPage.getCurrentUrl().contains("login"),
                "Should remain on login page when name is empty");
    }
}
