package sudoku.command;


import lombok.Getter;
import lombok.Setter;
import sudoku.exceptions.NoAvailableSolutionException;
import sudoku.model.Sudoku;
import sudoku.strategy.Resolvable;
import sudoku.strategy.StrategyFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/*
 * uses set strategies in order to resolve a sudoku.
 * the simples strategy will be used as much as possible, if there's no progress with one method, then more
 * sophisticated method will be called
 */

// TODO in the future the steps forwards and backwards will be implemented with methods returning Command object
@Getter @Setter
public class AutomatedInvoker implements Invoker {
    private StrategyFactory strategyFactory = new StrategyFactory();
    private List<Command> commands = new LinkedList<>();

    private List<Resolvable> strategies = new ArrayList<>();
    private int currentStep = -1;

    public void setStrategies(Resolvable ... useStrategies) {
        this.strategies = new ArrayList<>();
        this.strategies.addAll(Arrays.asList(useStrategies));
    }

    private Sudoku sudoku;

    public AutomatedInvoker(Sudoku sudoku, Resolvable ... useStrategies) throws NoAvailableSolutionException {
        this.sudoku = sudoku;
        setStrategies(useStrategies);
        solvingStepsOrder();
    }

     protected List<Command> solvingStepsOrder() throws NoAvailableSolutionException {

        int strategyNumber = -1;
         do {
             strategyNumber++;
             Command command = new CommandPicker(strategies.get(strategyNumber), sudoku.copy());
             sudoku = command.execute();
             command.setSudoku(sudoku);
             if (strategies.get(strategyNumber).isUpdated() || sudoku.isSudokuResolved()) {
                 commands.add(command);
             }
             if (strategies.get(strategyNumber).isUpdated() && !sudoku.isSudokuResolved()) {
                 // choosing first strategy
                 strategyNumber=-1;
             }
         } while (!sudoku.isSudokuResolved());

        if (!sudoku.isSudokuResolved()) {
            throw new NoAvailableSolutionException(sudoku);
        }

        return commands;
    }

    @Override
    public Command getNextState() {
        currentStep++;
        int lastStepIndex = commands.size()-1;
        if (currentStep > lastStepIndex) {
            currentStep = lastStepIndex;
        }
        return commands.get(currentStep);
    }

    @Override
    public Command getPreviousState() {
        currentStep--;
        if (currentStep < 0) {
            currentStep = 0;
        }
        return commands.get(currentStep);
    }

}
