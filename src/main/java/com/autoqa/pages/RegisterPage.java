package com.autoqa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

/**
 * RegisterPage – form that appears after clicking Signup on the login page.
 */
public class RegisterPage extends BasePage {

    // ── Locators ─────────────────────────────────────────────────────────────
    private final By titleMr           = By.id("id_gender1");
    private final By passwordField     = By.id("password");
    private final By dayDropdown       = By.id("days");
    private final By monthDropdown     = By.id("months");
    private final By yearDropdown      = By.id("years");
    private final By newsletterChk     = By.id("newsletter");
    private final By firstNameField    = By.id("first_name");
    private final By lastNameField     = By.id("last_name");
    private final By addressField      = By.id("address1");
    private final By countryDropdown   = By.id("country");
    private final By stateField        = By.id("state");
    private final By cityField         = By.id("city");
    private final By zipCodeField      = By.id("zipcode");
    private final By mobileField       = By.id("mobile_number");
    private final By createAccountBtn  = By.cssSelector("button[data-qa='create-account']");
    private final By accountCreatedMsg = By.cssSelector("h2[data-qa='account-created']");

    // ── Actions ───────────────────────────────────────────────────────────────

    public void fillRegistrationForm(String password, String firstName, String lastName,
                                     String address, String state, String city,
                                     String zipCode, String mobile) {
        waitAndClick(titleMr);
        waitAndType(passwordField, password);
        new Select(waitForElement(dayDropdown)).selectByValue("10");
        new Select(waitForElement(monthDropdown)).selectByValue("5");
        new Select(waitForElement(yearDropdown)).selectByValue("1995");
        waitAndClick(newsletterChk);
        waitAndType(firstNameField, firstName);
        waitAndType(lastNameField, lastName);
        waitAndType(addressField, address);
        new Select(waitForElement(countryDropdown)).selectByVisibleText("United States");
        waitAndType(stateField, state);
        waitAndType(cityField, city);
        waitAndType(zipCodeField, zipCode);
        waitAndType(mobileField, mobile);
    }

    public void clickCreateAccount() {
        waitAndClick(createAccountBtn);
    }

    public boolean isAccountCreated() {
        return waitForElement(accountCreatedMsg).isDisplayed();
    }
}
