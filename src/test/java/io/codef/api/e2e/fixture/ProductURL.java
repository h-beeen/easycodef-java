package io.codef.api.e2e.fixture;

public enum ProductURL {

	FLOODED_VEHICLE("/v1/kr/etc/mt/car-history/flooded-vehicle"),
	FLOODED_VEHICLE_WRONG("/v1/kr/etc/mt/car-history/wrong-vehicle");

	private final String url;

	ProductURL(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
