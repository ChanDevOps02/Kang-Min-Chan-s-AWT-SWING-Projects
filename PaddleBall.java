package sec02.exam06;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class PaddleBall extends Frame implements ActionListener, KeyListener {
    int paddleX = 350;
    boolean isRun = false;
    public static final int PADDLE_WIDTH = 100;
    public static final int PADDLE_HEIGHT = 10;
    public static final int BALL_SIZE = 50; // 공 크기 상수화
    Color ballColor = Color.BLACK;
    Color backgroundColor = Color.WHITE;

    Button start, addBall, changeBackground, changeBallColor, clear;
    Panel panel;
    List<Point> balls = new ArrayList<>();
    List<int[]> velocities = new ArrayList<>();

    public PaddleBall() {
        setTitle("Paddle Ball Game");
        setBackground(backgroundColor);
        setSize(800, 800);
        setFocusable(true);
        addKeyListener(this); // KeyListener 등록

        panel = new Panel();
        panel.setLayout(new FlowLayout());
        panel.setBackground(Color.GRAY);

        start = new Button("Game Start");
        addBall = new Button("Add Ball");
        changeBackground = new Button("Change Background");
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

        setVisible(true);
        requestFocusInWindow(); // 키 입력 활성화
    }

    public void actionPerformed(ActionEvent ae) {
        requestFocusInWindow(); // 버튼 클릭 후에도 키 이벤트가 작동하도록 포커스 유지

        if (ae.getSource() == start) {
            isRun = true;
            moveBall();
        } else if (ae.getSource() == addBall) {
            Random random = new Random();
            int x = random.nextInt(750 - BALL_SIZE); // 화면 너비에서 공 크기 제외
            int y = random.nextInt(500) + 100;
            int dx = random.nextInt(4) + 1;
            int dy = random.nextInt(4) + 1;
            balls.add(new Point(x, y));
            velocities.add(new int[]{dx, dy});
            repaint();
        } else if (ae.getSource() == changeBackground) {
            Random random = new Random();
            backgroundColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            setBackground(backgroundColor);
            repaint();
        } else if (ae.getSource() == changeBallColor) {
            Random random = new Random();
            ballColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            repaint();
        } else if (ae.getSource() == clear) {
            balls.clear();
            repaint();
        }
    }

    public void moveBall() {
        new Thread(() -> {
            while (isRun) {
                boolean ballHitBottom = false; // 바닥 충돌 여부 플래그 추가

                for (int i = 0; i < balls.size(); i++) {
                    Point point = balls.get(i);
                    int[] velocity = velocities.get(i);
                    point.xpos += velocity[0];
                    point.ypos += velocity[1];

                    // 좌우 벽 충돌 처리
                    if (point.xpos <= 0 || point.xpos >= getWidth() - BALL_SIZE) {
                        velocity[0] = -velocity[0];
                    }

                    // 천장 충돌 처리
                    if (point.ypos <= panel.getHeight()) {
                        velocity[1] = -velocity[1];
                    }

                    // 바닥 충돌 감지 (게임 종료)
                    if (point.ypos >= getHeight() - BALL_SIZE - 40) {
                        ballHitBottom = true; // 바닥 충돌 감지
                        break; // 하나라도 바닥에 닿으면 반복문 종료
                    }

                    // 패들과 충돌하면 반사
                    if (point.ypos + BALL_SIZE >= 730 && point.xpos + BALL_SIZE >= paddleX && point.xpos <= paddleX + PADDLE_WIDTH) {
                        velocity[1] = -velocity[1];
                    }
                }

                if (ballHitBottom) {
                    isRun = false;
                    balls.clear();
                    repaint();
                    break; // while문 종료
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


    public void keyTyped(KeyEvent ke) {}

    public void keyPressed(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
            paddleX = Math.max(0, paddleX - 20); // 왼쪽 경계 체크
        } else if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
            paddleX = Math.min(getWidth() - PADDLE_WIDTH, paddleX + 20); // 오른쪽 경계 체크
        }
        repaint();
    }

    public void keyReleased(KeyEvent ke) {}

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(ballColor);
        for (Point temp : balls) {
            g.fillOval(temp.xpos, temp.ypos, BALL_SIZE, BALL_SIZE);
        }

        // 패들 색상 변경 (더 잘 보이도록 BLUE로 설정)
        g.setColor(Color.BLUE);
        g.fillRect(paddleX, 730, PADDLE_WIDTH, PADDLE_HEIGHT);
    }

    public static void main(String[] args) {
        new PaddleBall();
    }
}
