package io.codef.api.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import static io.codef.api.constants.HttpConstant.*;

public class HttpClientFactory {

    private static volatile HttpClient instance;

    public static HttpClient getInstance() {
        if (instance == null) {
            synchronized (HttpClientFactory.class) {
                if (instance == null) {
                    instance = createPooledHttpClient();
                }
            }
        }
        return instance;
    }

    private static HttpClient createPooledHttpClient() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(WAIT_CONNECTION_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(REAT_TIMEOUT)
                .build();

        CloseableHttpClient pooledClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setMaxConnTotal(MAX_CONNECTIONS)
                .setMaxConnPerRoute(MAX_CONNECTIONS)
                .build();

        return new HttpClient(pooledClient);
    }
}
