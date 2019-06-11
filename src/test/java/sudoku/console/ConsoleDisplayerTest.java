package sudoku.console;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.Console;
import java.util.InputMismatchException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Scanner.class, Console.class})
class ConsoleDisplayerTest {

    @Mock
    private Console consoleMock;

    @Mock
    private Scanner scannerMock;

    private ConsoleDisplayer consoleDisplayer;


    @BeforeEach
    void setup() {
        consoleMock = mock(Console.class);
        scannerMock = mock(Scanner.class);
        consoleDisplayer = new ConsoleDisplayer(scannerMock, consoleMock);
    }

    @Test
    void display() {

        try {
        consoleDisplayer.display("five");
            verify(consoleMock, times(1)).printf("five");
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void displayLine() {
        consoleDisplayer.displayLine("five");
        verify(consoleMock, times(1)).printf("five");
    }

    @Test
    void inputInt() {
        doReturn(5).when(scannerMock).nextInt();
        assertEquals(5, consoleDisplayer.inputInt());
    }

    @Test
    void inputIntException() {
        final int[] result = new int[1];
        doThrow(InputMismatchException.class).when(scannerMock).nextInt();
        assertEquals(-1, consoleDisplayer.inputInt());
    }

    @Test
    void inputString() {
        doReturn("five").when(scannerMock).next();
        assertEquals("five", consoleDisplayer.inputString());
    }
}