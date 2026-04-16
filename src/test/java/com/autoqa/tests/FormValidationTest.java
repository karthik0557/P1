package com.autoqa.tests;

import com.autoqa.pages.HomePage;
import com.autoqa.pages.LoginPage;
import com.autoqa.utils.RetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test Module 5 – Form Validations
 *  TC_FORM_01 : Submit registration form with empty fields
 *  TC_FORM_02 : Enter invalid email format – validation triggers
 *  TC_FORM_03 : Login with empty credentials – error shown
 */
public class FormValidationTest extends BaseTest {

    @DataProvider(name = "invalidEmails")
    public Object[][] invalidEmails() {
        return new Object[][] {
                { "notanemail"       },
                { "missing@"        },
                { "@nodomain.com"   },
                { "spaces in@email.com" }
        };
    }

    // ── TC_FORM_01 – empty signup fields ─────────────────────────────────────
    @Test(description = "Submit signup form with all empty fields – remain on page",
          retryAnalyzer = RetryAnalyzer.class)
    public void testEmptySignupFormSubmission() {
        HomePage  homePage  = new HomePage();
        LoginPage loginPage = new LoginPage();

        homePage.clickSignupLoginLink();
        // Click signup without entering anything
        loginPage.clickSignupButton();

        // HTML5 validation keeps user on /login page
        Assert.assertTrue(loginPage.getCurrentUrl().contains("login"),
                "Should stay on login/signup page when fields are empty");
    }

    // ── TC_FORM_02 – invalid email format (data-driven) ───────────────────────
    @Test(dataProvider = "invalidEmails",
          description = "Invalid email format should trigger validation",
          retryAnalyzer = RetryAnalyzer.class)
    public void testInvalidEmailFormatOnSignup(String invalidEmail) {
        HomePage  homePage  = new HomePage();
        LoginPage loginPage = new LoginPage();

        homePage.clickSignupLoginLink();
        loginPage.enterSignupName("Test User");
        loginPage.enterSignupEmail(invalidEmail);
        loginPage.clickSignupButton();

        // Browser HTML5 validation or server error – must not proceed past /login
        Assert.assertTrue(loginPage.getCurrentUrl().contains("login"),
                "Should not proceed with invalid email: " + invalidEmail);
    }

    // ── TC_FORM_03 – empty login credentials ─────────────────────────────────
    @Test(description = "Submit login form with empty email and password",
          retryAnalyzer = RetryAnalyzer.class)
    public void testEmptyLoginFormSubmission() {
        HomePage  homePage  = new HomePage();
        LoginPage loginPage = new LoginPage();

        homePage.clickSignupLoginLink();
        loginPage.enterLoginEmail("");
        loginPage.enterLoginPassword("");
        loginPage.clickLoginButton();

        // Should stay on /login (HTML5 required validation)
        Assert.assertTrue(loginPage.getCurrentUrl().contains("login"),
                "Should remain on login page when both fields are empty");
    }

    // ── TC_FORM_04 – invalid login credentials ────────────────────────────────
    @Test(description = "Login with wrong credentials shows error message",
          retryAnalyzer = RetryAnalyzer.class)
    public void testInvalidLoginShowsError() {
        HomePage  homePage  = new HomePage();
        LoginPage loginPage = new LoginPage();

        homePage.clickSignupLoginLink();
        loginPage.login("invalid_user@fake.com", "WrongPass999");

        String errorMsg = loginPage.getLoginErrorMessage();
        Assert.assertFalse(errorMsg.isEmpty(),
                "An error message must be shown for invalid login credentials");
        Assert.assertTrue(errorMsg.toLowerCase().contains("incorrect"),
                "Error message should indicate incorrect email/password");
    }
}
