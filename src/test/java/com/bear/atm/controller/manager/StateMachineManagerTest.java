package com.bear.atm.controller.manager;

import com.bear.atm.controller.statemachine.Events;
import com.bear.atm.controller.statemachine.States;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
class StateMachineManagerTest {

    @Autowired
    private StateMachineManager stateMachineManager;

    @Test
    public void testGetCurrentState() {
        Assertions.assertThat(stateMachineManager.getCurrentState()).isEqualTo(States.IDLE);
    }

    @Test
    public void testSendEvent() {
        stateMachineManager.sendEvent(Events.INSERT_CARD);
        Assertions.assertThat(stateMachineManager.getCurrentState()).isEqualTo(States.PIN_CHECK);
        stateMachineManager.sendEvent(Events.INVALID_PIN);
        Assertions.assertThat(stateMachineManager.getCurrentState()).isEqualTo(States.PIN_CHECK2);
        stateMachineManager.sendEvent(Events.INVALID_PIN);
        Assertions.assertThat(stateMachineManager.getCurrentState()).isEqualTo(States.PIN_CHECK3);
        stateMachineManager.sendEvent(Events.VALID_PIN);
        Assertions.assertThat(stateMachineManager.getCurrentState()).isEqualTo(States.ACCOUNT_SELECTION);
        stateMachineManager.sendEvent(Events.SELECT_ACCOUNT);
        Assertions.assertThat(stateMachineManager.getCurrentState()).isEqualTo(States.ACCOUNT_INFO);
        stateMachineManager.sendEvent(Events.DONE);
        Assertions.assertThat(stateMachineManager.getCurrentState()).isEqualTo(States.IDLE);
    }

    @Test
    public void testCancelEvent() {
        stateMachineManager.sendEvent(Events.INSERT_CARD);
        Assertions.assertThat(stateMachineManager.getCurrentState()).isNotEqualTo(States.IDLE);
        stateMachineManager.sendEvent(Events.CANCEL);
        Assertions.assertThat(stateMachineManager.getCurrentState()).isEqualTo(States.IDLE);

        stateMachineManager.sendEvent(Events.INSERT_CARD);
        stateMachineManager.sendEvent(Events.VALID_PIN);
        Assertions.assertThat(stateMachineManager.getCurrentState()).isNotEqualTo(States.IDLE);
        stateMachineManager.sendEvent(Events.CANCEL);
        Assertions.assertThat(stateMachineManager.getCurrentState()).isEqualTo(States.IDLE);

        stateMachineManager.sendEvent(Events.INSERT_CARD);
        stateMachineManager.sendEvent(Events.VALID_PIN);
        stateMachineManager.sendEvent(Events.SELECT_ACCOUNT);
        Assertions.assertThat(stateMachineManager.getCurrentState()).isNotEqualTo(States.IDLE);
        stateMachineManager.sendEvent(Events.CANCEL);
        Assertions.assertThat(stateMachineManager.getCurrentState()).isEqualTo(States.IDLE);
    }
}