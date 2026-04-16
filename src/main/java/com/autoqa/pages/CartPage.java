package com.autoqa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * CartPage – view cart, verify items, remove items, proceed to checkout.
 * Uses safeClick() + jsClick() to handle ad interceptions on /view_cart.
 */
public class CartPage extends BasePage {

    // ── Locators ─────────────────────────────────────────────────────────────
    private final By cartRows        = By.cssSelector("#cart_info_table tbody tr");
    private final By productNames    = By.cssSelector(".cart_description h4 a");
    private final By productPrices   = By.cssSelector(".cart_price p");
    private final By deleteButtons   = By.cssSelector(".cart_quantity_delete");
    private final By emptyCartMsg    = By.id("empty_cart");
    private final By proceedCheckout = By.cssSelector("a.btn.btn-default.check_out");

    // ── Actions ───────────────────────────────────────────────────────────────

    public boolean isProductInCart(String productName) {
        List<WebElement> names = driver.findElements(productNames);
        for (WebElement el : names) {
            if (el.getText().equalsIgnoreCase(productName)) return true;
        }
        return false;
    }

    public String getFirstProductName() {
        return fluentWaitForElement(By.cssSelector(".cart_description h4 a")).getText();
    }

    public String getFirstProductPrice() {
        return waitForElement(productPrices).getText();
    }

    public int getCartItemCount() {
        // Wait for cart table to be present before counting rows
        try {
            waitForElement(By.cssSelector("#cart_info_table"));
            return driver.findElements(cartRows).size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void removeFirstProduct() {
        dismissAds();
        List<WebElement> deleteBtns = driver.findElements(deleteButtons);
        if (!deleteBtns.isEmpty()) {
            // JS click avoids ElementClickInterceptedException from ads
            jsClick(deleteBtns.get(0));
        }
    }

    public boolean isCartEmpty() {
        try {
            return fluentWaitForElement(emptyCartMsg).isDisplayed();
        } catch (Exception e) {
            // Also check if no rows remain
            return driver.findElements(cartRows).isEmpty();
        }
    }

    public void clickProceedToCheckout() {
        dismissAds();
        scrollAndClick(proceedCheckout);
    }
}
