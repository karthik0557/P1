package com.autoqa.pages;

import org.openqa.selenium.By;

/**
 * HomePage – locators and actions for the main landing page.
 */
public class HomePage extends BasePage {

    // ── Locators ────────────────────────────────────────────────────────────
    private final By navSignup    = By.cssSelector("a[href='/login']");
    private final By navLogout    = By.cssSelector("a[href='/logout']");
    private final By navCart      = By.cssSelector("a[href='/view_cart']");
    private final By homeSlider   = By.id("slider");
    private final By loggedInText = By.cssSelector("li a b");

    // ── Actions ──────────────────────────────────────────────────────────────

    public void clickSignupLoginLink() {
        safeClick(navSignup);
    }

    public void clickLogout() {
        dismissAds();
        scrollAndClick(navLogout);
    }

    public void clickCart() {
        safeClick(navCart);
    }

    public boolean isHomePageLoaded() {
        try {
            return driver.getCurrentUrl().contains("automationexercise")
                    && driver.getTitle().toLowerCase().contains("automation");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLoggedIn() {
        try {
            return waitForElement(loggedInText).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getLoggedInUsername() {
        return waitForElement(loggedInText).getText();
    }
}
