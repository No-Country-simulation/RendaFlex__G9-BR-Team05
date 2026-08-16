package com.rendaflex.demo.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record NewExpenseInput(
        @NotBlank(message = "A descrição da nova despesa é obrigatória.")
        String description,

        @NotNull(message = "O valor total da nova compra é obrigatório.")
        @DecimalMin(value = "0.01", message = "O valor total deve ser maior que zero.")
        BigDecimal totalAmount,

        @NotNull(message = "A quantidade de parcelas é obrigatória.")
        @Min(value = 1, message = "O número de parcelas deve ser no mínimo 1.")
        Integer installmentCount
    ) {}
