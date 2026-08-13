package com.rendaflex.demo.integration.client;

import com.rendaflex.demo.config.PythonServiceProperties;
import com.rendaflex.demo.dto.internal.InternalClassificationTransaction;
import com.rendaflex.demo.dto.internal.InternalTransactionClassificationRequest;
import com.rendaflex.demo.dto.internal.InternalTransactionClassificationResponse;
import com.rendaflex.demo.enums.TransactionCategory;
import com.rendaflex.demo.exception.ApiErrorCode;
import com.rendaflex.demo.exception.BusinessRuleException;
import com.rendaflex.demo.exception.ModelServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withException;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class TransactionClassificationClientTest {

    private static final String BASE_URL = "http://python.test";
    private static final String ENDPOINT =
            BASE_URL + TransactionClassificationClient.TRANSACTION_CLASSIFICATION_PATH;

    private MockRestServiceServer server;
    private TransactionClassificationClient client;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder().baseUrl(BASE_URL);
        server = MockRestServiceServer.bindTo(builder).build();

        PythonServiceProperties properties = new PythonServiceProperties();
        properties.setBaseUrl(URI.create(BASE_URL));
        properties.setMaximumRetries(1);

        client = new TransactionClassificationClient(builder.build(), properties);
    }

    @Test
    void shouldCallInternalClassificationEndpointAndDeserializeResponse() {
        server.expect(requestTo(ENDPOINT))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedRequestWithAmountJson()))
                .andRespond(withSuccess(validResponseJson(), MediaType.APPLICATION_JSON));

        InternalTransactionClassificationResponse response = client.classify(requestWithAmount());

        assertThat(response.transactions()).hasSize(1);
        assertThat(response.transactions().get(0).sourceIndex()).isZero();
        assertThat(response.transactions().get(0).predictedCategory())
                .isEqualTo(TransactionCategory.SERVICES);
        assertThat(response.transactions().get(0).classificationProbability())
                .isEqualByComparingTo("0.99");

        server.verify();
    }

    @Test
    void shouldOmitNullAmountFromRequestPayload() {
        server.expect(requestTo(ENDPOINT))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string(not(containsString("\"amount\""))))
                .andRespond(withSuccess(validResponseJson(), MediaType.APPLICATION_JSON));

        client.classify(requestWithoutAmount());

        server.verify();
    }

    @Test
    void shouldRetryOnceFor503AndThenReturnSuccess() {
        server.expect(requestTo(ENDPOINT))
                .andRespond(withStatus(HttpStatus.SERVICE_UNAVAILABLE));

        server.expect(requestTo(ENDPOINT))
                .andRespond(withSuccess(validResponseJson(), MediaType.APPLICATION_JSON));

        InternalTransactionClassificationResponse response = client.classify(requestWithAmount());

        assertThat(response.transactions()).hasSize(1);
        server.verify();
    }

    @Test
    void shouldRetryTimeoutOnceAndThenMapToTimeout() {
        server.expect(requestTo(ENDPOINT))
                .andRespond(withException(new SocketTimeoutException("timeout")));

        server.expect(requestTo(ENDPOINT))
                .andRespond(withException(new SocketTimeoutException("timeout")));

        assertThatThrownBy(() -> client.classify(requestWithAmount()))
                .isInstanceOfSatisfying(ModelServiceException.class, exception ->
                        assertThat(exception.getCode())
                                .isEqualTo(ApiErrorCode.MODEL_SERVICE_TIMEOUT)
                );

        server.verify();
    }

    @Test
    void shouldRetryConnectionFailureOnceAndThenMapToUnavailable() {
        server.expect(requestTo(ENDPOINT))
                .andRespond(withException(new ConnectException("connection refused")));

        server.expect(requestTo(ENDPOINT))
                .andRespond(withException(new ConnectException("connection refused")));

        assertThatThrownBy(() -> client.classify(requestWithAmount()))
                .isInstanceOfSatisfying(ModelServiceException.class, exception ->
                        assertThat(exception.getCode())
                                .isEqualTo(ApiErrorCode.MODEL_SERVICE_UNAVAILABLE)
                );

        server.verify();
    }

    @Test
    void shouldNotRetry500AndShouldMapToUnavailable() {
        server.expect(requestTo(ENDPOINT))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThatThrownBy(() -> client.classify(requestWithAmount()))
                .isInstanceOfSatisfying(ModelServiceException.class, exception ->
                        assertThat(exception.getCode())
                                .isEqualTo(ApiErrorCode.MODEL_SERVICE_UNAVAILABLE)
                );

        server.verify();
    }

    @Test
    void shouldMap400ToInvalidResponseWithoutRetry() {
        server.expect(requestTo(ENDPOINT))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        assertThatThrownBy(() -> client.classify(requestWithAmount()))
                .isInstanceOfSatisfying(ModelServiceException.class, exception ->
                        assertThat(exception.getCode())
                                .isEqualTo(ApiErrorCode.MODEL_SERVICE_INVALID_RESPONSE)
                );

        server.verify();
    }

    @Test
    void shouldMap422ToBusinessRuleWithoutRetry() {
        server.expect(requestTo(ENDPOINT))
                .andRespond(withStatus(HttpStatus.UNPROCESSABLE_ENTITY));

        assertThatThrownBy(() -> client.classify(requestWithAmount()))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Não foi possível concluir a classificação de transações com os dados informados.");

        server.verify();
    }

    @Test
    void shouldMapMalformed200ResponseToInvalidResponse() {
        server.expect(requestTo(ENDPOINT))
                .andRespond(withSuccess("{not-valid-json", MediaType.APPLICATION_JSON));

        assertThatThrownBy(() -> client.classify(requestWithAmount()))
                .isInstanceOfSatisfying(ModelServiceException.class, exception ->
                        assertThat(exception.getCode())
                                .isEqualTo(ApiErrorCode.MODEL_SERVICE_INVALID_RESPONSE)
                );

        server.verify();
    }

    @Test
    void shouldRejectNullRequestBeforeCallingPython() {
        assertThatThrownBy(() -> client.classify(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("InternalTransactionClassificationRequest must not be null.");

        server.verify();
    }

    private InternalTransactionClassificationRequest requestWithAmount() {
        return new InternalTransactionClassificationRequest(
                List.of(new InternalClassificationTransaction(
                        0,
                        "Netflix",
                        new BigDecimal("45.90")
                ))
        );
    }

    private InternalTransactionClassificationRequest requestWithoutAmount() {
        return new InternalTransactionClassificationRequest(
                List.of(new InternalClassificationTransaction(
                        0,
                        "Netflix",
                        null
                ))
        );
    }

    private String expectedRequestWithAmountJson() {
        return """
                {
                  "transactions": [
                    {
                      "sourceIndex": 0,
                      "description": "Netflix",
                      "amount": 45.90
                    }
                  ]
                }
                """;
    }

    private String validResponseJson() {
        return """
                {
                  "transactions": [
                    {
                      "sourceIndex": 0,
                      "predictedCategory": "SERVICES",
                      "classificationProbability": 0.99
                    }
                  ]
                }
                """;
    }
}
