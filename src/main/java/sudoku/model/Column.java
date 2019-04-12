package sudoku.model;

/**
 * represents one column in sudoku matrix
 * @author Lucia
 */
public class Column extends SudokuElement {

    public Cell getCell(int i) {
        return super.getCellList().get(i);
    }

    @Override
    public String toString() {
        String output = "";
        for (int i = 0; i < 9; i++) {
            output += "\n" + super.getCellList().get(i).getActualValue();
        }
        return output;
    }

}
