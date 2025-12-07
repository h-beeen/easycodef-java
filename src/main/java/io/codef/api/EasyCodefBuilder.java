package io.codef.api;

import io.codef.api.constant.CodefServiceType;
import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;
import io.codef.api.handler.CodefValidator;
import io.codef.api.http.CodefHttpClient;
import io.codef.api.service.EasyCodefApiService;
import io.codef.api.service.EasyCodefOAuthService;

/**
 * {@link EasyCodef} 객체 생성을 위한 빌더 클래스
 *
 * @author : kdso10
 * @since  : Dec 5, 2025
 */
public class EasyCodefBuilder {

	private CodefServiceType serviceType;
	private String clientId;
	private String clientSecret;
	private String publicKey;

	/**
	 * EasyCodefBuilder 인스턴스 생성
	 *
	 * @return 새로운 EasyCodefBuilder 객체
	 */
	public static EasyCodefBuilder builder() {
		return new EasyCodefBuilder();
	}

	/**
	 * CODEF 서비스 타입 설정
	 *
	 * <p>
	 *     데모/정식 서비스 환경에 따라 {@link CodefServiceType} 지정
	 * </p>
	 *
	 * @param serviceType CODEF 서비스 타입
	 * @return 현재 EasyCodefBuilder 인스턴스
	 * @throws CodefException 서비스 타입이 {@code null}인 경우 {@link CodefError#EMPTY_SERVICE_TYPE}
	 */
	public EasyCodefBuilder serviceType(CodefServiceType serviceType) {
		this.serviceType = CodefValidator.validateNotNullOrThrow(serviceType, CodefError.EMPTY_SERVICE_TYPE);
		return this;
	}

	/**
	 * CODEF 클라이언트 아이디 설정
	 *
	 * @param clientId CODEF 클라이언트 아이디
	 * @return 현재 EasyCodefBuilder 인스턴스
	 * @throws CodefException 클라이언트 아이디가 {@code null}인 경우 {@link CodefError#EMPTY_CLIENT_ID}
	 */
	public EasyCodefBuilder clientId(String clientId) {
		this.clientId = CodefValidator.validateNotNullOrThrow(clientId, CodefError.EMPTY_CLIENT_ID);
		return this;
	}

	/**
	 * CODEF 클라이언트 시크릿 설정
	 *
	 * @param clientSecret CODEF 클라이언트 시크릿
	 * @return 현재 EasyCodefBuilder 인스턴스
	 * @throws CodefException 클라이언트 시크릿이 {@code null}인 경우 {@link CodefError#EMPTY_CLIENT_SECRET}
	 */
	public EasyCodefBuilder clientSecret(String clientSecret) {
		this.clientSecret = CodefValidator.validateNotNullOrThrow(clientSecret, CodefError.EMPTY_CLIENT_SECRET);
		return this;
	}

	/**
	 * RSA 암호화를 위한 퍼블릭 키 설정
	 *
	 * @param publicKey RSA 퍼블릭 키
	 * @return 현재 EasyCodefBuilder 인스턴스
	 * @throws CodefException 퍼블릭 키가 {@code null}인 경우 {@link CodefError#EMPTY_PUBLIC_KEY}
	 */
	public EasyCodefBuilder publicKey(String publicKey) {
		this.publicKey = CodefValidator.validateNotNullOrThrow(publicKey, CodefError.EMPTY_PUBLIC_KEY);
		return this;
	}

	/**
	 * 설정 값 기반 {@link EasyCodef} 생성
	 *
	 * <p>
	 *     필수 값이 유효한지 검증한 후, <br>
	 *     {@link EasyCodefToken}, {@link EasyCodefDispatcher} 등을 구성하여 <br>
	 *     {@link EasyCodef}를 반환
	 * </p>
	 *
	 * @return 새로운 {@link EasyCodef} 객체
	 */
	public EasyCodef build() {
		validateProperties();

		CodefHttpClient httpClient = new CodefHttpClient();
		EasyCodefOAuthService oAuthService = new EasyCodefOAuthService(httpClient);
		EasyCodefApiService apiService = new EasyCodefApiService(httpClient);

		EasyCodefToken token = new EasyCodefToken(clientId, clientSecret, oAuthService);
		EasyCodefDispatcher dispatcher = new EasyCodefDispatcher(token, serviceType, apiService);

		return new EasyCodef(dispatcher, publicKey);
	}

	/**
	 * 필수 설정 값 검증
	 *
	 * <p>
	 *     서비스 타입, 클라이언트 아이디, 클라이언트 시크릿, 퍼블릭 키가 null인 경우 예외 발생
	 * </p>
	 *
	 * @throws CodefException null인 경우 {@link CodefError}
	 */
	private void validateProperties() {
		CodefValidator.validateNotNullOrThrow(serviceType, CodefError.EMPTY_SERVICE_TYPE);
		CodefValidator.validateNotNullOrThrow(clientId, CodefError.EMPTY_CLIENT_ID);
		CodefValidator.validateNotNullOrThrow(clientSecret, CodefError.EMPTY_CLIENT_SECRET);
		CodefValidator.validateNotNullOrThrow(publicKey, CodefError.EMPTY_PUBLIC_KEY);
	}
}
