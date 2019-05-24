package sudoku.command;

//import sudoku.step.Step;

import sudoku.exceptions.NoAvailableSolutionException;

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
    Command getNextState() throws NoAvailableSolutionException;
}
