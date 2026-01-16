package io.xone.chain.onenft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cn.dev33.satoken.SaManager;

@SpringBootApplication
public class Application {
	
	
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("启动成功，Sa-Token 配置如下：" + SaManager.getConfig());
        System.out.println("onechain-onenft Application started successfully!");
	}

}
