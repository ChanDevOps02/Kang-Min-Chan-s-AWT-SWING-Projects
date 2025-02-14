package sec02.exam06;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

public class SortingGraphic extends Frame implements ActionListener {
    Button button1, button2, button3, button4;
    Label label, timeLabel;
    boolean isSelect = false;
    int[] data;
    String selectSource = "";
    long sortTime = 0;

    public SortingGraphic(){
        setTitle("Sorting Algorithm Visualization");
        setBackground(Color.YELLOW);
        setSize(1000, 800);

        Panel panel = new Panel();
        panel.setLayout(new FlowLayout());
        panel.setBackground(Color.GRAY);

        label = new Label("Select Sorting Algorithm : ", Label.LEFT);
        timeLabel = new Label("Sorting Time: 0 ms", Label.RIGHT);

        button1 = new Button("Bubble Sort");
        button2 = new Button("Quick Sort");
        button3 = new Button("Execute");
        button4 = new Button("Clear");
        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);
        button4.addActionListener(this);

        panel.add(label);
        panel.add(button1);
        panel.add(button2);
        panel.add(button3);
        panel.add(button4);
        panel.add(timeLabel);
        add(panel, BorderLayout.NORTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        generateRandomData();
        this.setVisible(true);
    }

    public void generateRandomData(){
        Random random = new Random();
        data = new int[100];
        for(int i = 0; i < data.length; i++){
            data[i] = random.nextInt(600) + 100;
        }
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource() == button1){
            selectSource = "Bubble";
            label.setText("Bubble Sort selected");
        }
        else if(e.getSource() == button2){
            selectSource = "Quick";
            label.setText("Quick Sort selected");
        }
        else if(e.getSource() == button3){
            if(selectSource != null && !selectSource.isEmpty()){
                isSelect = true;
                new Thread(() -> {
                    long startTime = System.currentTimeMillis();
                    if(selectSource.equals("Bubble")){
                        bubbleSort();
                    }
                    else if(selectSource.equals("Quick")){
                        quickSort(0, data.length - 1);
                    }
                    sortTime = System.currentTimeMillis() - startTime;
                    SwingUtilities.invokeLater(() -> timeLabel.setText("Sorting Time: " + sortTime + " ms"));
                    isSelect = false;
                }).start();
            }
            else{
                label.setText("Select a sorting algorithm first!");
            }
        }
        else if(e.getSource() == button4){
            generateRandomData();
            selectSource = "";
            label.setText("Select Sorting Algorithm : ");
            timeLabel.setText("Sorting Time: 0 ms");
            repaint();
        }
    }

    private void bubbleSort() {
        int n = data.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (data[j] > data[j + 1]) {
                    int temp = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = temp;
                    SwingUtilities.invokeLater(this::repaint);
                    sleep(10);
                }
            }
        }
    }

    private void quickSort(int low, int high) {
        if (low < high) {
            int pi = partition(low, high);
            SwingUtilities.invokeLater(this::repaint);
            sleep(10);

            quickSort(low, pi - 1);
            quickSort(pi + 1, high);
        }
    }

    private int partition(int low, int high) {
        int pivot = data[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (data[j] < pivot) {
                i++;
                int temp = data[i];
                data[i] = data[j];
                data[j] = temp;
                SwingUtilities.invokeLater(this::repaint);
                sleep(10);
            }
        }

        int temp = data[i + 1];
        data[i + 1] = data[high];
        data[high] = temp;
        SwingUtilities.invokeLater(this::repaint);
        sleep(10);

        return i + 1;
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void paint(Graphics g){
        super.paint(g);
        g.setColor(Color.RED);
        int spacing = (getWidth() - 100) / data.length;
        for(int i = 0; i < data.length; i++){
            int x = 50 + i * spacing;
            int y = data[i];
            g.fillOval(x, y, 8, 8);
        }
    }

    public static void main(String[] args){
        new SortingGraphic();
    }
}
