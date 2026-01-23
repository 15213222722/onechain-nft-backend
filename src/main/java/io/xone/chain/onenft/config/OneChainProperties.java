package io.xone.chain.onenft.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "onechain")
public class OneChainProperties {
    private String rpcUrl;
    private String faucetUrl;
    private String keystorePath;
    private Contract contract;

    @Data
    public static class Contract {
        private String packageId;
        private String moduleName;
        private String functionName;
    }
}
