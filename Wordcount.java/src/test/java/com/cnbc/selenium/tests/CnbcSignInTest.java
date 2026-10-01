package com.cnbc.selenium.tests;

import java.nio.file.Path;

import com.cnbc.selenium.config.DriverFactory;
import com.cnbc.selenium.config.TestConfig;
import com.cnbc.selenium.pages.CnbcHomePage;
import com.cnbc.selenium.pages.SignInModalPage;
import com.cnbc.selenium.util.ScreenshotHelper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * End-to-end sign-in test for https://www.cnbc.com/
 */
class CnbcSignInTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private TestConfig config;
    private Path failureScreenshot;

    @BeforeEach
    void setUp() {
        config = TestConfig.load();
        driver = DriverFactory.createChromeDriver();
        wait = DriverFactory.createWait(driver);
        failureScreenshot = null;
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void signInWithCredentials() {
        try {
            CnbcHomePage homePage = new CnbcHomePage(driver, wait);
            SignInModalPage signInModal = homePage
                    .open(config.getBaseUrl())
                    .openSignInModal()
                    .signIn(config.getEmail(), config.getPassword());

            if (!signInModal.isSignedIn()) {
                failureScreenshot = ScreenshotHelper.capture(driver, "cnbc-sign-in-error");
                Assertions.fail("Expected user to be signed in on CNBC. Screenshot: " + failureScreenshot);
            }
        } catch (AssertionError e) {
            if (failureScreenshot == null && driver != null) {
                failureScreenshot = ScreenshotHelper.capture(driver, "cnbc-sign-in-error");
            }
            throw new AssertionError(
                    e.getMessage() + (failureScreenshot != null ? " Screenshot: " + failureScreenshot : ""),
                    e);
        }
    }
}
