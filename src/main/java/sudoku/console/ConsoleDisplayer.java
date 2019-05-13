package sudoku.console;

import java.util.Scanner;

public class ConsoleDisplayer implements Displayer {
    private Scanner scanner = new Scanner(System.in);

    @Override
    public void display(String message) {
        System.out.println(message);
    }

    @Override
    public int inputInt() {
        return scanner.nextInt();
    }
}
