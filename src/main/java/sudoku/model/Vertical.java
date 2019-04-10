package sudoku.model;

import java.util.ArrayList;
import java.util.List;

public class Vertical extends SudokuElement {

    public Vertical() {

    }

    public Vertical(ArrayList<Cell> row) {
        super.setCellList(row);
    }

    public Cell getCellInVertical(int i) {
        return super.getCellList().get(i);
    }

    @Override
    public String toString() {
        String output = "";
        for (int i = 0; i < 9; i++) {
            output += "\n" + super.getCellList().get(i).getActualValue();
        }
        return output;
    }

    @Override
    public List<Cell> getCells() {
        return super.getCellList();
    }

}
