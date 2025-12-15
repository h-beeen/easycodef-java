package io.codef.api;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;

/**
 * {@link EasyCodefClient} 인스턴스를 서비스 타입 및 자격증명 기준으로 캐싱하는 레지스트리
 *
 */
final class EasyCodefClientRegistry {

	private final Map<Snapshot, EasyCodefClient> cache = new ConcurrentHashMap<>();

	/**
	 * 설정 정보를 기반으로 {@link EasyCodefClient}를 조회하거나 없으면 생성하여 반환
	 *
	 * @param properties CODEF 설정 정보
	 * @param serviceType 서비스 타입(API/DEMO)
	 * @return 캐시된 또는 신규 생성된 {@link EasyCodefClient}
	 * @throws CodefException 서비스 타입이 유효하지 않은 경우 {@link CodefError#EMPTY_SERVICE_TYPE}
	 */
	EasyCodefClient getOrCreate(EasyCodefProperties properties, EasyCodefServiceType serviceType) {
		Snapshot snapshot = Snapshot.from(properties, serviceType);

		return cache.computeIfAbsent(snapshot, s -> EasyCodefBuilder.builder()
			.serviceType(serviceType)
			.clientId(snapshot.clientId)
			.clientSecret(snapshot.clientSecret)
			.publicKey(snapshot.publicKey)
			.build());
	}

	/**
	 * 서비스 타입 및 자격증명 스냅샷(캐시 키) 클래스
	 *
	 */
	private static final class Snapshot {
		final EasyCodefServiceType serviceType;
		final String clientId;
		final String clientSecret;
		final String publicKey;

		private Snapshot(EasyCodefServiceType serviceType, String clientId, String clientSecret, String publicKey) {
			this.serviceType = serviceType;
			this.clientId = clientId;
			this.clientSecret = clientSecret;
			this.publicKey = publicKey;
		}

		/**
		 * 설정 정보로부터 서비스 타입에 맞는 자격증명 스냅샷 생성
		 *
		 * @param properties CODEF 설정 정보
		 * @param serviceType 서비스 타입(API/DEMO)
		 * @return 생성된 {@link Snapshot}
		 * @throws CodefException 서비스 타입이 유효하지 않은 경우 {@link CodefError#EMPTY_SERVICE_TYPE}
		 */
		static Snapshot from(EasyCodefProperties properties, EasyCodefServiceType serviceType) {
			if (serviceType == EasyCodefServiceType.API) {
				return new Snapshot(serviceType, properties.getClientId(), properties.getClientSecret(),
					properties.getPublicKey());
			}
			if (serviceType == EasyCodefServiceType.DEMO) {
				return new Snapshot(serviceType, properties.getDemoClientId(), properties.getDemoClientSecret(),
					properties.getPublicKey());
			}
			throw CodefException.from(CodefError.EMPTY_SERVICE_TYPE);
		}

		@Override
		public boolean equals(Object object) {
			if (this == object) {
				return true;
			}

			if (!(object instanceof Snapshot)) {
				return false;
			}

			Snapshot other = (Snapshot)object;
			return serviceType == other.serviceType
				&& Objects.equals(clientId, other.clientId)
				&& Objects.equals(clientSecret, other.clientSecret)
				&& Objects.equals(publicKey, other.publicKey);
		}

		@Override
		public int hashCode() {
			return Objects.hash(serviceType, clientId, clientSecret, publicKey);
		}
	}
}
