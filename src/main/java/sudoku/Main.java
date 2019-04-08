package sudoku;

import sudoku.model.*;
import sudoku.processing.Solution;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public List<Vertical> verticals = new ArrayList<>();
    public List<Horizontal> horizontals = new ArrayList<>();
    public List<Square> squares = new ArrayList<>();

    static ClassLoader classLoader = new Main().getClass().getClassLoader();

    public static final String path1 = new File(classLoader.getResource("inputs/simple1.txt").getFile()).getPath();
    public static final String path2 = new File(classLoader.getResource("inputs/simple2.txt").getFile()).getPath();
    public static final String path3 = new File(classLoader.getResource("inputs/simple3.txt").getFile()).getPath();
    public static final String path4 = new File(classLoader.getResource("inputs/simple4.txt").getFile()).getPath();

    public static void main(String[] args) {

        Main main = new Main();
//        int[][] data = main.readSudokuMatrix(path1);

        int[][] data =
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
        //TAZKE SUDOKU test 1 - to do - OK DONE, DOKAZAL VYRIESIT
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
        //TAZKE SUDOKU test 2 - to do - OK DONE, DOKAZAL VYRIESIT
//        {
//            {9, 1, 0, 6, 0, 0, 3, 0, 0},
//            {0, 0, 4, 0, 3, 8, 0, 9, 0},
//            {0, 0, 0, 0, 0, 0, 0, 2, 6},
//            {4, 0, 0, 0, 0, 0, 2, 0, 7},
//            {0, 0, 0, 0, 8, 0, 0, 0, 0},
//            {8, 0, 2, 0, 0, 0, 0, 0, 9},
//            {5, 8, 0, 0, 0, 0, 0, 0, 0},
//            {0, 4, 0, 5, 1, 0, 8, 0, 0},
//            {0, 0, 9, 0, 0, 3, 0, 7, 2}
//        };
        // TAZKE SUDOKU test 3 - to do - OK DONE, DOKAZAL VYRIESIT
//        {
//            {0, 5, 0, 0, 4, 0, 1, 3, 0},
//            {0, 4, 0, 0, 0, 9, 0, 6, 8},
//            {2, 0, 8, 0, 0, 3, 0, 0, 4},
//            {0, 0, 0, 0, 0, 0, 2, 8, 0},
//            {8, 0, 0, 0, 0, 0, 0, 0, 1},
//            {0, 3, 5, 0, 0, 0, 0, 0, 0},
//            {5, 0, 0, 4, 0, 0, 9, 0, 7},
//            {7, 6, 0, 9, 0, 0, 0, 1, 0},
//            {0, 2, 9, 0, 8, 0, 0, 5, 0}
//        };
        // TAZKE SUDOKU test 4 - to do - OK DONE, DOKAZAL VYRIESIT
        {
            {0, 5, 7, 0, 2, 0, 4, 8, 9},
            {0, 0, 6, 8, 0, 0, 0, 0, 5},
            {0, 0, 9, 5, 0, 0, 0, 0, 0},
            {5, 0, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 0, 7, 0, 0, 0, 0},
            {0, 0, 0, 3, 0, 0, 0, 0, 7},
            {0, 0, 0, 0, 0, 8, 9, 0, 0},
            {8, 0, 0, 0, 0, 2, 3, 0, 0},
            {7, 2, 3, 0, 9, 0, 8, 1, 0}
        };
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
            // v pripade new FileReader(file) = null => bufferedReader da NullPointerException =>
            // nevytvori sa a netreba robit bufferedReader.close()
            try {
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
            } finally {
                bufferedReader.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return data;
    }

    public void menu() {

        Scanner scanner = new Scanner(System.in);
        boolean quit = false;
        do {
            int option = scanner.nextInt();
            switch (option) {
                case 0:
                    printHelp();
                    break;
                case 1:
                    insertYourOwnSudoku();
                    break;
                case 2:
                    runDefaultSudoku();
                    break;
                case 3:
                    quit = true;
            }
        } while(!quit);
    }

    private void printHelp() {
        System.out.println(
                        "Insert 0 - to print this help\n" +
                        "Insert 1 - to be able to write your own sudoku to check\n" +
                        "         - empty places in sudoku reaplace with number 0\n" +
                        "Insert 2 - to see som example solutions of sudokus\n" +
                        "Insert 3 - to quit program"
        );
    }

    private void insertYourOwnSudoku() {

    }

    private void runDefaultSudoku() {

    }
}
