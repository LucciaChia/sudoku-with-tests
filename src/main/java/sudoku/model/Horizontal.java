package sudoku.model;

import java.util.List;

public class Horizontal extends SudokuElement {

    public Cell getCellInHorizontal(int i) {
        return super.getCellList().get(i);
    }

    @Override
    public String toString() {
        String output = "";
        for (int i = 0; i < 9; i++) {
            output += " " + super.getCellList().get(i).getActualValue();
        }
        return output;
    }

    @Override
    public List<Cell> getCells() {
        return super.getCellList();
    }

}
