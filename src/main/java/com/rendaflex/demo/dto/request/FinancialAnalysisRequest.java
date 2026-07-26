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
        @Size(
                min = 3,
                max = 6,
                message = "O histórico de renda deve conter de 3 a 6 itens."
        )
        List<@NotNull(message = "Os itens do histórico de renda não podem ser nulos.") @Valid IncomeHistoryItem> incomeHistory,

        @NotNull(message = "O total mensal de compromissos fixos é obrigatório.")
        @DecimalMin(
                value = "0.0",
                inclusive = true,
                message = "O total mensal de compromissos fixos deve ser maior ou igual a zero."
        )
        BigDecimal monthlyDebts,

        @NotNull(message = "A frequência de economia é obrigatória.")
        SavingFrequency savingFrequency,

        @NotNull(message = "A lista de transações é obrigatória.")
        @Size(min = 1, message = "A lista de transações deve conter pelo menos um item.")
        List<@NotNull(message = "Os itens da lista de transações não podem ser nulos.") @Valid TransactionInput> transactions
) {
}
