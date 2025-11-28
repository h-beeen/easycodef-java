package io.codef.api.http;

import io.codef.api.dto.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

public interface HttpClient {
    HttpResponse postJson (HttpUriRequest request);
}
