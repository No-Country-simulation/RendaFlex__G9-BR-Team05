package com.rendaflex.demo.service;

import com.rendaflex.demo.dto.request.SimulationRequest;
import com.rendaflex.demo.dto.response.SimulationResponse;
import com.rendaflex.demo.integration.SimulationGateway;
import org.springframework.stereotype.Service;

@Service
public class SimulationService {

    private final SimulationGateway SimulationGateway;

    public SimulationService(
            SimulationGateway SimulationGateway
    ) {
        this.SimulationGateway = SimulationGateway;
    }

    public SimulationResponse processSimulation(SimulationRequest request) {
        return SimulationGateway.processSimulation(request);
    }
}