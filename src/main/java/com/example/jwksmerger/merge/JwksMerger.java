package com.example.jwksmerger.merge;

import java.util.List;

public interface JwksMerger {

    List<JsonWebKey> merge(List<List<JsonWebKey>> upstreamKeySets);
}
