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

    public Solver(Resolvable resolvable) {
        this.resolvable = resolvable;
    }

    public Solver(List<Resolvable> strategies) {
        this.strategies = strategies;
    }

    public void setStrategies(Resolvable ... useStrategies) {
        this.strategies = new ArrayList<>();
        for (Resolvable strategy: useStrategies) {
            this.strategies.add(strategy);
        }
    }

    public void useStrategies(Sudoku sudoku) {
        do {
//            nakedSingleInACell.resolveSudoku(sudoku);
//            System.out.println(" N ");
//            if (!Solver.sudokuWasChanged) {
//                hiddenSingleInACell.resolveSudoku(sudoku);
//                System.out.println(" H ");
//            }
//
//            if (!Solver.sudokuWasChanged) {
//                pointingPairsInCell.resolveSudoku(sudoku);
//                System.out.println(" P ");
//            }
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
