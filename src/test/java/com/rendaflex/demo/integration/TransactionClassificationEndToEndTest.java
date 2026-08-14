package com.rendaflex.demo.integration;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
class TransactionClassificationEndToEndTest {

    private static final String INTERNAL_PATH =
            "/internal/v1/transactions/classify";

    private static final String SUCCESS_RESPONSE = """
            {
              "transactions": [
                {
                  "sourceIndex": 1,
                  "predictedCategory": "TRANSPORT",
                  "classificationProbability": 0.98
                },
                {
                  "sourceIndex": 2,
                  "predictedCategory": "ENTERTAINMENT",
                  "classificationProbability": 0.91
                }
              ]
            }
            """;

    private static final String MODEL_ERROR_RESPONSE = """
            {
              "code": "MODEL_INTERNAL_ERROR",
              "message": "Internal model failure.",
              "fieldErrors": []
            }
            """;

    private static final AtomicReference<String> LAST_INTERNAL_REQUEST =
            new AtomicReference<>();

    private static final AtomicInteger INTERNAL_REQUEST_COUNT =
            new AtomicInteger();

    private static final AtomicInteger STUB_STATUS =
            new AtomicInteger(200);

    private static final AtomicReference<String> STUB_BODY =
            new AtomicReference<>(SUCCESS_RESPONSE);

    private static final HttpServer PYTHON_STUB = startPythonStub();

    @Autowired
    private WebApplicationContext applicationContext;

    private MockMvc mockMvc;

    @DynamicPropertySource
    static void pythonServiceProperties(DynamicPropertyRegistry registry) {
        registry.add(
                "rendaflex.python.base-url",
                () -> "http://127.0.0.1:"
                        + PYTHON_STUB.getAddress().getPort()
        );
    }

    @BeforeEach
    void setUp() {
        mockMvc = webAppContextSetup(applicationContext).build();

        LAST_INTERNAL_REQUEST.set(null);
        INTERNAL_REQUEST_COUNT.set(0);
        STUB_STATUS.set(200);
        STUB_BODY.set(SUCCESS_RESPONSE);
    }

    @AfterAll
    static void stopPythonStub() {
        PYTHON_STUB.stop(0);
    }

    @Test
    void shouldCompleteTransactionClassificationThroughRealSpringHttpBoundary()
            throws Exception {

        mockMvc.perform(post("/api/v1/transactions/classify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(publicRequest()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactions[0].description")
                        .value("Client payment"))
                .andExpect(jsonPath("$.transactions[0].type")
                        .value("INCOME"))
                .andExpect(jsonPath(
                        "$.transactions[0].predictedCategory"
                ).doesNotExist())
                .andExpect(jsonPath(
                        "$.transactions[0].classificationProbability"
                ).doesNotExist())
                .andExpect(jsonPath("$.transactions[1].description")
                        .value("Uber"))
                .andExpect(jsonPath("$.transactions[1].type")
                        .value("EXPENSE"))
                .andExpect(jsonPath(
                        "$.transactions[1].predictedCategory"
                ).value("TRANSPORT"))
                .andExpect(jsonPath(
                        "$.transactions[1].classificationProbability"
                ).value(0.98))
                .andExpect(jsonPath("$.transactions[2].description")
                        .value("Netflix"))
                .andExpect(jsonPath("$.transactions[2].type")
                        .doesNotExist())
                .andExpect(jsonPath(
                        "$.transactions[2].predictedCategory"
                ).value("ENTERTAINMENT"))
                .andExpect(jsonPath(
                        "$.transactions[2].classificationProbability"
                ).value(0.91))
                .andExpect(jsonPath("$.categorySummary.TRANSPORT")
                        .value(60.00))
                .andExpect(jsonPath("$.categorySummary.ENTERTAINMENT")
                        .value(40.00))
                .andExpect(jsonPath("$.categoryPercentages.TRANSPORT")
                        .value(60.00))
                .andExpect(jsonPath(
                        "$.categoryPercentages.ENTERTAINMENT"
                ).value(40.00));

        String internalRequest = LAST_INTERNAL_REQUEST.get();

        assertNotNull(internalRequest);
        assertEquals(1, INTERNAL_REQUEST_COUNT.get());

        assertTrue(internalRequest.contains("\"sourceIndex\":1"));
        assertTrue(internalRequest.contains("\"description\":\"Uber\""));

        assertTrue(internalRequest.contains("\"sourceIndex\":2"));
        assertTrue(internalRequest.contains("\"description\":\"Netflix\""));

        assertFalse(internalRequest.contains("Client payment"));
    }

    @Test
    void shouldSkipPythonWhenAllTransactionsAreIncome() throws Exception {
        mockMvc.perform(post("/api/v1/transactions/classify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(allIncomeRequest()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactions[0].description")
                        .value("Salary"))
                .andExpect(jsonPath("$.transactions[0].type")
                        .value("INCOME"))
                .andExpect(jsonPath(
                        "$.transactions[0].predictedCategory"
                ).doesNotExist())
                .andExpect(jsonPath(
                        "$.transactions[0].classificationProbability"
                ).doesNotExist())
                .andExpect(jsonPath("$.categorySummary").isEmpty())
                .andExpect(jsonPath("$.categoryPercentages").isEmpty());

        assertEquals(0, INTERNAL_REQUEST_COUNT.get());
        assertNull(LAST_INTERNAL_REQUEST.get());
    }

    @Test
    void shouldMapPythonUnavailabilityToSafePublicErrorAfterRetry()
            throws Exception {

        STUB_STATUS.set(503);
        STUB_BODY.set(MODEL_ERROR_RESPONSE);

        mockMvc.perform(post("/api/v1/transactions/classify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(publicRequest()))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.code")
                        .value("MODEL_SERVICE_UNAVAILABLE"));

        assertEquals(2, INTERNAL_REQUEST_COUNT.get());
    }

    private static HttpServer startPythonStub() {
        try {
            HttpServer server = HttpServer.create(
                    new InetSocketAddress("127.0.0.1", 0),
                    0
            );

            server.createContext(
                    INTERNAL_PATH,
                    TransactionClassificationEndToEndTest::handlePythonRequest
            );

            server.start();
            return server;
        } catch (IOException exception) {
            throw new ExceptionInInitializerError(exception);
        }
    }

    private static void handlePythonRequest(HttpExchange exchange)
            throws IOException {

        try (exchange) {
            INTERNAL_REQUEST_COUNT.incrementAndGet();

            String requestBody = new String(
                    exchange.getRequestBody().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            LAST_INTERNAL_REQUEST.set(requestBody);

            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            byte[] responseBody =
                    STUB_BODY.get().getBytes(StandardCharsets.UTF_8);

            exchange.getResponseHeaders()
                    .set("Content-Type", "application/json");

            exchange.sendResponseHeaders(
                    STUB_STATUS.get(),
                    responseBody.length
            );

            exchange.getResponseBody().write(responseBody);
        }
    }

    private String publicRequest() {
        return """
                {
                  "transactions": [
                    {
                      "description": "Client payment",
                      "amount": 900.00,
                      "date": "2026-08-10",
                      "type": "INCOME"
                    },
                    {
                      "description": "Uber",
                      "amount": 60.00,
                      "date": "2026-08-11",
                      "type": "EXPENSE"
                    },
                    {
                      "description": "Netflix",
                      "amount": 40.00
                    }
                  ]
                }
                """;
    }

    private String allIncomeRequest() {
        return """
                {
                  "transactions": [
                    {
                      "description": "Salary",
                      "amount": 3500.00,
                      "date": "2026-08-05",
                      "type": "INCOME"
                    }
                  ]
                }
                """;
    }
}
