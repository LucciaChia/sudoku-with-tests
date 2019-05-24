package sudoku.readers;

/**
 *  an interface for sudoku reader that will prepare matrix of initial values for sudoku from an input
 */
public interface SudokuReader {

    /**
     * Method that reads input file and creates an matrix of initial values for sudoku
     *
     * @param path  a string containing a path to the input file
     * @return      an array that is a matrix of initial values of sudoku
     */
    int[][] read(String path);
}
