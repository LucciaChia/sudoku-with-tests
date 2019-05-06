package sudoku.model;

import lombok.Getter;
import lombok.Setter;

/**
 * represents one box in sudoku matrix
 * @author Lucia
 */
@Getter @Setter
public class Box extends SudokuElement {

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                output.append(" ");
                output.append(getCellList().get(index).getActualValue());
                index++;
            }
            output.append("\n");
        }
        return output.toString();
    }
}