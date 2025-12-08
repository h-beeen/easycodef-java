package io.codef.api.http;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link CodefHttpRequest} 객체 생성을 위한 빌더 클래스
 *
 */
public class HttpRequestBuilder {

	private final Map<String, String> headers = new HashMap<>();

	private String url;
	private String body;
	private Integer timeout;

	/**
	 * HttpRequestBuilder 인스턴스 생성
	 *
	 * @return 새로운 HttpRequestBuilder 객체
	 */
	public static HttpRequestBuilder builder() {
		return new HttpRequestBuilder();
	}

	/**
	 * 요청 URL 설정
	 *
	 * @param url 요청할 API URL
	 * @return 현재 HttpRequestBuilder 인스턴스
	 */
	public HttpRequestBuilder url(String url) {
		this.url = url;
		return this;
	}

	/**
	 * HTTP 헤더 추가
	 *
	 * @param key 헤더 키
	 * @param value 헤더 값
	 * @return 현재 HttpRequestBuilder 인스턴스
	 */
	public HttpRequestBuilder header(String key, String value) {
		headers.put(key, value);
		return this;
	}

	/**
	 * 요청 바디(Payload) 설정
	 *
	 * @param body 전송할 데이터 본문
	 * @return 현재 HttpRequestBuilder 인스턴스
	 */
	public HttpRequestBuilder body(String body) {
		this.body = body;
		return this;
	}

	/**
	 * 요청 타임아웃 설정
	 *
	 * @param timeout 타임아웃 시간 (초)
	 * @return 현재 HttpRequestBuilder 인스턴스
	 */
	public HttpRequestBuilder timeout(Integer timeout) {
		this.timeout = timeout;
		return this;
	}

	/**
	 * 설정 값 기반 {@link CodefHttpRequest} 생성
	 *
	 * @return 새로운 {@link CodefHttpRequest} 객체
	 */
	public CodefHttpRequest build() {
		return new CodefHttpRequest(url, headers, body, timeout);
	}
}
