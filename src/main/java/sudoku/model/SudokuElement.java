package sudoku.model;

import sudoku.customExceptions.IllegalSudokuStateException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SudokuElement can represent whole row (Row class), whole column (Column class)
 * or whole box (Box class)
 * @author Lucia
 */
public abstract class SudokuElement {
    private List<Cell> cellList = new ArrayList<>();

    /**
     * retrieves whole Sudoku Element (whole row, column or square)
     * @return List<Cell>
     */

    public List<Cell> getCellList() {
        return cellList;
    }

    public void setCellList(ArrayList<Cell> cellsInSquare) {
        cellList = cellsInSquare;
    }

    public void validateRepetition() throws IllegalSudokuStateException{

        Map<Integer, Integer> repetition = new HashMap<>();

        for (int i = 0; i < 9; i++) {
            Cell cell = cellList.get(i);
            int key = cell.getActualValue();
            if ((key != 0) && !(repetition.containsKey(key))) {
                repetition.put(key, 1);
            } else if (repetition.containsKey(key)) {
                System.out.println("Row");
                throw new IllegalSudokuStateException(key,cell.getI(),cell.getJ());
            }
        }
    }
    // removePossibilityFrom: Row / Column / Box - refactored from class Solution

    /**
     * vymazanie konkretnej hodnoty z moznosti konkretnej celly
     * @param value
     * @param cell
     */
    public void removePossibility(int value, Cell cell) { // odoberanie potencialnych moznosti z ciell v riadku / stlpci / stvorci
        for (int i = 0; i < 9; i++) {
            List<Integer> possibilities = cell.getCellPossibilities();
            if (possibilities != null) {
                possibilities.remove((Integer) value);
            }
        }
    }

    // search: Row / Column / Box - refactored from class Solution
    /**
     * kontrolujem danu bunku a jej porencialne moznosti.
     * prechadzam bud cez riadok, stlpec alebo stovrec, v ktorom je. Ak najdem v riadku bunku, ktora uz ma realnu hodnotu
     * t. j. napr.: 4 a v moje bunke, ktoru kontrolujem je v potencialnych moznostiach cislo 4, tak ho odstranim
     * @param cell
     */
    public List<Integer> search(Cell cell) { // odoberanie potencialnych moznosti z ciell v riadku
        List<Integer> possibility = cell.getCellPossibilities();
        for (int i = 0; i < 9; i++) {
            int checkValue = cellList.get(i).getActualValue();
            if (checkValue != 0) {
                possibility.remove((Integer) checkValue);
            }
        }
        return possibility;
    }
}
