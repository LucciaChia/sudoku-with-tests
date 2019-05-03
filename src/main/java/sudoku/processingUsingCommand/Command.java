package sudoku.processingUsingCommand;

import sudoku.model.Sudoku;

public interface Command {
    Sudoku execute();
}
