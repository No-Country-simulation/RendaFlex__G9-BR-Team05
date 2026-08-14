package com.rendaflex.demo.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record TransactionClassificationRequest(
        @NotNull(message = "As transações são obrigatórias.")
        @Size(min = 1, message = "As transações devem conter pelo menos 1 item.")
        List<@Valid ClassificationTransactionInput> transactions
) {
}
