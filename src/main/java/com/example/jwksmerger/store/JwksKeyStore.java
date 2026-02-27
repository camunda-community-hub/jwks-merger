package com.example.jwksmerger.store;

import com.example.jwksmerger.merge.JsonWebKey;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class JwksKeyStore {

    private final AtomicReference<List<JsonWebKey>> currentKeys = new AtomicReference<>(List.of());

    public void update(List<JsonWebKey> keys) {
        currentKeys.set(keys);
    }

    public List<JsonWebKey> getCurrentKeys() {
        return currentKeys.get();
    }
}
