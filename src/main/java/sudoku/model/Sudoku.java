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

    private List<Square> squares = new ArrayList<>();
    private List<Vertical> verticals = new ArrayList<>();
    private List<Row> rows = new ArrayList<>();

    public Sudoku() {

    }

    public Sudoku(int[][] data) throws IllegalSudokuStateException{

        validateNumbers(data);
        createSudokuElementObjectsService(data);
        validateRepetition();
    }

    public List<Square> getSquares() {
        return squares;
    }

    public void setSquares(List<Square> squares) {
        this.squares = squares;
    }

    public List<Vertical> getVerticals() {
        return verticals;
    }

    public void setVerticals(List<Vertical> verticals) {
        this.verticals = verticals;
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
                row.getCells().add(cell);

                if (i == 0) {
                    Vertical vertical = new Vertical();
                    verticals.add(vertical);
                    verticals.get(j).getCells().add(cell);
                } else {
                    verticals.get(j).getCells().add(cell);
                }


                if (shouldCreateSquare(i, j)) {
                    Square square = new Square();
                    squares.add(square);
                }

                squares.get((i/3)*3 + j/3).getCells().add(cell);
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
            verticals.get(i).validateRepetition();
            squares.get(i).validateRepetition();
        }
    }

}
