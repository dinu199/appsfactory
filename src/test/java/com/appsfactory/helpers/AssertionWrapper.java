package com.appsfactory.helpers;

import com.appsfactory.pom.pages.Page;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public final class AssertionWrapper {

    public static void assertThatPageIsOpened(Page page) {
        assertThat(page.isAt()).as(page.getClass().getSimpleName() + " should be displayed.").isTrue();
    }

    public static void assertElementExists(WebElement element) {
        assertThat(element.isDisplayed()).as("Element should be present on page").isTrue();
    }

    public static void assertElementsAreEqual(double expected, double actual) {
        assertThat(actual)
                .as("Expected subtotal price (%.2f) should match calculated price (%.2f)", expected, actual)
                .isEqualTo(expected);
    }
}