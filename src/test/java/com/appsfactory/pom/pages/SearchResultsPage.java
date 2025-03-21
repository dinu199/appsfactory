package com.appsfactory.pom.pages;

import com.appsfactory.utils.WaitUtils;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;


@Component
@Slf4j
public class SearchResultsPage extends BasePage implements Page {

    @FindBy(css = ".s-main-slot div[data-component-type='s-search-result']")
    private List<WebElement> productContainers;

    @FindBy(css = ".s-pagination-next")
    private WebElement nextPageButton;

    public void findCheapestProduct(String productKeyword) {
        waitForProductsToLoad();
        refreshProductContainers();

        double minPrice = Double.MAX_VALUE;
        WebElement cheapestProductElement = null;
        String cheapestProductUrl = driver.getCurrentUrl();
        String cheapestProductASIN = "";
        int cheapestPage = 1;
        int currentPage = 1;

        while (true) {
            log.info("Searching for the cheapest '{}' on page {}", productKeyword, currentPage);
            List<WebElement> productContainers = driver.findElements(By.cssSelector(".s-main-slot div[data-component-type='s-search-result']"));

            for (WebElement product : productContainers) {
                try {
                    String asin = product.getAttribute("data-asin");

                    List<WebElement> titleElements = product.findElements(By.cssSelector("h2.s-line-clamp-1 span.a-size-base-plus.a-color-base"));
                    String titleText = titleElements.isEmpty() ? "" : titleElements.get(0).getText().trim();

                    List<WebElement> spanElements = product.findElements(By.cssSelector("a.a-link-normal.s-line-clamp-3.s-link-style.a-text-normal h2 span"));
                    String spanText = spanElements.isEmpty() ? "" : spanElements.get(0).getText().trim();

                    if (!titleText.toLowerCase().contains(productKeyword.toLowerCase()) &&
                            !spanText.toLowerCase().contains(productKeyword.toLowerCase())) {
                        log.info("Skipping product: '{}' (Does not match '{}')", titleText.isEmpty() ? spanText : titleText, productKeyword);
                        continue;
                    }

                    List<WebElement> priceElements = product.findElements(By.cssSelector(".a-price:not(.a-text-price) > .a-offscreen"));
                    List<WebElement> addToCartButtons = product.findElements(By.cssSelector("button[name='submit.addToCart']"));

                    if (!priceElements.isEmpty() && !addToCartButtons.isEmpty()) {
                        String priceText = priceElements.get(0).getAttribute("textContent").replace("$", "").replace(",", "").trim();
                        double price = Double.parseDouble(priceText);

                        if (price < minPrice) {
                            minPrice = price;
                            cheapestProductElement = product;
                            cheapestProductUrl = driver.getCurrentUrl();
                            cheapestProductASIN = asin;
                            cheapestPage = currentPage;
                            log.info("Found a new cheapest product at ${} on page {}.", price, currentPage);
                        }
                    }
                } catch (NoSuchElementException | NumberFormatException e) {
                    log.warn("Skipping product due to missing price or invalid elements: {}", e.getMessage());
                }
            }

            if (!goToNextPage()) {
                break;
            }
            currentPage++;
        }

        if (cheapestProductElement != null) {
            log.info("Cheapest product: ${}, found on page {}. Navigating back to that page...", minPrice, cheapestPage);
            driver.get(cheapestProductUrl);
            waitForProductsToLoad();

            try {
                WebElement reloadedCheapestProduct = driver.findElement(By.cssSelector("div[data-asin='" + cheapestProductASIN + "']"));
                WebElement finalAddToCartButton = reloadedCheapestProduct.findElement(By.cssSelector("button[name='submit.addToCart']"));

                log.info("Clicking 'Add to Cart' for the cheapest product (${})", minPrice);
                finalAddToCartButton.click();
            } catch (TimeoutException | NoSuchElementException e) {
                log.warn("'Add to Cart' button not found for the cheapest product.");
            }
        } else {
            log.warn("No valid '{}' product found.", productKeyword);
        }
    }

    private void waitForProductsToLoad() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(
                    ".s-main-slot div[data-component-type='s-search-result']")));
            log.info("Products are fully loaded.");
        } catch (TimeoutException e) {
            log.warn("Timeout waiting for products to load: {}", e.getMessage());
        }
    }

    private void refreshProductContainers() {
        productContainers = driver.findElements(By.cssSelector(".s-main-slot div[data-component-type='s-search-result']"));
    }

    public boolean goToNextPage() {
        try {
            WebElement nextPageButton = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".s-pagination-next")));

            if (!nextPageButton.isDisplayed() || nextPageButton.getAttribute("aria-disabled") != null) {
                log.info("No more pages available.");
                return false;
            }

            log.info("Navigating to the next page..");
            nextPageButton.click();

            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.stalenessOf(nextPageButton));

            return true;
        } catch (NoSuchElementException | TimeoutException e) {
            log.info("No more pages available.");
            return false;
        }
    }

    @Override
    public boolean isAt() {
        return WaitUtils.awaitForElements(
                getHeader().getLogoImage(),
                getHeader().getLocationContainer(),
                getHeader().getNavigationCart(),
                getHeader().getSearchBox(),
                getHeader().getSearchSubmitButton()
        );
    }
}