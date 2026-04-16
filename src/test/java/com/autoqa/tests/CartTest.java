package com.autoqa.tests;

import com.autoqa.pages.CartPage;
import com.autoqa.pages.HomePage;
import com.autoqa.pages.ProductPage;
import com.autoqa.utils.RetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test Module 3 – Shopping Cart
 *  TC_CART_01 : Add a product to cart – verify name and price appear
 *  TC_CART_02 : Remove a product – verify cart becomes empty
 *  TC_CART_03 : Cart count updates after adding a product
 */
public class CartTest extends BaseTest {

    // ── TC_CART_01 ────────────────────────────────────────────────────────────
    @Test(description = "Add a product to cart and verify name and price appear",
          retryAnalyzer = RetryAnalyzer.class)
    public void testAddProductToCart() {
        ProductPage productPage = new ProductPage();
        CartPage    cartPage    = new CartPage();
        HomePage    homePage    = new HomePage();

        productPage.navigateToProductsPage();
        productPage.addFirstProductToCart();
        productPage.clickContinueShopping();
        homePage.clickCart();

        String name  = cartPage.getFirstProductName();
        String price = cartPage.getFirstProductPrice();

        Assert.assertFalse(name.isEmpty(),  "Cart product name should not be empty");
        Assert.assertFalse(price.isEmpty(), "Cart product price should not be empty");
    }

    // ── TC_CART_02 ────────────────────────────────────────────────────────────
    @Test(description = "Remove a product from cart and verify cart is empty",
          retryAnalyzer = RetryAnalyzer.class)
    public void testRemoveProductFromCart() {
        ProductPage productPage = new ProductPage();
        CartPage    cartPage    = new CartPage();
        HomePage    homePage    = new HomePage();

        productPage.navigateToProductsPage();
        productPage.addFirstProductToCart();
        productPage.clickContinueShopping();
        homePage.clickCart();

        int before = cartPage.getCartItemCount();
        Assert.assertTrue(before > 0, "Pre-condition: cart must have at least 1 item");

        cartPage.removeFirstProduct();

        Assert.assertTrue(cartPage.isCartEmpty(),
                "Cart should be empty after removing the only product");
    }

    // ── TC_CART_03 ────────────────────────────────────────────────────────────
    @Test(description = "Cart item count should be > 0 after adding a product",
          retryAnalyzer = RetryAnalyzer.class)
    public void testCartCountUpdates() {
        ProductPage productPage = new ProductPage();
        CartPage    cartPage    = new CartPage();
        HomePage    homePage    = new HomePage();

        productPage.navigateToProductsPage();
        productPage.addFirstProductToCart();
        productPage.clickContinueShopping();
        homePage.clickCart();

        int count = cartPage.getCartItemCount();
        Assert.assertTrue(count > 0,
                "Cart item count must be > 0 after adding a product");
    }
}
