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
     * Method that validates actual values of the sudoku so that every number other than zero is unique
     * in its SudokuElement
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
     * Method, used by PointingPairsInCell strategy, that checks and removes an input possibility from possibilities
     * of the cells in the same row or column but not th same box as input cell
     *
     * @param cell                              a cell whose row or column was searched
     * @param possibilityToCheck                a value that was searched for
     * @param deletedPossibilitiesWithLocation  a map containing possibilities that were deleted and their location
     * @return                                  boolean that says whether a possibility was deleted
     */

    //SudokuElement v nazve nema byt - je to nazov klasy
    //movnut je do strategy
    //ako tri implemetacie -
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
