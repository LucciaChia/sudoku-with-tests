package sudoku.processingUsingStrategy.invoker;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sudoku.customExceptions.IllegalSudokuStateException;
import sudoku.model.Sudoku;
import sudoku.processingUsingCommand.Command;
import sudoku.processingUsingCommand.CommandPicker;
import sudoku.processingUsingCommand.ManualInvoker;
import sudoku.processingUsingStrategy.BacktrackLuciaTest;
import sudoku.processingUsingStrategy.NakedSingleInACell;
import sudoku.readers.FileSudokuReader;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class ManualInvokerTest {
    private static final Logger extAppLogFile = LoggerFactory.getLogger(ManualInvoker.class);

    static ClassLoader classLoader = new BacktrackLuciaTest().getClass().getClassLoader();

    private static final String inp1 = new File(classLoader.getResource("inputs/NakedSingleInACell/extremelySimple.txt").getFile()).getPath();
    private static final String out1 = new File(classLoader.getResource("outputs/NakedSingleInACell/extremelySimple.txt").getFile()).getPath();

    private static final String inp2 = new File(classLoader.getResource("inputs/simple1.txt").getFile()).getPath();
    private static final String out2 = new File(classLoader.getResource("outputs/simple1.txt").getFile()).getPath();

    private static final String inp3 = new File(classLoader.getResource("inputs/harder1.txt").getFile()).getPath();
    private static final String out3 = new File(classLoader.getResource("outputs/harder1.txt").getFile()).getPath();

    private static final String inp4 = new File(classLoader.getResource("inputs/extremelyHard1.txt").getFile()).getPath();
    private static final String out4 = new File(classLoader.getResource("outputs/extremelyHard1.txt").getFile()).getPath();

    private static final String inp5 = new File(classLoader.getResource("inputs/1step.txt").getFile()).getPath();
    private static final String out5 = new File(classLoader.getResource("outputs/simple1.txt").getFile()).getPath();

    private static final String inp6 = new File(classLoader.getResource("inputs/2steps.txt").getFile()).getPath();
    private static final String out6 = new File(classLoader.getResource("outputs/simple1.txt").getFile()).getPath();

    private static final String inp7 = new File(classLoader.getResource("inputs/9steps.txt").getFile()).getPath();
    private static final String out7 = new File(classLoader.getResource("outputs/simple1.txt").getFile()).getPath();

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
        result = ((CommandPicker) command).getSudoku();

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
        result = ((CommandPicker) command).getSudoku();

        // THEN
        assertArrayEquals(expectedOutput, setArrayAccordingToObjectValues(result));
    }

}
