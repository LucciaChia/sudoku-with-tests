package sudoku.console;

import java.io.Console;
import java.util.Scanner;

public class ConsoleDisplayer implements Displayer {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Console console = System.console();

    @Override
    public void display(String message) {
        console.printf(message);
    }

    @Override
    public int inputInt() {
        return scanner.nextInt();
    }
}
