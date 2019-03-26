package sudoku.processing;

import sudoku.model.Cell;
import sudoku.model.Possibility;
import sudoku.model.SudokuElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {

    public Map<Integer, Integer> amountOfParticularPossibilities(SudokuElement sudokuElement) {
        Map<Integer, Integer> countOfPossibilities = new HashMap<>();
        List<Cell> listOfCells = sudokuElement.retrieveCells();
        for (int i = 0; i < 9; i++) {
            Possibility possibility = listOfCells.get(i).getCellPossibilities(); // moznosti v bunke
            if (possibility != null) {
                for (int j = 0; j < possibility.getPosibilities().size(); j++) {
                    if (!countOfPossibilities.containsKey(possibility.getPosibilities().get(j))) {
                        countOfPossibilities.put(possibility.getPosibilities().get(j), 1);
                    } else {
                        int key = possibility.getPosibilities().get(j);
                        countOfPossibilities.put(key, countOfPossibilities.get(key) + 1);
                    }
                }
            }
        }
        return countOfPossibilities;
    }
}
