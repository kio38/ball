package ball;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainForm {
    private class ball implements Runnable {
        private JTextArea logArea;
        private Graphics g;
        private float radius,x,y,dx,dy,time,dt,k;
        private boolean showgraph;
        public ball(JTextArea logArea, Graphics g, float radius, float x, float y, float dx, float dy, float time, float dt, float k, boolean showgraph) {
            this.logArea = logArea;
            this.showgraph = showgraph;
            this.g = g;
            this.radius = radius;
            this.x = x;
            this.y = y;
            this.dx = dx;
            this.dy = dy;
            this.time = time;
            this.dt = dt;
            this.k = k;
        }

        public void start() {
            Thread thread = new Thread(this);
            thread.start();
        }


        @Override
        public void run() {
            logArea.setText("");
            float timeTillNextLog;
            float leftBorder=timeTillNextLog = 0;
            float rightBorder = 10;
            float dtActual = dt;
            float multiplier = dtActual/1000;
            int counter = 1;
            int pixelsPerMeter = 30;
            java.util.List<HeightRecord> heights = new ArrayList<>();

            for (int i = 0; i <= time; i+=dtActual) {

                g.setColor(Color.ORANGE);
                g.fillRect(420, 10, 300, 300);
                g.setColor(Color.BLUE);
                g.fillArc((int) (420 + (x-radius)*pixelsPerMeter), (int) (310 - (y + radius) * pixelsPerMeter), (int) (radius*2*pixelsPerMeter), (int) (radius*2*pixelsPerMeter), 0, 360);

                if(timeTillNextLog < 1) {
                    if(showgraph) {
                        g.setColor(Color.WHITE);
                        g.fillRect(420, 320, 650, 350);
                        g.setColor(Color.BLACK);
                        g.drawLine(460, 330, 460, 650);
                        g.drawLine(460, 650, 1060, 650);
                        g.drawLine(455, 330, 465, 330);
                        g.drawString("10", 440, 340);
                        g.drawString("0", 450, 650);
                        heights.add(new HeightRecord(y - radius, ((float) (counter - 1)) * dt / 1000));
                        int interval = 600 / heights.size();
                        for (int j = 1; j < heights.size(); j++) {
                            g.drawLine(j * interval + 460, 650 - (int) (heights.get(j).height * 32F), (j - 1) * interval + 460, 650 - (int) (heights.get(j - 1).height * 32F));
                        }
                    }
                    timeTillNextLog = dt;
                    logArea.append(String.format("%d: height = %.2f , speed = %.2f \n",counter,y-radius,Math.sqrt(dx*dx + dy*dy)));
                    counter++;
                }
                timeTillNextLog -= dtActual;
                dy -= 9.8*multiplier;
                x += dx*multiplier;
                y += dy*multiplier;

                if (y - radius < 0) {
                    y = radius;
                    dy *= -k;
                    if (Math.abs(dy) < 0.01) {
                        dy = 0;
                    }
                }

                if (x - radius < leftBorder) {
                    x = radius;
                    dx *= -k;
                }

                if (x + radius > rightBorder) {
                    x = rightBorder - radius;
                    dx *= -k;
                }

                try {
                    Thread.sleep((long)dtActual);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        class HeightRecord {
            public float height,time;
            public HeightRecord(float height, float time) {
                this.height = height;
                this.time = time;
            }
        }
    }
    private static JFrame frame;
    private JPanel panel1;
    private JTextArea logArea;
    private JButton simulateButton;


    public MainForm() {
        simulateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ball myball = new ball(logArea, panel1.getGraphics(),0.5f,
                        2f, 9f, -2f, 0,
                        5000, 30, 0.7f, false);
                myball.start();

            }
        });
    }



    public static void main(String[] args) {
        frame = new JFrame("Sumulation");
        frame.setContentPane(new MainForm().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }
}

