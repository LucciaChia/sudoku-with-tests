import org.junit.jupiter.api.Test;
import sudoku.model.Cell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCell {
    private int actualValue = 5;
    private Cell cell = new Cell(0,0,0);
    private List<Integer> cellPossibilities = new ArrayList<Integer>(Arrays.asList(7,8));;

    @Test
    public void testSetActualValue() {
        cell.setCellPossibilities(cellPossibilities);
        cell.setActualValue(actualValue);
        assertEquals(5, cell.getActualValue(), 0.0);
    }

}
