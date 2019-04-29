package sudoku.processing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sudoku.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * contains resolving logic for sudoku
 * @author Lucia
 */
public class Solution extends SudokuElement{

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    private static final Logger extAppLogFile = LoggerFactory.getLogger(Solution.class);

    private List<Column> columns;
    private List<Row> rows;
    private List<Box> boxes;
    private List<List<Integer>> possibilityList = new ArrayList<>();

    // constructor for test purposes
    public Solution(List<Box> boxes) {
        this.boxes = boxes;
    }

    public Solution(Sudoku sudoku) {
        this.columns = sudoku.getColumns();
        this.rows = sudoku.getRows();
        this.boxes = sudoku.getBoxes();
    }

    @Deprecated
    public Solution(List<Column> columns, List<Row> rows, List<Box> boxes) {
        this.columns = columns;
        this.rows = rows;
        this.boxes = boxes;
    }

    public List<Row> outputWITHOUTdataArray() {
        boolean end = false;
        int endCount = 0;
        int firstRound = 1;
        boolean sudokuUpdated;
        do {
            sudokuUpdated = false;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    Cell currentCell = rows.get(i).getCell(j);
                    int currentCellValue = currentCell.getActualValue();
                    if (currentCellValue == 0) {
                        List<Integer> possibility = currentCell.getCellPossibilities();
                        if (firstRound == 1) {
                            possibilityList.add(possibility);
                        }

                        rows.get(i).search(currentCell);
                        columns.get(j).search(currentCell);
                        findCorrectSquare(i, j).search(currentCell);

                        if (possibility.size() == 1) {
                            currentCell.setActualValue(possibility.get(0));
                            //currentCellValue = rows.get(i).getCell(j).getActualValue();
                            sudokuUpdated = true;
                            possibilityList.remove(possibility);
                            continue;
                        }
                        if (firstRound > 1){
                            currentCellValue = findHiddenSingleInCell(currentCell);
                            if (currentCellValue != 0) {
                                possibilityList.remove(possibility);
                                sudokuUpdated = true;
                            }

                            if (end && !possibilityList.isEmpty()) {
                                extAppLogFile.info("Not updated, try something more advanced > i = " + i + " j = " + j);
                                if (pointingPairInCells(currentCell)) {
                                    sudokuUpdated = true;
                                    //end = false;
                                    endCount--; // toto este overit
                                }
                            }
                        }
                    }
                }
            }


            // skus advanced
            if (!sudokuUpdated) {
                end = true;
                System.out.println("Koniec");
            }
            if (end && !possibilityList.isEmpty() && endCount < 1) {
                extAppLogFile.info("Som na konci, ale mam este nieco v possibility liste -> skus advanced metody");
                sudokuUpdated = true;
                endCount++;
            }
            // ===============
            firstRound++;
        } while (sudokuUpdated || firstRound <= 2);
        return rows;
    }

    private Box findCorrectSquare(int rowIndex, int columnIndex) {

        return boxes.get((rowIndex/3)*3 + columnIndex/3);

    }

    private int findHiddenSingleInCell(Cell cell) {
        int indexI = cell.getI();
        int indexJ = cell.getJ();
        Box box = findCorrectSquare(indexI, indexJ);
        List<Integer> cellPossibilities = cell.getCellPossibilities();

        Util util = new Util();
        Map<Integer, Integer> horizMap = util.amountOfParticularPossibilities(rows.get(indexI));
        Map<Integer, Integer> vericalMap = util.amountOfParticularPossibilities(columns.get(indexJ));
        Map<Integer, Integer> squareMap = util.amountOfParticularPossibilities(box);

        for (int cellPosibility : cellPossibilities) {
            if ((horizMap.containsKey(cellPosibility) && horizMap.get(cellPosibility) == 1) ||
                    (vericalMap.containsKey(cellPosibility) && vericalMap.get(cellPosibility) == 1) ||
                    (squareMap.containsKey(cellPosibility) && squareMap.get(cellPosibility) == 1))
            {
                cell.setActualValue(cellPosibility); // nastavenie na hodnotu a zrusenie poss pre tuto bunku
                // vymazanie tejto hodnoty z possibilities v riadku, stlpci, stvorci

                rows.get(indexI).removePossibility(cellPosibility, cell);
                columns.get(indexJ).removePossibility(cellPosibility, cell);
                box.removePossibility(cellPosibility,cell);

                return cell.getActualValue();

            }

        }
        return 0;
    }

    private boolean pointingPairInCells(Cell cell) {
        int cellI = cell.getI();
        int cellJ = cell.getJ();
        Box cellBox = findCorrectSquare(cellI, cellJ);
        List<Cell> eligiblePartnerCells;
        boolean changed = false;

        for (int possibilityToCheck : cell.getCellPossibilities()) {
            eligiblePartnerCells = findPartnerCell(cell, possibilityToCheck);

            for (Cell partnerCell : eligiblePartnerCells) {
                boolean changedInLoop = false;

                // vypisy
                extAppLogFile.info("Partner cell for cell possibility: " + possibilityToCheck + " in cell  i = " + cellI + " j = " + cellJ + " IS: i = " +
                        partnerCell.getI() + " j = " + partnerCell.getJ());

                if (!partnerCell.getCellPossibilities().contains((Integer)possibilityToCheck)) {
                    continue;
                }

                if (!isPossibilityToCheckPresentSomewhereElseInSquare(cell, partnerCell, cellBox.getCellList(), possibilityToCheck)) {
                    extAppLogFile.info(ANSI_GREEN + "Possibility " + possibilityToCheck + " presents only in row / column" + ANSI_RESET);
                    // ak je len v tomto riadku vyhadzem tu possibilitu z tohto riadka v ostatnych stvorcoch, ak v stlpci tak zo stlpca

                    if (cell.getI() == partnerCell.getI()) {
                        changedInLoop = rows.get(cellI).deletePossibilitiesInRowOrColumnSudokuElement(cell, possibilityToCheck, boxes);
                    } else if (cell.getJ() == partnerCell.getJ()) {
                        changedInLoop = columns.get(cellJ).deletePossibilitiesInRowOrColumnSudokuElement(cell, possibilityToCheck, boxes);
                    }


                    // old way, which is working // =====================================================================
                    //changedInLoop = deletePossibilitiesInRowOrColumn(cell, partnerCell, possibilityToCheck);
                } else { // nachadza sa aj inde vo stvorci
                    extAppLogFile.info(ANSI_RED + "Possibility " + possibilityToCheck + " presents somewhere else too" + ANSI_RESET);
                    if (cellI == partnerCell.getI() && !isPossibilityToCheckPresentSomewhereElseInRow(cell, partnerCell, possibilityToCheck)) {
                        changedInLoop = deletePossibilitiesInSquare(cell, partnerCell, possibilityToCheck);
                    } else if (cellJ == partnerCell.getJ() && !isPossibilityToCheckPresentSomewhereElseInColumn(cell, partnerCell, possibilityToCheck)) {
                        changedInLoop = deletePossibilitiesInSquare(cell, partnerCell, possibilityToCheck);
                    }
                }

                if (changedInLoop) {
                    changed = true;
                }
            }
        }
        return changed;
    }

    public List<Cell> findPartnerCell(Cell cell, int possibilityToCheck) {
        int indexI = cell.getI();
        int indexJ = cell.getJ();
        Box targetBox = findCorrectSquare(indexI, indexJ);
        List<Cell> eligiblePartnerCellList = new ArrayList<>();

        for (Cell candidateCell : targetBox.getCellList()) {

            if (candidateCell.getActualValue() == 0 ) {
                int candidateCellI = candidateCell.getI();
                int candidateCellJ = candidateCell.getJ();

                if ((indexI == candidateCellI || indexJ == candidateCellJ) && cell != candidateCell) {
                    List<Integer> candidatePosibilities = candidateCell.getCellPossibilities();

                    if (candidatePosibilities.contains((Integer)possibilityToCheck)) {
                        eligiblePartnerCellList.add(candidateCell);
                    }
                }
            }
        }

        return eligiblePartnerCellList;
    }

    private boolean isPossibilityToCheckPresentSomewhereElseInSquare(Cell cell, Cell partnerCell, List<Cell> squareOfCells, int possibilityToCheck) {
        int cellI = cell.getI();
        int cellJ = cell.getJ();
        int partnerCellI = partnerCell.getI();
        int partnerCellJ = partnerCell.getJ();

        if (cellI == partnerCellI) {
            extAppLogFile.info("i case");
            for (Cell testedCell : squareOfCells) {
                if (testedCell.getActualValue() == 0 && testedCell.getI() != cellI && testedCell.getCellPossibilities().contains((Integer) possibilityToCheck)) {
                    return true;
                }
            }
        }
        if (cellJ == partnerCellJ) {
            extAppLogFile.info("j case");
            for (Cell testedCell : squareOfCells) {
                if (testedCell.getActualValue() == 0 && testedCell.getJ() != cellJ && testedCell.getCellPossibilities().contains((Integer) possibilityToCheck)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isPossibilityToCheckPresentSomewhereElseInRow(Cell cell, Cell partnerCell, int possibilityToCheck) {
        int cellI = cell.getI();
        int cellJ = cell.getJ();
        int partnerCellI = partnerCell.getI();
        Box cellBox = findCorrectSquare(cellI, cellJ);

        if (cellI == partnerCellI) {
            extAppLogFile.info("horizontal i case");
            for (Cell testedCell : rows.get(cellI).getCellList()) {
                if (testedCell.getActualValue() == 0 && cellBox != findCorrectSquare(testedCell.getI(), testedCell.getJ()) &&
                        testedCell.getCellPossibilities().contains((Integer) possibilityToCheck)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isPossibilityToCheckPresentSomewhereElseInColumn(Cell cell, Cell partnerCell, int possibilityToCheck) {
        int cellI = cell.getI();
        int cellJ = cell.getJ();
        int partnerCellJ = partnerCell.getJ();
        Box cellBox = findCorrectSquare(cellI, cellJ);

        if (cellJ == partnerCellJ) {
            extAppLogFile.info("vertical j case");
            for (Cell testedCell : columns.get(cellJ).getCellList()) {
                if (testedCell.getActualValue() == 0 && cellBox != findCorrectSquare(testedCell.getI(), testedCell.getJ()) &&
                        testedCell.getCellPossibilities().contains((Integer) possibilityToCheck)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean deletePossibilitiesInSquare(Cell cell, Cell partnerCell, int possibilityToCheck) {
        int cellI = cell.getI();
        int cellJ = cell.getJ();
        int partnerCellI = partnerCell.getI();
        int partnerCellJ = partnerCell.getJ();
        boolean somethingWasRemoved = false;
        Box cellBox = findCorrectSquare(cellI, cellJ);

        if (cellI == partnerCellI) {
            for (Cell testedCell : cellBox.getCellList()) {
                if (testedCell.getActualValue() == 0 && testedCell.getI() != cellI && testedCell.getCellPossibilities().contains((Integer)possibilityToCheck)) {
                    extAppLogFile.info(ANSI_PURPLE + "\t SQUARE CASE: Possibility " + possibilityToCheck + " will be removed from " +
                            "i=" + testedCell.getI() + " j=" + testedCell.getJ() + ANSI_RESET);
                    somethingWasRemoved = testedCell.getCellPossibilities().remove((Integer)possibilityToCheck);
                }
            }
        } else if (cellJ == partnerCellJ) {
            for (Cell testedCell : cellBox.getCellList()) {
                if (testedCell.getActualValue() == 0 && testedCell.getJ() != cellJ && testedCell.getCellPossibilities().contains((Integer)possibilityToCheck)) {
                    extAppLogFile.info(ANSI_PURPLE + "\t SQUARE CASE: Possibility " + possibilityToCheck + " will be removed from " +
                            "i=" + testedCell.getI() + " j=" + testedCell.getJ() + ANSI_RESET);
                    somethingWasRemoved = testedCell.getCellPossibilities().remove((Integer)possibilityToCheck);
                }
            }
        } else {
            extAppLogFile.info(ANSI_RED + "\tSQUARE CASE: Something's wrong - incorrect partner cell!" + ANSI_RESET);
        }
        return somethingWasRemoved;
    }

//    private boolean deletePossibilitiesInRowOrColumn(Cell cell, Cell partnerCell, int possibilityToCheck) {
//        int cellI = cell.getI();
//        int cellJ = cell.getJ();
//        boolean somethingWasRemoved = false;
//        Box cellBox = findCorrectSquare(cell.getI(), cell.getJ());
//
//        if (cellI == partnerCell.getI()) {
//            for (Cell testedCell : rows.get(cellI).getCellList()) {
//                Box testedCellBox = findCorrectSquare(testedCell.getI(), testedCell.getJ());
//                if (testedCell.getActualValue() == 0 && cellBox != testedCellBox && testedCell.getCellPossibilities().contains((Integer)possibilityToCheck)) {
//                    extAppLogFile.info(ANSI_BLUE + "\tROW-COLUMN CASE: Possibility " + possibilityToCheck + " will be removed from " +
//                            "i=" + testedCell.getI() + " j=" + testedCell.getJ() + ANSI_RESET);
//                    somethingWasRemoved = testedCell.getCellPossibilities().remove((Integer)possibilityToCheck);
//                }
//            }
//
//        } else if (cellJ == partnerCell.getJ()){
//            for (Cell testedCell : columns.get(cellJ).getCellList()) {
//                Box testedCellBox = findCorrectSquare(testedCell.getI(), testedCell.getJ());
//                if (testedCell.getActualValue() == 0 && cellBox != testedCellBox && testedCell.getCellPossibilities().contains((Integer)possibilityToCheck)) {
//                    extAppLogFile.info(ANSI_BLUE + "\tROW-COLUMN CASE: Possibility " + possibilityToCheck + " will be removed from " +
//                            "i=" + testedCell.getI() + " j=" + testedCell.getJ() + ANSI_RESET);
//                    somethingWasRemoved = testedCell.getCellPossibilities().remove((Integer)possibilityToCheck);
//                }
//            }
//        } else {
//            extAppLogFile.info(ANSI_RED + "\tROW-COLUMN CASE: Something's wrong - incorrect partner cell!" + ANSI_RESET);
//        }
//        return somethingWasRemoved;
//    }

}