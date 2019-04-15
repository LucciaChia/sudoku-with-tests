package sudoku.model;

/**
 * represents one box in sudoku matrix
 * @author Lucia
 */
public class Box extends SudokuElement {

    @Override
    public String toString() {
        String output = "";
        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                output +=  " " + super.getCellList().get(index).getActualValue();
                index++;
            }
            output += "\n";
        }
        return output;
    }
}