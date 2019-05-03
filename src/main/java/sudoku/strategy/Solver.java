package sudoku.strategy;

import sudoku.model.Sudoku;

import java.util.ArrayList;
import java.util.List;

public class Solver {
    private List<Resolvable> strategies;

    public Solver() {

    }

    public void setStrategies(Resolvable ... useStrategies) {
        this.strategies = new ArrayList<>();
        for (Resolvable strategy: useStrategies) {
            this.strategies.add(strategy);
        }
    }

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
