package com.adfecomm.adfecomm.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${frontend.url}")
    private String frontEndUrl;

    @Value("${spring.security.token.transport}")
    private String tokenTransport;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(frontEndUrl)
                .allowedMethods("GET","POST","PUT", "DELETE","OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);

        if (tokenTransport.equals("HEADER")) {
            registry.addMapping("/**")
                    .exposedHeaders("Authorization");
        }

        WebMvcConfigurer.super.addCorsMappings(registry);
    }
}
