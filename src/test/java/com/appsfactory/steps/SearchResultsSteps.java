package com.appsfactory.steps;

import com.appsfactory.helpers.ScenarioContext;
import com.appsfactory.pom.pages.SearchResultsPage;
import com.appsfactory.utils.WebDriverWaiter;
import io.cucumber.java.en.And;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Autowired;

import static com.appsfactory.helpers.AssertionWrapper.assertThatPageIsOpened;

public class SearchResultsSteps {

    @Autowired
    private SearchResultsPage searchResultsPage;

    @Autowired
    private ScenarioContext scenarioContext;

    @Autowired
    private WebDriverWaiter webDriverWaiter;

    @And("user adds the cheapest one in the cart")
    public void userAddsTheCheapestOneInTheCart() {
        assertThatPageIsOpened(searchResultsPage);
        var item = scenarioContext.getValue("lastSearchedProduct");
        searchResultsPage.findCheapestProduct(item);
    }

    @And("user goes to checkout page")
    public void userGoesToCheckoutPage() {
        webDriverWaiter.getWait(10).until(ExpectedConditions
                .visibilityOf(searchResultsPage.getHeader().getNavigationCart()));
        searchResultsPage.getHeader().goToShoppingCart();
    }
}