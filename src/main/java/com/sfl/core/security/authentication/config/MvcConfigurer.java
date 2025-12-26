package com.sfl.core.security.authentication.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebMvc
@EnableAutoConfiguration

public class MvcConfigurer implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("account/reset/css/**").addResourceLocations("classpath:/css/").setCachePeriod(31556926);
        registry.addResourceHandler("account/reset/images/**").addResourceLocations("classpath:/images/").setCachePeriod(31556926);
        registry.addResourceHandler("account/reset/js/**").addResourceLocations("classpath:/js/").setCachePeriod(31556926);
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

}
