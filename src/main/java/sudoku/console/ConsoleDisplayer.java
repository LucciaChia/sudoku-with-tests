package sudoku.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Console;
import java.util.Scanner;

public class ConsoleDisplayer implements Displayer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleDisplayer.class);

    private static final Scanner scanner = new Scanner(System.in);
    private static final Console console = System.console();

    @Override
    public void display(String message) {

        if (console != null) {
            console.printf(message);
        } else {
            System.out.print(message);
        }
    }

    @Override
    public void displayLine(String message) {

        if (console != null) {
            console.printf(message);
        } else {
            System.out.println(message);
        }
    }

    @Override
    public int inputInt() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            LOGGER.debug("ConsoleDisplayer.inputInt: Type mismatch");
            scanner.next();
            return -1;
        }
    }

    @Override
    public String inputString() {
        return scanner.next();
    }
}
