package com.rendaflex.demo.dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rendaflex.demo.enums.FinancialProfile;
import com.rendaflex.demo.enums.RecommendationPriority;
import com.rendaflex.demo.enums.TransactionCategory;
import com.rendaflex.demo.enums.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class FinancialAnalysisResponseTest {

    @Test
    void shouldCreateResponseWithPublicContractFields() {
        FinancialMetrics metrics = new FinancialMetrics(
                new BigDecimal("3280.94"),
                new BigDecimal("6.04"),
                new BigDecimal("26.06"),
                new BigDecimal("37.03")
        );

        ClassifiedTransaction transaction = new ClassifiedTransaction(
                "Uber",
                new BigDecimal("51.00"),
                LocalDate.of(2026, 7, 5),
                TransactionType.EXPENSE,
                TransactionCategory.TRANSPORT,
                new BigDecimal("0.98")
        );

        Recommendation recommendation = new Recommendation(
                RecommendationPriority.MEDIUM,
                "Construa uma reserva para meses de menor renda."
        );

        FinancialAnalysisResponse response = new FinancialAnalysisResponse(
                FinancialProfile.HEALTHY,
                new BigDecimal("0.91"),
                metrics,
                List.of(transaction),
                Map.of(TransactionCategory.TRANSPORT, new BigDecimal("51.00")),
                List.of(recommendation)
        );

        assertEquals(FinancialProfile.HEALTHY, response.financialProfile());
        assertEquals(new BigDecimal("0.91"), response.probability());
        assertEquals(metrics, response.metrics());
        assertEquals(List.of(transaction), response.classifiedTransactions());
        assertEquals(
                new BigDecimal("51.00"),
                response.categorySummary().get(TransactionCategory.TRANSPORT)
        );
        assertEquals(List.of(recommendation), response.recommendations());
    }

    @Test
    void shouldAllowIncomeTransactionWithoutClassificationFields() {
        ClassifiedTransaction incomeTransaction = new ClassifiedTransaction(
                "Freelance payment",
                new BigDecimal("850.00"),
                LocalDate.of(2026, 7, 15),
                TransactionType.INCOME,
                null,
                null
        );

        assertNull(incomeTransaction.predictedCategory());
        assertNull(incomeTransaction.classificationProbability());
    }

    @Test
    void shouldOmitNullOptionalFieldsFromJson() {
        JsonInclude annotation =
                ClassifiedTransaction.class.getAnnotation(JsonInclude.class);

        assertNotNull(annotation);
        assertEquals(JsonInclude.Include.NON_NULL, annotation.value());
    }
}
