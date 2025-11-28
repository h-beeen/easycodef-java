package io.codef.api.constants;

public enum CodefServiceType {
	DEMO(CodefHost.DEMO_DOMAIN),
	API(CodefHost.API_DOMAIN);
	
	private final String serviceType;
	
	CodefServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceType() {
		return serviceType;
	}
}
