package com.autoqa.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.autoqa.utils.ExtentReportManager;
import com.autoqa.utils.ScreenshotUtil;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * ITestListener that:
 *  - Creates an ExtentTest entry for every test
 *  - Captures a screenshot and attaches it to the report on failure
 *  - Flushes the report after the suite finishes
 */
public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        // Initialise the report once when the suite starts
        ExtentReportManager.getExtentReports();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentTest test = ExtentReportManager.getExtentReports()
                .createTest(testName, result.getMethod().getDescription());
        ExtentReportManager.setExtentTest(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentReportManager.getExtentTest().log(Status.PASS, "Test PASSED");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String screenshotPath = ScreenshotUtil.captureScreenshot(testName);

        ExtentTest test = ExtentReportManager.getExtentTest();
        test.log(Status.FAIL, "Test FAILED: " + result.getThrowable().getMessage());
        test.fail(
            MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build()
        );
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentReportManager.getExtentTest().log(Status.SKIP, "Test SKIPPED");
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.flushReports();
    }
}
