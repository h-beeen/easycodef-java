package io.codef.api.root;

import static io.codef.api.error.CodefError.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.codef.api.EasyCodefBuilder;
import io.codef.api.EasyCodefClient;
import io.codef.api.EasyCodefDispatcher;
import io.codef.api.EasyCodefServiceType;
import io.codef.api.EasyCodefToken;
import io.codef.api.error.CodefException;
import io.codef.api.http.CodefHttpClient;
import io.codef.api.service.EasyCodefApiService;
import io.codef.api.service.EasyCodefOAuthService;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Root Layer] EasyCodefBuilder Test")
public class EasyCodefBuilderTest {

	@Mock
	private EasyCodefToken mockToken;

	@Mock
	private EasyCodefDispatcher mockDispatcher;

	@Nested
	@DisplayName("[isSuccessResponse] 초기 설정이 정상적으로 작동하면 성공")
	class IsSuccessResponse {

		@Test
		@DisplayName("[Success] 필수 값이 모두 설정되면 Client 생성 성공")
		void build_success() throws Exception {
			EasyCodefServiceType serviceType = EasyCodefServiceType.DEMO;
			String clientId = "test-client-id";
			String clientSecret = "test-client-secret";
			String publicKey = "test-public-key";

			EasyCodefBuilder builder = new EasyCodefBuilder() {
				@Override
				protected EasyCodefToken createToken(String clientId, String clientSecret,
					EasyCodefOAuthService oAuthService) {
					return mockToken;
				}

				@Override
				protected EasyCodefDispatcher createDispatcher(EasyCodefToken token, EasyCodefServiceType serviceType,
					EasyCodefApiService apiService) {
					return mockDispatcher;
				}

				@Override
				protected CodefHttpClient createHttpClient() {
					return mock(CodefHttpClient.class);
				}
			};

			EasyCodefClient client = builder
				.serviceType(serviceType)
				.clientId(clientId)
				.clientSecret(clientSecret)
				.publicKey(publicKey)
				.build();

			assertNotNull(client);
			assertEquals(publicKey, client.getPublicKey());

			Field dispatcherField = EasyCodefClient.class.getDeclaredField("dispatcher");
			dispatcherField.setAccessible(true);
			EasyCodefDispatcher actualDispatcher = (EasyCodefDispatcher)dispatcherField.get(client);

			assertEquals(mockDispatcher, actualDispatcher);
		}
	}

	@Nested
	@DisplayName("[Throw Exception] Exception Cases")
	class ThrowsException {

		@Test
		@DisplayName("[Exception] 클라이언트 ID 누락 시 EMPTY_CLIENT_ID 예외처리")
		void build_fail_emptyClientId() {
			EasyCodefBuilder builder = EasyCodefBuilder.builder()
				.serviceType(EasyCodefServiceType.DEMO)
				.clientSecret("test-client-secret")
				.publicKey("key");

			CodefException exception = assertThrows(CodefException.class, builder::build);

			assertEquals(EMPTY_CLIENT_ID, exception.getCodefError());
		}

		@Test
		@DisplayName("[Exception] 클라이언트 시크릿 누락 시 EMPTY_CLIENT_SECRET 예외처리")
		void build_fail_emptyClientSecret() {
			EasyCodefBuilder builder = EasyCodefBuilder.builder()
				.serviceType(EasyCodefServiceType.DEMO)
				.clientId("test-client-id")
				.publicKey("key");

			CodefException exception = assertThrows(CodefException.class, builder::build);

			assertEquals(EMPTY_CLIENT_SECRET, exception.getCodefError());
		}

		@Test
		@DisplayName("[Exception] 설정 메서드에 null 전달 시 예외처리")
		void build_fail_nullCheck() {
			EasyCodefBuilder builder = EasyCodefBuilder.builder();

			assertThrows(CodefException.class, () -> builder.serviceType(null));
			assertThrows(CodefException.class, () -> builder.clientId(null));
			assertThrows(CodefException.class, () -> builder.clientSecret(null));
			assertThrows(CodefException.class, () -> builder.publicKey(null));
		}
	}
}
