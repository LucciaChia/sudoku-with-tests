import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sudoku.model.Horizontal;
import sudoku.model.Square;
import sudoku.model.SudokuElement;
import sudoku.model.Vertical;
import sudoku.processing.FileSudokuReader;
import sudoku.processing.SudokuService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MainTest {

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

        SudokuService sudokuService = new SudokuService();

        ArrayList<List<? extends SudokuElement>> sudokuElementsList = sudokuService.createSudokuElementObjectsService(inputData);

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
        return Stream.of(Arguments.of(MainTest.inp1),
                Arguments.of(MainTest.inp2),
                Arguments.of(MainTest.inp3),
                Arguments.of(MainTest.inp4),
                Arguments.of(MainTest.inp5),
                Arguments.of(MainTest.inp6),
                Arguments.of(MainTest.inp7),
                Arguments.of(MainTest.inp8)
        );
    }

    @Test
    public void shouldCreateSquareMultipleTests() {
        SudokuService sudokuService = new SudokuService();
        boolean[][] solutionMatrix = new boolean[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                solutionMatrix[i][j] = false;
            }
        }

        solutionMatrix[0][0] = true;
        solutionMatrix[0][3] = true;
        solutionMatrix[0][6] = true;
        solutionMatrix[3][0] = true;
        solutionMatrix[3][3] = true;
        solutionMatrix[3][6] = true;
        solutionMatrix[6][0] = true;
        solutionMatrix[6][3] = true;
        solutionMatrix[6][6] = true;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                assertEquals(solutionMatrix[i][j],sudokuService.shouldCreateSquare(i,j));
            }
        }
    }

}
