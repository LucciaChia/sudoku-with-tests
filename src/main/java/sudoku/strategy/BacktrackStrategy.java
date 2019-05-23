package sudoku.strategy;

import sudoku.exceptions.NoAvailableSolutionException;
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

    private boolean updatedInBacktrack = false;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Sudoku resolveSudoku(Sudoku sudoku) throws NoAvailableSolutionException {
        Sudoku backtrackSolution = backtrack(sudoku);
        if (backtrackSolution == null) {
            throw new NoAvailableSolutionException(sudoku);
        } else {
            updatedInBacktrack = true;
            if (backtrackSolution.getSudokuLevelType().ordinal() < this.getType().ordinal() ) {
                backtrackSolution.setSudokuLevelType(this.getType());
            }

            return backtrackSolution;
        }
    }

    private Sudoku backtrack(Sudoku sudoku) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {

                Cell cell = sudoku.getRows().get(i).getCell(j);

                if (cell.getActualValue() != 0) {
                    continue;
                }

                int cellPossibilitiesAmount = cell.getCellPossibilities().size();

                if (cellPossibilitiesAmount > 0 ) {
                    for (int k = 0; k < cellPossibilitiesAmount; k++) {

                        Sudoku newSudoku = sudoku.copy();
                        Cell newCell = newSudoku.getRows().get(i).getCell(j);

                        int usedPossibility = cell.getCellPossibilities().get(k);
                        newCell.setActualValue(usedPossibility);

                        Sudoku resolvedSudoku = backtrack(newSudoku);

                        if (resolvedSudoku != null) {
                            return resolvedSudoku;
                        }
                    }
                    return null;
                }
                // cellPossibilities == null => return null
                return null;
            }
        }
        return sudoku;
    }

    @Override
    public boolean isUpdated() {
        return updatedInBacktrack;
    }

    @Override
    public StrategyType getType() {
        return type;
    }
}
