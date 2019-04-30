package sudoku.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    private static final Logger extAppLogFile = LoggerFactory.getLogger(SudokuElement.class);

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

    public Map<Integer, Integer> amountOfParticularPossibilities() {
        Map<Integer, Integer> countOfPossibilities = new HashMap<>();
        List<Cell> listOfCells = cellList;
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

    public boolean deletePossibilitiesInRowOrColumnSudokuElement(Cell cell, int possibilityToCheck, Map<int[], Integer> deletedPossibilitiesWithLocation) {
        deletedPossibilitiesWithLocation.clear();
        boolean somethingWasRemoved = false;
        Box cellBox = cell.getBox();

        for (Cell testedCell : cellList) {
            Box testedCellBox = testedCell.getBox();
            if (testedCell.getActualValue() == 0 && cellBox != testedCellBox && testedCell.getCellPossibilities().contains((Integer)possibilityToCheck)) {
                int[] possibilityLocation = {testedCell.getI(), testedCell.getJ()};
                deletedPossibilitiesWithLocation.put(possibilityLocation, possibilityToCheck);
                extAppLogFile.info(ANSI_BLUE + "\tROW-COLUMN CASE: Possibility " + possibilityToCheck + " will be removed from " +
                        "i=" + testedCell.getI() + " j=" + testedCell.getJ() + ANSI_RESET);
                somethingWasRemoved = testedCell.getCellPossibilities().remove((Integer)possibilityToCheck);
            }
        }
        return somethingWasRemoved;
    }

    private Box findCorrectBox( List<Box> boxes, int rowIndex, int columnIndex) {
        return boxes.get((rowIndex/3)*3 + columnIndex/3);
    }

    // musim to uz zavolat na spravnom Row / spravnom Column
    public boolean isPossibilityToCheckPresentSomewhereElseInRowInColumnSudokuElement(Cell cell, int possibilityToCheck) {
        Box cellBox = cell.getBox();

        extAppLogFile.info("i or j case");
        for (Cell testedCell : cellList) {
            if (testedCell.getActualValue() == 0 && cellBox != testedCell.getBox() &&
                    testedCell.getCellPossibilities().contains((Integer) possibilityToCheck)) {
                return true;
            }
        }

        return false;
    }
}
