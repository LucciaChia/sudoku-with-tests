package sudoku.strategy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sudoku.console.ConsoleDisplayer;
import sudoku.console.Displayer;
import sudoku.exceptions.IllegalSudokuStateException;
import sudoku.model.StrategyType;
import sudoku.model.Sudoku;
import sudoku.readers.FileSudokuReader;

import java.io.File;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PointingPairsBoxRowColumnStrategyTest {
    static ClassLoader classLoader = new PointingPairsBoxRowColumnStrategyTest().getClass().getClassLoader();

    private static final Displayer consoleDisplayer = new ConsoleDisplayer();

    private static final String inp1 = new File(classLoader.getResource("inputs/NakedSingleInACell/extremelySimple.txt").getFile()).getPath();
    private static final String out1 = new File(classLoader.getResource("outputs/NakedSingleInACell/extremelySimple.txt").getFile()).getPath();

    private static final String inp2 = new File(classLoader.getResource("inputs/simple1.txt").getFile()).getPath();
    private static final String out2 = new File(classLoader.getResource("outputs/simple1.txt").getFile()).getPath();

    private static final String inp3 = new File(classLoader.getResource("inputs/harder1.txt").getFile()).getPath();
    private static final String out3 = new File(classLoader.getResource("outputs/harder1.txt").getFile()).getPath();

    @ParameterizedTest
    @MethodSource("linksToInputs")
    void resolveSudokuBoxStrategy(String inputSudokuMatrixPath, String expectedSudokuOutputPath) {

        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        StrategyType initialSudokuLevelType = StrategyType.LOW;
        StrategyType endSudokuLevelType;
        int[][] inputData = fileSudokuReader.read(inputSudokuMatrixPath);
        int[][] expectedOutput = fileSudokuReader.read(expectedSudokuOutputPath);

        try {
            Sudoku sudoku = new Sudoku(inputData);
            initialSudokuLevelType = sudoku.getSudokuLevelType();
            NakedSingleStrategy nakedSingleStrategy = new NakedSingleStrategy();
            HiddenSingleStrategy hiddenSingleStrategy = new HiddenSingleStrategy();
            PointingPairsBoxStrategy pointingPairsBoxStrategy = new PointingPairsBoxStrategy();

            do {
                nakedSingleStrategy.resolveSudoku(sudoku);
                consoleDisplayer.displayLine(" N ");
                if (!nakedSingleStrategy.isUpdated()) {
                    hiddenSingleStrategy.resolveSudoku(sudoku);
                    consoleDisplayer.displayLine(" H ");
                }

                if (!hiddenSingleStrategy.isUpdated()) {
                    pointingPairsBoxStrategy.resolveSudoku(sudoku);
                    consoleDisplayer.displayLine(" P ");
                }
            } while (nakedSingleStrategy.isUpdated() || hiddenSingleStrategy.isUpdated() || pointingPairsBoxStrategy.isUpdated());
            endSudokuLevelType = sudoku.getSudokuLevelType();

            printPoss(sudoku);
            consoleDisplayer.displayLine("=================================");
            assertArrayEquals(expectedOutput, setArrayAccordingToObjectValues(sudoku));
            assertEquals(StrategyType.LOW, initialSudokuLevelType);
            assertEquals(StrategyType.MEDIUM, endSudokuLevelType);
        } catch (IllegalSudokuStateException ex) {
            consoleDisplayer.displayLine("Test incorrect input");
        }
    }

    @ParameterizedTest
    @MethodSource("linksToInputs")
    void resolveSudokuRowColumnStrategy(String inputSudokuMatrixPath, String expectedSudokuOutputPath) {
        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        int[][] inputData = fileSudokuReader.read(inputSudokuMatrixPath);
        int[][] expectedOutput = fileSudokuReader.read(expectedSudokuOutputPath);

        try {
            Sudoku sudoku = new Sudoku(inputData);
            NakedSingleStrategy nakedSingleStrategy = new NakedSingleStrategy();
            HiddenSingleStrategy hiddenSingleStrategy = new HiddenSingleStrategy();
            PointingPairsRowColumnStrategy pointingPairsRowColumnStrategy = new PointingPairsRowColumnStrategy();

            do {
                nakedSingleStrategy.resolveSudoku(sudoku);
                consoleDisplayer.displayLine(" N ");
                if (!nakedSingleStrategy.isUpdated()) {
                    hiddenSingleStrategy.resolveSudoku(sudoku);
                    consoleDisplayer.displayLine(" H ");
                }

                if (!hiddenSingleStrategy.isUpdated()) {
                    pointingPairsRowColumnStrategy.resolveSudoku(sudoku);
                    consoleDisplayer.displayLine(" P ");
                }
            } while (nakedSingleStrategy.isUpdated() || hiddenSingleStrategy.isUpdated() || pointingPairsRowColumnStrategy.isUpdated());

            printPoss(sudoku);
            consoleDisplayer.displayLine("=================================");
            assertArrayEquals(expectedOutput, setArrayAccordingToObjectValues(sudoku));
        } catch (IllegalSudokuStateException ex) {
            consoleDisplayer.displayLine("Test incorrect input");
        }
    }

    @Test
    void findPartnerCell() {
    }

    private static Stream<Arguments> linksToInputs() {
        return Stream.of(Arguments.of(PointingPairsBoxRowColumnStrategyTest.inp1, PointingPairsBoxRowColumnStrategyTest.out1),
                Arguments.of(PointingPairsBoxRowColumnStrategyTest.inp2, PointingPairsBoxRowColumnStrategyTest.out2),
                Arguments.of(PointingPairsBoxRowColumnStrategyTest.inp3, PointingPairsBoxRowColumnStrategyTest.out3)
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
                consoleDisplayer.displayLine(sudoku.getRows().get(i).getCell(j).toString());
            }
            consoleDisplayer.displayLine("*");
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                consoleDisplayer.display(sudoku.getRows().get(i).getCell(j).getActualValue() + " ");
            }
            consoleDisplayer.displayLine("");
        }
    }
}