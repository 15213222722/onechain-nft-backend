package io.xone.chain.onenft.config;

import io.onechain.OneChain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OneChainConfig {

    private final OneChainProperties properties;

    public OneChainConfig(OneChainProperties properties) {
        this.properties = properties;
    }

    @Bean
    public OneChain oneChain() {
        return new OneChain(
            properties.getRpcUrl(),
            properties.getFaucetUrl(),
            "", // Assuming empty password as per Test.java
            properties.getKeystorePath()
        );
    }
}
