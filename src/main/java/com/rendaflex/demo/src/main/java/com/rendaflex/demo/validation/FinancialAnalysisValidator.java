package com.rendaflex.demo.validation;

import com.rendaflex.demo.dto.request.FinancialAnalysisRequest;
import com.rendaflex.demo.dto.request.IncomeHistoryItem;
import com.rendaflex.demo.dto.request.TransactionInput;
import com.rendaflex.demo.exception.BusinessRuleException;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class FinancialAnalysisValidator {

    public void validate(FinancialAnalysisRequest request) {
        validateUniqueIncomeMonths(request.incomeHistory());

        YearMonth latestIncomeMonth = findLatestIncomeMonth(request.incomeHistory());
        validateTransactionPeriod(request.transactions(), latestIncomeMonth);
        validatePositiveAverageIncome(request.incomeHistory());
    }

    private void validateUniqueIncomeMonths(List<IncomeHistoryItem> incomeHistory) {
        Set<String> uniqueMonths = new HashSet<>();

        for (IncomeHistoryItem item : incomeHistory) {
            if (!uniqueMonths.add(item.month())) {
                throw new BusinessRuleException(
                        "Os meses do histórico de renda não podem se repetir."
                );
            }
        }
    }

    private YearMonth findLatestIncomeMonth(List<IncomeHistoryItem> incomeHistory) {
        return incomeHistory.stream()
                .map(item -> YearMonth.parse(item.month()))
                .max(YearMonth::compareTo)
                .orElseThrow(() -> new BusinessRuleException(
                        "Não foi possível identificar o mês mais recente do histórico de renda."
                ));
    }

    private void validateTransactionPeriod(
            List<TransactionInput> transactions,
            YearMonth latestIncomeMonth
    ) {
        for (int index = 0; index < transactions.size(); index++) {
            TransactionInput transaction = transactions.get(index);
            YearMonth transactionMonth = YearMonth.from(transaction.date());

            if (!latestIncomeMonth.equals(transactionMonth)) {
                throw new BusinessRuleException(
                        "A data de transactions[" + index + "] deve pertencer ao mês "
                                + latestIncomeMonth + "."
                );
            }
        }
    }

    private void validatePositiveAverageIncome(List<IncomeHistoryItem> incomeHistory) {
        BigDecimal totalIncome = incomeHistory.stream()
                .map(IncomeHistoryItem::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalIncome.signum() == 0) {
            throw new BusinessRuleException(
                    "A análise financeira não pode ser realizada quando a renda média é zero."
            );
        }
    }
}
