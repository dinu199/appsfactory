package com.appsfactory.pom.pages;

import com.appsfactory.utils.WaitUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Slf4j
public class SearchResultsPage extends BasePage implements Page {

    @FindBy(css = ".s-main-slot div[data-component-type='s-search-result']")
    private List<WebElement> productContainers;

    public WebElement findCheapestProduct(String productKeyword) {
        refreshProductContainers();
        double minPrice = Double.MAX_VALUE;
        WebElement cheapestProduct = null;
        List<WebElement> validProducts = driver.findElements(By.cssSelector(
                ".s-main-slot div[data-component-type='s-search-result']"));

        if (validProducts.isEmpty()) {
            log.warn("No products found in the search results.");
            return null;
        }

        for (WebElement product : validProducts) {
            try {
                WebElement titleElement = product.findElement(By.cssSelector("h2.s-line-clamp-1 span.a-size-base-plus.a-color-base"));
                String titleText = titleElement.getText().trim();

                if (!titleText.toLowerCase().contains(productKeyword.toLowerCase())) {
                    log.info("Skipping product: '{}' (Does not contain '{}')", titleText, productKeyword);
                    continue;
                }

                List<WebElement> priceElements = product.findElements(By.cssSelector(".a-price:not(.a-text-price) > .a-offscreen"));
                List<WebElement> addToCartButtons = product.findElements(By.cssSelector("button[name='submit.addToCart']"));

                if (!priceElements.isEmpty() && !addToCartButtons.isEmpty()) {
                    String priceText = priceElements.get(0).getAttribute("textContent").replace("$", "").replace(",", "").trim();
                    double price = Double.parseDouble(priceText);

                    if (price < minPrice) {
                        minPrice = price;
                        cheapestProduct = addToCartButtons.get(0);
                    }
                }
            } catch (NoSuchElementException | NumberFormatException e) {
                log.warn("Skipping product due to missing title or invalid price: {}", e.getMessage());
            }
        }

        if (cheapestProduct != null) {
            log.info("Cheapest '{}' product found at ${}", productKeyword, minPrice);
            cheapestProduct.click();
        } else {
            log.warn("No valid '{}' product found.", productKeyword);
        }
        return cheapestProduct;
    }

    private void refreshProductContainers() {
        driver.findElements(By.cssSelector(".s-main-slot div[data-component-type='s-search-result']"));
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