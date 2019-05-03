package sudoku;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sudoku.customExceptions.IllegalSudokuStateException;
import sudoku.model.Sudoku;
import sudoku.processingUsingCommand.AutomatedInvoker;
import sudoku.processingUsingCommand.Command;
import sudoku.processingUsingStrategy.BacktrackLucia;
import sudoku.processingUsingStrategy.HiddenSingleInACell;
import sudoku.processingUsingStrategy.NakedSingleInACell;
import sudoku.processingUsingStrategy.PointingPairsInCell;
import sudoku.readers.FileSudokuReader;
import sudoku.stepHandlers.Step;

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
                        System.out.println("Insert your sudoku:");
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

        int[][] data = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                data[i][j] = scanner.nextInt();
            }
        }
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
        boolean quit = false;
        stepByStepPrintHelp();
        System.out.println("First insert your sudoku:");
        Sudoku sudoku = insertYourOwnSudoku();
        AutomatedInvoker automatedInvoker = new AutomatedInvoker(sudoku);
        automatedInvoker.setStrategies(nakedSingleInACell, hiddenSingleInACell, pointingPairsInCell, backtrackLucia);
        List<Step> allSteps = automatedInvoker.solvingStepsOrderLucia();

        int currentStep = 0;

        do {
            Step actualStep;
            System.out.println("Choose your option");
            String option = scanner.next();
            switch (option) {
                case "help":
                    stepByStepPrintHelp();
                    break;
                case "n":
                    currentStep++;
                    actualStep = automatedInvoker.getNextStep();
                    System.out.println(actualStep.toString());
                    break;
                case "p":
                    currentStep--;
                    actualStep = automatedInvoker.getPreviousStep();
                    System.out.println(actualStep.toString());
                    break;
                case "all":
                    automatedInvoker.printStepList();
                    break;
                case "end":
                    System.out.println("Step by step has been ended");
                    LOGGER.info("Step by step was left");
                    quit = true;
            }
        } while(!quit);

    }
    private void stepByStepPrintHelp() {
        System.out.println(
                "Insert help - to see NEXT step\n" +
                "Insert n - to see NEXT step\n" +
                        "Insert p - to see PREVIOUS step\n" +
                        "Insert all - to see ALL steps\n" +
                        "Insert end - to EXIT\n"
        );
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
        automatedInvoker.solvingStepsOrderLucia();
        automatedInvoker.printStepList();
        System.out.println("Solution:");
        printSudoku(automatedInvoker.getSudoku());
        return automatedInvoker.getCommands();
    }
}
