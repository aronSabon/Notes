package com.apptechnosoft.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix="fonepay")
public class FonepayConfig {
    private String pid;
    private String secret;
    private String baseUrl;
    private String returnUrl;
 
}
