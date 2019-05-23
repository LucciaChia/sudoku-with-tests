package sudoku.command;

import org.junit.jupiter.api.Test;
import sudoku.ANSIColour;
import sudoku.console.ConsoleDisplayer;
import sudoku.console.Displayer;
import sudoku.exceptions.IllegalSudokuStateException;
import sudoku.exceptions.NoAvailableSolutionException;
import sudoku.model.Sudoku;
import sudoku.readers.FileSudokuReader;
import sudoku.strategy.Resolvable;
import sudoku.strategy.StrategyFactory;

import java.io.File;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class AutomatedInvokerTest implements ANSIColour {
    private static ClassLoader classLoader = AutomatedInvokerTest.class.getClassLoader();

    private static final Displayer consoleDisplayer = new ConsoleDisplayer();

    private static final String out_extremelySimple1_step3 = new File(Objects.requireNonNull(classLoader.getResource("outputs/AutomatedInvoker/extremelySimple-AutomatedInv.txt")).getFile()).getPath();
    private static final String out_simple1_step5 = new File(Objects.requireNonNull(classLoader.getResource("outputs/AutomatedInvoker/simple1-AutomatedInv.txt")).getFile()).getPath();
    private static final String out_harder1_step12 = new File(Objects.requireNonNull(classLoader.getResource("outputs/AutomatedInvoker/harder1-AutomatedInv.txt")).getFile()).getPath();
    private static final String out_harder2_step12 = new File(Objects.requireNonNull(classLoader.getResource("outputs/AutomatedInvoker/harder2-AutomatedInv.txt")).getFile()).getPath();
    private static final String out_extremelyHard1_step30 = new File(Objects.requireNonNull(classLoader.getResource("outputs/AutomatedInvoker/extremelyHard1-AutomatedInv.txt")).getFile()).getPath();
    private static final String out_extremelyHard2_step15 = new File(Objects.requireNonNull(classLoader.getResource("outputs/AutomatedInvoker/extremelyHard2-AutomatedInv.txt")).getFile()).getPath();

    private static final String out_Previous_harder2_step15 = new File(Objects.requireNonNull(classLoader.getResource("outputs/AutomatedInvoker/stepsNextPrevious/harder2-Previous-step15-Hidden.txt")).getFile()).getPath();
    private static final String out_Next_harder2_step17 = new File(Objects.requireNonNull(classLoader.getResource("outputs/AutomatedInvoker/stepsNextPrevious/harder2-Next-step17-Nacked.txt")).getFile()).getPath();



    private static final String inp_ExtremelySimple = new File(Objects.requireNonNull(classLoader.getResource("inputs/NakedSingleInACell/extremelySimple.txt")).getFile()).getPath();
    private static final String inp_Simple1          = new File(Objects.requireNonNull(classLoader.getResource("inputs/simple1.txt")).getFile()).getPath();
    private static final String inp_Harder1          = new File(Objects.requireNonNull(classLoader.getResource("inputs/harder1.txt")).getFile()).getPath();
    private static final String inp_Harder2          = new File(Objects.requireNonNull(classLoader.getResource("inputs/harder2.txt")).getFile()).getPath();
    private static final String inp_ExtremelyHard1   = new File(Objects.requireNonNull(classLoader.getResource("inputs/extremelyHard1.txt")).getFile()).getPath();
    private static final String inp_ExtremelyHard2   = new File(Objects.requireNonNull(classLoader.getResource("inputs/extremelyHard2.txt")).getFile()).getPath();
    private static final String inp_NoSolution   = new File(Objects.requireNonNull(classLoader.getResource("inputs/noSolution.txt")).getFile()).getPath();

    private StrategyFactory strategyFactory = new StrategyFactory();
    private Resolvable nakedSingleInACell = strategyFactory.createNakedSingleInACellStrategy();
    private Resolvable hiddenSingleInACell = strategyFactory.createHiddenSingleInACellStrategy();
    private Resolvable pointingPairBox = strategyFactory.createPointingPairsBoxStrategy();
    private Resolvable pointingPairRowColumn = strategyFactory.createPointingPairsRowColumnStrategy();
    private Resolvable backtrack = strategyFactory.createBacktrackStrategy();

    @Test
    void solvingStepsOrderExtremelyHard1() {

        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        int[][] inputData = fileSudokuReader.read(inp_ExtremelyHard1);
        List<Command> commands = null;
        try {
            commands = useAutomatedInvokerFunctionality(inputData);
        } catch (NoAvailableSolutionException e) {
            fail();
            consoleDisplayer.displayLine(e.toString());
        }
        Sudoku sudokuStep = ((CommandPicker)commands.get(29)).getSudoku();
        int[][] actualSudokuValues = setArrayAccordingToObjectValues(sudokuStep);
        int[][] extremelyHard1_step30_array = fileSudokuReader.read(out_extremelyHard1_step30);
        // extremlyHard1 - step 30.
        assertArrayEquals(extremelyHard1_step30_array, actualSudokuValues);

    }

    @Test
    void solvingStepsOrderExtremelyHard2() {

        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        int[][] inputData = fileSudokuReader.read(inp_ExtremelyHard2);
        List<Command> commands = null;

        try {
            commands = useAutomatedInvokerFunctionality(inputData);
        } catch (NoAvailableSolutionException e) {
            consoleDisplayer.displayLine(e.toString());
        }

        assertNotNull(commands);
        Sudoku sudokuStep = ((CommandPicker)commands.get(14)).getSudoku();
        int[][] actualSudokuValues = setArrayAccordingToObjectValues(sudokuStep);
        int[][] extremelyHard2_step15_array = fileSudokuReader.read(out_extremelyHard2_step15);
        // extremlyHard2 - step 15.
        assertArrayEquals(extremelyHard2_step15_array, actualSudokuValues);

    }

    @Test
    void solvingStepsOrderHarder1() {

        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        int[][] inputData = fileSudokuReader.read(inp_Harder1);
        List<Command> commands = null;

        try {
            commands = useAutomatedInvokerFunctionality(inputData);
        } catch (NoAvailableSolutionException e) {
            consoleDisplayer.displayLine(e.toString());
        }

        assertNotNull(commands);
        Sudoku sudokuStep = ((CommandPicker)commands.get(11)).getSudoku();
        int[][] actualSudokuValues = setArrayAccordingToObjectValues(sudokuStep);
        int[][] hard_step12_array = fileSudokuReader.read(out_harder1_step12);
        // harder1 - step 12.
        assertArrayEquals(hard_step12_array, actualSudokuValues);

    }

    @Test
    void solvingStepsOrderHarder2() {

        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        int[][] inputData = fileSudokuReader.read(inp_Harder2);
        List<Command> commands = null;

        try {
            commands = useAutomatedInvokerFunctionality(inputData);
        } catch (NoAvailableSolutionException e) {
            consoleDisplayer.displayLine(e.toString());
        }

        assertNotNull(commands);
        Sudoku sudokuStep = ((CommandPicker)commands.get(11)).getSudoku();
        int[][] actualSudokuValues = setArrayAccordingToObjectValues(sudokuStep);
        int[][] harder2_step12_array = fileSudokuReader.read(out_harder2_step12);
        // harder2 - step 12.
        assertArrayEquals(harder2_step12_array, actualSudokuValues);

    }

    @Test
    void solvingStepsOrderSimple1() {

        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        int[][] inputData = fileSudokuReader.read(inp_Simple1);
        List<Command> commands = null;

        try {
            commands = useAutomatedInvokerFunctionality(inputData);
        } catch (NoAvailableSolutionException e) {
            consoleDisplayer.displayLine(e.toString());
        }

        assertNotNull(commands);
        Sudoku sudokuStep = ((CommandPicker)commands.get(4)).getSudoku();
        int[][] actualSudokuValues = setArrayAccordingToObjectValues(sudokuStep);
        int[][] simple1_step5_array = fileSudokuReader.read(out_simple1_step5);
        // simple1 - step 5.
        assertArrayEquals(simple1_step5_array, actualSudokuValues);

    }

    @Test
    void solvingStepsOrderExtremelySimple() {

        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        int[][] inputData = fileSudokuReader.read(inp_ExtremelySimple);
        List<Command> commands = null;

        try {
            commands = useAutomatedInvokerFunctionality(inputData);
        } catch (NoAvailableSolutionException e) {
            consoleDisplayer.displayLine(e.toString());
        }

        assertNotNull(commands);
        Sudoku sudokuStep = ((CommandPicker)commands.get(2)).getSudoku();
        int[][] actualSudokuValues = setArrayAccordingToObjectValues(sudokuStep);
        int[][] extremelySimple_step3_array = fileSudokuReader.read(out_extremelySimple1_step3);
        // extremlySimple - step 3.
        assertArrayEquals(extremelySimple_step3_array, actualSudokuValues);

    }

    @Test
    void getNextState() {

        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        int[][] inputData = fileSudokuReader.read(inp_Harder2);
        List<Command> commands;
        Sudoku sudoku;
        AutomatedInvoker automatedInvoker = null;
        try {

            sudoku = new Sudoku(inputData);
            consoleDisplayer.displayLine("SUDOKU SOLVING STEPS:");
            sudoku.print();
            try {
                automatedInvoker = new AutomatedInvoker(sudoku, nakedSingleInACell, hiddenSingleInACell, pointingPairBox, pointingPairRowColumn, backtrack);
            } catch (NoAvailableSolutionException e) {
                consoleDisplayer.displayLine(e.toString());
            }

            assertNotNull(automatedInvoker);
            commands = automatedInvoker.getCommands();
            int count = 1;
            for (Command command : commands) {
                consoleDisplayer.displayLine(String.valueOf(count++));
                String methodName = ((CommandPicker) command).getResolvable().getName();
                consoleDisplayer.displayLine(methodName);
                sudoku = ((CommandPicker) command).getSudoku();
                sudoku.print();

            }

        } catch (IllegalSudokuStateException ex) {
            consoleDisplayer.displayLine("Invalid sudoku");
        }

        assertNotNull(automatedInvoker);
        consoleDisplayer.displayLine("current step = " + automatedInvoker.getCurrentStep());
        automatedInvoker.setCurrentStep(16);
        Sudoku nextSudoku = ((CommandPicker)(automatedInvoker.getNextState())).getSudoku();

        int[][] actualSudokuValues = setArrayAccordingToObjectValues(nextSudoku);
        int[][] harder2_nextStep_17 = fileSudokuReader.read(out_Next_harder2_step17);
        assertArrayEquals(harder2_nextStep_17, actualSudokuValues);

    }

    @Test
    void getPreviousState() {

        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        int[][] inputData = fileSudokuReader.read(inp_Harder2);
        List<Command> commands;
        Sudoku sudoku;
        AutomatedInvoker automatedInvoker = null;
        try {

            sudoku = new Sudoku(inputData);
            consoleDisplayer.displayLine("SUDOKU SOLVING STEPS:");
            sudoku.print();
            try {
                automatedInvoker = new AutomatedInvoker(sudoku, nakedSingleInACell, hiddenSingleInACell, pointingPairBox, pointingPairRowColumn, backtrack);
            } catch (NoAvailableSolutionException e) {
                consoleDisplayer.displayLine(e.toString());
            }
            assertNotNull(automatedInvoker);
            commands = automatedInvoker.getCommands();
            int count = 1;
            for (Command command : commands) {
                consoleDisplayer.displayLine(String.valueOf(count++));
                String methodName = ((CommandPicker) command).getResolvable().getName();
                consoleDisplayer.displayLine(methodName);
                sudoku = ((CommandPicker) command).getSudoku();
                sudoku.print();

            }

        } catch (IllegalSudokuStateException ex) {
            consoleDisplayer.displayLine("Invalid sudoku");
        }

        assertNotNull(automatedInvoker);
        consoleDisplayer.displayLine("current step = " + automatedInvoker.getCurrentStep());
        automatedInvoker.setCurrentStep(15);
        Sudoku previousSudoku = ((CommandPicker)(automatedInvoker.getPreviousState())).getSudoku();

        int[][] actualSudokuValues = setArrayAccordingToObjectValues(previousSudoku);
        int[][] harder2_previousStep_15 = fileSudokuReader.read(out_Previous_harder2_step15);
        assertArrayEquals(harder2_previousStep_15, actualSudokuValues);

    }

    @Test
    void exceptionTest() {
        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        int[][] inputData = fileSudokuReader.read(inp_NoSolution);
        List<Command> commands;
        String exception = "";
        try {
            commands = useAutomatedInvokerFunctionality(inputData);
            int lastStep = commands.size()-1;
            Sudoku sudokuStep = ((CommandPicker)commands.get(lastStep)).getSudoku();
            setArrayAccordingToObjectValues(sudokuStep);
        } catch (NoAvailableSolutionException e) {
            exception = e.toString();
            consoleDisplayer.displayLine(e.toString());
        }


        assertEquals(ANSI_RED + "NoAvailableSolutionException  No available solution for this sudoku" + ANSI_RESET, exception);

    }

    private List<Command> useAutomatedInvokerFunctionality(int[][] inputData) throws NoAvailableSolutionException {
        Sudoku sudoku;
        AutomatedInvoker automatedInvoker = null;
        try {

            sudoku = new Sudoku(inputData);
            consoleDisplayer.displayLine("SUDOKU SOLVING STEPS:");
            sudoku.print();
            automatedInvoker = new AutomatedInvoker(sudoku, nakedSingleInACell, hiddenSingleInACell, pointingPairBox, pointingPairRowColumn, backtrack);


        } catch (IllegalSudokuStateException ex) {
            consoleDisplayer.displayLine("Invalid sudoku");
        }
        assertNotNull(automatedInvoker);
        return automatedInvoker.getCommands();
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
}
