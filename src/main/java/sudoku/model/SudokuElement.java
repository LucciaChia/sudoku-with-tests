package sudoku.model;

import java.util.List;

public abstract class SudokuElement {
    /**
     * ziska cely Sudoku Element (cely riadok, stlpec, stvorec)
     * @return List<Cell>
     */
    public abstract List<Cell> getCells();

}
