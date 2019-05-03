package sudoku.strategy;

import java.util.ArrayList;
import java.util.List;
import sudoku.model.Cell;
import sudoku.model.Sudoku;
import sudoku.step.OneChangeStep;
import sudoku.step.Step;

public class NakedSingleInACell implements Resolvable {
    private boolean updatedInNakedSingle = false;
    private Step step;
    private List<Step> stepList = new ArrayList<>();
    private String name = "0: NackedSingleInACell";

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
        do {
            updatedInNakedSingle = false;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    Cell cell = sudoku.getRows().get(i).getCell(j);
                    List<Integer> cellPossibilities = cell.getCellPossibilities();
                    if (cellPossibilities.size() == 1) {
                        cell.setActualValue(cellPossibilities.get(0));
                        deletePossibilities(cell, cell.getActualValue());
                        step = new OneChangeStep(sudoku.copy(), name, cell);
                        ((OneChangeStep)step).setResolvable(this);
                        //step.printStep(cell);
                        stepList.add(step); // ************************************************************************
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