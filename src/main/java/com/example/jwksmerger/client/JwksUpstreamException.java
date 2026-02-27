package com.example.jwksmerger.client;

public class JwksUpstreamException extends RuntimeException {

    public JwksUpstreamException(String upstreamUrl, Throwable cause) {
        super("Failed to fetch JWKS from upstream: " + upstreamUrl, cause);
    }
}
