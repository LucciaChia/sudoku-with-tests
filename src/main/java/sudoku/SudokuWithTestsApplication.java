package sudoku;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SudokuWithTestsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SudokuWithTestsApplication.class, args);
        System.out.println("Hello, I am running!");
    }
}
