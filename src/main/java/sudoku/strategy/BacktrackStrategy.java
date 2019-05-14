package sudoku.strategy;

import sudoku.model.Cell;
import sudoku.model.Sudoku;

/*
 * brute force method. Backtrack will try all possible combinations of possibilities in order to find the solution
 * this single method is able to resolve any sudoku
 */
class BacktrackStrategy implements Resolvable{
    private boolean updatedInBacktrackLucia = false;

    private static long stepCount = 0;
    private String name = "3: Backtrack";

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
                        newCell.deletePossibilities();

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
}
