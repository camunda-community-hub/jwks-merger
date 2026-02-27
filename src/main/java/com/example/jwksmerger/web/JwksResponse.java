package com.example.jwksmerger.web;

import com.example.jwksmerger.merge.JsonWebKey;

import java.util.List;

public record JwksResponse(List<JsonWebKey> keys) {
}
