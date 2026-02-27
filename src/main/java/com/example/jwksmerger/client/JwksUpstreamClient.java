package com.example.jwksmerger.client;

import com.example.jwksmerger.merge.JsonWebKey;

import java.util.List;

public interface JwksUpstreamClient {

    List<JsonWebKey> fetchKeys(String upstreamUrl);
}
