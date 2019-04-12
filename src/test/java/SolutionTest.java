import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sudoku.customExceptions.IllegalSudokuStateException;
import sudoku.model.*;
import sudoku.processing.FileSudokuReader;
import sudoku.processing.Solution;
import sudoku.processing.SudokuService;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SolutionTest {

    static ClassLoader classLoader = new SolutionTest().getClass().getClassLoader();

    private static final String inp1 = new File(classLoader.getResource("inputs/simple1.txt").getFile()).getPath();
    private static final String inp2 = new File(classLoader.getResource("inputs/simple2.txt").getFile()).getPath();
    private static final String inp3 = new File(classLoader.getResource("inputs/simple3.txt").getFile()).getPath();
    private static final String inp4 = new File(classLoader.getResource("inputs/simple4.txt").getFile()).getPath();
    private static final String inp5 = new File(classLoader.getResource("inputs/harder1.txt").getFile()).getPath();
    private static final String inp6 = new File(classLoader.getResource("inputs/harder2.txt").getFile()).getPath();
    private static final String inp7 = new File(classLoader.getResource("inputs/harder3.txt").getFile()).getPath();
    private static final String inp8 = new File(classLoader.getResource("inputs/harder4.txt").getFile()).getPath();

    private static final String out1 = new File(classLoader.getResource("outputs/simple1.txt").getFile()).getPath();
    private static final String out2 = new File(classLoader.getResource("outputs/simple2.txt").getFile()).getPath();
    private static final String out3 = new File(classLoader.getResource("outputs/simple3.txt").getFile()).getPath();
    private static final String out4 = new File(classLoader.getResource("outputs/simple4.txt").getFile()).getPath();
    private static final String out5 = new File(classLoader.getResource("outputs/harder1.txt").getFile()).getPath();
    private static final String out6 = new File(classLoader.getResource("outputs/harder2.txt").getFile()).getPath();
    private static final String out7 = new File(classLoader.getResource("outputs/harder3.txt").getFile()).getPath();
    private static final String out8 = new File(classLoader.getResource("outputs/harder4.txt").getFile()).getPath();
    private int[][] sudokuMatrix = {
            {0, 5, 0, 0, 4, 0, 1, 3, 0},
            {0, 4, 0, 0, 0, 9, 0, 6, 8},
            {2, 0, 8, 0, 0, 3, 0, 0, 4},
            {0, 0, 0, 0, 0, 0, 2, 8, 0},
            {8, 0, 0, 0, 0, 0, 0, 0, 1},
            {0, 3, 5, 0, 0, 0, 0, 0, 0},
            {5, 0, 0, 4, 0, 0, 9, 0, 7},
            {7, 6, 0, 9, 0, 0, 0, 1, 0},
            {0, 2, 9, 0, 8, 0, 0, 5, 0}
    };

    @Test
    public void testOutputWithoutArray() {
        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        int[][] inputData = fileSudokuReader.read(inp7);
        int[][] expectedOutput = fileSudokuReader.read(out7);


        try {
            Sudoku sudokuMatrix = new Sudoku(inputData);
            SudokuService sudokuService = new SudokuService(sudokuMatrix);
            sudokuService.resolveSudokuService();
            assertArrayEquals(expectedOutput, sudokuService.printSudokuMatrixService());
        } catch (IllegalSudokuStateException ex) {
            System.out.println("Test incorrect input");
        }

    }

    @ParameterizedTest
//    @ValueSource(strings = { SolutionTest.inp1, SolutionTest.exp1 }) - nefungovala, lebo
//    viac parametrov znamena viacnasobny test, nie druhy parameter
//    chyba bola: ParameterResolutionException: No ParameterResolver registered for parameter
    @MethodSource("linksToInputs")
    public void testOutputWithoutArrayParametrized(String inputSudokuMatrixPath, String expectedSudokuOutputPath) {
        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        int[][] inputData = fileSudokuReader.read(inputSudokuMatrixPath);
        int[][] expectedOutput = fileSudokuReader.read(expectedSudokuOutputPath);


        try {
            Sudoku sudokuMatrix = new Sudoku(inputData);
            SudokuService sudokuService = new SudokuService(sudokuMatrix);
            sudokuService.resolveSudokuService();
            assertArrayEquals(expectedOutput, sudokuService.printSudokuMatrixService());
        } catch (IllegalSudokuStateException ex) {
            System.out.println("Test incorrect input");
        }


    }

//    https://nirajsonawane.github.io/2018/12/30/Junit-5-Write-Powerful-Unit-Test-Cases-Using-Parameterized-Tests/
//    Method Source in Same class
//    Factory methods within the test class must be static.
//    Each factory method must generate a stream of arguments.
    private static Stream<Arguments> linksToInputs() {
        return Stream.of(Arguments.of(SolutionTest.inp1, SolutionTest.out1),
                         Arguments.of(SolutionTest.inp2, SolutionTest.out2),
                         Arguments.of(SolutionTest.inp3, SolutionTest.out3),
                         Arguments.of(SolutionTest.inp4, SolutionTest.out4),
                         Arguments.of(SolutionTest.inp5, SolutionTest.out5),
                         Arguments.of(SolutionTest.inp6, SolutionTest.out6),
                         Arguments.of(SolutionTest.inp7, SolutionTest.out7),
                         Arguments.of(SolutionTest.inp8, SolutionTest.out8)
        );
    }

    @Test
    public void findPartnerCell() {
        List<Integer> possibility03 = new ArrayList<Integer>(Arrays.asList(7,8));
        List<Integer> possibility04 = new ArrayList<Integer>();
        List<Integer> possibility05 = new ArrayList<Integer>(Arrays.asList(7,8));
        List<Integer> possibility13 = new ArrayList<Integer>(Arrays.asList(1,2,5,7,8));
        List<Integer> possibility14 = new ArrayList<Integer>(Arrays.asList(1,2,5,7,8));
        List<Integer> possibility15 = new ArrayList<Integer>();
        List<Integer> possibility23 = new ArrayList<Integer>(Arrays.asList(1,5,6,7));
        List<Integer> possibility24 = new ArrayList<Integer>(Arrays.asList(1,5,6,7));
        List<Integer> possibility25 = new ArrayList<Integer>();

        Cell c03 = new Cell(0,0,3);
        c03.setCellPossibilities(possibility03);
        Cell c04 = new Cell(4,0,4);
        c04.setCellPossibilities(possibility04);
        Cell c05 = new Cell(0,0,5);
        c05.setCellPossibilities(possibility05);

        Cell c13 = new Cell(0,1,3);
        c13.setCellPossibilities(possibility13);
        Cell c14 = new Cell(0,1,4);
        c14.setCellPossibilities(possibility14);
        Cell c15 = new Cell(9,1,5);
        c15.setCellPossibilities(possibility15);

        Cell c23 = new Cell(0,2,3);
        c23.setCellPossibilities(possibility23);
        Cell c24 = new Cell(0,2,4);
        c24.setCellPossibilities(possibility24);
        Cell c25 = new Cell(3,2,5);
        c25.setCellPossibilities(possibility25);

        ArrayList<Cell> cellsInSquare = new ArrayList<>(Arrays.asList(c03,c04,c05,c13,c14,c15,c23,c24,c25));

        Square square = new Square(cellsInSquare);
        List<Square> squareList = new ArrayList<>();
        squareList.add(new Square());
        squareList.add(square);
        Solution solution = new Solution(squareList);
        List<Cell> testedEligiblePartnerCells = solution.findPartnerCell(c03,7);

        // correct answer:
        List<Cell> eligiblePartnerCells = new ArrayList<>();
        eligiblePartnerCells.add(c05);
        eligiblePartnerCells.add(c13);
        eligiblePartnerCells.add(c23);

        assertEquals(eligiblePartnerCells,testedEligiblePartnerCells);
    }
}
