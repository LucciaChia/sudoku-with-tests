package sudoku.processing;

import sudoku.model.*;

import java.util.ArrayList;
import java.util.List;

public class SudokuService {

    public List<Vertical> verticals = new ArrayList<>();
    public List<Horizontal> horizontals = new ArrayList<>();
    public List<Square> squares = new ArrayList<>();

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

    public List<Square> getSquares() {
        return squares;
    }

    public void setSquares(List<Square> squares) {
        this.squares = squares;
    }

    public void resolveSudokuService() {
        System.out.println("POSSIBILITIES: ");
        Solution solution = new Solution(verticals, horizontals, squares);
        List<Horizontal> horizontals = solution.outputWITHOUTdataArray();

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

    public int[][] printSudokuMatrixService(){
        int[][] tempData = new int[9][9];
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                tempData[i][j] = horizontals.get(i).getCellInHorizontal(j).getActualValue();
                System.out.print(horizontals.get(i).getCellInHorizontal(j).getActualValue()+ " ");
            }
            System.out.println();
        }
        return tempData;
    }
}
