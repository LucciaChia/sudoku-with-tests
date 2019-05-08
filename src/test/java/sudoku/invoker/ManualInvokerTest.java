package sudoku.invoker;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sudoku.command.Command;
import sudoku.command.CommandPicker;
import sudoku.command.ManualInvoker;
import sudoku.exceptions.IllegalSudokuStateException;
import sudoku.model.Sudoku;
import sudoku.readers.FileSudokuReader;
import sudoku.step.OneChangeStep;
import sudoku.strategy.BacktrackLuciaTest;
import sudoku.strategy.StrategyFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    private StrategyFactory strategyFactory = new StrategyFactory();

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
        Sudoku result;
        ManualInvoker invoker;
        Command command;

        try {
            sudoku = new Sudoku(inputData);
        } catch (IllegalSudokuStateException ex) {
            extAppLogFile.error("Test incorrect input");
        }

        // WHEN
        invoker = new ManualInvoker(sudoku);
        invoker.setStrategies(strategyFactory.createNakedSingleInACellStrategy());
        command = invoker.getNextState();
        result = ((OneChangeStep)((CommandPicker) command).getStepList().get(0)).getSudoku();

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
        Sudoku result;
        ManualInvoker invoker;
        Command command;

        try {
            sudoku = new Sudoku(inputData);
        } catch (IllegalSudokuStateException ex) {
            extAppLogFile.error("Test incorrect input");
        }

        // WHEN
        invoker = new ManualInvoker(sudoku);
        invoker.setStrategies(strategyFactory.createNakedSingleInACellStrategy());
        invoker.getNextState();
        command = invoker.getNextState();
        result = ((OneChangeStep)((CommandPicker) command).getStepList().get(0)).getSudoku();

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
        Sudoku result;
        ManualInvoker invoker;
        Command command;

        try {
            sudoku = new Sudoku(inputData);
        } catch (IllegalSudokuStateException ex) {
            extAppLogFile.error("Test incorrect input");
        }

        // WHEN
        invoker = new ManualInvoker(sudoku);
        invoker.setStrategies(strategyFactory.createNakedSingleInACellStrategy());
        command = invoker.getNextState();
        result = ((OneChangeStep)((CommandPicker) command).getStepList().get(0)).getSudoku();

        // THEN
        assertArrayEquals(expectedOutput, setArrayAccordingToObjectValues(result));
    }

    @Test
    public void testPreviousStep() {
        // GIVEN
        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        int[][] inputData = fileSudokuReader.read(inp7);
//        int[][] expectedOutput = fileSudokuReader.read(inp7);
        Sudoku sudoku = null;
        Sudoku result;
        Sudoku expectedResult;
        ManualInvoker invoker;
//        Command command0 = null;
//        Command command1 = null;
        Command command2;

        try {
            sudoku = new Sudoku(inputData);
        } catch (IllegalSudokuStateException ex) {
            extAppLogFile.error("Test incorrect input");
        }

        // WHEN
        invoker = new ManualInvoker(sudoku);
        invoker.setStrategies(strategyFactory.createNakedSingleInACellStrategy());
//        command0 = invoker.getNextState();
        invoker.getNextState();
//        command1 = invoker.getNextState();
        invoker.getNextState();
        command2 = invoker.getPreviousState();
        expectedResult = ((OneChangeStep)((CommandPicker) command2).getStepList().get(0)).getSudoku();
        result = ((OneChangeStep)((CommandPicker) command2).getStepList().get(0)).getSudoku();

        // THEN
        assertArrayEquals(setArrayAccordingToObjectValues(expectedResult), setArrayAccordingToObjectValues(result));
    }

    @Test
    public void testGetNextStateUsingChosenStrategy() {
        // GIVEN
        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        int[][] inputData = fileSudokuReader.read(inp7);
        int[][] expectedOutput = fileSudokuReader.read(out7);
        Sudoku sudoku = null;
        Sudoku result;
        Sudoku expectedResult;
        ManualInvoker invoker;
        List<Command> commands = new ArrayList<>();
        try {
            sudoku = new Sudoku(inputData);
        } catch (IllegalSudokuStateException ex) {
            extAppLogFile.error("Test incorrect input");
        }
        invoker = new ManualInvoker(sudoku);

        // WHEN
        commands.add(invoker.getNextState(strategyFactory.createNakedSingleInACellStrategy()));
        commands.add(invoker.getNextState(strategyFactory.createNakedSingleInACellStrategy()));
        commands.add(invoker.getNextState(strategyFactory.createHiddenSingleInACellStrategy()));
        commands.add(invoker.getNextState(strategyFactory.createNakedSingleInACellStrategy()));
        commands.add(invoker.getNextState(strategyFactory.createPointingPairsInCellStrategy()));
        commands.add(invoker.getNextState(strategyFactory.createNakedSingleInACellStrategy()));
        commands.add(invoker.getNextState(strategyFactory.createBacktrackStrategy()));
        result = ((OneChangeStep)((CommandPicker) commands.get(commands.size() - 1)).getStepList().get(0)).getSudoku();

        // THEN
        assertArrayEquals(expectedOutput, setArrayAccordingToObjectValues(result));
        assertEquals("0: NackedSingleInACell", ((OneChangeStep)((CommandPicker)commands.get(0)).getStepList().get(0)).getResolvable().getName());
        assertEquals("0: NackedSingleInACell", ((OneChangeStep)((CommandPicker)commands.get(1)).getStepList().get(0)).getResolvable().getName());
        assertEquals("1: HiddenSingleInACell", ((OneChangeStep)((CommandPicker)commands.get(2)).getStepList().get(0)).getResolvable().getName());
        assertEquals("0: NackedSingleInACell", ((OneChangeStep)((CommandPicker)commands.get(3)).getStepList().get(0)).getResolvable().getName());
        assertEquals("2: PointingPairsInCell", ((OneChangeStep)((CommandPicker)commands.get(4)).getStepList().get(0)).getResolvable().getName());
        assertEquals("0: NackedSingleInACell", ((OneChangeStep)((CommandPicker)commands.get(5)).getStepList().get(0)).getResolvable().getName());
        assertEquals("3: Backtrack", ((OneChangeStep)((CommandPicker)commands.get(6)).getStepList().get(0)).getResolvable().getName());
    }

}

