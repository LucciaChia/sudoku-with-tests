package sudoku.exceptions;

import lombok.Getter;
import sudoku.model.Sudoku;
@Getter
public class NoAvailableSolution extends Exception {

    private Sudoku sudoku;

    public NoAvailableSolution(Sudoku sudoku) {
        this.sudoku = sudoku;
    }

    @Override
    public String toString() {
        return "No available solution for sudoku";
    }
}
