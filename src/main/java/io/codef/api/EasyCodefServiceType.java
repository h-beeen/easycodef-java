package io.codef.api;

public enum EasyCodefServiceType {
	DEMO(EasyCodefConstant.DEMO_DOMAIN),
	API(EasyCodefConstant.API_DOMAIN);
	
	private final String serviceType;
	
	EasyCodefServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceType() {
		return serviceType;
	}
}
