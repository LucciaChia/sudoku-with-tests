package sudoku.processing;

import sudoku.model.Cell;
import sudoku.model.Row;
import sudoku.model.Sudoku;

import java.util.List;

public class SudokuService {

    private Sudoku sudoku;


    public SudokuService(Sudoku sudoku) {
        this.sudoku = sudoku;
    }


    public void resolveSudokuService() {
        System.out.println("POSSIBILITIES: ");
        Solution solution = new Solution(sudoku);
        List<Row> rows = solution.outputWITHOUTdataArray();

        for (int i = 0; i < rows.size(); i++) {
            for (int j = 0; j < 9; j++) {
                Cell cell= rows.get(i).getCell(j);
                if (cell.getActualValue() == 0) {
                    System.out.print(i + ":" + j + " = ");
                    System.out.println(cell.toString());
                }
            }
        }
    }

    public int[][] printSudokuMatrixService(){
        int[][] tempData = new int[9][9];
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                tempData[i][j] = sudoku.getRows().get(i).getCell(j).getActualValue();
                System.out.print(tempData[i][j] + " ");
            }
            System.out.println();
        }
        return tempData;
    }
}
