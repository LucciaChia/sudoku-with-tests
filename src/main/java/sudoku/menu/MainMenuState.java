package sudoku.menu;

import lombok.AllArgsConstructor;
import sudoku.command.AutomatedInvoker;
import sudoku.command.Command;
import sudoku.command.Invoker;
import sudoku.exceptions.IllegalSudokuStateException;
import sudoku.exceptions.NoAvailableSolution;
import sudoku.model.Sudoku;
import sudoku.readers.FileSudokuReader;
import sudoku.strategy.Resolvable;
import sudoku.strategy.StrategyFactory;

import java.io.File;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class MainMenuState implements MenuState{

    private static final ClassLoader classLoader = MainMenuState.class.getClassLoader();

    private static final String extremelyHard = new File(Objects.requireNonNull(classLoader.getResource("inputs/extremelyHard.txt")).getFile()).getPath();

    private Invoker automaticInvoker;
    private Invoker manualInvoker;

    @Override
    public MenuState transitionFunction(String input) {

        switch (input) {
            case "0":
                printHelpMainMenu();
                return this;
            case "1":
                return allStepsAutomatedInvokerUserInputSudoku();
            case "2":
                return allStepsAutomatedInvokerDefaultSudoku();
            case "3":
                return option3();
            case "4":
                return option4();
            case "5":
                return quitApplication();
        }

        return this;
    }

    private MenuState allStepsAutomatedInvokerUserInputSudoku() {
        try {
            consoleDisplayer.displayLine("Insert your sudoku:");
            Sudoku sudoku = insertYourOwnSudoku();
            automatedInvokerWithAllSolvingMethods(sudoku);
            LOGGER.info("Reading sudoku - valid input");
        } catch (Exception e) {
            LOGGER.warn("Reading sudoku - incorrect input");
        }
        return this;
    }

    private MenuState allStepsAutomatedInvokerDefaultSudoku() {
        try {
            runDefaultSudokuAutomaticInvoker();
            LOGGER.info("Default sudoku - valid input");
        } catch (IllegalSudokuStateException ex) {
            LOGGER.warn("Reading sudoku - incorrect input");
        }
        return this;
    }

    private MenuState option3() {
        return this;
    }

    private MenuState option4() {
        return this;
    }

    private MenuState quitApplication() {
        consoleDisplayer.displayLine("Bye, bye");
        LOGGER.info("Program has finished");
        return null;
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

    private Sudoku insertYourOwnSudoku() throws IllegalSudokuStateException {

        int[][] data = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                data[i][j] = consoleDisplayer.inputInt();
            }
        }
        return new Sudoku(data);
    }

    private void automatedInvokerWithAllSolvingMethods(Sudoku sudoku) {
        StrategyFactory strategyFactory = new StrategyFactory();
        Resolvable nakedSingle = strategyFactory.createNakedSingleInACellStrategy();
        Resolvable hiddenSingle = strategyFactory.createHiddenSingleInACellStrategy();
        Resolvable pointingPair1 = strategyFactory.createPointingPairsRowColumnStrategy();
        Resolvable pointingPair2 = strategyFactory.createPointingPairsBoxStrategy();
        Resolvable backtrack = strategyFactory.createBacktrackStrategy();

        try {
            AutomatedInvoker automatedInvoker = new AutomatedInvoker(
                sudoku, nakedSingle, hiddenSingle, pointingPair1, pointingPair2, backtrack);
            List<Command> allStates = automatedInvoker.getCommands();
            printAllCommands(allStates);
        } catch (NoAvailableSolution ne) {
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

    private void runDefaultSudokuAutomaticInvoker() throws IllegalSudokuStateException{
        consoleDisplayer.displayLine("AutomaticInvoker Used");
        FileSudokuReader fileSudokuReader = new FileSudokuReader();
        int[][] data = fileSudokuReader.read(extremelyHard);
        Sudoku sudoku = new Sudoku(data);
        consoleDisplayer.displayLine(sudoku.toString());
        automatedInvokerWithAllSolvingMethods(sudoku);
    }

}
