package sudoku.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    private Cell cell0;
    private Cell cell1;

    @BeforeEach
    void setUp() {
        Row row = new Row();
        Column column = new Column();
        Box box = new Box();
        Cell cell01 = new Cell(0, 1, 2, row, column, box);
        Cell cell02 = new Cell(0, 1, 2, row, column, box);
        Cell cell03 = new Cell(0, 1, 2, row, column, box);
        Cell cell04 = new Cell(0, 1, 2, row, column, box);
        Cell cell05 = new Cell(0, 1, 2, row, column, box);
        Cell cell06 = new Cell(0, 1, 2, row, column, box);
        Cell cell07 = new Cell(0, 1, 2, row, column, box);
        Cell cell08 = new Cell(0, 1, 2, row, column, box);
        cell0 = new Cell(0, 1, 2, row, column, box);
        cell1 = new Cell(1, 2, 1, row, column, box);

        box.getCellList().add(cell1);
        box.getCellList().add(cell01);
        box.getCellList().add(cell02);
        box.getCellList().add(cell03);
        box.getCellList().add(cell04);
        box.getCellList().add(cell05);
        box.getCellList().add(cell06);
        box.getCellList().add(cell07);
        box.getCellList().add(cell08);

        column.getCellList().add(cell1);
        column.getCellList().add(cell01);
        column.getCellList().add(cell02);
        column.getCellList().add(cell03);
        column.getCellList().add(cell04);
        column.getCellList().add(cell05);
        column.getCellList().add(cell06);
        column.getCellList().add(cell07);
        column.getCellList().add(cell08);

        row.getCellList().add(cell1);
        row.getCellList().add(cell0);
        row.getCellList().add(cell02);
        row.getCellList().add(cell03);
        row.getCellList().add(cell04);
        row.getCellList().add(cell05);
        row.getCellList().add(cell06);
        row.getCellList().add(cell07);
        row.getCellList().add(cell08);
    }

    @Test
    void setActualValue() {
        // GIVEN

        // WHEN
        cell0.setActualValue(3);

        // THEN
        assertEquals(3, cell0.getActualValue());
        assertEquals(0, cell0.getCellPossibilities().size());
    }

    @Test
    void deletePossibility() {
        // WHEN
        cell0.deletePossibility(3);

        // THEN
        assertFalse(cell0.getCellPossibilities().contains(3));
    }

    @Test
    void deletePossibilities() {
        // WHEN
        cell1.deletePossibilities();

        // THEN
        assertFalse(cell0.getCellPossibilities().contains(1));
    }
}