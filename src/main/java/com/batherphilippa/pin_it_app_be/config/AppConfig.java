package com.batherphilippa.pin_it_app_be.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AppConfig - application configuration.
 */
@Configuration
public class AppConfig {

    @Bean
    public ModelMapper ModelMapper() {
        return new ModelMapper();
    }
}
