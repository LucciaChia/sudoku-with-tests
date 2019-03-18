import java.util.ArrayList;
import java.util.List;

public class Vertical implements SudokuElement{
    private List<Cell> row = new ArrayList<>(); // idem zvisle cez kazdy riadok, preto row

    public Vertical() {

    }

    public Vertical(List<Cell> row) {
        this.row = row;
    }

    public List<Cell> getRow() {
        return row;
    }

    public Cell getCellInVertical(int i) {
        return row.get(i);
    }

    @Override
    public String toString() {
        String output = "";
        for (int i = 0; i < 9; i++) {
            output += "\n" + row.get(i).getActualValue();
        }
        return output;
    }

    @Override
    public List<Cell> retrieveCells() {
        return row;
    }

}
