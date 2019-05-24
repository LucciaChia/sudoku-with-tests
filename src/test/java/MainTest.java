import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sudoku.exceptions.IllegalSudokuStateException;
import sudoku.exceptions.NoAvailableSolutionException;
import sudoku.model.Sudoku;
import sudoku.readers.FileSudokuReader;
import sudoku.strategy.*;

import java.io.File;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class MainTest {

    static ClassLoader classLoader = new MainTest().getClass().getClassLoader();

    private static final String inp1 = new File(classLoader.getResource("inputs/simple1.txt").getFile()).getPath();
    private static final String inp2 = new File(classLoader.getResource("inputs/simple2.txt").getFile()).getPath();
    private static final String inp3 = new File(classLoader.getResource("inputs/simple3.txt").getFile()).getPath();
    private static final String inp4 = new File(classLoader.getResource("inputs/simple4.txt").getFile()).getPath();
    private static final String inp5 = new File(classLoader.getResource("inputs/harder1.txt").getFile()).getPath();
    private static final String inp6 = new File(classLoader.getResource("inputs/harder2.txt").getFile()).getPath();
    private static final String inp7 = new File(classLoader.getResource("inputs/harder3.txt").getFile()).getPath();
    private static final String inp8 = new File(classLoader.getResource("inputs/harder4.txt").getFile()).getPath();
    private static final String inp9 = new File(classLoader.getResource("inputs/extremelyHard.txt").getFile()).getPath();

    private static final String out1 = new File(classLoader.getResource("outputs/simple1.txt").getFile()).getPath();
    private static final String out2 = new File(classLoader.getResource("outputs/simple2.txt").getFile()).getPath();
    private static final String out3 = new File(classLoader.getResource("outputs/simple3.txt").getFile()).getPath();
    private static final String out4 = new File(classLoader.getResource("outputs/simple4.txt").getFile()).getPath();
    private static final String out5 = new File(classLoader.getResource("outputs/harder1.txt").getFile()).getPath();
    private static final String out6 = new File(classLoader.getResource("outputs/harder2.txt").getFile()).getPath();
    private static final String out7 = new File(classLoader.getResource("outputs/harder3.txt").getFile()).getPath();
    private static final String out8 = new File(classLoader.getResource("outputs/harder4.txt").getFile()).getPath();
    private static final String out9 = new File(classLoader.getResource("outputs/extremelyHardTmp.txt").getFile()).getPath();

    private StrategyFactory strategyFactory = new StrategyFactory();
    private Resolvable nakedSingleInACell = strategyFactory.createNakedSingleInACellStrategy();
    private Resolvable hiddenSingleInACell = strategyFactory.createHiddenSingleInACellStrategy();
    private Resolvable pointingPairBox = strategyFactory.createPointingPairsBoxStrategy();
    private Resolvable pointingPairRowColumn = strategyFactory.createPointingPairsRowColumnStrategy();


    @ParameterizedTest
//    @ValueSource(strings = { SolutionTest.inp1, SolutionTest.exp1 }) - nefungovala, lebo
//    viac parametrov znamena viacnasobny test, nie druhy parameter
//    chyba bola: ParameterResolutionException: No ParameterResolver registered for parameter
    @MethodSource("linksToInputs")
    public void menu(String inputSudokuMatrixPath, String expectedSudokuOutputPath) {
        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        int[][] inputData = fileSudokuReader.read(inputSudokuMatrixPath);
        int[][] expectedOutput = fileSudokuReader.read(expectedSudokuOutputPath);


        try {

            Sudoku sudoku = new Sudoku(inputData);
            Solver solver = new Solver();
            solver.setStrategies(nakedSingleInACell, hiddenSingleInACell, pointingPairBox, pointingPairRowColumn);
            solver.useStrategies(sudoku);

            assertArrayEquals(expectedOutput, setArrayAccordingToObjectValues(sudoku));
        } catch (IllegalSudokuStateException ex) {
            ex.toString();
        } catch (NoAvailableSolutionException ne) {
            ne.toString();
        }
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

    //    https://nirajsonawane.github.io/2018/12/30/Junit-5-Write-Powerful-Unit-Test-Cases-Using-Parameterized-Tests/
//    Method Source in Same class
//    Factory methods within the test class must be static.
//    Each factory method must generate a stream of arguments.
    private static Stream<Arguments> linksToInputs() {
        return Stream.of(Arguments.of(MainTest.inp1, MainTest.out1),
                Arguments.of(MainTest.inp2, MainTest.out2),
                Arguments.of(MainTest.inp3, MainTest.out3),
                Arguments.of(MainTest.inp4, MainTest.out4),
                Arguments.of(MainTest.inp5, MainTest.out5),
                Arguments.of(MainTest.inp6, MainTest.out6),
                Arguments.of(MainTest.inp7, MainTest.out7),
                Arguments.of(MainTest.inp8, MainTest.out8),
                Arguments.of(MainTest.inp9, MainTest.out9)
        );
    }
}