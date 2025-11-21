package io.codef.api;

import java.io.IOException;
import java.util.Map;

public interface EasyCodefHttpClient {
    EasyCodefHttpResponse postJson (
            String url,
            Map<String, String> headers,
            String body
    ) throws IOException;
}
