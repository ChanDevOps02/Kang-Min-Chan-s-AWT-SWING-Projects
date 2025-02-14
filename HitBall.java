package sec02.exam06;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class HitBall extends Frame implements ActionListener {
    Button AddBall;
    Button Clear;
    Button setBackground;
    Button setBallColor;
    Button start;
    Panel panel;
    Color background = Color.WHITE;
    Color ballColor = Color.BLACK;
    List<Point> balls = new ArrayList<Point>();
    List<int[]>velocities = new ArrayList<>();

    public HitBall(){
        setTitle("Kang Min Chan's HitBall Example");
        setBackground(background);
        setSize(800,800);

        panel = new Panel();
        panel.setBackground(Color.GRAY);
        panel.setLayout(new FlowLayout());

        AddBall = new Button("Add Ball");
        setBackground = new Button("Change Background");
        Clear = new Button("Clear");
        setBallColor = new Button("Change Ball color");
        start = new Button("Start");
        AddBall.addActionListener(this);
        setBackground.addActionListener(this);
        Clear.addActionListener(this);
        setBallColor.addActionListener(this);
        start.addActionListener(this);

        panel.add(AddBall);
        panel.add(setBackground);
        panel.add(setBallColor);
        panel.add(start);
        panel.add(Clear);

        add(panel, BorderLayout.NORTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        this.setVisible(true);
    }
    public void actionPerformed(ActionEvent ae){
        if(ae.getSource() == AddBall){
            Random random = new Random();
            int x = random.nextInt(256);
            int y = random.nextInt(256);
            balls.add(new Point(x, y));

            int dx = random.nextInt(4) + 1;
            int dy = random.nextInt(4) + 1;
            velocities.add(new int[] {dx, dy});
            repaint();
        }
        else if(ae.getSource() == setBackground){
            Random random = new Random();
            int RED = random.nextInt(256);
            int GREEN = random.nextInt(256);
            int BLUE = random.nextInt(256);
            background = new Color(RED, GREEN, BLUE);
            this.setBackground(background);
            repaint();

        }
        else if(ae.getSource() == setBallColor){
            Random random = new Random();
            int RED = random.nextInt(256);
            int GREEN = random.nextInt(256);
            int BLUE = random.nextInt(256);
            ballColor = new Color(RED, GREEN, BLUE);
            repaint();
        }
        else if(ae.getSource() == start){
            moveBall(); //구현예정
        }
        else if(ae.getSource() == Clear){
            balls.clear();
            velocities.clear();
            repaint();
        }
    }
   public void moveBall() {
       new Thread(() -> {
           while (true) {
               for (int i = 0; i < balls.size(); i++) {
                   Point point = balls.get(i);
                   int[] velocity = velocities.get(i);

                   point.xpos += velocity[0];
                   point.ypos += velocity[1];

                   if (point.xpos <= 0 || point.xpos >= 750) {
                       velocity[0] = -velocity[0];
                   }
                   if (point.ypos <= 50 || point.ypos >= 750) {
                       velocity[1] = -velocity[1];
                   }
                   repaint();
                   try {
                       Thread.sleep(10);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
           }
       }).start();
   }
    public void paint(Graphics g){
        super.paint(g);
        g.setColor(ballColor);
        for(Point ball : balls){
            g.fillOval(ball.xpos, ball.ypos, 50, 50);
        }
    }
    public static void main(String[] args){
        Frame frame = new HitBall();
    }

}
