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

class NakedSingleStrategyTest {

    static ClassLoader classLoader = new NakedSingleStrategyTest().getClass().getClassLoader();

    private static final Displayer consoleDisplayer = new ConsoleDisplayer();

    private static final String inp1 = new File(classLoader.getResource("inputs/NakedSingleInACell/extremelySimple.txt").getFile()).getPath();
    private static final String out1 = new File(classLoader.getResource("outputs/NakedSingleInACell/extremelySimple.txt").getFile()).getPath();

    private static final String inp2 = new File(classLoader.getResource("inputs/simple1.txt").getFile()).getPath();
    private static final String out2 = new File(classLoader.getResource("outputs/NakedSingleInACell/simple1.txt").getFile()).getPath();

    private static final String inp3 = new File(classLoader.getResource("inputs/harder1.txt").getFile()).getPath();
    private static final String out3 = new File(classLoader.getResource("outputs/NakedSingleInACell/harder1.txt").getFile()).getPath();


    /**
     * extremelySimple.txt input should be completely resolved by NakedSingleStrategy() method
     * hard inputs won't be changed by this method at first at all
     */
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
            do {
                nakedSingleStrategy.resolveSudoku(sudoku);
            } while (nakedSingleStrategy.isUpdated());
            endSudokuLevelType = sudoku.getSudokuLevelType();

            printPoss(sudoku);
            consoleDisplayer.displayLine("=================================");
            assertArrayEquals(expectedOutput, setArrayAccordingToObjectValues(sudoku));
            assertEquals(StrategyType.LOW, initialSudokuLevelType);
            assertEquals(StrategyType.LOW, endSudokuLevelType);
        } catch (IllegalSudokuStateException ex) {
            consoleDisplayer.displayLine("Test incorrect input");
        }
    }
    @Test
    public void getName() {
        NakedSingleStrategy nakedSingleStrategy = new NakedSingleStrategy();
        assertEquals("Naked Single", nakedSingleStrategy.getName());
    }

    @Test
    public void getType() {
        NakedSingleStrategy nakedSingleStrategy = new NakedSingleStrategy();
        assertEquals("LOW", nakedSingleStrategy.getType().toString());
    }
    private static Stream<Arguments> linksToInputs() {
        return Stream.of(Arguments.of(NakedSingleStrategyTest.inp1, NakedSingleStrategyTest.out1),
                Arguments.of(NakedSingleStrategyTest.inp2, NakedSingleStrategyTest.out2),
                Arguments.of(NakedSingleStrategyTest.inp3, NakedSingleStrategyTest.out3)
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