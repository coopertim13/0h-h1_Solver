import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.function.Consumer;

class Grid implements Iterable<Cell> {
    static int cellAmount = 12;
    static int cellSize = 45;
    static int gridPadding = 10;
    static Cell[][] cells = new Cell[cellAmount][cellAmount];
    static int countComplete = 0;

    // constructor
    public Grid(){
        for(int x = 0; x < cells.length; x++){
            for(int y = 0; y < cells[x].length; y++){
                    cells[x][y] = new Cell(position(x), position(y));
            }
        }
    }

    private int position(int a) {
        return gridPadding + cellSize * a;
    }

    // methods
    public void paint(Graphics g, Point mousePos){
        g.setFont(new Font("Cabin-Regular", Font.PLAIN, 20));
        doToEachCell(   (Cell c) -> c.paint(g, mousePos)  );
        g.setColor(Color.GREEN);
        g.fillRect(0,(gridPadding+(cellSize*cellAmount)+2),560,100);
        g.setColor(Color.BLACK);
        g.drawString("Solve", 270, (gridPadding+(cellSize*cellAmount)+55));
    }

    public void mouseClicked(int x, int y) {
        for(int i = 0; i < cells.length; i++){
            for(int j = 0; j < cells[i].length; j++){
                if (cells[i][j].contains(x, y)) {
                    if(cells[i][j].color == Color.WHITE) {
                        cells[i][j].color = Color.RED;
                        countComplete++;
                    }
                    else if(cells[i][j].color == Color.RED) {
                        cells[i][j].color = Color.BLUE;
                    }
                    else {
                        cells[i][j].color = Color.WHITE;
                        countComplete--;
                    }
                }
            }
        }
        if(y>=(gridPadding+(cellSize*cellAmount))) {
            solve();
        }
    }

    public static void solve() {
        int i = 0;
        while(i < 3) {
            countComplete += threes();
            i++;
        }
        while(i < 7) {
            countComplete += countSixRowColumn();
            i++;
        }
        //while(countComplete != cellAmount * cellAmount) {
        //    countComplete+=threes();
        //}
    }

    public static int countSixRowColumn() {
        //columns
        int totalCount = 0;
        totalCount += sixColumn();
        totalCount += sixRow();
        return totalCount;
    }

    public static int sixRow() {
        int sumRed;
        int sumBlue;
        int changes = 0;
        for(int i = 0; i < cells.length; i++) {
            sumRed = 0;
            sumBlue = 0;
            for(int j = 0; j < cells[i].length; j++) {
                if(cells[j][i].color.equals(Color.RED)) {
                    sumRed++;
                }
                if(cells[j][i].color.equals(Color.BLUE)) {
                    sumBlue++;
                }
            }
            if(sumRed == 6) {
                for(int k = 0; k < cells[i].length; k++) {
                    if(cells[k][i].color.equals(Color.WHITE)) {
                        cells[k][i].color = Color.BLUE;
                    }
                }
                changes += 6 - sumBlue;
            }
            else if(sumBlue == 6) {
                for(int k = 0; k < cells[i].length; k++) {
                    if(cells[k][i].color.equals(Color.WHITE)) {
                        cells[k][i].color = Color.RED;
                    }
                }
                changes+= 6 - sumRed;
            }
        }
        return changes;
    }

    public static int sixColumn() {
        int sumRed;
        int sumBlue;
        int changes = 0;
        for(int i = 0; i < cells.length; i++) {
            sumRed = 0;
            sumBlue = 0;
            for(int j = 0; j < cells[i].length; j++) {
                if(cells[i][j].color.equals(Color.RED)) {
                    sumRed++;
                }
                if(cells[i][j].color.equals(Color.BLUE)) {
                    sumBlue++;
                }
            }
            if(sumRed == 6) {
                for(int k = 0; k < cells[i].length; k++) {
                    if(cells[i][k].color.equals(Color.WHITE)) {
                        cells[i][k].color = Color.BLUE;
                    }
                }
                changes += 6 - sumBlue;
            }
            else if(sumBlue == 6) {
                for(int k = 0; k < cells[i].length; k++) {
                    if(cells[i][k].color.equals(Color.WHITE)) {
                        cells[i][k].color = Color.RED;
                    }
                }
                changes+= 6 - sumRed;
            }
        }
        return changes;
    }

    public static int threes() {
        int changes = 0;
        ArrayList<Cell> cellAround = new ArrayList<>();
        for(int i = 0; i < cells.length; i++) {
            for(int j = 0; j < cells[i].length; j++) {
                cellAround.clear();
                cellAround = cellNAround(cells[i][j], 2);
                for(int k = 0; k < cellAround.size(); k++) {
                    if(cellAround.get(k).color.equals(cells[i][j].color) && !(cells[i][j].color.equals(Color.WHITE))) {
                        if(cells[i][j].color.equals(Color.RED)) {
                            if(!(cellInbetween(cells[i][j], cellAround.get(k)).color.equals(Color.BLUE))) {
                                cellInbetween(cells[i][j], cellAround.get(k)).color = Color.BLUE;
                                changes++;
                            }
                        }
                        else {
                            if(!(cellInbetween(cells[i][j], cellAround.get(k)).color.equals(Color.RED))) {
                                cellInbetween(cells[i][j], cellAround.get(k)).color = Color.RED;
                                changes++;
                            }
                        }
                    }
                }
                cellAround.clear();
                cellAround = cellNAround(cells[i][j], 1);
                for(int k = 0; k < cellAround.size(); k++) {
                    if(cellAround.get(k).color.equals(cells[i][j].color) && !(cells[i][j].color.equals(Color.WHITE))) {
                        Cell smaller = cells[i][j];
                        Cell bigger = cellAround.get(k);
                        if(cells[i][j].color.equals(Color.RED)) {
                            if(i != getIJ(cellAround.get(k))[0]) {
                                if(getIJ(bigger)[0] < getIJ(smaller)[0]) {
                                    smaller = cellAround.get(k);
                                    bigger = cells[i][j];
                                }
                                if(getIJ(smaller)[0]-1 >= 0) {
                                    if(!(cells[getIJ(smaller)[0]-1][getIJ(smaller)[1]].color.equals(Color.BLUE))) {
                                        cells[getIJ(smaller)[0]-1][getIJ(smaller)[1]].color = Color.BLUE;
                                        changes++;
                                    }
                                }
                                if(getIJ(bigger)[0]+1 < cellAmount) {
                                    if(!(cells[getIJ(bigger)[0]+1][getIJ(bigger)[1]].color.equals(Color.BLUE))) {
                                        cells[getIJ(bigger)[0]+1][getIJ(bigger)[1]].color = Color.BLUE;
                                        changes++;
                                    }
                                }
                            }
                            if(j != getIJ(cellAround.get(k))[1]) {
                                if(getIJ(bigger)[1] < getIJ(smaller)[1]) {
                                    smaller = cellAround.get(k);
                                    bigger = cells[i][j];
                                }
                                if(getIJ(smaller)[1]-1 >= 0) {
                                    if(!(cells[getIJ(smaller)[0]][getIJ(smaller)[1]-1].color.equals(Color.BLUE))) {
                                        cells[getIJ(smaller)[0]][getIJ(smaller)[1]-1].color = Color.BLUE;
                                        changes++;
                                    }
                                }
                                if(getIJ(bigger)[1]+1 < cellAmount) {
                                    if(!(cells[getIJ(bigger)[0]][getIJ(bigger)[1]+1].color.equals(Color.BLUE))) {
                                        cells[getIJ(bigger)[0]][getIJ(bigger)[1]+1].color = Color.BLUE;
                                        changes++;
                                    }
                                }
                            }
                        }

                        if(cells[i][j].color.equals(Color.BLUE)) {
                            if(i != getIJ(cellAround.get(k))[0]) {
                                if(getIJ(bigger)[0] < getIJ(smaller)[0]) {
                                    smaller = cellAround.get(k);
                                    bigger = cells[i][j];
                                }
                                if(getIJ(smaller)[0]-1 >= 0) {
                                    if(!(cells[getIJ(smaller)[0]-1][getIJ(smaller)[1]].color.equals(Color.RED))) {
                                        cells[getIJ(smaller)[0]-1][getIJ(smaller)[1]].color = Color.RED;
                                        changes++;
                                    }
                                }
                                if(getIJ(bigger)[0]+1 < cellAmount) {
                                    if(!(cells[getIJ(bigger)[0]+1][getIJ(bigger)[1]].color.equals(Color.RED))) {
                                        cells[getIJ(bigger)[0]+1][getIJ(bigger)[1]].color = Color.RED;
                                        changes++;
                                    }
                                }
                            }
                            if(j != getIJ(cellAround.get(k))[1]) {
                                if(getIJ(bigger)[1] < getIJ(smaller)[1]) {
                                    smaller = cellAround.get(k);
                                    bigger = cells[i][j];
                                }
                                if(getIJ(smaller)[1]-1 >= 0) {
                                    if(!(cells[getIJ(smaller)[0]][getIJ(smaller)[1]-1].color.equals(Color.RED))) {
                                        cells[getIJ(smaller)[0]][getIJ(smaller)[1]-1].color = Color.RED;
                                        changes++;
                                    }
                                }
                                if(getIJ(bigger)[1]+1 < cellAmount) {
                                    if(!(cells[getIJ(bigger)[0]][getIJ(bigger)[1]+1].color.equals(Color.RED))) {
                                        cells[getIJ(bigger)[0]][getIJ(bigger)[1]+1].color = Color.RED;
                                        changes++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return changes;
    }

    public static ArrayList<Cell> cellNAround(Cell c, int n) {
        int i = getIJ(c)[0];
        int j = getIJ(c)[1];
        ArrayList<Cell> cellsAround = new ArrayList<>();
        if(i - n >= 0) {
            cellsAround.add(cells[i-n][j]);
        }
        if(i + n <= cellAmount - 1) {
            cellsAround.add(cells[i+n][j]);
        }
        if(j - n >= 0) {
            cellsAround.add(cells[i][j-n]);
        }
        if(j + n <= cellAmount-1) {
            cellsAround.add(cells[i][j+n]);
        }
        return cellsAround;
    }

    public static Cell cellInbetween(Cell a, Cell b) {
        for(int i = 0; i < cells.length; i++) {
            for(int j = 0; j < cells.length; j++) {
                if(cells[i][j].equals(a)) {
                    if(i - 2 >= 0) {
                        if(cells[i-2][j].equals(b)) {
                            return cells[i-1][j];
                        }
                    }
                    if(i + 2 <= cellAmount - 1) {
                        if(cells[i+2][j].equals(b)) {
                            return cells[i+1][j];
                        }
                    }
                    if(j - 2 >= 0) {
                        if(cells[i][j-2].equals(b)) {
                            return cells[i][j-1];
                        }
                    }
                    if(j + 2 <= cellAmount - 1) {
                        if(cells[i][j+2].equals(b)) {
                            return cells[i][j+1];
                        }
                    }
                }
            }
        }
        return new Cell(-20,-20);
    }

    public static int[] getIJ(Cell c) {
        for(int i = 0; i < cells.length; i++){
            for(int j = 0; j < cells[i].length; j++){
                if (cells[i][j].equals(c)) {
                    int[] a = {i,j};
                    return a;
                }
            }
        }
        return new int[0];
    }

    /**
     * Takes a cell consumer (i.e. a function that has a single `Cell` argument and
     * returns `void`) and applies that consumer to each cell in the grid.
     * @param func The `Cell` to `void` function to apply at each spot.
     */
    public void doToEachCell(Consumer<Cell> func){
        for(Cell c : this){
            func.accept(c);
        }
    }

    @Override
    public CellIterator iterator(){
        return new CellIterator(cells);
    }

}