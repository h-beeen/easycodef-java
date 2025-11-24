package io.codef.api.constants;

public enum EasyCodefServiceType {
	DEMO(CodefHost.DEMO_DOMAIN),
	API(CodefHost.API_DOMAIN);
	
	private final String serviceType;
	
	EasyCodefServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceType() {
		return serviceType;
	}
}
