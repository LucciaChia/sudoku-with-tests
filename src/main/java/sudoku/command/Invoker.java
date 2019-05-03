package sudoku.command;

import sudoku.step.Step;

import java.util.List;

public interface Invoker {

    Command getPreviousState();
    Command getNextState();

//    Command solvingStepsOrder();

    default Step getPreviousStep() { return null; }
    default Step getNextStep() { return null; }
    default List<Step> solvingStepsOrderLucia() { return null; }
}
