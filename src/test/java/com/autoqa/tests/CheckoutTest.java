package com.autoqa.tests;

import com.autoqa.dataproviders.JsonDataProvider;
import com.autoqa.pages.CartPage;
import com.autoqa.pages.CheckoutPage;
import com.autoqa.pages.HomePage;
import com.autoqa.pages.LoginPage;
import com.autoqa.pages.ProductPage;
import com.autoqa.utils.RetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test Module 4 – Checkout Flow
 *  TC_CHK_01 : Full checkout – cart → address → payment → order confirmation
 *  TC_CHK_02 : Checkout without login – should prompt login
 */
public class CheckoutTest extends BaseTest {

    // ── TC_CHK_01 – full checkout flow ────────────────────────────────────────
    @Test(description = "Complete checkout from cart to order confirmation",
          retryAnalyzer = RetryAnalyzer.class)
    public void testCompleteCheckout() {
        HomePage     homePage     = new HomePage();
        LoginPage    loginPage    = new LoginPage();
        ProductPage  productPage  = new ProductPage();
        CartPage     cartPage     = new CartPage();
        CheckoutPage checkoutPage = new CheckoutPage();

        // Step 1: Login
        homePage.clickSignupLoginLink();
        Object[][] data = JsonDataProvider.getLoginData();
        loginPage.login((String) data[0][0], (String) data[0][1]);
        Assert.assertTrue(homePage.isLoggedIn(),
                "Pre-condition: user must be logged in before checkout");

        // Step 2: Add product and go to cart
        productPage.navigateToProductsPage();
        productPage.addFirstProductToCart();
        productPage.clickContinueShopping();
        homePage.clickCart();

        Assert.assertTrue(cartPage.getCartItemCount() > 0,
                "Cart must have items to proceed with checkout");

        // Step 3: Proceed to checkout
        cartPage.clickProceedToCheckout();

        // Step 4: Place order (address page)
        checkoutPage.enterOrderComment("Please deliver between 9am-5pm");
        checkoutPage.clickPlaceOrder();

        // Step 5: Fill payment form
        checkoutPage.fillPaymentDetails(
                "QA Tester", "4111111111111111",
                "123", "12", "2027");
        checkoutPage.clickPayAndConfirm();

        Assert.assertTrue(checkoutPage.isOrderConfirmed(),
                "Order confirmation should display after successful payment");
    }

    // ── TC_CHK_02 – checkout without login ────────────────────────────────────
    @Test(description = "Guest checkout should prompt user to login",
          retryAnalyzer = RetryAnalyzer.class)
    public void testCheckoutWithoutLogin() {
        ProductPage  productPage  = new ProductPage();
        CartPage     cartPage     = new CartPage();
        HomePage     homePage     = new HomePage();
        CheckoutPage checkoutPage = new CheckoutPage();

        // Add product WITHOUT logging in
        productPage.navigateToProductsPage();
        productPage.addFirstProductToCart();
        productPage.clickContinueShopping();
        homePage.clickCart();

        cartPage.clickProceedToCheckout();

        // Either a modal with login link appears, or redirect to /login
        Assert.assertTrue(checkoutPage.isLoginPromptShown(),
                "Guest user should be prompted to login during checkout");
    }
}
