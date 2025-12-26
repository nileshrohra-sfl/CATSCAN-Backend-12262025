package com.sfl.core.config;

import com.sfl.core.service.UserStatusCheckInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    @Bean
    UserStatusCheckInterceptor commonInterceptor() {
        return new UserStatusCheckInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(commonInterceptor())
            .addPathPatterns("/api/**")
            .excludePathPatterns("/swagger-ui.html","/swagger**","/webjars/springfox-swagger-ui/**","/webjars/springfox-swagger-ui**","/swagger-resources/**");

    }


}
