package io.codef.api;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

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

        HashMap<String, Object> tokenMap = EasyCodefConnector.publishToken(oauthToken);

        this.accessToken = tokenMap.get("access_token").toString();

        int exp = (int) tokenMap.get("expires_in");
        this.expiresAt = LocalDateTime.now().plusSeconds(exp);
    }

    public EasyCodefToken validateAndRefreshToken() {
        Optional.of(expiresAt).filter(this::isTokenExpiringSoon)
                .ifPresent(expiry -> refreshToken());

        return this;
    }

    private boolean isTokenExpiringSoon(LocalDateTime expiry) {
        return expiry.isBefore(LocalDateTime.now().plusHours(24));
    }

    private void refreshToken() {
        HashMap<String, Object> tokenMap = EasyCodefConnector.publishToken(oauthToken);

        this.accessToken = tokenMap.get("access_token").toString();

        int exp = (int) tokenMap.get("expires_in");
        this.expiresAt = LocalDateTime.now().plusSeconds(exp);
    }

    public String getAccessToken() {
        return accessToken;
    }
}
