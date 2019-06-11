package sudoku.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {StateMachineConfiguration.class})
class StateMachineConfigurationTest {

    @Autowired
    private StateMachine<String, String> fSA; // finite state automaton

    @Test
    void stateTransitionFunction() {
        // GIVEN
        fSA.start();

        // WHEN
        fSA.sendEvent("other");

        // THEN
        assertEquals("mainMenu", fSA.getState().getId());
    }
}