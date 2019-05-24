package sudoku.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sudoku.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sudoku.ANSIColour.*;

/**
 * Variation 2. Reducing box candidates
 * if a pair of empty cells (current value is 0 for both) within a box in the same row / column share a given
 * possibility and this possibility doesn't occur anywhere else in the row / column outside the box, this possibility
 * will be removed from all other cell's possibilities within the box
 *
 * see example: http://www.sudoku-solutions.com/index.php?page=solvingInteractions
 */
class PointingPairsBoxStrategy extends PointingPairsAbstractStrategy implements Resolvable{
    private static final String name = "Pointing Pairs Box";
    private static final StrategyType type = StrategyType.MEDIUM;

    private Map<int[], Integer> deletedPossibilitiesWithLocation = new HashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(PointingPairsBoxStrategy.class);
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
                    if (pointingPairBox(cell)){
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

    private boolean pointingPairBox(Cell cell) {
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


                if (isPossibilityToCheckPresentSomewhereElseInBox(cell, partnerCell, cellBox.getCellList(), possibilityToCheck)) {
                    // possibilita sa nachadza inde v boxe => vymazem moznosti z ciel v riadkoch a v stlpcoch v tomto boxe
                    // iCase: nevymazavam z ciel ktore su v tom istom riadku ako je cell a parterCell
                    // jCase: nevymazavam z ciel ktore su v tom istom stlpci ako je cell a parterCell
                    LOGGER.info(ANSI_RED + "Possibility " + possibilityToCheck + " presents somewhere else too" + ANSI_RESET);
                    changedInLoop = reduceBoxCandidates(cell, cellRow, cellColumn, possibilityToCheck, partnerCell, changedInLoop, iCase, jCase);
                }

                if (changedInLoop) {
                    Map<int[], Integer> deletedPossibilitiesWithLocationCopy = new HashMap<>(deletedPossibilitiesWithLocation);
                    return updatedInPointingPair = true;
                }
            }
        }
        return updatedInPointingPair;
    }

    private boolean reduceBoxCandidates(Cell cell, Row cellRow, Column cellColumn, int possibilityToCheck, Cell partnerCell, boolean changedInLoop, boolean iCase, boolean jCase) {
        if ((iCase && !isPossibilityToCheckPresentSomewhereElseInRowInColumn(cell, possibilityToCheck, cellRow)) ||
                (jCase && !isPossibilityToCheckPresentSomewhereElseInRowInColumn(cell, possibilityToCheck, cellColumn))) {
            changedInLoop = deletePossibilitiesInBox(cell, partnerCell, possibilityToCheck);
        }
        return changedInLoop;
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
     * Method that checks if a cell from different box but same row/column, that has an input possibility
     * among its possibilities, exists. Used by PointingPairsStrategy strategy.
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
