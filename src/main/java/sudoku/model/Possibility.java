package sudoku.model;

import java.util.ArrayList;

public class Possibility {
    private int i;
    private int j;
    private ArrayList<Integer> possibilities;

    public Possibility(int i, int j) {
        this.i = i;
        this.j = j;
        possibilities = new ArrayList<>();
        possibilities.add(1);
        possibilities.add(2);
        possibilities.add(3);
        possibilities.add(4);
        possibilities.add(5);
        possibilities.add(6);
        possibilities.add(7);
        possibilities.add(8);
        possibilities.add(9);
    }

    public Possibility(int i, int j, ArrayList<Integer> possibilities) {
        this.i = i;
        this.j = j;
        this.possibilities = new ArrayList<>();
        this.possibilities = possibilities;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public ArrayList<Integer> getPosibilities() {
        return possibilities;
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < possibilities.size(); i++) {
            s += possibilities.get(i) + ", ";
        }
        return "i= " + this.i + ", j= " + this.j + " possibilities: " + s;
    }

}
