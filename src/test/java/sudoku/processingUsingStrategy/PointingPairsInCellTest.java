package sudoku.processingUsingStrategy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sudoku.customExceptions.IllegalSudokuStateException;
import sudoku.model.Sudoku;
import sudoku.readers.FileSudokuReader;

import java.io.File;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PointingPairsInCellTest {
    static ClassLoader classLoader = new PointingPairsInCellTest().getClass().getClassLoader();

    private static final String inp1 = new File(classLoader.getResource("inputs/NakedSingleInACell/extremelySimple.txt").getFile()).getPath();
    private static final String out1 = new File(classLoader.getResource("outputs/NakedSingleInACell/extremelySimple.txt").getFile()).getPath();

    private static final String inp2 = new File(classLoader.getResource("inputs/simple1.txt").getFile()).getPath();
    private static final String out2 = new File(classLoader.getResource("outputs/simple1.txt").getFile()).getPath();

    private static final String inp3 = new File(classLoader.getResource("inputs/harder1.txt").getFile()).getPath();
    private static final String out3 = new File(classLoader.getResource("outputs/harder1.txt").getFile()).getPath();

    @ParameterizedTest
    @MethodSource("linksToInputs")
    void resolveSudoku(String inputSudokuMatrixPath, String expectedSudokuOutputPath) {
        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        int[][] inputData = fileSudokuReader.read(inputSudokuMatrixPath);
        int[][] expectedOutput = fileSudokuReader.read(expectedSudokuOutputPath);

        try {
            Sudoku sudoku = new Sudoku(inputData);
            NakedSingleInACell nakedSingleInACell = new NakedSingleInACell();
            HiddenSingleInACell hiddenSingleInACell = new HiddenSingleInACell();
            PointingPairsInCell pointingPairsInCell = new PointingPairsInCell();
            do {
                nakedSingleInACell.resolveSudoku(sudoku);
                System.out.println(" N ");
                if (!Solver.sudokuWasChanged) {
                    hiddenSingleInACell.resolveSudoku(sudoku);
                    System.out.println(" H ");
                }

                if (!Solver.sudokuWasChanged) {
                    pointingPairsInCell.resolveSudoku(sudoku);
                    System.out.println(" P ");
                }
            } while (Solver.sudokuWasChanged);



            printPoss(sudoku);
            System.out.println("=================================");
            assertArrayEquals(expectedOutput, setArrayAccordingToObjectValues(sudoku));
        } catch (IllegalSudokuStateException ex) {
            System.out.println("Test incorrect input");
        }
    }

    @Test
    void findPartnerCell() {
    }

    private static Stream<Arguments> linksToInputs() {
        return Stream.of(Arguments.of(PointingPairsInCellTest.inp1, PointingPairsInCellTest.out1),
                Arguments.of(PointingPairsInCellTest.inp2, PointingPairsInCellTest.out2),
                Arguments.of(PointingPairsInCellTest.inp3, PointingPairsInCellTest.out3)
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
                System.out.println(sudoku.getRows().get(i).getCell(j).toString());
            }
            System.out.println("*");
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(sudoku.getRows().get(i).getCell(j).getActualValue() + " ");
            }
            System.out.println();
        }
    }
}