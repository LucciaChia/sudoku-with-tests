package sudoku.command;

import sudoku.model.Sudoku;
import sudoku.processingUsingStrategy.Resolvable;
import sudoku.stepHandlers.OneChangeStep;
import sudoku.stepHandlers.Step;

public class CommandPicker implements Command {
//    Resolvable resolvable;
//    Sudoku sudoku;
    Step step;

    public CommandPicker(Resolvable resolvable, Sudoku sudoku) {
//        this.resolvable = resolvable;
//        this.sudoku = sudoku;
        step = new OneChangeStep(resolvable, sudoku);
    }

    public Step getStep() {
        return step;
    }

    @Override
    public Sudoku execute() {
        Sudoku sudoku = ((OneChangeStep)step).getSudoku();
        return ((OneChangeStep)step).getResolvable().resolveSudoku(sudoku);
    }

    @Override
    public String toString() {
        Sudoku sudoku = ((OneChangeStep)step).getSudoku();
        return ((OneChangeStep)step).getResolvable().getName() + "\n" + sudoku.toString();
    }

    public Sudoku getSudoku() {
        return ((OneChangeStep) step).getSudoku();
    }
}

