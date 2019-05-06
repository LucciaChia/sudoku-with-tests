package sudoku.model;

import lombok.Getter;
import lombok.Setter;

/**
 * represents one row in sudoku matrix
 * @author Lucia
 */
@Getter @Setter
public class Row extends SudokuElement {

    public Cell getCell(int i) {
        return super.getCellList().get(i);
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            output.append(" ").append(super.getCellList().get(i).getActualValue());
        }
        return output.toString();
    }
}