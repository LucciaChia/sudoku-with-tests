package sudoku;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sudoku.command.AutomatedInvoker;
import sudoku.command.Command;
import sudoku.command.CommandPicker;
import sudoku.console.ConsoleDisplayer;
import sudoku.exceptions.IllegalSudokuStateException;
import sudoku.exceptions.NoAvailableSolution;
import sudoku.model.Sudoku;
import sudoku.readers.FileSudokuReader;
import sudoku.strategy.Resolvable;
import sudoku.strategy.StrategyFactory;

import java.io.File;
import java.util.LinkedList;
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

    private static final ConsoleDisplayer consoleDisplayer = new ConsoleDisplayer();

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

    // TODO impose Command pattern into this application
    public static void main(String[] args) {
        Main main = new Main();
        main.menu();
    }

    private void menu() {
        boolean quit = false;
        printHelpMainMenu();
        LOGGER.info("Program has stared");
        do {
            consoleDisplayer.displayLine("Choose your option");
            int option = consoleDisplayer.inputInt();
            switch (option) {
                case 0:
                    printHelpMainMenu();
                    break;
                case 1:
                    try {
                        consoleDisplayer.displayLine("Insert your sudoku:");
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
                    consoleDisplayer.displayLine("Bye, bye");
                    LOGGER.info("Program has finished");
                    quit = true;
            }
        } while(!quit);
    }

    private void printHelpMainMenu() {
        consoleDisplayer.displayLine(
                "MAIN MENU\n" +
                "Insert 0 - to print this help\n" +
                        "Insert 1 - to insert your sudoku and see solution with all steps\n" +
                        "         - empty places in sudoku reaplace with number 0\n" +
                        "Insert 2 - to see some example solution of sudoku with all steps\n" +
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
        consoleDisplayer.displayLine("AutomaticInvoker Used");
        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        int[][] data = fileSudokuReader.read(extremelyHard);
        Sudoku sudoku = new Sudoku(data);
        consoleDisplayer.displayLine(sudoku.toString());
        automatedInvokerWithAllSolvingMethods(sudoku);
    }

    private void stepByStepSudokuAutomaticInvoker() throws IllegalSudokuStateException {
        boolean quit = false;
        printHelpStepByStepMenu();
        List<Command> allStates = new LinkedList<>();
        LOGGER.info("STEP BY STEP module has stared");
        consoleDisplayer.displayLine("Insert your sudoku:");
        Sudoku sudoku = insertYourOwnSudoku();
        AutomatedInvoker automatedInvoker = automatedInvokerWithAllSolvingMethodsStepByStep(sudoku, allStates);
        do {
            consoleDisplayer.displayLine("Choose your option");
            String option = consoleDisplayer.inputString();
            switch (option) {
                case "help":
                    printHelpStepByStepMenu();
                    break;
                case "n":
                    try {
                        goNext(automatedInvoker);
                        LOGGER.info("Reading sudoku - valid input");
                    } catch (Exception e) {
                        LOGGER.warn("Reading sudoku - incorrect input");
                    }
                    break;
                case "p":
                    try {
                        goPrevious(automatedInvoker);
                        LOGGER.info("Default sudoku - valid input");
                    } catch (Exception ex) {
                        LOGGER.warn("Reading sudoku - incorrect input");
                    }
                    break;
                case "all":
                    printAllCommands(automatedInvoker.getCommands());
                    LOGGER.info("Default sudoku - valid input");
                    break;
                case "end":
                    consoleDisplayer.displayLine("Bye, bye STEP BY STEP");
                    LOGGER.info("STEP BY STEP has finished");
                    quit = true;
            }
        } while(!quit);
    }

    private void printHelpStepByStepMenu() {
        consoleDisplayer.displayLine(
                "STEP BY STEP\n" +
                "Insert help - to see this menu step\n" +
                        "Insert n - to see NEXT step\n" +
                        "Insert p - to see PREVIOUS step\n" +
                        "Insert all - to see ALL steps\n" +
                        "Insert end - to EXIT\n"
        );
    }

    private void printSudoku(Sudoku sudoku) {

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                consoleDisplayer.displayLine(sudoku.getRows().get(i).getCell(j).getActualValue() + " ");
            }
            consoleDisplayer.displayLine("");
        }
    }

    private void goNext(AutomatedInvoker automatedInvoker) {
        CommandPicker commandPicker = (CommandPicker) automatedInvoker.getNextState();
        printCommandPicker(commandPicker);
    }

    private void goPrevious(AutomatedInvoker automatedInvoker) {
        CommandPicker commandPicker = (CommandPicker) automatedInvoker.getPreviousState();
        printCommandPicker(commandPicker);
    }

    private AutomatedInvoker automatedInvokerWithAllSolvingMethodsStepByStep(Sudoku sudoku, List<Command> allStates ) {
        try {
            AutomatedInvoker automatedInvoker = new AutomatedInvoker(sudoku, nakedSingleInACell, hiddenSingleInACell, pointingPairBox, pointingPairRowColumn, backtrackLucia);
            allStates = automatedInvoker.getCommands();
            return automatedInvoker;
        } catch (NoAvailableSolution ne) {
            LOGGER.error(ne.toString());
            return null;
        }
    }

    private void automatedInvokerWithAllSolvingMethods(Sudoku sudoku) {
        try {
            AutomatedInvoker automatedInvoker = new AutomatedInvoker(sudoku, nakedSingleInACell, hiddenSingleInACell, pointingPairBox, pointingPairRowColumn, backtrackLucia);
            List<Command> allStates = automatedInvoker.getCommands();
            printAllCommands(allStates);
        } catch (NoAvailableSolution ne) {
            LOGGER.error(ne.toString());
        }
    }

    private void printAllCommands(List<Command> allSudokuStates) {
        for (int i = 0; i < allSudokuStates.size(); i++) {
            CommandPicker actualCommand = ((CommandPicker)allSudokuStates.get(i));
            Sudoku actualSudoku = actualCommand.getSudoku();
            String strategyName = actualCommand.getResolvable().getName();
            consoleDisplayer.displayLine(i + ". " + strategyName + "\n" + actualSudoku.toString());
        }
    }

    private void printCommandPicker(CommandPicker commandPicker) {
        Sudoku actualSudoku = commandPicker.getSudoku();
        String strategyName = commandPicker.getResolvable().getName();
        consoleDisplayer.displayLine(strategyName + "\n" + actualSudoku.toString());
    }
}
