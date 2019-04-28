package sudoku.processingUsingStrategy;

import sudoku.model.*;

/**
 * zvladla test pre NakedSingleInACell
 */
public interface Resolvable {
    Sudoku resolveSudoku(Sudoku sudoku);

    boolean isUpdated();

    String getName();

    default void deletePossibilities(Cell cell, int valueToBeDeleted) {

        Row cellRow = cell.getRow();
        Column cellColumn = cell.getColumn();
        Box cellBox = cell.getBox();

        for (int i = 0; i < 9; i++) {
            int rowValue = cellRow.getCell(i).getActualValue();
            int colValue = cellColumn.getCell(i).getActualValue();
            int boxValue = cellBox.getCellList().get(i).getActualValue();

            if (rowValue == 0) {
                cellRow.getCell(i).getCellPossibilities().remove((Integer)valueToBeDeleted);
            }
            if (colValue == 0) {
                cellColumn.getCell(i).getCellPossibilities().remove((Integer) valueToBeDeleted);
            }
            if (boxValue == 0) {
                cellBox.getCellList().get(i).getCellPossibilities().remove((Integer)valueToBeDeleted);
            }
        }
    }
}
