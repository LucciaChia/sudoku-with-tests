package sudoku.processingUsingCommand;

import java.util.LinkedList;
import java.util.List;

public class AutomatedInvoker implements Invoker {

    List<Command> commands = new LinkedList<>();

    @Override
    public List<String> getMethodUsedInAllStep() {
        return null;
    }

    @Override
    public Command getPreviousState() {
        return null;
    }

    @Override
    public Command getPreviousState(int step) {
        return null;
    }

    @Override
    public Command getNextState() {
        return null;
    }

    @Override
    public Command solvingStepsOrder() {
        return null;
    }
}
