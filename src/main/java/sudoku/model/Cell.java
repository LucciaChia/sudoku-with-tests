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
    private Row row;
    private Column column;
    private Box box;

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public Row getRow() {
        return row;
    }

    public Column getColumn() {
        return column;
    }

    public Box getBox() {
        return box;
    }

    /**
     * Constructor method that sets variables to input parameters. If an actual value is zero, adds numbers 1 to 9
     * to possibilities.
     *
     * @param actualValue   represents final number 1 to 9 in sudoku. Zero represents that this cells final number
     *                      is still unknown
     * @param i             cell's coordinate
     * @param j             cell's coordinate
     * @param row           a row which this cell belongs to
     * @param column        a column which this cell belongs to
     * @param box           a box which this cell belongs to
     */
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

    /**
     * Sets an actual value. If new value is not zero, clears possibilities.
     */
    public void setActualValue(int actualValue) {
        if (actualValue != 0) {
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

    /**
     * Print actual value of all cells in a box to a string.
     *
     * @return      string containing actual values of all cells in a box
     */
    @Override
    public String toString() {

        String s = "";
        for (Integer cellPossibility : cellPossibilities) {
            s += cellPossibility + ", ";
        }
        return "value: " + actualValue + ", i=" + i + ", j=" + j + " possibilities: " + s;
    }
}
