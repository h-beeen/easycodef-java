package io.codef.api;

import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;

public class EasyCodefToken {

    private final String oauthToken;
    private String accessToken;
    private LocalDateTime expiresAt;

    protected EasyCodefToken(String clientId, String clientSecret) {
        final int VALIDITY_PERIOD_DAYS = 7;

        String auth = clientId + ":" + clientSecret;
        byte[] authEncBytes = Base64.encodeBase64(auth.getBytes());
        String authStringEnc = new String(authEncBytes);

        this.oauthToken = "Basic " + authStringEnc;

        this.accessToken = EasyCodefConnector.publishToken(oauthToken);

        this.expiresAt = LocalDateTime.now().plusDays(VALIDITY_PERIOD_DAYS);
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
        this.accessToken = EasyCodefConnector.publishToken(oauthToken);

        this.expiresAt = LocalDateTime.now().plusDays(7);
    }

    public String getAccessToken() {
        return accessToken;
    }
}

