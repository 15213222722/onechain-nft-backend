package io.xone.chain.onenft;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import cn.dev33.satoken.SaManager;

@SpringBootApplication
@ComponentScan(basePackages = "io.xone.chain.onenft")
@MapperScan("io.xone.chain.onenft.mapper")
public class Application {
	
	
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("启动成功，Sa-Token 配置如下：" + SaManager.getConfig());
        System.out.println("swagger访问路径: http://localhost:8001/swagger-ui/index.html");
        System.out.println("onechain-onenft Application started successfully!");
        
	}

}