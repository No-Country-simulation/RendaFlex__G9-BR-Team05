package com.rendaflex.demo.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;

public record IncomeHistoryItem(
        @NotBlank(message = "O mês do histórico de renda é obrigatório.")
        @Pattern(
                regexp = "\\d{4}-(0[1-9]|1[0-2])",
                message = "O mês deve seguir o formato YYYY-MM."
        )
        String month,

        @NotNull(message = "O valor da renda é obrigatório.")
        @DecimalMin(
                value = "0.0",
                inclusive = true,
                message = "O valor da renda deve ser maior ou igual a zero."
        )
        BigDecimal amount
) {
}
