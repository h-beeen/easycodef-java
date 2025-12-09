package io.codef.api.dto;

import java.util.Map;

import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;
import io.codef.api.handler.CodefValidator;

/**
 * {@link EasyCodefRequest} 객체 생성을 위한 빌더 클래스
 *
 */
public class EasyCodefRequestBuilder {

	private String productUrl;
	private Map<String, Object> parameterMap;
	private Integer customTimeout;

	/**
	 * EasyCodefRequestBuilder 인스턴스 생성
	 *
	 * @return 새로운 EasyCodefRequestBuilder 객체
	 */
	public static EasyCodefRequestBuilder builder() {
		return new EasyCodefRequestBuilder();
	}

	/**
	 * CODEF 상품 API 경로 설정
	 *
	 * <p>
	 *     "/v1/***"으로 시작하는 상품 경로 설정
	 * </p>
	 *
	 * @param productUrl CODEF 상품 API 경로
	 * @return 현재 EasyCodefRequestBuilder 인스턴스
	 * @throws CodefException 경로가 {@code null} 이거나 "/v1/***" 형식이 아닌 경우
	 */
	public EasyCodefRequestBuilder productUrl(String productUrl) {
		this.productUrl = CodefValidator.validatePathOrThrow(productUrl);
		return this;
	}

	/**
	 *  요청 파라미터 맵 설정
	 *
	 * @param parameterMap 요청 파라미터 맵
	 * @return 현재 EasyCodefRequestBuilder 인스턴스
	 * @throws CodefException 경로가 {@code null} 이거나 비어 있는 경우 {@link CodefError#EMPTY_PARAMETER}
	 */
	public EasyCodefRequestBuilder parameterMap(Map<String, Object> parameterMap) {
		this.parameterMap = CodefValidator.validateNotNullOrThrow(parameterMap, CodefError.EMPTY_PARAMETER);
		return this;
	}

	/**
	 * 요청 단위 커스텀 타임아웃 설정 (Optional)
	 *
	 * @param customTimeout 요청 타임아웃(초 단위)
	 * @return 현재 EasyCodefRequestBuilder 인스턴스
	 */
	public EasyCodefRequestBuilder customTimeout(Integer customTimeout) {
		this.customTimeout = customTimeout;
		return this;
	}

	/**
	 * 설정 값 기반 {@link EasyCodefRequest} 생성
	 *
	 * @return 새로운 {@link EasyCodefRequest} 객체
	 */
	public EasyCodefRequest build() {
		validateProperties();

		return new EasyCodefRequest(productUrl, parameterMap, customTimeout);
	}

	/**
	 * 필수 설정 값 검증
	 *
	 * <p>
	 *     상품 경로, 요청 파라미터 맵이 null인 경우 예외 발생
	 * </p>
	 *
	 * @throws CodefException null인 경우 {@link CodefError}
	 */
	private void validateProperties() {
		CodefValidator.validateNotNullOrThrow(productUrl, CodefError.EMPTY_PATH);
		CodefValidator.validateNotNullOrThrow(parameterMap, CodefError.EMPTY_PARAMETER);
	}
}
