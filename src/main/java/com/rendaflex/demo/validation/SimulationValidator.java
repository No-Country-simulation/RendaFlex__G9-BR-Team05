package com.rendaflex.demo.validation;

import org.springframework.stereotype.Component;

import com.rendaflex.demo.dto.request.SimulationRequest;

@Component
public class SimulationValidator {

    public void validate(SimulationRequest request){
        if (request.amount() == null || request.installments() == null){
            return;
        }
        //incorporates business rules.
    }
    
}
