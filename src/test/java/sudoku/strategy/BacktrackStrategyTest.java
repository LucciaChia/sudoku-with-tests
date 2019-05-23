package sudoku.strategy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sudoku.console.ConsoleDisplayer;
import sudoku.console.Displayer;
import sudoku.exceptions.IllegalSudokuStateException;
import sudoku.exceptions.NoAvailableSolutionException;
import sudoku.model.StrategyType;
import sudoku.model.Sudoku;
import sudoku.readers.FileSudokuReader;

import java.io.File;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BacktrackStrategyTest {

    private static ClassLoader classLoader = new BacktrackStrategyTest().getClass().getClassLoader();

    private static final Displayer consoleDisplayer = new ConsoleDisplayer();

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
        StrategyType initialSudokuLevelType;
        StrategyType endSudokuLevelType;

        try {
            Sudoku sudoku = new Sudoku(inputData);
            initialSudokuLevelType = sudoku.getSudokuLevelType();
            BacktrackStrategy backtrackStrategy = new BacktrackStrategy();
            sudoku = backtrackStrategy.resolveSudoku(sudoku);
            endSudokuLevelType = sudoku.getSudokuLevelType();
            printPoss(sudoku);
            consoleDisplayer.displayLine("=================================");
            assertArrayEquals(expectedOutput, setArrayAccordingToObjectValues(sudoku));
            assertEquals(StrategyType.LOW, initialSudokuLevelType);
            assertEquals(StrategyType.HIGH, endSudokuLevelType);
        } catch (IllegalSudokuStateException ex) {
            consoleDisplayer.displayLine(ex.toString());
        } catch (NoAvailableSolutionException ne) {
            consoleDisplayer.displayLine(ne.toString());
        }
    }
    @Test
    public void getName() {
        BacktrackStrategy backtrackStrategy = new BacktrackStrategy();
        assertEquals("Backtracking", backtrackStrategy.getName());
    }

    @Test
    public void getType() {
        BacktrackStrategy backtrackStrategy = new BacktrackStrategy();
        assertEquals("HIGH", backtrackStrategy.getType().toString());
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
