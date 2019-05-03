package sudoku;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sudoku.exceptions.IllegalSudokuStateException;
import sudoku.model.Sudoku;
import sudoku.processingUsingCommand.AutomatedInvoker;
import sudoku.processingUsingCommand.Command;
import sudoku.processingUsingStrategy.BacktrackLucia;
import sudoku.processingUsingStrategy.HiddenSingleInACell;
import sudoku.processingUsingStrategy.NakedSingleInACell;
import sudoku.processingUsingStrategy.PointingPairsInCell;
import sudoku.readers.FileSudokuReader;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class MainCommandPattern {

    // TODO dopnit do logov, co este chyba
    // TODO okomentovat vsetky metody, ktore obsahuju nejaku logiku
    // TODO do refactoring, remove duplicate code

    private static ClassLoader classLoader = new Main().getClass().getClassLoader();

    private static final Logger LOGGER = LoggerFactory.getLogger(MainCommandPattern.class);

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
        LOGGER.info("Program has stared");
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
                        LOGGER.info("Reading sudoku - valid input");
                    } catch (Exception e) {
                        LOGGER.warn("Reading sudoku - incorrect input");
                    }
                    break;
                case 2:
                    try {
                        runDefaultSudokuAutomaticInvoker();
                        LOGGER.info("Default sudoku - valid input");
                    } catch (IllegalSudokuStateException ex) {
                        LOGGER.warn("Reading sudoku - incorrect input");
                    }

                    break;
                case 3:
                    try {
                        stepByStepSudokuAutomaticInvoker();
                        LOGGER.info("Default sudoku - valid input");
                    } catch (IllegalSudokuStateException ex) {
                        LOGGER.warn("Reading sudoku - incorrect input");
                    }

                    break;
                case 4:
                    System.out.println("Bye, bye");
                    LOGGER.info("Program has finished");
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
        int[][] data = fileSudokuReader.read(harder);
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
//                    LOGGER.info("Step by step was left");
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
