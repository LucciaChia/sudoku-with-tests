package sudoku.command;

import lombok.Getter;
import sudoku.model.Sudoku;
import sudoku.step.OneChangeStep;
import sudoku.step.Step;
import sudoku.strategy.Resolvable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * CommandPicker runs given strategy on sudoku to achieve next state
 */
@Getter
public class CommandPicker implements Command {
//    Resolvable resolvable;
//    Sudoku sudoku;
    private List<Step> stepList = new ArrayList<>();

    /**
     * Constructor method that is given current state of sudoku and strategy that is to be used in next step
     *
     * @param resolvable    resolvable that is strategy chosen to be used in this step
     * @param sudoku        sudoku that is the current state
     */
    public CommandPicker(Resolvable resolvable, Sudoku sudoku) {
//        this.resolvable = resolvable;
//        this.sudoku = sudoku;
        stepList.add(new OneChangeStep(resolvable, sudoku));
    }

    /**
     * Choosen strategy will be executed and returns sudoku that should be closer to solution
     *
     * @return      Sudoku that is next state achieved by applying chosen strategy
     */
    @Override
    public Sudoku execute() {
        Sudoku sudoku = ((OneChangeStep) Objects.requireNonNull(getLastStep())).getSudoku();
        return ((OneChangeStep) getLastStep()).getResolvable().resolveSudoku(sudoku);
    }

    private Step getLastStep() {
        if (stepList.size() > 0) {
            return stepList.get(stepList.size() - 1);
        } else {
            return null;
        }
    }

    /**
     * Method that prints name of the chosen strategy and current state of the sudoku
     *
     * @return      a string containing strategy name and sudoku
     */
    @Override
    public String toString() {
        Sudoku sudoku = ((OneChangeStep) Objects.requireNonNull(getLastStep())).getSudoku();
        return ((OneChangeStep)getLastStep()).getResolvable().getName() + "\n" + sudoku.toString();
    }

    public Sudoku getSudoku() {
        return ((OneChangeStep) Objects.requireNonNull(getLastStep())).getSudoku();
    }
}

