package sudoku.model;


import sudoku.customExceptions.IllegalSudokuStateException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Sudoku class:
 *  - creates all objects
 *  - validate inserted sudoku matrix if it contains - incorrect input numbers - only 0-9 ara allowed
 *                                                   - more than one number of a kind in row, column and box
 * @author Lucia
 */
public class Sudoku {
    private static final Logger LOGGER = Logger.getLogger(Sudoku.class.getName());

    private List<Box> boxes = new ArrayList<>();
    private List<Column> columns = new ArrayList<>();
    private List<Row> rows = new ArrayList<>();

    public Sudoku() {

    }

    public Sudoku(int[][] data) throws IllegalSudokuStateException{

        validateNumbers(data);
        createSudokuElementObjectsService(data);
        validateRepetition();
        reducePossibilities();
    }

    public Sudoku copy(){
        int[][] data = new int[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                data[i][j] = this.getRows().get(i).getCell(j).getActualValue();
            }
        }
        try {
            return new Sudoku(data);
        } catch (IllegalSudokuStateException ex) {
            System.out.printf("Empty sudoku");
            return new Sudoku();
        }

    }

    public List<Box> getBoxes() {
        return boxes;
    }

    public void setBoxes(List<Box> boxes) {
        this.boxes = boxes;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    private void createSudokuElementObjectsService(int[][] data) {

        for (int i = 0; i < 9; i++) {
            Row row = new Row();
            Column column = new Column();
            Box box = new Box();
            rows.add(row);
            columns.add(column);
            boxes.add(box);
        }

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                Cell cell = new Cell(data[i][j], i, j, rows.get(i), columns.get(j), boxes.get((i / 3) * 3 + j / 3));
                rows.get(i).getCellList().add(cell);
                columns.get(j).getCellList().add(cell);
                boxes.get((i / 3) * 3 + j / 3).getCellList().add(cell);
            }
        }
    }

    private void validateNumbers(int[][] data) throws IllegalSudokuStateException{
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (data[i][j] < 0 || data[i][j] > 9) {
                    throw new IllegalSudokuStateException(data[i][j], i, j);
                }
            }
        }
    }

    private void validateRepetition() throws IllegalSudokuStateException {
        for (int i = 0; i < 9; i++) {
            rows.get(i).validateRepetition();
            columns.get(i).validateRepetition();
            boxes.get(i).validateRepetition();
        }
    }

    private void reducePossibilities() {

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Cell cell = rows.get(i).getCell(j);
                if (cell.getActualValue() == 0) {
                    deletePossibilities(cell);
                }
            }
        }
    }

    private Cell deletePossibilities(Cell cell) {

        List<Integer> cellPossibilities = cell.getCellPossibilities();
        Row cellRow = cell.getRow();
        Column cellColumn = cell.getColumn();
        Box cellBox = cell.getBox();

        for (int i = 0; i < 9; i++) {
            int rowValue = cellRow.getCell(i).getActualValue();
            int colValue = cellColumn.getCell(i).getActualValue();
            int boxValue = cellBox.getCellList().get(i).getActualValue();

            if (rowValue != 0 && cellPossibilities.contains((Integer)rowValue)) {
                cellPossibilities.remove((Integer)rowValue);
            }
            if (colValue != 0 && cellPossibilities.contains((Integer)colValue)) {
                cellPossibilities.remove((Integer)colValue);
            }
            if (boxValue != 0 && cellPossibilities.contains((Integer)boxValue)) {
                cellPossibilities.remove((Integer)boxValue);
            }
        }
        return cell;
    }

    public boolean isSudokuResolved() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (rows.get(i).getCellList().get(j).getActualValue() == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void print() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(this.getRows().get(i).getCell(j).getActualValue() + " ");
            }
            System.out.println();
        }
        System.out.println("******************");
    }

    public void printPossibilitiesInSudoku() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.println(this.getRows().get(i).getCell(j).toString());
            }
            System.out.println();
        }
    }

    @Override
    public String toString() {
        String s = "";

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                s += this.getRows().get(i).getCell(j).getActualValue() + " ";
            }
            s += "\n";
        }
        s += "******************";

        return s;
    }
}
