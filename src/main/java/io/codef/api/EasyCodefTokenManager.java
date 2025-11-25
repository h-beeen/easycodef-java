package io.codef.api;

import io.codef.api.constants.CodefServiceType;

import java.util.EnumMap;
import java.util.Map;

public class EasyCodefTokenManager {

    private final EasyCodefProperties properties;
    private final Map<CodefServiceType, EasyCodefToken> tokens = new EnumMap<>(CodefServiceType.class);

    public EasyCodefTokenManager(EasyCodefProperties properties) {
        this.properties = properties;
    }

    public String getAccessToken(CodefServiceType serviceType) {
        EasyCodefToken token = getOrCreateToken(serviceType).validateAndRefreshToken();
        return token.getAccessToken();
    }

    public String getNewAccessToken(CodefServiceType serviceType) {
        EasyCodefToken newToken = createNewToken(serviceType);
        tokens.put(serviceType, newToken);
        return newToken.getAccessToken();
    }

    private EasyCodefToken getOrCreateToken(CodefServiceType serviceType) {
        EasyCodefToken token = tokens.get(serviceType);
        if (token == null) {
            token = createNewToken(serviceType);
            tokens.put(serviceType, token);
        }
        return token;
    }

    private EasyCodefToken createNewToken(CodefServiceType serviceType) {
        String clientId = properties.getClientIdByServiceType(serviceType);
        String clientSecret = properties.getClientSecretByServiceType(serviceType);
        return new EasyCodefToken(clientId, clientSecret);
    }
}
