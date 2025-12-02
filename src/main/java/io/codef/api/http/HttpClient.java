package io.codef.api.http;

import org.apache.hc.client5.http.classic.methods.HttpPost;

public interface HttpClient {
	String execute(HttpPost request);
}
