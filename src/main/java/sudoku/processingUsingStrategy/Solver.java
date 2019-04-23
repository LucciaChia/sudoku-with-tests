package sudoku.processingUsingStrategy;

import sudoku.model.Sudoku;

import java.util.ArrayList;
import java.util.List;

public class Solver {
    private List<Resolvable> strategies;

    public Solver() {

    }

    public void setStrategies(Resolvable ... useStrategies) {
        this.strategies = new ArrayList<>();
        for (Resolvable strategy: useStrategies) {
            this.strategies.add(strategy);
        }
    }

    public void useStrategies(Sudoku sudoku) {
        boolean updatedByStrategy = false;
        // possible strategies
        boolean nakedSingleInACellUpdated = false;
        boolean hiddenSingleInACellUpdated = false;
        boolean pointingPairsInCellUpdated = false;
        do {
            for (Resolvable strategy : this.strategies) {
                strategy.resolveSudoku(sudoku);
                if (sudoku.isSudokuResolved()) {
                    return;
                }

                // TODO reduce this
                if (strategy instanceof NakedSingleInACell) {
                    nakedSingleInACellUpdated = strategy.isUpdated();
                }
                if (strategy instanceof HiddenSingleInACell) {
                    hiddenSingleInACellUpdated = strategy.isUpdated();
                }
                if (strategy instanceof PointingPairsInCell) {
                    pointingPairsInCellUpdated = strategy.isUpdated();
                }

                if (nakedSingleInACellUpdated || hiddenSingleInACellUpdated || pointingPairsInCellUpdated) {
                    updatedByStrategy = true;
                } else {
                    updatedByStrategy = false;
                }
            }
        }while (updatedByStrategy);
        if (!sudoku.isSudokuResolved()) {
            System.out.println("Sudoku needs more advanced methods to be completely resolved");
        }
    }

}
