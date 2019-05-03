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
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            output.append("\n").append(super.getCellList().get(i).getActualValue());
        }
        return output.toString();
    }
}