package sudoku.command;

import sudoku.model.Sudoku;

public interface Command {
    Sudoku execute();
}
