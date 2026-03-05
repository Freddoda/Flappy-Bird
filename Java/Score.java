package Flappy_Bird_Clone;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class Score {
    int score = 0;
    int high;
    File highfile = new File("Flappy_Bird_Clone/Score.txt");
    FontMetrics fm;
    Font font = new Font("SansSerif",Font.PLAIN,60);

    public Score(){
        try (Scanner reader = new Scanner(highfile)){
            high = reader.nextInt();
        } catch(FileNotFoundException e){
            System.out.println("no such file");
        }
    }

    public void highch(){
        if (score>high){
            high=score;
            try(FileWriter writer = new FileWriter(highfile)){
                writer.write(String.valueOf(score));
            } catch(IOException e){
                System.out.println("didn't write");
            }
        }
    }

    public void draw(Graphics2D g){
        if (fm==null){
            fm = g.getFontMetrics(font);
        }
        g.setFont(font);
        g.setColor(Color.RED);
        g.drawString(String.valueOf(score),(int)(320-fm.stringWidth(String.valueOf(score))/2),fm.getHeight());
        g.drawString(String.valueOf(high),(int)(320-fm.stringWidth(String.valueOf(high))/2),2*fm.getHeight()); 
    }

    public void scoring(ArrayList<Pipe> pipes){
        for (Pipe pipe : pipes){
            if (pipe.x<100 && pipe.scored == false){
                pipe.scored=true;
                score++;
            }
        }
    }

    public void reset(){
        score=0;
    }
}
