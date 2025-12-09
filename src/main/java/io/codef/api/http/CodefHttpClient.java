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

/**
 * CODEF API 통신을 위한 HTTP 클라이언트
 *
 */
public class CodefHttpClient {

	/**
	 * HTTP 요청 실행
	 *
	 * <p>
	 *     {@link CodefHttpRequest} 정보를 기반으로 커넥션 설정, <br>
	 *     헤더 및 바디 데이터를 전송한 후 수신한 응답을 문자열로 반환
	 * </p>
	 *
	 * @param request 전송할 HTTP 요청 정보
	 * @return 서버로부터 수신한 응답 문자열
	 * @throws CodefException 타임아웃 발생 시 {@link CodefError#TIMEOUT_ERROR}, <br>
	 * 입출력 오류 발생 시 {@link CodefError#IO_ERROR}
	 */
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

	/**
	 * 응답 스트림 처리
	 *
	 * <p>
	 *     HTTP 상태 코드에 따라 성공/에러 스트림을 구분하여 읽고 <br>
	 *     문자열로 변환하여 반환
	 * </p>
	 *
	 * @param connection 연결된 HttpURLConnection 객체
	 * @return 변환된 응답 문자열
	 */
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
