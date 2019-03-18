import java.util.ArrayList;

public class Possibility {
    private int i;
    private int j;
    private ArrayList<Integer> posibilities;
    Cell c;

    public Possibility(int i, int j) {
        this.i = i;
        this.j = j;
        posibilities = new ArrayList<>();
        posibilities.add(1);
        posibilities.add(2);
        posibilities.add(3);
        posibilities.add(4);
        posibilities.add(5);
        posibilities.add(6);
        posibilities.add(7);
        posibilities.add(8);
        posibilities.add(9);
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public ArrayList<Integer> getPosibilities() {
        return posibilities;
    }

    public Cell getC() {
        return c;
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < posibilities.size(); i++) {
            s += posibilities.get(i) + ", ";
        }
        return "i= " + this.i + ", j= " + this.j + " possibilities: " + s;
    }
}
