package sudoku.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
/**
 * represents one cell in sudoku matrix
 * @author Lucia
 */
@Getter @Setter
public class Cell {
    private int i;
    private int j;
    private int actualValue;
    private List<Integer> cellPossibilities;
    private Row row;
    private Column column;
    private Box box;

    public Cell(int actualValue, int i, int j, Row row, Column column, Box box) {
        this.i = i;
        this.j = j;
        this.actualValue = actualValue;
        this.row = row;
        this.column = column;
        this.box = box;
        cellPossibilities = new ArrayList<>();
        if (actualValue == 0) {
            for (int k = 1; k < 10; k++) {
                cellPossibilities.add(k);
            }
        }
    }

    public void setActualValue(int actualValue) {
        if (actualValue != 0) {
            this.cellPossibilities.clear();
        }
        this.actualValue = actualValue;
    }

    @Override
    public String toString() {

        String s = "";
        for (Integer cellPossibility : cellPossibilities) {
            s += cellPossibility + ", ";
        }
        return "value: " + actualValue + ", i=" + i + ", j=" + j + " possibilities: " + s;
    }
}
