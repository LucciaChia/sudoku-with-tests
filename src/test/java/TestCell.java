import org.junit.jupiter.api.Test;
import sudoku.model.Cell;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCell {
    private int i = 0; // Fixme if is not using remove it
    private int j = 0; // Fixme if is not using remove it
    private int actualValue = 5;
    private Cell cell = new Cell(0,0,0);

    @Test
    public void testSetActualValue() {
        cell.setActualValue(actualValue);
        assertEquals(5, cell.getActualValue(), 0.0);
    }

}
