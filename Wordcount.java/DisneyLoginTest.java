
import java.time.Duration;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DisneyLoginTest {

    public static void main(String[] args) {

        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Open Disney login page
        driver.get("https://login.disney.com/");

        // Generate random email
        String randomEmail = generateRandomEmail();
        System.out.println("Generated Email: " + randomEmail);

        // Wait for page to load
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Dump page title and all iframes for diagnosis
        try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println("Page title: " + driver.getTitle());
        System.out.println("Current URL: " + driver.getCurrentUrl());

        // Check iframes
        List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
        System.out.println("Iframes found: " + iframes.size());
        for (int i = 0; i < iframes.size(); i++) {
            System.out.println("  iframe[" + i + "] src=" + iframes.get(i).getAttribute("src"));
        }

        // Try all inputs in main page
        List<WebElement> inputs = driver.findElements(By.tagName("input"));
        System.out.println("Inputs on main page: " + inputs.size());
        for (WebElement inp : inputs) {
            System.out.println("  input type=" + inp.getAttribute("type")
                + " name=" + inp.getAttribute("name")
                + " id=" + inp.getAttribute("id")
                + " placeholder=" + inp.getAttribute("placeholder"));
        }

        // Switch into each iframe and look for inputs
        for (int i = 0; i < iframes.size(); i++) {
            try {
                driver.switchTo().frame(i);
                List<WebElement> frameInputs = driver.findElements(By.tagName("input"));
                System.out.println("Inputs in iframe[" + i + "]: " + frameInputs.size());
                for (WebElement inp : frameInputs) {
                    System.out.println("  input type=" + inp.getAttribute("type")
                        + " name=" + inp.getAttribute("name")
                        + " id=" + inp.getAttribute("id")
                        + " placeholder=" + inp.getAttribute("placeholder"));
                }
                driver.switchTo().defaultContent();
            } catch (Exception e) {
                System.out.println("Could not switch to iframe[" + i + "]: " + e.getMessage());
                driver.switchTo().defaultContent();
            }
        }

        // Attempt to find and fill email field using common locators (main + iframes)
        WebElement emailField = null;
        String[] locators = {
            "input[type='email']",
            "input[name='email']",
            "input[name='emailAddress']",
            "input[id='email']",
            "input[id='emailAddress']",
            "input[placeholder*='mail' i]",
            "input[autocomplete='email']"
        };

        // Try main page first
        for (String css : locators) {
            try {
                List<WebElement> found = driver.findElements(By.cssSelector(css));
                if (!found.isEmpty() && found.get(0).isDisplayed()) {
                    emailField = found.get(0);
                    System.out.println("Found email field on main page using: " + css);
                    break;
                }
            } catch (Exception ignored) {}
        }

        // Try each iframe
        if (emailField == null) {
            for (int i = 0; i < iframes.size(); i++) {
                try {
                    driver.switchTo().frame(i);
                    for (String css : locators) {
                        try {
                            List<WebElement> found = driver.findElements(By.cssSelector(css));
                            if (!found.isEmpty() && found.get(0).isDisplayed()) {
                                emailField = found.get(0);
                                System.out.println("Found email field in iframe[" + i + "] using: " + css);
                                break;
                            }
                        } catch (Exception ignored) {}
                    }
                    if (emailField != null) break;
                    driver.switchTo().defaultContent();
                } catch (Exception e) {
                    driver.switchTo().defaultContent();
                }
            }
        }

        if (emailField == null) {
            System.out.println("Could not find email field. See diagnostics above.");
            driver.quit();
            return;
        }

        emailField.sendKeys(randomEmail);

        // Click Continue button
        WebElement continueBtn = driver.findElement(By.xpath("//button[@type='submit']"));
        continueBtn.click();

        // Optional wait to observe behavior
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.quit();
    }

    // Method to generate random email
    public static String generateRandomEmail() {
        String chars = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder username = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            username.append(chars.charAt(random.nextInt(chars.length())));
        }

        return username.toString() + "@testmail.com";
    }
}