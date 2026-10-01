package com.cnbc.selenium.debug;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.cnbc.selenium.config.TestConfig;
import com.cnbc.selenium.pages.SignInModalPage;

import io.github.bonigarcia.wdm.WebDriverManager;

public final class LoginFlowDebug3 {

    public static void main(String[] args) throws Exception {
        TestConfig config = TestConfig.load();
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver(new ChromeOptions().addArguments("--start-maximized"));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        try {
            driver.get(config.getBaseUrl());
            wait.until(ExpectedConditions.elementToBeClickable(By.id("onetrust-accept-btn-handler"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                    "//div[contains(@class,'SignInMenu-signInMenu')]//a[contains(.,'SIGN IN')]"))).click();

            new SignInModalPage(driver, wait).waitForModal().signIn(config.getEmail(), config.getPassword());

            Thread.sleep(3000);
            System.out.println("URL: " + driver.getCurrentUrl());
            var modals = driver.findElements(By.cssSelector(".SignInOrSignUpModal-modalContents"));
            if (!modals.isEmpty()) {
                System.out.println("MODAL TEXT:\n" + modals.get(0).getText());
            }
            driver.manage().getCookies().forEach(c ->
                    System.out.println("cookie: " + c.getName()));
            Thread.sleep(10000);
        } finally {
            driver.quit();
        }
    }

    private LoginFlowDebug3() {
    }
}
