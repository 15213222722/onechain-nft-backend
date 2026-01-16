package io.xone.chain.onenft.config;

import io.xone.chain.onenft.common.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/users/login",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v3/api-docs",
                        "/webjars/**",
                        "/doc.html"
                );
    }
}
