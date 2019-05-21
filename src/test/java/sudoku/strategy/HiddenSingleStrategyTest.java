package sudoku.strategy;

import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sudoku.console.ConsoleDisplayer;
import sudoku.console.Displayer;
import sudoku.exceptions.IllegalSudokuStateException;
import sudoku.model.Cell;
import sudoku.model.Row;
import sudoku.model.StrategyType;
import sudoku.model.Sudoku;
import sudoku.readers.FileSudokuReader;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class HiddenSingleStrategyTest {
    private static ClassLoader classLoader = new HiddenSingleStrategyTest().getClass().getClassLoader();

    private static final Displayer consoleDisplayer = new ConsoleDisplayer();

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
                consoleDisplayer.displayLine(" N ");
                if (!nakedSingleStrategy.isUpdated()) {
                    hiddenSingleStrategy.resolveSudoku(sudoku);
                    consoleDisplayer.displayLine(" H ");
                }
            } while (nakedSingleStrategy.isUpdated() || hiddenSingleStrategy.isUpdated());
            endSudokuLevelType = sudoku.getSudokuLevelType();

            sudoku.printPossibilitiesInSudoku();
            sudoku.print();
            consoleDisplayer.displayLine("=================================");
            assertArrayEquals(expectedOutput, setArrayAccordingToObjectValues(sudoku));
            assertEquals(StrategyType.LOW, initialSudokuLevelType);
            assertEquals(StrategyType.LOW, endSudokuLevelType);
        } catch (IllegalSudokuStateException ex) {
            consoleDisplayer.displayLine("Test incorrect input");
        }
    }
    @Test
    void getName() {
        HiddenSingleStrategy hiddenSingleStrategy = new HiddenSingleStrategy();
        assertEquals("Hidden Single", hiddenSingleStrategy.getName());
    }

    @Test
    void getType() {
        HiddenSingleStrategy hiddenSingleStrategy = new HiddenSingleStrategy();
        assertEquals("LOW", hiddenSingleStrategy.getType().toString());
    }

    @Test
    void amountOfParticularPossibilities() {
        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        int[][] inputData = fileSudokuReader.read(inp3);
        boolean mapComparison = false;
        try {
            Sudoku sudoku = new Sudoku(inputData);
            sudoku.print();
//            sudoku.printPossibilitiesInSudoku();
            Row rowZero = sudoku.getRows().get(0);
            Map<Integer, Integer> rowZeroPossibilities = new HashMap<>();
            IntStream.range(0, 9).mapToObj(rowZero::getCell).filter(cell -> cell.getActualValue() == 0).map(Cell::getCellPossibilities).flatMap(Collection::stream).forEach(actualPossibility -> {
                if (!rowZeroPossibilities.containsKey(actualPossibility)) {
                    rowZeroPossibilities.put(actualPossibility, 1);
                } else {
                    int value = rowZeroPossibilities.get(actualPossibility) + 1;
                    rowZeroPossibilities.put(actualPossibility, value);
                }
            });

            HiddenSingleStrategy hiddenSingleStrategy = new HiddenSingleStrategy();
            Map<Integer, Integer> possiblities = hiddenSingleStrategy.amountOfParticularPossibilities(sudoku.getRows().get(0));
            consoleDisplayer.displayLine("possibility map:");
            printMap(possiblities);
            consoleDisplayer.displayLine("zero possibility map:");
            printMap(rowZeroPossibilities);
            mapComparison = possiblities.equals(rowZeroPossibilities);
        } catch (IllegalSudokuStateException ie) {
            consoleDisplayer.displayLine(ie.toString());
        }
        assertTrue(mapComparison);
    }
    //TODO
    @Test
    void deleteHidden() {

    }
    //TODO
    @Test
    void checkUniqueOccurence() {

    }

    private void printMap(Map<Integer, Integer> countOfPossibilities) {
        for (Integer possibility : countOfPossibilities.keySet()) {
            String key = possibility.toString();
            String value = countOfPossibilities.get(possibility).toString();
            consoleDisplayer.displayLine("key: " + key + " -> value: " + value);
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
}