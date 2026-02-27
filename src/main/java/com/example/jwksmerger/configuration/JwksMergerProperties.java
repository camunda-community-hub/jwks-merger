package com.example.jwksmerger.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.List;

@ConfigurationProperties(prefix = "jwks")
public record JwksMergerProperties(
        List<String> upstreamUrls,
        long refreshIntervalMs
) {

    @ConstructorBinding
    public JwksMergerProperties {
        if (upstreamUrls == null || upstreamUrls.isEmpty()) {
            throw new IllegalStateException("jwks.upstream-urls must contain at least one URL");
        }
    }
}
