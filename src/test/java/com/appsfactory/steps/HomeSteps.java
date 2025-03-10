package com.appsfactory.steps;

import com.appsfactory.helpers.ScenarioContext;
import com.appsfactory.pom.controls.LocationPopup;
import com.appsfactory.pom.pages.HomePage;
import com.appsfactory.utils.WebDriverWaiter;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static com.appsfactory.helpers.AssertionWrapper.assertThatPageIsOpened;
import static com.appsfactory.utils.WaitUtils.waitFor;

public class HomeSteps {

    @Autowired
    private HomePage homePage;

    @Autowired
    private LocationPopup locationPopup;

    @Autowired
    private ScenarioContext scenarioContext;

    @Autowired
    private WebDriverWaiter webDriverWaiter;

    @Given("home page is displayed")
    public void homePageIsDisplayed() {
        assertThatPageIsOpened(homePage);
    }

    @Given("user ensures he is using the correct zip code")
    public void userEnsuresHeIsUsingTheCorrectZipCode(List<Map<String, String>> dataTable) {
        for (Map<String, String> map : dataTable) {
            String zipCode = map.get("zip code");
            homePage.getHeader().openLocationPopup();
            webDriverWaiter.getWait(10).until(ExpectedConditions
                    .visibilityOf(homePage.getHeader().getLocationContainer()));
            locationPopup.enterZipCodeAndApply(zipCode);
            locationPopup.clickDone();
        }
    }

    @When("user searches for a product")
    public void userSearchesForSkittles(List<Map<String, String>> dataTable) {
        for (Map<String, String> map : dataTable) {
            String item = map.get("item");
            webDriverWaiter.getWait(10).until(ExpectedConditions
                    .visibilityOf(homePage.getHeader().getSearchBox()));
            waitFor(3);
            homePage.getHeader().searchForProduct(item);
            scenarioContext.setValue("lastSearchedProduct", item);
        }
    }
}