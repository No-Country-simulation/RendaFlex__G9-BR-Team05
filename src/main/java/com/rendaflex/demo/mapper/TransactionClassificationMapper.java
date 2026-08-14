package com.rendaflex.demo.mapper;

import com.rendaflex.demo.dto.internal.InternalClassificationTransaction;
import com.rendaflex.demo.dto.internal.InternalClassifiedTransaction;
import com.rendaflex.demo.dto.internal.InternalTransactionClassificationRequest;
import com.rendaflex.demo.dto.internal.InternalTransactionClassificationResponse;
import com.rendaflex.demo.dto.request.ClassificationTransactionInput;
import com.rendaflex.demo.dto.request.TransactionClassificationRequest;
import com.rendaflex.demo.dto.response.ClassifiedTransaction;
import com.rendaflex.demo.dto.response.TransactionClassificationResponse;
import com.rendaflex.demo.enums.TransactionCategory;
import com.rendaflex.demo.enums.TransactionType;
import com.rendaflex.demo.exception.ApiErrorCode;
import com.rendaflex.demo.exception.ModelServiceException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

@Component
public class TransactionClassificationMapper {

    private static final BigDecimal ZERO = BigDecimal.ZERO;
    private static final BigDecimal ONE = BigDecimal.ONE;
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    public InternalTransactionClassificationRequest toInternalRequest(
            TransactionClassificationRequest request
    ) {
        Objects.requireNonNull(
                request,
                "TransactionClassificationRequest must not be null."
        );

        List<InternalClassificationTransaction> transactions =
                IntStream.range(0, request.transactions().size())
                        .filter(index ->
                                request.transactions().get(index).type() != TransactionType.INCOME)
                        .mapToObj(index -> toInternalTransaction(
                                index,
                                request.transactions().get(index)
                        ))
                        .toList();

        return new InternalTransactionClassificationRequest(transactions);
    }

    public TransactionClassificationResponse toPublicResponse(
            TransactionClassificationRequest originalRequest,
            InternalTransactionClassificationResponse internalResponse
    ) {
        Objects.requireNonNull(
                originalRequest,
                "Original TransactionClassificationRequest must not be null."
        );

        if (internalResponse == null || internalResponse.transactions() == null) {
            throw invalidInternalResponse();
        }

        Map<Integer, InternalClassifiedTransaction> classificationsBySourceIndex =
                indexClassifications(
                        originalRequest.transactions(),
                        internalResponse.transactions()
                );

        List<ClassifiedTransaction> transactions =
                toPublicTransactions(
                        originalRequest.transactions(),
                        classificationsBySourceIndex
                );

        Map<TransactionCategory, BigDecimal> categorySummary =
                toCategorySummary(
                        originalRequest.transactions(),
                        classificationsBySourceIndex
                );

        Map<TransactionCategory, BigDecimal> categoryPercentages =
                toCategoryPercentages(categorySummary);

        return new TransactionClassificationResponse(
                transactions,
                categorySummary,
                categoryPercentages
        );
    }

    private InternalClassificationTransaction toInternalTransaction(
            int sourceIndex,
            ClassificationTransactionInput transaction
    ) {
        return new InternalClassificationTransaction(
                sourceIndex,
                transaction.description(),
                transaction.amount()
        );
    }

    private Map<Integer, InternalClassifiedTransaction> indexClassifications(
            List<ClassificationTransactionInput> originalTransactions,
            List<InternalClassifiedTransaction> internalClassifications
    ) {
        Map<Integer, InternalClassifiedTransaction> result = new HashMap<>();

        for (InternalClassifiedTransaction classified : internalClassifications) {
            validateInternalClassification(classified, originalTransactions);

            InternalClassifiedTransaction previous =
                    result.put(classified.sourceIndex(), classified);

            if (previous != null) {
                throw invalidInternalResponse();
            }
        }

        return result;
    }

    private List<ClassifiedTransaction> toPublicTransactions(
            List<ClassificationTransactionInput> originalTransactions,
            Map<Integer, InternalClassifiedTransaction> classificationsBySourceIndex
    ) {
        List<ClassifiedTransaction> result =
                new ArrayList<>(originalTransactions.size());

        for (int index = 0; index < originalTransactions.size(); index++) {
            ClassificationTransactionInput original =
                    originalTransactions.get(index);

            if (original.type() == TransactionType.INCOME) {
                result.add(new ClassifiedTransaction(
                        original.description(),
                        original.amount(),
                        original.date(),
                        original.type(),
                        null,
                        null
                ));
                continue;
            }

            InternalClassifiedTransaction classification =
                    classificationsBySourceIndex.get(index);

            if (classification == null) {
                throw invalidInternalResponse();
            }

            result.add(new ClassifiedTransaction(
                    original.description(),
                    original.amount(),
                    original.date(),
                    original.type(),
                    classification.predictedCategory(),
                    classification.classificationProbability()
            ));
        }

        return List.copyOf(result);
    }

    private Map<TransactionCategory, BigDecimal> toCategorySummary(
            List<ClassificationTransactionInput> originalTransactions,
            Map<Integer, InternalClassifiedTransaction> classificationsBySourceIndex
    ) {
        Map<TransactionCategory, BigDecimal> result =
                new EnumMap<>(TransactionCategory.class);

        for (int index = 0; index < originalTransactions.size(); index++) {
            ClassificationTransactionInput original =
                    originalTransactions.get(index);

            if (original.type() == TransactionType.INCOME
                    || original.amount() == null) {
                continue;
            }

            InternalClassifiedTransaction classification =
                    classificationsBySourceIndex.get(index);

            if (classification == null) {
                throw invalidInternalResponse();
            }

            result.merge(
                    classification.predictedCategory(),
                    original.amount(),
                    BigDecimal::add
            );
        }

        result.replaceAll((category, amount) -> money(amount));

        return Map.copyOf(result);
    }

    private Map<TransactionCategory, BigDecimal> toCategoryPercentages(
            Map<TransactionCategory, BigDecimal> categorySummary
    ) {
        BigDecimal total = categorySummary.values().stream()
                .reduce(ZERO, BigDecimal::add);

        if (total.compareTo(ZERO) == 0) {
            return Map.of();
        }

        Map<TransactionCategory, BigDecimal> result =
                new EnumMap<>(TransactionCategory.class);

        categorySummary.forEach((category, amount) ->
                result.put(
                        category,
                        amount
                                .divide(total, 10, RoundingMode.HALF_UP)
                                .multiply(ONE_HUNDRED)
                                .setScale(2, RoundingMode.HALF_UP)
                )
        );

        return Map.copyOf(result);
    }

    private void validateInternalClassification(
            InternalClassifiedTransaction classified,
            List<ClassificationTransactionInput> originalTransactions
    ) {
        if (classified == null) {
            throw invalidInternalResponse();
        }

        Integer sourceIndex = classified.sourceIndex();

        if (sourceIndex == null
                || sourceIndex < 0
                || sourceIndex >= originalTransactions.size()) {
            throw invalidInternalResponse();
        }

        if (originalTransactions.get(sourceIndex).type() == TransactionType.INCOME) {
            throw invalidInternalResponse();
        }

        if (classified.predictedCategory() == null) {
            throw invalidInternalResponse();
        }

        validateProbability(classified.classificationProbability());
    }

    private void validateProbability(BigDecimal probability) {
        if (probability == null
                || probability.compareTo(ZERO) < 0
                || probability.compareTo(ONE) > 0) {
            throw invalidInternalResponse();
        }
    }

    private BigDecimal money(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    private ModelServiceException invalidInternalResponse() {
        return new ModelServiceException(
                ApiErrorCode.MODEL_SERVICE_INVALID_RESPONSE,
                "O serviço de classificação retornou uma resposta inválida."
        );
    }
}
