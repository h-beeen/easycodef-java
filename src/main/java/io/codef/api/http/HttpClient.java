package io.codef.api.http;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;

public class HttpClient {

	private static final CloseableHttpClient SHARED_CLIENT = HttpClients.createSystem();

	public static String postJson(HttpPost request) {
		try {
			return SHARED_CLIENT.execute(request, response -> (response.getEntity() == null)
				? ""
				: EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
		} catch (SocketTimeoutException e) {
			throw CodefException.from(CodefError.TIMEOUT_ERROR);
		} catch (IOException e) {
			throw CodefException.from(CodefError.IO_ERROR);
		}
	}
}
