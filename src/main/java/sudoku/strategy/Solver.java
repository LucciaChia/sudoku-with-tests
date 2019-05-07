package sudoku.strategy;

import sudoku.model.Sudoku;

import java.util.ArrayList;
import java.util.List;


/*
 * solver sets desired strategies to be used while solving a sudoku a decides in what order they will be used
 */

//zbytocny - invoker ho nahradzaju
public class Solver {
    private List<Resolvable> strategies;

    /**
     * Solver default constructor
     */
    public Solver() {

    }

    /**
     * arbitrary amount of strategies setter
     * @param useStrategies
     */
    public void setStrategies(Resolvable ... useStrategies) {
        this.strategies = new ArrayList<>();
        for (Resolvable strategy: useStrategies) {
            this.strategies.add(strategy);
        }
    }

    /**
     * makes decision when particular solving strategy will be used and if the sudoku can be resolved with
     * set methods
     * @param sudoku
     * @return Sudoku
     */
    public Sudoku useStrategies(Sudoku sudoku) {
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
