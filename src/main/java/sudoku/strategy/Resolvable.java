package sudoku.strategy;

import sudoku.model.Sudoku;

/**
 * resolvable has to be implemented by all strategies used while trying to resolve sudoku
 */
public interface Resolvable {

    /**
     * this method should contain solving logic
     * @param sudoku
     * @return Sudoku
     */
    Sudoku resolveSudoku(Sudoku sudoku);

    /**
     * info if sudoku was changed in particular step with particular method
     * @return boolean
     */
    boolean isUpdated();

    /**
     * solving strategy name
     * @return String
     */
    String getName();

//    /**
//     * returns a list of steps how sudoku was solved
//     *
//     * @return List<Step>
//     */
//    List<Step> getStepList();

//    /**
//     * inappropriate possibilities reducer
//     * @param cell
//     * @param valueToBeDeleted
//     */
//    //unify delete methods
//    default void deletePossibilities(Cell cell, int valueToBeDeleted) {
//
//        Row cellRow = cell.getRow();
//        Column cellColumn = cell.getColumn();
//        Box cellBox = cell.getBox();
//
//        for (int i = 0; i < 9; i++) {
//            int rowValue = cellRow.getCell(i).getActualValue();
//            int colValue = cellColumn.getCell(i).getActualValue();
//            int boxValue = cellBox.getCellList().get(i).getActualValue();
//
//            if (rowValue == 0) {
//                cellRow.getCell(i).getCellPossibilities().remove((Integer)valueToBeDeleted);
//            }
//            if (colValue == 0) {
//                cellColumn.getCell(i).getCellPossibilities().remove((Integer) valueToBeDeleted);
//            }
//            if (boxValue == 0) {
//                cellBox.getCellList().get(i).getCellPossibilities().remove((Integer)valueToBeDeleted);
//            }
//        }
//    }
}
