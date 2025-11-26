package io.codef.api.http;

public class HttpClientFactory {
    private static volatile HttpClient instance;

    public static HttpClient getInstance() {
        if (instance == null) {
            synchronized (HttpClientFactory.class) {
                if (instance == null) {
                    instance = new ApacheHttpClient();
                }
            }
        }
        return instance;
    }
}
