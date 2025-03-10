package com.appsfactory.utils;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Getter
public class WebDriverWaiter {

    private final WebDriver driver;

    @Autowired
    public WebDriverWaiter(WebDriver driver) {
        this.driver = driver;
    }

    public WebDriverWait getWait(long timeoutInSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
    }

    public WebDriverWait getDefaultWait() {
        return new WebDriverWait(driver, Duration.ofSeconds(10)); // Default wait time
    }
}
