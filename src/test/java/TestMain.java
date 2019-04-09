import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class TestMain {
    private static final int[] coordinates00 = {0,0};
    private static final boolean created00 = true;
    private static final int[] coordinates01 = {0,1};
    private static final boolean created01 = false;
    private static final int[] coordinates02 = {0,2};
    private static final boolean created02 = false;
    private static final int[] coordinates03 = {0,3};
    private static final boolean created03 = true;

    @Test
    public void createSudokuElementObjects() {

    }

//    @Test
//    @MethodSource("inputOutput")
//    public void shouldCreateSquare(int[] coordinates, boolean squareCreated) {
//        Main main = new Main();
//        boolean b = main.shouldCreateSquare(coordinates[0],coordinates[1]);
//        assertEquals(main.shouldCreateSquare(coordinates[0],coordinates[1]),squareCreated);
//    }

    private static Stream<Arguments> inputOutput() {
        return Stream.of(Arguments.of(TestMain.coordinates00, TestMain.created00),
                Arguments.of(TestMain.coordinates01, TestMain.created01),
                Arguments.of(TestMain.coordinates02, TestMain.created02),
                Arguments.of(TestMain.coordinates03, TestMain.created03)
        );
    }
}
