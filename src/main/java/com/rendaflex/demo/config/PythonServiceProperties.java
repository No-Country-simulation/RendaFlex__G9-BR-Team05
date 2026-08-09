package com.rendaflex.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;
import java.time.Duration;
import java.util.Objects;

@ConfigurationProperties(prefix = "rendaflex.python")
public class PythonServiceProperties {

    private URI baseUrl = URI.create("http://localhost:8000");
    private Duration connectTimeout = Duration.ofSeconds(3);
    private Duration responseTimeout = Duration.ofSeconds(15);
    private int maximumRetries = 1;

    public URI getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(URI baseUrl) {
        Objects.requireNonNull(baseUrl, "Python service base URL must not be null.");
        String scheme = baseUrl.getScheme();
        if (!baseUrl.isAbsolute() || !("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme))) {
            throw new IllegalArgumentException("Python service base URL must use HTTP or HTTPS.");
        }
        this.baseUrl = baseUrl;
    }

    public Duration getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Duration connectTimeout) {
        this.connectTimeout = requirePositive(connectTimeout, "Connect timeout");
    }

    public Duration getResponseTimeout() {
        return responseTimeout;
    }

    public void setResponseTimeout(Duration responseTimeout) {
        this.responseTimeout = requirePositive(responseTimeout, "Response timeout");
    }

    public int getMaximumRetries() {
        return maximumRetries;
    }

    public void setMaximumRetries(int maximumRetries) {
        if (maximumRetries < 0) {
            throw new IllegalArgumentException("Maximum retries must be greater than or equal to zero.");
        }
        this.maximumRetries = maximumRetries;
    }

    private Duration requirePositive(Duration duration, String fieldName) {
        Objects.requireNonNull(duration, fieldName + " must not be null.");
        if (duration.isZero() || duration.isNegative()) {
            throw new IllegalArgumentException(fieldName + " must be greater than zero.");
        }
        return duration;
    }
}
