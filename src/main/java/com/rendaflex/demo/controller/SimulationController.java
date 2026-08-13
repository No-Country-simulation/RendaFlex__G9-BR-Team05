package com.rendaflex.demo.controller;

//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//import jakarta.validation.Valid;

//import com.rendaflex.demo.dto.request.SimulationRequest;
//import com.rendaflex.demo.dto.response.SimulationResponse;
import com.rendaflex.demo.validation.SimulationValidator;
//import com.rendaflex.demo.dto.response.SimulationResponse;

@RestController
@RequestMapping("/api/v1/expense-simulations")
public class SimulationController {
    private final SimulationValidator simulationValidator;
    //private final SimulationService SimulationService;

    public SimulationController(SimulationValidator simulationValidator/*, SimulationService simulationService*/) {
        this.simulationValidator = simulationValidator;
        //this.simulationService = simulationService;
    }

/*@PostMapping
public ResponseEntity<SimulationResponse> simulateSpending(@Valid @RequestBody SimulationRequest request){

    simulationValidator.validate(request);
    SimulationResponse response = simulationService.processSimulation(request);
    return ResponseEntity.ok(response);

}*/
}