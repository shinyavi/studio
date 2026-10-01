package com.cnbc.selenium.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public final class ScreenshotHelper {

    private static final Path SCREENSHOT_DIR = Path.of("target", "screenshots");

    private ScreenshotHelper() {
    }

    public static Path capture(WebDriver driver, String name) {
        if (!(driver instanceof TakesScreenshot screenshotDriver)) {
            throw new IllegalStateException("WebDriver does not support screenshots");
        }

        try {
            Files.createDirectories(SCREENSHOT_DIR);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
            Path file = SCREENSHOT_DIR.resolve(name + "-" + timestamp + ".png");
            byte[] png = screenshotDriver.getScreenshotAs(OutputType.BYTES);
            Files.write(file, png);
            return file.toAbsolutePath();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to save screenshot", e);
        }
    }
}
