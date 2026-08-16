package com.rendaflex.demo.dto.request;

import com.rendaflex.demo.enums.SavingFrequency;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public record SimulationRequest(
    @NotNull(message = "O histórico de renda é obrigatório.")
    @Size(min = 3, max = 6, message = "O histórico de renda deve conter entre 3 e 6 meses.")
    List<@Valid IncomeHistoryItem> incomeHistory,

    @NotNull(message = "O pagamento mensal de dívidas é obrigatório.")
    @PositiveOrZero(message = "O valor das dívidas não pode ser negativo.")
    BigDecimal monthlyDebtPayments,

    @NotNull(message = "Outras despesas fixas mensais são obrigatórias.")
    @PositiveOrZero(message = "O valor de outras despesas fixas não pode ser negativo.")
    BigDecimal otherFixedMonthlyExpenses,

    @NotNull(message = "A frequência de poupança é obrigatória.")
    SavingFrequency savingFrequency,

    @NotNull(message = "A lista de transações é obrigatória.")
    @Size(min = 1, message = "Forneça pelo menos uma transação.")
    List<@Valid TransactionInput> transactions,

    @NotNull(message = "Os dados da nova despesa simulada são obrigatórios.")
    @Valid
    NewExpenseInput newExpense
){}