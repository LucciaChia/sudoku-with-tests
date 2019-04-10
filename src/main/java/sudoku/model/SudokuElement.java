package sudoku.model;

import java.util.ArrayList;
import java.util.List;

/**
 * SudokuElement can represent whole row (Horizontal class), whole column (Vertical class)
 * or whole box (Square class)
 * @author Lucia
 */
public abstract class SudokuElement {
    private List<Cell> cellList = new ArrayList<>();

    /**
     * retrieves whole Sudoku Element (whole row, column or square)
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
