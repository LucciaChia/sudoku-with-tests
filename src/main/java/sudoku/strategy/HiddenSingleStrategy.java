package sudoku.strategy;

import sudoku.model.*;
import sudoku.step.OneChangeStep;
import sudoku.step.Step;

import java.util.ArrayList;
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
    private Map<int[], Integer> deletedPossibilitiesWithLocation = new HashMap<>();
    private boolean updatedInHiddenSingle = false;
    private Step step;
    private List<Step> stepList = new ArrayList<>();
    private String name = "1: HiddenSingleInACell";

    public Step getStep() {
        return step;
    }

    @Override
    public List<Step> getStepList() {
        return stepList;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Sudoku resolveSudoku(Sudoku sudoku) {
        stepList.clear();
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
                        Sudoku sudokuCopy = sudoku.copy();
                        step = new OneChangeStep(sudokuCopy, name, cell);
                        ((OneChangeStep)step).setResolvable(this);
                        stepList.add(step);
                        //step.printStep(cell);
                        updatedInHiddenSingle = true;
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
    public Map<Integer, Integer> amountOfParticularPossibilities(SudokuElement sudokuElement) {
        Map<Integer, Integer> countOfPossibilities = new HashMap<>();
        List<Cell> listOfCells = sudokuElement.getCellList();
        for (int i = 0; i < 9; i++) {
            List<Integer> possibility = listOfCells.get(i).getCellPossibilities(); // moznosti v bunke
            if (possibility != null) {
                for (int j = 0; j < possibility.size(); j++) {
                    if (!countOfPossibilities.containsKey(possibility.get(j))) {
                        countOfPossibilities.put(possibility.get(j), 1);
                    } else {
                        int key = possibility.get(j);
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
    private boolean deleteHidden(Cell cell, Map<Integer, Integer> rowPoss, Map<Integer, Integer> columnPoss, Map<Integer, Integer> boxPoss) {

        List<Integer> cellPossibilities = cell.getCellPossibilities();

        for (int i = 0; i < cellPossibilities.size(); i++) {
            int currentCellPossibility = cellPossibilities.get(i);

            if (rowPoss.get((Integer)currentCellPossibility) == 1) {
                cell.setActualValue(currentCellPossibility);
                cell.deletePossibilities();
                return true;
            }

            if (columnPoss.get((Integer)currentCellPossibility) == 1) {
                cell.setActualValue(currentCellPossibility);
                cell.deletePossibilities();
                return true;
            }

            if (boxPoss.get((Integer)currentCellPossibility) == 1) {
                cell.setActualValue(currentCellPossibility);
                cell.deletePossibilities();
                return true;
            }
        }
        return false;
    }
}