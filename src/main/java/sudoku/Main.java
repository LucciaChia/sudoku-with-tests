package sudoku;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sudoku.command.AutomatedInvoker;
import sudoku.command.Command;
import sudoku.command.CommandPicker;
import sudoku.command.ManualInvoker;
import sudoku.console.ConsoleDisplayer;
import sudoku.console.Displayer;
import sudoku.exceptions.IllegalSudokuStateException;
import sudoku.exceptions.NoAvailableSolutionException;
import sudoku.model.Sudoku;
import sudoku.readers.FileSudokuReader;
import sudoku.strategy.Resolvable;
import sudoku.strategy.StrategyFactory;

import java.io.File;
import java.util.List;
import java.util.Objects;

/*
 * simple scanner schema used in order the client could communicate with the program via console + step by step
 * principle enabled via second switch
 */
public class Main {

    // TODO dopnit do logov, co este chyba
    // TODO okomentovat vsetky metody, ktore obsahuju nejaku logiku
    // TODO do refactoring, remove duplicate code

    private static ClassLoader classLoader = Main.class.getClassLoader();

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static final Displayer consoleDisplayer = new ConsoleDisplayer();

    private static final String extremelyHard = new File(Objects.requireNonNull(classLoader.getResource("inputs/extremelyHard.txt")).getFile()).getPath();

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
                    try {
                        consoleDisplayer.displayLine("Please enter your sudoku");
                        stepByStepSudokuManualInvoker();
                        LOGGER.info("Default sudoku - valid input");
                    } catch (IllegalSudokuStateException ex) {
                        LOGGER.warn("Reading sudoku - incorrect input");
                    }

                    break;
                case 5:
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
                        "         - empty places in sudoku replace with number 0\n" +
                        "Insert 2 - to see some example solution of sudoku with all steps\n" +
                        "Insert 3 - to insert your own sudoku and see solution solution STEP BY STEP\n" +
                        "         - empty places in sudoku replace with number 0\n" +
                        "Insert 4 - to insert your own sudoku and see solution solution STEP BY STEP choosing strategy each step\n" +
                        "         - empty places in sudoku replace with number 0\n" +
                        "Insert 5 - to quit program"
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
        LOGGER.info("STEP BY STEP module has stared");
        consoleDisplayer.displayLine("Insert your sudoku:");
        Sudoku sudoku = insertYourOwnSudoku();
        AutomatedInvoker automatedInvoker = automatedInvokerWithAllSolvingMethodsStepByStep(sudoku);
        do {
            String option = "end";
            if (automatedInvoker == null) {
                consoleDisplayer.displayLine("There's no available solution for this sudoku");
            }else {
                consoleDisplayer.displayLine("Choose your option");
                option = consoleDisplayer.inputString();
            }
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

    private void stepByStepSudokuManualInvoker() throws IllegalSudokuStateException {
        boolean quit = false;
        Sudoku sudoku = insertYourOwnSudoku();
        ManualInvoker manualInvoker = new ManualInvoker(sudoku);
        CommandPicker command;

        printHelpStepByStepMenuForManualInvoker();
        LOGGER.info("STEP BY STEP module has stared");
        do {
            consoleDisplayer.displayLine("Choose your option");
            String option = consoleDisplayer.inputString();
            switch (option) {
                case "help":
                    printHelpStepByStepMenuForManualInvoker();
                    break;
                case "n":
                    stepByStepSudokuManualInvokerNext(sudoku);
                    break;
                case "p":
                    try {
                        // TODO initial step needed
                        if (manualInvoker.getCurrentStep() > 1) {
                            command = (CommandPicker) manualInvoker.getPreviousState();
                            printCommandPicker(command);
                        } else {
                            LOGGER.info("There is no previous state");
                            consoleDisplayer.displayLine("There is no previous state");
                        }
                    } catch (Exception ex) {
                        LOGGER.warn("Reading sudoku - incorrect input");
                    }
                    break;
                case "end":
                    consoleDisplayer.displayLine("Bye, bye STEP BY STEP");
                    LOGGER.info("STEP BY STEP has finished");
                    quit = true;
            }
        } while(!quit);
    }

    private void stepByStepSudokuManualInvokerNext(Sudoku sudoku) {
        ManualInvoker manualInvoker = new ManualInvoker(sudoku);
        CommandPicker command;
        StrategyFactory strategyFactory = new StrategyFactory();
        Resolvable nakedSingle = strategyFactory.createNakedSingleInACellStrategy();
        Resolvable hiddenSingle = strategyFactory.createHiddenSingleInACellStrategy();
        Resolvable pointingPair1 = strategyFactory.createPointingPairsRowColumnStrategy();
        Resolvable pointingPair2 = strategyFactory.createPointingPairsBoxStrategy();
        Resolvable backtrack = strategyFactory.createBacktrackStrategy();
        boolean relevantChoise = false;

        printHelpStepByStepStrategyMenu();
        LOGGER.info("STEP BY STEP STRATEGY CHOICE module has stared");
        consoleDisplayer.displayLine("Choose your strategy option");
        String strategyOption = consoleDisplayer.inputString();
        switch (strategyOption) {
            case "help":
                printHelpStepByStepStrategyMenu();
                break;
            case "n" :
                relevantChoise = true;
                manualInvoker.setStrategies(nakedSingle);
                break;
            case "h" :
                relevantChoise = true;
                manualInvoker.setStrategies(hiddenSingle);
                break;
            case "p1" :
                relevantChoise = true;
                manualInvoker.setStrategies(pointingPair1);
                break;
            case "p2" :
                relevantChoise = true;
                manualInvoker.setStrategies(pointingPair2);
                break;
            case "b" :
                relevantChoise = true;
                manualInvoker.setStrategies(backtrack);
                break;
            case "end" :
                consoleDisplayer.displayLine("Bye, bye STEP BY STEP STRATEGY CHOICE");
                LOGGER.info("STEP BY STEP STRATEGY CHOICE has finished");
                break;
        }

        if (relevantChoise) {
            try {
                command = (CommandPicker) manualInvoker.getNextState();
                printCommandPicker(command);
                LOGGER.info("Reading sudoku - valid input");
            } catch (Exception e) {
                LOGGER.warn("Reading sudoku - incorrect input");
            }
        }
    }

    private void printHelpStepByStepMenuForManualInvoker() {
        consoleDisplayer.displayLine(
            "STEP BY STEP\n" +
                "Insert help - to see this menu step\n" +
                "Insert n - to see NEXT step\n" +
                "Insert p - to see PREVIOUS step\n" +
                "Insert end - to EXIT\n"
        );
    }

    private void printHelpStepByStepStrategyMenu() {
        consoleDisplayer.displayLine(
            "STRATEGY OPTIONS\n" +
                "Insert help - to see this menu step\n" +
                "Insert n - to see NEXT step after Naked Single strategy is used\n" +
                "Insert h - to see NEXT step after Hidden Single strategy is used\n" +
                "Insert p1 - to see NEXT step after Pointing Pair strategy for column and row is used\n" +
                "Insert p2 - to see NEXT step after Pointing Pair strategy for box is used\n" +
                "Insert b - to solve sudoku using backtrack\n" +
                "Insert end - to EXIT\n"
        );
    }

    private void goNext(AutomatedInvoker automatedInvoker) {
        CommandPicker commandPicker = (CommandPicker) automatedInvoker.getNextState();
        printCommandPicker(commandPicker);
    }

    private void goPrevious(AutomatedInvoker automatedInvoker) {
        CommandPicker commandPicker = (CommandPicker) automatedInvoker.getPreviousState();
        printCommandPicker(commandPicker);
    }

    private AutomatedInvoker automatedInvokerWithAllSolvingMethodsStepByStep(Sudoku sudoku) {
        try {
            return new AutomatedInvoker(sudoku, nakedSingleInACell, hiddenSingleInACell, pointingPairBox, pointingPairRowColumn, backtrackLucia);
        } catch (NoAvailableSolutionException ne) {
            LOGGER.error(ne.toString());
            return null;
        }
    }

    private void automatedInvokerWithAllSolvingMethods(Sudoku sudoku) {
        try {
            AutomatedInvoker automatedInvoker = new AutomatedInvoker(sudoku, nakedSingleInACell, hiddenSingleInACell, pointingPairBox, pointingPairRowColumn, backtrackLucia);
            List<Command> allStates = automatedInvoker.getCommands();
            printAllCommands(allStates);
        } catch (NoAvailableSolutionException ne) {
            LOGGER.error(ne.toString());
        }
    }

    private void printAllCommands(List<Command> allSudokuStates) {
        if (allSudokuStates == null) {
            consoleDisplayer.displayLine("This sudoku has no solution");
            return;
        }
        for (int i = 0; i < allSudokuStates.size(); i++) {
            Command actualCommand = allSudokuStates.get(i);
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
