package sudoku.model;

import lombok.Getter;
import lombok.Setter;

/**
 * represents one column in sudoku matrix
 * @author Lucia
 */
@Getter @Setter
public class Column extends SudokuElement {

    public Cell getCell(int i) {
        return super.getCellList().get(i);
    }

    /**
     * Print actual value of all cells in a column to a string.
     *
     * @return      string containing actual values of all cells in a column
     */
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            output.append("\n").append(super.getCellList().get(i).getActualValue());
        }
        return output.toString();
    }
}