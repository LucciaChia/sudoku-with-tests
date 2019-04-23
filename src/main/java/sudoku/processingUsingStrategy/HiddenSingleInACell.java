package sudoku.processingUsingStrategy;

import sudoku.model.*;

import java.util.List;
import java.util.Map;

public class HiddenSingleInACell implements Resolvable {

    private boolean updatedInHiddenSingle = false;

    @Override
    public Sudoku resolveSudoku(Sudoku sudoku) {
        updatedInHiddenSingle = false;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {

                Cell cell = sudoku.getRows().get(i).getCell(j);
                if (cell.getActualValue() == 0) {

                    Row cellRow = cell.getRow();
                    Column cellColumn = cell.getColumn();
                    Box cellBox = cell.getBox();

                    Map<Integer, Integer> rowPossibilities = cellRow.amountOfParticularPossibilities();
                    Map<Integer, Integer> columnPossibilities = cellColumn.amountOfParticularPossibilities();
                    Map<Integer, Integer> boxPossibilities = cellBox.amountOfParticularPossibilities();

                    if (deleteHidden(cell, rowPossibilities, columnPossibilities, boxPossibilities)) {
                        updatedInHiddenSingle = true;

                    }
                }
            }
        }
        return sudoku;
    }

    @Override
    public boolean isUpdated() {
        return updatedInHiddenSingle;
    }

    private boolean deleteHidden(Cell cell, Map<Integer, Integer> rowPoss, Map<Integer, Integer> columnPoss, Map<Integer, Integer> boxPoss) {

        List<Integer> cellPossibilities = cell.getCellPossibilities();

        for (int i = 0; i < cellPossibilities.size(); i++) {
            int currentCellPossibility = cellPossibilities.get(i);

            if (rowPoss.get((Integer)currentCellPossibility) == 1) {
                cell.setActualValue(currentCellPossibility);
                deletePossibilities(cell, cell.getActualValue());
                return true;
            }

            if (columnPoss.get((Integer)currentCellPossibility) == 1) {
                cell.setActualValue(currentCellPossibility);
                deletePossibilities(cell, cell.getActualValue());
                return true;
            }

            if (boxPoss.get((Integer)currentCellPossibility) == 1) {
                cell.setActualValue(currentCellPossibility);
                deletePossibilities(cell, cell.getActualValue());
                return true;
            }
        }
        return false;
    }
}
