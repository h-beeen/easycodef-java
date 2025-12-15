package io.codef.api;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;

final class EasyCodefClientRegistry {

	private final Map<Key, EasyCodefClient> cache = new ConcurrentHashMap<>();

	EasyCodefClient getOrCreate(EasyCodefProperties properties, EasyCodefServiceType serviceType) {
		Snapshot snapshot = Snapshot.from(properties, serviceType);
		Key key = new Key(snapshot);

		return cache.computeIfAbsent(key, k -> EasyCodefBuilder.builder()
			.serviceType(serviceType)
			.clientId(snapshot.clientId)
			.clientSecret(snapshot.clientSecret)
			.publicKey(snapshot.publicKey)
			.build());
	}

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
	}

	private static final class Key {
		final EasyCodefServiceType serviceType;
		final String clientId;
		final String clientSecret;
		final String publicKey;

		Key(Snapshot snapshot) {
			this.serviceType = snapshot.serviceType;
			this.clientId = snapshot.clientId;
			this.clientSecret = snapshot.clientSecret;
			this.publicKey = snapshot.publicKey;
		}

		@Override
		public boolean equals(Object object) {
			if (this == object) {
				return true;
			}

			if (!(object instanceof Key)) {
				return false;
			}

			Key key = (Key)object;
			return serviceType == key.serviceType
				&& Objects.equals(clientId, key.clientId)
				&& Objects.equals(clientSecret, key.clientSecret)
				&& Objects.equals(publicKey, key.publicKey);
		}

		@Override
		public int hashCode() {
			return Objects.hash(serviceType, clientId, clientSecret, publicKey);
		}
	}
}
