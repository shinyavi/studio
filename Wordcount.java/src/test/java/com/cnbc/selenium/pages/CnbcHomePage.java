package com.cnbc.selenium.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page object for https://www.cnbc.com/ header sign-in entry point.
 */
public class CnbcHomePage extends BasePage {

    private static final By SIGN_IN_LINK = By.xpath(
            "//div[contains(@class,'SignInMenu-signInMenu')]//a[contains(normalize-space(.),'SIGN IN')]");
    private static final By COOKIE_ACCEPT_BUTTON = By.id("onetrust-accept-btn-handler");
    private static final By SIGN_IN_MODAL = By.cssSelector(".SignInOrSignUpModal-modalContents");

    public CnbcHomePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public CnbcHomePage open(String baseUrl) {
        driver.get(baseUrl);
        dismissCookieBannerIfPresent();
        return this;
    }

    public SignInModalPage openSignInModal() {
        dismissCookieBannerIfPresent();
        WebElement signIn = waitForClickable(SIGN_IN_LINK);
        jsClick(signIn);
        wait.until(ExpectedConditions.visibilityOfElementLocated(SIGN_IN_MODAL));
        return new SignInModalPage(driver, wait).waitForModal();
    }

    private void dismissCookieBannerIfPresent() {
        if (isDisplayed(COOKIE_ACCEPT_BUTTON, Duration.ofSeconds(4))) {
            try {
                click(COOKIE_ACCEPT_BUTTON);
            } catch (Exception ignored) {
                // Banner may disappear between check and click.
            }
        }
    }
}
