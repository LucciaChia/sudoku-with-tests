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
        boolean sudokuUpdated;
        boolean triedMedium = false;
        do {
            Possibility possibility;
            sudokuUpdated = false;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {



                    if (data[i][j] == 0) {
                        //Possibility possibility = new Possibility(i, j);
                        possibility = new Possibility(i, j);

                        // TEST
                        if (i == 7 && j == 0) {
                            // big test of setting a value
//                            System.out.println(" i = 7 , j = 0");
                        }

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

                            // Medium technique - to test
//                            if (possibility.getPosibilities().size() == 2) { // good enough for primitive medium cases
//                                System.out.println("MEDIUM");
//                                Cell testedCell = horizontals.get(i).getCellInHorizontal(j);
//                            //     if (testedCell.getActualValue() == 0) {
//                                    pointingPairInCells(horizontals.get(i).getCellInHorizontal(j));
//                            //     }
//
//                            }

                            // ked si v koncoch skus Medium approach
                            if (sudokuUpdated || firstRound <= 2) {
                            //if (!sudokuUpdated) {
                                // skus medium
//                                if (possibility.getPosibilities().size() == 2) { // good enough for primitive medium cases
//                                    System.out.println("MEDIUM");
//                                    Cell testedCell = horizontals.get(i).getCellInHorizontal(j);
//                                    //     if (testedCell.getActualValue() == 0) {
//                                    pointingPairInCells(horizontals.get(i).getCellInHorizontal(j));
//                                    //     }
//
//                                }
                                System.out.println("MEDIUM");

                                //pointingPairInCells(horizontals.get(i).getCellInHorizontal(j));

                                pointingPairInCellsAdvanced(horizontals.get(i).getCellInHorizontal(j));
                            //    sudokuUpdated = false;
                                triedMedium = true;
                            }
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
            if (columnIndex >= 3 && columnIndex < 6) {
                return squares.get(1);
            }
            if (columnIndex >= 6 && columnIndex < 9) {
                return squares.get(2);
            }
        }
        if (rowIndex >= 3 && rowIndex < 6) {
            if (columnIndex < 3) {
                return squares.get(3);
            }
            if (columnIndex >= 3 && columnIndex < 6) {
                return squares.get(4);
            }
            if (columnIndex >= 6 && columnIndex < 9) {
                return squares.get(5);
            }
        }
        if (rowIndex >= 6 && rowIndex < 9) {
            if (columnIndex < 3) {
                return squares.get(6);
            }
            if (columnIndex >= 3 && columnIndex < 6) {
                return squares.get(7);
            }
            if (columnIndex >= 6 && columnIndex < 9) {
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
                cell.setActualValue(cellPosibility); // nastavenie na hodnotu a zrusenie poss pre tuto bunku --- nieco je tu zle
                cell.setCellPossibilities(null);
                // vymazanie tejto hodnoty z possibilities v riadku, stlpci, stvorci
                removePossibilityFromRow(cellPosibility, cell);
                removePossibilityFromColumn(cellPosibility, cell);
                removePossibilityFromSquare(cellPosibility, square);
                return cell.getActualValue();

            }

        }
        return 0;
    }

    @TODO // OK
    public void pointingPairInCells(Cell cell) {
        int cellI = cell.getI();
        int cellJ = cell.getJ();
        Square cellSquare = findCorrectSquare(cellI, cellJ);
        // numbers to remove:
        int removeFirst = cell.getCellPossibilities().getPosibilities().get(0);
        int removeSecond = cell.getCellPossibilities().getPosibilities().get(1);

        // get pair cell
        System.out.println(cell);
        Cell partnerCell = findPartnerCell(cell);

        if (partnerCell == null) {
            return;
        }

        System.out.println("cell: " + cell + " cellPos: " + cell.getCellPossibilities()
                + " partner cell: " + partnerCell + " partnerPos: " + partnerCell.getCellPossibilities());

        if (cellI == partnerCell.getI()) {
            // check horizontal
            for (Cell cellInRow : horizontals.get(cellI).getColumn()) {
                int i = cellInRow.getI();
                int j = cellInRow.getJ();
                Square testedCellSquare = findCorrectSquare(i,j);
                if (cellSquare != testedCellSquare && cellInRow.getActualValue() == 0) {
                    cellInRow.getCellPossibilities().getPosibilities().remove((Integer)removeFirst);
                    cellInRow.getCellPossibilities().getPosibilities().remove((Integer)removeSecond);
                }
            }


        } else if (cellJ == partnerCell.getJ()) {
            // check vertical
            for (Cell cellInColumn : verticals.get(cellJ).getRow()) {
                int i = cellInColumn.getI();
                int j = cellInColumn.getJ();
                Square testedCellSquare = findCorrectSquare(i,j);
                if (cellSquare != testedCellSquare && cellInColumn.getActualValue() == 0) {
                    cellInColumn.getCellPossibilities().getPosibilities().remove((Integer)removeFirst);
                    cellInColumn.getCellPossibilities().getPosibilities().remove((Integer)removeSecond);
                }
            }

        }

        // check square
        for (Cell cellInSquare : cellSquare.getcellsInSquare()) {
            if (cellInSquare != cell && cellInSquare != partnerCell && cellInSquare.getActualValue() == 0) {
                cellInSquare.getCellPossibilities().getPosibilities().remove((Integer)removeFirst);
                cellInSquare.getCellPossibilities().getPosibilities().remove((Integer)removeSecond);
            }
        }
    }

    //TODO
    private Cell findPartnerCell(Cell cell) { // OK
        int indexI = cell.getI();
        int indexJ = cell.getJ();
        Square targetSquare = findCorrectSquare(indexI, indexJ);
        ArrayList<Integer> cellPosibilities = cell.getCellPossibilities().getPosibilities();

        for (Cell candidateCell : targetSquare.getcellsInSquare()) {

            if (candidateCell.getActualValue() == 0 ) {
                ArrayList<Integer> candidatePosibilities = candidateCell.getCellPossibilities().getPosibilities();
                if (cellPosibilities.equals(candidatePosibilities) && cell != candidateCell) {
                    int i = candidateCell.getI();
                    int j = candidateCell.getJ();
                    if (i == indexI || j == indexJ) {
                        return candidateCell;
                    }
                }
            }
        }

        return null;
    }

    //TODO // OK
    public void pointingPairInCellsAdvanced(Cell cell) {
        int cellI = cell.getI();
        int cellJ = cell.getJ();
        Square cellSquare = findCorrectSquare(cellI, cellJ);

        // actual number to remove:
        if (cell.getActualValue() != 0) {
            return;
        }


        for (int possibilityToCheck : cell.getCellPossibilities().getPosibilities()) {
            boolean changesDone = false; // ?????????
            boolean checkOnlySquare = checkOnlySquare(cellSquare, possibilityToCheck);

            int numberToPotencialyRemove = possibilityToCheck;

            // get partner cell
            System.out.println(cell);
            Cell partnerCell = findPartnerCellAdvanced(cell, numberToPotencialyRemove);

            if (partnerCell == null) {
                return;
            }

            System.out.println("cell: " + cell + " cellPos: " + cell.getCellPossibilities()
                    + " partner cell: " + partnerCell + " partnerPos: " + partnerCell.getCellPossibilities());

            // TODO - check if I really can remove from a square
            // horizontal case
            if (cellI == partnerCell.getI() && !checkOnlySquare) {

            }

            // vertical case

            // ----


            if (cellI == partnerCell.getI() && !checkOnlySquare) {
                // check horizontal
                for (Cell cellInRow : horizontals.get(cellI).getColumn()) {
                    int i = cellInRow.getI();
                    int j = cellInRow.getJ();
                    Square testedCellSquare = findCorrectSquare(i, j);
                    if (cellSquare != testedCellSquare && cellInRow.getActualValue() == 0) {
                        cellInRow.getCellPossibilities().getPosibilities().remove((Integer) numberToPotencialyRemove);
                        changesDone = true;
                    }
                }


            } else if (cellJ == partnerCell.getJ() && !checkOnlySquare) {
                // check vertical
                for (Cell cellInColumn : verticals.get(cellJ).getRow()) {
                    int i = cellInColumn.getI();
                    int j = cellInColumn.getJ();
                    Square testedCellSquare = findCorrectSquare(i, j);
                    if (cellSquare != testedCellSquare && cellInColumn.getActualValue() == 0) {
                        cellInColumn.getCellPossibilities().getPosibilities().remove((Integer) numberToPotencialyRemove);
                        System.out.println("Cell REMOVAL");
                        changesDone = true;
                    }
                }
            }


            // check square
            if (checkOnlySquare) {
                for (Cell cellInSquare : cellSquare.getcellsInSquare()) {

                    int iCase = 0;
                    int jCase = 0;
                    if (cellI == partnerCell.getI()) {
                        iCase = cellI;
                    }
                    if (cellJ == partnerCell.getJ()) {
                        jCase = cellJ;
                    }

                    if (cellInSquare != cell &&
                            cellInSquare != partnerCell &&
                            cellInSquare.getActualValue() == 0 &&
                            (iCase != cellInSquare.getI())) {
                        cellInSquare.getCellPossibilities().getPosibilities().remove((Integer) numberToPotencialyRemove);
                        System.out.println("SOMETHING REMOVED");
                    }

                    if (cellInSquare != cell &&
                            cellInSquare != partnerCell &&
                            cellInSquare.getActualValue() == 0 &&
                            (jCase != cellInSquare.getJ())) {
                        cellInSquare.getCellPossibilities().getPosibilities().remove((Integer) numberToPotencialyRemove);
                        System.out.println("SOMETHING REMOVED");
                    }
                }
            }

        }
    }

    private boolean checkOnlySquare(Square cellsSquare, int possibility) {
        int amountOfCheckedPossibilityInSquare = 0;
        for (Cell cell : cellsSquare.getcellsInSquare()) {
            if (cell.getActualValue() == 0 && cell.getCellPossibilities().getPosibilities().contains((Integer)possibility)) {
                amountOfCheckedPossibilityInSquare++;
            }
        }
        if (amountOfCheckedPossibilityInSquare > 2) {
            return true;
        } else {
            return false;
        }
    }

    @TODO
    private Cell findPartnerCellAdvanced(Cell cell, int possibilityToCheck) { // TEST IT!
        int indexI = cell.getI();
        int indexJ = cell.getJ();
        Square targetSquare = findCorrectSquare(indexI, indexJ);
        ArrayList<Integer> cellPosibilities = cell.getCellPossibilities().getPosibilities();

        for (Cell candidateCell : targetSquare.getcellsInSquare()) {

            if (candidateCell.getActualValue() == 0 ) {
                int candidateCellI = candidateCell.getI();
                int candidateCellJ = candidateCell.getJ();

                if ((indexI == candidateCellI || indexJ == candidateCellJ) && cell != candidateCell) {
                    ArrayList<Integer> candidatePosibilities = candidateCell.getCellPossibilities().getPosibilities();

                    if (candidatePosibilities.contains((Integer)possibilityToCheck)) {
                        return candidateCell;
                    }

//                    for (int cellPos : cellPosibilities) {
//                        for (int i = 0; i < candidatePosibilities.size(); i++) {
//                            if (cellPos == candidatePosibilities.get(i)) {
//                                return candidateCell;
//                            }
//                        }
//                    }
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