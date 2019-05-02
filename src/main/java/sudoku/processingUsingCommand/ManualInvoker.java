package sudoku.processingUsingCommand;

import sudoku.model.Sudoku;
import sudoku.processingUsingStrategy.NakedSingleInACell;
import sudoku.processingUsingStrategy.Resolvable;
import sudoku.stepHandlers.OneChangeStep;
import sudoku.stepHandlers.Step;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ManualInvoker implements Invoker {
//    private static final Logger LOGGER = LoggerFactory.getLogger(ManualInvoker.class);

    private List<Command> commands = new LinkedList<>();
    private List<Step> stepListFromAllUsedMethods = new ArrayList<>();
    private List<Resolvable> strategies = new ArrayList<>();
//    private Resolvable nextStrategy = new BacktrackLucia(); // default next strategy will be backtrack
    private int currentStep = 0;
    private Sudoku sudoku;

//    public ManualInvoker() {}

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

//    public List<Step> getStepListFromAllUsedMethods() {
//        return stepListFromAllUsedMethods;
//    }

    public Sudoku getSudoku() {
        return sudoku;
    }

//    public List<Command> getCommands() {
//        return commands;
//    }

    @Override
    public Command getPreviousState() {
        Command command = null;
        Step step = null;
        Resolvable resolvable = null;
        Sudoku sudoku = null;

//        if (commands.size() > 0) {
//            command = commands.get(commands.size() - 1);
//            commands.remove(commands.size() - 1);
//        }

        if (currentStep > 0) {
            step = stepListFromAllUsedMethods.get(stepListFromAllUsedMethods.size() - 1);  // TODO ja si musim urobit poriadok v tom ktore steps som si uchoval a ktore zahodim
            stepListFromAllUsedMethods.remove(stepListFromAllUsedMethods.size() - 1);
            currentStep--;
            resolvable = ((OneChangeStep)step).getResolvable();
            sudoku = ((OneChangeStep)step).getSudoku();
            command = new CommandPicker(resolvable, sudoku);
        }

        return command;
    }

    @Override
    public Command getNextState() {

        // TODO ja si musim urobit poriadok v tom ktore steps som si uchoval a ktore zahodim

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


//    public List<String> getMethodUsedInAllStep() {
//        return null;
//    }

}
