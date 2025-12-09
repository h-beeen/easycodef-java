package io.codef.api.service;

import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.handler.CodefValidator;
import io.codef.api.handler.ResponseHandler;
import io.codef.api.http.CodefHttpClient;
import io.codef.api.http.CodefHttpRequest;

/**
 * CODEF API 요청 전송 공통 서비스 추상 클래스
 *
 */
public abstract class EasyCodefService {

	private final CodefHttpClient httpClient;

	/**
	 * EasyCodefService 생성자
	 *
	 * @param httpClient HTTP 요청 실행을 담당
	 */
	EasyCodefService(CodefHttpClient httpClient) {
		this.httpClient = httpClient;
	}

	/**
	 * CODEF API 요청 실행 및 결과 반환
	 *
	 * @param request 전송할 HTTP 요청 정보
	 * @return API 응답 결과를 처리한 EasyCodefResponse 객체
	 */
	EasyCodefResponse sendRequest(CodefHttpRequest request) {
		String httpResponse = CodefValidator.validateJSONResponse(httpClient.execute(request));

		return ResponseHandler.processResponse(httpResponse);
	}
}
