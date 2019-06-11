package sudoku.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.Arrays;
import java.util.HashSet;

@Configuration
@EnableStateMachine
public class StateMachineConfiguration extends StateMachineConfigurerAdapter<String, String> {

    @Override
    public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {

        states
            .withStates()
            .initial("mainMenu")
            .end("end")
            .states(new HashSet<String>(Arrays.asList(
                "mainMenu",
                "end",
                "stepByStepAutomatedInvoker",
                "stepByStepManualInvoker",
                "stepByStepManualInvokerNext"
            )));

    }

    @Override
    public void configure(StateMachineTransitionConfigurer<String, String> transitions) throws Exception {

        transitions.withExternal()
            .source("mainMenu").target("end").event("5").and()
            .withExternal()
            .source("mainMenu").target("mainMenu").event("1").and()
            .withExternal()
            .source("mainMenu").target("mainMenu").event("2").and()
            .withExternal()
            .source("mainMenu").target("mainMenu").event("other").and()
            .withExternal()
            .source("mainMenu").target("StepByStepAutomatedInvoker").event("3").and()
            .withExternal()
            .source("mainMenu").target("StepByStepManualInvoker").event("4").and()
            .withExternal()
            .source("StepByStepAutomatedInvoker").target("mainMenu").event("end").and()
            .withExternal()
            .source("StepByStepAutomatedInvoker").target("StepByStepAutomatedInvoker").event("help").and()
            .withExternal()
            .source("StepByStepAutomatedInvoker").target("StepByStepAutomatedInvoker").event("next").and()
            .withExternal()
            .source("StepByStepAutomatedInvoker").target("StepByStepAutomatedInvoker").event("prev").and()
            .withExternal()
            .source("StepByStepAutomatedInvoker").target("StepByStepAutomatedInvoker").event("all").and()
            .withExternal()
            .source("StepByStepAutomatedInvoker").target("StepByStepAutomatedInvoker").event("other").and()
            .withExternal()
            .source("StepByStepManualInvoker").target("mainMenu").event("end").and()
            .withExternal()
            .source("StepByStepManualInvoker").target("StepByStepManualInvoker").event("help").and()
            .withExternal()
            .source("StepByStepManualInvoker").target("StepByStepManualInvokerNext").event("next").and()
            .withExternal()
            .source("StepByStepManualInvoker").target("StepByStepManualInvoker").event("prev").and()
            .withExternal()
            .source("StepByStepManualInvoker").target("StepByStepManualInvoker").event("other").and()
            .withExternal()
            .source("StepByStepManualInvokerNext").target("StepByStepManualInvokerNext").event("help").and()
            .withExternal()
            .source("StepByStepManualInvokerNext").target("StepByStepManualInvoker").event("naked").and()
            .withExternal()
            .source("StepByStepManualInvokerNext").target("StepByStepManualInvoker").event("hidden").and()
            .withExternal()
            .source("StepByStepManualInvokerNext").target("StepByStepManualInvoker").event("pair1").and()
            .withExternal()
            .source("StepByStepManualInvokerNext").target("StepByStepManualInvoker").event("pair2").and()
            .withExternal()
            .source("StepByStepManualInvokerNext").target("StepByStepManualInvoker").event("end").and()
            .withExternal()
            .source("StepByStepManualInvokerNext").target("StepByStepManualInvokerNext").event("other").and();
    }
}
