package io.codef.api.dto;

import java.util.Map;

/**
 * CODEF API 요청 정보를 담는 DTO 클래스
 *
 */
public class EasyCodefRequest {

	private final String productUrl;
	private final Map<String, Object> parameterMap;

	/**
	 * EasyCodefRequest 생성자
	 *
	 * @param productUrl    CODEF 상품 API 경로
	 * @param parameterMap  요청 파라미터 정보(Map 형태)
	 */
	EasyCodefRequest(String productUrl, Map<String, Object> parameterMap) {
		this.productUrl = productUrl;
		this.parameterMap = parameterMap;
	}

	public String getProductUrl() {
		return productUrl;
	}

	public Map<String, Object> getParameterMap() {
		return parameterMap;
	}
}
