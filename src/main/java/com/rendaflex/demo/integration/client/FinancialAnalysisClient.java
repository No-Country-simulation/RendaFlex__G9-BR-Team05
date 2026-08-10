package com.rendaflex.demo.integration.client;

import com.rendaflex.demo.config.PythonServiceClientConfig;
import com.rendaflex.demo.config.PythonServiceProperties;
import com.rendaflex.demo.dto.internal.InternalFinancialAnalysisRequest;
import com.rendaflex.demo.dto.internal.InternalFinancialAnalysisResponse;
import com.rendaflex.demo.exception.ApiErrorCode;
import com.rendaflex.demo.exception.BusinessRuleException;
import com.rendaflex.demo.exception.ModelServiceException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.SocketTimeoutException;
import java.net.http.HttpTimeoutException;
import java.util.Objects;

@Component
public class FinancialAnalysisClient {

    static final String FINANCIAL_ANALYSIS_PATH = "/internal/v1/financial-analyses";

    private final RestClient restClient;
    private final int maximumRetries;

    public FinancialAnalysisClient(
            @Qualifier(PythonServiceClientConfig.PYTHON_SERVICE_REST_CLIENT) RestClient restClient,
            PythonServiceProperties properties
    ) {
        this.restClient = Objects.requireNonNull(restClient, "RestClient must not be null.");
        Objects.requireNonNull(properties, "PythonServiceProperties must not be null.");
        this.maximumRetries = properties.getMaximumRetries();
    }

    public InternalFinancialAnalysisResponse analyze(InternalFinancialAnalysisRequest request) {
        Objects.requireNonNull(request, "InternalFinancialAnalysisRequest must not be null.");

        int attempt = 0;
        while (true) {
            try {
                InternalFinancialAnalysisResponse response = restClient.post()
                        .uri(FINANCIAL_ANALYSIS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .body(request)
                        .retrieve()
                        .body(InternalFinancialAnalysisResponse.class);

                if (response == null) {
                    throw invalidResponse();
                }

                return response;
            } catch (HttpStatusCodeException exception) {
                int status = exception.getStatusCode().value();
                if (isRetryableStatus(status) && attempt < maximumRetries) {
                    attempt++;
                    continue;
                }
                throw mapHttpFailure(status);
            } catch (ResourceAccessException exception) {
                boolean timeout = isTimeout(exception);
                if (attempt < maximumRetries) {
                    attempt++;
                    continue;
                }
                throw timeout ? timeout() : unavailable();
            } catch (ModelServiceException | BusinessRuleException exception) {
                throw exception;
            } catch (RestClientException exception) {
                throw invalidResponse();
            }
        }
    }

    private RuntimeException mapHttpFailure(int status) {
        return switch (status) {
            case 400 -> invalidResponse();
            case 422 -> new BusinessRuleException(
                    "Não foi possível concluir a análise financeira com os dados informados."
            );
            case 500, 502, 503, 504 -> unavailable();
            default -> status >= 500 ? unavailable() : invalidResponse();
        };
    }

    private boolean isRetryableStatus(int status) {
        return status == 502 || status == 503 || status == 504;
    }

    private boolean isTimeout(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            if (current instanceof HttpTimeoutException || current instanceof SocketTimeoutException) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }

    private ModelServiceException invalidResponse() {
        return new ModelServiceException(
                ApiErrorCode.MODEL_SERVICE_INVALID_RESPONSE,
                "O serviço de análise retornou uma resposta inválida."
        );
    }

    private ModelServiceException unavailable() {
        return new ModelServiceException(
                ApiErrorCode.MODEL_SERVICE_UNAVAILABLE,
                "O serviço de análise financeira está temporariamente indisponível."
        );
    }

    private ModelServiceException timeout() {
        return new ModelServiceException(
                ApiErrorCode.MODEL_SERVICE_TIMEOUT,
                "O serviço de análise financeira demorou mais do que o esperado para responder."
        );
    }
}
