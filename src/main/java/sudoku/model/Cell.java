package sudoku.model;

import static java.util.stream.Collectors.joining;

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
        //example of stream and lambda
        String collect = cellPossibilities.stream().map(Object::toString).collect(joining(", "));
        return "value: " + actualValue + ", i=" + i + ", j=" + j + " possibilities: " + collect;
    }
}
