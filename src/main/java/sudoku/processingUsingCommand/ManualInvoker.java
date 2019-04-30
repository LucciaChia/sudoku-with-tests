package sudoku.processingUsingCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sudoku.model.Sudoku;
import sudoku.processingUsingStrategy.BacktrackLucia;
import sudoku.processingUsingStrategy.Resolvable;
import sudoku.stepHandlers.OneChangeStep;
import sudoku.stepHandlers.Step;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ManualInvoker implements Invoker {
    private static final Logger extAppLogFile = LoggerFactory.getLogger(ManualInvoker.class);

    private List<Command> commands = new LinkedList<>();
    private List<Step> stepListFromAllUsedMethods = new ArrayList<>();
    private List<Resolvable> strategies = new ArrayList<>();
//    private Resolvable nextStrategy = new BacktrackLucia(); // default next strategy will be backtrack
    private int currentStep = 0;
    private Sudoku sudoku;

    public ManualInvoker() {}

    public ManualInvoker(Sudoku sudoku) {
        this.sudoku = sudoku;
//         by default only Backtrack Strategy will be used
        this.strategies.add(new BacktrackLucia());
    }

    public void setStrategies(Resolvable ... useStrategies) {
        this.strategies = new ArrayList<>();
        for (Resolvable strategy: useStrategies) {
            this.strategies.add(strategy);
        }
    }

    public List<Step> getStepListFromAllUsedMethods() {
        return stepListFromAllUsedMethods;
    }

    public Sudoku getSudoku() {
        return sudoku;
    }

    public List<Command> getCommands() {
        return commands;
    }

    @Override
    public Command getPreviousState() {
        Command command = null;

        if (commands.size() > 0) {
            command = commands.get(commands.size() - 1);
            commands.remove(commands.size() - 1);
        }

        return command;
    }

    @Override
    public Command getPreviousState(int step) {
        Command command = null;
        List<Command> tmpCommands = new ArrayList<>();


//        if (commands.size() > step) {
//            command = commands.get(step);
//            for(int i = 0; i < step; i++) {
//                tmpCommands.add(commands.get(i));
//            }
//            commands.clear();
//            commands.addAll(tmpCommands);
//        } else {
//            command = commands.get(0);
//            commands.clear();
//            commands.add(command);
//        }

        return command;
    }

    @Override
    public Command getNextState() {
        Command command = new CommandPicker(strategies.get(0), sudoku);

        if (!sudoku.isSudokuResolved()) {
            sudoku = command.execute();
            Step oneStep = strategies.get(0).getStepList().get(0);
            stepListFromAllUsedMethods.add(oneStep);
            currentStep++;
            command = new CommandPicker(strategies.get(0), ((OneChangeStep) oneStep).getSudoku());
            commands.add(command);
        }

        return command;
    }

    @Override
    public Command solvingStepsOrder() {
        return null;
    }

    @Override
    public List<String> getMethodUsedInAllStep() {
        return null;
    }
}
