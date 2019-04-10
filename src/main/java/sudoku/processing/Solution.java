package sudoku.processing;

import sudoku.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * contains resolving logic for sudoku
 * @author Lucia
 */
public class Solution {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";



    List<Vertical> verticals;
    List<Horizontal> horizontals;
    List<Square> squares;
    int[][] data;
    List<List<Integer>> possibilityList = new ArrayList<>();

    // constructor for test purposes
    public Solution(List<Square> squares) {
        this.squares = squares;
    }


    public Solution(List<Vertical> verticals, List<Horizontal> horizontals, List<Square> squares, int[][] data) {
        this.verticals = verticals;
        this.horizontals = horizontals;
        this.squares = squares;
        this.data = data;
    }

    public List<Horizontal> output() {
        boolean end = false;
        int endCount = 0;
        int firstRound = 1;
        boolean sudokuUpdated;
        do {
            sudokuUpdated = false;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (data[i][j] == 0) {
                        List<Integer> possibility = horizontals.get(i).getCellInHorizontal(j).getCellPossibilities();
                        if (firstRound == 1) {
                            possibilityList.add(possibility);
                        }

                        searchRow(horizontals.get(i).getCellInHorizontal(j));
                        searchColumn(horizontals.get(i).getCellInHorizontal(j));
                        searchSquare(horizontals.get(i).getCellInHorizontal(j));

                        if (possibility.size() == 1) {
                            horizontals.get(i).getCellInHorizontal(j).setActualValue(possibility.get(0));
                            data[i][j] = horizontals.get(i).getCellInHorizontal(j).getActualValue();
                            sudokuUpdated = true;
                            possibilityList.remove(possibility);
                            continue;
                        }
                        if (firstRound > 1){
                            data[i][j] = findHiddenSingleInCell(horizontals.get(i).getCellInHorizontal(j));
                            if (data[i][j] != 0) {
                                possibilityList.remove(possibility);
                                sudokuUpdated = true;
                            }

                            if (end && !possibilityList.isEmpty()) {
                                System.out.println("Not updated, try something more advanced > i = " + i + " j = " + j);
                                if (pointingPairInCells(horizontals.get(i).getCellInHorizontal(j))) {
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
                System.out.println("Som na konci, ale mam este nieco v possibility liste -> skus advanced metody");
                sudokuUpdated = true;
                endCount++;
            }
            // ===============
            firstRound++;
        } while (sudokuUpdated || firstRound <= 2);

        return horizontals;
    }

    private List<Integer> searchRow(Cell cell) { // odoberanie potencialnych moznosti z ciell v riadku
        int rowIndex = cell.getI();
        Horizontal row = horizontals.get(rowIndex);
        List<Integer> possibility = cell.getCellPossibilities();
        for (int i = 0; i < 9; i++) {
            int checkValue = row.getCellInHorizontal(i).getActualValue();
            if (checkValue != 0) {
                possibility.remove((Integer) checkValue);
            }
        }
        return possibility;
    }

    private List<Integer> searchColumn(Cell cell) {
        int columnIndex = cell.getJ();
        Vertical column = verticals.get(columnIndex);
        List<Integer> possibility = cell.getCellPossibilities();
        for (int i = 0; i < 9; i++) {
            int checkValue = column.getCellInVertical(i).getActualValue();
            if (checkValue != 0) {
                possibility.remove((Integer) checkValue);
            }
        }
        return possibility;
    }

    private List<Integer> searchSquare(Cell cell) {
        int rowIndex = cell.getI();
        int columnIndex = cell.getJ();
        Square square = findCorrectSquare(rowIndex, columnIndex);
        List<Integer> possibility = cell.getCellPossibilities();
        if (square == null) {
            return possibility;
        }
        for (int i = 0; i < 9; i++) {
            int checkValue = square.getCells().get(i).getActualValue();
            if (checkValue != 0) {
                possibility.remove((Integer) checkValue);
            }
        }
        return possibility;
    }

    private Square findCorrectSquare(int rowIndex, int columnIndex) {

        return squares.get((rowIndex/3)*3 + columnIndex/3);

    }

    public int findHiddenSingleInCell(Cell cell) {
        int indexI = cell.getI();
        int indexJ = cell.getJ();
        Square square = findCorrectSquare(indexI, indexJ);
        List<Integer> cellPossibilities = cell.getCellPossibilities();

        Util util = new Util();
        Map<Integer, Integer> horizMap = util.amountOfParticularPossibilities(horizontals.get(indexI));
        Map<Integer, Integer> vericalMap = util.amountOfParticularPossibilities(verticals.get(indexJ));
        Map<Integer, Integer> squareMap = util.amountOfParticularPossibilities(square);

        for (int cellPosibility : cellPossibilities) {
            if ((horizMap.containsKey(cellPosibility) && horizMap.get(cellPosibility) == 1) ||
                    (vericalMap.containsKey(cellPosibility) && vericalMap.get(cellPosibility) == 1) ||
                    (squareMap.containsKey(cellPosibility) && squareMap.get(cellPosibility) == 1))
            {
                cell.setActualValue(cellPosibility); // nastavenie na hodnotu a zrusenie poss pre tuto bunku
                // vymazanie tejto hodnoty z possibilities v riadku, stlpci, stvorci
                removePossibilityFromRow(cellPosibility, cell);
                removePossibilityFromColumn(cellPosibility, cell);
                removePossibilityFromSquare(cellPosibility, square);
                return cell.getActualValue();

            }

        }
        return 0;
    }

    public boolean pointingPairInCells(Cell cell) {
        int cellI = cell.getI();
        int cellJ = cell.getJ();
        Square cellSquare = findCorrectSquare(cellI, cellJ);
        List<Cell> eligiblePartnerCells = new ArrayList<>();
        boolean changed = false;

        for (int possibilityToCheck : cell.getCellPossibilities()) {
            eligiblePartnerCells = findPartnerCell(cell, possibilityToCheck);

            for (Cell partnerCell : eligiblePartnerCells) {
                boolean changedInLoop = false;

                // =============== len vypisy ========================================
                System.out.println("Partner cell for cell possibility: " + possibilityToCheck + " in cell  i = " + cellI + " j = " + cellJ + " IS: i = " +
                        partnerCell.getI() + " j = " + partnerCell.getJ());
                if (partnerCell == null) {
                    System.out.println("No eligible partner cell for cell possibility: " + possibilityToCheck + " in cell  i = " + cellI + " j = " + cellJ);
                }
                // ====================================================================

                if (!partnerCell.getCellPossibilities().contains((Integer)possibilityToCheck)) {
                    continue;
                }

                if (!isPossibilityToCheckPresentSomewhereElseInSquare(cell, partnerCell, cellSquare.getCells(), possibilityToCheck)) {
                    System.out.println(ANSI_GREEN + "Possibility " + possibilityToCheck + " presents only in row / column" + ANSI_RESET);
                    // ak je len v tomto riadku vyhadzem tu possibilitu z tohto riadka v ostatnych stvorcoch, ak v stlpci tak zo stlpca
                    changedInLoop = deletePossibilitiesInRowOrColumn(cell, partnerCell, possibilityToCheck);
                } else { // nachadza sa aj inde vo stvorci
                    System.out.println(ANSI_RED + "Possibility " + possibilityToCheck + " presents somewhere else too" + ANSI_RESET);
                    if (cellI == partnerCell.getI() && !isPossibilityToCheckPresentSomewhereElseInRow(cell, partnerCell, possibilityToCheck)) {
                        changedInLoop = deletePossibilitiesInSquare(cell, partnerCell, possibilityToCheck);
                    } else if (cellJ == partnerCell.getJ() && !isPossibilityToCheckPresentSomewhereElseInColumn(cell, partnerCell, possibilityToCheck)) {
                        changedInLoop = deletePossibilitiesInSquare(cell, partnerCell, possibilityToCheck);
                    }
                }

                System.out.println();
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
        Square targetSquare = findCorrectSquare(indexI, indexJ);
        List<Cell> eligiblePartnerCellList = new ArrayList<>();

        for (Cell candidateCell : targetSquare.getCells()) {

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
            System.out.println("i case");
            for (Cell testedCell : squareOfCells) {
                if (testedCell.getActualValue() == 0 && testedCell.getI() != cellI && testedCell.getCellPossibilities().contains((Integer) possibilityToCheck)) {
                    return true;
                }
            }
        }
        if (cellJ == partnerCellJ) {
            System.out.println("j case");
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
        Square cellSquare = findCorrectSquare(cellI, cellJ);

        if (cellI == partnerCellI) {
            System.out.println("horizontal i case");
            for (Cell testedCell : horizontals.get(cellI).getCells()) {
                if (testedCell.getActualValue() == 0 && cellSquare != findCorrectSquare(testedCell.getI(), testedCell.getJ()) &&
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
        Square cellSquare = findCorrectSquare(cellI, cellJ);

        if (cellJ == partnerCellJ) {
            System.out.println("vertical j case");
            for (Cell testedCell : verticals.get(cellJ).getCells()) {
                if (testedCell.getActualValue() == 0 && cellSquare != findCorrectSquare(testedCell.getI(), testedCell.getJ()) &&
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
        Square cellSquare = findCorrectSquare(cellI, cellJ);

        if (cellI == partnerCellI) {
            for (Cell testedCell : cellSquare.getCells()) {
                if (testedCell.getActualValue() == 0 && testedCell.getI() != cellI && testedCell.getCellPossibilities().contains((Integer)possibilityToCheck)) {
                    System.out.println(ANSI_PURPLE + "\t SQUARE CASE: Possibility " + possibilityToCheck + " will be removed from " +
                            "i=" + testedCell.getI() + " j=" + testedCell.getJ() + ANSI_RESET);
                    somethingWasRemoved = testedCell.getCellPossibilities().remove((Integer)possibilityToCheck);
                }
            }
        } else if (cellJ == partnerCellJ) {
            for (Cell testedCell : cellSquare.getCells()) {
                if (testedCell.getActualValue() == 0 && testedCell.getJ() != cellJ && testedCell.getCellPossibilities().contains((Integer)possibilityToCheck)) {
                    System.out.println(ANSI_PURPLE + "\t SQUARE CASE: Possibility " + possibilityToCheck + " will be removed from " +
                            "i=" + testedCell.getI() + " j=" + testedCell.getJ() + ANSI_RESET);
                    somethingWasRemoved = testedCell.getCellPossibilities().remove((Integer)possibilityToCheck);
                }
            }
        } else {
            System.out.println(ANSI_RED + "\tSQUARE CASE: Something's wrong - incorrect partner cell!" + ANSI_RESET);
        }
        return somethingWasRemoved;
    }

    private boolean deletePossibilitiesInRowOrColumn(Cell cell, Cell partnerCell, int possibilityToCheck) {
        int cellI = cell.getI();
        int cellJ = cell.getJ();
        boolean somethingWasRemoved = false;
        Square cellSquare = findCorrectSquare(cell.getI(), cell.getJ());

        if (cellI == partnerCell.getI()) {
            for (Cell testedCell : horizontals.get(cellI).getCells()) {
                Square testedCellSquare = findCorrectSquare(testedCell.getI(), testedCell.getJ());
                if (testedCell.getActualValue() == 0 && cellSquare != testedCellSquare && testedCell.getCellPossibilities().contains((Integer)possibilityToCheck)) {
                    System.out.println(ANSI_BLUE + "\tROW-COLUMN CASE: Possibility " + possibilityToCheck + " will be removed from " +
                            "i=" + testedCell.getI() + " j=" + testedCell.getJ() + ANSI_RESET);
                    somethingWasRemoved = testedCell.getCellPossibilities().remove((Integer)possibilityToCheck);
                }
            }

        } else if (cellJ == partnerCell.getJ()){
            for (Cell testedCell : verticals.get(cellJ).getCells()) {
                Square testedCellSquare = findCorrectSquare(testedCell.getI(), testedCell.getJ());
                if (testedCell.getActualValue() == 0 && cellSquare != testedCellSquare && testedCell.getCellPossibilities().contains((Integer)possibilityToCheck)) {
                    System.out.println(ANSI_BLUE + "\tROW-COLUMN CASE: Possibility " + possibilityToCheck + " will be removed from " +
                            "i=" + testedCell.getI() + " j=" + testedCell.getJ() + ANSI_RESET);
                    somethingWasRemoved = testedCell.getCellPossibilities().remove((Integer)possibilityToCheck);
                }
            }
        } else {
            System.out.println(ANSI_RED + "\tROW-COLUMN CASE: Something's wrong - incorrect partner cell!" + ANSI_RESET);
        }
        return somethingWasRemoved;
    }

    public void removePossibilityFromRow(int value, Cell cell) {
        int indexI = cell.getI();
        for (int i = 0; i < 9; i++) {
            List<Integer> p = horizontals.get(indexI).getCells().get(i).getCellPossibilities();
            if (p != null) {
                horizontals.get(indexI).getCells().get(i).getCellPossibilities().remove((Integer) value);
            }
        }
    }

    public void removePossibilityFromColumn(int value, Cell cell) {
        int indexJ = cell.getJ();
        for (int i = 0; i < 9; i++) {
            List<Integer> p = verticals.get(indexJ).getCells().get(i).getCellPossibilities();
            if (p != null) {
                p.remove((Integer) value);
            }
        }
    }

    public void removePossibilityFromSquare(int value, Square square) {
        for (int i = 0; i < 9; i++) {
            List<Integer> p = square.getCells().get(i).getCellPossibilities();
            if (p != null) {
                p.remove((Integer) value);
            }
        }
    }
}