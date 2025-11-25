package io.codef.api;

import io.codef.api.constants.EasyCodefServiceType;

public class EasyCodefProperties {
	
	private String demoClientId 	= "";
	
	private String demoClientSecret 	= "";
	
	private String clientId 	= "";
	
	private String clientSecret 	= "";
	
	private String publicKey 	= "";


	protected void setClientInfo(String clientId, String clientSecret) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	protected void setClientInfoForDemo(String demoClientId, String demoClientSecret) {
		this.demoClientId = demoClientId;
		this.demoClientSecret = demoClientSecret;
	}

	protected String getPublicKey() {
		return publicKey;
	}

	protected void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

    protected boolean checkClientInfo(EasyCodefServiceType serviceType) {
        if(serviceType.isApiService()) {
            if(clientId == null || clientId.trim().isEmpty()) {
                return true;
            }
            return clientSecret == null || clientSecret.trim().isEmpty();
        } else {
            if(demoClientId == null || demoClientId.trim().isEmpty()) {
                return true;
            }
            return demoClientSecret == null || demoClientSecret.trim().isEmpty();
        }
    }

    protected boolean checkPublicKey() {
        return publicKey == null || publicKey.trim().isEmpty();
    }

    protected String getClientIdByServiceType(EasyCodefServiceType serviceType) {
        return serviceType.isApiService() ? clientId : demoClientId;
    }

    protected String getClientSecretByServiceType(EasyCodefServiceType serviceType) {
        return serviceType.isApiService() ? clientSecret : demoClientSecret;
    }
}
