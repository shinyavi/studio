package com.cnbc.selenium.debug;

import java.time.Duration;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.cnbc.selenium.config.TestConfig;

import io.github.bonigarcia.wdm.WebDriverManager;

public final class LoginFlowDebug2 {

    public static void main(String[] args) throws Exception {
        TestConfig config = TestConfig.load();
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        try {
            driver.get("https://www.cnbc.com/");
            wait.until(ExpectedConditions.elementToBeClickable(By.id("onetrust-accept-btn-handler"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                    "//div[contains(@class,'SignInMenu-signInMenu')]//a[contains(.,'SIGN IN')]"))).click();

            WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(
                    ".SignInOrSignUpModal-modalContents")));

            modal.findElement(By.cssSelector("input[name='email']")).sendKeys(config.getEmail());
            modal.findElement(By.cssSelector("input[name='password']")).sendKeys(config.getPassword());
            modal.findElement(By.cssSelector(".AuthForms-submitButton")).click();

            Thread.sleep(5000);
            System.out.println("URL: " + driver.getCurrentUrl());
            System.out.println("Modal visible: " + !driver.findElements(By.cssSelector(
                    ".SignInOrSignUpModal-modalContents")).isEmpty());
            System.out.println("Modal text: " + modal.getText());

            printAuthCookies(driver);

            // SSO direct
            driver.get("https://ssologin.nbcuni.com/login/login.jsp");
            Thread.sleep(3000);
            System.out.println("SSO URL: " + driver.getCurrentUrl());
            for (WebElement in : driver.findElements(By.cssSelector("input"))) {
                if (in.isDisplayed()) {
                    System.out.printf("SSO input name=%s type=%s%n", in.getAttribute("name"), in.getAttribute("type"));
                }
            }
        } finally {
            driver.quit();
        }
    }

    private static void printAuthCookies(WebDriver driver) {
        Set<Cookie> cookies = driver.manage().getCookies();
        System.out.println("--- cookies (auth-related) ---");
        for (Cookie c : cookies) {
            String n = c.getName().toLowerCase();
            if (n.contains("auth") || n.contains("session") || n.contains("user")
                    || n.contains("token") || n.contains("profile") || n.contains("login")) {
                System.out.println("  " + c.getName() + "=" + c.getValue().substring(0, Math.min(40, c.getValue().length())));
            }
        }
    }

    private LoginFlowDebug2() {
    }
}
