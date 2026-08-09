package com.rendaflex.demo.config;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PythonServicePropertiesTest {

    @Test
    void shouldUseContractV2IntegrationDefaults() {
        PythonServiceProperties properties = new PythonServiceProperties();

        assertThat(properties.getBaseUrl()).isEqualTo(URI.create("http://localhost:8000"));
        assertThat(properties.getConnectTimeout()).isEqualTo(Duration.ofSeconds(3));
        assertThat(properties.getResponseTimeout()).isEqualTo(Duration.ofSeconds(15));
        assertThat(properties.getMaximumRetries()).isEqualTo(1);
    }

    @Test
    void shouldRejectNonHttpBaseUrl() {
        PythonServiceProperties properties = new PythonServiceProperties();

        assertThatThrownBy(() -> properties.setBaseUrl(URI.create("file:///tmp/python")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectNonPositiveTimeouts() {
        PythonServiceProperties properties = new PythonServiceProperties();

        assertThatThrownBy(() -> properties.setConnectTimeout(Duration.ZERO))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> properties.setResponseTimeout(Duration.ofSeconds(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectNegativeRetryCount() {
        PythonServiceProperties properties = new PythonServiceProperties();

        assertThatThrownBy(() -> properties.setMaximumRetries(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
