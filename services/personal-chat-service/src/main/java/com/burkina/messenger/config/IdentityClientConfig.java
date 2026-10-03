package com.burkina.messenger.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class IdentityClientConfig {

    @Value("${identity.service.url}")
    private String identityServiceUrl;

    @Bean
    public RestClient identityRestClient() {
        return RestClient.builder()
                .baseUrl(identityServiceUrl)
                .build();
    }
}
