package sudoku.step;

import sudoku.model.Cell;

import java.util.Map;

public interface Step {

    void printStep(Cell cell);

    void printStepPointingPair(Cell cell, Cell partnerCell, Map<int[], Integer> deletedPossibilitiesWithLocation);

    void printBacktrack();
}
