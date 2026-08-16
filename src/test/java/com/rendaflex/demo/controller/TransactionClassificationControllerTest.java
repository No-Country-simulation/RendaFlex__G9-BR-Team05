package com.rendaflex.demo.controller;

import com.rendaflex.demo.dto.response.ClassifiedTransaction;
import com.rendaflex.demo.dto.response.TransactionClassificationResponse;
import com.rendaflex.demo.enums.TransactionCategory;
import com.rendaflex.demo.enums.TransactionType;
import com.rendaflex.demo.exception.ApiErrorCode;
import com.rendaflex.demo.exception.ModelServiceException;
import com.rendaflex.demo.service.TransactionClassificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
class TransactionClassificationControllerTest {

    private static final String ALLOWED_ORIGIN = "http://localhost:5173";

    @MockitoBean
    private TransactionClassificationService service;

    @Autowired
    private WebApplicationContext applicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = webAppContextSetup(applicationContext).build();
    }

    @Test
    void shouldReturnTransactionClassification() throws Exception {
        when(service.classify(any())).thenReturn(response());

        mockMvc.perform(post("/api/v1/transactions/classify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transactions[0].description").value("Supermercado"))
                .andExpect(jsonPath("$.transactions[0].type").value("EXPENSE"))
                .andExpect(jsonPath("$.transactions[0].predictedCategory").value("FOOD"))
                .andExpect(jsonPath("$.transactions[0].classificationProbability").value(0.90))
                .andExpect(jsonPath("$.categorySummary.FOOD").value(100))
                .andExpect(jsonPath("$.categoryPercentages.FOOD").value(100));

        verify(service).classify(any());
    }

    @Test
    void shouldAllowPreflightFromFrontendOrigin() throws Exception {
        mockMvc.perform(options("/api/v1/transactions/classify")
                        .header("Origin", ALLOWED_ORIGIN)
                        .header("Access-Control-Request-Method", "POST")
                        .header("Access-Control-Request-Headers", "Content-Type"))
                .andExpect(status().isOk())
                .andExpect(header().string(
                        "Access-Control-Allow-Origin",
                        ALLOWED_ORIGIN
                ))
                .andExpect(header().string(
                        "Access-Control-Allow-Methods",
                        org.hamcrest.Matchers.containsString("POST")
                ))
                .andExpect(header().string(
                        "Access-Control-Allow-Headers",
                        org.hamcrest.Matchers.containsString("Content-Type")
                ));

        verifyNoInteractions(service);
    }

    @Test
    void shouldAllowPostFromFrontendOrigin() throws Exception {
        when(service.classify(any())).thenReturn(response());

        mockMvc.perform(post("/api/v1/transactions/classify")
                        .header("Origin", ALLOWED_ORIGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest()))
                .andExpect(status().isOk())
                .andExpect(header().string(
                        "Access-Control-Allow-Origin",
                        ALLOWED_ORIGIN
                ))
                .andExpect(jsonPath("$.transactions[0].predictedCategory")
                        .value("FOOD"));

        verify(service).classify(any());
    }

    @Test
    void shouldRejectPreflightFromUnauthorizedOrigin() throws Exception {
        mockMvc.perform(options("/api/v1/transactions/classify")
                        .header("Origin", "http://localhost:9999")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist(
                        "Access-Control-Allow-Origin"
                ));

        verifyNoInteractions(service);
    }

    @Test
    void shouldReturnValidationErrorForEmptyTransactions() throws Exception {
        mockMvc.perform(post("/api/v1/transactions/classify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "transactions": []
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message")
                        .value("Existem campos inválidos na requisição."))
                .andExpect(jsonPath(
                        "$.fieldErrors[?(@.field == 'transactions')]"
                ).isNotEmpty());

        verify(service, never()).classify(any());
    }

    @Test
    void shouldReturnValidationErrorForBlankDescription() throws Exception {
        mockMvc.perform(post("/api/v1/transactions/classify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "transactions": [
                                    {
                                      "description": " ",
                                      "amount": 100,
                                      "type": "EXPENSE"
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message")
                        .value("Existem campos inválidos na requisição."));

        verify(service, never()).classify(any());
    }

    @Test
    void shouldReturnValidationErrorForZeroAmount() throws Exception {
        mockMvc.perform(post("/api/v1/transactions/classify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "transactions": [
                                    {
                                      "description": "Supermercado",
                                      "amount": 0,
                                      "type": "EXPENSE"
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));

        verify(service, never()).classify(any());
    }

    @Test
    void shouldReturnValidationErrorForNegativeAmount() throws Exception {
        mockMvc.perform(post("/api/v1/transactions/classify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "transactions": [
                                    {
                                      "description": "Supermercado",
                                      "amount": -1,
                                      "type": "EXPENSE"
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));

        verify(service, never()).classify(any());
    }

    @Test
    void shouldReturnValidationErrorForInvalidEnum() throws Exception {
        mockMvc.perform(post("/api/v1/transactions/classify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest().replace(
                                "EXPENSE",
                                "INVALID"
                        )))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value(
                        "A requisição possui formato ou valores inválidos."
                ))
                .andExpect(jsonPath("$.fieldErrors").isEmpty());

        verify(service, never()).classify(any());
    }

    @Test
    void shouldReturnModelServiceUnavailable() throws Exception {
        when(service.classify(any())).thenThrow(
                new ModelServiceException(
                        ApiErrorCode.MODEL_SERVICE_UNAVAILABLE,
                        "Serviço de classificação indisponível."
                )
        );

        mockMvc.perform(post("/api/v1/transactions/classify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest()))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.code")
                        .value("MODEL_SERVICE_UNAVAILABLE"));
    }

    @Test
    void shouldReturnSafeInternalError() throws Exception {
        String technicalMessage = "Detalhe técnico confidencial";

        when(service.classify(any()))
                .thenThrow(new RuntimeException(technicalMessage));

        mockMvc.perform(post("/api/v1/transactions/classify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest()))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("INTERNAL_ERROR"))
                .andExpect(jsonPath("$.message").value(
                        "Ocorreu um erro interno inesperado."
                ))
                .andExpect(content().string(
                        org.hamcrest.Matchers.not(
                                org.hamcrest.Matchers.containsString(
                                        technicalMessage
                                )
                        )
                ));
    }

    @Test
    void shouldAcceptTransactionWithDescriptionOnly() throws Exception {
        when(service.classify(any())).thenReturn(response());

        mockMvc.perform(post("/api/v1/transactions/classify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "transactions": [
                                    {
                                      "description": "Netflix"
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isOk());

        verify(service).classify(any());
    }

    @Test
    void shouldReturnValidationErrorForNullTransactionItem() throws Exception {
        mockMvc.perform(post("/api/v1/transactions/classify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "transactions": [
                                    null
                                  ]
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message")
                        .value("Existem campos inválidos na requisição."));

        verify(service, never()).classify(any());
    }

    private TransactionClassificationResponse response() {
        return new TransactionClassificationResponse(
                List.of(new ClassifiedTransaction(
                        "Supermercado",
                        new BigDecimal("100.00"),
                        LocalDate.of(2026, 8, 14),
                        TransactionType.EXPENSE,
                        TransactionCategory.FOOD,
                        new BigDecimal("0.90")
                )),
                Map.of(
                        TransactionCategory.FOOD,
                        new BigDecimal("100.00")
                ),
                Map.of(
                        TransactionCategory.FOOD,
                        new BigDecimal("100.00")
                )
        );
    }

    private String validRequest() {
        return """
                {
                  "transactions": [
                    {
                      "description": "Supermercado",
                      "amount": 100,
                      "date": "2026-08-14",
                      "type": "EXPENSE"
                    }
                  ]
                }
                """;
    }
}
