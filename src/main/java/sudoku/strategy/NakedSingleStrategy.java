package sudoku.strategy;

import sudoku.model.Cell;
import sudoku.model.StrategyType;
import sudoku.model.Sudoku;

import java.util.List;

/*
 * if only one possibility is left in a List<Integer> cellPossibilities for a cell
 * this possibility is set to be a value of this Cell
 * In the corresponding cells in row, column and box this number is removed from possibilities
 *
 * see examples: http://www.sudoku-solutions.com/index.php?page=solvingNakedSubsets
 */
class NakedSingleStrategy implements Resolvable {

    private static final String name = "Naked Single";
    private static final StrategyType type = StrategyType.LOW;

    private boolean updatedInNakedSingle = false;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Sudoku resolveSudoku(Sudoku sudoku) {
        do {
            updatedInNakedSingle = false;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    Cell cell = sudoku.getRows().get(i).getCell(j);
                    List<Integer> cellPossibilities = cell.getCellPossibilities();
                    if (cellPossibilities.size() == 1) {
                        cell.setActualValue(cellPossibilities.get(0));
                        updatedInNakedSingle = true;
                        if (sudoku.getSudokuLevelType().ordinal() < this.getType().ordinal() ) {
                            sudoku.setSudokuLevelType(this.getType());
                        }

                        return sudoku;
                    }
                }

            }
        } while (updatedInNakedSingle);
        return sudoku;
    }

    @Override
    public boolean isUpdated() {
        return updatedInNakedSingle;
    }

    @Override
    public StrategyType getType() {
        return type;
    }
}