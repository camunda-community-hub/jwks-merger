package com.example.jwksmerger.web;

import com.example.jwksmerger.store.JwksKeyStore;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwksController {

    private final JwksKeyStore keyStore;

    public JwksController(JwksKeyStore keyStore) {
        this.keyStore = keyStore;
    }

    @GetMapping(value = "/jwks", produces = MediaType.APPLICATION_JSON_VALUE)
    public JwksResponse getJwks() {
        return new JwksResponse(keyStore.getCurrentKeys());
    }
}
