package sudoku.processing;

import sudoku.model.Cell;
import sudoku.model.Possibility;
import sudoku.model.SudokuElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {
    /**
     * pre vybrany Sudoku Element (Horizontal, Vertical, Square) najde kolko jednotiek, dvojek, ... atd
     * sa v nom nachadza
     *
     * @param sudokuElement
     * @return Map<Integer,Integer,>
     *     Map<Integer, Integer>
     *     key = hodnota -> t. j. co to je -> jednotky, dvojky ... , deviatky
     *     value = pocet kusov jednotiek, dvojek ... v danom elemente => v riadku (horizontal)
     *                                                                   v stlpci (vertical)
     *                                                                   v square (vo stvorci)
     */
    public Map<Integer, Integer> amountOfParticularPossibilities(SudokuElement sudokuElement) {
        Map<Integer, Integer> countOfPossibilities = new HashMap<>();
        List<Cell> listOfCells = sudokuElement.getCells();
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
