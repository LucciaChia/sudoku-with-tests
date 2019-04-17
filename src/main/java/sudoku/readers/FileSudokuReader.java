package sudoku.readers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class FileSudokuReader implements SudokuReader {

    @Override
    public int[][] read(String path) {
        int[][] data = new int[9][9];
        try {
            File file = new File(path);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            // v pripade new FileReader(file) = null => bufferedReader da NullPointerException =>
            // nevytvori sa a netreba robit bufferedReader.close()
            try {
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
            } finally {
                bufferedReader.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return data;
    }
}
