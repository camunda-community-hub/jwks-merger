package com.example.jwksmerger.client;

import com.example.jwksmerger.merge.JsonWebKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;

@Component
public class HttpJwksUpstreamClient implements JwksUpstreamClient {

    private static final Logger logger = LoggerFactory.getLogger(HttpJwksUpstreamClient.class);

    private final RestClient restClient;

    public HttpJwksUpstreamClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    @Override
    public List<JsonWebKey> fetchKeys(String upstreamUrl) {
        try {
            Map<String, Object> response = restClient.get()
                    .uri(upstreamUrl)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            return extractKeys(upstreamUrl, response);
        } catch (RestClientException exception) {
            throw new JwksUpstreamException(upstreamUrl, exception);
        }
    }

    @SuppressWarnings("unchecked")
    private List<JsonWebKey> extractKeys(String upstreamUrl, Map<String, Object> response) {
        if (response == null || !response.containsKey("keys")) {
            logger.warn("Response from {} contains no 'keys' field", upstreamUrl);
            return List.of();
        }

        List<Map<String, Object>> rawKeys = (List<Map<String, Object>>) response.get("keys");
        return rawKeys.stream()
                .map(JsonWebKey::new)
                .toList();
    }
}
