package com.cnbc.selenium.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * CNBC sign-in modal (SignInOrSignUpModal / AuthForms).
 */
public class SignInModalPage extends BasePage {

    private static final By MODAL = By.cssSelector(".SignInOrSignUpModal-modalContents");
    private static final By EMAIL = By.cssSelector("input[name='email']");
    private static final By PASSWORD = By.cssSelector("input[name='password']");
    private static final By SUBMIT = By.cssSelector(".AuthForms-submitButton");
    private static final By LOADING_MESSAGE = By.cssSelector(".AuthForms-loginWaitMsg");
    private static final By ERROR_MESSAGE = By.cssSelector(
            "[class*='error' i], [role='alert'], .AuthForms-loginWaitMsg");

    private static final By EMAIL_IN_MODAL =
            By.cssSelector(".SignInOrSignUpModal-modalContents input[name='email']");
    private static final By PASSWORD_IN_MODAL =
            By.cssSelector(".SignInOrSignUpModal-modalContents input[name='password']");
    private static final By SUBMIT_IN_MODAL =
            By.cssSelector(".SignInOrSignUpModal-modalContents .AuthForms-submitButton");

    private static final Duration FORM_READY_TIMEOUT = Duration.ofSeconds(15);

    public SignInModalPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public SignInModalPage waitForModal() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(MODAL));
        waitForModalFormReady();
        return this;
    }

    /** Waits for the sign-in form inside the modal to finish rendering. */
    private void waitForModalFormReady() {
        WebDriverWait formWait = new WebDriverWait(driver, FORM_READY_TIMEOUT);
        formWait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(MODAL, EMAIL));
        formWait.until(ExpectedConditions.elementToBeClickable(EMAIL_IN_MODAL));
        formWait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(MODAL, PASSWORD));
        formWait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(MODAL, SUBMIT));
        waitForLoadingToFinish(formWait);
    }

    private void waitForLoadingToFinish(WebDriverWait formWait) {
        try {
            formWait.until(ExpectedConditions.invisibilityOfElementLocated(LOADING_MESSAGE));
        } catch (TimeoutException ignored) {
            // Spinner may never appear on a fast load.
        }
    }

    public SignInModalPage signIn(String email, String password) {
        waitForModal();

        WebDriverWait stepWait = new WebDriverWait(driver, FORM_READY_TIMEOUT);
        stepWait.until(ExpectedConditions.elementToBeClickable(EMAIL_IN_MODAL));
        fillField(EMAIL, email);

        stepWait.until(ExpectedConditions.elementToBeClickable(PASSWORD_IN_MODAL));
        fillField(PASSWORD, password);

        stepWait.until(ExpectedConditions.elementToBeClickable(SUBMIT_IN_MODAL));
        clickSubmit();

        waitForLoginResponse();
        waitForModalToClose();
        return this;
    }

    private void fillField(By field, String value) {
        typeWithRetry(MODAL, field, value, 5);
        WebElement input = waitForVisible(MODAL).findElement(field);
        String actual = input.getAttribute("value");
        if (actual == null || !actual.equals(value)) {
            setValueWithJs(input, value);
        }
    }

    private void clickSubmit() {
        for (int i = 0; i < 3; i++) {
            try {
                WebElement submit = waitForClickable(SUBMIT_IN_MODAL);
                jsClick(submit);
                return;
            } catch (Exception ignored) {
                // retry after re-render
            }
        }
        waitForClickable(SUBMIT_IN_MODAL).click();
    }

    private void waitForLoginResponse() {
        WebDriverWait responseWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            responseWait.until(ExpectedConditions.or(
                    ExpectedConditions.invisibilityOfElementLocated(MODAL),
                    ExpectedConditions.visibilityOfElementLocated(ERROR_MESSAGE)));
        } catch (TimeoutException ignored) {
            // Modal may stay open without a visible error.
        }
    }

    private void waitForModalToClose() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(MODAL));
        } catch (TimeoutException ignored) {
            // May remain open on invalid credentials.
        }
    }

    public boolean isSignedIn() {
        return isSignedIn(Duration.ofSeconds(45));
    }

    public boolean isSignedIn(Duration timeout) {
        WebDriverWait signInWait = new WebDriverWait(driver, timeout);
        try {
            return signInWait.until(this::isAuthenticated);
        } catch (TimeoutException e) {
            String error = readVisibleError();
            if (!error.isBlank()) {
                throw new AssertionError("CNBC sign-in failed: " + error, e);
            }
            throw new AssertionError(
                    "CNBC sign-in did not complete. Check credentials or complete MFA manually.", e);
        }
    }

    private boolean isAuthenticated(WebDriver driver) {
        if (!driver.getCurrentUrl().toLowerCase().contains("cnbc.com")) {
            return false;
        }

        return !driver.findElements(By.xpath(
                "//div[contains(@class,'SignInMenu')]"
                        + "//a[contains(.,'SIGN OUT') or contains(.,'Sign Out') or contains(.,'My Account')]"))
                .isEmpty();
    }

    private String readVisibleError() {
        try {
            return driver.findElement(MODAL).findElements(ERROR_MESSAGE).stream()
                    .filter(WebElement::isDisplayed)
                    .map(WebElement::getText)
                    .filter(t -> !t.isBlank())
                    .findFirst()
                    .orElse("");
        } catch (Exception e) {
            return "";
        }
    }
}
