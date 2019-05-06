package sudoku.command;

import lombok.Getter;
import lombok.Setter;
import sudoku.model.Sudoku;
import sudoku.step.OneChangeStep;
import sudoku.step.Step;
import sudoku.strategy.Resolvable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Getter @Setter
public class CommandPicker implements Command {
//    Resolvable resolvable;
//    Sudoku sudoku;
    private List<Step> stepList = new ArrayList<>();

    public CommandPicker(Resolvable resolvable, Sudoku sudoku) {
//        this.resolvable = resolvable;
//        this.sudoku = sudoku;
        stepList.add(new OneChangeStep(resolvable, sudoku));
    }

    @Override
    public Sudoku execute() {
        Sudoku sudoku = ((OneChangeStep) Objects.requireNonNull(getLastStep())).getSudoku();
        return ((OneChangeStep) getLastStep()).getResolvable().resolveSudoku(sudoku);
    }

    private Step getLastStep() {
        if (stepList.size() > 0) {
            return stepList.get(stepList.size() - 1);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        Sudoku sudoku = ((OneChangeStep) Objects.requireNonNull(getLastStep())).getSudoku();
        return ((OneChangeStep)getLastStep()).getResolvable().getName() + "\n" + sudoku.toString();
    }

    public Sudoku getSudoku() {
        return ((OneChangeStep) Objects.requireNonNull(getLastStep())).getSudoku();
    }
}

