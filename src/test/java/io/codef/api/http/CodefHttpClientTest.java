package io.codef.api.http;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;

@ExtendWith(MockitoExtension.class)
@DisplayName("[HTTP Layer] CodefHttpClient Test")
public class CodefHttpClientTest {

	@Mock
	private HttpURLConnection mockConnection;

	private CodefHttpClient client;
	private CodefHttpRequest validRequest;

	@BeforeEach
	void setUp() {
		client = new CodefHttpClient() {
			@Override
			protected HttpURLConnection createConnection(String url) {
				return mockConnection;
			}
		};

		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		validRequest = new CodefHttpRequest("http://test-api.com", headers, "{\"param\":\"value\"}");
	}

	@Nested
	@DisplayName("[isSuccessResponse] 응답이 정상이면 성공")
	class SuccessCases {

		@Test
		@DisplayName("[Success] HTTP 정상 요청 및 응답 처리")
		void execute_Success() throws IOException {
			String successBody = "{\"result\":\"success\"}";
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			when(mockConnection.getOutputStream()).thenReturn(outputStream);
			when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
			when(mockConnection.getInputStream()).thenReturn(
				new ByteArrayInputStream(successBody.getBytes(StandardCharsets.UTF_8)));

			String result = client.execute(validRequest);

			assertEquals(successBody, result);
			verify(mockConnection).setRequestMethod("POST");
			verify(mockConnection).setRequestProperty("Content-Type", "application/json");
			verify(mockConnection).setDoOutput(true);
			verify(mockConnection).setDoInput(true);
		}

		@Test
		@DisplayName("[Success] CodefHttpRequest 내의 body가 null일 경우 요청 바디 없이 정상 처리")
		void execute_RequestWithNullBody() throws IOException {
			CodefHttpRequest requestWithNullBody = new CodefHttpRequest("http://test-api.com", new HashMap<>(), null);
			String successBody = "{\"result\":\"success_no_body\"}";

			when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
			when(mockConnection.getInputStream()).thenReturn(
				new ByteArrayInputStream(successBody.getBytes(StandardCharsets.UTF_8)));

			String result = client.execute(requestWithNullBody);

			assertEquals(successBody, result);
			verify(mockConnection, never()).getOutputStream();
		}

		@Test
		@DisplayName("[Success] 요청 바디가 빈 문자열일 경우 요청 바디 없이 정상 처리")
		void execute_RequestWithEmptyBody() throws IOException {
			CodefHttpRequest requestWithEmptyBody = new CodefHttpRequest("http://test-api.com", new HashMap<>(), "");
			String successBody = "{\"result\":\"success_empty_body\"}";

			when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
			when(mockConnection.getInputStream()).thenReturn(
				new ByteArrayInputStream(successBody.getBytes(StandardCharsets.UTF_8)));

			String result = client.execute(requestWithEmptyBody);

			assertEquals(successBody, result);
			verify(mockConnection, never()).getOutputStream();
		}

		@Test
		@DisplayName("[Success] 연결 종료(disconnect)가 항상 호출되는지 확인")
		void execute_ConnectionAlwaysDisconnected() throws IOException {
			String successBody = "{\"result\":\"success\"}";
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			when(mockConnection.getOutputStream()).thenReturn(outputStream);
			when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
			when(mockConnection.getInputStream()).thenReturn(
				new ByteArrayInputStream(successBody.getBytes(StandardCharsets.UTF_8)));

			client.execute(validRequest);

			verify(mockConnection, times(1)).disconnect();

			reset(mockConnection);
			when(mockConnection.getOutputStream()).thenThrow(new SocketTimeoutException());

			assertThrows(CodefException.class, () -> client.execute(validRequest));

			verify(mockConnection, times(1)).disconnect();
		}
	}

	@Nested
	@DisplayName("[Throw Exception] Exception Cases (IO / Timeout / Empty Body)")
	class ExceptionCases {

		@Test
		@DisplayName("[Exception] IO 오류 발생 시 IO_ERROR 예외처리")
		void execute_IOException() throws IOException {
			when(mockConnection.getOutputStream()).thenThrow(new IOException());

			CodefException exception = assertThrows(CodefException.class, () -> client.execute(validRequest));
			assertEquals(CodefError.IO_ERROR, exception.getCodefError());
		}

		@Test
		@DisplayName("[Exception] 타임아웃 발생 시 TIMEOUT_ERROR 예외처리")
		void execute_SocketTimeoutException() throws IOException {
			when(mockConnection.getOutputStream()).thenThrow(new SocketTimeoutException());

			CodefException exception = assertThrows(CodefException.class, () -> client.execute(validRequest));
			assertEquals(CodefError.TIMEOUT_ERROR, exception.getCodefError());
		}

		@Test
		@DisplayName("[Exception] 응답 바디가 비어있을 경우 EMPTY_CODEF_RESPONSE 예외처리")
		void execute_EmptyResponse() throws IOException {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			when(mockConnection.getOutputStream()).thenReturn(outputStream);
			when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
			when(mockConnection.getInputStream()).thenReturn(null);

			CodefException exception = assertThrows(CodefException.class, () -> client.execute(validRequest));
			assertEquals(CodefError.EMPTY_CODEF_RESPONSE, exception.getCodefError());
		}
	}

	@Nested
	@DisplayName("[HTTP Error] HTTP Status Error Cases")
	class HTTPStatusErrorCases {

		@Test
		@DisplayName("[Exception] 400 Bad Request 응답 시 INTERNAL_SERVER_ERROR 예외 발생")
		void execute_BadRequest() throws IOException {
			String errorBody = "Bad Request";
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			when(mockConnection.getOutputStream()).thenReturn(outputStream);
			when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);
			when(mockConnection.getErrorStream()).thenReturn(
				new ByteArrayInputStream(errorBody.getBytes(StandardCharsets.UTF_8)));

			CodefException exception = assertThrows(CodefException.class, () -> client.execute(validRequest));
			assertEquals(CodefError.INTERNAL_SERVER_ERROR, exception.getCodefError());
			String expectedMessage = String.format("%s\n%s", CodefError.INTERNAL_SERVER_ERROR.getMessage(), errorBody);
			assertEquals(expectedMessage, exception.getMessage());
		}

		@Test
		@DisplayName("[Exception] 401 Unauthorized 응답 시 UNAUTHORIZED 예외처리")
		void execute_Unauthorized() throws IOException {
			String errorBody = "{\"error\":\"invalid_token\"}";
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			when(mockConnection.getOutputStream()).thenReturn(outputStream);
			when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_UNAUTHORIZED);
			when(mockConnection.getErrorStream()).thenReturn(
				new ByteArrayInputStream(errorBody.getBytes(StandardCharsets.UTF_8)));

			CodefException exception = assertThrows(CodefException.class, () -> client.execute(validRequest));
			assertEquals(CodefError.UNAUTHORIZED, exception.getCodefError());
			String expectedMessage = String.format("%s\n%s", CodefError.UNAUTHORIZED.getMessage(), errorBody);
			assertEquals(expectedMessage, exception.getMessage());
		}

		@Test
		@DisplayName("[Exception] 403 Forbidden 응답 시 INTERNAL_SERVER_ERROR 예외 발생")
		void execute_Forbidden() throws IOException {
			String errorBody = "Forbidden";
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			when(mockConnection.getOutputStream()).thenReturn(outputStream);
			when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_FORBIDDEN);
			when(mockConnection.getErrorStream()).thenReturn(
				new ByteArrayInputStream(errorBody.getBytes(StandardCharsets.UTF_8)));

			CodefException exception = assertThrows(CodefException.class, () -> client.execute(validRequest));
			assertEquals(CodefError.INTERNAL_SERVER_ERROR, exception.getCodefError());
			String expectedMessage = String.format("%s\n%s", CodefError.INTERNAL_SERVER_ERROR.getMessage(), errorBody);
			assertEquals(expectedMessage, exception.getMessage());
		}

		@Test
		@DisplayName("[Exception] 404 Not Found 응답 시 INTERNAL_SERVER_ERROR 예외 발생")
		void execute_NotFound() throws IOException {
			String errorBody = "Not Found";
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			when(mockConnection.getOutputStream()).thenReturn(outputStream);
			when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_NOT_FOUND);
			when(mockConnection.getErrorStream()).thenReturn(
				new ByteArrayInputStream(errorBody.getBytes(StandardCharsets.UTF_8)));

			CodefException exception = assertThrows(CodefException.class, () -> client.execute(validRequest));
			assertEquals(CodefError.INTERNAL_SERVER_ERROR, exception.getCodefError());
			String expectedMessage = String.format("%s\n%s", CodefError.INTERNAL_SERVER_ERROR.getMessage(), errorBody);
			assertEquals(expectedMessage, exception.getMessage());
		}

		@Test
		@DisplayName("[Exception] 500 서버 에러 응답 시 INTERNAL_SERVER_ERROR 예외처리")
		void execute_InternalServerError() throws IOException {
			String errorBody = "Server Error";
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			when(mockConnection.getOutputStream()).thenReturn(outputStream);
			when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_INTERNAL_ERROR);
			when(mockConnection.getErrorStream()).thenReturn(
				new ByteArrayInputStream(errorBody.getBytes(StandardCharsets.UTF_8)));

			CodefException exception = assertThrows(CodefException.class, () -> client.execute(validRequest));
			assertEquals(CodefError.INTERNAL_SERVER_ERROR, exception.getCodefError());
			String expectedMessage = String.format("%s\n%s", CodefError.INTERNAL_SERVER_ERROR.getMessage(), errorBody);
			assertEquals(expectedMessage, exception.getMessage());
		}
	}
}
