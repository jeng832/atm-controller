package com.bear.atm.controller.statemachine;

import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class StateMachineTest {

    @Autowired
    private StateMachine<States, Events> stateMachine;

    @Test
    public void testInitial() {
        Assertions.assertThat(stateMachine.getState().getId()).isEqualTo(States.IDLE);
    }

    @Test
    public void testNormalTransition() {
        stateMachine.sendEvent(Events.INSERT_CARD);
        Assertions.assertThat(stateMachine.getState().getId()).isEqualTo(States.PIN_CHECK);
        stateMachine.sendEvent(Events.VALID_PIN);
        Assertions.assertThat(stateMachine.getState().getId()).isEqualTo(States.ACCOUNT_SELECTION);
        stateMachine.sendEvent(Events.SELECT_ACCOUNT);
        Assertions.assertThat(stateMachine.getState().getId()).isEqualTo(States.ACCOUNT_INFO);
        stateMachine.sendEvent(Events.DONE);
        Assertions.assertThat(stateMachine.getState().getId()).isEqualTo(States.IDLE);
    }

    @Test
    public void testInvalidPin() {
        stateMachine.sendEvent(Events.INSERT_CARD);
        Assertions.assertThat(stateMachine.getState().getId()).isEqualTo(States.PIN_CHECK);
        stateMachine.sendEvent(Events.INVALID_PIN);
        Assertions.assertThat(stateMachine.getState().getId()).isEqualTo(States.PIN_CHECK2);
        stateMachine.sendEvent(Events.INVALID_PIN);
        Assertions.assertThat(stateMachine.getState().getId()).isEqualTo(States.PIN_CHECK3);
        stateMachine.sendEvent(Events.INVALID_PIN);
        Assertions.assertThat(stateMachine.getState().getId()).isEqualTo(States.IDLE);
    }

    @Test
    public void testWrongTransitionEvent() {
        Assertions.assertThat(stateMachine.getState().getId()).isEqualTo(States.IDLE);
        stateMachine.sendEvent(Events.DONE);
        Assertions.assertThat(stateMachine.getState().getId()).isEqualTo(States.IDLE);
    }

    @Test
    public void testTransitionCancelEvent() {
        Assertions.assertThat(stateMachine.getState().getId()).isEqualTo(States.IDLE);
        stateMachine.sendEvent(Events.CANCEL);
        Assertions.assertThat(stateMachine.getState().getId()).isEqualTo(States.IDLE);

        stateMachine.sendEvent(Events.INSERT_CARD);
        Assertions.assertThat(stateMachine.getState().getId()).isEqualTo(States.PIN_CHECK);
        stateMachine.sendEvent(Events.CANCEL);
        Assertions.assertThat(stateMachine.getState().getId()).isEqualTo(States.IDLE);

        stateMachine.sendEvent(Events.INSERT_CARD);
        stateMachine.sendEvent(Events.VALID_PIN);
        Assertions.assertThat(stateMachine.getState().getId()).isEqualTo(States.ACCOUNT_SELECTION);
        stateMachine.sendEvent(Events.CANCEL);
        Assertions.assertThat(stateMachine.getState().getId()).isEqualTo(States.IDLE);

        stateMachine.sendEvent(Events.INSERT_CARD);
        stateMachine.sendEvent(Events.VALID_PIN);
        stateMachine.sendEvent(Events.SELECT_ACCOUNT);
        Assertions.assertThat(stateMachine.getState().getId()).isEqualTo(States.ACCOUNT_INFO);
        stateMachine.sendEvent(Events.CANCEL);
        Assertions.assertThat(stateMachine.getState().getId()).isEqualTo(States.IDLE);

        stateMachine.sendEvent(Events.INSERT_CARD);
        stateMachine.sendEvent(Events.INVALID_PIN);
        Assertions.assertThat(stateMachine.getState().getId()).isEqualTo(States.PIN_CHECK2);
        stateMachine.sendEvent(Events.CANCEL);
        Assertions.assertThat(stateMachine.getState().getId()).isEqualTo(States.IDLE);

        stateMachine.sendEvent(Events.INSERT_CARD);
        stateMachine.sendEvent(Events.INVALID_PIN);
        stateMachine.sendEvent(Events.INVALID_PIN);
        Assertions.assertThat(stateMachine.getState().getId()).isEqualTo(States.PIN_CHECK3);
        stateMachine.sendEvent(Events.CANCEL);
        Assertions.assertThat(stateMachine.getState().getId()).isEqualTo(States.IDLE);
    }

}