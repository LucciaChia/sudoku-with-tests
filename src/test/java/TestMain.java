import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import sudoku.Main;
import sudoku.model.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestMain {
    private static final int[] coordinates00 = {0,0};
    private static final int[] coordinates03 = {0,3};
    private static final int[] coordinates06 = {0,6};
    private static final int[] coordinates30 = {3,0};
    private static final int[] coordinates33 = {3,3};
    private static final int[] coordinates36 = {3,6};
    private static final int[] coordinates60 = {6,0};
    private static final int[] coordinates63 = {6,3};
    private static final int[] coordinates66 = {6,6};

    private static final int[] coordinates01 = {0,1};
    private static final int[] coordinates12 = {1,2};
    private static final int[] coordinates24 = {2,4};
    private static final int[] coordinates37 = {3,7};
    private static final int[] coordinates45 = {4,5};
    private static final int[] coordinates58 = {5,8};
    private static final int[] coordinates61 = {6,1};
    private static final int[] coordinates72 = {7,2};
    private static final int[] coordinates83 = {8,3};

    static ClassLoader classLoader = new TestSolution().getClass().getClassLoader();

    private static final String inp1 = new File(classLoader.getResource("inputs/simple1.txt").getFile()).getPath();
    private static final String inp2 = new File(classLoader.getResource("inputs/simple2.txt").getFile()).getPath();
    private static final String inp3 = new File(classLoader.getResource("inputs/simple3.txt").getFile()).getPath();
    private static final String inp4 = new File(classLoader.getResource("inputs/simple4.txt").getFile()).getPath();
    private static final String inp5 = new File(classLoader.getResource("inputs/harder1.txt").getFile()).getPath();
    private static final String inp6 = new File(classLoader.getResource("inputs/harder2.txt").getFile()).getPath();
    private static final String inp7 = new File(classLoader.getResource("inputs/harder3.txt").getFile()).getPath();
    private static final String inp8 = new File(classLoader.getResource("inputs/harder4.txt").getFile()).getPath();

    @ParameterizedTest
    @MethodSource("linksToInputs")
    public void createSudokuElementObjects(String inputSudokuMatrixPath) {
        Main main = new Main();
        int[][] inputData = main.readSudokuMatrix(inputSudokuMatrixPath);
        boolean someObjectsNotCreated = false;
        ArrayList<List<? extends SudokuElement>> sudokuElementsList = main.createSudokuElementObjects(inputData);
        List<Horizontal> horizontals = (List<Horizontal>)sudokuElementsList.get(0);
        List<Vertical> verticals = (List<Vertical>)sudokuElementsList.get(1);
        List<Square> squares = (List<Square>)sudokuElementsList.get(2);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                try {
                    Cell horizontalCellCheck = horizontals.get(i).getCellInHorizontal(j);
                    Cell verticalCellCheck = verticals.get(j).getCellInVertical(i);
                    Cell squareCellCheck = squares.get((i / 3) * 3 + j / 3).getCells().get(j);
                } catch (NullPointerException ne) {
                    someObjectsNotCreated = true;
                }
            }
        }
        assertFalse(someObjectsNotCreated);
    }

    private static Stream<Arguments> linksToInputs() {
        return Stream.of(Arguments.of(TestMain.inp1),
                Arguments.of(TestMain.inp2),
                Arguments.of(TestMain.inp3),
                Arguments.of(TestMain.inp4),
                Arguments.of(TestMain.inp5),
                Arguments.of(TestMain.inp6),
                Arguments.of(TestMain.inp7),
                Arguments.of(TestMain.inp8)
        );
    }

    @Test
    public void shouldCreateSquareTrue() {
        Main main = new Main();
        final List<Integer> coordinates = Arrays.asList(0,0);
        assertTrue(main.shouldCreateSquare(coordinates.get(0),coordinates.get(1)));
    }

    @Test
    public void shouldCreateSquareFalse() {
        Main main = new Main();
        final List<Integer> coordinates = Arrays.asList(0,1);
        assertFalse(main.shouldCreateSquare(coordinates.get(0),coordinates.get(1)));
    }

    @ParameterizedTest
    @MethodSource("inputTrue")
    public void shouldCreateSquareTrueMultipleTests(int[] coordinates) {
        Main main = new Main();
        assertTrue(main.shouldCreateSquare(coordinates[0],coordinates[1]));
    }

    private static Stream<Arguments> inputTrue() {
        return Stream.of(Arguments.of(TestMain.coordinates00),
                Arguments.of(TestMain.coordinates03),
                Arguments.of(TestMain.coordinates06),
                Arguments.of(TestMain.coordinates30),
                Arguments.of(TestMain.coordinates33),
                Arguments.of(TestMain.coordinates36),
                Arguments.of(TestMain.coordinates60),
                Arguments.of(TestMain.coordinates63),
                Arguments.of(TestMain.coordinates66)
        );
    }

    @ParameterizedTest
    @MethodSource("inputFalse")
    public void shouldCreateSquareFalseMultipleTests(int[] coordinates) {
        Main main = new Main();
        assertFalse(main.shouldCreateSquare(coordinates[0],coordinates[1]));
    }

    private static Stream<Arguments> inputFalse() {
        return Stream.of(Arguments.of(TestMain.coordinates01),
                Arguments.of(TestMain.coordinates12),
                Arguments.of(TestMain.coordinates24),
                Arguments.of(TestMain.coordinates37),
                Arguments.of(TestMain.coordinates45),
                Arguments.of(TestMain.coordinates58),
                Arguments.of(TestMain.coordinates61),
                Arguments.of(TestMain.coordinates72),
                Arguments.of(TestMain.coordinates83)
        );
    }

    @Test
    public void helloMock() {
        Main main = new Main();
        Main mainSpy = Mockito.spy(main);

    }
}
