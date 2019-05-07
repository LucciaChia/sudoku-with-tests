package sudoku.command;

import sudoku.model.Sudoku;

/**
 * Command runs given strategy on sudoku to achieve next state
 */
public interface Command {

    /**
     * Given strategy will be executed and returns sudoku that should be closer to solution
     *
     * @return      Sudoku that is next state achieved by applying chosen strategy
     */
    Sudoku execute();
}
