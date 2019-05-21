package sudoku.menu;

import lombok.AllArgsConstructor;
import sudoku.command.Invoker;
import sudoku.model.Sudoku;

@AllArgsConstructor
public class StepByStepManualInvoker implements MenuState {

    private Sudoku sudoku;
    private Invoker automaticInvoker;
    private Invoker manualInvoker;

    @Override
    public MenuState transitionFunction(String input) {
        return null;
    }
}
