package com.example.jwksmerger.merge;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Map;

public record JsonWebKey(@JsonValue Map<String, Object> fields) {

    public String keyId() {
        return (String) fields.get("kid");
    }
}
