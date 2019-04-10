package sudoku.processing;

import sudoku.model.*;

import java.util.ArrayList;
import java.util.List;

public class SudokuService {

    public List<Vertical> verticals = new ArrayList<>();
    public List<Horizontal> horizontals = new ArrayList<>();
    public List<Square> squares = new ArrayList<>();

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

    public void resolveSudokuService(int[][] data) {
        System.out.println("POSSIBILITIES: ");
        Solution solution = new Solution(verticals, horizontals, squares, data);
        List<Horizontal> horizontals = solution.output();

        for (int i = 0; i < horizontals.size(); i++) {
            for (int j = 0; j < 9; j++) {
                Cell cell= horizontals.get(i).getCellInHorizontal(j);
                if (cell.getActualValue() == 0) {
                    System.out.print(i + ":" + j + " = ");
                    System.out.println(cell.toString());
                }
            }
        }
    }

    public static void printSudokuMatrixService(int[][] data){
        for (int i=0; i<data.length; i++) {
            for (int j=0; j<data[i].length; j++) {
                System.out.print(data[i][j] + " ");
            }
            System.out.println();
        }
    }
}
