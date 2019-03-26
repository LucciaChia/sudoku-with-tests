import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sudoku.Main;
import sudoku.model.Horizontal;
import sudoku.model.Square;
import sudoku.model.SudokuElement;
import sudoku.model.Vertical;
import sudoku.processing.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
public class TestSolution {

    // paths to resources
    // inputs
    public static final String inp1 = "C:\\Users\\Lucia\\IdeaProjects\\sudoku-with-tests\\src\\main\\\\resources\\inputs\\simple1.txt";
    public static final String inp2 = "C:\\Users\\Lucia\\IdeaProjects\\sudoku-with-tests\\src\\main\\\\resources\\inputs\\simple2.txt";
    public static final String inp3 = "C:\\Users\\Lucia\\IdeaProjects\\sudoku-with-tests\\src\\main\\\\resources\\inputs\\simple3.txt";
    public static final String inp4 = "C:\\Users\\Lucia\\IdeaProjects\\sudoku-with-tests\\src\\main\\\\resources\\inputs\\simple4.txt";

    // expected outputs
    public final static String exp1 = "C:\\Users\\Lucia\\IdeaProjects\\sudoku-with-tests\\src\\test\\java\\expectedSolutions\\simple\\exp1.txt";
    public final static String exp2 = "C:\\Users\\Lucia\\IdeaProjects\\sudoku-with-tests\\src\\test\\java\\expectedSolutions\\simple\\exp2.txt";
    public final static String exp3 = "C:\\Users\\Lucia\\IdeaProjects\\sudoku-with-tests\\src\\test\\java\\expectedSolutions\\simple\\exp3.txt";
    public final static String exp4 = "C:\\Users\\Lucia\\IdeaProjects\\sudoku-with-tests\\src\\test\\java\\expectedSolutions\\simple\\exp4.txt";

    @Test
    public void testOutput() {
        Main main = new Main();
        int[][] inputData = main.readSudokuMatrix(inp4);
        int[][] expectedOutput = main.readSudokuMatrix(exp4);
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
        return Stream.of(Arguments.of(TestSolution.inp1, TestSolution.exp1),
                         Arguments.of(TestSolution.inp2, TestSolution.exp2),
                         Arguments.of(TestSolution.inp3, TestSolution.exp3),
                         Arguments.of(TestSolution.inp4, TestSolution.exp4)
        );
    }
}
