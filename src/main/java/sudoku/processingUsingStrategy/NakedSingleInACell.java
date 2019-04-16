package sudoku.processingUsingStrategy;

import sudoku.model.Cell;
import sudoku.model.Sudoku;

import java.util.List;

public class NakedSingleInACell implements Resolvable {
    @Override
    public Sudoku resolveSudoku(Sudoku sudoku) {
        do {
            Solver.sudokuWasChanged = false;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    Cell cell = sudoku.getRows().get(i).getCell(j);
                    List<Integer> cellPossibilities = cell.getCellPossibilities();
                    if (cellPossibilities.size() == 1) {
                        cell.setActualValue(cellPossibilities.get(0));
                        deletePossibilities(cell, cell.getActualValue());
                        Solver.sudokuWasChanged = true;
                    }
                }
            }
        } while (Solver.sudokuWasChanged);
        return sudoku;
    }

}