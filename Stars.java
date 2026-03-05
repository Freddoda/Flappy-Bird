package Flappy_Bird_Clone;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

public class Stars {
    int starsnum;
    ArrayList<int[]> coords= new ArrayList<>();
    Random rand = new Random();

    public Stars(int Starsnum){
        starsnum = Starsnum;
        for (int i=0 ; i<starsnum ; i++){
            coords.add(new int[]{rand.nextInt(0,700),rand.nextInt(0,850)});
        }
    }

    public void reset(){
        coords.clear();
        for (int i=0 ; i<starsnum ; i++){
            coords.add(new int[]{rand.nextInt(0,700),rand.nextInt(0,850)});
        }
    }

    public void draw(Graphics2D g){
        g.setColor(Color.WHITE);
        for (int i=0 ; i<starsnum ; i++){
            g.fillOval(coords.get(i)[0],coords.get(i)[1],3,3);
        }
    }
}
