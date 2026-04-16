package com.autoqa.utils;

import com.autoqa.config.ConfigReader;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Captures and saves a screenshot; returns the absolute file path. */
public class ScreenshotUtil {

    private ScreenshotUtil() {}

    public static String captureScreenshot(String testName) {
        WebDriver driver = DriverManager.getDriver();
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String folderPath = ConfigReader.getInstance().getScreenshotsPath();
        String filePath = folderPath + testName + "_" + timestamp + ".png";

        try {
            Files.createDirectories(Paths.get(folderPath));
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(src.toPath(), Paths.get(filePath));
        } catch (IOException e) {
            System.err.println("Screenshot capture failed: " + e.getMessage());
        }
        return Paths.get(filePath).toAbsolutePath().toString();
    }
}
