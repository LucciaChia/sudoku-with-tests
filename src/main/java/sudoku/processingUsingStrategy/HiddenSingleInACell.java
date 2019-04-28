package sudoku.processingUsingStrategy;

import sudoku.model.*;
import sudoku.stepHandlers.OneChangeStep;
import sudoku.stepHandlers.Step;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HiddenSingleInACell implements Resolvable {
    private Map<int[], Integer> deletedPossibilitiesWithLocation = new HashMap<>();
    private boolean updatedInHiddenSingle = false;
    private String name = "1: HiddenSingleInACell";

    @Override
    public String getName() {
        return name;
    }

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
                        Sudoku sudokuCopy = sudoku.copy();
                        Step step = new OneChangeStep(sudokuCopy, name);
                        step.printStep(cell);
                        updatedInHiddenSingle = true;
                        return sudoku;
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
