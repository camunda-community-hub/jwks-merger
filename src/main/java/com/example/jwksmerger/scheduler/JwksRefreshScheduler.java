package com.example.jwksmerger.scheduler;

import com.example.jwksmerger.client.JwksUpstreamClient;
import com.example.jwksmerger.client.JwksUpstreamException;
import com.example.jwksmerger.configuration.JwksMergerProperties;
import com.example.jwksmerger.merge.JsonWebKey;
import com.example.jwksmerger.merge.JwksMerger;
import com.example.jwksmerger.store.JwksKeyStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JwksRefreshScheduler {

    private static final Logger logger = LoggerFactory.getLogger(JwksRefreshScheduler.class);

    private final JwksMergerProperties properties;
    private final JwksUpstreamClient upstreamClient;
    private final JwksMerger merger;
    private final JwksKeyStore keyStore;

    public JwksRefreshScheduler(
            JwksMergerProperties properties,
            JwksUpstreamClient upstreamClient,
            JwksMerger merger,
            JwksKeyStore keyStore) {
        this.properties = properties;
        this.upstreamClient = upstreamClient;
        this.merger = merger;
        this.keyStore = keyStore;
    }

    @Scheduled(fixedRateString = "${jwks.refresh-interval-ms}", initialDelay = 0)
    public void refresh() {
        logger.debug("Refreshing JWKS from {} upstream(s)", properties.upstreamUrls().size());

        List<List<JsonWebKey>> upstreamKeySets = fetchFromAllUpstreams();
        List<JsonWebKey> mergedKeys = merger.merge(upstreamKeySets);
        keyStore.update(mergedKeys);

        logger.info("JWKS key store updated — {} key(s) merged from {} upstream(s)",
                mergedKeys.size(), properties.upstreamUrls().size());
    }

    private List<List<JsonWebKey>> fetchFromAllUpstreams() {
        List<List<JsonWebKey>> results = new ArrayList<>();

        for (String upstreamUrl : properties.upstreamUrls()) {
            try {
                List<JsonWebKey> keys = upstreamClient.fetchKeys(upstreamUrl);
                results.add(keys);
                logger.debug("Fetched {} key(s) from {}", keys.size(), upstreamUrl);
            } catch (JwksUpstreamException exception) {
                logger.error("Skipping unreachable upstream — {}", exception.getMessage());
            }
        }

        return results;
    }
}
