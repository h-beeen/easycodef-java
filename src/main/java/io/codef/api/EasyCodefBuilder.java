package io.codef.api;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;

import io.codef.api.constant.CodefServiceType;
import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;
import io.codef.api.handler.CodefValidator;
import io.codef.api.http.ApacheHttpClient;
import io.codef.api.service.EasyCodefApiService;
import io.codef.api.service.EasyCodefOAuthService;

/**
 * {@link EasyCodef} 객체 생성을 위한 빌더 클래스
 *
 * @author : kdso10@codef.io
 * @since  : Dec 5, 2025
 */
public class EasyCodefBuilder {

	private CodefServiceType serviceType;
	private String clientId;
	private String clientSecret;
	private String publicKey;
	private ApacheHttpClient httpClient;

	/**
	 * EasyCodefBuilder 생성자
	 *
	 * @return EasyCodefBuilder
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
	 * @param serviceType CodefServiceType
	 * @return EasyCodefBuilder
	 * @throws CodefException {@link CodefError} EMPTY_SERVICE_TYPE
	 */
	public EasyCodefBuilder serviceType(CodefServiceType serviceType) {
		this.serviceType = CodefValidator.validateNotNullOrThrow(serviceType, CodefError.EMPTY_SERVICE_TYPE);
		return this;
	}

	/**
	 * CODEF 클라이언트 아이디 설정
	 *
	 * @param clientId String
	 * @return EasyCodefBuilder
	 * @throws CodefException {@link CodefError} EMPTY_CLIENT_ID
	 */
	public EasyCodefBuilder clientId(String clientId) {
		this.clientId = CodefValidator.validateNotNullOrThrow(clientId, CodefError.EMPTY_CLIENT_ID);
		return this;
	}

	/**
	 * CODEF 클라이언트 시크릿 설정
	 *
	 * @param clientSecret String
	 * @return EasyCodefBuilder
	 * @throws CodefException {@link CodefError} EMPTY_CLIENT_SECRET
	 */
	public EasyCodefBuilder clientSecret(String clientSecret) {
		this.clientSecret = CodefValidator.validateNotNullOrThrow(clientSecret, CodefError.EMPTY_CLIENT_SECRET);
		return this;
	}

	/**
	 * RSA 암호화를 위한 퍼블릭 키 설정
	 *
	 * @param publicKey String
	 * @return EasyCodefBuilder
	 * @throws CodefException {@link CodefError} EMPTY_PUBLIC_KEY
	 */
	public EasyCodefBuilder publicKey(String publicKey) {
		this.publicKey = CodefValidator.validateNotNullOrThrow(publicKey, CodefError.EMPTY_PUBLIC_KEY);
		return this;
	}

	/**
	 * HTTP 클라이언트 설정
	 *
	 * <p>
	 *     설정하지 않을 경우, {@link #build()} 호출 시 기본 설정의 {@link ApacheHttpClient} 생성
	 * </p>
	 *
	 * @param httpClient CloseableHttpClient
	 * @return EasyCodefBuilder
	 */
	public EasyCodefBuilder httpClient(CloseableHttpClient httpClient) {
		this.httpClient = ApacheHttpClient.from(httpClient);
		return this;
	}

	/**
	 * 설정 값 기반 {@link EasyCodef} 생성
	 *
	 * @return EasyCodef
	 */
	public EasyCodef build() {
		validateProperties();

		ApacheHttpClient httpClient = (this.httpClient == null)
			? ApacheHttpClient.create()
			: this.httpClient;

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
	 * @throws CodefException {@link CodefError}
	 */
	private void validateProperties() {
		CodefValidator.validateNotNullOrThrow(serviceType, CodefError.EMPTY_SERVICE_TYPE);
		CodefValidator.validateNotNullOrThrow(clientId, CodefError.EMPTY_CLIENT_ID);
		CodefValidator.validateNotNullOrThrow(clientSecret, CodefError.EMPTY_CLIENT_SECRET);
		CodefValidator.validateNotNullOrThrow(publicKey, CodefError.EMPTY_PUBLIC_KEY);
	}
}
