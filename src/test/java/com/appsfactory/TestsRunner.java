package com.appsfactory;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.appsfactory.steps", "com.appsfactory.config"},
        plugin = {"pretty", "html:target/cucumber-reports.html"}
)
public class TestsRunner {
}