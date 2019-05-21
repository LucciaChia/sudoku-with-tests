package sudoku.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sudoku.console.ConsoleDisplayer;
import sudoku.console.Displayer;

public interface MenuState {

    Logger LOGGER = LoggerFactory.getLogger(MenuState.class);
    Displayer consoleDisplayer = new ConsoleDisplayer();

    MenuState transitionFunction(String input);
}
