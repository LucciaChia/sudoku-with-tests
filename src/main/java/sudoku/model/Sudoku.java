package sudoku.model;


import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sudoku.console.ConsoleDisplayer;
import sudoku.console.Displayer;
import sudoku.exceptions.IllegalSudokuStateException;

import java.util.ArrayList;
import java.util.List;


/**
 * Sudoku class:
 *  - creates all objects
 *  - validate inserted sudoku matrix if it contains - incorrect input numbers - only 0-9 ara allowed
 *                                                   - more than one number of a kind in row, column and box
 * @author Lucia
 */
@Getter
public class Sudoku {
    //TODO tests for Sudoku
    private static final Logger LOGGER = LoggerFactory.getLogger(Sudoku.class);
    private static final Displayer consoleDisplayer = new ConsoleDisplayer();

    private List<Box> boxes = new ArrayList<>();
    private List<Column> columns = new ArrayList<>();
    private List<Row> rows = new ArrayList<>();

    // highest strategy type used to reach current sudoku
    // set to LOW by default
    @Setter
    private StrategyType sudokuLevelType = StrategyType.LOW;

    public Sudoku() {

    }

    /**
     * Constructor method that takes matrix of initial values. Constructor first validates the input data,
     * fills in actual values and generates possibilities for empty cells.
     *
     * @param data  an array of actual values of the cells. An empty cell is represented by 0.
     * @throws IllegalSudokuStateException  An exception is thrown when data breaks the sudoku rules
     * @see sudoku.model.Cell
     */
    public Sudoku(int[][] data) throws IllegalSudokuStateException{

        validateNumbers(data);
        createSudokuElementObjectsService(data);
        validateRepetition();
        reducePossibilities();
    }

    public Sudoku copy(){
        int[][] data = new int[9][9];
        Sudoku result;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                data[i][j] = this.getRows().get(i).getCell(j).getActualValue();
            }
        }
        try {
            result =  new Sudoku(data);
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    result.getRows().get(i).getCellList().get(j).getCellPossibilities().clear();
                    for (Integer value: this.getRows().get(i).getCellList().get(j).getCellPossibilities()) {
                        result.getRows().get(i).getCellList().get(j).getCellPossibilities().add(value);
                    }
                }
            }
        } catch (IllegalSudokuStateException ex) {
            consoleDisplayer.displayLine("Empty sudoku");

            return new Sudoku();
        }
        result.setSudokuLevelType(this.sudokuLevelType);

        return result;
    }

    /**
     * Method that fills in the values from input matrix to rows columns and boxes.
     *
     * @param data  an array of actual values of the cells. An empty cell is represented by 0.
     * @see sudoku.model.Box
     * @see sudoku.model.Column
     * @see sudoku.model.Row
     */
    //nazov zly
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

    /**
     * method that validates matrix of initial values. Valid values are those from interval [0, 9]
     *
     * @param data  an array of actual values of the cells. An empty cell is represented by 0.
     * @throws IllegalSudokuStateException  An exception is thrown when data contains value/s outside of interval [0,9]
     */
    private void validateNumbers(int[][] data) throws IllegalSudokuStateException{
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (data[i][j] < 0 || data[i][j] > 9) {
                    throw new IllegalSudokuStateException(data[i][j], i, j);
                }
            }
        }
    }

    /**
     * Method that validates runs validation on each row, column and box
     *
     * @throws IllegalSudokuStateException  An exception rise when initial values (other than zero) is not unique
     *                                      in row, column or box, thus breaking the sudoku rules
     */
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
                if (cell.getActualValue() > 0) {
                    cell.deletePossibilities();
                }
            }
        }
    }



    /**
     * Checks if all cells have set a non zero actual value. If there is no cell with actual value zero
     * then there is nothing more to solve.
     *
     * @return      boolean that is true if a cell with actual value set to zero does not exist
     */
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

    /**
     * Print actual value of all cells in form 9x9 matrix for an user to see current state.
     */
    public void print() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                consoleDisplayer.display(this.getRows().get(i).getCell(j).getActualValue() + " ");
            }
            consoleDisplayer.displayLine("");
        }
        consoleDisplayer.displayLine("******************");
    }

    /**
     * Print possibilities of all cells for an user to see current state. Cells with actual value not zero has
     * possibilities equal to an empty list.
     */
    public void printPossibilitiesInSudoku() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                consoleDisplayer.displayLine(this.getRows().get(i).getCell(j).toString());
            }
            consoleDisplayer.displayLine("");
        }
    }

    /**
     * Print actual value of all cells in form of 9x9 matrix to a string that is returned.
     *
     * @return      string containing actual values of all cells
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                s.append(this.getRows().get(i).getCell(j).getActualValue()).append(" ");
            }
            s.append("\n");
        }
        s.append("******************");

        return s.toString();
    }
}
