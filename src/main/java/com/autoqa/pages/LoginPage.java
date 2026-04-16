package com.autoqa.pages;

import org.openqa.selenium.By;

/**
 * LoginPage – handles both Login and Signup sections on /login.
 */
public class LoginPage extends BasePage {

    // ── Login locators ───────────────────────────────────────────────────────
    private final By loginEmailInput    = By.cssSelector("form[action='/login'] input[data-qa='login-email']");
    private final By loginPasswordInput = By.cssSelector("form[action='/login'] input[data-qa='login-password']");
    private final By loginButton        = By.cssSelector("button[data-qa='login-button']");
    private final By loginErrorMsg      = By.cssSelector("form[action='/login'] p");

    // ── Signup locators ──────────────────────────────────────────────────────
    private final By signupNameInput    = By.cssSelector("input[data-qa='signup-name']");
    private final By signupEmailInput   = By.cssSelector("input[data-qa='signup-email']");
    private final By signupButton       = By.cssSelector("button[data-qa='signup-button']");
    private final By signupErrorMsg     = By.cssSelector("form[action='/signup'] p");

    // ── Login actions ────────────────────────────────────────────────────────

    public void enterLoginEmail(String email) {
        waitAndType(loginEmailInput, email);
    }

    public void enterLoginPassword(String password) {
        waitAndType(loginPasswordInput, password);
    }

    public void clickLoginButton() {
        waitAndClick(loginButton);
    }

    /** Convenience method: fill and submit the login form */
    public void login(String email, String password) {
        enterLoginEmail(email);
        enterLoginPassword(password);
        clickLoginButton();
    }

    public String getLoginErrorMessage() {
        return waitForElement(loginErrorMsg).getText();
    }

    // ── Signup actions ───────────────────────────────────────────────────────

    public void enterSignupName(String name) {
        waitAndType(signupNameInput, name);
    }

    public void enterSignupEmail(String email) {
        waitAndType(signupEmailInput, email);
    }

    public void clickSignupButton() {
        waitAndClick(signupButton);
    }

    public String getSignupErrorMessage() {
        return waitForElement(signupErrorMsg).getText();
    }
}
