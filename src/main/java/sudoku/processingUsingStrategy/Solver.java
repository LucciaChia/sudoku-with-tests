package sudoku.processingUsingStrategy;

import sudoku.model.Sudoku;

import java.util.ArrayList;
import java.util.List;

public class Solver {
    public static boolean sudokuWasChanged = false;

    private Resolvable resolvable;
    private List<Resolvable> strategies;

    public Solver() {

    }

    public void setStrategies(Resolvable ... useStrategies) {
        this.strategies = new ArrayList<>();
        for (Resolvable strategy: useStrategies) {
            this.strategies.add(strategy);
        }
    }

    public void useStrategies(Sudoku sudoku) {
        do {
            for (Resolvable strategy: this.strategies) {
                strategy.resolveSudoku(sudoku);
                if (sudoku.isSudokuResolved()) {
                    return;
                }
            }

        } while (Solver.sudokuWasChanged);
        if (!sudoku.isSudokuResolved()) {
            System.out.println("Sudoku needs more advanced methods to be completely resolved");
        }
    }


    public void resolveSudoku(Sudoku sudoku) {
        resolvable.resolveSudoku(sudoku);
    }
}
