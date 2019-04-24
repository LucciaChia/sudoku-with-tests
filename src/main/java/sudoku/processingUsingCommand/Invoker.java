package sudoku.processingUsingCommand;

public interface Invoker {

    Command getStepState(int step);
    Command getPreviousState();
    Command getNextState();

    void solvingMethodsCallOrder();

}
