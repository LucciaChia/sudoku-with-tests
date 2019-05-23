package sudoku.strategy;

import sudoku.model.StrategyType;
import sudoku.exceptions.NoAvailableSolutionException;
import sudoku.model.Sudoku;

/**
 * resolvable has to be implemented by all strategies used while trying to resolve sudoku
 */
public interface Resolvable {

    /**
     * this method should contain solving logic
     * @param sudoku - sudoku beofre using a strategy
     * @return Sudoku
     */
    Sudoku resolveSudoku(Sudoku sudoku) throws NoAvailableSolutionException;

    /**
     * info if sudoku was changed in particular step with particular method
     * @return boolean
     */
    boolean isUpdated();

    /**
     * solving strategy name
     * @return String
     */
    String getName();

    StrategyType getType();
}
