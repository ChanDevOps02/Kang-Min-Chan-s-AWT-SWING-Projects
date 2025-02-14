package sec02.exam06;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class BounceBall extends Frame implements ActionListener {
    Canvas canvas = new Canvas();
    Button Left, Right, Up, Down, clear;
    Panel panel;
    int xpos = 375; // 초기 x 좌표
    int ypos = 375; // 초기 y 좌표
    int ballSize = 100; // 공 크기
    Thread moveThread = null; // 현재 실행 중인 이동 스레드

    public BounceBall() {
        setTitle("Kang Min Chan's Bounce Ball program");
        setBackground(Color.YELLOW);
        setSize(800, 800);

        panel = new Panel();
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setLayout(new FlowLayout());

        Left = new Button("Left");
        Right = new Button("Right");
        Up = new Button("Up");
        Down = new Button("Down");
        clear = new Button("Clear");

        Left.addActionListener(this);
        Right.addActionListener(this);
        Up.addActionListener(this);
        Down.addActionListener(this);
        clear.addActionListener(this);

        panel.add(Left);
        panel.add(Right);
        panel.add(Up);
        panel.add(Down);
        panel.add(clear);
        add(panel, BorderLayout.NORTH);
        add(canvas, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        // 기존 스레드가 있으면 중단
        if (moveThread != null) {
            moveThread.interrupt(); // 현재 실행 중인 스레드 중단 요청
        }

        // 새로운 스레드 생성하여 이동 실행
        if (e.getSource() == Left) {
            moveThread = new Thread(() -> {
                try {
                    while (xpos > 0) {
                        xpos -= 5;
                        repaint();
                        Thread.sleep(50);
                    }
                } catch (InterruptedException ex) {
                    // 스레드가 interrupt 되면 여기로 빠져나와 종료
                }
            });
        } else if (e.getSource() == Right) {
            moveThread = new Thread(() -> {
                try {
                    while (xpos < 800 - ballSize) {
                        xpos += 5;
                        repaint();
                        Thread.sleep(50);
                    }
                } catch (InterruptedException ex) {
                }
            });
        } else if (e.getSource() == Up) {
            moveThread = new Thread(() -> {
                try {
                    while (ypos > getInsets().top + panel.getHeight()) {
                        ypos -= 5;
                        repaint();
                        Thread.sleep(50);
                    }
                } catch (InterruptedException ex) {
                }
            });
        } else if (e.getSource() == Down) {
            moveThread = new Thread(() -> {
                try {
                    while (ypos < 800 - ballSize) {
                        ypos += 5;
                        repaint();
                        Thread.sleep(50);
                    }
                } catch (InterruptedException ex) {
                }
            });
        } else if (e.getSource() == clear) {
            xpos = 375;
            ypos = 375;
            repaint();
            return; // clear 버튼에서는 새로운 스레드 실행 안 함
        }

        // 새로운 스레드 시작
        moveThread.start();
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.RED);
        g.fillOval(xpos, ypos, ballSize, ballSize);
    }

    public static void main(String[] args) {
        Frame frame = new BounceBall();
    }
}
