import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame implements ActionListener {
    public static boolean mousePressed = false;
    public static boolean startAdded = false;
    public static boolean targetAdded = false;

    JPanel panel = new JPanel();
    JPanel panel2 = new JPanel();
    Astar astar;

    public GUI(int size) {
        super("AStar Algorithm");
        setSize(800, 800);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        panel.setLayout(new GridLayout(size, size));

        JCheckBox addStart = new JCheckBox("Done Setting Start");
        addStart.setForeground(Color.green);
        JCheckBox addTarget = new JCheckBox("Done Setting Target");
        addTarget.setForeground(Color.cyan);
        JCheckBox walls = new JCheckBox("Done Adding Walls");
        JButton findPath = new JButton("Find Path!");
        walls.setForeground(Color.black);
        panel2.add(addStart);
        panel2.setBackground(Color.gray);
        this.add(panel2, BorderLayout.PAGE_END);

        astar = new Astar(size,2 , 2, this);

        addStart.addActionListener(e -> {
            panel2.add(addTarget);
            panel2.validate();
            startAdded = true;
        });

        addTarget.addActionListener(e -> {
            panel2.add(walls);
            panel2.validate();
            targetAdded = true;
        });

        walls.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel2.add(findPath);
                panel2.validate();
            }
        });
        findPath.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            astar.findPath();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
