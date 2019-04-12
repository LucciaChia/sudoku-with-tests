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
    private List<Horizontal> horizontals = new ArrayList<>();

    public Sudoku() {

    }

    public Sudoku(int[][] data) throws IllegalSudokuStateException{

        ArrayList<List<? extends SudokuElement>> sudokuElementsList;
        validateNumbers(data);
        sudokuElementsList = createSudokuElementObjectsService(data);
        validateRepetition(sudokuElementsList);
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

    public List<Horizontal> getHorizontals() {
        return horizontals;
    }

    public void setHorizontals(List<Horizontal> horizontals) {
        this.horizontals = horizontals;
    }

    public ArrayList<List<? extends SudokuElement>> createSudokuElementObjectsService(int[][] data) {
        for (int i = 0; i < data.length; i++) {
            Horizontal horizontal = new Horizontal();
            for (int j = 0; j < data[i].length; j++) {
                Cell cell = new Cell(data[i][j], i, j);
                horizontal.getCells().add(cell);

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
            horizontals.add(horizontal);
        }
        // <? extends sudoku.model.SudokuElement> pre vsetky objekty, ktore dedia od sudoku.model.SudokuElement
        ArrayList<List<? extends SudokuElement>> sudokuElements = new ArrayList<>();

        sudokuElements.add(horizontals);
        sudokuElements.add(verticals);
        sudokuElements.add(squares);
        return sudokuElements;
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

    private void validateRepetition(ArrayList<List<? extends SudokuElement>> sudokuElementsList) throws IllegalSudokuStateException {
        List<Horizontal> horizontals = (List<Horizontal>) sudokuElementsList.get(0);
        List<Vertical> verticals = (List<Vertical>) sudokuElementsList.get(1);
        List<Square> squares = (List<Square>) sudokuElementsList.get(2);

        for (int i = 0; i < 9; i++) {
            horizontals.get(i).validateRepetition();
            verticals.get(i).validateRepetition();
            squares.get(i).validateRepetition();
        }
    }


}
