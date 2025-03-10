package com.appsfactory.pom.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.appsfactory.utils.WaitUtils.awaitForElements;

@Component
@Getter
public class CheckoutPage extends BasePage implements Page {

    @FindBy(css = "[id='sc-active-cart']")
    private WebElement shoppingCartContainer;

    @FindBy(css = "[data-name='Active Items'] .sc-list-item")
    private List<WebElement> cartItems;

    @FindBy(css = "#sc-subtotal-amount-activecart .sc-price")
    private WebElement subTotal;

    public double calculateTotalPrice() {
        double totalPrice = 0.0;
        List<WebElement> priceElements = shoppingCartContainer.findElements(By.cssSelector(".sc-badge-price-to-pay .a-price .a-offscreen"));

        for (WebElement priceElement : priceElements) {
            try {
                String priceText = priceElement.getAttribute("textContent")
                        .replace("$", "").trim();

                double price = Double.parseDouble(priceText);
                totalPrice += price;
            } catch (NoSuchElementException | NumberFormatException e) {
                System.err.println("Skipping item due to missing or invalid price: " + e.getMessage());
            }
        }
        return totalPrice;
    }

    public double getSubTotals() {
        String priceText = subTotal.getAttribute("textContent").replace("$", "").trim();

        return Double.parseDouble(priceText);
    }

    @Override
    public boolean isAt() {
        return awaitForElements(getHeader().getLogoImage(),
                getHeader().getLocationContainer(),
                getHeader().getSearchBox(),
                getHeader().getSearchSubmitButton(),
                getHeader().getNavigationCart(),
                shoppingCartContainer,
                subTotal);
    }
}