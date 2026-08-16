package com.rendaflex.demo.integration;

import com.rendaflex.demo.dto.request.SimulationRequest;
import com.rendaflex.demo.dto.response.SimulationResponse;

public interface SimulationGateway {

    SimulationResponse processSimulation(SimulationRequest request);
}
