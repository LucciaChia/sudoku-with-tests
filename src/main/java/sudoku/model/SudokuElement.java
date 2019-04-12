package sudoku.model;

import sudoku.customExceptions.IllegalSudokuStateException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SudokuElement can represent whole row (Row class), whole column (Vertical class)
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

    public void validateRepetition() throws IllegalSudokuStateException{

        Map<Integer, Integer> repetition = new HashMap<>();

        for (int i = 0; i < 9; i++) {
            Cell cell = cellList.get(i);
            int key = cell.getActualValue();
            if ((key != 0) && !(repetition.containsKey(key))) {
                repetition.put(key, 1);
            } else if (repetition.containsKey(key)) {
                System.out.println("Row");
                throw new IllegalSudokuStateException(key,cell.getI(),cell.getJ());
            }
        }
    }
}
