package sudoku.model;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sudoku.console.ConsoleDisplayer;
import sudoku.console.Displayer;
import sudoku.exceptions.IllegalSudokuStateException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SudokuElement can represent whole row (Row class), whole column (Column class)
 * or whole box (Box class)
 * @author Lucia
 */
@Getter @Setter
public abstract class SudokuElement {

    private static final Logger LOGGER = LoggerFactory.getLogger(SudokuElement.class);
    private static final Displayer consoleDisplayer = new ConsoleDisplayer();

    private List<Cell> cellList = new ArrayList<>();

    /**
     * Method that validates actual values of the sudoku so that every number other than zero is unique
     * in its SudokuElement
     *
     * @throws IllegalSudokuStateException  an exception risen if number other that zero is not unique
     *                                      in its row, column or box
     */
    void validateRepetition() throws IllegalSudokuStateException {

        Map<Integer, Integer> repetition = new HashMap<>();

        for (int i = 0; i < 9; i++) {
            Cell cell = cellList.get(i);
            int key = cell.getActualValue();
            if ((key != 0) && !(repetition.containsKey(key))) {
                repetition.put(key, 1);
            } else if (repetition.containsKey(key)) {
                consoleDisplayer.displayLine("Row");
                throw new IllegalSudokuStateException(key,cell.getI(),cell.getJ());
            }
        }
    }
}
