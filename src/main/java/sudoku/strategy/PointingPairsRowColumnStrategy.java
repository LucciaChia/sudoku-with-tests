package sudoku.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sudoku.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sudoku.ANSIColour.*;

/**
 * Variation 1. Reducing row or column
 * if a pair of empty cells (current value is 0 for both) within a box in the same row or column share a given
 * possibility and this possibility doesn't occur anywhere else in the box => this possibility will be removed from
 * all other cell's possibilities lists outside this box within the same row or column
 *
 * see example: http://www.sudoku-solutions.com/index.php?page=solvingInteractions
 */
class PointingPairsRowColumnStrategy extends PointingPairsAbstractStrategy implements Resolvable{
    private static final String name = "Pointing Pairs Row Column";
    private static final StrategyType type = StrategyType.MEDIUM;

    private Map<int[], Integer> deletedPossibilitiesWithLocation = new HashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(PointingPairsRowColumnStrategy.class);
    private boolean updatedInPointingPair = false;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Sudoku resolveSudoku(Sudoku sudoku) {
        updatedInPointingPair = false;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Cell cell = sudoku.getRows().get(i).getCell(j);
                if (cell.getActualValue() == 0) {
                    if (pointingPairRowColumn(cell)){
                        if (sudoku.getSudokuLevelType().ordinal() < this.getType().ordinal() ) {
                            sudoku.setSudokuLevelType(this.getType());
                        }
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

    @Override
    public StrategyType getType() {
        return type;
    }

    private boolean pointingPairRowColumn(Cell cell) {
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
                    changedInLoop = reduceRowOrColumnCandidates(cell, partnerCell, cellRow, cellColumn, possibilityToCheck, iCase);
                }

                if (changedInLoop) {
                    Map<int[], Integer> deletedPossibilitiesWithLocationCopy = new HashMap<>(deletedPossibilitiesWithLocation);
                    return updatedInPointingPair = true;
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

    /**
     * Method, used by PointingPairsStrategy strategy, that checks and removes an input possibility from possibilities
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

}
