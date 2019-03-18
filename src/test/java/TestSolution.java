import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
public class TestSolution {

    //JEDNODUCHE SUDOKU test 1
    int[][] data1 =
                {
                        {0, 3, 0, 4, 1, 0, 0, 2, 0},
                        {5, 6, 0, 0, 0, 0, 0, 3, 0},
                        {8, 0, 0, 0, 0, 7, 0, 0, 0},
                        {7, 5, 6, 0, 0, 9, 0, 0, 0},
                        {0, 1, 0, 0, 0, 0, 0, 6, 0},
                        {0, 0, 0, 6, 0, 0, 3, 5, 7},
                        {0, 0, 0, 5, 0, 0, 0, 0, 8},
                        {0, 9, 0, 0, 0, 0, 0, 1, 2},
                        {0, 7, 0, 0, 8, 2, 0, 9, 0}
                };
    int[][] expected1 =
            {
                    {9, 3, 7, 4, 1, 6, 8, 2, 5},
                    {5, 6, 4, 9, 2, 8, 7, 3, 1},
                    {8, 2, 1, 3, 5, 7, 9, 4, 6},
                    {7, 5, 6, 2, 3, 9, 1, 8, 4},
                    {4, 1, 3, 8, 7, 5, 2, 6, 9},
                    {2, 8, 9, 6, 4, 1, 3, 5, 7},
                    {1, 4, 2, 5, 9, 3, 6, 7, 8},
                    {3, 9, 8, 7, 6, 4, 5, 1, 2},
                    {6, 7, 5, 1, 8, 2, 4, 9, 3}
            };

    //JEDNODUCHE SUDOKU test 4
    int[][] data4 =
                {
                        {0, 0, 4, 0, 1, 2, 0, 6, 0},
                        {0, 3, 7, 0, 0, 0, 0, 9, 0},
                        {9, 0, 0, 0, 0, 4, 0, 0, 8},
                        {0, 0, 0, 3, 0, 0, 0, 0, 1},
                        {6, 0, 0, 0, 8, 0, 0, 0, 3},
                        {3, 0, 0, 0, 0, 9, 0, 0, 0},
                        {7, 0, 0, 2, 0, 0, 0, 0, 4},
                        {0, 8, 0, 0, 0, 0, 7, 2, 0},
                        {0, 1, 0, 6, 7, 0, 5, 0, 0}
                };
    int[][] expected4 =
            {
                    {8, 5, 4, 9, 1, 2, 3, 6, 7},
                    {1, 3, 7, 8, 6, 5, 4, 9, 2},
                    {9, 2, 6, 7, 3, 4, 1, 5, 8},
                    {2, 7, 5, 3, 4, 6, 9, 8, 1},
                    {6, 9, 1, 5, 8, 7, 2, 4, 3},
                    {3, 4, 8, 1, 2, 9, 6, 7, 5},
                    {7, 6, 9, 2, 5, 3, 8, 1, 4},
                    {5, 8, 3, 4, 9, 1, 7, 2, 6},
                    {4, 1, 2, 6, 7, 8, 5, 3, 9}
            };

    @Test
    public void testOutput4() {
        Main main = new Main();
        ArrayList<List<? extends SudokuElement>> sudokuElementsList = main.createSudokuElementObjects(data4);
        List<Horizontal> horizontals = (List<Horizontal>)sudokuElementsList.get(0);
        List<Vertical> verticals = (List<Vertical>)sudokuElementsList.get(1);
        List<Square> squares = (List<Square>)sudokuElementsList.get(2);
        Solution solution = new Solution(verticals, horizontals, squares, data4);
        solution.output();
        assertArrayEquals(expected4, data4);
    }

    @Test
    public void testOutput1() {
        Main main = new Main();
        ArrayList<List<? extends SudokuElement>> sudokuElementsList = main.createSudokuElementObjects(data1);
        List<Horizontal> horizontals = (List<Horizontal>)sudokuElementsList.get(0);
        List<Vertical> verticals = (List<Vertical>)sudokuElementsList.get(1);
        List<Square> squares = (List<Square>)sudokuElementsList.get(2);
        Solution solution = new Solution(verticals, horizontals, squares, data1);
        solution.output();
        assertArrayEquals(expected1, data1);
    }

}
