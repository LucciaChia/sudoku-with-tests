package sudoku.strategy;

import sudoku.model.Cell;
import sudoku.model.StrategyType;
import sudoku.model.Sudoku;

/*
 * brute force method. Backtrack will try all possible combinations of possibilities in order to find the solution
 * this single method is able to resolve any sudoku
 */
class BacktrackStrategy implements Resolvable{

    private static final String name = "Backtracking";
    private static final StrategyType type = StrategyType.HIGH;

    private static long stepCount = 0;

    private boolean updatedInBacktrackLucia = false;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Sudoku resolveSudoku(Sudoku sudoku) {
        updatedInBacktrackLucia = false;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {

                Cell cell = sudoku.getRows().get(i).getCell(j);
                int cellPossibilitiesAmount = cell.getCellPossibilities().size();
                if (cell.getActualValue() != 0) {
                    continue;
                }

                if (cellPossibilitiesAmount > 0 ) {
                    stepCount++;

                    for (int k = 0; k < cellPossibilitiesAmount; k++) {
                        Sudoku newSudoku = sudoku.copy();

                        Cell newCell = newSudoku.getRows().get(i).getCell(j);

                        int usedPossibility = cell.getCellPossibilities().get(k);
                        newCell.setActualValue(usedPossibility);

                        Sudoku resolvedSudoku = resolveSudoku(newSudoku);

                        if (resolvedSudoku != null) {

                            updatedInBacktrackLucia = false;

                            return resolvedSudoku;
                        }
                    }
                    return null;
                }

                if (cellPossibilitiesAmount == 0) {
                    return null;
                }
            }
        }
        return sudoku;
    }

    @Override
    public boolean isUpdated() {
        return updatedInBacktrackLucia;
    }

    @Override
    public StrategyType getType() {
        return type;
    }
}
