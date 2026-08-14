package com.rendaflex.demo.mapper;

import com.rendaflex.demo.dto.internal.InternalClassifiedTransaction;
import com.rendaflex.demo.dto.internal.InternalTransactionClassificationRequest;
import com.rendaflex.demo.dto.internal.InternalTransactionClassificationResponse;
import com.rendaflex.demo.dto.request.ClassificationTransactionInput;
import com.rendaflex.demo.dto.request.TransactionClassificationRequest;
import com.rendaflex.demo.dto.response.TransactionClassificationResponse;
import com.rendaflex.demo.enums.TransactionCategory;
import com.rendaflex.demo.enums.TransactionType;
import com.rendaflex.demo.exception.ApiErrorCode;
import com.rendaflex.demo.exception.ModelServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TransactionClassificationMapperTest {

    private TransactionClassificationMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new TransactionClassificationMapper();
    }

    @Test
    void shouldMapOnlyClassifiableTransactionsAndPreserveOriginalSourceIndex() {
        TransactionClassificationRequest request = request(
                transaction(
                        "Salário",
                        "3000.00",
                        TransactionType.INCOME
                ),
                transaction(
                        "Supermercado",
                        "120.00",
                        TransactionType.EXPENSE
                ),
                new ClassificationTransactionInput(
                        "Netflix",
                        null,
                        LocalDate.of(2026, 8, 14),
                        null
                )
        );

        InternalTransactionClassificationRequest result =
                mapper.toInternalRequest(request);

        assertThat(result.transactions()).hasSize(2);

        assertThat(result.transactions().get(0).sourceIndex()).isEqualTo(1);
        assertThat(result.transactions().get(0).description())
                .isEqualTo("Supermercado");
        assertThat(result.transactions().get(0).amount())
                .isEqualByComparingTo("120.00");

        assertThat(result.transactions().get(1).sourceIndex()).isEqualTo(2);
        assertThat(result.transactions().get(1).description())
                .isEqualTo("Netflix");
        assertThat(result.transactions().get(1).amount()).isNull();
    }

    @Test
    void shouldRecomposeOriginalOrderPreserveIncomeAndCalculateAggregates() {
        TransactionClassificationRequest request = request(
                transaction(
                        "Supermercado",
                        "100.00",
                        TransactionType.EXPENSE
                ),
                transaction(
                        "Salário",
                        "2000.00",
                        TransactionType.INCOME
                ),
                new ClassificationTransactionInput(
                        "Netflix",
                        new BigDecimal("50.00"),
                        LocalDate.of(2026, 8, 14),
                        null
                )
        );

        InternalTransactionClassificationResponse internalResponse =
                response(
                        classification(
                                2,
                                TransactionCategory.SERVICES,
                                "0.80"
                        ),
                        classification(
                                0,
                                TransactionCategory.FOOD,
                                "0.90"
                        )
                );

        TransactionClassificationResponse result =
                mapper.toPublicResponse(request, internalResponse);

        assertThat(result.transactions()).hasSize(3);

        assertThat(result.transactions().get(0).description())
                .isEqualTo("Supermercado");
        assertThat(result.transactions().get(0).predictedCategory())
                .isEqualTo(TransactionCategory.FOOD);
        assertThat(result.transactions().get(0).classificationProbability())
                .isEqualByComparingTo("0.90");

        assertThat(result.transactions().get(1).description())
                .isEqualTo("Salário");
        assertThat(result.transactions().get(1).type())
                .isEqualTo(TransactionType.INCOME);
        assertThat(result.transactions().get(1).predictedCategory()).isNull();
        assertThat(result.transactions().get(1).classificationProbability()).isNull();

        assertThat(result.transactions().get(2).description())
                .isEqualTo("Netflix");
        assertThat(result.transactions().get(2).type()).isNull();
        assertThat(result.transactions().get(2).predictedCategory())
                .isEqualTo(TransactionCategory.SERVICES);

        assertThat(result.categorySummary())
                .containsEntry(
                        TransactionCategory.FOOD,
                        new BigDecimal("100.00")
                )
                .containsEntry(
                        TransactionCategory.SERVICES,
                        new BigDecimal("50.00")
                )
                .hasSize(2);

        assertThat(result.categoryPercentages())
                .containsEntry(
                        TransactionCategory.FOOD,
                        new BigDecimal("66.67")
                )
                .containsEntry(
                        TransactionCategory.SERVICES,
                        new BigDecimal("33.33")
                )
                .hasSize(2);
    }

    @Test
    void shouldClassifyTransactionWithoutAmountButExcludeItFromAggregates() {
        TransactionClassificationRequest request =
                new TransactionClassificationRequest(
                        List.of(new ClassificationTransactionInput(
                                "Netflix",
                                null,
                                null,
                                TransactionType.EXPENSE
                        ))
                );

        InternalTransactionClassificationResponse internalResponse =
                response(
                        classification(
                                0,
                                TransactionCategory.SERVICES,
                                "0.95"
                        )
                );

        TransactionClassificationResponse result =
                mapper.toPublicResponse(request, internalResponse);

        assertThat(result.transactions()).hasSize(1);
        assertThat(result.transactions().get(0).predictedCategory())
                .isEqualTo(TransactionCategory.SERVICES);
        assertThat(result.transactions().get(0).classificationProbability())
                .isEqualByComparingTo("0.95");

        assertThat(result.categorySummary()).isEmpty();
        assertThat(result.categoryPercentages()).isEmpty();
    }

    @Test
    void shouldSupportOnlyIncomeTransactionsWithEmptyInternalResponse() {
        TransactionClassificationRequest request = request(
                transaction(
                        "Salário",
                        "3000.00",
                        TransactionType.INCOME
                )
        );

        InternalTransactionClassificationResponse internalResponse =
                new InternalTransactionClassificationResponse(List.of());

        TransactionClassificationResponse result =
                mapper.toPublicResponse(request, internalResponse);

        assertThat(result.transactions()).hasSize(1);
        assertThat(result.transactions().get(0).description())
                .isEqualTo("Salário");
        assertThat(result.transactions().get(0).predictedCategory()).isNull();
        assertThat(result.transactions().get(0).classificationProbability()).isNull();
        assertThat(result.categorySummary()).isEmpty();
        assertThat(result.categoryPercentages()).isEmpty();
    }

    @Test
    void shouldRejectDuplicateSourceIndex() {
        TransactionClassificationRequest request = request(
                transaction(
                        "Supermercado",
                        "100.00",
                        TransactionType.EXPENSE
                )
        );

        InternalTransactionClassificationResponse internalResponse =
                response(
                        classification(
                                0,
                                TransactionCategory.FOOD,
                                "0.90"
                        ),
                        classification(
                                0,
                                TransactionCategory.FOOD,
                                "0.80"
                        )
                );

        assertInvalidResponse(request, internalResponse);
    }

    @Test
    void shouldRejectMissingClassificationForClassifiableTransaction() {
        TransactionClassificationRequest request = request(
                transaction(
                        "Supermercado",
                        "100.00",
                        TransactionType.EXPENSE
                )
        );

        InternalTransactionClassificationResponse internalResponse =
                new InternalTransactionClassificationResponse(List.of());

        assertInvalidResponse(request, internalResponse);
    }

    @Test
    void shouldRejectClassificationForIncomeTransaction() {
        TransactionClassificationRequest request = request(
                transaction(
                        "Salário",
                        "3000.00",
                        TransactionType.INCOME
                )
        );

        InternalTransactionClassificationResponse internalResponse =
                response(
                        classification(
                                0,
                                TransactionCategory.OTHER,
                                "0.90"
                        )
                );

        assertInvalidResponse(request, internalResponse);
    }

    @Test
    void shouldRejectSourceIndexOutsideOriginalTransactionRange() {
        TransactionClassificationRequest request = request(
                transaction(
                        "Supermercado",
                        "100.00",
                        TransactionType.EXPENSE
                )
        );

        InternalTransactionClassificationResponse internalResponse =
                response(
                        classification(
                                5,
                                TransactionCategory.FOOD,
                                "0.90"
                        )
                );

        assertInvalidResponse(request, internalResponse);
    }

    @Test
    void shouldRejectNullPredictedCategory() {
        TransactionClassificationRequest request = request(
                transaction(
                        "Supermercado",
                        "100.00",
                        TransactionType.EXPENSE
                )
        );

        InternalTransactionClassificationResponse internalResponse =
                response(
                        classification(
                                0,
                                null,
                                "0.90"
                        )
                );

        assertInvalidResponse(request, internalResponse);
    }

    @Test
    void shouldRejectNegativeClassificationProbability() {
        TransactionClassificationRequest request = request(
                transaction(
                        "Supermercado",
                        "100.00",
                        TransactionType.EXPENSE
                )
        );

        InternalTransactionClassificationResponse internalResponse =
                response(
                        classification(
                                0,
                                TransactionCategory.FOOD,
                                "-0.01"
                        )
                );

        assertInvalidResponse(request, internalResponse);
    }

    @Test
    void shouldRejectClassificationProbabilityGreaterThanOne() {
        TransactionClassificationRequest request = request(
                transaction(
                        "Supermercado",
                        "100.00",
                        TransactionType.EXPENSE
                )
        );

        InternalTransactionClassificationResponse internalResponse =
                response(
                        classification(
                                0,
                                TransactionCategory.FOOD,
                                "1.01"
                        )
                );

        assertInvalidResponse(request, internalResponse);
    }

    @Test
    void shouldRejectNullInternalTransactionsList() {
        TransactionClassificationRequest request = request(
                transaction(
                        "Supermercado",
                        "100.00",
                        TransactionType.EXPENSE
                )
        );

        InternalTransactionClassificationResponse internalResponse =
                new InternalTransactionClassificationResponse(null);

        assertInvalidResponse(request, internalResponse);
    }

    @Test
    void shouldRejectNullClassificationItem() {
        TransactionClassificationRequest request = request(
                transaction(
                        "Supermercado",
                        "100.00",
                        TransactionType.EXPENSE
                )
        );

        InternalTransactionClassificationResponse internalResponse =
                new InternalTransactionClassificationResponse(
                        Collections.singletonList(null)
                );

        assertInvalidResponse(request, internalResponse);
    }

    @Test
    void shouldRejectNullPublicRequestWhenMappingToInternalRequest() {
        assertThatThrownBy(() -> mapper.toInternalRequest(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(
                        "TransactionClassificationRequest must not be null."
                );
    }

    private void assertInvalidResponse(
            TransactionClassificationRequest request,
            InternalTransactionClassificationResponse internalResponse
    ) {
        assertThatThrownBy(
                () -> mapper.toPublicResponse(request, internalResponse)
        )
                .isInstanceOfSatisfying(
                        ModelServiceException.class,
                        exception -> assertThat(exception.getCode())
                                .isEqualTo(
                                        ApiErrorCode.MODEL_SERVICE_INVALID_RESPONSE
                                )
                );
    }

    private TransactionClassificationRequest request(
            ClassificationTransactionInput... transactions
    ) {
        return new TransactionClassificationRequest(
                List.of(transactions)
        );
    }

    private ClassificationTransactionInput transaction(
            String description,
            String amount,
            TransactionType type
    ) {
        return new ClassificationTransactionInput(
                description,
                new BigDecimal(amount),
                LocalDate.of(2026, 8, 14),
                type
        );
    }

    private InternalTransactionClassificationResponse response(
            InternalClassifiedTransaction... classifications
    ) {
        return new InternalTransactionClassificationResponse(
                List.of(classifications)
        );
    }

    private InternalClassifiedTransaction classification(
            int sourceIndex,
            TransactionCategory category,
            String probability
    ) {
        return new InternalClassifiedTransaction(
                sourceIndex,
                category,
                new BigDecimal(probability)
        );
    }
}
