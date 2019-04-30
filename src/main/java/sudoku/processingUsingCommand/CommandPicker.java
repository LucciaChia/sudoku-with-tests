package sudoku.processingUsingCommand;

import sudoku.model.Sudoku;
import sudoku.processingUsingStrategy.Resolvable;

public class CommandPicker implements Command {
    Resolvable resolvable;

    Sudoku sudoku;

    public CommandPicker(Resolvable resolvable, Sudoku sudoku) {
        this.resolvable = resolvable;
        this.sudoku = sudoku;
    }
    @Override
    public Sudoku execute() {
        return resolvable.resolveSudoku(sudoku);
    }

    @Override
    public String toString() {
        return resolvable.getName() + "\n" + sudoku.toString();
    }

    public Sudoku getSudoku() {
        return sudoku;
    }
}

