import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Node extends JButton {
    public static Node startingNode = null;
    public static Node targetNode = null;
    Position position;
    Position prevPosition;
    int gCost;
    int hCost;
    int fCost;
    boolean visited;
    boolean isWall;


    public Node(Position position, Position prevPosition, int gCost, int hCost, boolean visited, boolean isWall) {
        this.position = position;
        this.prevPosition = prevPosition;
        this.gCost = gCost;
        this.hCost = hCost;
        this.fCost = gCost + hCost;
        this.visited = visited;
        this.isWall = isWall;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if(!GUI.startAdded) {
                    setStart();
                }
                else if(!GUI.targetAdded) {
                    setTarget();
                }
                else {
                    GUI.mousePressed = true;
                    makeWall();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                GUI.mousePressed = false;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if(GUI.mousePressed) {
                    makeWall();
                }
            }
        });
    }

    public void setfCost() {
        this.fCost = gCost + hCost;
    }

    public void makeWall() {
        this.setBackground(Color.black);
        this.setOpaque(true);
        this.isWall = true;
    }

    public void setStart() {
        this.setBackground(Color.green);
        this.setOpaque(true);
        startingNode = this;
        this.visited = true;
        this.gCost = 0;
    }

    public void setTarget() {
        this.setBackground(Color.CYAN);
        this.setOpaque(true);
        targetNode = this;
    }
}
