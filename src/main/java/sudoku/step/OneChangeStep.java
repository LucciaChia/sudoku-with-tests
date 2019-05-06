package sudoku.step;

import sudoku.model.Cell;
import sudoku.model.Sudoku;
import sudoku.strategy.Resolvable;

import java.util.Arrays;
import java.util.Map;

import static sudoku.ANSIColour.*;

public class OneChangeStep implements Step {

    public static int stepNumber = 0;
    private Sudoku sudoku;
    private String solvingStrategyName;
    private Cell cell;
    private Cell partnerCell;
    private Map<int[], Integer> deletedPossibilitiesWithLocation;
    private Resolvable resolvable;

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

    public OneChangeStep(Sudoku sudoku, String solvingStrategyName) {
        stepNumber++;
        this.sudoku = sudoku;
        this.solvingStrategyName = solvingStrategyName;
    }

    public OneChangeStep(Sudoku sudoku, String solvingStrategyName, Cell cell) {
        stepNumber++;
        this.sudoku = sudoku;
        this.solvingStrategyName = solvingStrategyName;
        this.cell = cell;
    }

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

    @Override
    public void printStep(Cell cell) {
        System.out.println(stepNumber + ". " + solvingStrategyName);
        System.out.println(ANSI_RED + ANSI_BOLD + "[" + cell.getI() + ", " + cell.getJ() + "] = " + cell.getActualValue() + ANSI_RESET);
        sudoku.print();
        sudoku.toString();
//        sudoku.printPossibilitiesInSudoku();
    }

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

    @Override
    public void printBacktrack() {
        System.out.println(stepNumber + ". " + solvingStrategyName);
        System.out.println(ANSI_RED + ANSI_BOLD + "Brute Force method was used." + ANSI_RESET);
        sudoku.print();
        sudoku.toString();
    }

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
