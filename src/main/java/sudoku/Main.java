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
    public static String path1 = "C:\\Users\\Lucia\\IdeaProjects\\sudoku-with-tests\\src\\main\\\\resources\\inputs\\simple1.txt";
    public static String path2 = "C:\\Users\\Lucia\\IdeaProjects\\sudoku-with-tests\\src\\main\\\\resources\\inputs\\simple2.txt";
    public static String path3 = "C:\\Users\\Lucia\\IdeaProjects\\sudoku-with-tests\\src\\main\\\\resources\\inputs\\simple3.txt";
    public static String path4 = "C:\\Users\\Lucia\\IdeaProjects\\sudoku-with-tests\\src\\main\\\\resources\\inputs\\simple4.txt";


    public static void main(String[] args) {
        Main main = new Main();

//        int[][] data = main.readSudokuMatrix(path1);

        int[][] data =

        //JEDNODUCHE SUDOKU test 4 ok
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

        //MEDIUM SUDOKU test 1 - to do
        {
            {9, 1, 0, 6, 0, 0, 3, 0, 0},
            {0, 0, 4, 0, 3, 8, 0, 9, 0},
            {0, 0, 0, 0, 0, 0, 0, 2, 6},
            {4, 0, 0, 0, 0, 0, 2, 0, 7},
            {0, 0, 0, 0, 8, 0, 0, 0, 0},
            {8, 0, 2, 0, 0, 0, 0, 0, 9},
            {5, 8, 0, 0, 0, 0, 0, 0, 0},
            {0, 4, 0, 5, 1, 0, 8, 0, 0},
            {0, 0, 9, 0, 0, 3, 0, 7, 2}
        };

//        {
//            {6, 0, 1, 0, 0, 7, 5, 8, 0},
//            {0, 0, 4, 0, 1, 6, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 7, 0},
//            {0, 0, 0, 0, 2, 3, 0, 5, 0},
//            {0, 0, 8, 0, 0, 0, 3, 0, 0},
//            {0, 7, 0, 4, 6, 0, 0, 0, 0},
//            {0, 1, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 1, 3, 0, 9, 0, 0},
//            {0, 5, 2, 6, 0, 0, 4, 0, 8}
//        };

        //HARD SUDOKU test 1 - to do
//        {
//            {0, 0, 0, 0, 0, 6, 0, 3, 7},
//            {0, 0, 0, 0, 0, 0, 6, 0, 4},
//            {2, 0, 0, 0, 3, 0, 0, 0, 0},
//            {8, 0, 0, 4, 0, 7, 0, 0, 2},
//            {5, 0, 6, 0, 0, 0, 4, 0, 3},
//            {4, 0, 0, 3, 0, 9, 0, 0, 5},
//            {0, 0, 0, 0, 2, 0, 0, 0, 9},
//            {6, 0, 8, 0, 0, 0, 0, 0, 0},
//            {9, 7, 0, 8, 0, 0, 0, 0, 0}
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
                if (i < 3) {
                    if (j < 3) {
                        squares.get(0).addCell(cell);
                    }
                    if (j >= 3 && j < 6) {
                        squares.get(1).getcellsInSquare().add(cell);
                    }
                    if (j >= 6 && j < 9) {
                        squares.get(2).getcellsInSquare().add(cell);
                    }
                }
                if (i >= 3 && i < 6) {
                    if (j < 3) {
                        squares.get(3).getcellsInSquare().add(cell);
                    }
                    if (j >= 3 && j < 6) {
                        squares.get(4).getcellsInSquare().add(cell);
                    }
                    if (j >= 6 && j < 9) {
                        squares.get(5).getcellsInSquare().add(cell);
                    }
                }
                if (i >= 6 && i < 9) {
                    if (j < 3) {
                        squares.get(6).getcellsInSquare().add(cell);
                    }
                    if (j >= 3 && j < 6) {
                        squares.get(7).getcellsInSquare().add(cell);
                    }
                    if (j >= 6 && j < 9) {
                        squares.get(8).getcellsInSquare().add(cell);
                    }
                }
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
