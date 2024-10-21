package com.marchseniy.GenPictureAiBot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("application.properties")
public class FusionBrainConfig {
    @Value("${fusion.key.secret}")
    private String secretKey;
    @Value("${fusion.key.api}")
    private String apiKey;
}
