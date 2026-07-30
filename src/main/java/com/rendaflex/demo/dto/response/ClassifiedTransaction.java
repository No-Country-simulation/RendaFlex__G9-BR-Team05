package com.rendaflex.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rendaflex.demo.enums.TransactionCategory;
import com.rendaflex.demo.enums.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Transaction returned by the API after the classification flow.
 *
 * <p>Null optional fields are omitted from the JSON response. This allows
 * income transactions to remain without classification fields, as defined
 * by the public API contract.</p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ClassifiedTransaction(
        String description,
        BigDecimal amount,
        LocalDate date,
        TransactionType type,
        TransactionCategory predictedCategory,
        BigDecimal classificationProbability
) {
}
