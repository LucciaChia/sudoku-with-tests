package sudoku.readers;

import sudoku.console.ConsoleDisplayer;
import sudoku.console.Displayer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Implementation of SudokuReader that will read an input file and set values of matrix for sudoku
 */
public class FileSudokuReader implements SudokuReader {

    private static final Displayer consoleDisplayer = new ConsoleDisplayer();

    /**
     * Method that reads the input file and setsvalues af the matrix to values that are in the input file.
     * Values are seperated by " " and there are should be 9 rows of 9 values
     *
     * @param path  a string containing a path to the input file
     * @return      an array that is a matrix of initial values of sudoku
     */
    @Override
    public int[][] read(String path) {
        int[][] data = new int[9][9];
        try {
            File file = new File(path);
            // v pripade new FileReader(file) = null => bufferedReader da NullPointerException =>
            // nevytvori sa a netreba robit bufferedReader.close()
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
                String st;
                String[] row;
                int i = 0;
                while ((st = bufferedReader.readLine()) != null) {
                    st = st.trim();
                    row = st.split(" ");
                    for (int j = 0; j < row.length; j++) {
                        data[i][j] = Integer.valueOf(row[j]);
                    }
                    i++;
                }
            }
        } catch (Exception e) {
            consoleDisplayer.displayLine(e.getMessage());
        }

        return data;
    }
}
