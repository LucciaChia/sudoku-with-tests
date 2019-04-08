package sudoku;

import java.io.File;

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
            System.out.println("Cislo " + i);
        }
    }

}
