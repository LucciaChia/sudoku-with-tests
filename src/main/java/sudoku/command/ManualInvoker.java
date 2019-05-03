package sudoku.command;

import sudoku.model.Sudoku;
import sudoku.step.OneChangeStep;
import sudoku.step.Step;
import sudoku.strategy.NakedSingleInACell;
import sudoku.strategy.Resolvable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ManualInvoker implements Invoker {
//    private static final Logger LOGGER = LoggerFactory.getLogger(ManualInvoker.class);

    private List<Command> commands = new LinkedList<>();
    private List<Step> stepListFromAllUsedMethods = new ArrayList<>();
    private List<Resolvable> strategies = new ArrayList<>();
    private int currentStep = 0;
    private Sudoku sudoku;

    public ManualInvoker(Sudoku sudoku) {
        this.sudoku = sudoku;
////         by default only Backtrack Strategy will be used
//        this.strategies.add(new BacktrackLucia());
//      default strategy will be NakedSingleCell
        this.strategies.add(new NakedSingleInACell());
    }

    public void setStrategies(Resolvable ... useStrategies) {
        this.strategies = new ArrayList<>();
        this.strategies.addAll(Arrays.asList(useStrategies));
    }

    public Sudoku getSudoku() {
        return sudoku;
    }

    @Override
    public Command getPreviousState() {
        CommandPicker command = null;
        Step step;
        Resolvable resolvable;
        Sudoku sudoku;

        if (currentStep > 0) {
            step = stepListFromAllUsedMethods.get(stepListFromAllUsedMethods.size() - 1);
            stepListFromAllUsedMethods.remove(stepListFromAllUsedMethods.size() - 1);
            currentStep--;
            resolvable = ((OneChangeStep)step).getResolvable();
            sudoku = ((OneChangeStep)step).getSudoku();
            command = new CommandPicker(resolvable, sudoku);
        }
        if (command != null) {
//            LOGGER.info("getPreviousState: current state is " + command.getStepList().get(0).toString());
        }

        return command;
    }

    @Override
    public Command getNextState() {

        CommandPicker command = new CommandPicker(strategies.get(0), sudoku);

        if (!sudoku.isSudokuResolved()) {
            sudoku = command.execute();
            Step oneStep = strategies.get(0).getStepList().get(0);
            stepListFromAllUsedMethods.add(oneStep);
            currentStep++;
            command = new CommandPicker(strategies.get(0), ((OneChangeStep) oneStep).getSudoku());
            commands.add(command);
        }
//        LOGGER.info("getNextState: current state is " + command.getStepList().get(0).toString());

        return command;
    }

    public Command getNextState(Resolvable strategy) {
        setStrategies(strategy);

        return getNextState();
    }

//    @Override
//    public Command solvingStepsOrder() {
//        return null;
//    }
}
