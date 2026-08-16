package com.rendaflex.demo.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.HttpClientSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(PythonServiceProperties.class)
public class PythonServiceClientConfig {

    public static final String PYTHON_SERVICE_REST_CLIENT = "pythonServiceRestClient";

    @Bean(PYTHON_SERVICE_REST_CLIENT)
    RestClient pythonServiceRestClient(
            RestClient.Builder restClientBuilder,
            PythonServiceProperties properties
    ) {
        HttpClientSettings settings = HttpClientSettings.defaults()
                .withTimeouts(properties.getConnectTimeout(), properties.getResponseTimeout());

        ClientHttpRequestFactory requestFactory = ClientHttpRequestFactoryBuilder.jdk().build(settings);

        return restClientBuilder.clone()
                .baseUrl(properties.getBaseUrl().toString())
                .requestFactory(requestFactory)
                .build();
    }
}
