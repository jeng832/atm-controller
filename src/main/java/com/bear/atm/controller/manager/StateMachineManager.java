package com.bear.atm.controller.manager;

import com.bear.atm.controller.statemachine.Events;
import com.bear.atm.controller.statemachine.States;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

@Component
public class StateMachineManager {

    private StateMachine<States, Events> stateMachine;

    @Autowired
    public StateMachineManager(StateMachine<States, Events> stateMachine) {
        this.stateMachine = stateMachine;
    }

    public States getCurrentState() {
        return stateMachine.getState().getId();
    }

    public States sendEvent(Events event) {
        stateMachine.sendEvent(event);
        return stateMachine.getState().getId();
    }

}
