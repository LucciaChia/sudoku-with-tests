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
//    private static final String ANSI_RED = "\u001B[31m";
//    private static final String ANSI_GREEN = "\u001B[32m";
//    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
//    private static final String ANSI_PURPLE = "\u001B[35m";
//    private static final String ANSI_CYAN = "\u001B[36m";
//    private static final String ANSI_WHITE = "\u001B[37m";

    private static final Logger extAppLogFile = LoggerFactory.getLogger(SudokuElement.class);

    private List<Cell> cellList = new ArrayList<>();

    /**
     * retrieves whole Sudoku Element (whole row, column or square)
     * @return List<Cell>
     */

    public List<Cell> getCellList() {
        return cellList;
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
                for (Integer integer : possibility) {
                    if (!countOfPossibilities.containsKey(integer)) {
                        countOfPossibilities.put(integer, 1);
                    } else {
                        int key = integer;
                        countOfPossibilities.put(key, countOfPossibilities.get(key) + 1);
                    }
                }
            }
        }
        return countOfPossibilities;
    }

    public boolean deletePossibilitiesInRowOrColumnSudokuElement(Cell cell, int possibilityToCheck, Map<int[], Integer> deletedPossibilitiesWithLocation) {
        deletedPossibilitiesWithLocation.clear();
        boolean somethingWasRemoved = false;
        Box cellBox = cell.getBox();

        for (Cell testedCell : cellList) {
            Box testedCellBox = testedCell.getBox();
            if (testedCell.getActualValue() == 0 && cellBox != testedCellBox && testedCell.getCellPossibilities().contains(possibilityToCheck)) {
                int[] possibilityLocation = {testedCell.getI(), testedCell.getJ()};
                deletedPossibilitiesWithLocation.put(possibilityLocation, possibilityToCheck);
                extAppLogFile.info(ANSI_BLUE + "\tROW-COLUMN CASE: Possibility " + possibilityToCheck + " will be removed from " +
                        "i=" + testedCell.getI() + " j=" + testedCell.getJ() + ANSI_RESET);
                somethingWasRemoved = testedCell.getCellPossibilities().remove((Integer)possibilityToCheck);
            }
        }
        return somethingWasRemoved;
    }

    // musim to uz zavolat na spravnom Row / spravnom Column
    public boolean isPossibilityToCheckPresentSomewhereElseInRowInColumnSudokuElement(Cell cell, int possibilityToCheck) {
        Box cellBox = cell.getBox();

        extAppLogFile.info("i or j case");
        for (Cell testedCell : cellList) {
            if (testedCell.getActualValue() == 0 && cellBox != testedCell.getBox() &&
                    testedCell.getCellPossibilities().contains(possibilityToCheck)) {
                return true;
            }
        }

        return false;
    }
}
