package com.appsfactory.steps;

import com.appsfactory.pom.pages.CheckoutPage;
import com.appsfactory.pom.pages.RegistrationPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;

import static com.appsfactory.helpers.AssertionWrapper.*;

public class CheckoutSteps {

    @Autowired
    private CheckoutPage checkoutPage;

    @Autowired
    private RegistrationPage registrationPage;

    @Then("user should see his final price calculated correctly")
    public void userShouldSeeHisFinalPriceCalculatedCorrectly() {
        assertThatPageIsOpened(checkoutPage);
        assertElementsAreEqual(checkoutPage.calculateTotalPrice(), checkoutPage.getSubTotals());
    }

    @And("if user is not signed in redirect him to registration page")
    public void ifUserIsNotSignedInRedirectHimToRegistrationPage() {
        checkoutPage.getHeader().redirectToRegistrationPageIfNotSignedIn();
        assertThatPageIsOpened(registrationPage);
    }
}