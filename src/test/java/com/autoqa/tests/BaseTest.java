package com.autoqa.tests;

import com.autoqa.config.ConfigReader;
import com.autoqa.utils.DriverManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * BaseTest handles browser lifecycle.
 * Every test class extends this — no WebDriver code in any test method.
 */
public class BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverManager.initDriver();
        DriverManager.getDriver().get(ConfigReader.getInstance().getBaseUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
