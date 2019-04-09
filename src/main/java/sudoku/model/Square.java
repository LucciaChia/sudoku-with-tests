package sudoku.model;

import java.util.ArrayList;
import java.util.List;

public class Square extends SudokuElement {
    //TODO cells dat do abstraktnej class
    private List<Cell> cellsInSquare = new ArrayList<>();

    public Square() {
    }

    public Square(ArrayList<Cell> cellsInSquare) {
        this.cellsInSquare = cellsInSquare;
    }

    @Override
    public List<Cell> getCells() {
        return cellsInSquare;
    }

    @Override
    public String toString() {
        String output = "";
        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                output +=  " " + cellsInSquare.get(index).getActualValue();
                index++;
            }
            output += "\n";
        }

        return output;
    }

}
