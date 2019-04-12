package sudoku.processing;

import sudoku.model.Cell;
import sudoku.model.SudokuElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lucia
 */
public class Util {
    /**
     * pre vybrany Sudoku Element (Row, Column, Box) najde kolko jednotiek, dvojek, ... atd
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
        List<Cell> listOfCells = sudokuElement.getCellList();
        for (int i = 0; i < 9; i++) {
            List<Integer> possibility = listOfCells.get(i).getCellPossibilities(); // moznosti v bunke
            if (possibility != null) {
                for (int j = 0; j < possibility.size(); j++) {
                    if (!countOfPossibilities.containsKey(possibility.get(j))) {
                        countOfPossibilities.put(possibility.get(j), 1);
                    } else {
                        int key = possibility.get(j);
                        countOfPossibilities.put(key, countOfPossibilities.get(key) + 1);
                    }
                }
            }
        }
        return countOfPossibilities;
    }
}
