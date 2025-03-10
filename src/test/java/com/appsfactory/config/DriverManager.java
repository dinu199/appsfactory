package com.appsfactory.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URL;

@Configuration
@Slf4j
public class DriverManager {

    @Value("#{'${amazon-url}'}")
    private URL amazonUrl;

    @Value("${browser:edge}") // Default to Edge if not specified
    private String browser;

    private static WebDriver driver;

    @Bean
    public WebDriver getDriver() {
        if (driver == null) {
            synchronized (DriverManager.class) {
                if (driver == null) {
                    String os = System.getProperty("os.name").toLowerCase();
                    String selectedBrowser = browser.toLowerCase();

                    log.info("OS detected: {}", os);
                    log.info("Browser selected: {}", selectedBrowser);

                    switch (selectedBrowser) {
                        case "firefox":
                            WebDriverManager.firefoxdriver().setup();
                            FirefoxOptions firefoxOptions = new FirefoxOptions();
                            driver = new FirefoxDriver(firefoxOptions);
                            break;
                        case "chrome":
                            WebDriverManager.chromedriver().setup();
                            ChromeOptions chromeOptions = new ChromeOptions();
                            driver = new ChromeDriver(chromeOptions);
                            break;
                        case "safari":
                            if (os.contains("mac")) {
                                driver = new SafariDriver();
                            } else {
                                throw new UnsupportedOperationException("Safari is only available on macOS.");
                            }
                            break;
                        case "edge":
                        default:
                            WebDriverManager.edgedriver().setup();
                            EdgeOptions edgeOptions = new EdgeOptions();
                            edgeOptions.addArguments("--start-maximized");
                            edgeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
                            driver = new EdgeDriver(edgeOptions);
                            break;
                    }
                    log.info("Browser initialized: {}", selectedBrowser);
                    driver.navigate().to(amazonUrl);
                    log.info("Navigated to: {}", amazonUrl);
                }
            }
        }
        return driver;
    }
}
