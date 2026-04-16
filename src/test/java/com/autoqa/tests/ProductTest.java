package com.autoqa.tests;

import com.autoqa.dataproviders.TestDataProvider;
import com.autoqa.pages.ProductPage;
import com.autoqa.utils.RetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test Module 2 – Product Search and Browse
 */
public class ProductTest extends BaseTest {

    // ── @DataProvider from Excel (SearchData sheet) ───────────────────────────
    @DataProvider(name = "searchKeywordsExcel")
    public Object[][] searchKeywordsFromExcel() {
        return TestDataProvider.getSearchKeywordsFromExcel();
    }

    // ── TC_PROD_01 – search from Excel ────────────────────────────────────────
    @Test(dataProvider = "searchKeywordsExcel",
          description = "Search by keyword from Excel – verify results appear",
          retryAnalyzer = RetryAnalyzer.class)
    public void testProductSearch(String keyword) {
        ProductPage productPage = new ProductPage();

        productPage.navigateToProductsPage();
        productPage.searchForProduct(keyword);

        Assert.assertTrue(productPage.areSearchResultsDisplayed(),
                "No results shown for keyword: " + keyword);
        Assert.assertTrue(productPage.getSearchResultCount() > 0,
                "Result count must be > 0 for keyword: " + keyword);
    }

    // ── TC_PROD_02 – category navigation ─────────────────────────────────────
    @Test(description = "Navigate to Women > Dresses and verify products listed",
          retryAnalyzer = RetryAnalyzer.class)
    public void testCategoryNavigation() {
        ProductPage productPage = new ProductPage();

        productPage.navigateToWomenDressesCategory();

        Assert.assertTrue(productPage.areProductsListedOnPage(),
                "No products listed under Women > Dresses category");
    }

    // ── TC_PROD_03 – product detail page ─────────────────────────────────────
    @Test(description = "Open a product detail page and verify name and price shown",
          retryAnalyzer = RetryAnalyzer.class)
    public void testProductDetailPage() {
        ProductPage productPage = new ProductPage();

        productPage.navigateToProductsPage();
        productPage.openFirstProductDetail();

        String name  = productPage.getDetailProductName();
        String price = productPage.getDetailProductPrice();

        Assert.assertFalse(name.isEmpty(),  "Product name must not be empty");
        Assert.assertFalse(price.isEmpty(), "Product price must not be empty");
    }
}
