package sudoku.processingUsingStrategy.invoker;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sudoku.exceptions.IllegalSudokuStateException;
import sudoku.model.Sudoku;
import sudoku.processingUsingCommand.Command;
import sudoku.processingUsingCommand.CommandPicker;
import sudoku.processingUsingCommand.ManualInvoker;
import sudoku.processingUsingStrategy.BacktrackLuciaTest;
import sudoku.processingUsingStrategy.NakedSingleInACell;
import sudoku.readers.FileSudokuReader;
import sudoku.stepHandlers.OneChangeStep;

import java.io.File;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class ManualInvokerTest {
    private static final Logger extAppLogFile = LoggerFactory.getLogger(ManualInvoker.class);

    private static ClassLoader classLoader = BacktrackLuciaTest.class.getClassLoader();

//    private static final String inp1 = new File(Objects.requireNonNull(classLoader.getResource("inputs/NakedSingleInACell/extremelySimple.txt")).getFile()).getPath();
//    private static final String out1 = new File(Objects.requireNonNull(classLoader.getResource("outputs/NakedSingleInACell/extremelySimple.txt")).getFile()).getPath();

//    private static final String inp2 = new File(Objects.requireNonNull(classLoader.getResource("inputs/simple1.txt")).getFile()).getPath();
//    private static final String out2 = new File(Objects.requireNonNull(classLoader.getResource("outputs/simple1.txt")).getFile()).getPath();

//    private static final String inp3 = new File(Objects.requireNonNull(classLoader.getResource("inputs/harder1.txt")).getFile()).getPath();
//    private static final String out3 = new File(Objects.requireNonNull(classLoader.getResource("outputs/harder1.txt")).getFile()).getPath();

//    private static final String inp4 = new File(Objects.requireNonNull(classLoader.getResource("inputs/extremelyHard1.txt")).getFile()).getPath();
//    private static final String out4 = new File(Objects.requireNonNull(classLoader.getResource("outputs/extremelyHard1.txt")).getFile()).getPath();

    private static final String inp5 = new File(Objects.requireNonNull(classLoader.getResource("inputs/1step.txt")).getFile()).getPath();
    private static final String out5 = new File(Objects.requireNonNull(classLoader.getResource("outputs/simple1.txt")).getFile()).getPath();

    private static final String inp6 = new File(Objects.requireNonNull(classLoader.getResource("inputs/2steps.txt")).getFile()).getPath();
    private static final String out6 = new File(Objects.requireNonNull(classLoader.getResource("outputs/simple1.txt")).getFile()).getPath();

    private static final String inp7 = new File(Objects.requireNonNull(classLoader.getResource("inputs/9steps.txt")).getFile()).getPath();
    private static final String out7 = new File(Objects.requireNonNull(classLoader.getResource("outputs/simple1.txt")).getFile()).getPath();

    private int[][] setArrayAccordingToObjectValues(Sudoku sudoku) {
        int[][] output = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                output[i][j] = sudoku.getRows().get(i).getCell(j).getActualValue();
            }
        }
        return output;
    }

    @Test
    public void testOneStepSolvingSudoku() {

        // GIVEN
        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        int[][] inputData = fileSudokuReader.read(inp5);
        int[][] expectedOutput = fileSudokuReader.read(out5);
        Sudoku sudoku = null;
        Sudoku result = null;
        ManualInvoker invoker = null;
        Command command = null;

        try {
            sudoku = new Sudoku(inputData);
        } catch (IllegalSudokuStateException ex) {
            extAppLogFile.error("Test incorrect input");
        }

        // WHEN
        invoker = new ManualInvoker(sudoku);
        invoker.setStrategies(new NakedSingleInACell());
        command = invoker.getNextState();
        result = ((OneChangeStep)((CommandPicker) command).getStep()).getSudoku();

        // THEN
        assertArrayEquals(expectedOutput, setArrayAccordingToObjectValues(result));
    }

    @Test
    public void testTwoStepsSolvingSudoku() {

        // GIVEN
        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        int[][] inputData = fileSudokuReader.read(inp6);
        int[][] expectedOutput = fileSudokuReader.read(out6);
        Sudoku sudoku = null;
        Sudoku result = null;
        ManualInvoker invoker = null;
        Command command = null;

        try {
            sudoku = new Sudoku(inputData);
        } catch (IllegalSudokuStateException ex) {
            extAppLogFile.error("Test incorrect input");
        }

        // WHEN
        invoker = new ManualInvoker(sudoku);
        invoker.setStrategies(new NakedSingleInACell());
        command = invoker.getNextState();
        command = invoker.getNextState();
        result = ((OneChangeStep)((CommandPicker) command).getStep()).getSudoku();

        // THEN
        assertArrayEquals(expectedOutput, setArrayAccordingToObjectValues(result));
    }

    @Test
    public  void testFirstFromLastTwoStepSolvingSudoku() {
        // GIVEN
        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        int[][] inputData = fileSudokuReader.read(inp6);
        int[][] expectedOutput = fileSudokuReader.read(inp5);
        Sudoku sudoku = null;
        Sudoku result = null;
        ManualInvoker invoker = null;
        Command command = null;

        try {
            sudoku = new Sudoku(inputData);
        } catch (IllegalSudokuStateException ex) {
            extAppLogFile.error("Test incorrect input");
        }

        // WHEN
        invoker = new ManualInvoker(sudoku);
        invoker.setStrategies(new NakedSingleInACell());
        command = invoker.getNextState();
        result = ((OneChangeStep)((CommandPicker) command).getStep()).getSudoku();

        // THEN
        assertArrayEquals(expectedOutput, setArrayAccordingToObjectValues(result));
    }

    @Test
    public void testPreviousStep() {
        // GIVEN
        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        int[][] inputData = fileSudokuReader.read(inp7);
        int[][] expectedOutput = fileSudokuReader.read(inp7);
        Sudoku sudoku = null;
        Sudoku result = null;
        Sudoku expectedResult = null;
        ManualInvoker invoker = null;
        Command command0 = null;
        Command command1 = null;
        Command command2 = null;

        try {
            sudoku = new Sudoku(inputData);
        } catch (IllegalSudokuStateException ex) {
            extAppLogFile.error("Test incorrect input");
        }

        // WHEN
        invoker = new ManualInvoker(sudoku);
        invoker.setStrategies(new NakedSingleInACell());
        command0 = invoker.getNextState();
        command1 = invoker.getNextState();
        command2 = invoker.getPreviousState();
        expectedResult = ((OneChangeStep)((CommandPicker) command2).getStep()).getSudoku();
        result = ((OneChangeStep)((CommandPicker) command2).getStep()).getSudoku();

        // THEN
        assertArrayEquals(setArrayAccordingToObjectValues(expectedResult), setArrayAccordingToObjectValues(result));
    }

}

