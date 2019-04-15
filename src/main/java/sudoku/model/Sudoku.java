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
    //TODO tests for Sudoku
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

    public void createSudokuElementObjectsService(int[][] data) {
        for (int i = 0; i < data.length; i++) {
            Row row = new Row();
            for (int j = 0; j < data[i].length; j++) {
                Cell cell = new Cell(data[i][j], i, j);
                row.getCellList().add(cell);

                if (i == 0) {
                    Column column = new Column();
                    columns.add(column);
                    columns.get(j).getCellList().add(cell);
                } else {
                    columns.get(j).getCellList().add(cell);
                }

                if (shouldCreateSquare(i, j)) {
                    Box box = new Box();
                    boxes.add(box);
                }
                boxes.get((i/3)*3 + j/3).getCellList().add(cell);
            }
            rows.add(row);
        }
    }

    // iba kvoli testu public, inak bolo private
    public boolean shouldCreateSquare(int i, int j) {
        return (i % 3 == 0) && (j % 3 == 0);
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
}
