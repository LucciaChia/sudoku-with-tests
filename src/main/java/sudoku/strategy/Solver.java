package sudoku.strategy;

import sudoku.exceptions.NoAvailableSolutionException;
import sudoku.model.Sudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Solver sets desired strategies to be used while solving a sudoku a decides in what order they will be used
 * Solver has the same function like Invokers
 */

public class Solver {
    private List<Resolvable> strategies;

    /**
     * Solver default constructor
     */
    public Solver() {

    }

    /**
     * arbitrary amount of strategies setter
     * @param useStrategies - particular strategies
     */
    public void setStrategies(Resolvable ... useStrategies) {
        this.strategies = new ArrayList<>();
        Collections.addAll(this.strategies, useStrategies);
    }

    /**
     * makes decision when particular solving strategy will be used and if the sudoku can be resolved with
     * set methods
     * @param sudoku - sudoku before using strategy
     * @return Sudoku
     */
    public Sudoku useStrategies(Sudoku sudoku) throws NoAvailableSolutionException {
        boolean updatedByStrategy;
        do {
            updatedByStrategy = false;
            for (Resolvable strategy : this.strategies) {
                sudoku = strategy.resolveSudoku(sudoku);
                if (sudoku.isSudokuResolved()) {
                    return sudoku;
                }
                updatedByStrategy = updatedByStrategy || strategy.isUpdated();
            }
        } while (updatedByStrategy);
        if (!sudoku.isSudokuResolved()) {
            System.out.println("Sudoku needs more advanced methods to be completely resolved");
        }
        return sudoku;
    }
}