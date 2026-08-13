package com.rendaflex.demo.dto.request;

import com.rendaflex.demo.enums.SavingFrequency;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record FinancialAnalysisRequest(
        @NotNull(message = "O histórico de renda é obrigatório.")
        @Size(min = 3, max = 6, message = "O histórico de renda deve conter de 3 a 6 itens.")
        List<@Valid IncomeHistoryItem> incomeHistory,

        @NotNull(message = "O pagamento mensal de dívidas é obrigatório.")
        @DecimalMin(value = "0.0", message = "O pagamento mensal de dívidas deve ser maior ou igual a zero.")
        BigDecimal monthlyDebtPayments,

        @NotNull(message = "As outras despesas fixas mensais são obrigatórias.")
        @DecimalMin(value = "0.0", message = "As outras despesas fixas mensais devem ser maiores ou iguais a zero.")
        BigDecimal otherFixedMonthlyExpenses,

        @NotNull(message = "A frequência de economia é obrigatória.")
        SavingFrequency savingFrequency,

        @NotNull(message = "As transações são obrigatórias.")
        @Size(min = 1, message = "As transações devem conter pelo menos 1 item.")
        List<@Valid TransactionInput> transactions
) {
}
