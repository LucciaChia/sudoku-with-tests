import org.junit.jupiter.api.Test;
import sudoku.model.Cell;
import sudoku.model.Possibility;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCell {
    private int actualValue = 5;
    private Cell cell = new Cell(0,0,0);
    private Possibility cellPossibilities = new Possibility(0,0);

    @Test
    public void testSetActualValue() {
        cell.setCellPossibilities(cellPossibilities);
        cell.setActualValue(actualValue);
        assertEquals(5, cell.getActualValue(), 0.0);
    }

}
