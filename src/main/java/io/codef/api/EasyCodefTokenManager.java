package io.codef.api;

import java.util.EnumMap;
import java.util.Map;

public class EasyCodefTokenManager {

    private final EasyCodefProperties properties;
    private final Map<EasyCodefServiceType, EasyCodefToken> tokens = new EnumMap<>(EasyCodefServiceType.class);

    public EasyCodefTokenManager(EasyCodefProperties properties) {
        this.properties = properties;
    }

    public String getAccessToken(EasyCodefServiceType serviceType) {
        EasyCodefToken token = getOrCreateToken(serviceType).validateAndRefreshToken();
        return token.getAccessToken();
    }

    private EasyCodefToken getOrCreateToken(EasyCodefServiceType serviceType) {
        EasyCodefToken token = tokens.get(serviceType);
        if (token == null) {
            token = createNewToken();
            tokens.put(serviceType, token);
        }
        return token;
    }

    private EasyCodefToken createNewToken() {
        String clientId = properties.getClientId();
        String clientSecret = properties.getClientSecret();
        return new EasyCodefToken(clientId, clientSecret);
    }
}
