import java.awt.*;
import java.util.ArrayList;
import java.util.function.Consumer;

class Grid implements Iterable<Cell> {
    static int cellAmount = 12;
    static int cellSize = 45;
    static int gridPadding = 10;
    static Cell[][] cells = new Cell[cellAmount][cellAmount];

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
        g.setColor(Color.YELLOW);
        g.fillRect(0,(gridPadding+(cellSize*cellAmount)+102),112,100);
        g.fillRect(113,(gridPadding+(cellSize*cellAmount)+102),112,100);
        g.fillRect(226,(gridPadding+(cellSize*cellAmount)+102),112,100);
        g.fillRect(339,(gridPadding+(cellSize*cellAmount)+102),112,100);
        g.fillRect(452,(gridPadding+(cellSize*cellAmount)+102),112,100);
        g.setColor(Color.BLACK);
        g.drawString("4", 50, (gridPadding+(cellSize*cellAmount)+152));
        g.drawString("6", 161, (gridPadding+(cellSize*cellAmount)+152));
        g.drawString("8", 272, (gridPadding+(cellSize*cellAmount)+152));
        g.drawString("10", 383, (gridPadding+(cellSize*cellAmount)+152));
        g.drawString("12", 494, (gridPadding+(cellSize*cellAmount)+152));
    }

    public void mouseClicked(int x, int y) {
        for(int i = 0; i < cells.length; i++){
            for(int j = 0; j < cells[i].length; j++){
                if (cells[i][j].contains(x, y)) {
                    if(cells[i][j].color == Color.WHITE) {
                        cells[i][j].color = Color.RED;
                    }
                    else if(cells[i][j].color == Color.RED) {
                        cells[i][j].color = Color.BLUE;
                    }
                    else {
                        cells[i][j].color = Color.WHITE;
                    }
                }
            }
        }
        if(y>=(gridPadding+(cellSize*cellAmount))) {
            if(y < (gridPadding+(cellSize*cellAmount))+102) {
                solve();
            }
            else {
                doToEachCell((Cell c) -> c.color = Color.WHITE);
                if(x <= 113) {
                    cellAmount = 4;
                }
                else if(x <= 226) {
                    cellAmount = 6;
                }
                else if(x <= 339) {
                    cellAmount = 8;
                }
                else if(x <= 452) {
                    cellAmount = 10;
                }
                else {
                    cellAmount = 12;
                }
                cells = new Cell[cellAmount][cellAmount];
                for(int a = 0; a < cells.length; a++){
                    for(int b = 0; b < cells[a].length; b++){
                            cells[a][b] = new Cell(position(a), position(b));
                    }
                }
            }
        }
    }

    public static void solve() {
        for(int j = 0; j < 100 && stillWhite(); j++) {
            int i = 0;
            while(i < 3) {
                threes();
                i++;
            }
            while(i < 7) {
                countHalfCellRowColumn();
                i++;
            }
            while(i < 11) {
                compareRowColumn();
                i++;
            }
        }
    }

    public static boolean stillWhite() {
        for(int i = 0; i < cells.length; i++) {
            for(int j = 0; j < cells[i].length; j++) {
                if(cells[i][j].color.equals(Color.WHITE)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void compareRowColumn() {
        compareRow();
        compareColumn();
    }

    public static void compareRow() {
        int sumRed;
        int sumBlue;
        ArrayList<Integer> fullRowIndexes = getFullRows();
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
            if(sumRed == sumBlue && sumRed == cellAmount/2-1) {
                int count = 0;
                while(count < fullRowIndexes.size()) {
                    int totalSame = 0;
                    for(int k = 0; k < cells.length; k++) {
                        if(cells[k][fullRowIndexes.get(count)].color.equals(cells[k][i].color)) {
                            totalSame++;
                        }
                    }
                    if(totalSame == cellAmount - 2) {
                        for(int k = 0; k < cells.length; k++) {
                            if(cells[k][i].color.equals(Color.WHITE)) {
                                if(cells[k][fullRowIndexes.get(count)].color.equals(Color.BLUE)) {
                                    cells[k][i].color = Color.RED;
                                }
                                else if(cells[k][fullRowIndexes.get(count)].color.equals(Color.RED)){
                                    cells[k][i].color = Color.BLUE;
                                }
                            }
                        }
                    }
                    count++;
                }
            }
        }
    }

    public static void compareColumn() {
        int sumRed;
        int sumBlue;
        ArrayList<Integer> fullRowIndexes = getFullRows();
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
            if(sumRed == sumBlue && sumRed == cellAmount/2-1) {
                int count = 0;
                while(count < fullRowIndexes.size()) {
                    int totalSame = 0;
                    for(int k = 0; k < cells.length; k++) {
                        if(cells[fullRowIndexes.get(count)][k].color.equals(cells[i][k].color)) {
                            totalSame++;
                        }
                    }
                    if(totalSame == cellAmount - 2) {
                        for(int k = 0; k < cells.length; k++) {
                            if(cells[i][k].color.equals(Color.WHITE)) {
                                if(cells[fullRowIndexes.get(count)][k].color.equals(Color.BLUE)) {
                                    cells[i][k].color = Color.RED;
                                }
                                else if(cells[fullRowIndexes.get(count)][k].color.equals(Color.RED)){
                                    cells[i][k].color = Color.BLUE;
                                }
                            }
                        }
                    }
                    count++;
                }
            }
        }
    }

    public static ArrayList<Integer> getFullRows() {
        ArrayList<Integer> array = new ArrayList<Integer>();
        for(int i = 0; i < cells.length; i++) {
            int nonWhite = 0;
            for(int j = 0; j < cells[i].length; j++) {
                if(!(cells[j][i].color.equals(Color.WHITE))) {
                    nonWhite++;
                }
            }
            if(nonWhite == cellAmount) {
                array.add(i);
            }
        }
        return array;
    }

    public static ArrayList<Integer> getFullColumns() {
        ArrayList<Integer> array = new ArrayList<Integer>();
        for(int i = 0; i < cells.length; i++) {
            int nonWhite = 0;
            for(int j = 0; j < cells[i].length; j++) {
                if(!(cells[i][j].color.equals(Color.WHITE))) {
                    nonWhite++;
                }
            }
            if(nonWhite == cellAmount) {
                array.add(i);
            }
        }
        return array;
    }

    public static void countHalfCellRowColumn() {
        halfColumn();
        halfRow();
    }

    public static void halfRow() {
        int sumRed;
        int sumBlue;
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
            if(sumRed == cellAmount/2) {
                for(int k = 0; k < cells[i].length; k++) {
                    if(cells[k][i].color.equals(Color.WHITE)) {
                        cells[k][i].color = Color.BLUE;
                    }
                }
            }
            else if(sumBlue == cellAmount/2) {
                for(int k = 0; k < cells[i].length; k++) {
                    if(cells[k][i].color.equals(Color.WHITE)) {
                        cells[k][i].color = Color.RED;
                    }
                }
            }
        }
    }

    public static void halfColumn() {
        int sumRed;
        int sumBlue;
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
            if(sumRed == cellAmount/2) {
                for(int k = 0; k < cells[i].length; k++) {
                    if(cells[i][k].color.equals(Color.WHITE)) {
                        cells[i][k].color = Color.BLUE;
                    }
                }
            }
            else if(sumBlue == cellAmount/2) {
                for(int k = 0; k < cells[i].length; k++) {
                    if(cells[i][k].color.equals(Color.WHITE)) {
                        cells[i][k].color = Color.RED;
                    }
                }
            }
        }
    }

    public static void threes() {
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
                            }
                        }
                        else {
                            if(!(cellInbetween(cells[i][j], cellAround.get(k)).color.equals(Color.RED))) {
                                cellInbetween(cells[i][j], cellAround.get(k)).color = Color.RED;
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
                                    }
                                }
                                if(getIJ(bigger)[0]+1 < cellAmount) {
                                    if(!(cells[getIJ(bigger)[0]+1][getIJ(bigger)[1]].color.equals(Color.BLUE))) {
                                        cells[getIJ(bigger)[0]+1][getIJ(bigger)[1]].color = Color.BLUE;
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
                                    }
                                }
                                if(getIJ(bigger)[1]+1 < cellAmount) {
                                    if(!(cells[getIJ(bigger)[0]][getIJ(bigger)[1]+1].color.equals(Color.BLUE))) {
                                        cells[getIJ(bigger)[0]][getIJ(bigger)[1]+1].color = Color.BLUE;
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
                                    }
                                }
                                if(getIJ(bigger)[0]+1 < cellAmount) {
                                    if(!(cells[getIJ(bigger)[0]+1][getIJ(bigger)[1]].color.equals(Color.RED))) {
                                        cells[getIJ(bigger)[0]+1][getIJ(bigger)[1]].color = Color.RED;
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
                                    }
                                }
                                if(getIJ(bigger)[1]+1 < cellAmount) {
                                    if(!(cells[getIJ(bigger)[0]][getIJ(bigger)[1]+1].color.equals(Color.RED))) {
                                        cells[getIJ(bigger)[0]][getIJ(bigger)[1]+1].color = Color.RED;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
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