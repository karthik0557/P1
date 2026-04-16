package com.autoqa.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.autoqa.config.ConfigReader;

/**
 * Manages a single ExtentReports instance shared across the test suite.
 * Uses ThreadLocal<ExtentTest> for parallel-safe test logging.
 */
public class ExtentReportManager {

    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> extentTestThreadLocal = new ThreadLocal<>();

    private ExtentReportManager() {}

    public static synchronized ExtentReports getExtentReports() {
        if (extentReports == null) {
            String reportPath = ConfigReader.getInstance().getReportPath();
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setDocumentTitle("AutoQA Pro – Test Report");
            sparkReporter.config().setReportName("E-Commerce Automation Suite");

            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);
            extentReports.setSystemInfo("Application", "automationexercise.com");
            extentReports.setSystemInfo("Framework", "Selenium + TestNG + POM");
        }
        return extentReports;
    }

    public static ExtentTest getExtentTest() {
        return extentTestThreadLocal.get();
    }

    public static void setExtentTest(ExtentTest test) {
        extentTestThreadLocal.set(test);
    }

    public static void flushReports() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }
}
