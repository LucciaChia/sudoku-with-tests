package sudoku.model;

import java.util.ArrayList;
import java.util.List;

/**
 * represents one box in sudoku matrix
 * @author Lucia
 */
public class Square extends SudokuElement {
    public Square() {
    }

    public Square(ArrayList<Cell> cellsInSquare) {
        super.setCellList(cellsInSquare);
    }

    @Override
    public List<Cell> getCells() {
        return super.getCellList();
    }

    @Override
    public String toString() {
        String output = "";
        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                output +=  " " + super.getCellList().get(index).getActualValue();
                index++;
            }
            output += "\n";
        }

        return output;
    }

}
