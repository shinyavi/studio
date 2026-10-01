package com.cnbc.selenium.debug;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.cnbc.selenium.config.TestConfig;

import io.github.bonigarcia.wdm.WebDriverManager;

/** Temporary debug — prints DOM state during login. */
public final class LoginFlowDebug {

    public static void main(String[] args) throws Exception {
        TestConfig config = TestConfig.load();
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        try {
            driver.get(config.getBaseUrl());
            Thread.sleep(2000);
            dump(driver, "after load");

            tryClick(driver, By.id("onetrust-accept-btn-handler"));
            Thread.sleep(1000);

            tryClick(driver, By.xpath(
                    "//div[contains(@class,'SignInMenu-signInMenu')]//a[contains(.,'SIGN IN')]"));

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input[name='email'], input[name='password']")));
            dump(driver, "after sign in click");

            System.out.println("iframes: " + driver.findElements(By.tagName("iframe")).size());

            List<WebElement> inputs = driver.findElements(By.tagName("input"));
            System.out.println("--- inputs (" + inputs.size() + ") ---");
            for (WebElement in : inputs) {
                if (in.isDisplayed()) {
                    System.out.printf("  type=%s name=%s id=%s placeholder=%s%n",
                            in.getAttribute("type"),
                            in.getAttribute("name"),
                            in.getAttribute("id"),
                            in.getAttribute("placeholder"));
                }
            }

            WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input[name='email']")));
            WebElement password = driver.findElement(By.cssSelector("input[name='password']"));
            email.clear();
            email.sendKeys(config.getEmail());
            password.clear();
            password.sendKeys(config.getPassword());

            WebElement modal = driver.findElement(By.cssSelector(
                    ".SignInOrSignUpModal-modalContents, .AuthForms-containerNew, .AuthForms-container"));
            WebElement signInBtn = modal.findElement(By.cssSelector(
                    ".AuthForms-submitButton, button[type='submit'], input[type='submit']"));
            System.out.println("submit tag=" + signInBtn.getTagName() + " text=" + signInBtn.getText());
            signInBtn.click();
            System.out.println("clicked modal submit");

            Thread.sleep(3000);
            driver.findElements(By.cssSelector(
                    "[class*='error' i], [class*='Error'], [role='alert'], .AuthForms-loginWaitMsg"))
                    .stream().filter(WebElement::isDisplayed)
                    .forEach(e -> System.out.println("MSG: " + e.getText()));

            for (int i = 0; i < 3; i++) {
                Thread.sleep(2000);
                dump(driver, "poll " + i);
            }

            // Try NBC Universal profile login URL directly
            driver.get("https://www.nbc.com/sign-in");
            Thread.sleep(4000);
            dump(driver, "nbc.com sign-in");
            driver.findElements(By.tagName("input")).stream().filter(WebElement::isDisplayed)
                    .forEach(in -> System.out.printf("nbc input type=%s name=%s%n",
                            in.getAttribute("type"), in.getAttribute("name")));

            System.out.println("--- header sign in area ---");
            driver.findElements(By.xpath("//div[contains(@class,'SignInMenu')]//*")).stream()
                    .filter(WebElement::isDisplayed)
                    .limit(15)
                    .forEach(e -> System.out.println("  " + e.getTagName() + ": " + e.getText().replace("\n", " ")));
        } finally {
            driver.quit();
        }
    }

    private static void tryClick(WebDriver driver, By locator) {
        List<WebElement> els = driver.findElements(locator);
        if (!els.isEmpty() && els.get(0).isDisplayed()) {
            els.get(0).click();
            System.out.println("clicked " + locator);
        }
    }

    private static void dump(WebDriver driver, String label) {
        System.out.println("=== " + label + " ===");
        System.out.println("URL: " + driver.getCurrentUrl());
        System.out.println("Title: " + driver.getTitle());
        int windows = driver.getWindowHandles().size();
        System.out.println("Windows: " + windows);
    }

    private LoginFlowDebug() {
    }
}
