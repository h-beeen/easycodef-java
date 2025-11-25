package io.codef.api;

import io.codef.api.constants.EasyCodefServiceType;

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

    public String getNewAccessToken(EasyCodefServiceType serviceType) {
        EasyCodefToken newToken = createNewToken(serviceType);
        tokens.put(serviceType, newToken);
        return newToken.getAccessToken();
    }

    private EasyCodefToken getOrCreateToken(EasyCodefServiceType serviceType) {
        EasyCodefToken token = tokens.get(serviceType);
        if (token == null) {
            token = createNewToken(serviceType);
            tokens.put(serviceType, token);
        }
        return token;
    }

    private EasyCodefToken createNewToken(EasyCodefServiceType serviceType) {
        String clientId = properties.getClientIdByServiceType(serviceType);
        String clientSecret = properties.getClientSecretByServiceType(serviceType);
        return new EasyCodefToken(clientId, clientSecret);
    }
}
