package Flappy_Bird_Clone;

import java.util.Random;
import java.awt.Graphics2D;
import java.awt.Color;

public class Pipe {
    int num;
    int y;
    float x;
    Random rand = new Random();
    int width = 110;
    int inter=100;
    boolean scored=false;

    public Pipe(int Num){
        this.num=Num;
        this.x=520*num;
        this.y=rand.nextInt(125,725);
    }

    public void draw(Graphics2D g){
        g.setColor(new Color(0,220,0));
        g.fillRect((int)x-width/2,0,width,y-inter);
        g.fillRect((int)x-width/2,y+inter,width,1000);
    }

    public void move(float increment){
        x-=increment;
        if (x<-55){
            x=985;
            y=rand.nextInt(125,725);
            scored=false;
        }
    }

    public void reset(){
        this.x=520*num;
        this.y=rand.nextInt(125,725);
        scored=false;
    }
}
