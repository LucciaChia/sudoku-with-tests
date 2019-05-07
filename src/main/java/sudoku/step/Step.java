package sudoku.step;

import sudoku.model.Cell;

import java.util.Map;

/**
 * An interface for a step as an discrete state
 *
 * vyrazit
 */
public interface Step {

    /**
     * Method for printing information about one step when using NakedSingleCell or HiddenSingleCell strategy
     *
     * @param cell  a cell whose actual value has been set in this step
     */
    void printStep(Cell cell);

    /**
     * Method for printing information about one step when using PointingPairsInCell strategy
     *
     * @param cell                              cell of the pair of cells used in the strategy
     * @param partnerCell                       cell of the pair of cells used in the strategy
     * @param deletedPossibilitiesWithLocation  possibilities that has been removed in this step
     */
    void printStepPointingPair(Cell cell, Cell partnerCell, Map<int[], Integer> deletedPossibilitiesWithLocation);

    /**
     * MEthod for printing information of the result of the use of Backtracking strategy
     */
    void printBacktrack();
}
