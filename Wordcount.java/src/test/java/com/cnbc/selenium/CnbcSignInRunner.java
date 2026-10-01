package com.cnbc.selenium;

import com.cnbc.selenium.config.DriverFactory;
import com.cnbc.selenium.config.TestConfig;
import com.cnbc.selenium.pages.CnbcHomePage;
import com.cnbc.selenium.pages.SignInModalPage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Runnable entry point (no JUnit required). Run main from your IDE.
 */
public final class CnbcSignInRunner {

    public static void main(String[] args) {
        TestConfig config = TestConfig.load();
        WebDriver driver = DriverFactory.createChromeDriver();
        WebDriverWait wait = DriverFactory.createWait(driver);

        try {
            SignInModalPage signInModal = new CnbcHomePage(driver, wait)
                    .open(config.getBaseUrl())
                    .openSignInModal()
                    .signIn(config.getEmail(), config.getPassword());

            boolean signedIn = signInModal.isSignedIn();
            System.out.println(signedIn ? "Sign-in succeeded." : "Sign-in may have failed — verify in browser.");

            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            driver.quit();
        }
    }

    private CnbcSignInRunner() {
    }
}
