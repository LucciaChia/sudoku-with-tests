package sudoku.strategy;

import sudoku.model.*;
import sudoku.step.Step;

import java.util.List;

/**
 * resolvable has to be implemented by all strategies used while trying to resolve sudoku
 */
public interface Resolvable {

    /*
     * method containing the solving strategy
     */

    Sudoku resolveSudoku(Sudoku sudoku);

    /*
     * checker if sudoku was updated by particular solving strategy
     */
    boolean isUpdated();

    String getName();

    List<Step> getStepList();


    /*
     * inappropriate possibilities reducer
     */
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
