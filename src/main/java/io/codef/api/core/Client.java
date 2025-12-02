package io.codef.api.core;

import io.codef.api.dto.EasyCodefResponse;

public interface Client {
	EasyCodefResponse publishToken(String basicToken);

	EasyCodefResponse requestProduct(String urlPath, String bearerToken, String jsonBody, Integer customTimeout);
}
