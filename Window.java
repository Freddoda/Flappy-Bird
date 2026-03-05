package Flappy_Bird_Clone;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JPanel;

public class Window extends JPanel implements Runnable{

    Thread gamethread;
    Keys K = new Keys();

    Bird b = new Bird(25);
    ArrayList<Pipe> pipes = new ArrayList<>(Arrays.asList(new Pipe(1), new Pipe(2)));
    Ground grou = new Ground();
    Score score = new Score();
    Stars stars = new Stars(50);

    boolean playing = false;
    boolean playtrig = false;
    boolean hitboxModeling = false;

    public Window(){
        this.setPreferredSize(new Dimension(640,940));
        this.setBackground(new Color(0,0,0));
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.addKeyListener(K);
    }

    public void startthread(){
        gamethread = new Thread(this);
        gamethread.start();
    }

    double drawinter = 1000000000/60;

    public void run(){

        while (gamethread != null){
            double nextdraw = (System.nanoTime()+drawinter);

            update();
            repaint();
            
            double remtime = (nextdraw - System.nanoTime())/1000000;
            try {
                Thread.sleep((long) remtime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (K.keys.contains(KeyEvent.VK_Q)){
                System.exit(0);
            }
        }
    }

    public void update(){
        if (playing){

            score.scoring(pipes);
            score.highch();
            b.move(K.keys);
            grou.move(2.8f);
            for (Pipe pipe : pipes){
                pipe.move(2.8f);
            }
            if (b.collide(pipes)){
                playing=false;
                b.reset();
                grou.reset();
                score.reset();
                for (Pipe i : pipes){
                    i.reset();
                }
                stars.reset();
            }

        } else if(K.keys.contains(KeyEvent.VK_SPACE) || K.keys.contains(KeyEvent.VK_W)){
            if (playtrig){
                playing=true;
                playtrig=false;
            }
        } else{
            playtrig=true;
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D G= (Graphics2D) g;

        if (!hitboxModeling){
            stars.draw(G);
            b.draw(G);
            for (Pipe pipe : pipes){
                pipe.draw(G);
            }
            grou.draw(G);
            score.draw(G);
        } else {
            G.setColor(Color.GREEN);
            G.fillRect(b.x,(int)b.y,1,1);
            for (int x=1 ; x<=640 ; x++){
                for (int y=1 ; y<=940 ; y++){
                    if(Bird.collide(pipes,x,y,b.radius)){
                        G.setColor(Color.RED);
                        G.fillRect(x,y,1,1);
                    }
                }
            }
        }

        G.dispose();
    }
}
