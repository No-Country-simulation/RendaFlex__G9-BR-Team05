package com.rendaflex.demo.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.rendaflex.demo.dto.request.FinancialAnalysisRequest;
import com.rendaflex.demo.dto.request.IncomeHistoryItem;
import com.rendaflex.demo.dto.request.TransactionInput;
import com.rendaflex.demo.enums.SavingFrequency;
import com.rendaflex.demo.enums.TransactionType;
import com.rendaflex.demo.exception.BusinessRuleException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FinancialAnalysisValidatorTest {

    private FinancialAnalysisValidator validator;

    @BeforeEach
    void setUp() {
        validator = new FinancialAnalysisValidator();
    }

    @Test
    void shouldAcceptValidFinancialContext() {
        FinancialAnalysisRequest request = validRequest();

        assertDoesNotThrow(() -> validator.validate(request));
    }

    @Test
    void shouldRejectRepeatedIncomeMonths() {
        FinancialAnalysisRequest request = new FinancialAnalysisRequest(
                List.of(
                        income("2026-05", "3000.00"),
                        income("2026-06", "3200.00"),
                        income("2026-06", "3100.00")
                ),
                new BigDecimal("900.00"),
                SavingFrequency.MEDIUM,
                List.of(expense("2026-06-10"))
        );

        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> validator.validate(request)
        );

        assertEquals(
                "Os meses do histórico de renda não podem se repetir.",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectTransactionOutsideLatestIncomeMonth() {
        FinancialAnalysisRequest request = new FinancialAnalysisRequest(
                List.of(
                        income("2026-05", "3000.00"),
                        income("2026-06", "3200.00"),
                        income("2026-07", "3100.00")
                ),
                new BigDecimal("900.00"),
                SavingFrequency.MEDIUM,
                List.of(expense("2026-06-30"))
        );

        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> validator.validate(request)
        );

        assertEquals(
                "A data de transactions[0] deve pertencer ao mês 2026-07.",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectZeroAverageIncome() {
        FinancialAnalysisRequest request = new FinancialAnalysisRequest(
                List.of(
                        income("2026-05", "0.00"),
                        income("2026-06", "0.00"),
                        income("2026-07", "0.00")
                ),
                BigDecimal.ZERO,
                SavingFrequency.LOW,
                List.of(expense("2026-07-10"))
        );

        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> validator.validate(request)
        );

        assertEquals(
                "A análise financeira não pode ser realizada quando a renda média é zero.",
                exception.getMessage()
        );
    }

    private FinancialAnalysisRequest validRequest() {
        return new FinancialAnalysisRequest(
                List.of(
                        income("2026-05", "3000.00"),
                        income("2026-06", "3200.00"),
                        income("2026-07", "3100.00")
                ),
                new BigDecimal("900.00"),
                SavingFrequency.MEDIUM,
                List.of(expense("2026-07-10"))
        );
    }

    private IncomeHistoryItem income(String month, String amount) {
        return new IncomeHistoryItem(month, new BigDecimal(amount));
    }

    private TransactionInput expense(String date) {
        return new TransactionInput(
                "Supermercado",
                new BigDecimal("150.00"),
                LocalDate.parse(date),
                TransactionType.EXPENSE
        );
    }
}
