package com.rendaflex.demo.dto.request;

import com.rendaflex.demo.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionInput(
        @NotBlank(message = "A descrição da transação é obrigatória.")
        String description,

        @NotNull(message = "O valor da transação é obrigatório.")
        @Positive(message = "O valor da transação deve ser maior que zero.")
        BigDecimal amount,

        @NotNull(message = "A data da transação é obrigatória.")
        LocalDate date,

        @NotNull(message = "O tipo da transação é obrigatório.")
        TransactionType type
) {
}
