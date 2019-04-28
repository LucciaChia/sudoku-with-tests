package sudoku.stepHandlers;

import sudoku.model.Cell;
import sudoku.model.Sudoku;

import java.util.Arrays;
import java.util.Map;

public class OneChangeStep implements Step {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_BOLD = "\u001B[1m";

    public static int stepNumber = 0;
    Sudoku sudoku;
    String solvingStrategyName;

    public OneChangeStep(Sudoku sudoku, String solvingStrategyName) {
        stepNumber++;
        this.sudoku = sudoku;
        this.solvingStrategyName = solvingStrategyName;
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
    }
}
