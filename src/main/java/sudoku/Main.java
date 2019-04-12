package sudoku;

import org.apache.log4j.Logger;
import sudoku.customExceptions.IllegalSudokuStateException;
import sudoku.model.Sudoku;
import sudoku.processing.FileSudokuReader;
import sudoku.processing.SudokuService;

import java.io.File;
import java.util.Scanner;
//import java.util.logging.Logger;

public class Main {

    // TODO zacat pouzivat logy namiesto System.out.println()
    // TODO okomentovat vsetky metody, ktore obsahuju nejaku logiku

    //private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static ClassLoader classLoader = new Main().getClass().getClassLoader();

    static final Logger LOGGER = Logger.getLogger(Main.class);
    static final Logger extAppLogFile = Logger.getLogger("ExternalAppLogger");

    public static final String path1 = new File(classLoader.getResource("inputs/simple1.txt").getFile()).getPath();
    public static final String path2 = new File(classLoader.getResource("inputs/simple2.txt").getFile()).getPath();
    public static final String path3 = new File(classLoader.getResource("inputs/simple3.txt").getFile()).getPath();
    public static final String path4 = new File(classLoader.getResource("inputs/simple4.txt").getFile()).getPath();

    public static void main(String[] args) {

        Main main = new Main();
        main.menu();
    }


    public void menu() {

        Scanner scanner = new Scanner(System.in);
        boolean quit = false;
        printHelp();
        extAppLogFile.info("Program has stared");
        do {
            System.out.println("Choose your option");
            int option = scanner.nextInt();
            switch (option) {
                case 0:
                    printHelp();
                    break;
                case 1:
                    try {
                        Sudoku sudokuMatrix = insertYourOwnSudoku();
                        SudokuService sudokuService = new SudokuService();
                        sudokuService.setHorizontals(sudokuMatrix.getHorizontals());
                        sudokuService.setVerticals(sudokuMatrix.getVerticals());
                        sudokuService.setSquares(sudokuMatrix.getSquares());

                        sudokuService.resolveSudokuService();
                        sudokuService.printSudokuMatrixService();
                        LOGGER.info("Reading sudoku - valid input - using log4j");
                    } catch (Exception e) {
//                        LOGGER.log(Level.WARNING, "Reading sudoku - incorrect input");
                        extAppLogFile.warn("Reading sudoku - incorrect input - using log4j");
                    }
                    break;
                case 2:
                    try {
                        runDefaultSudoku();
                        LOGGER.info("Default sudoku - valid input - using log4j");
                    } catch (IllegalSudokuStateException ex) {
//                        LOGGER.log(Level.WARNING, "Default sudoku - incorrect input");
                        extAppLogFile.warn("Reading sudoku - incorrect input - using log4j");
                    }

                    break;
                case 3:
                    System.out.println("Bye, bye");
                    extAppLogFile.info("Program has finished");
                    quit = true;
            }
        } while(!quit);
    }

    private void printHelp() {
        System.out.println(
                        "Insert 0 - to print this help\n" +
                        "Insert 1 - to insert your own sudoku and see solution\n" +
                        "         - empty places in sudoku reaplace with number 0\n" +
                        "Insert 2 - to see som example solutions of sudokus\n" +
                        "Insert 3 - to quit program"
        );
    }

    private Sudoku insertYourOwnSudoku() throws IllegalSudokuStateException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert your sudoku:");
        int[][] errorData = new int[0][0];
        int[][] data = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                data[i][j] = scanner.nextInt();
            }
        }
        // constructor creates objects and validates them
        Sudoku sudoku = new Sudoku(data);
        return sudoku;
    }

    private void runDefaultSudoku() throws IllegalSudokuStateException{

        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        int[][] data = fileSudokuReader.read(path1);

        Sudoku sudokuMatrix = new Sudoku(data);
        SudokuService sudokuService = new SudokuService();

        sudokuService.setHorizontals(sudokuMatrix.getHorizontals());
        sudokuService.setVerticals(sudokuMatrix.getVerticals());
        sudokuService.setSquares(sudokuMatrix.getSquares());

        sudokuService.printSudokuMatrixService();

        System.out.println("FINAL SOLUTION:");
        sudokuService.resolveSudokuService();
        sudokuService.printSudokuMatrixService();
    }

}
