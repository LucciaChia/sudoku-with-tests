package sudoku;

import org.apache.log4j.Logger;
import sudoku.customExceptions.IllegalSudokuStateException;
import sudoku.model.Sudoku;
import sudoku.processingUsingCommand.AutomatedInvoker;
import sudoku.processingUsingCommand.Command;
import sudoku.processingUsingStrategy.*;
import sudoku.readers.FileSudokuReader;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class MainCommandPattern {

    // TODO dopnit do logov, co este chyba
    // TODO okomentovat vsetky metody, ktore obsahuju nejaku logiku
    // TODO do refactoring, remove duplicate code

    private static ClassLoader classLoader = new Main().getClass().getClassLoader();

    private static final Logger extAppLogFile = Logger.getLogger("ExternalAppLogger");

    private static final String extremelySimple = new File(classLoader.getResource("inputs/NakedSingleInACell/extremelySimple.txt").getFile()).getPath();
    private static final String simple = new File(classLoader.getResource("inputs/simple1.txt").getFile()).getPath();
    private static final String harder = new File(classLoader.getResource("inputs/harder1.txt").getFile()).getPath();
    private static final String extremelyHardOnlyBacktrackUsed = new File(classLoader.getResource("outputs/extremelyHardTmp.txt").getFile()).getPath();
    private static final String extremelyHard = new File(classLoader.getResource("inputs/extremelyHard.txt").getFile()).getPath();
    private static final String insane = new File(classLoader.getResource("inputs/insaneSudoku.txt").getFile()).getPath();
    private static final String empty = new File(classLoader.getResource("inputs/emptySudoku.txt").getFile()).getPath();

    private Scanner scanner = new Scanner(System.in);

    private NakedSingleInACell nakedSingleInACell = new NakedSingleInACell();
    private HiddenSingleInACell hiddenSingleInACell = new HiddenSingleInACell();
    private PointingPairsInCell pointingPairsInCell = new PointingPairsInCell();
    private BacktrackLucia backtrackLucia = new BacktrackLucia();

    // TODO impose Command pattern into this application
    public static void main(String[] args) {
        MainCommandPattern mainCommandPattern = new MainCommandPattern();
        mainCommandPattern.menu();
    }

    private void menu() {

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
                        Sudoku sudoku = insertYourOwnSudoku();
                        automatedInvokerWithAllSolvingMethods(sudoku);
                        extAppLogFile.info("Reading sudoku - valid input - using log4j");
                    } catch (Exception e) {
                        extAppLogFile.warn("Reading sudoku - incorrect input - using log4j");
                    }
                    break;
                case 2:
                    try {
                        runDefaultSudokuAutomaticInvoker();
                        extAppLogFile.info("Default sudoku - valid input - using log4j");
                    } catch (IllegalSudokuStateException ex) {
                        extAppLogFile.warn("Reading sudoku - incorrect input - using log4j");
                    }

                    break;
                case 3:
                    try {
                        stepByStepSudokuAutomaticInvoker();
                        extAppLogFile.info("Default sudoku - valid input - using log4j");
                    } catch (IllegalSudokuStateException ex) {
                        extAppLogFile.warn("Reading sudoku - incorrect input - using log4j");
                    }

                    break;
                case 4:
                    System.out.println("Bye, bye");
                    extAppLogFile.info("Program has finished");
                    quit = true;
            }
        } while(!quit);
    }

    private void printHelp() {
        System.out.println(
                "Insert 0 - to print this help\n" +
                        "Insert 1 - to insert your own sudoku and see solution whole solution with all steps\n" +
                        "         - empty places in sudoku reaplace with number 0\n" +
                        "Insert 2 - to see some example solutions of sudokus with all steps\n" +
                        "Insert 3 - to insert your own sudoku and see solution solution STEP BY STEP\n" +
                        "         - empty places in sudoku reaplace with number 0\n" +
                        "Insert 4 - to quit program"
        );
    }
    // TODO pouzit mock test na simulaciu Scannera
    private Sudoku insertYourOwnSudoku() throws IllegalSudokuStateException {

        System.out.println("Insert your sudoku:");
        int[][] data = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                data[i][j] = scanner.nextInt();
            }
        }
        // constructor creates objects and validates them
        return new Sudoku(data);
    }

    private void runDefaultSudokuAutomaticInvoker() throws IllegalSudokuStateException{
        System.out.println("AutomaticInvoker Used");
        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        int[][] data = fileSudokuReader.read(extremelyHard);
        Sudoku sudoku = new Sudoku(data);

        printSudoku(sudoku);
        automatedInvokerWithAllSolvingMethods(sudoku);
    }

    // TODO Step by step NOT WORKING YET
    private void stepByStepSudokuAutomaticInvoker() throws IllegalSudokuStateException {
        System.out.println("There's nothing here yet");
//        boolean quit = false;
//        System.out.println("Insert your sudoku:");
//        Sudoku sudoku = insertYourOwnSudoku();
//        AutomatedInvoker automatedInvoker = new AutomatedInvoker(sudoku);
//        automatedInvoker.setStrategies(nakedSingleInACell, hiddenSingleInACell, pointingPairsInCell, backtrackLucia);
//        automatedInvoker.solvingStepsOrder();
//        List<Command> allCommands = automatedInvoker.getCommands();
//        int currentCommand = 0;
//        System.out.println(
//                "Insert N - to see NEXT step\n" +
//                "Insert P - to see PREVIOUS step\n" +
//                "Insert A - to see ALL steps\n" +
//                        "Insert E - to EXIT\n"
//        );
//        do {
//            if (currentCommand > allCommands.size()-1) {
//                quit = true;
//            }
//            System.out.println("Choose your option");
//            String option = scanner.nextLine();
//            switch (option) {
//                case "N":
//                    currentCommand++;
//                    Command commandNext = automatedInvoker.getNextState(currentCommand);
//                    System.out.println(commandNext.toString());
//                    break;
//                case "P":
//                    currentCommand--;
//                    Command commandPrevious = automatedInvoker.getPreviousState(currentCommand);
//                    System.out.println(commandPrevious.toString());
//                    break;
//                case "A":
//                    System.out.println("Not implemented yet");
//                    break;
//                case "E":
//                    System.out.println("Step by step has been ended");
//                    extAppLogFile.info("Step by step was left");
//                    quit = true;
//            }
//        } while(!quit);

    }

    private void printSudoku(Sudoku sudoku) {

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(sudoku.getRows().get(i).getCell(j).getActualValue() + " ");
            }
            System.out.println();
        }
    }

    private List<Command> automatedInvokerWithAllSolvingMethods(Sudoku sudoku) {
        AutomatedInvoker automatedInvoker = new AutomatedInvoker(sudoku);
        automatedInvoker.setStrategies(nakedSingleInACell, hiddenSingleInACell, pointingPairsInCell, backtrackLucia);
        automatedInvoker.solvingStepsOrder();
        System.out.println("Solution:");
        printSudoku(automatedInvoker.getSudoku());
        return automatedInvoker.getCommands();
    }
}
