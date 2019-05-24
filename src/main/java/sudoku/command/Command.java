package sudoku.command;

import sudoku.exceptions.NoAvailableSolutionException;
import sudoku.model.Sudoku;
import sudoku.strategy.Resolvable;

/**
 * Command runs given strategy on sudoku to achieve next state
 */
public interface Command {

    /**
     * Given strategy will be executed and returns sudoku that should be closer to solution
     *
     * @return      Sudoku that is next state achieved by applying chosen strategy
     */
    Sudoku execute() throws NoAvailableSolutionException;

    void setSudoku(Sudoku sudoku);

    Sudoku getSudoku();

    Resolvable getResolvable();
}
