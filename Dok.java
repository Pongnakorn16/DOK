import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine.Info;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Dok extends JFrame {
    GamePanel panel = new GamePanel();

    public Dok() {
        setSize(700, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        add(panel);
    }

    public static void main(String[] args) {
        Dok win = new Dok();
        win.setVisible(true);
    }

}

class GamePanel extends JPanel implements MouseMotionListener, MouseListener {
    Image bg = Toolkit.getDefaultToolkit()
            .createImage(System.getProperty("user.dir")
                    + File.separator + "tree.jpg");

    Image apple = Toolkit.getDefaultToolkit()
            .createImage(System.getProperty("user.dir")
                    + File.separator + "apple2.png");

    Image basket = Toolkit.getDefaultToolkit()
            .createImage(System.getProperty("user.dir")
                    + File.separator + "basket.png");
    int num = 5;
    int x[] = new int[num];
    int y[] = new int[num];
    int sx, sy, px, py;
    int score = 0;

    public GamePanel() {
        setSize(700, 650);
        setLayout(null);
        pointXY();
        addMouseMotionListener(this);
        addMouseListener(this);
        BackgroundSound soundB = new BackgroundSound();
        soundB.start();
        // Timer timer=new Timer();
        // timer.scheduleAtFixedRate(new GameTimerTask(this), 0, 1000);
        GameThread t = new GameThread(this);
        t.start();
    }

    public void pointXY() {
        for (int i = 0; i < num; i++) {
            x[i] = (i * 150);
            y[i] = 0;
        }
    }

    public void paintComponent(Graphics g) {
        g.drawImage(bg, 0, 0, 700, 650, 0, 350, 980, 980, this);
        g.drawImage(basket, sx, sy, this);

        for (int i = 0; i < num; i++) {
            g.drawImage(apple, x[i], y[i], this);
        }
        Font font = new Font("Tahoma", Font.BOLD, 30);
        g.setFont(font);
        g.setColor(Color.red);
        g.drawString("Score : ", 20, 50);
        g.drawString(Integer.toString(score), 130, 50);
    }

    class GameTimerTask extends TimerTask {
        GamePanel panel;

        public GameTimerTask(GamePanel panel) {
            this.panel = panel;
        }

        @Override
        public void run() {
            for (int i = 0; i < panel.num; i++) {
                panel.y[i] = panel.y[i] + new Random().nextInt(100);
                int time = new Random().nextInt(10);
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    // TODO: handle exception
                }
                if (panel.y[i] > 600)
                    panel.y[i] = 0;
            }
            panel.repaint();
        }

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        panelMouseMove(e);

    }

    private void panelMouseMove(MouseEvent e) {
        sx = e.getX() - 100;
        sy = e.getY() - 50;
        repaint();

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        px = e.getX();
        py = e.getY();

        for (int i = 0; i < num; i++) {
            if (Math.abs(px - x[i]) < 80 && (Math.abs(py - y[i]) < 50)) {
                score++;
                y[i] = 0;
                SoundThread soundT = new SoundThread();
                soundT.start();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }
}

class GameThread extends Thread {
    GamePanel panel;

    public GameThread(GamePanel panel) {
        this.panel = panel;
    }

    @Override
    public void run() {
        while (true) {
            for (int i = 0; i < panel.num; i++) {
                panel.y[i] = panel.y[i] + new Random().nextInt(100);
                int time = new Random().nextInt(300);
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    // TODO: handle exception
                }
                if (panel.y[i] > 600)
                    panel.y[i] = 0;
            }
            panel.repaint();
        }
    }

}

class SoundThread extends Thread {
    public void run() {
        try {
            File wave = new File(System.getProperty("user.dir")
                    + File.separator + "coin.wav");
            AudioInputStream stream = AudioSystem.getAudioInputStream(wave);
            AudioFormat format = stream.getFormat();
            Info info = new Info(Clip.class, format);
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(stream);
            clip.start();
        } catch (Exception e) {

        }
    }
}

class BackgroundSound extends Thread {
    public void run() {
        try {
            File wave = new File(System.getProperty("user.dir")
                    + File.separator + "gaming.wav");
            AudioInputStream stream = AudioSystem.getAudioInputStream(wave);
            AudioFormat format = stream.getFormat();
            Info info = new Info(Clip.class, format);
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(stream);
            clip.start();
            clip.loop(MAX_PRIORITY);

        } catch (Exception e) {

        }
    }
}
