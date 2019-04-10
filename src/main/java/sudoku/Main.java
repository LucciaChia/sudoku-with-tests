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

//        FileSudokuReader fileSudokuReader = new FileSudokuReader();
//        SudokuService sudokuService = new SudokuService();
//        int[][] data = fileSudokuReader.read(path1);
//
//        sudokuService.printSudokuMatrixService(data);
//
//        System.out.println("FINAL SOLUTION:");
//        sudokuService.createSudokuElementObjectsService(data);
//        sudokuService.resolveSudokuService(data);
//
//        sudokuService.printSudokuMatrixService(data);

        Main main = new Main();
        main.menu();
    }


// TODO dat do main a dokoncit
    public void menu() {

        Scanner scanner = new Scanner(System.in);
        boolean quit = false;
        printHelp();

        do {
            int option = scanner.nextInt();
            switch (option) {
                case 0:
                    printHelp();
                    break;
                case 1:
                    int[][] data = insertYourOwnSudoku();;
                    SudokuService sudokuService = new SudokuService();
                    System.out.println();
                    sudokuService.printSudokuMatrixService(data);
                    sudokuService.createSudokuElementObjectsService(data);
                    sudokuService.resolveSudokuService(data);
                    sudokuService.printSudokuMatrixService(data);
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

    private int[][] insertYourOwnSudoku() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert your sudoku:");
        int[][] errorData = {{0,0}};
        int[][] data = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                data[i][j] = scanner.nextInt();
                if (data[i][j] < 0 || data[i][j] > 9) {
                    System.out.println("Invalid number");
                    return errorData;
                }
                scanner.nextLine();
            }
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(data[i][j] + " ");
            }
            System.out.println();
        }
        return data;
    }

    private void runDefaultSudoku() {
        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        SudokuService sudokuService = new SudokuService();
        int[][] data = fileSudokuReader.read(path1);

        sudokuService.printSudokuMatrixService(data);

        System.out.println("FINAL SOLUTION:");
        sudokuService.createSudokuElementObjectsService(data);
        sudokuService.resolveSudokuService(data);

        sudokuService.printSudokuMatrixService(data);
    }

}
