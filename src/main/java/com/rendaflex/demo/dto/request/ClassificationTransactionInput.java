package com.rendaflex.demo.dto.request;

import com.rendaflex.demo.enums.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ClassificationTransactionInput(
        @NotBlank(message = "A descrição é obrigatória.")
        String description,

        @DecimalMin(value = "0.0", inclusive = false, message = "O valor da transação deve ser maior que zero.")
        BigDecimal amount,

        LocalDate date,

        TransactionType type
) {
}
