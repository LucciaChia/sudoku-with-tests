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
        boolean end = false;
        int endCount = 0;
        int firstRound = 1;
        boolean sudokuUpdated;
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

                            if (end && !possibilityList.isEmpty()) {
                                System.out.println("Not updated, try something more advanced > i = " + i + " j = " + j);
                                if (pointingPairInCells(horizontals.get(i).getCellInHorizontal(j))) {
                                    sudokuUpdated = true;
                                    endCount--; // toto este overit!
                                }
                            }
                        }
                    }
                }
            }


            // skus advanced ak plati toto ===============
            if (!sudokuUpdated) {
                end = true;
                System.out.println("Koniec");
            }
            if (end == true && !possibilityList.isEmpty() && endCount < 1) {
                System.out.println("Som na konci, ale mam este nieco v possibility liste -> skus advanced metody");
                sudokuUpdated = true;
                endCount++;
            }
            // ===========================================
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

        return squares.get((rowIndex/3)*3 + columnIndex/3);

    }

    public int findHiddenSingleInCell(Cell cell) {
        int indexI = cell.getI();
        int indexJ = cell.getJ();
        Square square = findCorrectSquare(indexI, indexJ);
        List<Integer> cellPossibilities = cell.getCellPossibilities().getPosibilities();

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

    //TODO for more advacned sudokus
    public boolean pointingPairInCells(Cell cell) {
        int cellI = cell.getI();
        int cellJ = cell.getJ();
        Square cellSquare = findCorrectSquare(cellI, cellJ);
        List<Cell> eligiblePartnerCells = new ArrayList<>();

        for (int possibilityToCheck : cell.getCellPossibilities().getPosibilities()) {
            eligiblePartnerCells = findPartnerCell(cell, possibilityToCheck);
            for (Cell partnerCell : eligiblePartnerCells) {
                System.out.println("Partner cell for cell possibility: " + possibilityToCheck + " in cell  i = " + cellI + " j = " + cellJ + " IS: i = " +
                        partnerCell.getI() + " j = " + partnerCell.getJ());
                if (partnerCell == null) {
                    System.out.println("No eligible partner cell for cell possibility: " + possibilityToCheck + " in cell  i = " + cellI + " j = " + cellJ);
                }
            }
        }

        // check vertical

        // check horizontal

        // ZATIAL VRAT FALSE, POTOM SA BUDE VRACAT TRUE, AK SA NAJDE VHODNA CELLA A NIECO SA UPDATNE
        return false;
    }

    //TODO - logic completed - DO test for multiple sudokus while used in pointingPairCells(Cell cell) method
    private List<Cell> findPartnerCell(Cell cell, int possibilityToCheck) {
        int indexI = cell.getI();
        int indexJ = cell.getJ();
        Square targetSquare = findCorrectSquare(indexI, indexJ);
        ArrayList<Integer> cellPosibilities = cell.getCellPossibilities().getPosibilities();
        List<Cell> eligiblePartnerCellList = new ArrayList<>();

        for (Cell candidateCell : targetSquare.getcellsInSquare()) {

            if (candidateCell.getActualValue() == 0 ) {
                int candidateCellI = candidateCell.getI();
                int candidateCellJ = candidateCell.getJ();

                if ((indexI == candidateCellI || indexJ == candidateCellJ) && cell != candidateCell) {
                    ArrayList<Integer> candidatePosibilities = candidateCell.getCellPossibilities().getPosibilities();

                    if (candidatePosibilities.contains((Integer)possibilityToCheck)) {
                        eligiblePartnerCellList.add(candidateCell);
                    }
                }
            }
        }

        return eligiblePartnerCellList;
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