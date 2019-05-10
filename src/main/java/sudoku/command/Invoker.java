package sudoku.command;

//import sudoku.step.Step;

/**
 * An interface which is part of the strategy pattern. Invoker chooses strategy that will be used in next step
 */
public interface Invoker {

    /**
     * Method that helps navigate between states in backward direction.
     *
     * @return      command containing information about previous state
     */
    Command getPreviousState();

    /**
     * Method that helps navigate between states in forward direction.
     *
     * @return      command containing information about next state
     */
    Command getNextState();

//    Command solvingStepsOrder();

//    /**
//     * Method that helps navigate between steps in backward direction.
//     *
//     * @return      step that precedes current step
//     */
//    default Step getPreviousStep() { return null; }
//
//    /**
//     * Method that helps navigate between steps in backward direction.
//     *
//     * @return      step that precedes current step
//     */
//    default Step getNextStep() { return null; }
//
//    /**
//     * Method that returns all steps that has been made.
//     *
//     * @return      list of step from first step to current step or solution (depending on implementation)
//     */
//    default List<Step> solvingStepsOrderLucia() { return null; }
}
