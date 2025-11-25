package io.codef.api;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import io.codef.api.constants.EasyCodefConstant;
import io.codef.api.dto.EasyCodefResponse;
import org.apache.commons.codec.binary.Base64;

import static io.codef.api.EasyCodefUtil.mapper;
import static io.codef.api.EasyCodefUtil.mapTypeRef;

public class EasyCodefToken {

    private final String oauthToken;
    private String accessToken;
    private LocalDateTime expiresAt;

    protected EasyCodefToken(String clientId, String clientSecret) {
        this.oauthToken = createOAuthToken(clientId, clientSecret);

        EasyCodefResponse response = EasyCodefConnector.publishToken(oauthToken);
        initializeToken(response);
    }

    protected EasyCodefToken validateAndRefreshToken() {
        Optional.of(expiresAt).filter(this::isTokenExpiringSoon)
                .ifPresent(expiry -> refreshToken());

        return this;
    }

    protected String getAccessToken() {
        return accessToken;
    }

    private String createOAuthToken(String clientId, String clientSecret) {
        String auth = clientId + ":" + clientSecret;
        byte[] authEncBytes = Base64.encodeBase64(auth.getBytes());
        return "Basic " + new String(authEncBytes);
    }

    private void initializeToken(EasyCodefResponse response) {
        Object data = response.getData();

        Map<String, Object> tokenMap = mapper().convertValue(data, mapTypeRef());

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
