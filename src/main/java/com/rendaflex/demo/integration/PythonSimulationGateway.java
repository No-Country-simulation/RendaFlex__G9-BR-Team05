package com.rendaflex.demo.integration;

import com.rendaflex.demo.dto.internal.InternalExpenseSimulationRequest;
import com.rendaflex.demo.dto.internal.InternalExpenseSimulationResponse;
import com.rendaflex.demo.dto.request.SimulationRequest;
import com.rendaflex.demo.dto.response.SimulationResponse;
import com.rendaflex.demo.integration.client.ExpenseSimulationClient;
import com.rendaflex.demo.mapper.SimulationMapper;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PythonSimulationGateway implements SimulationGateway {

    private final SimulationMapper mapper;
    private final ExpenseSimulationClient client;

    public PythonSimulationGateway(
            SimulationMapper mapper,
            ExpenseSimulationClient client
    ) {
        this.mapper = Objects.requireNonNull(
                mapper,
                "SimulationMapper must not be null."
        );
        this.client = Objects.requireNonNull(
                client,
                "ExpenseSimulationClient must not be null."
        );
    }

    @Override
    public SimulationResponse processSimulation(SimulationRequest request) {
        InternalExpenseSimulationRequest internalRequest =
                mapper.toInternalRequest(request);

        InternalExpenseSimulationResponse internalResponse =
                client.simulate(internalRequest);

        return mapper.toPublicResponse(request, internalResponse);
    }
}