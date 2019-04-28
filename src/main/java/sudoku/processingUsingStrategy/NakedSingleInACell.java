package sudoku.processingUsingStrategy;

import sudoku.model.Cell;
import sudoku.model.Sudoku;
import sudoku.stepHandlers.OneChangeStep;
import sudoku.stepHandlers.Step;

import java.util.List;

public class NakedSingleInACell implements Resolvable {
    private boolean updatedInNakedSingle = false;
    private String name = "0: NackedSingleInACell";
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
                        deletePossibilities(cell, cell.getActualValue());
                        Step step = new OneChangeStep(sudoku, name);
                        step.printStep(cell);
                        updatedInNakedSingle = true;
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

}