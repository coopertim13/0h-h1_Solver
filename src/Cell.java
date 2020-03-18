import java.awt.*;

public class Cell extends Rectangle {
    // fields
    Color color = Color.WHITE;
    boolean computerProvided;

    //constructors
    public Cell(int x, int y){
        super(x,y,Grid.cellSize,Grid.cellSize);
    }

    //methods
    void paint(Graphics g, Point mousePos){
        g.setColor(color);
        g.fillRect(x,y,Grid.cellSize,Grid.cellSize);
        g.setColor(Color.BLACK);
        g.drawRect(x,y,Grid.cellSize, Grid.cellSize);
    }

    public boolean contains(Point p){
        if (p != null){
            return super.contains(p);
        } else {
            return false;
        }
    }
}