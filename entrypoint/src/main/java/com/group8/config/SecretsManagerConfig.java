package com.group8.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;

@Configuration
public class SecretsManagerConfig {
    @Bean
    public SecretsManagerClient secretsManagerClient(){
        return SecretsManagerClient.builder().build();
    }
}
