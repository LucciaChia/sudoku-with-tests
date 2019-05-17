package sudoku.strategy;

import sudoku.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * if cell with value 0, contains multiple possibilities, but one of these possibilities occurs only once in
 * possibilities in whole row / in whole column / whole box, this possibility will be set as a value for this
 * cell
 *
 * see example: http://www.sudoku-solutions.com/index.php?page=solvingHiddenSubsets
 */
class HiddenSingleStrategy implements Resolvable {

    private static final String name = "Hidden Single";
    private static final StrategyType type = StrategyType.LOW;

    private boolean updatedInHiddenSingle = false;

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

                    Map<Integer, Integer> rowPossibilities = amountOfParticularPossibilities(cellRow);
                    Map<Integer, Integer> columnPossibilities = amountOfParticularPossibilities(cellColumn);
                    Map<Integer, Integer> boxPossibilities = amountOfParticularPossibilities(cellBox);

                    if (deleteHidden(cell, rowPossibilities, columnPossibilities, boxPossibilities)) {
                        updatedInHiddenSingle = true;
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

    /**
     * check the amount of particular possibility in a sudoku element - row / column / box
     *
     * @return      a map containing a number of occurrence of each possibility
     */
    Map<Integer, Integer> amountOfParticularPossibilities(SudokuElement sudokuElement) {
        Map<Integer, Integer> countOfPossibilities = new HashMap<>();
        List<Cell> listOfCells = sudokuElement.getCellList();
        for (int i = 0; i < 9; i++) {
            List<Integer> possibility = listOfCells.get(i).getCellPossibilities();
            if (possibility != null) {
                for (Integer integer : possibility) {
                    if (!countOfPossibilities.containsKey(integer)) {
                        countOfPossibilities.put(integer, 1);
                    } else {
                        int key = integer;
                        countOfPossibilities.put(key, countOfPossibilities.get(key) + 1);
                    }
                }
            }
        }
        return countOfPossibilities;
    }


    @Override
    public boolean isUpdated() {
        return updatedInHiddenSingle;
    }

/*
 * if amount of particular possibility in whole row / column / box is only one, this possibility will be set
 * as actual value for this cell
 */
    boolean deleteHidden(Cell cell, Map<Integer, Integer> rowPoss, Map<Integer, Integer> columnPoss, Map<Integer, Integer> boxPoss) {

        List<Integer> cellPossibilities = cell.getCellPossibilities();
        for (Integer currentCellPossibility : cellPossibilities) {

            if (checkUniqueOccurence(cell, rowPoss, currentCellPossibility)
                || checkUniqueOccurence(cell, columnPoss,currentCellPossibility)
                || checkUniqueOccurence(cell, boxPoss, currentCellPossibility)) {
                return true;
            }
        }
        return false;
    }

    boolean checkUniqueOccurence(Cell cell, Map<Integer, Integer> rowPoss, Integer currentCellPossibility) {
        if (rowPoss.get(currentCellPossibility) == 1) {
            cell.setActualValue(currentCellPossibility);
            return true;
        }
        return false;
    }

    @Override
    public StrategyType getType() {
        return type;
    }
}
