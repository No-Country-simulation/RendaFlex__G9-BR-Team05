package com.rendaflex.demo.dto.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.rendaflex.demo.enums.TransactionType;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionClassificationRequestValidationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void closeValidatorFactory() {
        validatorFactory.close();
    }

    @Test
    void shouldAcceptTransactionWithDescriptionOnly() {
        ClassificationTransactionInput transaction =
                new ClassificationTransactionInput(
                        "Netflix",
                        null,
                        null,
                        null
                );

        assertThat(validator.validate(transaction)).isEmpty();
    }

    @Test
    void shouldRejectBlankDescription() {
        ClassificationTransactionInput transaction =
                new ClassificationTransactionInput(
                        " ",
                        null,
                        null,
                        null
                );

        assertThat(validator.validate(transaction))
                .extracting(violation -> violation.getMessage())
                .contains("A descrição é obrigatória.");
    }

    @Test
    void shouldRejectZeroAmountWhenAmountIsProvided() {
        ClassificationTransactionInput transaction =
                new ClassificationTransactionInput(
                        "Netflix",
                        BigDecimal.ZERO,
                        null,
                        null
                );

        assertThat(validator.validate(transaction))
                .extracting(violation -> violation.getMessage())
                .contains("O valor da transação deve ser maior que zero.");
    }

    @Test
    void shouldRejectNegativeAmountWhenAmountIsProvided() {
        ClassificationTransactionInput transaction =
                new ClassificationTransactionInput(
                        "Netflix",
                        new BigDecimal("-10.00"),
                        null,
                        null
                );

        assertThat(validator.validate(transaction))
                .extracting(violation -> violation.getMessage())
                .contains("O valor da transação deve ser maior que zero.");
    }

    @Test
    void shouldAcceptOptionalAmountDateAndTypeWhenProvidedWithValidValues() {
        ClassificationTransactionInput transaction =
                new ClassificationTransactionInput(
                        "Netflix",
                        new BigDecimal("45.90"),
                        LocalDate.of(2026, 8, 14),
                        TransactionType.EXPENSE
                );

        assertThat(validator.validate(transaction)).isEmpty();
    }

    @Test
    void shouldRejectNullTransactionsList() {
        TransactionClassificationRequest request =
                new TransactionClassificationRequest(null);

        assertThat(validator.validate(request))
                .extracting(violation -> violation.getMessage())
                .contains("As transações são obrigatórias.");
    }

    @Test
    void shouldRejectEmptyTransactionsList() {
        TransactionClassificationRequest request =
                new TransactionClassificationRequest(List.of());

        assertThat(validator.validate(request))
                .extracting(violation -> violation.getMessage())
                .contains("As transações devem conter pelo menos 1 item.");
    }

    @Test
    void shouldCascadeValidationToTransactionItems() {
        ClassificationTransactionInput invalidTransaction =
                new ClassificationTransactionInput(
                        "",
                        null,
                        null,
                        null
                );

        TransactionClassificationRequest request =
                new TransactionClassificationRequest(
                        List.of(invalidTransaction)
                );

        assertThat(validator.validate(request))
                .extracting(violation -> violation.getMessage())
                .contains("A descrição é obrigatória.");
    }
}
