package io.codef.api;

import io.codef.api.constants.EasyCodefServiceType;

public class EasyCodefProperties {
	
	private String demoClientId 	= "";
	
	private String demoClientSecret 	= "";
	
	private String clientId 	= "";
	
	private String clientSecret 	= "";
	
	private String publicKey 	= "";


	public void setClientInfo(String clientId, String clientSecret) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	public void setClientInfoForDemo(String demoClientId, String demoClientSecret) {
		this.demoClientId = demoClientId;
		this.demoClientSecret = demoClientSecret;
	}

	public String getDemoClientId() {
		return demoClientId;
	}

	public String getDemoClientSecret() {
		return demoClientSecret;
	}

	public String getClientId() {
		return clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

    public boolean checkClientInfo(EasyCodefServiceType serviceType) {
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

    public boolean checkPublicKey() {
        return publicKey == null || publicKey.trim().isEmpty();
    }

    public String getClientIdByServiceType(EasyCodefServiceType serviceType) {
        return serviceType.isApiService() ? clientId : demoClientId;
    }

    public String getClientSecretByServiceType(EasyCodefServiceType serviceType) {
        return serviceType.isApiService() ? clientSecret : demoClientSecret;
    }
}
