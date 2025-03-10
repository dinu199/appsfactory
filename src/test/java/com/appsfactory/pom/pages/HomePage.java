package com.appsfactory.pom.pages;

import com.appsfactory.utils.WaitUtils;
import org.springframework.stereotype.Component;

@Component
public class HomePage extends BasePage implements Page {


    @Override
    public boolean isAt() {
        return WaitUtils.awaitForElements(getHeader().getLogoImage(),
                getHeader().getLocationContainer(),
                getHeader().getNavigationCart(),
                getHeader().getSearchBox(),
                getHeader().getSearchSubmitButton());
    }
}