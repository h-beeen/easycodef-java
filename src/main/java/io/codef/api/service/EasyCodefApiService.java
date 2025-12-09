package io.codef.api.service;

import static io.codef.api.constant.CodefConstant.*;

import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.http.CodefHttpClient;
import io.codef.api.http.CodefHttpRequest;
import io.codef.api.http.HttpRequestBuilder;

/**
 * CODEF API 상품 요청 서비스 클래스
 *
 */
public class EasyCodefApiService extends EasyCodefService {

	public EasyCodefApiService(CodefHttpClient httpClient) {
		super(httpClient);
	}

	/**
	 * API 상품 요청 실행
	 *
	 * @param urlPath       요청 URL 경로
	 * @param bearerToken   Bearer 인증 토큰
	 * @param jsonBody      요청 파라미터 (JSON 문자열)
	 * @return API 응답 결과 객체
	 */
	public EasyCodefResponse requestProduct(String urlPath, String bearerToken, String jsonBody) {
		CodefHttpRequest request = HttpRequestBuilder.builder()
			.url(urlPath)
			.header("Authorization", bearerToken)
			.header("Content-Type", APPLICATION_JSON.getValue())
			.body(jsonBody)
			.build();

		return sendRequest(request);
	}
}
