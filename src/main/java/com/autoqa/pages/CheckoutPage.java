package com.autoqa.pages;

import org.openqa.selenium.By;

/**
 * CheckoutPage – address review, comment, payment, order confirmation.
 * Uses safeClick() for all buttons to handle ad interceptions.
 */
public class CheckoutPage extends BasePage {

    // ── Locators ─────────────────────────────────────────────────────────────
    private final By orderCommentArea  = By.name("message");
    private final By placeOrderBtn     = By.cssSelector("a.btn.btn-default.check_out");
    private final By cardNameInput     = By.cssSelector("input[data-qa='name-on-card']");
    private final By cardNumberInput   = By.cssSelector("input[data-qa='card-number']");
    private final By cvcInput          = By.cssSelector("input[data-qa='cvc']");
    private final By expiryMonthInput  = By.cssSelector("input[data-qa='expiry-month']");
    private final By expiryYearInput   = By.cssSelector("input[data-qa='expiry-year']");
    private final By payAndConfirmBtn  = By.cssSelector("button[data-qa='pay-button']");
    private final By orderConfirmMsg   = By.cssSelector(
            "#success_message b, p[style*='color:green'], h2[data-qa='order-placed']");

    // Guest checkout – modal login link OR redirect
    private final By loginLinkInModal  = By.cssSelector(
            "#checkoutModal a[href='/login'], .modal-body a[href='/login']");

    // ── Actions ───────────────────────────────────────────────────────────────

    public void enterOrderComment(String comment) {
        waitAndType(orderCommentArea, comment);
    }

    public void clickPlaceOrder() {
        dismissAds();
        scrollAndClick(placeOrderBtn);
    }

    public void fillPaymentDetails(String cardName, String cardNumber,
                                   String cvc, String expiryMonth, String expiryYear) {
        waitAndType(cardNameInput,    cardName);
        waitAndType(cardNumberInput,  cardNumber);
        waitAndType(cvcInput,         cvc);
        waitAndType(expiryMonthInput, expiryMonth);
        waitAndType(expiryYearInput,  expiryYear);
    }

    public void clickPayAndConfirm() {
        safeClick(payAndConfirmBtn);
    }

    public boolean isOrderConfirmed() {
        try {
            return waitForElement(orderConfirmMsg).isDisplayed();
        } catch (Exception e) {
            // Also accept URL-based confirmation
            return driver.getCurrentUrl().contains("order_placed")
                    || driver.getCurrentUrl().contains("payment_done");
        }
    }

    /**
     * For guest checkout test – the site shows a modal with a login link
     * OR redirects to /login directly.
     */
    public boolean isLoginPromptShown() {
        try {
            return waitForElement(loginLinkInModal).isDisplayed();
        } catch (Exception e) {
            return driver.getCurrentUrl().contains("login");
        }
    }
}
