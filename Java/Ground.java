package Flappy_Bird_Clone;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;
import java.util.ArrayList;

public class Ground {
    Random rand = new Random();
    int height = 90;
    int grass=20;
    int grleak=40;
    float pos=0;
    ArrayList<Integer> grlepos = new ArrayList<>();
    int grleks=5;

    public Ground(){
        for (int n=0 ; n<grleks ; n++){
            grlepos.add(rand.nextInt(1480));
        }
    }
    
    public void draw(Graphics2D g){
        g.setColor(new Color(40,0,0));
        g.fillRect((int)pos,940-height,1500,height);
        g.fillRect((int)pos+1500,940-height,1500,height);
        g.setColor(new Color(0,140,0));
        g.fillRect((int)pos,940-height,1500,grass);
        g.fillRect((int)pos+1500,940-height,1500,grass);
        for ( Integer i : grlepos){
            g.fillRect(i+(int)pos,940-height,grass,grleak);
            g.fillRect(i+(int)pos+1500,940-height,grass,grleak);
        }
    }

    public void move(float increment){
        pos-=increment;
        if (pos<=-1500){
            pos+=1500;
        }
    }

    public void reset(){
        pos=0;
        grlepos.clear();
        for (int n=0 ; n<grleks ; n++){
            grlepos.add(rand.nextInt(1480));
        }
    }
}
