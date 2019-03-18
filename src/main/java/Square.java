import java.util.ArrayList;
import java.util.List;

public class Square implements SudokuElement{
    private List<Cell> cellsInSquare = new ArrayList<>();


    public List<Cell> getcellsInSquare() {
        return cellsInSquare;
    }


    public void printSquare() {
        for (int i = 0; i < 9; i++) {
                System.out.print(cellsInSquare.get(i).getActualValue());
                if (i%3 == 2) {
                    System.out.println();
                }
        }
    }

    public void addCell(Cell cell) {
        cellsInSquare.add(cell);
    }

    @Override
    public List<Cell> retrieveCells() {
        return cellsInSquare;
    }

    @Override
    public String toString() {
        String output = "";
        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                output +=  " " + cellsInSquare.get(index).getActualValue();
                index++;
            }
            output += "\n";
        }

        return output;
    }

}
