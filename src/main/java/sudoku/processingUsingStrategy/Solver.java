package sudoku.processingUsingStrategy;

import sudoku.model.Sudoku;

public class Solver {
    public static boolean sudokuWasChanged = false;

    private Resolvable resolvable;

    public Solver(Resolvable resolvable) {
        this.resolvable = resolvable;
    }

    // konstruktor so vsetkymi objektami rieseni param List<Resolvable>

    public void resolveSudoku(Sudoku sudoku) {
        resolvable.resolveSudoku(sudoku);
    }
}
