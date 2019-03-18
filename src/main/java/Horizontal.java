import java.util.ArrayList;
import java.util.List;

public class Horizontal implements SudokuElement{
    private List<Cell> column = new ArrayList<>();


    public List<Cell> getColumn() {
        return column;
    }

    public Cell getCellInHorizontal(int i) {
        return column.get(i);
    }
    @Override
    public String toString() {
        String output = "";
        for (int i = 0; i < 9; i++) {
            output += " " + column.get(i).getActualValue();
        }
        return output;
    }

    @Override
    public List<Cell> retrieveCells() {
        return column;
    }

}
