package com.appsfactory.pom.pages;

import com.appsfactory.pom.blocks.Header;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Component
@Slf4j
public class BasePage {

    @Autowired
    private Header header;

    @Autowired
    protected WebDriver driver;

    @PostConstruct
    private void init() {
        PageFactory.initElements(driver, this);
    }
}