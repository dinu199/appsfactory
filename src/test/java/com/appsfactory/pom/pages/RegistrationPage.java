package com.appsfactory.pom.pages;

import com.appsfactory.utils.WaitUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

@Component
public class RegistrationPage extends BasePage implements Page{

    @FindBy(css = "[class='a-icon a-icon-logo']")
    private WebElement imageLogo;

    @FindBy(css = "[class='a-box-inner']")
    private WebElement registrationForm;

    @FindBy(xpath = "//h1[contains(text(), 'Create account')]")
    private WebElement createAccountLabel;

    @FindBy(xpath = "//input[@id='ap_customer_name']")
    private WebElement nameField;

    @FindBy(xpath = "//input[@id='ap_email']")
    private WebElement phoneField;

    @FindBy(xpath = "//input[@id='ap_password']")
    private WebElement passwordField;

    @FindBy(xpath = "//input[@id='ap_password_check']")
    private WebElement reEnterPasswordField;

    @FindBy(xpath = "//input[@id='continue']")
    private WebElement continueButton;

    @Override
    public boolean isAt() {
        return WaitUtils.awaitForElements(imageLogo,
                registrationForm,
                createAccountLabel,
                nameField,
                phoneField,
                passwordField,
                reEnterPasswordField,
                continueButton);
    }
}