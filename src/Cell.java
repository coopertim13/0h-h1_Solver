import java.awt.*;

public class Cell extends Rectangle {
    // fields
    final static int size = 45;
    Color color = Color.WHITE;
    boolean computerProvided;

    //constructors
    public Cell(int x, int y){
        super(x,y,size,size);
    }

    //methods
    void paint(Graphics g, Point mousePos){
        g.setColor(color);
        g.fillRect(x,y,size,size);
        g.setColor(Color.BLACK);
        g.drawRect(x,y,size,size);
        //g.drawString(num+"", x+25, y+35);
    }

    public boolean contains(Point p){
        if (p != null){
            return super.contains(p);
        } else {
            return false;
        }
    }
}