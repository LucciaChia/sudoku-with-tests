package sudoku;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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

        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert your sudoku:");
        int[][] data = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                data[i][j] = scanner.nextInt();
                scanner.nextLine();
            }
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(data[i][j] + " ");
            }
            System.out.println();
        }
    }

}
