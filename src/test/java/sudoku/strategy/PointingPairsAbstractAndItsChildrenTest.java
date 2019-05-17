package sudoku.strategy;

import org.junit.jupiter.api.Test;
import sudoku.command.AutomatedInvoker;
import sudoku.command.AutomatedInvokerTest;
import sudoku.exceptions.IllegalSudokuStateException;
import sudoku.exceptions.NoAvailableSolution;
import sudoku.model.StrategyType;
import sudoku.model.Sudoku;
import sudoku.readers.FileSudokuReader;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class PointingPairsAbstractAndItsChildrenTest {
    static ClassLoader classLoader = new AutomatedInvokerTest().getClass().getClassLoader();

    private static final String inp_ExtremelyHard1   = new File(classLoader.getResource("inputs/extremelyHard1.txt").getFile()).getPath();
    private static final String inp_ExtremelyHard2   = new File(classLoader.getResource("inputs/extremelyHard2.txt").getFile()).getPath();

    private static final String out_ExtremelyHard1   = new File(classLoader.getResource("outputs/extremelyHard1.txt").getFile()).getPath();
    private static final String out_ExtremelyHard2   = new File(classLoader.getResource("outputs/extremelyHard2.txt").getFile()).getPath();

    private StrategyFactory strategyFactory = new StrategyFactory();
    private Resolvable nakedSingleInACell = strategyFactory.createNakedSingleInACellStrategy();
    private Resolvable hiddenSingleInACell = strategyFactory.createHiddenSingleInACellStrategy();
    private Resolvable pointingPairsRowColumn = strategyFactory.createPointingPairsRowColumnStrategy();
    private Resolvable pointingPairsBox = strategyFactory.createPointingPairsBoxStrategy();
    private Resolvable backtrack = strategyFactory.createBacktrackStrategy();

    @Test
    public void reslveSudokuByRowColumnAndBoxClasses() {

        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        int[][] inputData = fileSudokuReader.read(inp_ExtremelyHard1);
        StrategyType initialSudokuLevelType = StrategyType.LOW;
        StrategyType endSudokuLevelType;
        Sudoku sudoku = null;
        AutomatedInvoker automatedInvoker = null;
        try {
            sudoku = new Sudoku(inputData);
            initialSudokuLevelType = sudoku.getSudokuLevelType();
            System.out.println("SUDOKU SOLVING STEPS:");
            sudoku.print();

            automatedInvoker = new AutomatedInvoker(sudoku, nakedSingleInACell, pointingPairsBox, pointingPairsRowColumn, hiddenSingleInACell, backtrack);

        } catch (NoAvailableSolution e) {
            System.out.println(e.toString());
        } catch (IllegalSudokuStateException ie) {
            System.out.println(ie.toString());
        }

        Sudoku solvedSudoku = automatedInvoker.getSudoku();
        endSudokuLevelType = solvedSudoku.getSudokuLevelType();
        int[][] actualSudokuValues = setArrayAccordingToObjectValues(solvedSudoku);
        int[][] expectedSudokuValues = fileSudokuReader.read(out_ExtremelyHard1);
        System.out.println(sudoku.toString());
        System.out.println(solvedSudoku.toString());
        assertArrayEquals(expectedSudokuValues, actualSudokuValues);
        assertEquals(StrategyType.LOW, initialSudokuLevelType);
        assertFalse(StrategyType.MEDIUM.ordinal() >  endSudokuLevelType.ordinal());

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
