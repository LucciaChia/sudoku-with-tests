package sudoku.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sudoku.model.Box;
import sudoku.model.Cell;
import sudoku.model.SudokuElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PointingPairsAbstractStrategy {

    private Map<int[], Integer> deletedPossibilitiesWithLocation = new HashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(PointingPairsStrategy.class);
    private boolean updatedInPointingPair = false;


    protected List<Cell> findPartnerCell(Cell cell, int possibilityToCheck) {
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

    protected boolean isPossibilityToCheckPresentSomewhereElseInBox(Cell cell, Cell partnerCell, List<Cell> cellsInBox, int possibilityToCheck) {
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