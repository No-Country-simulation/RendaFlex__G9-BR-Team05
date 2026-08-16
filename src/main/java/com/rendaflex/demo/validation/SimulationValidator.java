package com.rendaflex.demo.validation;

import com.rendaflex.demo.dto.request.SimulationRequest;
import com.rendaflex.demo.dto.request.IncomeHistoryItem;
import com.rendaflex.demo.dto.request.TransactionInput;
import com.rendaflex.demo.exception.BusinessRuleException;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SimulationValidator {

    public void validate(SimulationRequest request) {
        if (request == null) {
            throw new BusinessRuleException("O corpo da requisição não pode ser nulo.");
        }

        validateUniqueMonthsInIncomeHistory(request.incomeHistory());
        validateTransactionDatesInMostRecentMonth(request.incomeHistory(), request.transactions());
    }


    private void validateUniqueMonthsInIncomeHistory(List<IncomeHistoryItem> history) {
        if (history == null || history.isEmpty()) return;

        Set<String> uniqueMonths = new HashSet<>();
        for (IncomeHistoryItem item : history) {
            if (item.month() != null && !uniqueMonths.add(item.month())) {
                throw new BusinessRuleException(
                    "O histórico de renda contém meses duplicados: " + item.month()
                );
            }
        }
    }


    private void validateTransactionDatesInMostRecentMonth(
            List<IncomeHistoryItem> history,
            List<TransactionInput> transactions) {

        if (history == null || history.isEmpty() || transactions == null || transactions.isEmpty()) {
            return;
        }

        String mostRecentMonth = history.stream()
                .map(IncomeHistoryItem::month)
                .filter(month -> month != null && !month.isBlank())
                .max(String::compareTo)
                .orElseThrow(() -> new BusinessRuleException("Histórico de renda inválido."));

        for (TransactionInput tx : transactions) {
            if (tx.date() == null) continue;
            String txMonth = YearMonth.from(tx.date()).toString();

        if (!txMonth.equals(mostRecentMonth)) {
            throw new BusinessRuleException(
                "A transação deve pertencer ao mês mais recente (" + mostRecentMonth + ")."
            );
            }
        }
    }
}
