import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sudoku.Main;
import sudoku.model.*;
import sudoku.processing.Solution;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSolution {

    static ClassLoader classLoader = new TestSolution().getClass().getClassLoader();

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
    public void testOutput() {
        Main main = new Main();
        int[][] inputData = main.readSudokuMatrix(inp7);
        int[][] expectedOutput = main.readSudokuMatrix(out7);
        ArrayList<List<? extends SudokuElement>> sudokuElementsList = main.createSudokuElementObjects(inputData);
        List<Horizontal> horizontals = (List<Horizontal>)sudokuElementsList.get(0);
        List<Vertical> verticals = (List<Vertical>)sudokuElementsList.get(1);
        List<Square> squares = (List<Square>)sudokuElementsList.get(2);
        Solution solution = new Solution(verticals, horizontals, squares, inputData);
        solution.output();
        assertArrayEquals(expectedOutput, inputData);
    }

    @ParameterizedTest
//    @ValueSource(strings = { TestSolution.inp1, TestSolution.exp1 }) - nefungovala, lebo
//    viac parametrov znamena viacnasobny test, nie druhy parameter
//    chyba bola: ParameterResolutionException: No ParameterResolver registered for parameter
    @MethodSource("linksToInputs")
    public void testOutputParametrized(String inputSudokuMatrixPath, String expectedSudokuOutputPath) {
        Main main = new Main();
        int[][] inputData = main.readSudokuMatrix(inputSudokuMatrixPath);
        int[][] expectedOutput = main.readSudokuMatrix(expectedSudokuOutputPath);
        ArrayList<List<? extends SudokuElement>> sudokuElementsList = main.createSudokuElementObjects(inputData);
        List<Horizontal> horizontals = (List<Horizontal>)sudokuElementsList.get(0);
        List<Vertical> verticals = (List<Vertical>)sudokuElementsList.get(1);
        List<Square> squares = (List<Square>)sudokuElementsList.get(2);
        Solution solution = new Solution(verticals, horizontals, squares, inputData);
        solution.output();
        assertArrayEquals(expectedOutput, inputData);
    }

//    https://nirajsonawane.github.io/2018/12/30/Junit-5-Write-Powerful-Unit-Test-Cases-Using-Parameterized-Tests/
//    Method Source in Same class
//    Factory methods within the test class must be static.
//    Each factory method must generate a stream of arguments.
    private static Stream<Arguments> linksToInputs() {
        return Stream.of(Arguments.of(TestSolution.inp1, TestSolution.out1),
                         Arguments.of(TestSolution.inp2, TestSolution.out2),
                         Arguments.of(TestSolution.inp3, TestSolution.out3),
                         Arguments.of(TestSolution.inp4, TestSolution.out4),
                         Arguments.of(TestSolution.inp5, TestSolution.out5),
                         Arguments.of(TestSolution.inp6, TestSolution.out6),
                         Arguments.of(TestSolution.inp7, TestSolution.out7),
                         Arguments.of(TestSolution.inp8, TestSolution.out8)
        );
    }

    @Test
    public void findPartnerCell() {
        Possibility possibility03 = new Possibility(0,3, new ArrayList<Integer>(Arrays.asList(7,8)));
        Possibility possibility04 = new Possibility(0,4, new ArrayList<Integer>());
        Possibility possibility05 = new Possibility(0,5, new ArrayList<Integer>(Arrays.asList(7,8)));
        Possibility possibility13 = new Possibility(1,3, new ArrayList<Integer>(Arrays.asList(1,2,5,7,8)));
        Possibility possibility14 = new Possibility(1,4, new ArrayList<Integer>(Arrays.asList(1,2,5,7,8)));
        Possibility possibility15 = new Possibility(1,5, new ArrayList<Integer>());
        Possibility possibility23 = new Possibility(2,3, new ArrayList<Integer>(Arrays.asList(1,5,6,7)));
        Possibility possibility24 = new Possibility(2,4, new ArrayList<Integer>(Arrays.asList(1,5,6,7)));
        Possibility possibility25 = new Possibility(2,5, new ArrayList<Integer>());

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

//    @ParameterizedTest
//    // @ValueSource(strings = { "hi", "hello"}) --- ok
//    // @ValueSource(ints = { 1,2}) --- ok
//    @MethodSource("links")
//    public void pointingPairInCells() {
//
//    }
}
