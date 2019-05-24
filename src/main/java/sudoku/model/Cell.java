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

    /**
     * Constructor method that sets variables to input parameters. If an actual value is zero, adds numbers 1 to 9 to possibilities.
     *
     * @param actualValue represents final number 1 to 9 in sudoku. Zero represents that this cells final number is still unknown
     * @param i cell's coordinate
     * @param j cell's coordinate
     * @param row a row which this cell belongs to
     * @param column a column which this cell belongs to
     * @param box a box which this cell belongs to
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
        this.actualValue = actualValue;
        if (actualValue != 0) {
            this.cellPossibilities.clear();
            deletePossibilities();
        }

    }

    /**
     * method that tells cell that there is one less candidate to actual value thus it is deleted
     * from list of candidates (possibilities)
     *
     * @param possibilityToDelete Integer value that is to be deleted from list of possibilities
     */
    public  void deletePossibility(Integer possibilityToDelete) {
        cellPossibilities.remove(possibilityToDelete);
    }

    /**
     *
     */
    public void deletePossibilities() {

        for(int i = 0; i < 9; i++) {
            row.getCell(i).deletePossibility(actualValue);
            column.getCell(i).deletePossibility(actualValue);
            box.getCellList().get(i).deletePossibility(actualValue);
        }
    }



    /**
     * Print actual value of all cells in a box to a string.
     *
     * @return string containing actual values of all cells in a box
     */
    @Override
    public String toString() {

        StringBuilder s = new StringBuilder();
        for (Integer cellPossibility : cellPossibilities) {
            s.append(cellPossibility).append(", ");
        }
        return "value: " + actualValue + ", i=" + i + ", j=" + j + " possibilities: " + s;
    }
}