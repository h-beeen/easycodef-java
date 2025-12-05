package io.codef.api.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;

public class CodefHttpClient {

	public String execute(CodefHttpRequest request) {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(request.getUrl());
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);

			Map<String, String> headers = request.getHeaders();
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				connection.setRequestProperty(entry.getKey(), entry.getValue());
			}

			Integer timeout = request.getTimeout();
			if (timeout != null && timeout > 0) {
				int timeoutMillis = timeout * 1000;
				connection.setConnectTimeout(timeoutMillis);
				connection.setReadTimeout(timeoutMillis);
			}

			String body = request.getBody();
			if (body != null && !body.isEmpty()) {
				try (OutputStream os = connection.getOutputStream()) {
					os.write(body.getBytes(StandardCharsets.UTF_8));
				}
			}

			return getResponse(connection);
		} catch (SocketTimeoutException e) {
			throw CodefException.from(CodefError.TIMEOUT_ERROR);
		} catch (IOException e) {
			throw CodefException.from(CodefError.IO_ERROR);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	private String getResponse(HttpURLConnection connection) {
		try {
			int status = connection.getResponseCode();
			InputStream is;
			if (status >= 200 && status < 300) {
				is = connection.getInputStream();
			} else {
				is = connection.getErrorStream();
			}

			if (is == null) {
				return "";
			}

			try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
				StringBuilder response = new StringBuilder();
				String responseLine;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine);
				}
				return response.toString();
			}
		} catch (IOException e) {
			throw CodefException.from(CodefError.IO_ERROR);
		}
	}
}
