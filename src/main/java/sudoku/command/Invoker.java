package sudoku.command;

public interface Invoker {

    Command getPreviousState();
//    Command getPreviousState(int step);
    Command getNextState();

    Command solvingStepsOrder();
//    List<String> getMethodUsedInAllStep();
}