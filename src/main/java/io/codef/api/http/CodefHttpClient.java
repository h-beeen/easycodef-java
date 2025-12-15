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
import io.codef.api.handler.CodefValidator;

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
			connection = createConnection(request.getUrl());
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
	 * @throws CodefException 응답 코드가 200이 아닌 경우 {@link CodefError#UNAUTHORIZED} 또는
	 * {@link CodefError#INTERNAL_SERVER_ERROR}
	 * @throws CodefException 응답 본문이 비어 있는 경우 {@link CodefError#EMPTY_CODEF_RESPONSE}
	 * @throws CodefException 응답 처리 중 I/O 오류가 발생한 경우 {@link CodefError#IO_ERROR}
	 */
	private String getResponse(HttpURLConnection connection) {
		try {
			int responseCode = connection.getResponseCode();
			String responseBody = getString(connection, responseCode);

			if (responseCode != HttpURLConnection.HTTP_OK) {
				CodefError codefError = (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED)
					? CodefError.UNAUTHORIZED
					: CodefError.INTERNAL_SERVER_ERROR;

				throw CodefException.of(codefError, responseBody);
			}

			CodefValidator.validateNotNullOrThrow(responseBody, CodefError.EMPTY_CODEF_RESPONSE);

			return responseBody;
		} catch (IOException e) {
			throw CodefException.from(CodefError.IO_ERROR);
		}
	}

	/**
	 * 응답 코드를 기준으로 성공/에러 스트림을 읽어 문자열로 변환
	 *
	 * @param connection 연결된 HttpURLConnection 객체
	 * @param responseCode HTTP 응답 상태 코드
	 * @return 변환된 응답 문자열
	 * @throws IOException 스트림 읽기 중 오류 발생 시
	 */
	private static String getString(HttpURLConnection connection, int responseCode) throws IOException {
		InputStream inputStream;

		if (responseCode == HttpURLConnection.HTTP_OK) {
			inputStream = connection.getInputStream();
		} else {
			inputStream = connection.getErrorStream();
		}

		StringBuilder responseBuilder = new StringBuilder();
		if (inputStream != null) {
			try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
				String line;
				while ((line = reader.readLine()) != null) {
					responseBuilder.append(line);
				}
			}
		}
		return responseBuilder.toString();
	}

	/**
	 * URL 문자열로부터 {@link HttpURLConnection} 생성
	 *
	 * @param urlString 요청 URL 문자열
	 * @return 생성된 {@link HttpURLConnection}
	 * @throws CodefException 커넥션 생성 중 I/O 오류가 발생한 경우 {@link CodefError#IO_ERROR}
	 */
	private HttpURLConnection createConnection(String urlString) {
		try {
			URL url = new URL(urlString);
			return (HttpURLConnection)url.openConnection();
		} catch (IOException e) {
			throw CodefException.from(CodefError.IO_ERROR);
		}
	}
}
