package io.codef.api;

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
        String id = getClientIdByServiceType(serviceType);
        String secret = getClientSecretByServiceType(serviceType);
        return isNullOrEmpty(id) || isNullOrEmpty(secret);
    }

    protected boolean checkPublicKey() {
        return isNullOrEmpty(publicKey);
    }

    protected String getClientIdByServiceType(EasyCodefServiceType serviceType) {
        return serviceType.isApiService() ? clientId : demoClientId;
    }

    protected String getClientSecretByServiceType(EasyCodefServiceType serviceType) {
        return serviceType.isApiService() ? clientSecret : demoClientSecret;
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
