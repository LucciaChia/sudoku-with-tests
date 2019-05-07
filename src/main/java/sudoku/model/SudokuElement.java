package sudoku.model;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sudoku.exceptions.IllegalSudokuStateException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sudoku.ANSIColour.ANSI_BLUE;
import static sudoku.ANSIColour.ANSI_RESET;

/**
 * SudokuElement can represent whole row (Row class), whole column (Column class)
 * or whole box (Box class)
 * @author Lucia
 */
@Getter @Setter
public abstract class SudokuElement {

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

    /**
     * Method that validates actual values of the sudoku so that every number other than zero is unique
     * in its column, row and box
     *
     * @throws IllegalSudokuStateException  an exception risen if number other that zero is not unique
     *                                      in its row, column or box
     */
    public void validateRepetition() throws IllegalSudokuStateException {

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
    /**
     * check the amount of particular possibility in a sudoku element - row / column / box
     *
     * @return      a map containing a number of occurrence of each possibility
     */
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
     * delletion of a possibility from possibilities of the cell
     *
     * @param value     a value that is to be deleted
     * @param cell      a cell whose possibilities is to be changed
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
     * Checks and removes possibilities from an input cell if a cell in same SudokuElement has that number as an actual
     * value
     *
     * @param cell  cell whose possibilities are checked
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

    /**
     * Method, used by PointingPairsInCell strategy, that checks and removes an input possibility from possibilities
     * of the cells in the same row or column but not th same box as input cell
     *
     * @param cell                              a cell whose row or column was searched
     * @param possibilityToCheck                a value that was searched for
     * @param deletedPossibilitiesWithLocation  a map containing possibilities that were deleted and their location
     * @return                                  boolean that says whether a possibility was deleted
     */
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

    /**
     * Method that checks if a cell from different box but same row/column, that has an input possibility
     * among its possibilities, exists. Used by PointingPairsInCell strategy.
     *
     * @param cell                      a cell whose row or column is searched
     * @param possibilityToCheck        value that is checked among possibilities
     * @return                          boolean that is answer whether cell with an input possibility exists
     */
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
