package com.rendaflex.demo.dto.internal;

import java.util.List;

public record InternalTransactionClassificationResponse(
        List<InternalClassifiedTransaction> transactions
) {
}
