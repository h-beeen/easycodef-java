package io.codef.api;

import io.codef.api.dto.EasyCodefRequest;
import io.codef.api.dto.EasyCodefResponse;
import io.codef.api.handler.CodefValidator;

/**
 * CODEF APIf를 간편하게 호출하기 위한 유틸 라이브러리 클래스
 *
 * @author kdso10@codef.io
 * @since Dec 5, 2025
 */
public class EasyCodef {

	private final EasyCodefDispatcher dispatcher;
	private final String publicKey;

	/**
	 * EasyCodef 생성자
	 *
	 * <p>
	 *     {@link EasyCodefDispatcher}는 요청 전송을 담당하는 디스패처이며, <br>
	 *     {@code publicKey}는 민감 정보에 대한 RSA 암호화가 필요할 때 사용
	 * </p>
	 *
	 * @param dispatcher EasyCodefDispatcher
	 * @param publicKey  String
	 */
	EasyCodef(EasyCodefDispatcher dispatcher, String publicKey) {
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
	 * @param request EasyCodefRequest
	 * @return EasyCodefResponse
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
	 * @return EasyCodefResponse
	 */
	public EasyCodefResponse requestCertification(EasyCodefRequest request) {
		CodefValidator.validateTwoWayInfoOrThrow(request.getParameterMap());

		return dispatcher.dispatchRequest(request);
	}

	/**
	 * RSA 암호화를 위한 퍼블릭 키 반환
	 *
	 * @return String
	 */
	public String getPublicKey() {
		return publicKey;
	}
}
