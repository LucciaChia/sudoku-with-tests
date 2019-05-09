package sudoku.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sudoku.model.*;
import sudoku.step.OneChangeStep;
import sudoku.step.Step;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sudoku.ANSIColour.*;

/*
 * Variation 1. Reducing row or column
 * if a pair of empty cells (current value is 0 for both) within a box in the same row or column share a given
 * possibility and this possibility doesn't occur anywhere else in the box => this possibility will be removed from
 * all other cell's possibilities lists outside this box within the same row or column
 *
 * Variation 2. Reducing box candidates
 * if a pair of empty cells (current value is 0 for both) within a box in the same row / column share a given
 * possibility and this possibility doesn't occur anywhere else in the row / column outside the box, this possibility
 * will be removed from all other cell's possibilities within the box
 *
 * see example: http://www.sudoku-solutions.com/index.php?page=solvingInteractions
 */

class PointingPairsInCell implements Resolvable {

    private Map<int[], Integer> deletedPossibilitiesWithLocation = new HashMap<>();
    private Step step;
    private List<Step> stepList = new ArrayList<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(PointingPairsInCell.class);
    private boolean updatedInPointingPair = false;
    private String name = "2: PointingPairsInCell";

    public Step getStep() {
        return step;
    }

    @Override
    public List<Step> getStepList() {
        return stepList;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Sudoku resolveSudoku(Sudoku sudoku) {
        stepList.clear();
        updatedInPointingPair = false;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Cell cell = sudoku.getRows().get(i).getCell(j);
                if (cell.getActualValue() == 0) {
                    pointingPairInCells(sudoku, cell);
                }
            }
        }

        return sudoku;
    }

    @Override
    public boolean isUpdated() {
        return updatedInPointingPair;
    }

    //big methods
    private boolean pointingPairInCells(Sudoku sudoku, Cell cell) {
        int cellI = cell.getI();
        int cellJ = cell.getJ();
        Row cellRow = cell.getRow();
        Column cellColumn = cell.getColumn();
        Box cellBox = cell.getBox();
        List<Cell> eligiblePartnerCells;

        for (int possibilityToCheck : cell.getCellPossibilities()) {
            eligiblePartnerCells = findPartnerCell(cell, possibilityToCheck);

            for (Cell partnerCell : eligiblePartnerCells) {

                boolean changedInLoop = false;
                boolean iCase = cellI == partnerCell.getI();
                boolean jCase = cellJ == partnerCell.getJ();

                // vypisy
                LOGGER.info("Partner cell for cell possibility: " + possibilityToCheck + " in cell  i = " + cellI + " j = " + cellJ + " IS: i = " +
                        partnerCell.getI() + " j = " + partnerCell.getJ());

                // ak partner cella neobsahuje momentalne kontrolovanu possibilitu referencnej celly, chod prec t.j. -> do dalsej obratky na novu partner cellu
                if (!partnerCell.getCellPossibilities().contains((Integer)possibilityToCheck)) {
                    continue;
                }

                if (!isPossibilityToCheckPresentSomewhereElseInSquare(cell, partnerCell, cellBox.getCellList(), possibilityToCheck)) {
                    LOGGER.info(ANSI_GREEN + "Possibility " + possibilityToCheck + " presents only in row / column" + ANSI_RESET);
                    // ak je len v tomto riadku vyhadzem tu possibilitu z tohto riadka v ostatnych stvorcoch, ak v stlpci tak zo stlpca

                    if (iCase) {
                        changedInLoop = deletePossibilitiesInRowOrColumn(cell, possibilityToCheck, deletedPossibilitiesWithLocation, cellRow);
                    } else {
                        changedInLoop = deletePossibilitiesInRowOrColumn(cell, possibilityToCheck, deletedPossibilitiesWithLocation, cellColumn);
                    }

                } else { // nachadza sa aj inde vo stvorci
                    LOGGER.info(ANSI_RED + "Possibility " + possibilityToCheck + " presents somewhere else too" + ANSI_RESET);

                    if ((iCase && !isPossibilityToCheckPresentSomewhereElseInRowInColumn(cell, possibilityToCheck, cellRow)) ||
                            (jCase && !isPossibilityToCheckPresentSomewhereElseInRowInColumn(cell, possibilityToCheck, cellColumn))) {
                        changedInLoop = deletePossibilitiesInSquare(cell, partnerCell, possibilityToCheck);
                    }
                }

                if (changedInLoop) {
                    Sudoku sudokuCopy = sudoku.copy();
                    Map<int[], Integer> deletedPossibilitiesWithLocationCopy = new HashMap<>();
                    deletedPossibilitiesWithLocationCopy.putAll(deletedPossibilitiesWithLocation);
                    step = new OneChangeStep(sudokuCopy, name, cell, partnerCell, deletedPossibilitiesWithLocationCopy);
                    ((OneChangeStep)step).setResolvable(this);
                    //step.printStepPointingPair(cell, partnerCell, deletedPossibilitiesWithLocationCopy);
                    stepList.add(step);
                    updatedInPointingPair = true;
                    return updatedInPointingPair;
                }
            }
        }
        return updatedInPointingPair;
    }

    private List<Cell> findPartnerCell(Cell cell, int possibilityToCheck) {
        int indexI = cell.getI();
        int indexJ = cell.getJ();
        Box targetBox = cell.getBox();
        List<Cell> eligiblePartnerCellList = new ArrayList<>();

        for (Cell candidateCell : targetBox.getCellList()) {

            if (candidateCell.getActualValue() == 0 ) {
                int candidateCellI = candidateCell.getI();
                int candidateCellJ = candidateCell.getJ();

                if ((indexI == candidateCellI || indexJ == candidateCellJ) && cell != candidateCell) {
                    List<Integer> candidatePosibilities = candidateCell.getCellPossibilities();

                    if (candidatePosibilities.contains((Integer)possibilityToCheck)) {
                        eligiblePartnerCellList.add(candidateCell);
                    }
                }
            }
        }

        return eligiblePartnerCellList;
    }

    private boolean isPossibilityToCheckPresentSomewhereElseInSquare(Cell cell, Cell partnerCell, List<Cell> squareOfCells, int possibilityToCheck) {
        int cellI = cell.getI();
        int cellJ = cell.getJ();
        int partnerCellI = partnerCell.getI();
        int partnerCellJ = partnerCell.getJ();

        if (cellI == partnerCellI) {
            LOGGER.info("i case");
            for (Cell testedCell : squareOfCells) {
                if (testedCell.getActualValue() == 0 && testedCell.getI() != cellI && testedCell.getCellPossibilities().contains((Integer) possibilityToCheck)) {
                    return true;
                }
            }
        }
        if (cellJ == partnerCellJ) {
            LOGGER.info("j case");
            for (Cell testedCell : squareOfCells) {
                if (testedCell.getActualValue() == 0 && testedCell.getJ() != cellJ && testedCell.getCellPossibilities().contains((Integer) possibilityToCheck)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean deletePossibilitiesInSquare(Cell cell, Cell partnerCell, int possibilityToCheck) {
        deletedPossibilitiesWithLocation.clear();
        int cellI = cell.getI();
        int cellJ = cell.getJ();
        int partnerCellI = partnerCell.getI();
        int partnerCellJ = partnerCell.getJ();
        boolean somethingWasRemoved = false;
        Box cellBox = cell.getBox();

        if (cellI == partnerCellI || cellJ == partnerCellJ) {
            for (Cell testedCell : cellBox.getCellList()) {
                boolean isSameCoord = cellI == partnerCellI ? testedCell.getI() != cellI : testedCell.getJ() != cellJ;
                if (isSameCoord && testedCell.getCellPossibilities().contains((Integer)possibilityToCheck)) {
                    int[] possibilityLocation = {testedCell.getI(), testedCell.getJ()};
                    deletedPossibilitiesWithLocation.put(possibilityLocation, possibilityToCheck);
                    LOGGER.info(ANSI_PURPLE + "\t SQUARE CASE: Possibility " + possibilityToCheck + " will be removed from " +
                            "i=" + testedCell.getI() + " j=" + testedCell.getJ() + ANSI_RESET);
                    somethingWasRemoved = testedCell.getCellPossibilities().remove((Integer)possibilityToCheck);
                }
            }
        } else {
            LOGGER.info(ANSI_RED + "\tSQUARE CASE: Something's wrong - incorrect partner cell!" + ANSI_RESET);
        }
        return somethingWasRemoved;
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
    public boolean deletePossibilitiesInRowOrColumn(Cell cell, int possibilityToCheck, Map<int[], Integer> deletedPossibilitiesWithLocation, SudokuElement sudokuElement) {
        deletedPossibilitiesWithLocation.clear();
        boolean somethingWasRemoved = false;
        Box cellBox = cell.getBox();

        for (Cell testedCell : sudokuElement.getCellList()) {
            Box testedCellBox = testedCell.getBox();
            if (testedCell.getActualValue() == 0 && cellBox != testedCellBox && testedCell.getCellPossibilities().contains((Integer)possibilityToCheck)) {
                int[] possibilityLocation = {testedCell.getI(), testedCell.getJ()};
                deletedPossibilitiesWithLocation.put(possibilityLocation, possibilityToCheck);
                LOGGER.info(ANSI_BLUE + "\tROW-COLUMN CASE: Possibility " + possibilityToCheck + " will be removed from " +
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
    public boolean isPossibilityToCheckPresentSomewhereElseInRowInColumn(Cell cell, int possibilityToCheck, SudokuElement sudokuElement) {
        Box cellBox = cell.getBox();

        LOGGER.info("i or j case");
        for (Cell testedCell : sudokuElement.getCellList()) {
            if (testedCell.getActualValue() == 0 && cellBox != testedCell.getBox() &&
                    testedCell.getCellPossibilities().contains((Integer) possibilityToCheck)) {
                return true;
            }
        }

        return false;
    }
}