package com.appsfactory.pom.blocks;

import com.appsfactory.utils.WebDriverWaiter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static com.appsfactory.utils.WaitUtils.*;

@Getter
@Component
@Slf4j
public class Header {

    private final WebDriver driver;

    @Autowired
    private WebDriverWaiter webDriverWaiter;

    @FindBy(css = "[id='nav-logo']")
    private WebElement logoImage;

    @FindBy(css = "[id='nav-global-location-popover-link']")
    private WebElement locationContainer;

    @FindBy(css = "[id='twotabsearchtextbox']")
    private WebElement searchBox;

    @FindBy(css = "input[id='nav-search-submit-button']")
    private WebElement searchSubmitButton;

    @FindBy(css = "[id='nav-link-accountList']")
    private WebElement accountContainer;

    @FindBy(linkText = "Start here.")
    private WebElement startHereLink;

    @FindBy(css = "[id='nav-cart']")
    private WebElement navigationCart;

    public Header(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void searchForProduct(String item) {
        WebElement searchBoxRefreshed = driver.findElement(By.cssSelector("[id='twotabsearchtextbox']"));
        searchBoxRefreshed.clear();
        searchBoxRefreshed.sendKeys(item);
        webDriverWaiter.getWait(10).until(ExpectedConditions.elementToBeClickable(navigationCart));
        searchBox.sendKeys(Keys.ENTER);
    }

    public void openLocationPopup() {
        awaitForElements(locationContainer);
        locationContainer.click();
    }
    public void goToShoppingCart() {
        webDriverWaiter.getWait(10).until(ExpectedConditions.elementToBeClickable(navigationCart));
        waitFor(2);
        navigationCart.click();
    }

    public void redirectToRegistrationPageIfNotSignedIn() {
        if (!hasValidSession() || !isUserLoggedIn() || isSignInVisible()) {
            log.info("User is not logged in. Redirecting to registration page...");

            Actions actions = new Actions(driver);
            actions.moveToElement(accountContainer).perform();

            WebElement startHere = webDriverWaiter.getWait(10)
                    .until(ExpectedConditions.elementToBeClickable(startHereLink));

            startHere.click();
        }
    }

    private boolean hasValidSession() {
        return !driver.manage().getCookies().isEmpty();
    }

    private boolean isUserLoggedIn() {
        return driver.findElements(By.id("nav-link-accountList")).isEmpty();
    }

    private boolean isSignInVisible() {
        return !driver.findElements(By.cssSelector("#nav-signin-tooltip")).isEmpty();
    }
}
