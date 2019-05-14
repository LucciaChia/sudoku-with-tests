package sudoku.invoker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sudoku.command.Command;
import sudoku.command.CommandPicker;
import sudoku.command.ManualInvoker;
import sudoku.exceptions.IllegalSudokuStateException;
import sudoku.model.Cell;
import sudoku.model.Sudoku;
import sudoku.readers.FileSudokuReader;
import sudoku.strategy.BacktrackStrategyTest;
import sudoku.strategy.Resolvable;
import sudoku.strategy.StrategyFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ManualInvokerTest {
    private static final Logger extAppLogFile = LoggerFactory.getLogger(ManualInvoker.class);

    private static ClassLoader classLoader = BacktrackStrategyTest.class.getClassLoader();

    private static final String inp5 = new File(Objects.requireNonNull(classLoader.getResource("inputs/1step.txt")).getFile()).getPath();
    private static final String out5 = new File(Objects.requireNonNull(classLoader.getResource("outputs/simple1.txt")).getFile()).getPath();

    private static final String inp6 = new File(Objects.requireNonNull(classLoader.getResource("inputs/2steps.txt")).getFile()).getPath();
    private static final String out6 = new File(Objects.requireNonNull(classLoader.getResource("outputs/simple1.txt")).getFile()).getPath();

    private static final String inp7 = new File(Objects.requireNonNull(classLoader.getResource("inputs/9steps.txt")).getFile()).getPath();
    private static final String out7 = new File(Objects.requireNonNull(classLoader.getResource("outputs/simple1.txt")).getFile()).getPath();

    private static final String inpPair = new File(Objects.requireNonNull(classLoader.getResource("inputs/PointingPairInput.txt")).getFile()).getPath();

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

    @ParameterizedTest
    @MethodSource("inputStrategies")
    public void testOneStepSolvingSudoku(Resolvable resolvable) {

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
        invoker.setStrategies(resolvable);
        command = invoker.getNextState();
        result = ((CommandPicker) command).getSudoku();

        // THEN
        assertArrayEquals(expectedOutput, setArrayAccordingToObjectValues(result));
    }

    @ParameterizedTest
    @MethodSource("inputStrategies")
    public void testTwoStepsSolvingSudoku(Resolvable resolvable) {

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
        invoker.setStrategies(resolvable);
        invoker.getNextState();
        command = invoker.getNextState();
        result = ((CommandPicker) command).getSudoku();

        // THEN
        assertArrayEquals(expectedOutput, setArrayAccordingToObjectValues(result));
    }

    @ParameterizedTest
    @MethodSource("inputStrategies")
    public  void testFirstFromLastTwoStepSolvingSudoku(Resolvable resolvable) {

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
        invoker.setStrategies(resolvable);
        command = invoker.getNextState();
        result = ((CommandPicker) command).getSudoku();

        // THEN
        assertArrayEquals(expectedOutput, setArrayAccordingToObjectValues(result));
    }

    @ParameterizedTest
    @MethodSource("inputStrategies")
    public void testPreviousStep(Resolvable resolvable) {

        // GIVEN
        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        int[][] inputData = fileSudokuReader.read(inp7);
        Sudoku sudoku = null;
        Sudoku result;
        Sudoku expectedResult;
        ManualInvoker invoker;
        Command command2;

        try {
            sudoku = new Sudoku(inputData);
        } catch (IllegalSudokuStateException ex) {
            extAppLogFile.error("Test incorrect input");
        }

        // WHEN
        invoker = new ManualInvoker(sudoku);
        invoker.setStrategies(resolvable);
        invoker.getNextState();
        invoker.getNextState();
        command2 = invoker.getPreviousState();
        expectedResult = ((CommandPicker) command2).getSudoku();
        result = ((CommandPicker) command2).getSudoku();

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
        result = ((CommandPicker) commands.get(commands.size() - 1)).getSudoku();

        // THEN
        assertArrayEquals(expectedOutput, setArrayAccordingToObjectValues(result));
        assertEquals("0: NackedSingleInACell", ((CommandPicker)commands.get(0)).getResolvable().getName());
        assertEquals("0: NackedSingleInACell", ((CommandPicker)commands.get(1)).getResolvable().getName());
        assertEquals("1: HiddenSingleInACell", ((CommandPicker)commands.get(2)).getResolvable().getName());
        assertEquals("0: NackedSingleInACell", ((CommandPicker)commands.get(3)).getResolvable().getName());
        assertEquals("2: PointingPairsInCell", ((CommandPicker)commands.get(4)).getResolvable().getName());
        assertEquals("0: NackedSingleInACell", ((CommandPicker)commands.get(5)).getResolvable().getName());
        assertEquals("3: Backtrack", ((CommandPicker)commands.get(6)).getResolvable().getName());
    }

    @Test
    public void testNextAndPreviousOnPointingPairsInCellStrategy() {
        // GIVEN
        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        int[][] inputData = fileSudokuReader.read(inpPair);
        Sudoku sudoku = null;
        Sudoku resultSudoku0;
        Sudoku resultSudoku1;
        Sudoku resultSudoku2;
        Command command0;
        Command command1;
        Command command2;
        Cell resultCell0a;
        Cell resultCell1a;
        Cell resultCell2a;
        ManualInvoker invoker;

        try {
            sudoku = new Sudoku(inputData);
        } catch (IllegalSudokuStateException ex) {
            extAppLogFile.error("Test incorrect input");
        }

        // WHEN
        invoker = new ManualInvoker(sudoku);
        invoker.setStrategies(strategyFactory.createPointingPairsInCellStrategy());
        command0 = invoker.getNextState();
        resultSudoku0 = ((CommandPicker) command0).getSudoku();
        resultCell0a = resultSudoku0.getRows().get(0).getCell(7);
        command1 = invoker.getNextState();
        resultSudoku1 = ((CommandPicker) command1).getSudoku();
        resultCell1a = resultSudoku1.getRows().get(0).getCell(7);
        command2 = invoker.getPreviousState();
        resultSudoku2 = ((CommandPicker) command2).getSudoku();
        resultCell2a = resultSudoku2.getRows().get(0).getCell(7);

        // THEN
        assertFalse(resultCell0a.getCellPossibilities().contains(1));
        assertTrue(resultCell0a.getCellPossibilities().contains(3));

        assertFalse(resultCell1a.getCellPossibilities().contains(new Integer(1)));
        assertFalse(resultCell1a.getCellPossibilities().contains(new Integer(3)));

        assertFalse(resultCell2a.getCellPossibilities().contains(new Integer(1)));
        assertTrue(resultCell2a.getCellPossibilities().contains(new Integer(3)));
    }

    private static Stream<Arguments> inputStrategies() {
        StrategyFactory factory = new StrategyFactory();
        return Stream.of(Arguments.of(factory.createNakedSingleInACellStrategy()),
                Arguments.of(factory.createHiddenSingleInACellStrategy())
        );
    }
}

