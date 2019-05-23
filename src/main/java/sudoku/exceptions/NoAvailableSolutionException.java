package sudoku.exceptions;

import lombok.Getter;
import sudoku.ANSIColour;
import sudoku.model.Sudoku;
@Getter
public class NoAvailableSolutionException extends Exception implements ANSIColour {

    private Sudoku sudoku;

    public NoAvailableSolutionException(Sudoku sudoku) {
        this.sudoku = sudoku;
    }

    @Override
    public String toString() {
        return ANSI_RED + "NoAvailableSolutionException  No available solution for this sudoku" + ANSI_RESET;
    }
}
