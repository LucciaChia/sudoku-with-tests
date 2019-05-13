package sudoku.command;


import lombok.Getter;
import lombok.Setter;
import sudoku.model.Sudoku;
//import sudoku.step.Step;
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

    public AutomatedInvoker(Sudoku sudoku) {
        this.sudoku = sudoku;
        // by default only Backtrack Strategy is used
        this.strategies.add(strategyFactory.createBacktrackStrategy());
    }

     public List<Command> solvingStepsOrder() {

            for (int i = 0; i < strategies.size(); i++) {

//                Sudoku oldSudoku = sudoku.copy();
                Command command = new CommandPicker(strategies.get(i), sudoku.copy());

                sudoku = command.execute();

                if (strategies.get(i).isUpdated() || sudoku.isSudokuResolved()) {
//                    oldSudoku = sudoku.copy();
//                    ((CommandPicker)command).setSudoku(oldSudoku);
                    commands.add(command);
                    currentStep++;
                }

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

//    @Override
//    public Step getPreviousStep() {
//        return null;
//    }

//    @Override
//    public Step getNextStep() {
//        return null;
//    }
}
