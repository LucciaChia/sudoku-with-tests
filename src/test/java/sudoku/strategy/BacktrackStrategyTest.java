package sudoku.strategy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sudoku.exceptions.IllegalSudokuStateException;
import sudoku.model.Sudoku;
import sudoku.readers.FileSudokuReader;

import java.io.File;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class BacktrackStrategyTest {

    static ClassLoader classLoader = new BacktrackStrategyTest().getClass().getClassLoader();

    private static final String inp1 = new File(classLoader.getResource("inputs/NakedSingleInACell/extremelySimple.txt").getFile()).getPath();
    private static final String out1 = new File(classLoader.getResource("outputs/NakedSingleInACell/extremelySimple.txt").getFile()).getPath();

    private static final String inp2 = new File(classLoader.getResource("inputs/simple1.txt").getFile()).getPath();
    private static final String out2 = new File(classLoader.getResource("outputs/simple1.txt").getFile()).getPath();

    private static final String inp3 = new File(classLoader.getResource("inputs/harder1.txt").getFile()).getPath();
    private static final String out3 = new File(classLoader.getResource("outputs/harder1.txt").getFile()).getPath();

    private static final String inp4 = new File(classLoader.getResource("inputs/extremelyHard1.txt").getFile()).getPath();
    private static final String out4 = new File(classLoader.getResource("outputs/extremelyHard1.txt").getFile()).getPath();

    private static final String inp5 = new File(classLoader.getResource("inputs/extremelyHard2.txt").getFile()).getPath();
    private static final String out5 = new File(classLoader.getResource("outputs/extremelyHard2.txt").getFile()).getPath();

    private static final String inp6 = new File(classLoader.getResource("inputs/extremelyHard3.txt").getFile()).getPath();
    private static final String out6 = new File(classLoader.getResource("outputs/extremelyHard3.txt").getFile()).getPath();

    private static final String inp7 = new File(classLoader.getResource("inputs/extremelyHard3.txt").getFile()).getPath();
    private static final String out7 = new File(classLoader.getResource("outputs/extremelyHard3.txt").getFile()).getPath();

    @ParameterizedTest
    @MethodSource("linksToInputs")
    void resolveSudoku(String inputSudokuMatrixPath, String expectedSudokuOutputPath) {
        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        int[][] inputData = fileSudokuReader.read(inputSudokuMatrixPath);
        int[][] expectedOutput = fileSudokuReader.read(expectedSudokuOutputPath);

        try {
            Sudoku sudoku = new Sudoku(inputData);
            BacktrackStrategy backtrackStrategy = new BacktrackStrategy();
            sudoku = backtrackStrategy.resolveSudoku(sudoku);
            printPoss(sudoku);
            System.out.println("=================================");
            assertArrayEquals(expectedOutput, setArrayAccordingToObjectValues(sudoku));
        } catch (IllegalSudokuStateException ex) {
            System.out.println("Test incorrect input");
        }
    }

    private static Stream<Arguments> linksToInputs() {
        return Stream.of(Arguments.of(BacktrackStrategyTest.inp1, BacktrackStrategyTest.out1),
                Arguments.of(BacktrackStrategyTest.inp2, BacktrackStrategyTest.out2),
                Arguments.of(BacktrackStrategyTest.inp3, BacktrackStrategyTest.out3),
                Arguments.of(BacktrackStrategyTest.inp4, BacktrackStrategyTest.out4),
                Arguments.of(BacktrackStrategyTest.inp5, BacktrackStrategyTest.out5),
                Arguments.of(BacktrackStrategyTest.inp6, BacktrackStrategyTest.out6),
                Arguments.of(BacktrackStrategyTest.inp7, BacktrackStrategyTest.out7)
        );
    }

    private int[][] setArrayAccordingToObjectValues(Sudoku sudoku) {
        int[][] output = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                output[i][j] = sudoku.getRows().get(i).getCell(j).getActualValue();
            }
        }
        return output;
    }

    private void printPoss(Sudoku sudoku) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.println(sudoku.getRows().get(i).getCell(j).toString());
            }
            System.out.println("*");
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(sudoku.getRows().get(i).getCell(j).getActualValue() + " ");
            }
            System.out.println();
        }
    }
}
