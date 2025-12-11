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
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;

@ExtendWith(MockitoExtension.class)
@DisplayName("[HTTP Layer] CodefHttpClient Test")
public class CodefHttpClientTest {

	private static final String TEST_URL = "http://test-api.com";
	private static final String HEADER_CONTENT_TYPE = "Content-Type";
	private static final String APPLICATION_JSON = "application/json";

	@Mock
	private HttpURLConnection mockConnection;

	private CodefHttpClient client;
	private CodefHttpRequest validRequest;

	@BeforeEach
	void setUp() throws JsonProcessingException {
		client = new CodefHttpClient() {
			@Override
			protected HttpURLConnection createConnection(String url) {
				return mockConnection;
			}
		};

		Map<String, String> headers = new HashMap<>();
		headers.put(HEADER_CONTENT_TYPE, APPLICATION_JSON);

		Map<String, Object> body = new HashMap<>();
		body.put("param", "value");

		String jsonBody = new ObjectMapper().writeValueAsString(body);

		validRequest = new CodefHttpRequest(TEST_URL, headers, jsonBody);
	}

	@Nested
	@DisplayName("[isSuccessResponse] 응답이 정상이면 성공")
	class ResponseCases {

		@Test
		@DisplayName("[Success] HTTP 정상 요청 및 응답 처리")
		void execute_success() throws IOException {
			Map<String, Object> successMap = new HashMap<>();
			successMap.put("result", "success");
			String successBody = new ObjectMapper().writeValueAsString(successMap);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			when(mockConnection.getOutputStream()).thenReturn(outputStream);
			when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
			when(mockConnection.getInputStream()).thenReturn(
				new ByteArrayInputStream(successBody.getBytes(StandardCharsets.UTF_8)));

			String result = client.execute(validRequest);

			assertAll(
				() -> assertEquals(successBody, result),
				() -> verify(mockConnection).setRequestMethod("POST"),
				() -> verify(mockConnection).setRequestProperty(HEADER_CONTENT_TYPE, APPLICATION_JSON),
				() -> verify(mockConnection).setDoOutput(true),
				() -> verify(mockConnection).setDoInput(true));
		}

		@Test
		@DisplayName("[Success] CodefHttpRequest 내의 body가 null일 경우 요청 바디 없이 정상 처리")
		void execute_requestWithNullBody() throws IOException {
			Map<String, String> emptyHeaders = new HashMap<>();

			CodefHttpRequest requestWithNullBody = new CodefHttpRequest("http://test-api.com", emptyHeaders, null);

			Map<String, Object> body = new HashMap<>();
			body.put("result", "success_no_body");
			String successBody = new ObjectMapper().writeValueAsString(body);

			when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
			when(mockConnection.getInputStream()).thenReturn(
				new ByteArrayInputStream(successBody.getBytes(StandardCharsets.UTF_8)));

			String result = client.execute(requestWithNullBody);

			assertAll(
				() -> assertEquals(successBody, result),
				() -> verify(mockConnection, never()).getOutputStream());
		}

		@Test
		@DisplayName("[Success] 요청 바디가 빈 문자열일 경우 요청 바디 없이 정상 처리")
		void execute_requestWithEmptyBody() throws IOException {
			CodefHttpRequest requestWithEmptyBody = new CodefHttpRequest("http://test-api.com", new HashMap<>(), "");

			Map<String, Object> body = new HashMap<>();
			body.put("result", "success_empty_body");
			String successBody = new ObjectMapper().writeValueAsString(body);

			when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
			when(mockConnection.getInputStream()).thenReturn(
				new ByteArrayInputStream(successBody.getBytes(StandardCharsets.UTF_8)));

			String result = client.execute(requestWithEmptyBody);

			assertAll(
				() -> assertEquals(successBody, result),
				() -> verify(mockConnection, never()).getOutputStream());
		}

		@Test
		@DisplayName("[Success] 연결 종료(disconnect) 호출 보장")
		void execute_connectionAlwaysDisconnected() throws IOException {
			Map<String, Object> successMap = new HashMap<>();
			successMap.put("result", "success");
			String successBody = new ObjectMapper().writeValueAsString(successMap);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			when(mockConnection.getOutputStream()).thenReturn(outputStream);
			when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
			when(mockConnection.getInputStream()).thenReturn(
				new ByteArrayInputStream(successBody.getBytes(StandardCharsets.UTF_8)));

			assertAll(
				() -> {
					String result = client.execute(validRequest);
					assertEquals(successBody, result);
				},
				() -> assertThrows(CodefException.class, () -> client.execute(validRequest)),
				() -> verify(mockConnection, times(2)).disconnect());
		}
	}

	@Nested
	@DisplayName("[Throw Exception] 예외처리가 정상 동작하면 성공")
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

	@ParameterizedTest(name = "[{index}] HTTP {0} → {1}")
	@MethodSource("errorResponseProvider")
	@DisplayName("HTTP 에러 코드 매핑 테스트")
	void execute_HttpErrorMapping(int httpStatus,
		CodefError expectedError,
		String errorBody) throws IOException {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		when(mockConnection.getOutputStream()).thenReturn(outputStream);
		when(mockConnection.getResponseCode()).thenReturn(httpStatus);
		when(mockConnection.getErrorStream()).thenReturn(
			new ByteArrayInputStream(errorBody.getBytes(StandardCharsets.UTF_8)));

		CodefException exception = assertThrows(CodefException.class,
			() -> client.execute(validRequest));

		String expectedMessage = expectedError.getMessage()
			+ System.lineSeparator()
			+ errorBody;

		assertAll(
			() -> assertEquals(expectedError, exception.getCodefError()),
			() -> assertEquals(expectedMessage, exception.getMessage()));
	}

	private static Stream<Arguments> errorResponseProvider() {
		return Stream.of(
			Arguments.of(
				HttpURLConnection.HTTP_BAD_REQUEST,
				CodefError.INTERNAL_SERVER_ERROR,
				"Bad Request"),
			Arguments.of(
				HttpURLConnection.HTTP_UNAUTHORIZED,
				CodefError.UNAUTHORIZED,
				"{\"error\":\"invalid_token\"}"),
			Arguments.of(
				HttpURLConnection.HTTP_FORBIDDEN,
				CodefError.INTERNAL_SERVER_ERROR,
				"Forbidden"),
			Arguments.of(
				HttpURLConnection.HTTP_NOT_FOUND,
				CodefError.INTERNAL_SERVER_ERROR,
				"Not Found"),
			Arguments.of(
				HttpURLConnection.HTTP_INTERNAL_ERROR,
				CodefError.INTERNAL_SERVER_ERROR,
				"Server Error"));
	}
}
