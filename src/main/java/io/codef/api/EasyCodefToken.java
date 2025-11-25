package io.codef.api;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.codef.api.constants.EasyCodefConstant;
import io.codef.api.dto.EasyCodefResponse;
import org.apache.commons.codec.binary.Base64;

public class EasyCodefToken {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final TypeReference<Map<String, Object>> MAP_TYPE_REF = new TypeReference<Map<String, Object>>() {};

    private final String oauthToken;
    private String accessToken;
    private LocalDateTime expiresAt;

    EasyCodefToken(String clientId, String clientSecret) {
        this.oauthToken = createOAuthToken(clientId, clientSecret);

        EasyCodefResponse response = EasyCodefConnector.publishToken(oauthToken);
        initializeToken(response);
    }

    EasyCodefToken validateAndRefreshToken() {
        Optional.of(expiresAt).filter(this::isTokenExpiringSoon)
                .ifPresent(expiry -> refreshToken());

        return this;
    }

    String getAccessToken() {
        return accessToken;
    }

    private String createOAuthToken(String clientId, String clientSecret) {
        String auth = clientId + ":" + clientSecret;
        byte[] authEncBytes = Base64.encodeBase64(auth.getBytes());
        return "Basic " + new String(authEncBytes);
    }

    private void initializeToken(EasyCodefResponse response) {
        Object data = response.getData();

        Map<String, Object> tokenMap = MAPPER.convertValue(data, MAP_TYPE_REF);

        Optional.ofNullable(tokenMap.get(EasyCodefConstant.ACCESS_TOKEN))
                .map(String::valueOf)
                .ifPresent(token -> this.accessToken = token);

        Optional.ofNullable(tokenMap.get(EasyCodefConstant.EXPIRES_IN))
                .map(v -> Integer.parseInt(String.valueOf(v)))
                .ifPresent(exp -> this.expiresAt = LocalDateTime.now().plusSeconds(exp));
    }

    private boolean isTokenExpiringSoon(LocalDateTime expiry) {
        return expiry.isBefore(LocalDateTime.now().plusHours(24));
    }

    private void refreshToken() {
        EasyCodefResponse response = EasyCodefConnector.publishToken(oauthToken);
        initializeToken(response);
    }
}
