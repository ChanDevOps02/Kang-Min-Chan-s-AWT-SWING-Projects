package sec02.exam06;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DodgePoop extends Frame implements ActionListener, KeyListener {
    public static final int BALL_SIZE = 50;
    public static final int HUMAN_HEIGHT =70;
    public static final int HUMAN_WIDTH = 30;
    public static final Color HUMAN_COLOR = Color.CYAN;
    int HumanXpos = 350;
    Boolean isHit = false;
    Button start;
    Button addBall;
    Button changeBackground;
    Button changeBallColor;
    Button clear;
    Panel panel;
    Color backGround = Color.WHITE;
    Color ballColor = Color.BLACK;
    List<Point> balls = new ArrayList<>();
    List<int[]> velocities = new ArrayList<>();

    public DodgePoop(){
        setTitle("Let's dodge Poops!");
        setBackground(backGround);
        setSize(800, 800);
        setFocusable(true);
        addKeyListener(this);

        panel = new Panel();
        panel.setBackground(Color.GRAY);
        panel.setLayout(new FlowLayout());

        start = new Button("Game Start");
        addBall = new Button("Add Balls");
        changeBackground = new Button("Change Background Color");
        changeBallColor = new Button("Change Ball Color");
        clear = new Button("Clear");
        start.addActionListener(this);
        addBall.addActionListener(this);
        changeBackground.addActionListener(this);
        changeBallColor.addActionListener(this);
        clear.addActionListener(this);

        panel.add(start);
        panel.add(addBall);
        panel.add(changeBackground);
        panel.add(changeBallColor);
        panel.add(clear);
        add(panel, BorderLayout.NORTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        this.setVisible(true);
        requestFocusInWindow();
    }
    public void actionPerformed(ActionEvent ae){
        requestFocusInWindow();
        if(ae.getSource() == start){
            moveBall();
        }
        else if(ae.getSource() == addBall){
            Random random = new Random();
            int x = random.nextInt(750);
            int y = random.nextInt(730);
            int dx = random.nextInt(4) + 1;
            int dy = random.nextInt(4) + 1;

            balls.add(new Point(x, y));
            velocities.add(new int[]{dx, dy});
            repaint();
        }
        else if(ae.getSource() == changeBackground){
            Random random = new Random();
            int red = random.nextInt(256);
            int green = random.nextInt(256);
            int blue = random.nextInt(256);
            backGround = new Color(red, green, blue);
            this.setBackground(backGround);
        }
        else if(ae.getSource() == changeBallColor){
            Random random = new Random();
            int red = random.nextInt(256);
            int green = random.nextInt(256);
            int blue = random.nextInt(256);
            ballColor = new Color(red, green, blue);
            repaint();
        }
        else if(ae.getSource() == clear){
            balls.clear();
            velocities.clear();
            repaint();
        }
    }
    public void moveBall(){
        new Thread(() -> {
            while(true){
                for(int i = 0; i < balls.size(); i++){
                    Point point = balls.get(i);
                    point.xpos += velocities.get(i)[0];
                    point.ypos += velocities.get(i)[1];

                    if(point.xpos <= 0 || point.xpos >= 750){
                        velocities.get(i)[0] = -velocities.get(i)[0];
                    }
                    if(point.ypos <= 40 || point.ypos >= 750){
                        velocities.get(i)[1] = -velocities.get(i)[1];
                    }
                    if (point.xpos + BALL_SIZE >= HumanXpos && point.xpos <= HumanXpos + HUMAN_WIDTH &&
                            point.ypos + BALL_SIZE >= 730 && point.ypos <= 730 + HUMAN_HEIGHT) {
                        isHit = true;
                        break;
                    }

                }
                if(isHit){
                    balls.clear();
                    velocities.clear();
                    repaint();
                    System.exit(0);
                }
                repaint();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void paint(Graphics g){
        super.paint(g);
        for(Point temp : balls){
            g.setColor(ballColor);
            g.fillOval(temp.xpos, temp.ypos, BALL_SIZE, BALL_SIZE);
        }
        g.setColor(HUMAN_COLOR);
        g.fillRect(HumanXpos, 730, HUMAN_WIDTH, HUMAN_HEIGHT);
    }
    public void keyTyped(KeyEvent ke){}
    public void keyPressed(KeyEvent ke){
        if(ke.getKeyCode() == KeyEvent.VK_RIGHT){
            HumanXpos = Math.min(getWidth() - HUMAN_WIDTH, HumanXpos + 20);
            repaint();
        }
        if(ke.getKeyCode() == KeyEvent.VK_LEFT){
            HumanXpos = Math.max(0, HumanXpos - 20);
            repaint();
        }
    }
    public void keyReleased(KeyEvent ke){}

    public static void main(String[] args){
        Frame frame = new DodgePoop();
    }
}
