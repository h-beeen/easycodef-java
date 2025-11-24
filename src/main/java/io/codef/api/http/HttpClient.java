package io.codef.api.http;

import io.codef.api.dto.HttpResponse;

import java.io.IOException;
import java.util.Map;

public interface HttpClient {
    HttpResponse postJson (
            String url,
            Map<String, String> headers,
            String body
    ) throws IOException;
}
