package io.codef.api;

import java.time.LocalDateTime;
import java.util.Map;

import io.codef.api.dto.EasyCodefResponse;
import org.apache.commons.codec.binary.Base64;

public class EasyCodefToken {

    private final String oauthToken;
    private String accessToken;
    private LocalDateTime expiresAt;

    protected EasyCodefToken(String clientId, String clientSecret) {
        String auth = clientId + ":" + clientSecret;
        byte[] authEncBytes = Base64.encodeBase64(auth.getBytes());
        String authStringEnc = new String(authEncBytes);

        this.oauthToken = "Basic " + authStringEnc;

        EasyCodefResponse response = EasyCodefConnector.publishToken(oauthToken);
        initializeTokenFromResponse(response);
    }

    @SuppressWarnings("unchecked")
    private void initializeTokenFromResponse(EasyCodefResponse response) {
        if (response == null || response.getData() == null) {
            return;
        }

        // 토큰 응답은 data에 직접 토큰 정보가 포함됨
        if (response.getData() instanceof Map) {
            Map<String, Object> tokenMap = (Map<String, Object>) response.getData();

            Object accessTokenObj = tokenMap.get("access_token");
            Object expiresInObj = tokenMap.get("expires_in");

            if (accessTokenObj != null) {
                this.accessToken = accessTokenObj.toString();
            }

            if (expiresInObj != null) {
                int exp = ((Number) expiresInObj).intValue();
                this.expiresAt = LocalDateTime.now().plusSeconds(exp);
            }
        }
    }

    public EasyCodefToken validateAndRefreshToken() {
        if (expiresAt != null && isTokenExpiringSoon(expiresAt)) {
            refreshToken();
        }
        return this;
    }

    private boolean isTokenExpiringSoon(LocalDateTime expiry) {
        return expiry.isBefore(LocalDateTime.now().plusHours(24));
    }

    private void refreshToken() {
        EasyCodefResponse response = EasyCodefConnector.publishToken(oauthToken);
        initializeTokenFromResponse(response);
    }

    public String getAccessToken() {
        return accessToken;
    }
}
