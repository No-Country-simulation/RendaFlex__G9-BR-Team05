package com.rendaflex.demo.dto.internal;

import java.util.List;

public record InternalTransactionClassificationRequest(
        List<InternalClassificationTransaction> transactions
) {
}
