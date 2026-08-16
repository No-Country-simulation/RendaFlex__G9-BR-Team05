package com.rendaflex.demo.controller;

import com.rendaflex.demo.dto.request.SimulationRequest;
import com.rendaflex.demo.dto.response.SimulationResponse;
import com.rendaflex.demo.service.SimulationService;
import com.rendaflex.demo.validation.SimulationValidator;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/expense-simulations")
public class SimulationController {

    private final SimulationValidator simulationValidator;
    private final SimulationService simulationService;

    public SimulationController(
            SimulationValidator simulationValidator,
            SimulationService simulationService) {
        this.simulationValidator = simulationValidator;
        this.simulationService = simulationService;
    }

    @PostMapping
    public ResponseEntity<SimulationResponse> simulateExpense(
            @Valid @RequestBody SimulationRequest request) {

        simulationValidator.validate(request);


        SimulationResponse response = simulationService.processSimulation(request);

        return ResponseEntity.ok(response);
    }
}