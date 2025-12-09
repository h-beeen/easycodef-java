package io.codef.api;

import io.codef.api.dto.EasyCodefRequest;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.handler.CodefValidator;

/**
 * CODEF API를 간편하게 호출하기 위한 유틸 라이브러리 클래스
 *
 */
public class EasyCodefClient {

	private final EasyCodefDispatcher dispatcher;
	private final String publicKey;

	/**
	 * EasyCodefClient 생성자
	 *
	 * @param dispatcher CODEF API 요청 전송 디스패처
	 * @param publicKey  RSA 암호화를 위한 퍼블릭 키
	 */
	EasyCodefClient(EasyCodefDispatcher dispatcher, String publicKey) {
		this.dispatcher = dispatcher;
		this.publicKey = publicKey;
	}

	/**
	 * 상품 API 요청 수행
	 *
	 * <p>
	 *     요청 파라미터에 Two-Way 정보가 포함된 경우 예외 발생
	 * </p>
	 *
	 * @param request CODEF 상품 요청 정보
	 * @return CODEF 응답 객체
	 */
	public EasyCodefResponse requestProduct(EasyCodefRequest request) {
		CodefValidator.validateTwoWayKeywordsOrThrow(request.getParameterMap());

		return dispatcher.dispatchRequest(request);
	}

	/**
	 * Two-Way 상품(인증) 요청 수행
	 *
	 * <p>
	 *     요청 파라미터에 Two-Way 정보가 없는 경우 예외 발생
	 * </p>
	 *
	 * @param request EasyCodefRequest
	 * @return CODEF 응답 객체
	 */
	public EasyCodefResponse requestCertification(EasyCodefRequest request) {
		CodefValidator.validateTwoWayInfoOrThrow(request.getParameterMap());

		return dispatcher.dispatchRequest(request);
	}

	/**
	 * RSA 암호화를 위한 퍼블릭 키 반환
	 *
	 * @return RSA 퍼블릭 키 문자열
	 */
	public String getPublicKey() {
		return publicKey;
	}
}
