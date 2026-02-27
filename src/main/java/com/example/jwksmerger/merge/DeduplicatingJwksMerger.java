package com.example.jwksmerger.merge;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class DeduplicatingJwksMerger implements JwksMerger {

    @Override
    public List<JsonWebKey> merge(List<List<JsonWebKey>> upstreamKeySets) {
        Map<String, JsonWebKey> keysByKeyId = new LinkedHashMap<>();

        for (List<JsonWebKey> keySet : upstreamKeySets) {
            for (JsonWebKey key : keySet) {
                if (key.keyId() == null) {
                    continue;
                }
                keysByKeyId.putIfAbsent(key.keyId(), key);
            }
        }

        return List.copyOf(keysByKeyId.values());
    }
}
