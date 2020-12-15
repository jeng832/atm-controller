package com.bear.atm.controller.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

@Configuration
@EnableStateMachine
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<States, Events> {

    private Logger logger = LoggerFactory.getLogger(StateMachineConfig.class);

    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config) throws Exception {
        super.configure(config);
        config.withConfiguration()
                .autoStartup(true)
                .listener(listener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
        super.configure(states);
        states.withStates()
                .initial(States.IDLE)
                .states(EnumSet.allOf(States.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
        super.configure(transitions);
        transitions
                .withExternal()
                    .source(States.IDLE).target(States.PIN_CHECK).event(Events.INSERT_CARD)
                    .and()
                .withExternal()
                    .source(States.PIN_CHECK).target(States.ACCOUNT_SELECTION).event(Events.VALID_PIN)
                    .and()
                .withExternal()
                    .source(States.PIN_CHECK).target(States.PIN_CHECK2).event(Events.INVALID_PIN)
                    .and()
                .withExternal()
                    .source(States.PIN_CHECK2).target(States.ACCOUNT_SELECTION).event(Events.VALID_PIN)
                    .and()
                .withExternal()
                    .source(States.PIN_CHECK2).target(States.PIN_CHECK3).event(Events.INVALID_PIN)
                    .and()
                .withExternal()
                    .source(States.PIN_CHECK3).target(States.ACCOUNT_SELECTION).event(Events.VALID_PIN)
                    .and()
                .withExternal()
                    .source(States.PIN_CHECK3).target(States.IDLE).event(Events.INVALID_PIN)
                    .and()
                .withExternal()
                    .source(States.ACCOUNT_SELECTION).target(States.ACCOUNT_INFO).event(Events.SELECT_ACCOUNT)
                    .and()
                .withExternal()
                    .source(States.ACCOUNT_INFO).target(States.IDLE).event(Events.DONE)
                    .and()
                .withExternal()
                    .source(States.PIN_CHECK).target(States.IDLE).event(Events.CANCEL)
                    .and()
                .withExternal()
                    .source(States.PIN_CHECK2).target(States.IDLE).event(Events.CANCEL)
                    .and()
                .withExternal()
                    .source(States.PIN_CHECK3).target(States.IDLE).event(Events.CANCEL)
                    .and()
                .withExternal()
                    .source(States.ACCOUNT_SELECTION).target(States.IDLE).event(Events.CANCEL)
                    .and()
                .withExternal()
                    .source(States.ACCOUNT_INFO).target(States.IDLE).event(Events.CANCEL);
    }

    @Bean
    public StateMachineListener<States, Events> listener() {
        return new StateMachineListenerAdapter<States, Events>() {
            @Override
            public void stateChanged(State<States, Events> from, State<States, Events> to) {
                if (from != null) {
                    logger.info("[StateMachine] State change from " + from.getId() + " to " + to.getId());
                } else {
                    logger.info("[StateMachine] State change to " + to.getId());
                }
            }
        };
    }
}
