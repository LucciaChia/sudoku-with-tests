import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sudoku.model.*;
import sudoku.processing.FileSudokuReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SudokuTest {

    static ClassLoader classLoader = new SolutionTest().getClass().getClassLoader();

    private static final String inp1 = new File(classLoader.getResource("inputs/simple1.txt").getFile()).getPath();
    private static final String inp2 = new File(classLoader.getResource("inputs/simple2.txt").getFile()).getPath();
    private static final String inp3 = new File(classLoader.getResource("inputs/simple3.txt").getFile()).getPath();
    private static final String inp4 = new File(classLoader.getResource("inputs/simple4.txt").getFile()).getPath();
    private static final String inp5 = new File(classLoader.getResource("inputs/harder1.txt").getFile()).getPath();
    private static final String inp6 = new File(classLoader.getResource("inputs/harder2.txt").getFile()).getPath();
    private static final String inp7 = new File(classLoader.getResource("inputs/harder3.txt").getFile()).getPath();
    private static final String inp8 = new File(classLoader.getResource("inputs/harder4.txt").getFile()).getPath();

    @ParameterizedTest
    @MethodSource("linksToInputs")
    public void createSudokuElementObjects(String inputSudokuMatrixPath) {
        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        int[][] inputData = fileSudokuReader.read(inputSudokuMatrixPath);

        Sudoku sudoku = new Sudoku();
        ArrayList<List<? extends SudokuElement>> sudokuElementsList = sudoku.createSudokuElementObjectsService(inputData);

        List<Horizontal> horizontals = (List<Horizontal>)sudokuElementsList.get(0);
        List<Vertical> verticals = (List<Vertical>)sudokuElementsList.get(1);
        List<Square> squares = (List<Square>)sudokuElementsList.get(2);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                assertNotNull(horizontals.get(i).getCellInHorizontal(j));
                assertNotNull(verticals.get(j).getCellInVertical(i));
                assertNotNull(squares.get((i / 3) * 3 + j / 3).getCells().get(j));
            }
        }
    }

    private static Stream<Arguments> linksToInputs() {
        return Stream.of(Arguments.of(SudokuTest.inp1),
                Arguments.of(SudokuTest.inp2),
                Arguments.of(SudokuTest.inp3),
                Arguments.of(SudokuTest.inp4),
                Arguments.of(SudokuTest.inp5),
                Arguments.of(SudokuTest.inp6),
                Arguments.of(SudokuTest.inp7),
                Arguments.of(SudokuTest.inp8)
        );
    }

    @Test
    public void shouldCreateSquareMultipleTests() {
        Sudoku sudoku = new Sudoku();
        boolean[][] solutionMatrix = new boolean[9][9];

        // boolean sa automaticky inicializuje na false => netreba inic. cyklus

        int[] squareStartingIndexes = {0, 3, 6};
        for (int i: squareStartingIndexes) {
            for (int j: squareStartingIndexes) {
                solutionMatrix[i][j] = true;
            }
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                assertEquals(solutionMatrix[i][j],sudoku.shouldCreateSquare(i,j));
            }
        }
    }

}
