package com.autoqa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

/**
 * ProductPage – search, browse by category, product detail, add to cart.
 * All click actions use safeClick() / jsClick() to survive ad popup interceptions.
 */
public class ProductPage extends BasePage {

    // ── Locators ─────────────────────────────────────────────────────────────
    private final By navProducts          = By.cssSelector("a[href='/products']");
    private final By searchInput          = By.id("search_product");
    private final By searchButton         = By.id("submit_search");
    private final By searchedProductCards = By.cssSelector(".productinfo");
    private final By productItems         = By.cssSelector(".single-products");
    private final By allViewProductLinks  = By.cssSelector("a[href*='product_details']");
    private final By detailProductName    = By.cssSelector(".product-information h2");
    private final By detailProductPrice   = By.cssSelector(".product-information span span");
    private final By continueShoppingBtn  = By.cssSelector(
            "button.close-modal, .modal-footer button, #cartModal button");
    private final By womenCategoryHeader  = By.xpath("//a[contains(@href,'#Women')]");
    private final By dressesCategoryLink  = By.xpath(
            "//div[@id='Women']//a[contains(@href,'category_products')]");

    // ── Actions ───────────────────────────────────────────────────────────────

    public void navigateToProductsPage() {
        safeClick(navProducts);
    }

    public void searchForProduct(String keyword) {
        waitAndType(searchInput, keyword);
        safeClick(searchButton);
    }

    public boolean areSearchResultsDisplayed() {
        return !driver.findElements(searchedProductCards).isEmpty();
    }

    public int getSearchResultCount() {
        return driver.findElements(productItems).size();
    }

    public void openFirstProductDetail() {
        dismissAds();
        List<WebElement> links = driver.findElements(allViewProductLinks);
        if (!links.isEmpty()) {
            jsClick(links.get(0));
        }
    }

    public String getDetailProductName() {
        return waitForElement(detailProductName).getText();
    }

    public String getDetailProductPrice() {
        return waitForElement(detailProductPrice).getText();
    }

    public void addFirstProductToCart() {
        dismissAds();
        // Hover to reveal overlay button, then JS click to avoid interception
        List<WebElement> products = driver.findElements(
                By.cssSelector(".single-products .productinfo"));
        if (!products.isEmpty()) {
            new Actions(driver).moveToElement(products.get(0)).perform();
        }
        jsClick(By.cssSelector(".productinfo a.add-to-cart, .product-overlay .add-to-cart"));
    }

    public void clickContinueShopping() {
        try {
            waitForElement(By.cssSelector("#cartModal, .modal.in, .modal.show"));
            safeClick(continueShoppingBtn);
        } catch (Exception e) {
            // Modal may have auto-closed – proceed silently
        }
    }

    public void navigateToWomenDressesCategory() {
        safeClick(womenCategoryHeader);
        safeClick(dressesCategoryLink);
    }

    public boolean areProductsListedOnPage() {
        return !driver.findElements(productItems).isEmpty();
    }
}
