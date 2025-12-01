package io.codef.api.http;

import static io.codef.api.constants.HttpConstant.*;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;

public class HttpClient {

	private static final CloseableHttpClient SHARED_CLIENT = HttpClients.createSystem();

	public static HttpResponse postJson(HttpPost request) {
		try {
			return SHARED_CLIENT.execute(request, response -> {
				int statusCode = response.getCode();

				String responseBody = (response.getEntity() == null)
					? ""
					: EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

				return new HttpResponse(statusCode, responseBody);
			});
		} catch (SocketTimeoutException e) {
			return new HttpResponse(STATUS_TIMEOUT_ERROR, e.getMessage());
		} catch (IOException e) {
			return new HttpResponse(STATUS_CONNECTION_ERROR, e.getMessage());
		}
	}
}
