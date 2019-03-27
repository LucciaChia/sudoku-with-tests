package sudoku.processing;

import sudoku.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Solution {
    List<Vertical> verticals;
    List<Horizontal> horizontals;
    List<Square> squares;
    int[][] data;
    List<Possibility> possibilityList = new ArrayList<>();

    public Solution(List<Vertical> verticals, List<Horizontal> horizontals, List<Square> squares, int[][] data) {
        this.verticals = verticals;
        this.horizontals = horizontals;
        this.squares = squares;
        this.data = data;
    }

    public List<Possibility> output() {
        int firstRound = 1;
        boolean sudokuUpdated = false; // FIXME this is redundant initialization
        do {
            sudokuUpdated = false;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (data[i][j] == 0) {
                        Possibility possibility = new Possibility(i, j);
                        if (firstRound == 1) {
                            possibilityList.add(possibility);
                            horizontals.get(i).getCellInHorizontal(j).setCellPossibilities(possibility);
                        }
                        if (firstRound > 1) {
                            possibility = horizontals.get(i).getCellInHorizontal(j).getCellPossibilities();
                        }

                        searchRow(possibility);
                        searchColumn(possibility);
                        searchSquare(possibility);

                        if (possibility.getPosibilities().size() == 1) {
                            horizontals.get(i).getCellInHorizontal(j).setActualValue(possibility.getPosibilities().get(0));
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

                            // Advanced technique - to test
//                            if (possibility.getPosibilities().size() == 2) { // think about this cond. one more time
//                                pointingPairInCells(horizontals.get(i).getCellInHorizontal(j));
//                            }


//                            System.out.println("TEST" + firstRound);
//                            for (int m = 0; m < data.length; m++) {
//                                for (int n = 0; n < data[m].length; n++) {
//                                    System.out.print(data[m][n] + " ");
//                                }
//                                System.out.println();
//                            }
                        }
                    }
                }
            }


            firstRound++;
    } while (sudokuUpdated || firstRound <= 2);

        return possibilityList;
    }

    private Possibility searchRow(Possibility possibility) { // odoberanie potencialnych moznosti z ciell v riadku
        int rowIndex = possibility.getI();
        Horizontal row = horizontals.get(rowIndex);
        for (int i = 0; i < 9; i++) {
            int checkValue = row.getCellInHorizontal(i).getActualValue();
            if (checkValue != 0) {
                possibility.getPosibilities().remove((Integer) checkValue);
            }
        }
        return possibility;
    }

    private Possibility searchColumn(Possibility possibility) {
        int columnIndex = possibility.getJ();
        Vertical column = verticals.get(columnIndex);
        for (int i = 0; i < 9; i++) {
            int checkValue = column.getCellInVertical(i).getActualValue();
            if (checkValue != 0) {
                possibility.getPosibilities().remove((Integer) checkValue);
            }
        }
        return possibility;
    }

    private Possibility searchSquare(Possibility possibility) {
        int rowIndex = possibility.getI();
        int columnIndex = possibility.getJ();
        Square square = findCorrectSquare(rowIndex, columnIndex);
        if (square == null) {
            return possibility;
        }
        for (int i = 0; i < 9; i++) {
            int checkValue = square.getcellsInSquare().get(i).getActualValue();
            if (checkValue != 0) {
                possibility.getPosibilities().remove((Integer) checkValue);
            }
        }
        return possibility;
    }

    private Square findCorrectSquare(int rowIndex, int columnIndex) {
        if (rowIndex < 3) {
            if (columnIndex < 3) {
                return squares.get(0);
            }
            if (columnIndex >= 3 && columnIndex < 6) { // Fixme columnIndex >= 3 is still true
                return squares.get(1);
            }
            if (columnIndex >= 6 && columnIndex < 9) { // Fixme columnIndex >= 6 is still true
                return squares.get(2);
            }
        }
        if (rowIndex >= 3 && rowIndex < 6) {
            if (columnIndex < 3) {
                return squares.get(3);
            }
            if (columnIndex >= 3 && columnIndex < 6) { // Fixme columnIndex >= 3 is still true
                return squares.get(4);
            }
            if (columnIndex >= 6 && columnIndex < 9) { // Fixme columnIndex >= 6 is still true
                return squares.get(5);
            }
        }
        if (rowIndex >= 6 && rowIndex < 9) {
            if (columnIndex < 3) {
                return squares.get(6);
            }
            if (columnIndex >= 3 && columnIndex < 6) {// Fixme columnIndex >= 3 is still true
                return squares.get(7);
            }
            if (columnIndex >= 6 && columnIndex < 9) {// Fixme columnIndex >= 6 is still true
                return squares.get(8);
            }
        }
        return null;
    }


    public int findHiddenSingleInCell(Cell cell) {
        int indexI = cell.getI();
        int indexJ = cell.getJ();
        Square square = findCorrectSquare(indexI, indexJ);
        List<Integer> cellPossibilities = cell.getCellPossibilities().getPosibilities();
//Fixme old code should be removed
//        Map<Integer, Integer> horizMap = horizontals.get(indexI).amountOfParticularPossibilities();
//        Map<Integer, Integer> vericalMap = verticals.get(indexJ).amountOfParticularPossibilities();
//        Map<Integer, Integer> squareMap = square.amountOfParticularPossibilities();
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

    @TODO
    public void pointingPairInCells(Cell cell) {
        // get pair cell
        Cell partnerCell = findPartnerCell(cell);
        if (partnerCell == null) {
            return;
        }
        // check vertical

        // check horizontal
    }

    @TODO
    private Cell findPartnerCell(Cell cell) {
        int indexI = cell.getI();
        int indexJ = cell.getJ();
        Square targetSquare = findCorrectSquare(indexI, indexJ);

        for (Cell c : targetSquare.getcellsInSquare()) {
            if (c.getCellPossibilities().getPosibilities().size() == 2 && !c.equals(cell)) {
                int i = c.getI();
                int j = c.getJ();
                if (     (i == indexI || j == indexJ) &&
                        ((i == indexI+1 || i == indexI-1) || (j == indexJ+1 || j == indexJ-1))) {
                    return c;
                }
            }
        }

        return null;
    }

    public void removePossibilityFromRow(int value, Cell cell) {
        int indexI = cell.getI();

        for (int i = 0; i < 9; i++) {
            Possibility p = horizontals.get(indexI).getColumn().get(i).getCellPossibilities();
            if (p != null) {
                horizontals.get(indexI).getColumn().get(i).getCellPossibilities().getPosibilities().remove((Integer) value);

            }
        }

    }

    public void removePossibilityFromColumn(int value, Cell cell) {
        int indexJ = cell.getJ();

        for (int i = 0; i < 9; i++) {
            Possibility p = verticals.get(indexJ).getRow().get(i).getCellPossibilities();
            if (p != null) {
                p.getPosibilities().remove((Integer) value);

            }
        }


    }

    public void removePossibilityFromSquare(int value, Square square) {
        for (int i = 0; i < 9; i++) {
            Possibility p = square.getcellsInSquare().get(i).getCellPossibilities();
            if (p != null) {
                p.getPosibilities().remove((Integer) value);

            }
        }

    }
}