package com.rendaflex.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;


public record SimulationRequest(
    @NotNull(message="O valor da simulação é obrigatório.")
    @Positive(message="O valor deve ser maior que zero.")
    BigDecimal amount,

    @NotNull(message="O número de parcelas é obrigatório.")
    @Min(value=1, message="Mínimo de 1 parcela.")
    @Max(value=72, message="Máximo de 72 parcelas.")
    Integer installments,

    @NotBlank(message="A classificação é obrigatória.")
    String classification
){}

