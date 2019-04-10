package sudoku.model;

import java.util.ArrayList;
import java.util.List;
/**
 * represents one cell in sudoku matrix
 * @author Lucia
 */
public class Cell {
    private int i;
    private int j;
    private int actualValue;
    private List<Integer> cellPossibilities;

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public Cell(int actualValue, int i, int j) {
        this.i = i;
        this.j = j;
        this.actualValue = actualValue;
        cellPossibilities = new ArrayList<>();
        if (actualValue == 0) {
            for (int k = 1; k < 10; k++) {
                cellPossibilities.add(k);
            }
        }
    }

    public void setActualValue(int actualValue) {
        if (actualValue != 0) {
            //cellPossibilities = null;// !!!!!!!
            this.cellPossibilities.clear();
        }
        this.actualValue = actualValue;
    }

    public List<Integer> getCellPossibilities() {
        return cellPossibilities;
    }

    public void setCellPossibilities(List<Integer> cellPossibilities) {
        this.cellPossibilities = cellPossibilities;
    }


    public int getActualValue() {
        return actualValue;
    }

    @Override
    public String toString() {

        String s = "";
        for (int i = 0; i < cellPossibilities.size(); i++) {
            s += cellPossibilities.get(i) + ", ";
        }
        return "value: " + actualValue + ", i=" + i + ", j=" + j + " possibilities: " + s;
    }
}
