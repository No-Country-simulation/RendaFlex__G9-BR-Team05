package com.rendaflex.demo.validation;

import com.rendaflex.demo.dto.request.SimulationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;

class SimulationValidatorTest {

    private final SimulationValidator validator = new SimulationValidator();

    @Test
    @DisplayName("Deve validar com sucesso uma simulação dentro do limite")
    void shouldValidateSuccessfully() {
        var request = new SimulationRequest(new BigDecimal("10000"), 24, "ALIMENTACAO");

        assertDoesNotThrow(() -> validator.validate(request));
    }
}
