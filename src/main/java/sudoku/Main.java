package sudoku;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sudoku.command.Command;
import sudoku.console.ConsoleDisplayer;
import sudoku.exceptions.IllegalSudokuStateException;
import sudoku.model.Sudoku;
import sudoku.readers.FileSudokuReader;
import sudoku.strategy.Resolvable;
import sudoku.strategy.StrategyFactory;

import java.io.File;
import java.util.List;
/*
 * simple scanner schema used in order the client could communicate with the program via console + step by step
 * principle enabled via second switch
 */
public class Main {

    // TODO dopnit do logov, co este chyba
    // TODO okomentovat vsetky metody, ktore obsahuju nejaku logiku
    // TODO do refactoring, remove duplicate code

    private static ClassLoader classLoader = new Main().getClass().getClassLoader();

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static final String extremelySimple = new File(classLoader.getResource("inputs/NakedSingleInACell/extremelySimple.txt").getFile()).getPath();
    private static final String simple = new File(classLoader.getResource("inputs/simple1.txt").getFile()).getPath();
    private static final String harder = new File(classLoader.getResource("inputs/harder1.txt").getFile()).getPath();
    private static final String extremelyHardOnlyBacktrackUsed = new File(classLoader.getResource("outputs/extremelyHardTmp.txt").getFile()).getPath();
    private static final String extremelyHard = new File(classLoader.getResource("inputs/extremelyHard.txt").getFile()).getPath();
    private static final String insane = new File(classLoader.getResource("inputs/insaneSudoku.txt").getFile()).getPath();
    private static final String empty = new File(classLoader.getResource("inputs/emptySudoku.txt").getFile()).getPath();

    private StrategyFactory strategyFactory = new StrategyFactory();
    private Resolvable nakedSingleInACell = strategyFactory.createNakedSingleInACellStrategy();
    private Resolvable hiddenSingleInACell = strategyFactory.createHiddenSingleInACellStrategy();
    private Resolvable pointingPairBox = strategyFactory.createPointingPairsBoxStrategy();
    private Resolvable pointingPairRowColumn = strategyFactory.createPointingPairsRowColumnStrategy();
    private Resolvable backtrackLucia = strategyFactory.createBacktrackStrategy();
    private ConsoleDisplayer consoleDisplayer = new ConsoleDisplayer();

    // TODO impose Command pattern into this application
    public static void main(String[] args) {
        Main main = new Main();
        main.menu();
    }

    private void menu() {

        boolean quit = false;
        printHelp();
        LOGGER.info("Program has stared");
        do {
            consoleDisplayer.display("Choose your option");
            int option = consoleDisplayer.inputInt();
            switch (option) {
                case 0:
                    printHelp();
                    break;
                case 1:
                    try {
                        consoleDisplayer.display("Insert your sudoku:");
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
                    consoleDisplayer.display("Bye, bye");
                    LOGGER.info("Program has finished");
                    quit = true;
            }
        } while(!quit);
    }

    private void printHelp() {
        consoleDisplayer.display(
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
                data[i][j] = consoleDisplayer.inputInt();
            }
        }
        return new Sudoku(data);
    }

    private void runDefaultSudokuAutomaticInvoker() throws IllegalSudokuStateException{
        consoleDisplayer.display("AutomaticInvoker Used");
        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        int[][] data = fileSudokuReader.read(extremelyHard);
        Sudoku sudoku = new Sudoku(data);

        printSudoku(sudoku);
        automatedInvokerWithAllSolvingMethods(sudoku);
    }

    // TODO Step by step NOT WORKING YET
    private void stepByStepSudokuAutomaticInvoker() throws IllegalSudokuStateException {
        consoleDisplayer.display("To be implemented");
    }

    private void stepByStepPrintHelp() {
        consoleDisplayer.display(
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
                consoleDisplayer.display(sudoku.getRows().get(i).getCell(j).getActualValue() + " ");
            }
            consoleDisplayer.display("");
        }
    }

    private List<Command> automatedInvokerWithAllSolvingMethods(Sudoku sudoku) {
        return null;
    }
}
