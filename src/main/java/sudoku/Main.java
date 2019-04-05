package sudoku;

import sudoku.model.*;
import sudoku.processing.Solution;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public List<Vertical> verticals = new ArrayList<>();
    public List<Horizontal> horizontals = new ArrayList<>();
    public List<Square> squares = new ArrayList<>();
    public static String path1 = "C:\\Users\\lukr\\IdeaProjects\\sudoku-with-tests\\src\\main\\\\resources\\inputs\\simple1.txt";
    public static String path2 = "C:\\Users\\lukr\\IdeaProjects\\sudoku-with-tests\\src\\main\\\\resources\\inputs\\simple2.txt";
    public static String path3 = "C:\\Users\\lukr\\IdeaProjects\\sudoku-with-tests\\src\\main\\\\resources\\inputs\\simple3.txt";
    public static String path4 = "C:\\Users\\lukr\\IdeaProjects\\sudoku-with-tests\\src\\main\\\\resources\\inputs\\simple4.txt";


    public static void main(String[] args) {
        Main main = new Main();

        int[][] data = main.readSudokuMatrix(path1);

//        int[][] data =
//                {
//                        {0, 3, 0, 4, 1, 0, 0, 2, 0},
//                        {5, 6, 0, 0, 0, 0, 0, 3, 0},
//                        {8, 0, 0, 0, 0, 7, 0, 0, 0},
//                        {7, 5, 6, 0, 0, 9, 0, 0, 0},
//                        {0, 1, 0, 0, 0, 0, 0, 6, 0},
//                        {0, 0, 0, 6, 0, 0, 3, 5, 7},
//                        {0, 0, 0, 5, 0, 0, 0, 0, 8},
//                        {0, 9, 0, 0, 0, 0, 0, 1, 2},
//                        {0, 7, 0, 0, 8, 2, 0, 9, 0}
//                };

                // JEDNODUCHE SUDOKU test 2
//                {
//                        {1, 0, 6, 2, 0, 0, 0, 0, 0},
//                        {7, 0, 0, 1, 0, 0, 0, 8, 0},
//                        {0, 0, 3, 5, 0, 6, 4, 0, 1},
//                        {0, 6, 1, 0, 0, 0, 0, 0, 8},
//                        {2, 0, 0, 0, 0, 0, 0, 0, 4},
//                        {4, 0, 0, 0, 0, 0, 1, 2, 0},
//                        {8, 0, 5, 3, 0, 2, 9, 0, 0},
//                        {0, 1, 0, 0, 0, 9, 0, 0, 5},
//                        {0, 0, 0, 0, 0, 1, 8, 0, 2}
//                };

                //JEDNODUCHE SUDOKU test 3
//                {
//                        {2, 9, 6, 0, 0, 0, 0, 5, 0},
//                        {5, 1, 0, 9, 3, 8, 0, 0, 0},
//                        {0, 0, 0, 0, 0, 0, 0, 0, 0},
//                        {0, 0, 4, 2, 0, 0, 0, 0, 0},
//                        {0, 0, 5, 7, 0, 9, 8, 0, 0},
//                        {0, 0, 0, 0, 0, 4, 1, 0, 0},
//                        {0, 0, 0, 0, 0, 0, 0, 0, 0},
//                        {0, 0, 0, 8, 1, 2, 0, 6, 7},
//                        {0, 6, 0, 0, 0, 0, 9, 1, 2}
//                };

        //JEDNODUCHE SUDOKU test 4
//        {
//            {0, 0, 4, 0, 1, 2, 0, 6, 0},
//            {0, 3, 7, 0, 0, 0, 0, 9, 0},
//            {9, 0, 0, 0, 0, 4, 0, 0, 8},
//            {0, 0, 0, 3, 0, 0, 0, 0, 1},
//            {6, 0, 0, 0, 8, 0, 0, 0, 3},
//            {3, 0, 0, 0, 0, 9, 0, 0, 0},
//            {7, 0, 0, 2, 0, 0, 0, 0, 4},
//            {0, 8, 0, 0, 0, 0, 7, 2, 0},
//            {0, 1, 0, 6, 7, 0, 5, 0, 0}
//        };
        //TAZKE SUDOKU test 1 - to do
//        {
//            {0, 0, 0, 2, 0, 1, 4, 3, 0},
//            {1, 0, 2, 7, 0, 0, 0, 6, 5},
//            {0, 0, 0, 0, 0, 5, 0, 0, 1},
//            {0, 7, 9, 0, 0, 0, 0, 0, 6},
//            {0, 0, 6, 0, 0, 0, 1, 0, 0},
//            {3, 0, 0, 0, 0, 0, 7, 4, 0},
//            {8, 0, 0, 3, 0, 0, 0, 0, 0},
//            {7, 1, 0, 0, 0, 4, 9, 0, 8},
//            {0, 6, 4, 8, 0, 9, 0, 0, 0}
//        };
        printIt(data);

        // create necessary objects
        main.createSudokuElementObjects(data);
        main.resolveSudoku(data);
        System.out.println("FINAL SOLUTION:");
        printIt(data);


    }
    private static void printIt(int[][] data){
        for (int i=0; i<data.length; i++) {
            for (int j=0; j<data[i].length; j++) {
                System.out.print(data[i][j] + " ");
            }
            System.out.println();
        }
    }

    private void resolveSudoku(int[][] data) {
        System.out.println("POSSIBILITIES: ");
        Solution solution = new Solution(verticals, horizontals, squares, data);
        List<Possibility> pos = solution.output();

        System.out.println(pos.size());
        for (int i = 0; i < pos.size(); i++) {
            System.out.print(i + ": ");
            System.out.println(pos.get(i).toString());
        }
    }

    public ArrayList<List<? extends SudokuElement>> createSudokuElementObjects(int[][] data) {
        for (int i = 0; i < data.length; i++) {
            Horizontal horizontal = new Horizontal();
            for (int j = 0; j < data[i].length; j++) {
                Cell cell = new Cell(data[i][j], i, j);
                horizontal.getColumn().add(cell);

                if (i == 0) {
                    Vertical vertical = new Vertical();
                    verticals.add(vertical);
                    verticals.get(j).getRow().add(cell);
                } else {
                    verticals.get(j).getRow().add(cell);

                }

                if ((i % 3 == 0 || i % 3 == 3) && (j % 3 == 0 || j % 3 == 3)) {
                    Square square = new Square();
                    squares.add(square);
                }

                squares.get((i/3)*3 + j/3).getcellsInSquare().add(cell);

            }
            horizontals.add(horizontal);
        }
        // <? extends sudoku.model.SudokuElement> pre vsetky objekty, ktore dedia od sudoku.model.SudokuElement
        ArrayList<List<? extends SudokuElement>> sudokuElements = new ArrayList<>();
        sudokuElements.add(horizontals);
        sudokuElements.add(verticals);
        sudokuElements.add(squares);
        return sudokuElements;
    }

    public int[][] readSudokuMatrix(String path) {
        int[][] data = new int[9][9];
        try {
            File file = new File(path);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String st;
            String[] row;
            int i = 0;
            while ((st = bufferedReader.readLine()) != null) {
                st = st.trim();
                row = st.split(" ");
                for (int j = 0; j < row.length; j++) {
                    data[i][j] = Integer.valueOf(row[j]);
                }
                i++;
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }
}
