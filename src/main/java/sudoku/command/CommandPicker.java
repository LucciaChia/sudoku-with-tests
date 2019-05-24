package sudoku.command;

import lombok.Getter;
import lombok.Setter;
import sudoku.exceptions.NoAvailableSolutionException;
import sudoku.model.Sudoku;
import sudoku.strategy.Resolvable;

/**
 * CommandPicker runs given strategy on sudoku to achieve next state
 */
@Getter @Setter
public class CommandPicker implements Command {
    Resolvable resolvable;
    Sudoku sudoku;

    /**
     * Constructor method that is given current state of sudoku and strategy that is to be used in next step
     *
     * @param resolvable    resolvable that is strategy chosen to be used in this step
     * @param sudoku        sudoku that is the current state
     */
    public CommandPicker(Resolvable resolvable, Sudoku sudoku) {
        this.resolvable = resolvable;
        this.sudoku = sudoku;
    }

    /**
     * Choosen strategy will be executed and returns sudoku that should be closer to solution
     *
     * @return      Sudoku that is next state achieved by applying chosen strategy
     */
    @Override
    public Sudoku execute() throws NoAvailableSolutionException {
        return resolvable.resolveSudoku(sudoku);
    }

    /**
     * Method that prints name of the chosen strategy and current state of the sudoku
     *
     * @return      a string containing strategy name and sudoku
     */
    @Override
    public String toString() {
        return resolvable.getName() + "\n" + sudoku.toString();
    }

}

