package sudoku;

import sudoku.customExceptions.IllegalSudokuStateException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class newFunctTests {
    public static void main(String[] args) {
//        for (int i = 0; i < 9; i++) {
//            for (int j = 0; j < 9; j++) {
//                int box = (i/3)*3 + j/3;
//                System.out.println("[" + i + "," + j +"] > " + box);
//            }
//        }
        ClassLoader classLoader = new newFunctTests().getClass().getClassLoader();
        File file22 = new File(classLoader.getResource("inputs/simple1.txt").getFile());
        System.out.println("file22 = " + file22.getPath());


        for (int i = 0; i < 10; i++) {

            if (i == 4 || i == 6) {
                continue; // preskoci na dalsiu obratku cyklu
            }
            //System.out.println("Cislo " + i);
        }

        ArrayList<Integer> test = new ArrayList<>();

        String[] inpString = new String[]{"one", "two", "three"};
        List<String> test2 = Arrays.asList("one", "two");
        ArrayList<String> symbolsPresent = new ArrayList<String>(Arrays.asList(new String[] {"One","Two","Three","Four"}));
        // https://stackoverflow.com/questions/16194921/initializing-arraylist-with-some-predefined-values

        //

        ArrayList<Integer> integers = new ArrayList<Integer>(Arrays.asList(new Integer[] {0,1,2}));
        for (int i = 0; i < integers.size(); i++) {
            System.out.println(integers.get(i));
        }

        ArrayList<Integer> integers2 = new ArrayList<Integer>(Arrays.asList(10, 11, 12));
        for (int i = 0; i < integers2.size(); i++) {
            System.out.println(integers2.get(i));
        }


        //======================================
        TestExc testExc = new TestExc();
        try {
            testExc.test();
        } catch (IllegalSudokuStateException ex) {
            System.out.println("i is bigger than 5");
        }
    }

}

class TestExc {

    public void test() throws IllegalSudokuStateException {
        for (int i = 0; i < 10; i++) {
            if (i > 5) {
                throw new IllegalSudokuStateException(i, 0, 0);
            }
        }
    }
}
