package io.codef.api;

import com.alibaba.fastjson2.JSON;

import io.codef.api.constant.CodefServiceType;
import io.codef.api.dto.EasyCodefRequest;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.service.EasyCodefApiService;

/**
 * CODEF API 호출에 필요한 값을 변환하고 전달하는 디스패처 클래스
 *
 * @author : kdso10
 * @since  : Dec 5, 2025
 */
public class EasyCodefDispatcher {

	private final EasyCodefToken token;
	private final CodefServiceType codefServiceType;
	private final EasyCodefApiService apiService;

	/**
	 * EasyCodefDispatcher 생성자
	 *
	 * @param token             Access Token 발급 및 갱신 담당 객체
	 * @param codefServiceType  데모/정식 서비스 환경
	 * @param apiService        API 요청 담당 서비스
	 */
	EasyCodefDispatcher(EasyCodefToken token, CodefServiceType codefServiceType,
		EasyCodefApiService apiService) {
		this.token = token;
		this.codefServiceType = codefServiceType;
		this.apiService = apiService;
	}

	/**
	 * 요청 객체를 CODEF API 호출에 필요한 형태로 변환하여 전송
	 *
	 * <p>
	 *     호출 URL 생성, 유효한 Access Token 조회, 파라미터 JSON 직렬화하여
	 *     {@link EasyCodefApiService}로 전달
	 * </p>
	 *
	 * @param request CODEF 상품 요청 객체
	 * @return CODEF 서버 응답 결과
	 */
	EasyCodefResponse dispatchRequest(EasyCodefRequest request) {
		String urlPath = codefServiceType.getHost() + request.getProductUrl();
		String bearerToken = token.getValidAccessToken();
		String jsonBody = JSON.toJSONString(request.getParameterMap());
		Integer customTimeout = request.getCustomTimeout();

		return apiService.requestProduct(urlPath, bearerToken, jsonBody, customTimeout);
	}
}
