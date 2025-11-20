package io.codef.api;

public enum EasyCodefServiceType {
	DEMO(1),
	API(0);
	
	private final int serviceType;
	
	EasyCodefServiceType(int serviceType) {
		this.serviceType = serviceType;
	}

	public int getServiceType() {
		return serviceType;
	}
}
