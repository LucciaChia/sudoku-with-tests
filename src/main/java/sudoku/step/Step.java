package sudoku.step;

import java.util.Map;
import sudoku.model.Cell;

public interface Step {

    void printStep(Cell cell);

    void printStepPointingPair(Cell cell, Cell partnerCell, Map<int[], Integer> deletedPossibilitiesWithLocation);

    void printBacktrack();
}
