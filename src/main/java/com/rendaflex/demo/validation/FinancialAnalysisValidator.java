package com.rendaflex.demo.validation;

import com.rendaflex.demo.dto.request.FinancialAnalysisRequest;
import com.rendaflex.demo.dto.request.IncomeHistoryItem;
import com.rendaflex.demo.exception.BusinessRuleException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.YearMonth;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class FinancialAnalysisValidator {

    public void validate(FinancialAnalysisRequest request) {
        validateUniqueIncomeHistoryMonths(request.incomeHistory());
        validatePositiveAverageIncome(request.incomeHistory());
        validateTransactionPeriod(request);
    }

    private void validateUniqueIncomeHistoryMonths(List<IncomeHistoryItem> incomeHistory) {
        Set<String> months = new HashSet<>();

        boolean hasDuplicate = incomeHistory.stream()
                .map(IncomeHistoryItem::month)
                .anyMatch(month -> !months.add(month));

        if (hasDuplicate) {
            throw new BusinessRuleException("O histórico de renda não pode conter meses duplicados.");
        }
    }

    private void validatePositiveAverageIncome(List<IncomeHistoryItem> incomeHistory) {
        BigDecimal totalIncome = incomeHistory.stream()
                .map(IncomeHistoryItem::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal averageIncome = totalIncome.divide(
                BigDecimal.valueOf(incomeHistory.size()),
                MathContext.DECIMAL128
        );

        if (averageIncome.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("A média da renda histórica deve ser maior que zero.");
        }
    }

    private void validateTransactionPeriod(FinancialAnalysisRequest request) {
        YearMonth latestIncomeMonth = request.incomeHistory().stream()
                .map(IncomeHistoryItem::month)
                .map(YearMonth::parse)
                .max(YearMonth::compareTo)
                .orElseThrow();

        boolean hasTransactionOutsideLatestMonth = request.transactions().stream()
                .map(transaction -> YearMonth.from(transaction.date()))
                .anyMatch(month -> !month.equals(latestIncomeMonth));

        if (hasTransactionOutsideLatestMonth) {
            throw new BusinessRuleException(
                    "As transações devem pertencer ao mês mais recente do histórico de renda."
            );
        }
    }
}
