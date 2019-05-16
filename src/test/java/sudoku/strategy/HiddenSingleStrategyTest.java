package sudoku.strategy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sudoku.exceptions.IllegalSudokuStateException;
import sudoku.model.StrategyType;
import sudoku.model.Sudoku;
import sudoku.readers.FileSudokuReader;

import java.io.File;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HiddenSingleStrategyTest {
    static ClassLoader classLoader = new HiddenSingleStrategyTest().getClass().getClassLoader();

    private static final String inp1 = new File(classLoader.getResource("inputs/NakedSingleInACell/extremelySimple.txt").getFile()).getPath();
    private static final String out1 = new File(classLoader.getResource("outputs/NakedSingleInACell/extremelySimple.txt").getFile()).getPath();

    private static final String inp2 = new File(classLoader.getResource("inputs/simple1.txt").getFile()).getPath();
    private static final String out2 = new File(classLoader.getResource("outputs/simple1.txt").getFile()).getPath();

    private static final String inp3 = new File(classLoader.getResource("inputs/harder1.txt").getFile()).getPath();
    private static final String out3 = new File(classLoader.getResource("outputs/HiddenSingleInACell/harder1.txt").getFile()).getPath();

    @ParameterizedTest
    @MethodSource("linksToInputs")
    void resolveSudoku(String inputSudokuMatrixPath, String expectedSudokuOutputPath) {
        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        int[][] inputData = fileSudokuReader.read(inputSudokuMatrixPath);
        int[][] expectedOutput = fileSudokuReader.read(expectedSudokuOutputPath);
        StrategyType initialSudokuLevelType;
        StrategyType endSudokuLevelType;

        try {
            Sudoku sudoku = new Sudoku(inputData);
            initialSudokuLevelType = sudoku.getSudokuLevelType();
            NakedSingleStrategy nakedSingleStrategy = new NakedSingleStrategy();
            HiddenSingleStrategy hiddenSingleStrategy = new HiddenSingleStrategy();

            do {
                nakedSingleStrategy.resolveSudoku(sudoku);
                System.out.println(" N ");
                if (!nakedSingleStrategy.isUpdated()) {
                    hiddenSingleStrategy.resolveSudoku(sudoku);
                    System.out.println(" H ");
                }
            } while (nakedSingleStrategy.isUpdated() || hiddenSingleStrategy.isUpdated());
            endSudokuLevelType = sudoku.getSudokuLevelType();

            printPoss(sudoku);
            System.out.println("=================================");
            assertArrayEquals(expectedOutput, setArrayAccordingToObjectValues(sudoku));
            assertEquals(StrategyType.LOW, initialSudokuLevelType);
            assertEquals(StrategyType.LOW, endSudokuLevelType);
        } catch (IllegalSudokuStateException ex) {
            System.out.println("Test incorrect input");
        }
    }

    private static Stream<Arguments> linksToInputs() {
        return Stream.of(Arguments.of(HiddenSingleStrategyTest.inp1, HiddenSingleStrategyTest.out1),
                Arguments.of(HiddenSingleStrategyTest.inp2, HiddenSingleStrategyTest.out2),
                Arguments.of(HiddenSingleStrategyTest.inp3, HiddenSingleStrategyTest.out3)
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