package com.appsfactory.config;

import com.appsfactory.AmazonApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;


@EnableConfigurationProperties
@CucumberContextConfiguration
@SpringBootTest(classes = {AmazonApplication.class})
@ComponentScan({"com.appsfactory.config", "com.appsfactory.config"})
public class CucumberSpringConfig {
}


