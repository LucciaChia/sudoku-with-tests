package sudoku.model;

import java.util.ArrayList;
import java.util.List;

public abstract class SudokuElement {
    private List<Cell> cellList = new ArrayList<>();

    /**
     * ziska cely Sudoku Element (cely riadok, stlpec, stvorec)
     * @return List<Cell>
     */
    public abstract List<Cell> getCells();

    public List<Cell> getCellList() {
        return cellList;
    }

    protected void setCellList(ArrayList<Cell> cellsInSquare) {
        cellList = cellsInSquare;
    }
}
