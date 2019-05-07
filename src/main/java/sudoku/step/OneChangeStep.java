package sudoku.step;

import sudoku.model.Cell;
import sudoku.model.Sudoku;
import sudoku.strategy.Resolvable;

import java.util.Arrays;
import java.util.Map;

import static sudoku.ANSIColour.*;

/**
 * Class representing single step containing information about current state of the sudoku,
 * strategy that was used in this step and changes that were made
 */
public class OneChangeStep implements Step {

    public static int stepNumber = 0;
    private Sudoku sudoku;
    private String solvingStrategyName;
    private Cell cell;
    private Cell partnerCell;
    private Map<int[], Integer> deletedPossibilitiesWithLocation;
    private Resolvable resolvable;

    /**
     * Constructor method which use the most basic information required for step - current state of sudoku
     * and strategy that will be used in this step
     *
     * @param strategy  Resolvable that will be used as strategy in this step
     * @param sudoku    sudoku that is current state of the sudoku
     */
    public OneChangeStep(Resolvable strategy, Sudoku sudoku) {
        this.resolvable = strategy;
        this.sudoku = sudoku;
    }

    public Resolvable getResolvable() {
        return resolvable;
    }

    public void setResolvable(Resolvable resolvable) {
        this.resolvable = resolvable;
    }

    /**
     * Constructor method which use the information required for step - current state of sudoku
     * and strategy that will be used in this step
     *
     * @param sudoku                sudoku that is current state of the sudoku
     * @param solvingStrategyName   string that is name of the strategy that will be used
     */
    public OneChangeStep(Sudoku sudoku, String solvingStrategyName) {
        stepNumber++;
        this.sudoku = sudoku;
        this.solvingStrategyName = solvingStrategyName;
    }

    /**
     * Constructor method which use the information required for step - current state of sudoku
     * and strategy that will be used in this step and the cell that will be affected.
     *
     * @param sudoku                sudoku that is current state of the sudoku
     * @param solvingStrategyName   string that is name of the strategy that will be used
     * @param cell                  a cell that is affected in this step
     */
    public OneChangeStep(Sudoku sudoku, String solvingStrategyName, Cell cell) {
        stepNumber++;
        this.sudoku = sudoku;
        this.solvingStrategyName = solvingStrategyName;
        this.cell = cell;
    }

    /**
     * Constructor method which use the information required for step - current state of sudoku
     * and strategy that will be used in this step and the cell that will be affected
     *
     * @param sudoku                                sudoku that is current state of the sudoku
     * @param solvingStrategyName                   string that is name of the strategy that will be used
     * @param cell                                  cell of the pair of cells used in the strategy
     * @param partnerCell                           cell of the pair of cells used in the strategy
     * @param deletedPossibilitiesWithLocation      map of possibilities that are deleted in this step
     */
    public OneChangeStep(Sudoku sudoku, String solvingStrategyName, Cell cell, Cell partnerCell, Map<int[], Integer> deletedPossibilitiesWithLocation) {
        stepNumber++;
        this.sudoku = sudoku;
        this.solvingStrategyName = solvingStrategyName;
        this.cell = cell;
        this.partnerCell = partnerCell;
        this.deletedPossibilitiesWithLocation = deletedPossibilitiesWithLocation;
    }


    public Sudoku getSudoku() {
        return sudoku;
    }

    public static int getStepNumber() {
        return stepNumber;
    }

    public String getSolvingStrategyName() {
        return solvingStrategyName;
    }

    public Cell getCell() {
        return cell;
    }

    public Cell getPartnerCell() {
        return partnerCell;
    }

    public Map<int[], Integer> getDeletedPossibilitiesWithLocation() {
        return deletedPossibilitiesWithLocation;
    }

    /**
     * Method that prints changes achieve by the use of NakedSingleCell or HiddenSingleCell strategy
     * and then prints the current state of the sudoku
     *
     * @param cell  a cell whose actual value has been set in this step
     */
    @Override
    public void printStep(Cell cell) {
        System.out.println(stepNumber + ". " + solvingStrategyName);
        System.out.println(ANSI_RED + ANSI_BOLD + "[" + cell.getI() + ", " + cell.getJ() + "] = " + cell.getActualValue() + ANSI_RESET);
        sudoku.print();
        sudoku.toString();
//        sudoku.printPossibilitiesInSudoku();
    }

    /**
     * Method that prints information about possibilities removed by the use of PointingPairsInCell strategy
     * and then prints current state of the sudoku
     *
     * @param cell                              cell of the pair of cells used in the strategy
     * @param partnerCell                       cell of the pair of cells used in the strategy
     * @param deletedPossibilitiesWithLocation  possibilities that has been removed in this step
     */
    @Override
    public void printStepPointingPair(Cell cell, Cell partnerCell, Map<int[], Integer> deletedPossibilitiesWithLocation) {
        System.out.println(stepNumber + ". " + solvingStrategyName);
        System.out.println(ANSI_RED + ANSI_BOLD + "[" + cell.getI() + ", " + cell.getJ() + "] = " + cell.getActualValue() + "\n"
                + "[" + partnerCell.getI() + ", " + partnerCell.getJ() + "] = " + partnerCell.getActualValue() + ANSI_RESET);
        for (int[] key : deletedPossibilitiesWithLocation.keySet()) {
            System.out.println(ANSI_RED + ANSI_BOLD + deletedPossibilitiesWithLocation.get(key) + ": " + Arrays.toString(key) + ANSI_RESET);
        }
        sudoku.print();
        sudoku.toString();
    }

    /**
     * Method that prints information that Backtracking strategy has been used and prints solved sudoku
     */
    @Override
    public void printBacktrack() {
        System.out.println(stepNumber + ". " + solvingStrategyName);
        System.out.println(ANSI_RED + ANSI_BOLD + "Brute Force method was used." + ANSI_RESET);
        sudoku.print();
        sudoku.toString();
    }

    /**
     * method that calls print method for the strategy used in this step
     *
     * @return      string that is empty
     */
    @Override
    public String toString() {
        if (this.getSolvingStrategyName().equals("0: NackedSingleInACell") || this.getSolvingStrategyName().equals("1: HiddenSingleInACell")) {
            printStep(this.cell);
        } else if (this.getSolvingStrategyName().equals("2: PointingPairsInCell")) {
            printStepPointingPair(this.cell, this.partnerCell, this.deletedPossibilitiesWithLocation);
        } else if (this.getSolvingStrategyName().equals("3: Backtrack")) {
            printBacktrack();
        }

        return "";
    //    return super.toString();
    }
}
