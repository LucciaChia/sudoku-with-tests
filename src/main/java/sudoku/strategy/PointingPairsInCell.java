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
                    if (pointingPairInCells(sudoku, cell)){
                        return sudoku;
                    }
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

                LOGGER.info("Partner cell for cell possibility: " + possibilityToCheck + " in cell  i = " + cellI + " j = " + cellJ + " IS: i = " +
                        partnerCell.getI() + " j = " + partnerCell.getJ());

                // ak partner cella neobsahuje momentalne kontrolovanu possibilitu referencnej celly
                // pokracujem, kym nenajdem taku partnerCellu, ktora obsahuje momentalne testovanu possibilitu
                if (!partnerCell.getCellPossibilities().contains(possibilityToCheck)) {
                    continue;
                }


                if (!isPossibilityToCheckPresentSomewhereElseInBox(cell, partnerCell, cellBox.getCellList(), possibilityToCheck)) {

                    // possibilita sa nenachadza inde v boxe => vymazem moznosti z ciel v riadku alebo v stlpci.
                    // iCase: vymazem z ciel ktore su v tom istom riadku ako je cell a parterCell, ale su v inom boxe ako je cell a partnerCell
                    // jCase: vymazem z ciel ktore su v tom istom stlpci ako je cell a parterCell, ale su v inom boxe ako je cell a partnerCell
                    LOGGER.info(ANSI_GREEN + "Possibility " + possibilityToCheck + " presents only in row / column" + ANSI_RESET);
                    changedInLoop = reduceRowOrColumnCandidates(cell, partnerCell, cellRow, cellColumn, possibilityToCheck, iCase); // ------------------

                } else {
                    // possibilita sa nachadza inde v boxe => vymazem moznosti z ciel v riadkoch a v stlpcoch v tomto boxe
                    // iCase: nevymazavam z ciel ktore su v tom istom riadku ako je cell a parterCell
                    // jCase: nevymazavam z ciel ktore su v tom istom stlpci ako je cell a parterCell
                    LOGGER.info(ANSI_RED + "Possibility " + possibilityToCheck + " presents somewhere else too" + ANSI_RESET);
                    changedInLoop = reduceBoxCandidates(cell, cellRow, cellColumn, possibilityToCheck, partnerCell, changedInLoop, iCase, jCase);
                }

                if (changedInLoop) {
                    Sudoku sudokuCopy = sudoku.copy();
                    Map<int[], Integer> deletedPossibilitiesWithLocationCopy = new HashMap<>();
                    deletedPossibilitiesWithLocationCopy.putAll(deletedPossibilitiesWithLocation);
                    step = new OneChangeStep(sudokuCopy, name, cell, partnerCell, deletedPossibilitiesWithLocationCopy);
                    ((OneChangeStep)step).setResolvable(this);
                    stepList.add(step);
                    updatedInPointingPair = true;
                    return updatedInPointingPair;
                }
            }
        }
        return updatedInPointingPair;
    }

    private boolean reduceRowOrColumnCandidates(Cell cell, Cell partnerCell, Row cellRow, Column cellColumn, int possibilityToCheck, boolean iCase) {
        boolean changedInLoop;
        if (iCase) {
            changedInLoop = deletePossibilitiesInRowOrColumn(cell, partnerCell, possibilityToCheck, deletedPossibilitiesWithLocation, cellRow);
        } else {
            changedInLoop = deletePossibilitiesInRowOrColumn(cell, partnerCell, possibilityToCheck, deletedPossibilitiesWithLocation, cellColumn);
        }
        return changedInLoop;
    }

    private boolean reduceBoxCandidates(Cell cell, Row cellRow, Column cellColumn, int possibilityToCheck, Cell partnerCell, boolean changedInLoop, boolean iCase, boolean jCase) {
        if ((iCase && !isPossibilityToCheckPresentSomewhereElseInRowInColumn(cell, possibilityToCheck, cellRow)) ||
                (jCase && !isPossibilityToCheckPresentSomewhereElseInRowInColumn(cell, possibilityToCheck, cellColumn))) {
            changedInLoop = deletePossibilitiesInBox(cell, partnerCell, possibilityToCheck);
        }
        return changedInLoop;
    }

    private List<Cell> findPartnerCell(Cell cell, int possibilityToCheck) {
        int cellI = cell.getI();
        int cellJ = cell.getJ();
        Box targetBox = cell.getBox();
        List<Cell> eligiblePartnerCellList = new ArrayList<>();

        for (Cell candidateCell : targetBox.getCellList()) {

            if (cell != candidateCell && candidateCell.getCellPossibilities().contains(possibilityToCheck)) {
                int candidateCellI = candidateCell.getI();
                int candidateCellJ = candidateCell.getJ();
                if ((cellI == candidateCellI || cellJ == candidateCellJ)) {
                    eligiblePartnerCellList.add(candidateCell);
                }
            }
        }

        return eligiblePartnerCellList;
    }

    private boolean isPossibilityToCheckPresentSomewhereElseInBox(Cell cell, Cell partnerCell, List<Cell> cellsInBox, int possibilityToCheck) {
        int cellI = cell.getI();
        int cellJ = cell.getJ();
        int partnerCellI = partnerCell.getI();
        int partnerCellJ = partnerCell.getJ();

        if (cellI == partnerCellI) {
            LOGGER.info("i case");
            for (Cell testedCell : cellsInBox) {
                if (testedCell.getI() != cellI && testedCell.getCellPossibilities().contains(possibilityToCheck)) {
                    return true;
                }
            }
        }
        if (cellJ == partnerCellJ) {
            LOGGER.info("j case");
            for (Cell testedCell : cellsInBox) {
                if (testedCell.getJ() != cellJ && testedCell.getCellPossibilities().contains(possibilityToCheck)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean deletePossibilitiesInBox(Cell cell, Cell partnerCell, int possibilityToCheck) {
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
                if (isSameCoord && testedCell.getCellPossibilities().contains(possibilityToCheck)) {
                    int[] possibilityLocation = {testedCell.getI(), testedCell.getJ()};
                    deletedPossibilitiesWithLocation.put(possibilityLocation, possibilityToCheck);
                    LOGGER.info(ANSI_PURPLE + "\t BOX CASE: " + "Possibility " + possibilityToCheck + " will be removed from " +
                            "i=" + testedCell.getI() + " j=" + testedCell.getJ()
                            + " ~~~ cell: [" + cellI + ", " + cellJ + "]=" + cell.getActualValue()+ " partner cell: "+
                            "[" + partnerCellI + ", "+ partnerCellJ + "]=" + partnerCell.getActualValue() + ANSI_RESET);
                    somethingWasRemoved = testedCell.getCellPossibilities().remove((Integer)possibilityToCheck);
                }
            }
        } else {
            LOGGER.info(ANSI_RED + "\tBOX CASE: Something's wrong - incorrect partner cell!" + ANSI_RESET);
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

    public boolean deletePossibilitiesInRowOrColumn(Cell cell, Cell partnerCell, int possibilityToCheck, Map<int[], Integer> deletedPossibilitiesWithLocation, SudokuElement sudokuElement) {
        deletedPossibilitiesWithLocation.clear();
        boolean somethingWasRemoved = false;
        Box cellBox = cell.getBox();

        for (Cell testedCell : sudokuElement.getCellList()) {
            Box testedCellBox = testedCell.getBox();
            if (cellBox != testedCellBox && testedCell.getCellPossibilities().contains(possibilityToCheck)) {
                int[] possibilityLocation = {testedCell.getI(), testedCell.getJ()};
                deletedPossibilitiesWithLocation.put(possibilityLocation, possibilityToCheck);
                LOGGER.info(ANSI_BLUE + "\tROW-COLUMN CASE: Possibility " + possibilityToCheck + " will be removed from " +
                        "i=" + testedCell.getI() + " j=" + testedCell.getJ() +
                        " ~~~ cell: [" + cell.getI() + ", " + cell.getJ() + "]=" + cell.getActualValue()+ " partner cell: "+
                        "[" + partnerCell.getI()+ ", "+ partnerCell.getJ() + "]=" + partnerCell.getActualValue() +ANSI_RESET);
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
            if (cellBox != testedCell.getBox() &&  testedCell.getCellPossibilities().contains(possibilityToCheck)) {
                return true;
            }
        }

        return false;
    }
}