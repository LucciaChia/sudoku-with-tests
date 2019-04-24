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
    public void execute() {
        resolvable.resolveSudoku(sudoku);
    }
}
