package sudoku.customExceptions;

public class IllegalSudokuStateException extends Exception {
    private final int incorrectNumber;
    private final int iCoordinate;
    private final int jCoordinate;

    public IllegalSudokuStateException(final int incorrectNumber, final int i, final int j) {
        this.incorrectNumber = incorrectNumber;
        iCoordinate = i;
        jCoordinate = j;
    }

    @Override
    public String toString() {
        return "IllegalSudokuException - number " + incorrectNumber + " at coordinate i = " + iCoordinate + ", j = " + jCoordinate +
                " is either outside the range <1;9> or it occurs more than once in row, column or square";
    }
}
