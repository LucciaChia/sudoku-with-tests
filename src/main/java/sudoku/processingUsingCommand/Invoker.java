package sudoku.processingUsingCommand;

import sudoku.stepHandlers.Step;

import java.util.List;

/*
 * Invoker represents
 */
public interface Invoker {

    Command getPreviousState();
    Command getNextState();

    Command solvingStepsOrder();

    Step getPreviousStep();
    Step getNextStep();
    List<Step> solvingStepsOrderLucia();
}
