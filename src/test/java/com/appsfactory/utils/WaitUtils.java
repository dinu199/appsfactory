package com.appsfactory.utils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Slf4j
public class WaitUtils {

    private static final long DEFAULT_TIMEOUT = 10;

    private WaitUtils() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    public static boolean awaitForElements(long timeoutInSeconds, WebElement... elements) {
        if (elements == null || elements.length == 0) {
            log.warn("No elements provided to wait for.");
            return false;
        }

        try {
            await().ignoreExceptions()
                    .atMost(timeoutInSeconds, TimeUnit.SECONDS)
                    .until(() -> {
                        boolean allDisplayed = true;
                        for (WebElement element : elements) {
                            boolean isDisplayed = element.isDisplayed();
                            if (!isDisplayed) {
                                log.warn("Element {} is not displayed yet.", element);
                            }
                            allDisplayed = allDisplayed && isDisplayed;
                        }
                        return allDisplayed;
                    });
            return true;
        } catch (Exception e) {
            log.error("Timeout occurred while waiting for elements to be displayed: {}", e.getMessage());
            return false;
        }
    }

    public static boolean awaitForElements(WebElement... elements) {
        return awaitForElements(DEFAULT_TIMEOUT, elements);
    }

    public static boolean awaitForElements(WebDriver driver, By locator, long timeoutInSeconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds))
                    .until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
            return true;
        } catch (Exception e) {
            System.err.println("Timeout occurred while waiting for elements: " + e.getMessage());
            return false;
        }
    }

    public static void waitFor(long seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Thread interrupted during wait: {}", e.getMessage());
        }
    }
}