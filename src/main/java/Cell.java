public class Cell {
    private int i;
    private int j;
    private int actualValue;
    private Possibility cellPossibilities;

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public Cell(int actualValue, int i, int j) {
        this.i = i;
        this.j = j;
        this.actualValue = actualValue;
    }

    public void setActualValue(int actualValue) {
        if (actualValue != 0) {
            cellPossibilities = null;
        }
        this.actualValue = actualValue;
    }

    public Possibility getCellPossibilities() {
        return cellPossibilities;
    }

    public void setCellPossibilities(Possibility cellPossibilities) {
        this.cellPossibilities = cellPossibilities;
    }


    public int getActualValue() {
        return actualValue;
    }

    @Override
    public String toString() {
        return "value: " + actualValue + ", i=" + i + ", j=" + j;

    }
}
