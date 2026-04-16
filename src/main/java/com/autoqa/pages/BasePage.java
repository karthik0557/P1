package com.autoqa.pages;

import com.autoqa.config.ConfigReader;
import com.autoqa.utils.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * BasePage provides shared WebDriverWait and FluentWait utilities.
 * All Page classes extend this; no WebDriver code goes into test classes.
 *
 * IMPORTANT: automationexercise.com shows ad popups/iframes randomly.
 * dismissAds() must be called before any click that might be blocked.
 */
public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    // All known ad overlay / popup selectors on automationexercise.com
    private static final By[] AD_CLOSE_SELECTORS = {
        By.cssSelector("div[id^='ad_'] a[onclick*='close'], div[id^='ad_'] button"),
        By.cssSelector("button.close, .modal-header button.close"),
        By.cssSelector("a[onclick='javascript:void(0)'][style*='position']"),
        By.xpath("//ins[contains(@class,'adsbygoogle')]//following::div[contains(@id,'aswift')]")
    };

    public BasePage() {
        this.driver = DriverManager.getDriver();
        int timeout = ConfigReader.getInstance().getTimeout();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
    }

    /**
     * Dismiss any ad popup / overlay that automationexercise.com injects.
     * Uses JS to remove ad iframes and clicks any visible close button.
     * Safe to call even when no ads are present.
     */
    protected void dismissAds() {
        try {
            // 1. Remove all Google ad iframes via JS (fastest + most reliable)
            ((JavascriptExecutor) driver).executeScript(
                "document.querySelectorAll('iframe[id^=\"aswift\"], " +
                "iframe[id^=\"google_ads\"], div[id^=\"google_vignette\"]')" +
                ".forEach(el => el.remove());"
            );
        } catch (Exception ignored) {}

        try {
            // 2. Click any visible modal close button
            List<WebElement> closeButtons = driver.findElements(
                    By.cssSelector("button.close, .modal-header .close, " +
                                   "a[onclick*='void(0)'].close, #dismiss-button"));
            for (WebElement btn : closeButtons) {
                if (btn.isDisplayed()) {
                    btn.click();
                    break;
                }
            }
        } catch (Exception ignored) {}
    }

    /**
     * Dismiss ads, then wait for element to be clickable and click it.
     * Use this instead of waitAndClick() for any cart/nav/checkout action.
     */
    protected void safeClick(By locator) {
        dismissAds();
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        } catch (Exception e) {
            // Fallback: JS click if normal click still intercepted
            WebElement el = driver.findElement(locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    /** Wait until element is visible, then return it */
    protected WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /** Wait until element is clickable, then click */
    protected void waitAndClick(By locator) {
        dismissAds();
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    /** Wait until element is visible, then type */
    protected void waitAndType(By locator, String text) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        el.clear();
        el.sendKeys(text);
    }

    /** FluentWait variant for slow-loading elements (polls every 500 ms) */
    protected WebElement fluentWaitForElement(By locator) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(ConfigReader.getInstance().getTimeout()))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(org.openqa.selenium.NoSuchElementException.class);
        return fluentWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /** Scroll element into view using JS, then click */
    protected void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    /** JS click by locator directly */
    protected void jsClick(By locator) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    /** Scroll to element then click – handles elements below the fold */
    protected void scrollAndClick(By locator) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", el);
        dismissAds();
        el.click();
    }

    /** Return the current page title */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /** Return the current URL */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
