package sudoku.processingUsingCommand;

import org.apache.log4j.Logger;
import sudoku.model.Sudoku;
import sudoku.processingUsingStrategy.BacktrackLucia;
import sudoku.processingUsingStrategy.Resolvable;
import sudoku.stepHandlers.Step;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AutomatedInvoker implements Invoker {
    private static final org.apache.log4j.Logger extAppLogFile = Logger.getLogger("ExternalAppLogger");
    private List<Command> commands = new LinkedList<>();
    private List<Step> stepList = new ArrayList<>();
    private List<Resolvable> strategies = new ArrayList<>();

    public void setStrategies(Resolvable ... useStrategies) {
        this.strategies = new ArrayList<>();
        for (Resolvable strategy: useStrategies) {
            this.strategies.add(strategy);
        }
    }

    private Sudoku sudoku;

    public Sudoku getSudoku() {
        return sudoku;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public AutomatedInvoker(Sudoku sudoku) {
        this.sudoku = sudoku;
//         by default only Backtrack Strategy will be used
        this.strategies.add(new BacktrackLucia());
    }

    @Override
    public Command solvingStepsOrder() {
        //CommandPicker commandPicker = new CommandPicker()
        Resolvable currentlyUsedMethod;

            for (int i = 0; i < strategies.size(); i++) {
                System.out.println(strategies.get(i).getName());
                Command command = new CommandPicker(strategies.get(i), sudoku);
                sudoku = command.execute();
                commands.add(command);
                if (sudoku.isSudokuResolved()) {
                    break;
                }
                if (strategies.get(i).isUpdated() && !sudoku.isSudokuResolved()) {
                    i=-1;
                }
            }
        if (!sudoku.isSudokuResolved()) {
            System.out.println("Sudoku needs more advanced methods to be completely resolved");
        }

        return null;
    }





    @Override
    public List<String> getMethodUsedInAllStep() {
        return null;
    }
    // TODO implement previous and next methods
    @Override
    public Command getPreviousState() {
//        int currentStep = commands.size() - 1;
//        if (currentStep - 1 >= 0) {
//            return commands.get(currentStep - 1);
//        } else {
//            extAppLogFile.info(getClass().getName() + " no previous element exists. First element has been returned.");
//            return commands.get(0);
//        }
        return null;
    }

    @Override
    public Command getPreviousState(int step) {
//        int currentStep = commands.size() - 1;
//        if (step >= 0 && step < currentStep) {
//            return commands.get(step);
//        } else {
//            extAppLogFile.info(getClass().getName() + " invalid previous index inserted. First element has been returned.");
//            return commands.get(0);
//        }
        return null;
    }

    @Override
    public Command getNextState() {
//        int currentStep = commands.size() - 1;
//        if (step >= 0 && step < currentStep) {
//            return commands.get(step);
//        } else {
//            extAppLogFile.info(getClass().getName() + " invalid previous index inserted. First element has been returned.");
//            return commands.get(0);
//        }
        return null;
    }

}
