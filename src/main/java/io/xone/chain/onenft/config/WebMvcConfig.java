package io.xone.chain.onenft.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cn.dev33.satoken.fun.strategy.SaCorsHandleFunction;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import io.xone.chain.onenft.common.LoginInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }
    
    /**
	 * CORS 跨域处理策略
	 */
	@Bean
	SaCorsHandleFunction corsHandle() {
		return (req, res, sto) -> {
			res.
				// 允许指定域访问跨域资源
				setHeader("Access-Control-Allow-Origin", "*")
				// 允许所有请求方式
				.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE")
				// 有效时间
				.setHeader("Access-Control-Max-Age", "3600")
				// 允许的header参数
				.setHeader("Access-Control-Allow-Headers", "*");

			// 如果是预检请求，则立即返回到前端
			SaRouter.match(SaHttpMethod.OPTIONS)
				.free(r -> System.out.println("--------OPTIONS预检请求，不做处理"))
				.back();
		};
	}


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