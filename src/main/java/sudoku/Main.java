package sudoku;

import sudoku.processing.FileSudokuReader;
import sudoku.processing.SudokuService;

import java.io.File;
import java.util.Scanner;

public class Main {

    // TODO zacat pouzivat logy namiesto System.out.println()
    // TODO okomentovat vsetky metody, ktore obsahuju nejaku logiku
    // TODO vsetko ostatne okrem menu() musi ist prec z Main()

    static ClassLoader classLoader = new Main().getClass().getClassLoader();

    public static final String path1 = new File(classLoader.getResource("inputs/simple1.txt").getFile()).getPath();
    public static final String path2 = new File(classLoader.getResource("inputs/simple2.txt").getFile()).getPath();
    public static final String path3 = new File(classLoader.getResource("inputs/simple3.txt").getFile()).getPath();
    public static final String path4 = new File(classLoader.getResource("inputs/simple4.txt").getFile()).getPath();

    public static void main(String[] args) {

        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        SudokuService sudokuService = new SudokuService();
        int[][] data = fileSudokuReader.read(path1);

        sudokuService.printSudokuMatrixService(data);

        System.out.println("FINAL SOLUTION:");
        sudokuService.createSudokuElementObjectsService(data);
        sudokuService.resolveSudokuService(data);

        sudokuService.printSudokuMatrixService(data);
    }


// TODO dat do main a dokoncit
    public void menu() {

        Scanner scanner = new Scanner(System.in);
        boolean quit = false;
        do {
            int option = scanner.nextInt();
            switch (option) {
                case 0:
                    printHelp();
                    break;
                case 1:
                    insertYourOwnSudoku();
                    break;
                case 2:
                    runDefaultSudoku();
                    break;
                case 3:
                    quit = true;
            }
        } while(!quit);
    }

    private void printHelp() {
        System.out.println(
                        "Insert 0 - to print this help\n" +
                        "Insert 1 - to be able to write your own sudoku to check\n" +
                        "         - empty places in sudoku reaplace with number 0\n" +
                        "Insert 2 - to see som example solutions of sudokus\n" +
                        "Insert 3 - to quit program"
        );
    }

    private void insertYourOwnSudoku() {

    }

    private void runDefaultSudoku() {

    }

}
