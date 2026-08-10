package com.rendaflex.demo.validation;

import com.rendaflex.demo.dto.request.FinancialAnalysisRequest;
import com.rendaflex.demo.dto.request.IncomeHistoryItem;
import com.rendaflex.demo.dto.request.TransactionInput;
import com.rendaflex.demo.enums.SavingFrequency;
import com.rendaflex.demo.enums.TransactionType;
import com.rendaflex.demo.exception.BusinessRuleException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FinancialAnalysisValidatorTest {

    private final FinancialAnalysisValidator validator = new FinancialAnalysisValidator();

    @Test
    void shouldAcceptValidRequest() {
        assertDoesNotThrow(() -> validator.validate(request(incomeHistory(), LocalDate.of(2026, 7, 15))));
    }

    @Test
    void shouldRejectDuplicateIncomeHistoryMonth() {
        List<IncomeHistoryItem> incomeHistory = List.of(
                income("2026-06", "3000"),
                income("2026-06", "3200"),
                income("2026-07", "3100")
        );

        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> validator.validate(request(incomeHistory, LocalDate.of(2026, 7, 15)))
        );

        assertEquals("O histórico de renda não pode conter meses duplicados.", exception.getMessage());
    }

    @Test
    void shouldRejectZeroAverageIncome() {
        List<IncomeHistoryItem> incomeHistory = List.of(
                income("2026-05", "0"),
                income("2026-06", "0"),
                income("2026-07", "0")
        );

        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> validator.validate(request(incomeHistory, LocalDate.of(2026, 7, 15)))
        );

        assertEquals("A média da renda histórica deve ser maior que zero.", exception.getMessage());
    }

    @Test
    void shouldAcceptHistoryWithPositiveAverageIncome() {
        List<IncomeHistoryItem> incomeHistory = List.of(
                income("2026-05", "0"),
                income("2026-06", "100"),
                income("2026-07", "0")
        );

        assertDoesNotThrow(
                () -> validator.validate(request(incomeHistory, LocalDate.of(2026, 7, 15)))
        );
    }

    @Test
    void shouldAcceptTransactionInLatestIncomeHistoryMonth() {
        assertDoesNotThrow(() -> validator.validate(request(incomeHistory(), LocalDate.of(2026, 7, 31))));
    }

    @Test
    void shouldRejectTransactionBeforeLatestIncomeHistoryMonth() {
        assertInvalidTransactionPeriod(LocalDate.of(2026, 6, 30));
    }

    @Test
    void shouldRejectTransactionAfterLatestIncomeHistoryMonth() {
        assertInvalidTransactionPeriod(LocalDate.of(2026, 8, 1));
    }

    private void assertInvalidTransactionPeriod(LocalDate transactionDate) {
        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> validator.validate(request(incomeHistory(), transactionDate))
        );

        assertEquals(
                "As transações devem pertencer ao mês mais recente do histórico de renda.",
                exception.getMessage()
        );
    }

    private FinancialAnalysisRequest request(
            List<IncomeHistoryItem> incomeHistory,
            LocalDate transactionDate
    ) {
        return new FinancialAnalysisRequest(
                incomeHistory,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                SavingFrequency.SOMETIMES,
                List.of(new TransactionInput(
                        "Supermercado",
                        new BigDecimal("100.00"),
                        transactionDate,
                        TransactionType.EXPENSE
                ))
        );
    }

    private List<IncomeHistoryItem> incomeHistory() {
        return List.of(
                income("2026-05", "3000"),
                income("2026-06", "3200"),
                income("2026-07", "3100")
        );
    }

    private IncomeHistoryItem income(String month, String amount) {
        return new IncomeHistoryItem(month, new BigDecimal(amount));
    }
}
