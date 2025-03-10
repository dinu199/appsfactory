package com.appsfactory.pom.controls;

import com.appsfactory.utils.WaitUtils;
import com.appsfactory.utils.WebDriverWaiter;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Component
public class LocationPopup {

    private final WebDriver driver;

    @Autowired
    private WebDriverWaiter webDriverWaiter;

    @FindBy(css = "div.a-popover-wrapper.GLUX_Popover")
    private WebElement locationPopUp;

    @FindBy(css = "#GLUXZipUpdateInput")
    private WebElement zipCodeField;

    @FindBy(css = "button[name='glowDoneButton']")
    private WebElement doneButton;

    @FindBy(css = "div.a-popover-wrapper.GLUX_Popover input[type='submit'][aria-labelledby='GLUXZipUpdate-announce']")
    private WebElement applyButton;

    @FindBy(css = "button[data-action='a-popover-close']")
    private WebElement closeButton;

    public LocationPopup(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isPopupDisplayed() {
        webDriverWaiter.getWait(10).until(ExpectedConditions.visibilityOf(locationPopUp));
        return locationPopUp.isDisplayed();
    }

    public void enterZipCodeAndApply(String zipCode) {
        if (isPopupDisplayed()) {
            WaitUtils.awaitForElements(zipCodeField);
            zipCodeField.clear();
            zipCodeField.sendKeys(zipCode);
            WaitUtils.awaitForElements(applyButton);
            applyButton.click();
        }
    }

    public void clickDone() {
        if (isPopupDisplayed()) {
            doneButton.click();
        }
    }

    public void closePopup() {
        if (isPopupDisplayed()) {
            closeButton.click();
        }
    }
}