package sudoku.processingUsingStrategy;

import sudoku.model.Cell;
import sudoku.model.Sudoku;
import sudoku.stepHandlers.OneChangeStep;
import sudoku.stepHandlers.Step;

import java.util.Scanner;

public class BacktrackLucia implements Resolvable{

    Scanner scanner = new Scanner(System.in);
    private boolean updatedInBacktrackLucia = false;

    private static long stepCount = 0;
    private String name = "3: Backtrack";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Sudoku resolveSudoku(Sudoku sudoku) {
        updatedInBacktrackLucia = false;
        int possibilityIndex = 0;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {

                Cell cell = sudoku.getRows().get(i).getCell(j);
                int cellPossibilitiesAmount = cell.getCellPossibilities().size();
                if (cell.getActualValue() != 0) {
                    continue;
                }

                if (cellPossibilitiesAmount > 0) {
                    stepCount++;

                    for (int k = 0; k < cellPossibilitiesAmount; k++) {
                        Sudoku newSudoku = sudoku.copy();
//                        if (sudoku != newSudoku) {
//                            System.out.println("mam novy objekt newSudoku");
//                        }

                        Cell newCell = newSudoku.getRows().get(i).getCell(j);

                        int usedPossibility = cell.getCellPossibilities().get(k);
                        newCell.setActualValue(usedPossibility);
//                        System.out.println("VALUE WAS SET " + usedPossibility + " i=" + i + " j=" + j);
                        deletePossibilities(newCell, usedPossibility);


                        Sudoku resolvedSudoku = resolveSudoku(newSudoku);

//                        System.out.println("REACHED sudoku");
//                        sudoku.print();
//                        newSudoku.print();
//                        System.out.println("Value " + usedPossibility);
//                        System.out.println("I = " + i + " J = " + j);
//                        System.out.println("Possibilities " + cell.getCellPossibilities());

                        if (resolvedSudoku != null) {
//                            System.out.println("Resolved");
//                            resolvedSudoku.print();
                            updatedInBacktrackLucia = false;

                            return resolvedSudoku;
                        } else {
                            //System.out.println("not resolved");
                        }

//                        scanner.nextLine();
                    }

//                    System.out.println("cannot solve this sudoku :(");
                    return null;
                }

                if (cellPossibilitiesAmount == 0) {
                    return null;
                }
            }
        }
        Step step = new OneChangeStep(sudoku, name);
        step.printBacktrack();
        sudoku.print();
        return sudoku;
    }

    @Override
    public boolean isUpdated() {
        return updatedInBacktrackLucia;
    }
}
