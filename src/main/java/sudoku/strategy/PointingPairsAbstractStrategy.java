package sudoku.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sudoku.model.Box;
import sudoku.model.Cell;

import java.util.ArrayList;
import java.util.List;

abstract class PointingPairsAbstractStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(PointingPairsAbstractStrategy.class);


    List<Cell> findPartnerCell(Cell cell, int possibilityToCheck) {
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

    boolean isPossibilityToCheckPresentSomewhereElseInBox(Cell cell, Cell partnerCell, List<Cell> cellsInBox, int possibilityToCheck) {
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
}
